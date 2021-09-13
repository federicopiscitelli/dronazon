package proto;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 * <pre>
 *Service used for the election
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.25.0)",
    comments = "Source: Welcome.proto")
public final class ElectionerGrpc {

  private ElectionerGrpc() {}

  public static final String SERVICE_NAME = "proto.Electioner";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<proto.Welcome.ElectionMessage,
      proto.Welcome.ElectionResponse> getElectionMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Election",
      requestType = proto.Welcome.ElectionMessage.class,
      responseType = proto.Welcome.ElectionResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<proto.Welcome.ElectionMessage,
      proto.Welcome.ElectionResponse> getElectionMethod() {
    io.grpc.MethodDescriptor<proto.Welcome.ElectionMessage, proto.Welcome.ElectionResponse> getElectionMethod;
    if ((getElectionMethod = ElectionerGrpc.getElectionMethod) == null) {
      synchronized (ElectionerGrpc.class) {
        if ((getElectionMethod = ElectionerGrpc.getElectionMethod) == null) {
          ElectionerGrpc.getElectionMethod = getElectionMethod =
              io.grpc.MethodDescriptor.<proto.Welcome.ElectionMessage, proto.Welcome.ElectionResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Election"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.Welcome.ElectionMessage.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.Welcome.ElectionResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ElectionerMethodDescriptorSupplier("Election"))
              .build();
        }
      }
    }
    return getElectionMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static ElectionerStub newStub(io.grpc.Channel channel) {
    return new ElectionerStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static ElectionerBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new ElectionerBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static ElectionerFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new ElectionerFutureStub(channel);
  }

  /**
   * <pre>
   *Service used for the election
   * </pre>
   */
  public static abstract class ElectionerImplBase implements io.grpc.BindableService {

    /**
     */
    public void election(proto.Welcome.ElectionMessage request,
        io.grpc.stub.StreamObserver<proto.Welcome.ElectionResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getElectionMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getElectionMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                proto.Welcome.ElectionMessage,
                proto.Welcome.ElectionResponse>(
                  this, METHODID_ELECTION)))
          .build();
    }
  }

  /**
   * <pre>
   *Service used for the election
   * </pre>
   */
  public static final class ElectionerStub extends io.grpc.stub.AbstractStub<ElectionerStub> {
    private ElectionerStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ElectionerStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ElectionerStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ElectionerStub(channel, callOptions);
    }

    /**
     */
    public void election(proto.Welcome.ElectionMessage request,
        io.grpc.stub.StreamObserver<proto.Welcome.ElectionResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getElectionMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * <pre>
   *Service used for the election
   * </pre>
   */
  public static final class ElectionerBlockingStub extends io.grpc.stub.AbstractStub<ElectionerBlockingStub> {
    private ElectionerBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ElectionerBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ElectionerBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ElectionerBlockingStub(channel, callOptions);
    }

    /**
     */
    public proto.Welcome.ElectionResponse election(proto.Welcome.ElectionMessage request) {
      return blockingUnaryCall(
          getChannel(), getElectionMethod(), getCallOptions(), request);
    }
  }

  /**
   * <pre>
   *Service used for the election
   * </pre>
   */
  public static final class ElectionerFutureStub extends io.grpc.stub.AbstractStub<ElectionerFutureStub> {
    private ElectionerFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ElectionerFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ElectionerFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ElectionerFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<proto.Welcome.ElectionResponse> election(
        proto.Welcome.ElectionMessage request) {
      return futureUnaryCall(
          getChannel().newCall(getElectionMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_ELECTION = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final ElectionerImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(ElectionerImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_ELECTION:
          serviceImpl.election((proto.Welcome.ElectionMessage) request,
              (io.grpc.stub.StreamObserver<proto.Welcome.ElectionResponse>) responseObserver);
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
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class ElectionerBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    ElectionerBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return proto.Welcome.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("Electioner");
    }
  }

  private static final class ElectionerFileDescriptorSupplier
      extends ElectionerBaseDescriptorSupplier {
    ElectionerFileDescriptorSupplier() {}
  }

  private static final class ElectionerMethodDescriptorSupplier
      extends ElectionerBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    ElectionerMethodDescriptorSupplier(String methodName) {
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
      synchronized (ElectionerGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new ElectionerFileDescriptorSupplier())
              .addMethod(getElectionMethod())
              .build();
        }
      }
    }
    return result;
  }
}
