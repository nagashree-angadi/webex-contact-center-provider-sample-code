package com.cisco.wccai.grpc.server;

import com.cisco.wcc.ccai.media.v1.Conversationaudioforking;
import com.google.protobuf.util.Timestamps;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConversationAudioForkService {

    public void processConversationAudioForkRequest(Conversationaudioforking.ConversationAudioForkingRequest conversationAudioForkingRequest, StreamObserver<Conversationaudioforking.ConversationAudioForkingResponse> conversationAudioForkingResponse) {
        String conversationId = conversationAudioForkingRequest.getConversationId();
        processAudioInput(conversationAudioForkingRequest, conversationAudioForkingResponse);
    }

    private void processAudioInput(Conversationaudioforking.ConversationAudioForkingRequest conversationAudioForkingRequest, StreamObserver<Conversationaudioforking.ConversationAudioForkingResponse> conversationAudioForkingResponse) {
        log.info("Validating forked audio input for conversationId: {}, latency: {} seconds, sampling rate: {}, encoding: {}",
                conversationAudioForkingRequest.getConversationId(),
                (Timestamps.toMillis(conversationAudioForkingRequest.getAudio().getAudioTimestamp()) - System.currentTimeMillis()) / 1000,
                conversationAudioForkingRequest.getAudio().getSampleRateHertz(),
                conversationAudioForkingRequest.getAudio().getEncoding());
    }
}

