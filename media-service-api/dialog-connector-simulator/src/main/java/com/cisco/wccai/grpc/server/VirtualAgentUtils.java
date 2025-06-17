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
    private static final String THANK_YOU_AUDIO = "thankyou.wav";

    private static final String AGENT_TRANSFER_AUDIO = "agent-transfer.wav";

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
                .setResponseType(VoiceVAResponse.ResponseType.PARTIAL).build();
    }

    public static VoiceVAResponse getEndOfInputResponse() {
        return VoiceVAResponse.newBuilder().addOutputEvents(getOutputEvent(ByovaCommon.OutputEvent.EventType.END_OF_INPUT)).build();
    }

    public static VoiceVAResponse getAgentTransferResponse() {
        return VoiceVAResponse.newBuilder()
                .addPrompts(createPrompt("setting up prompt from dialog simulator for AGENT_TRANSFER event", AGENT_TRANSFER_AUDIO, false))
                .addOutputEvents(getOutputEvent(ByovaCommon.OutputEvent.EventType.TRANSFER_TO_AGENT))
                .build();
    }

    public static VoiceVAResponse getQueryFinalResponse() {
        return VoiceVAResponse.newBuilder()
                .addPrompts(createPrompt("Thank you for calling, have a good day", THANK_YOU_AUDIO, false))
                .setResponseType(VoiceVAResponse.ResponseType.FINAL)
                .setInputMode(Voicevirtualagent.VoiceVAInputMode.INPUT_VOICE_DTMF)
                .build();
    }

    public static VoiceVAResponse getQueryChunkResponse() {
        return VoiceVAResponse.newBuilder()
                .addPrompts(createPrompt("Thank you for calling, have a good day", THANK_YOU_AUDIO, false))
                .setResponseType(VoiceVAResponse.ResponseType.CHUNK)
                .setInputMode(Voicevirtualagent.VoiceVAInputMode.INPUT_VOICE_DTMF)
                .build();
    }

    public static VoiceVAResponse getQueryFinalChunkResponse() {
        return VoiceVAResponse.newBuilder()
                .addPrompts(createPromptWithEmptyAudio("Thank you for calling, have a good day", false))
                .setResponseType(VoiceVAResponse.ResponseType.FINAL)
                .setInputMode(Voicevirtualagent.VoiceVAInputMode.INPUT_VOICE_DTMF)
                .build();
    }

    public static VoiceVAResponse getNoInputResponse() {
        return VoiceVAResponse.newBuilder()
                .addPrompts(createPrompt("setting up prompt from dialog simulator for NO_INPUT event", GOOD_BYE_AUDIO, false))
                .addOutputEvents(getOutputEvent(ByovaCommon.OutputEvent.EventType.NO_INPUT))
                .setInputMode(Voicevirtualagent.VoiceVAInputMode.INPUT_VOICE_DTMF)
                .build();
    }

    private static Prompt createPrompt(String text, String audioFileName, Boolean barginEnabled) {
        return Prompt.newBuilder()
                .setText(text)
                .setAudioContent(getAudioContent(audioFileName))
                .setIsBargeInEnabled(barginEnabled)
                .build();
    }

    private static Prompt createPromptWithEmptyAudio(String text, Boolean barginEnabled) {
        return Prompt.newBuilder()
                .setText(text)
                .setAudioContent(ByteString.EMPTY)
                .setIsBargeInEnabled(barginEnabled)
                .build();
    }

    public static VoiceVAResponse getCallEndResponse() {
        return VoiceVAResponse.newBuilder()
                .addPrompts(createPrompt("setting up prompt from dialog simulator for CALL_END event", GOOD_BYE_AUDIO, false))
                .addOutputEvents(getOutputEvent(ByovaCommon.OutputEvent.EventType.SESSION_END))
                .build();
    }

    public static VoiceVAResponse getFinalVAResponse() {
        return VoiceVAResponse.newBuilder()
                .addPrompts(createPrompt("Thank you for calling the Virtual agent simulator. Have a nice day!", GOOD_BYE_AUDIO))
                .setSessionTranscript(createTextContent("setting reply text from dialog simulator for final NLU Response"))
                .setInputMode(Voicevirtualagent.VoiceVAInputMode.INPUT_VOICE_DTMF)
                .build();
    }

    public static VoiceVAResponse getCallStartResponse() {
        return VoiceVAResponse
                .newBuilder()
                .addPrompts(createPrompt("setting prompt from dialog simulator", WELCOME_AUDIO))
                .setSessionTranscript(createTextContent("Hi ! I'm your virtual agent for ticket booking from dialog simulator. How can I assist you today"))
                .setInputMode(Voicevirtualagent.VoiceVAInputMode.INPUT_VOICE_DTMF)
                .setInputHandlingConfig(inputHandlingConfig(5, 3000, ByovaCommon.DTMFDigits.DTMF_DIGIT_POUND, 10000))
                .build();
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
                case WELCOME_AUDIO, GOOD_BYE_AUDIO, AGENT_TRANSFER_AUDIO ->
                        ByteString.readFrom(VirtualAgentUtils.class.getClassLoader().getResourceAsStream("audio/" + fileName));
                default -> ByteString.EMPTY;
            };
        } catch (IOException e) {
            log.error("Error reading audio content from file: {}", fileName, e);
        }
        return ByteString.EMPTY;
    }

    public static ByovaCommon.InputHandlingConfig inputHandlingConfig(int dtmfInputLength, int interDigitTimeoutMillis, ByovaCommon.DTMFDigits termChar, int inputTimeoutMillis) {
        return ByovaCommon.InputHandlingConfig.newBuilder()
                .setDtmfConfig(dtmfInputConfig(dtmfInputLength, interDigitTimeoutMillis, termChar))
                .setSpeechTimers(ByovaCommon.InputSpeechTimers.newBuilder()
                        .setCompleteTimeoutMsec(inputTimeoutMillis)
                        .build())
                .build();
    }

    public static ByovaCommon.DTMFInputConfig dtmfInputConfig(int dtmfInputLength, int interDigitTimeoutMillis, ByovaCommon.DTMFDigits termChar) {
        return ByovaCommon.DTMFInputConfig.newBuilder()
                .setDtmfInputLength(dtmfInputLength)
                .setInterDigitTimeoutMsec(interDigitTimeoutMillis)
                .setTermchar(termChar)
                .build();
    }

}
