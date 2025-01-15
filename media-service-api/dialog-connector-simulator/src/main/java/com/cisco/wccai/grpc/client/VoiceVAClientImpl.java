package com.cisco.wccai.grpc.client;

import com.cisco.wcc.ccai.media.v1.ByovaCommon;
import com.cisco.wcc.ccai.media.v1.VoiceVirtualAgentGrpc.VoiceVirtualAgentStub;
import com.cisco.wcc.ccai.media.v1.Voicevirtualagent;
import com.cisco.wcc.ccai.media.v1.Voicevirtualagent.VoiceVARequest;
import com.cisco.wccai.grpc.utils.Utils;
import com.google.common.util.concurrent.Uninterruptibles;
import com.google.protobuf.ByteString;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Slf4j
public class VoiceVAClientImpl {

    private final VoiceVirtualAgentStub audioVirtualAgentBlockingStub;

    VoiceVAClientImpl(VoiceVirtualAgentStub audioVirtualAgentBlockingStub) {
        this.audioVirtualAgentBlockingStub = audioVirtualAgentBlockingStub;
    }

    public void executeListVirtualAgent() {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        audioVirtualAgentBlockingStub.listVirtualAgents(ByovaCommon.ListVARequest.newBuilder().build(), new StreamObserver<>() {
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
        String conversationID = RandomStringUtils.randomAlphanumeric(10);
        var streamObserver = new VoiceVAStreamObserver(countDownLatch);
        streamObserver.setSingleResponse(true);
        var requestObserver = audioVirtualAgentBlockingStub.processCallerInput(streamObserver);
        requestObserver.onNext(Voicevirtualagent.VoiceVARequest
                .newBuilder()
                .setConversationId(conversationID)
                .setDtmfInput(ByovaCommon.DTMFInputs.newBuilder().addDtmfEvents(ByovaCommon.DTMFDigits.DTMF_DIGIT_ONE).build()).build());
        waitForCountDownLatch(countDownLatch);
        requestObserver.onCompleted();
    }

    public void executeVoiceStream(String convoId) {
        sendCallStartEvent(convoId);
        CountDownLatch countDownLatch = new CountDownLatch(1);
        var streamObserver = new VoiceVAStreamObserver(countDownLatch);
        streamObserver.setAudioFileName("Good_bye");
        var requestObserver = audioVirtualAgentBlockingStub.processCallerInput(streamObserver);
        sendAudioConfig(requestObserver, convoId);
        sendAudioStream(requestObserver, convoId, streamObserver);
        requestObserver.onCompleted();
        waitForCountDownLatch(countDownLatch);
        log.info("Voice stream completed");
    }

    private void sendCallStartEvent(String convoId) {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        var streamObserver = new VoiceVAStreamObserver(countDownLatch);
        streamObserver.setAudioFileName("Welcome");
        var requestObserver = audioVirtualAgentBlockingStub.processCallerInput(streamObserver);
        requestObserver.onNext(Voicevirtualagent.VoiceVARequest
                .newBuilder()
                .setEventInput(ByovaCommon.EventInput
                        .newBuilder()
                        .setEventType(ByovaCommon.EventInput.EventType.SESSION_START).build())
                .setConversationId(convoId)
                .build());
        requestObserver.onCompleted();
        waitForCountDownLatch(countDownLatch);
    }

    private void sendAudioConfig(StreamObserver<VoiceVARequest> requestObserver, String convoId) {
        requestObserver.onNext(Voicevirtualagent.VoiceVARequest
                .newBuilder()
                .setAudioInput(Voicevirtualagent.VoiceInput
                        .newBuilder()
                        .setEncoding(Voicevirtualagent.VoiceInput.VoiceEncoding.MULAW_FORMAT)
                        .setSampleRateHertz(8000)
                        .build())
                .setConversationId(convoId)
                .build());
    }

    private void sendAudioStream(StreamObserver<VoiceVARequest> requestObserver, String convoId, VoiceVAStreamObserver streamObserver) {
        long startTime = System.currentTimeMillis();
        var audioToBookFlight = Utils.getAudioBytesForBookAFlight().toByteArray();
        var totalChunks = audioToBookFlight.length / 160;
        int currentChunk = 0;
        log.info("Sending audio stream to VA");
        while (currentChunk < totalChunks && !streamObserver.isAnyError()) {
            var chunk = Arrays.copyOfRange(audioToBookFlight, currentChunk * 160, (currentChunk + 1) * 160);
            requestObserver.onNext(VoiceVARequest
                    .newBuilder()
                    .setAudioInput(Voicevirtualagent.VoiceInput
                            .newBuilder()
                            .setCallerAudio(ByteString.copyFrom(chunk))
                            .build())
                    .setConversationId(convoId)
                    .build());
            Uninterruptibles.sleepUninterruptibly(20, TimeUnit.MILLISECONDS);
            currentChunk++;
        }
        if(streamObserver.isAnyError()) {
            log.error("Error occurred while streaming audio");
        }
        log.info("Total time taken to stream audio: {} ms", System.currentTimeMillis() - startTime);
    }

    private void waitForCountDownLatch(CountDownLatch countDownLatch) {
        try {
            countDownLatch.await();
        } catch (InterruptedException exception) {
            log.error("Error while waiting for countdown", exception);
        }
    }

}
