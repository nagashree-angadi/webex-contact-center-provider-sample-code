package com.cisco.wccai.grpc.server;

import com.cisco.wcc.ccai.media.v1.ByovaCommon;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
public class ListVoiceVirtualAgentsProcessor {

    /**
     * Prepares the response for listing voice virtual agents.
     * Retrieves the list of virtual agents based on the configured count
     * and constructs a ListVAResponse object containing the virtual agents.
     *
     * @return a ListVAResponse object containing the list of voice virtual agents
     */
    public static ByovaCommon.ListVAResponse prepareVirtualAgent() {
        log.info("Request received for listing voice virtual agents");
        var virtualAgents = getVirtualAgents( 3);
        return ByovaCommon.ListVAResponse.newBuilder().addAllVirtualAgents(virtualAgents).build();
    }

    private static ByovaCommon.VirtualAgentInfo getVirtualAgent(String virtualAgentId, String virtualAgentName) {
        return ByovaCommon.VirtualAgentInfo.newBuilder()
                .setVirtualAgentId(virtualAgentId)
                .setVirtualAgentName(virtualAgentName)
                .build();
    }

    private static List<ByovaCommon.VirtualAgentInfo> getVirtualAgents(int count) {
        List<ByovaCommon.VirtualAgentInfo> virtualAgents = new ArrayList<ByovaCommon.VirtualAgentInfo>();
        while (count > 0) {
            virtualAgents.add(getVirtualAgent(String.valueOf(count), "Virtual Agent " + count));
            count--;
        }
        Collections.reverse(virtualAgents);
        return virtualAgents;
    }

}

