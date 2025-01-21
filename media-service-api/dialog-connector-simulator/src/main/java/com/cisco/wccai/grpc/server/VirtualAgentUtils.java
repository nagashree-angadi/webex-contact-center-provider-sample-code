package com.cisco.wccai.grpc.server;

import com.cisco.wcc.ccai.media.v1.ByovaCommon;
import com.cisco.wcc.ccai.media.v1.Voicevirtualagent;
import com.cisco.wccai.grpc.model.State;
import com.google.protobuf.ByteString;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Optional;

import static com.cisco.wcc.ccai.media.v1.ByovaCommon.OutputEvent.EventType.START_OF_INPUT;
import static com.cisco.wcc.ccai.media.v1.Voicevirtualagent.Prompt;
import static com.cisco.wcc.ccai.media.v1.Voicevirtualagent.VoiceVAResponse;

@Slf4j
public class VirtualAgentUtils {

    private static final String EN_US = "en-US";
    private static final String WELCOME_AUDIO = "welcome.wav";
    private static final String GOOD_BYE_AUDIO = "good_bye.wav";

    private static final String AGENT_TRANSFER_AUDIO = "agent_transfer.wav";

    private VirtualAgentUtils() {
    }

    public static Optional<VoiceVAResponse> getDTMFResponse(State state) {
        var outputEvent = switch (state) {
            case START_OF_INPUT -> getOutputEvent(START_OF_INPUT);
            case END_OF_INPUT -> getOutputEvent(ByovaCommon.OutputEvent.EventType.END_OF_INPUT);
            default -> null;
        };
        return outputEvent != null ? Optional.of(getVoiceVaResponseForOutPutEvent(outputEvent.getEventType())) : Optional.empty();
    }

    public static VoiceVAResponse getVoiceVaResponseForOutPutEvent(ByovaCommon.OutputEvent.EventType outputEvent) {
        return VoiceVAResponse
                .newBuilder()
                .addOutputEvents(getOutputEvent(outputEvent))
                .build();
    }

    public static VoiceVAResponse startOfInputResponse() {
        return getVoiceVaResponseForOutPutEvent(START_OF_INPUT);
    }

    public static VoiceVAResponse getPartialRecognitionResponse() {
        return VoiceVAResponse
                .newBuilder()
                .setSessionTranscript(
                        ByovaCommon.TextContent.newBuilder()
                                .setLanguageCode(EN_US)
                                .setText("I want to book ticket")
                                .build())
                .setIsPartial(true).build();
    }

    public static VoiceVAResponse getEndOfInputResponse() {
        return VoiceVAResponse.newBuilder().addOutputEvents(getOutputEvent(ByovaCommon.OutputEvent.EventType.END_OF_INPUT)).build();
    }

    public static Optional<VoiceVAResponse> getFinalDTMFResponse(State state) {
        VoiceVAResponse.Builder result = VoiceVAResponse.newBuilder();

        if (state == State.CALL_END) {
            log.info("Processing DTMF final response, sending SESSION_END event");
            ByovaCommon.OutputEvent outputEvent = getOutputEvent(ByovaCommon.OutputEvent.EventType.SESSION_END);
            result.addOutputEvents(outputEvent);
            result.addPrompts(createPrompt("Thank you for calling the Virtual agent simulator. Have a nice day!", GOOD_BYE_AUDIO));
            result.addPrompts(createPrompt("Thank you for calling the Virtual agent simulator. Have a nice day!", GOOD_BYE_AUDIO, false));
            result.setSessionTranscript(createTextContent("setting reply text from dialog simulator for final NLU Response"));
        } else if (state == State.AGENT_TRANSFER) {
            log.info("Processing DTMF final response, sending TRANSFER_TO_AGENT event");
            ByovaCommon.OutputEvent outputEvent = getOutputEvent(ByovaCommon.OutputEvent.EventType.TRANSFER_TO_AGENT);
            result.addOutputEvents(outputEvent);
            result.addPrompts(createPrompt("Transferring to the agent", AGENT_TRANSFER_AUDIO,false));
            result.addPrompts(createPrompt("Transferring to the agent", AGENT_TRANSFER_AUDIO, true));
            result.setSessionTranscript(createTextContent("setting reply text from dialog simulator for final NLU Response"));
        } else {
            result.addPrompts(createPrompt("Thank you for calling the Virtual agent simulator. Have a nice day!", GOOD_BYE_AUDIO));
            log.info("Processing DTMF final response, sending NO_INPUT event");
            ByovaCommon.OutputEvent outputEvent = getOutputEvent(ByovaCommon.OutputEvent.EventType.NO_INPUT);
            result.addOutputEvents(outputEvent);
            result.addPrompts(createPrompt("Thank you for calling the Virtual agent simulator. Have a nice day!", GOOD_BYE_AUDIO, false));
            result.setSessionTranscript(createTextContent("setting reply text from dialog simulator for final NLU Response"));
            result.setInputMode(Voicevirtualagent.VoiceVAInputMode.INPUT_EVENT_DTMF);
        }

        return Optional.of(result.build());
    }

    private static Prompt createPrompt(String text, String audioFileName, Boolean barginEnabled) {
        return Prompt.newBuilder()
                .setText(text)
                .setAudioContent(getAudioContent(audioFileName))
                .setIsBargeInEnabled(barginEnabled)
                .build();
    }

    public static VoiceVAResponse getCallEndResponse() {
        VoiceVAResponse result = VoiceVAResponse.newBuilder()
                .addPrompts(createPrompt("setting up prompt from dialog simulator for CALL_END event", WELCOME_AUDIO,false))
                .addOutputEvents(getOutputEvent(ByovaCommon.OutputEvent.EventType.END_OF_INPUT))
                .build();
        return result;
    }

    public static VoiceVAResponse getFinalVAResponse(boolean isDtmfEvent) {
        VoiceVAResponse result = VoiceVAResponse.newBuilder()
                .addPrompts(createPrompt("Thank you for calling the Virtual agent simulator. Have a nice day!", GOOD_BYE_AUDIO))
                .setSessionTranscript(createTextContent("setting reply text from dialog simulator for final NLU Response"))
                .setInputMode(isDtmfEvent ? Voicevirtualagent.VoiceVAInputMode.INPUT_VOICE_DTMF : Voicevirtualagent.VoiceVAInputMode.INPUT_VOICE)
                .build();
        return result;
    }

    public static VoiceVAResponse getCallStartResponse() {
        VoiceVAResponse result = VoiceVAResponse
                .newBuilder()
                .addPrompts(createPrompt("setting prompt from dialog simulator", WELCOME_AUDIO))
                .setSessionTranscript(createTextContent("Hi ! I'm your virtual agent for ticket booking from dialog simulator. How can I assist you today"))
                .setInputMode(Voicevirtualagent.VoiceVAInputMode.INPUT_VOICE_DTMF)
                .build();
        return result;
    }

    private static Prompt createPrompt(String text, String audioFileName) {
        return Prompt.newBuilder()
                .setText(text)
                .setAudioContent(getAudioContent(audioFileName))
                .setIsBargeInEnabled(false)
                .build();
    }

    private static ByovaCommon.TextContent createTextContent(String text) {
        return ByovaCommon.TextContent.newBuilder().setLanguageCode(EN_US).setText(text).build();
    }

    private static ByovaCommon.OutputEvent getOutputEvent(ByovaCommon.OutputEvent.EventType eventType) {
        return ByovaCommon.OutputEvent.newBuilder().setEventType(eventType).build();
    }

    private static ByteString getAudioContent(String fileName) {
        try {
            return switch (fileName) {
                case WELCOME_AUDIO, GOOD_BYE_AUDIO ->
                        ByteString.readFrom(VirtualAgentUtils.class.getClassLoader().getResourceAsStream("audio/" + fileName));
                default -> ByteString.EMPTY;
            };
        } catch (IOException e) {
            log.error("Error reading audio content from file: {}", fileName, e);
        }
        return ByteString.EMPTY;
    }

}