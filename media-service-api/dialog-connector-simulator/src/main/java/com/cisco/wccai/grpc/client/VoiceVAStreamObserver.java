package com.cisco.wccai.grpc.client;

import com.cisco.wcc.ccai.media.v1.Voicevirtualagent;
import com.cisco.wccai.grpc.utils.Utils;
import io.grpc.stub.StreamObserver;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;

@Slf4j
public class VoiceVAStreamObserver implements StreamObserver<Voicevirtualagent.VoiceVAResponse> {

    final CountDownLatch countDownLatch;

    @Getter
    private boolean isAnyError = false;

    @Setter
    private boolean singleResponse = false;

    private boolean shouldSaveInputAudio = true;

    @Setter
    private String audioFileName;

    VoiceVAStreamObserver(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void onNext(Voicevirtualagent.VoiceVAResponse voiceVirtualAgentResponse) {
        log.info("Received voice virtual agent response: {}", voiceVirtualAgentResponse);
        if (!audioFileName.isEmpty() && shouldSaveInputAudio && !voiceVirtualAgentResponse.getPromptsList().isEmpty()) {
            Utils.write(voiceVirtualAgentResponse.getPromptsList().get(0).getAudioContent(), audioFileName + ".wav");
        }
        if (singleResponse && countDownLatch != null) {
            countDownLatch.countDown();
        }
    }

    @Override
    public void onError(Throwable throwable) {
        isAnyError = true;
        if (countDownLatch != null) countDownLatch.countDown();
        log.error("Error occurred while processing voice stream: {} Stacktrace: {}", throwable.getMessage(), throwable.getStackTrace());
    }

    @Override
    public void onCompleted() {
        if (countDownLatch != null) countDownLatch.countDown();
        log.info("Voice stream processing completed");
    }
}