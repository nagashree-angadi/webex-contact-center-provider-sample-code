package com.cisco.wccai.grpc.client;

import com.cisco.wcc.ccai.media.v1.VoiceVirtualAgentGrpc;
import com.cisco.wccai.grpc.utils.LoadProperties;
import io.grpc.ManagedChannel;
import io.grpc.netty.NegotiationType;
import io.grpc.netty.NettyChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.MDC;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

@Slf4j
public class VoiceVAClient {

    ManagedChannel channel;
    private static final Properties property = LoadProperties.loadProperties();
    private static final String TOKEN = property.getProperty("TOKEN");
    public static final String API_URL = "API_URL";
    /**
     * The constant PORT.
     */
    public static final String PORT = "PORT";
    String apiUrl = property.getProperty(API_URL);

    int portUsed = Integer.parseInt(property.getProperty(PORT));

    VoiceVAClient() {
        this.channel = NettyChannelBuilder.forAddress(apiUrl, portUsed)
                .negotiationType(NegotiationType.PLAINTEXT)
                .idleTimeout(5, TimeUnit.SECONDS)
                .build();
    }

    public void executeListVirtualAgent(VoiceVAClientImpl client) {
        client.executeListVirtualAgent();
    }

    public VoiceVAClientImpl createClient(String convId) {
        MDC.putCloseable("conversation_id", convId);
        VoiceVirtualAgentGrpc.VoiceVirtualAgentStub stub = VoiceVirtualAgentGrpc
                .newStub(channel);
        return new VoiceVAClientImpl(stub);
    }

    public void executeDtmfEvent(VoiceVAClientImpl client) {
        client.executeDtmfEvent();
    }

    public void executeAudioStream(VoiceVAClientImpl client, String convoId) {
        client.executeVoiceStream(convoId);
    }

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        VoiceVAClient voiceVAClient = new VoiceVAClient();
        var convoId = RandomStringUtils.randomAlphanumeric(10);
        var client = voiceVAClient.createClient(convoId);
        voiceVAClient.executeListVirtualAgent(client);
        voiceVAClient.executeDtmfEvent(client);
        voiceVAClient.executeAudioStream(client, convoId);
        long endTime = System.currentTimeMillis();
        log.info("Total time taken by VA call: {} ms", (endTime - startTime));
    }


}