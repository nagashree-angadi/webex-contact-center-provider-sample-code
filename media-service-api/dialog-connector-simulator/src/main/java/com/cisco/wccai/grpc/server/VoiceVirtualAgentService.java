package com.cisco.wccai.grpc.server;

import com.cisco.wcc.ccai.media.v1.ByovaCommon;
import com.cisco.wcc.ccai.media.v1.Voicevirtualagent;
import com.cisco.wccai.grpc.model.State;
import io.grpc.stub.StreamObserver;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import static com.cisco.wcc.ccai.media.v1.Voicevirtualagent.VoiceVAInputMode.INPUT_VOICE_DTMF;
import static com.cisco.wcc.ccai.media.v1.Voicevirtualagent.VoiceVARequest;
import static com.cisco.wcc.ccai.media.v1.Voicevirtualagent.VoiceVAResponse;
import static com.cisco.wccai.grpc.server.VirtualAgentUtils.*;


/*
    This class is used to provide implementation for Virtual agent and processes the input events or lists the virtual agents.
*/
@Slf4j
public class VoiceVirtualAgentService {
    private boolean isStartOfInput = false;
    @Getter
    private boolean isEndOfInput = false;
    private boolean isWavAudio = true; // Set this flag to false to enable CHUNK streaming
    private long audioBufferSize = 0;

    public void processVoiceVirtualAgentRequest(VoiceVARequest voiceVARequest, StreamObserver<VoiceVAResponse> voiceVAResponse) {
        switch (voiceVARequest.getVoiceVaInputTypeCase()) {
            case EVENT_INPUT -> {
                log.info("Received voice virtual event input");
                processEventInput(voiceVARequest.getEventInput().getEventType(), voiceVARequest.getConversationId(), voiceVAResponse);
            }
            case DTMF_INPUT -> {
                log.info("Received dtmf input");
                processDtmfInput(voiceVARequest, voiceVAResponse);
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

    private void processDtmfInput(VoiceVARequest voiceVARequest, StreamObserver<VoiceVAResponse> responseStreamObserver) {
        String conversationId = voiceVARequest.getConversationId();
        log.info("Received DTMF input for conversationId: {}", conversationId);

        List<ByovaCommon.DTMFDigits> dtmfDigits = voiceVARequest.getDtmfInput().getDtmfEventsList();

        if (dtmfDigits.isEmpty()) {
            log.info("Received empty DTMF input for conversationId: {}", conversationId);
            responseStreamObserver.onNext(getNoInputResponse());
        }

        // send audio bytes and events based on the DTMF input
        mapDtmfInputToEvents(conversationId, dtmfDigits, responseStreamObserver);
    }

    public void mapDtmfInputToEvents(String conversationId, List<ByovaCommon.DTMFDigits> dtmfDigits, StreamObserver<Voicevirtualagent.VoiceVAResponse> responseStreamObserver) {
        for (ByovaCommon.DTMFDigits dtmfDigit : dtmfDigits) {
            log.info("Received DTMF digit: {} for conversationId: {}", dtmfDigit, conversationId);
            switch (dtmfDigit) {
                case DTMF_DIGIT_NINE -> responseStreamObserver.onNext(getAgentTransferResponse());
                case DTMF_DIGIT_STAR -> responseStreamObserver.onNext(getCallEndResponse());
                default ->
                        log.info("Received unknown DTMF digit: {} for conversationId: {}", dtmfDigit, conversationId);
            }
        }
    }

    private void processAudioInput(VoiceVARequest voiceVARequest, StreamObserver<VoiceVAResponse> voiceVAResponse) {
        var callerAudioSize = voiceVARequest.getAudioInput().getCallerAudio().size();
        if (callerAudioSize < 15) {
            log.info("cleaning up initial audio packets");
            return;
        }
        processCallerAudio(voiceVARequest, voiceVAResponse);
    }

    private void processEventInput(ByovaCommon.EventInput.EventType eventInput, String conversationId, StreamObserver<VoiceVAResponse> voiceVAResponse) {
        switch (eventInput) {
            case SESSION_END ->
                    log.info("Received SESSION_END event for conversationId : {} ", conversationId);
            case SESSION_START ->
                    log.info("Received SESSION_START event for conversationId : {} ", conversationId);
            default ->
                    log.info("Ignoring event : {} for conversationId : {} ", eventInput, conversationId);
        }
    }

    private void processCallerAudio(VoiceVARequest voiceVARequest, StreamObserver<VoiceVAResponse> voiceVAResponse) {
        // if 16000 bytes has silence then clear the buffer.
        if (audioBufferSize < 16000) {
            audioBufferSize += voiceVARequest.getAudioInput().getCallerAudio().size();
            log.info("Setting initial audio buffer size to: {} bytes for conversationId: {}", audioBufferSize, voiceVARequest.getConversationId());
            return;
        }
        // send START_OF_INPUT once speech is detected
        if (!isStartOfInput) {
            isStartOfInput = true;
            log.info("Sending START_OF_INPUT event.");
            voiceVAResponse.onNext(startOfInputResponse());
        }

        // Captures the caller audio here and process it.
        int audioSize = voiceVARequest.getAudioInput().getCallerAudio().size();
        log.info("Received audio input of size: {} bytes for conversationId: {}", audioSize, voiceVARequest.getConversationId());

        // send END_OF_INPUT once silence is detected
        if (isStartOfInput & !isEndOfInput) {
            isEndOfInput = true;
            log.info("Sending END_OF_INPUT event.");
            voiceVAResponse.onNext(getEndOfInputResponse());
        }
        // send the caller's query response
        if (isWavAudio) {
            log.info("Sending response with WAV audio content.");
            voiceVAResponse.onNext(getQueryFinalResponse());
        } else {
            log.info("Sending response with CHUNK audio content.");
            voiceVAResponse.onNext(getQueryChunkResponse());
            voiceVAResponse.onNext(getQueryChunkResponse());
            voiceVAResponse.onNext(getQueryFinalChunkResponse());
        }
        log.info("Buffered audio processed, clearing the audio buffer for conversationId: {}", voiceVARequest.getConversationId());
        audioBufferSize = 0;
    }
}