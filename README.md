# Webex Contact Center Media service APIs
## Media Service APIs
To be used by Providers integrating with CCAI Platform to offer below services-
1. BYoVA(Bring your own Virtual)- Where a provider can integrate their Virtual agents within webex contact center platform. Communication between the webex contact center client and Virtual agent happens via gRPC.
2. Real Time Media forking(Human agent - caller interaction)- Where a provider/customer can register a url with us where they want to receive the forked audio of the conversation between the agent(human agent) and caller.


## Bring your Own Virtual Agent
BYoVA can be leveraged for Self Service to offer Conversational IVR experience to end customer and offering them life like conversational experience by utilising conversational Speech and NLU enabled BOTs. 
Communication Protocol - gRPC
Proto defintion - voiceVirtualagent.proto(path- dialogue-connector-simulator/src/main/proto/com/cisco/wcc/ccai/media/v1)

## Real Time Media forking
Media forking can leveraged by customers/partners to fork the audio of human agent-caller interaction to external url(registered with cisco).
Communication Protocol - gRPC
Proto defintion - conversationAudioForking.proto(path- dialogue-connector-simulator/src/main/proto/com/cisco/wcc/ccai/media/v1)

## Common concepts/frameworks used for BYoVA and Media forking-
1. Service apps - https://developer.webex-cx.com/documentation/service-apps
2. BYoDS - https://developer.webex-cx.com/documentation/guides/bring-your-own-data-source
3. BYoVA - https://developer.webex-cx.com/documentation/guides/bring-your-own-virtual-agent

