package com.cisco.wccai.grpc.server;

import com.cisco.wcc.ccai.media.v1.ByovaCommon;
import com.cisco.wcc.ccai.media.v1.Voicevirtualagent;
import com.cisco.wccai.grpc.utils.Utils;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

import static com.cisco.wccai.grpc.server.VirtualAgentUtils.getCallStartResponse;
import static com.cisco.wccai.grpc.server.VirtualAgentUtils.getFinalVAResponse;

@Slf4j
public class VoiceVAContentObserver implements StreamObserver<Voicevirtualagent.VoiceVARequest> {

    private final StreamObserver<Voicevirtualagent.VoiceVAResponse> responseObserver;
    private Voicevirtualagent.VoiceVARequest voiceVARequest;
    private VoiceVirtualAgentService voiceVirtualAgentService = null;
    private String conversationId;

    public VoiceVAContentObserver(StreamObserver<Voicevirtualagent.VoiceVAResponse> responseObserver) {
        this.responseObserver = responseObserver;
    }

    @Override
    public void onNext(Voicevirtualagent.VoiceVARequest voiceVARequest) {
        conversationId = voiceVARequest.getConversationId();
        if (voiceVirtualAgentService == null) {
            this.voiceVARequest = voiceVARequest;
            voiceVirtualAgentService = new VoiceVirtualAgentService();
        }
        voiceVirtualAgentService.processVoiceVirtualAgentRequest(voiceVARequest, responseObserver);
    }

    @Override
    public void onError(Throwable throwable) {
        log.error("Error occurred for conversationId: {} with cause: {} and , message: {} ",
                voiceVARequest.getConversationId(), throwable.getCause(), throwable.getMessage());
        responseObserver.onError(throwable);
    }

    @Override
    public void onCompleted() {
        log.info("On onCompleted for conversationId: {} ", conversationId);

        if (voiceVirtualAgentService.isEndOfInput()) {
            log.info("writing response from onCompleted to client for IS_END_OF_INPUT event, conversationId : {}", conversationId);
            responseObserver.onNext(getFinalVAResponse(false));
        } else if (voiceVARequest.getEventInput().getEventType() == ByovaCommon.EventInput.EventType.SESSION_END) {
            log.info("writing empty response from onCompleted to client for SESSION_END event, conversationId : {}", conversationId);
            responseObserver.onNext(Voicevirtualagent.VoiceVAResponse.newBuilder().build());
        } else if (voiceVARequest.getEventInput().getEventType() == ByovaCommon.EventInput.EventType.SESSION_START) {
            log.info("writing response from onCompleted to client for SESSION_START event, conversationId : {}", conversationId);
            responseObserver.onNext(getCallStartResponse());
        } else if (voiceVirtualAgentService.isDtmfReceived()) {
            log.info("writing response from onCompleted to client, conversationId : {}", conversationId);
            responseObserver.onNext(getFinalVAResponse(true));
        }

        responseObserver.onCompleted();
    }
}
