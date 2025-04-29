package com.cisco.wccai.grpc.server;

import com.cisco.wcc.ccai.media.v1.ConversationAudioGrpc;
import com.cisco.wcc.ccai.media.v1.Conversationaudioforking;
import io.grpc.stub.StreamObserver;

public class ConversationAudioForkImpl extends ConversationAudioGrpc.ConversationAudioImplBase {

    @Override
    public StreamObserver<Conversationaudioforking.ConversationAudioForkingRequest> streamConversationAudio(StreamObserver<Conversationaudioforking.ConversationAudioForkingResponse> responseObserver) {
        return new ConversationAudioForkContentObserver(responseObserver);
    }
}