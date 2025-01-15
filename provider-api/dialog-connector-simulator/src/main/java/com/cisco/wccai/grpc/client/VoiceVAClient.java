package com.cisco.wccai.grpc.client;
import com.cisco.wcc.ccai.media.v1.VoiceVirtualAgentGrpc.VoiceVirtualAgentStub;
import com.cisco.wccai.grpc.client.VoiceVAClientImpl;
import com.cisco.wccai.grpc.utils.LoadProperties;
import io.grpc.ManagedChannel;
import io.grpc.netty.NegotiationType;
import io.grpc.netty.NettyChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.MDC;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

//import static com.cisco.wccai.grpc.client.CCAIClient.API_URL;
//import static com.cisco.wccai.grpc.client.CCAIClient.PORT;

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

    int portUsed =  Integer.parseInt(property.getProperty(PORT));

    VoiceVAClient() {
        this.channel = NettyChannelBuilder.forAddress(apiUrl, portUsed)
                .negotiationType(NegotiationType.PLAINTEXT)
                .idleTimeout(5, TimeUnit.SECONDS)
                .build();
    }

    public void executeListVirtualAgent() {
        String convId = RandomStringUtils.randomAlphanumeric(10);
        MDC.putCloseable("conversation_id",convId);
        VoiceVirtualAgentStub stub = com.cisco.wcc.ccai.media.v1.VoiceVirtualAgentGrpc
                .newStub(channel);

        VoiceVAClientImpl client = new VoiceVAClientImpl(stub);

        client.executeListVirtualAgent();

        client.executeDtmfEvent();
    }

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        VoiceVAClient voiceVAClient = new VoiceVAClient();
        voiceVAClient.executeListVirtualAgent();
        long endTime = System.currentTimeMillis();
        log.info("Total time taken by VA call: {} ms" , (endTime-startTime));
    }
}
