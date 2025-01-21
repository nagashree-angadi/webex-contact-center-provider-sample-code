package com.cisco.wccai.grpc.server;

import com.cisco.wcc.ccai.media.v1.ByovaCommon;
import com.cisco.wccai.grpc.model.State;
import io.grpc.stub.StreamObserver;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.LinkedHashMap;

import static com.cisco.wcc.ccai.media.v1.Voicevirtualagent.VoiceVARequest;
import static com.cisco.wcc.ccai.media.v1.Voicevirtualagent.VoiceVAResponse;
import static com.cisco.wccai.grpc.server.VirtualAgentUtils.*;


@Slf4j
public class VoiceVirtualAgentService {

    private static final int CHUNK_SIZE =  4080;
    private static final int STREAM_INTERVAL_MS = 500;
    private boolean isFirstTimeDTMF = true;
    @Getter
    private boolean isDtmfReceived = false;
    private boolean isTermCharacter = true;
    private boolean isStartOfInput = false;
    private int totalChunkReceivedSoFarInBytesVB = 0;
    private int totalSizeVB = 0;
    private int totalChunkReceivedSoFarInBytesGateway = 0;
    private int totalSizeGateway = 0;
    @Getter
    private boolean isEndOfInput = false;
    private HashMap<String, StringBuilder> dtmfInputBuffer = new LinkedHashMap<>();

    public void processVoiceVirtualAgentRequest(VoiceVARequest voiceVARequest, StreamObserver<VoiceVAResponse> voiceVAResponse) {
        String conversationId = voiceVARequest.getConversationId();
        switch (voiceVARequest.getVoiceVaInputTypeCase()) {
            case EVENT_INPUT -> {
                log.info("Received voice virtual event input");
                processEventInput(voiceVARequest.getEventInput().getEventType(), voiceVARequest.getConversationId(), voiceVAResponse);
            }
            case DTMF_INPUT -> {
                log.info("Received voice virtual dtmf input");
                processDtmfInput(conversationId, voiceVARequest, voiceVAResponse);
            }
            case AUDIO_INPUT -> {
                log.info("Received voice virtual audio input");
                processAudioInput(voiceVARequest, voiceVAResponse);
            }
            default -> {
                log.error("Received Invalid voice virtual agent input type");
                voiceVAResponse.onError(new RuntimeException("Invalid voice virtual agent input type"));
            }
        }
    }

    private void processDtmfInput(String conversationId,VoiceVARequest voiceVARequest, StreamObserver<VoiceVAResponse> voiceVAResponse) {
        var state = getDTMFState(voiceVARequest.getDtmfInput().getDtmfEventsList().get(0).getNumber(), conversationId);
        getDTMFResponse(state).ifPresent(voiceVAResponse::onNext);
    }

    private State getDTMFState(int dtmfNo, String conversationId) {
        State resultState = null;
        if (isFirstTimeDTMF) {
            isDtmfReceived = true;
            isFirstTimeDTMF = false;
            log.info("Received dtmf: {} as first character for conversationId : {} , sending START_OF_INPUT event ", dtmfNo, conversationId);
            resultState = State.START_OF_INPUT;
        } else if ((ByovaCommon.DTMFDigits.DTMF_DIGIT_POUND.getNumber() == dtmfNo) && isTermCharacter) {
            log.info("Received dtmf: {} as term character for conversationId : {} , sending END_OF_INPUT event", dtmfNo, conversationId);
            isTermCharacter = false;
            resultState = State.END_OF_INPUT;
        }
        return resultState;
    }

    public void getFinalDTMF(String conversationId, StreamObserver<VoiceVAResponse> voiceVAResponse) {
        log.info("Processing DTMF final response onComplete");

        State finalState = null;
        String dtmfInput = dtmfInputBuffer.get(conversationId).toString();
        if (ByovaCommon.DTMFDigits.DTMF_DIGIT_FIVE.name().equals(dtmfInput)) {
            finalState = State.AGENT_TRANSFER;
        } else if (ByovaCommon.DTMFDigits.DTMF_DIGIT_SIX.name().equals(dtmfInput)) {
            finalState = State.CALL_END;
        }

        if (null != finalState) {
            getFinalDTMFResponse(finalState).ifPresent(voiceVAResponse::onNext);
        }
        dtmfInputBuffer.remove(conversationId);
    }

    private void processAudioInput(VoiceVARequest voiceVARequest, StreamObserver<VoiceVAResponse> voiceVAResponse) {
        var callerAudioSize = voiceVARequest.getAudioInput().getCallerAudio().size();
        if (callerAudioSize < 15) {
            log.info("cleaning up initial audio packets");
            return;
        }
        if (callerAudioSize == CHUNK_SIZE && STREAM_INTERVAL_MS == 500) {
            vaResponse(voiceVARequest, voiceVAResponse);
        } else if (callerAudioSize == CHUNK_SIZE && STREAM_INTERVAL_MS == 20) {
            gatewayResponse(voiceVARequest, voiceVAResponse);
        } else {
            voiceVAResponse.onError(new IllegalArgumentException("Invalid audio size received from client for VA : " + callerAudioSize));
        }
    }

    private void processEventInput(ByovaCommon.EventInput.EventType eventInput, String conversationId, StreamObserver<VoiceVAResponse> voiceVAResponse) {
        switch (eventInput) {
            case SESSION_END -> {
                log.info("Received SESSION_END event for conversationId : {} ", conversationId);
                voiceVAResponse.onNext(getCallEndResponse());
            }
            case SESSION_START -> log.info("Received SESSION_START event for conversationId : {} ", conversationId);
            default -> {
                log.info("Unsupported event : {} type received for conversationId : {} ", eventInput, conversationId);
                voiceVAResponse.onNext(getVoiceVaResponseForOutPutEvent(ByovaCommon.OutputEvent.EventType.UNSPECIFIED_EVENT));
            }
        }
    }

    private void gatewayResponse(VoiceVARequest voiceVARequest, StreamObserver<VoiceVAResponse> voiceVAResponse) {
        if (!isStartOfInput) {
            isStartOfInput = true;
            log.info("writing start of input event to gateway client");
            voiceVAResponse.onNext(startOfInputResponse());
        }
        var audioSize = voiceVARequest.getAudioInput().getCallerAudio().size();
        totalChunkReceivedSoFarInBytesGateway += audioSize;
        totalSizeGateway += audioSize;
        while (totalChunkReceivedSoFarInBytesGateway >= 100028) { // 100028 = 100KB, which means for every 100KB of audio, we are sending partial recognition response
            log.info("writing partial recognition response to gateway client");
            voiceVAResponse.onNext(getPartialRecognitionResponse());
            totalChunkReceivedSoFarInBytesGateway = Math.max(0, totalChunkReceivedSoFarInBytesGateway - 100028);
        }
        // sending END_OF_INPUT when user takes pause ( Ex. END_OF_SINGLE_UTTERANCE for Google).
        // sending partial recognition responses
        if (!isEndOfInput && totalSizeGateway > 781 * 1024) { // 781 * 1024 = 800KB, which means we are expecting the audio of almost 800KB
            isEndOfInput = true;
            log.info("writing end of input event to gateway client");
            voiceVAResponse.onNext(getEndOfInputResponse());
        }
    }

    private void vaResponse(VoiceVARequest voiceVARequest, StreamObserver<VoiceVAResponse> voiceVAResponse) {
        // upon receiving the first interim response, sending START_OF_INPUT event, will be used by client for barge-in.
        if (!isStartOfInput) {
            isStartOfInput = true;
            log.info("writing start of input event to VA client");
            voiceVAResponse.onNext(startOfInputResponse());
        }
        // sending partial recognition responses
        var audioSize = voiceVARequest.getAudioInput().getCallerAudio().size();
        totalChunkReceivedSoFarInBytesVB += audioSize;
        totalSizeVB += audioSize;
        while (totalChunkReceivedSoFarInBytesVB >= CHUNK_SIZE) {
            voiceVAResponse.onNext(getPartialRecognitionResponse());
            totalChunkReceivedSoFarInBytesGateway = Math.max(0, totalChunkReceivedSoFarInBytesGateway - CHUNK_SIZE);
        }
        // sending END_OF_INPUT when user takes pause ( Ex. END_OF_SINGLE_UTTERANCE for Google).
        if (!isEndOfInput) {
            isEndOfInput = true;
            log.info("writing end of input event to VB client");
            voiceVAResponse.onNext(getEndOfInputResponse());
        }
    }
}