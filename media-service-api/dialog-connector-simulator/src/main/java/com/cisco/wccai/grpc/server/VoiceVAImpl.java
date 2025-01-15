package com.cisco.wccai.grpc.server;

import com.cisco.wcc.ccai.media.v1.ByovaCommon;
import com.cisco.wcc.ccai.media.v1.VoiceVirtualAgentGrpc;
import com.cisco.wcc.ccai.media.v1.Voicevirtualagent;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class VoiceVAImpl extends VoiceVirtualAgentGrpc.VoiceVirtualAgentImplBase {

    @Override
    public StreamObserver<Voicevirtualagent.VoiceVARequest> processCallerInput(StreamObserver<Voicevirtualagent.VoiceVAResponse> responseObserver) {
        log.info("Audio request received");
        return new VoiceVAContentObserver(responseObserver);
    }

    @Override
    public void listVirtualAgents(ByovaCommon.ListVARequest request, StreamObserver<ByovaCommon.ListVAResponse> responseObserver) {
        log.info("List virtual agent for audio service request received");
        var voiceVirtualAgents = ListVoiceVirtualAgentsProcessor.prepareVirtualAgent();
        responseObserver.onNext(voiceVirtualAgents);
        responseObserver.onCompleted();
    }

}