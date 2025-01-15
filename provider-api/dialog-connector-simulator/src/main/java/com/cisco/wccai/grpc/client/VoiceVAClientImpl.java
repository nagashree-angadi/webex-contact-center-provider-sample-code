package com.cisco.wccai.grpc.client;

import com.cisco.wcc.ccai.media.v1.ByovaCommon;
import com.cisco.wcc.ccai.media.v1.VoiceVirtualAgentGrpc;
import com.cisco.wcc.ccai.media.v1.Voicevirtualagent;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;

@Slf4j
public class VoiceVAClientImpl {

    private VoiceVirtualAgentGrpc.VoiceVirtualAgentStub virtualAgentBlockingStub;

    public VoiceVAClientImpl(VoiceVirtualAgentGrpc.VoiceVirtualAgentStub VoiceVirtualAgentStub) {
        this.virtualAgentBlockingStub = VoiceVirtualAgentStub;
    }


    public void executeListVirtualAgent() {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        virtualAgentBlockingStub.listVirtualAgents(ByovaCommon.ListVARequest.newBuilder().build(), new StreamObserver<>() {
            @Override
            public void onNext(ByovaCommon.ListVAResponse listVAResponse) {
                countDownLatch.countDown();
                listVAResponse.getVirtualAgentsList().forEach(virtualAgentInfo -> log.info(virtualAgentInfo.getVirtualAgentName()));
            }

            @Override
            public void onError(Throwable throwable) {
                countDownLatch.countDown();
                log.error("Error occurred while listing virtual agents: {}", throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                countDownLatch.countDown();
                log.info("List virtual agents completed");
            }
        });
        waitForCountDownLatch(countDownLatch);
    }

    public void executeDtmfEvent() {
        var countDownLatch = new CountDownLatch(1);
        String conversationID = UUID.randomUUID().toString();
        var requestObserver = virtualAgentBlockingStub.processCallerInput(new StreamObserver<>() {
            @Override
            public void onNext(Voicevirtualagent.VoiceVAResponse voiceVAResponse) {
                log.info("Received response from DTMF event: {}", voiceVAResponse.toString());
                countDownLatch.countDown();
            }

            @Override
            public void onError(Throwable throwable) {
                log.error("Error occurred while processing DTMF event: {}", throwable.getMessage());
                countDownLatch.countDown();
            }

            @Override
            public void onCompleted() {
                log.info("DTMF event processing completed");
                countDownLatch.countDown();
            }
        });
        requestObserver.onNext(Voicevirtualagent.VoiceVARequest
                .newBuilder()
                .setConversationId(conversationID)
                .setDtmfInput(ByovaCommon.DTMFInputs.newBuilder().addDtmfEvents(ByovaCommon.DTMFDigits.DTMF_DIGIT_ONE).build()).build());
        waitForCountDownLatch(countDownLatch);
        requestObserver.onCompleted();
    }

    private void waitForCountDownLatch(CountDownLatch countDownLatch) {
        try {
            countDownLatch.await();
        } catch (Exception e) {
            log.error("Error while waiting for countdown", e);
        }
    }


}

