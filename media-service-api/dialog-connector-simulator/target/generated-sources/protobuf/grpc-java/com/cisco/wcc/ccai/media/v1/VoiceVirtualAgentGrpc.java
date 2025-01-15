package com.cisco.wcc.ccai.media.v1;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 * <pre>
 * Service definition for the Audio Virtual Agent gRPC API.
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.62.2)",
    comments = "Source: com/cisco/wcc/ccai/media/v1/voicevirtualagent.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class VoiceVirtualAgentGrpc {

  private VoiceVirtualAgentGrpc() {}

  public static final java.lang.String SERVICE_NAME = "com.cisco.wcc.ccai.media.v1.VoiceVirtualAgent";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.cisco.wcc.ccai.media.v1.Voicevirtualagent.VoiceVARequest,
      com.cisco.wcc.ccai.media.v1.Voicevirtualagent.VoiceVAResponse> getProcessCallerInputMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ProcessCallerInput",
      requestType = com.cisco.wcc.ccai.media.v1.Voicevirtualagent.VoiceVARequest.class,
      responseType = com.cisco.wcc.ccai.media.v1.Voicevirtualagent.VoiceVAResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
  public static io.grpc.MethodDescriptor<com.cisco.wcc.ccai.media.v1.Voicevirtualagent.VoiceVARequest,
      com.cisco.wcc.ccai.media.v1.Voicevirtualagent.VoiceVAResponse> getProcessCallerInputMethod() {
    io.grpc.MethodDescriptor<com.cisco.wcc.ccai.media.v1.Voicevirtualagent.VoiceVARequest, com.cisco.wcc.ccai.media.v1.Voicevirtualagent.VoiceVAResponse> getProcessCallerInputMethod;
    if ((getProcessCallerInputMethod = VoiceVirtualAgentGrpc.getProcessCallerInputMethod) == null) {
      synchronized (VoiceVirtualAgentGrpc.class) {
        if ((getProcessCallerInputMethod = VoiceVirtualAgentGrpc.getProcessCallerInputMethod) == null) {
          VoiceVirtualAgentGrpc.getProcessCallerInputMethod = getProcessCallerInputMethod =
              io.grpc.MethodDescriptor.<com.cisco.wcc.ccai.media.v1.Voicevirtualagent.VoiceVARequest, com.cisco.wcc.ccai.media.v1.Voicevirtualagent.VoiceVAResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ProcessCallerInput"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.cisco.wcc.ccai.media.v1.Voicevirtualagent.VoiceVARequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.cisco.wcc.ccai.media.v1.Voicevirtualagent.VoiceVAResponse.getDefaultInstance()))
              .setSchemaDescriptor(new VoiceVirtualAgentMethodDescriptorSupplier("ProcessCallerInput"))
              .build();
        }
      }
    }
    return getProcessCallerInputMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.cisco.wcc.ccai.media.v1.ByovaCommon.ListVARequest,
      com.cisco.wcc.ccai.media.v1.ByovaCommon.ListVAResponse> getListVirtualAgentsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ListVirtualAgents",
      requestType = com.cisco.wcc.ccai.media.v1.ByovaCommon.ListVARequest.class,
      responseType = com.cisco.wcc.ccai.media.v1.ByovaCommon.ListVAResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.cisco.wcc.ccai.media.v1.ByovaCommon.ListVARequest,
      com.cisco.wcc.ccai.media.v1.ByovaCommon.ListVAResponse> getListVirtualAgentsMethod() {
    io.grpc.MethodDescriptor<com.cisco.wcc.ccai.media.v1.ByovaCommon.ListVARequest, com.cisco.wcc.ccai.media.v1.ByovaCommon.ListVAResponse> getListVirtualAgentsMethod;
    if ((getListVirtualAgentsMethod = VoiceVirtualAgentGrpc.getListVirtualAgentsMethod) == null) {
      synchronized (VoiceVirtualAgentGrpc.class) {
        if ((getListVirtualAgentsMethod = VoiceVirtualAgentGrpc.getListVirtualAgentsMethod) == null) {
          VoiceVirtualAgentGrpc.getListVirtualAgentsMethod = getListVirtualAgentsMethod =
              io.grpc.MethodDescriptor.<com.cisco.wcc.ccai.media.v1.ByovaCommon.ListVARequest, com.cisco.wcc.ccai.media.v1.ByovaCommon.ListVAResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ListVirtualAgents"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.cisco.wcc.ccai.media.v1.ByovaCommon.ListVARequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.cisco.wcc.ccai.media.v1.ByovaCommon.ListVAResponse.getDefaultInstance()))
              .setSchemaDescriptor(new VoiceVirtualAgentMethodDescriptorSupplier("ListVirtualAgents"))
              .build();
        }
      }
    }
    return getListVirtualAgentsMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static VoiceVirtualAgentStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<VoiceVirtualAgentStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<VoiceVirtualAgentStub>() {
        @java.lang.Override
        public VoiceVirtualAgentStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new VoiceVirtualAgentStub(channel, callOptions);
        }
      };
    return VoiceVirtualAgentStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static VoiceVirtualAgentBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<VoiceVirtualAgentBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<VoiceVirtualAgentBlockingStub>() {
        @java.lang.Override
        public VoiceVirtualAgentBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new VoiceVirtualAgentBlockingStub(channel, callOptions);
        }
      };
    return VoiceVirtualAgentBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static VoiceVirtualAgentFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<VoiceVirtualAgentFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<VoiceVirtualAgentFutureStub>() {
        @java.lang.Override
        public VoiceVirtualAgentFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new VoiceVirtualAgentFutureStub(channel, callOptions);
        }
      };
    return VoiceVirtualAgentFutureStub.newStub(factory, channel);
  }

  /**
   * <pre>
   * Service definition for the Audio Virtual Agent gRPC API.
   * </pre>
   */
  public interface AsyncService {

    /**
     * <pre>
     * Bidirectional streaming RPC to send and receive caller audio, DTMF,
     * or input events.
     * </pre>
     */
    default io.grpc.stub.StreamObserver<com.cisco.wcc.ccai.media.v1.Voicevirtualagent.VoiceVARequest> processCallerInput(
        io.grpc.stub.StreamObserver<com.cisco.wcc.ccai.media.v1.Voicevirtualagent.VoiceVAResponse> responseObserver) {
      return io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall(getProcessCallerInputMethod(), responseObserver);
    }

    /**
     * <pre>
     *The Service that takes virtual agent list request and org id and returns a list of bots
     * </pre>
     */
    default void listVirtualAgents(com.cisco.wcc.ccai.media.v1.ByovaCommon.ListVARequest request,
        io.grpc.stub.StreamObserver<com.cisco.wcc.ccai.media.v1.ByovaCommon.ListVAResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getListVirtualAgentsMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service VoiceVirtualAgent.
   * <pre>
   * Service definition for the Audio Virtual Agent gRPC API.
   * </pre>
   */
  public static abstract class VoiceVirtualAgentImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return VoiceVirtualAgentGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service VoiceVirtualAgent.
   * <pre>
   * Service definition for the Audio Virtual Agent gRPC API.
   * </pre>
   */
  public static final class VoiceVirtualAgentStub
      extends io.grpc.stub.AbstractAsyncStub<VoiceVirtualAgentStub> {
    private VoiceVirtualAgentStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected VoiceVirtualAgentStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new VoiceVirtualAgentStub(channel, callOptions);
    }

    /**
     * <pre>
     * Bidirectional streaming RPC to send and receive caller audio, DTMF,
     * or input events.
     * </pre>
     */
    public io.grpc.stub.StreamObserver<com.cisco.wcc.ccai.media.v1.Voicevirtualagent.VoiceVARequest> processCallerInput(
        io.grpc.stub.StreamObserver<com.cisco.wcc.ccai.media.v1.Voicevirtualagent.VoiceVAResponse> responseObserver) {
      return io.grpc.stub.ClientCalls.asyncBidiStreamingCall(
          getChannel().newCall(getProcessCallerInputMethod(), getCallOptions()), responseObserver);
    }

    /**
     * <pre>
     *The Service that takes virtual agent list request and org id and returns a list of bots
     * </pre>
     */
    public void listVirtualAgents(com.cisco.wcc.ccai.media.v1.ByovaCommon.ListVARequest request,
        io.grpc.stub.StreamObserver<com.cisco.wcc.ccai.media.v1.ByovaCommon.ListVAResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getListVirtualAgentsMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service VoiceVirtualAgent.
   * <pre>
   * Service definition for the Audio Virtual Agent gRPC API.
   * </pre>
   */
  public static final class VoiceVirtualAgentBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<VoiceVirtualAgentBlockingStub> {
    private VoiceVirtualAgentBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected VoiceVirtualAgentBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new VoiceVirtualAgentBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     *The Service that takes virtual agent list request and org id and returns a list of bots
     * </pre>
     */
    public com.cisco.wcc.ccai.media.v1.ByovaCommon.ListVAResponse listVirtualAgents(com.cisco.wcc.ccai.media.v1.ByovaCommon.ListVARequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getListVirtualAgentsMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service VoiceVirtualAgent.
   * <pre>
   * Service definition for the Audio Virtual Agent gRPC API.
   * </pre>
   */
  public static final class VoiceVirtualAgentFutureStub
      extends io.grpc.stub.AbstractFutureStub<VoiceVirtualAgentFutureStub> {
    private VoiceVirtualAgentFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected VoiceVirtualAgentFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new VoiceVirtualAgentFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     *The Service that takes virtual agent list request and org id and returns a list of bots
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.cisco.wcc.ccai.media.v1.ByovaCommon.ListVAResponse> listVirtualAgents(
        com.cisco.wcc.ccai.media.v1.ByovaCommon.ListVARequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getListVirtualAgentsMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_LIST_VIRTUAL_AGENTS = 0;
  private static final int METHODID_PROCESS_CALLER_INPUT = 1;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_LIST_VIRTUAL_AGENTS:
          serviceImpl.listVirtualAgents((com.cisco.wcc.ccai.media.v1.ByovaCommon.ListVARequest) request,
              (io.grpc.stub.StreamObserver<com.cisco.wcc.ccai.media.v1.ByovaCommon.ListVAResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_PROCESS_CALLER_INPUT:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.processCallerInput(
              (io.grpc.stub.StreamObserver<com.cisco.wcc.ccai.media.v1.Voicevirtualagent.VoiceVAResponse>) responseObserver);
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getProcessCallerInputMethod(),
          io.grpc.stub.ServerCalls.asyncBidiStreamingCall(
            new MethodHandlers<
              com.cisco.wcc.ccai.media.v1.Voicevirtualagent.VoiceVARequest,
              com.cisco.wcc.ccai.media.v1.Voicevirtualagent.VoiceVAResponse>(
                service, METHODID_PROCESS_CALLER_INPUT)))
        .addMethod(
          getListVirtualAgentsMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.cisco.wcc.ccai.media.v1.ByovaCommon.ListVARequest,
              com.cisco.wcc.ccai.media.v1.ByovaCommon.ListVAResponse>(
                service, METHODID_LIST_VIRTUAL_AGENTS)))
        .build();
  }

  private static abstract class VoiceVirtualAgentBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    VoiceVirtualAgentBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.cisco.wcc.ccai.media.v1.Voicevirtualagent.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("VoiceVirtualAgent");
    }
  }

  private static final class VoiceVirtualAgentFileDescriptorSupplier
      extends VoiceVirtualAgentBaseDescriptorSupplier {
    VoiceVirtualAgentFileDescriptorSupplier() {}
  }

  private static final class VoiceVirtualAgentMethodDescriptorSupplier
      extends VoiceVirtualAgentBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    VoiceVirtualAgentMethodDescriptorSupplier(java.lang.String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (VoiceVirtualAgentGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new VoiceVirtualAgentFileDescriptorSupplier())
              .addMethod(getProcessCallerInputMethod())
              .addMethod(getListVirtualAgentsMethod())
              .build();
        }
      }
    }
    return result;
  }
}
