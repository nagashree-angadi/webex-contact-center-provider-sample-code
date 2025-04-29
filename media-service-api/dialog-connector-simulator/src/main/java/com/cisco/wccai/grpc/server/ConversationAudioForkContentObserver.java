package com.cisco.wccai.grpc.server;

import com.cisco.wcc.ccai.media.v1.Conversationaudioforking;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConversationAudioForkContentObserver implements StreamObserver<Conversationaudioforking.ConversationAudioForkingRequest> {
    private final StreamObserver<Conversationaudioforking.ConversationAudioForkingResponse> responseObserver;
    private final ConversationAudioForkService conversationAudioForkService;
    private String conversationId;

    public ConversationAudioForkContentObserver(StreamObserver<Conversationaudioforking.ConversationAudioForkingResponse> responseObserver) {
        this.responseObserver = responseObserver;
        this.conversationAudioForkService = new ConversationAudioForkService();
    }

    @Override
    public void onNext(Conversationaudioforking.ConversationAudioForkingRequest conversationAudioForkingRequest) {
        conversationId = conversationAudioForkingRequest.getConversationId();
        log.info("Global variable sample_rate_hertz is {} on forked audio for conversationId: {}", conversationAudioForkingRequest.getAudio().getSampleRateHertz(), conversationId);
        conversationAudioForkService.processConversationAudioForkRequest(conversationAudioForkingRequest, responseObserver);
    }

    @Override
    public void onError(Throwable throwable) {
        log.error("Error occurred on Audio Forking for conversationId: {} with cause: {} and , message: {} ",
                conversationId, throwable.getCause(), throwable.getMessage());
        responseObserver.onError(throwable);
    }

    @Override
    public void onCompleted() {
        log.info("On onCompleted on Audio Forking for conversationId: {}", conversationId);
        responseObserver.onNext(Conversationaudioforking.ConversationAudioForkingResponse.newBuilder().setStatusMessage("SUCCESS").build());
        responseObserver.onCompleted();
    }
}
