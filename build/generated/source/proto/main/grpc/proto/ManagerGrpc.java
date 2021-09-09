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
 *Service used when a drone enters the network
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.25.0)",
    comments = "Source: Welcome.proto")
public final class ManagerGrpc {

  private ManagerGrpc() {}

  public static final String SERVICE_NAME = "proto.Manager";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<proto.Welcome.WelcomeMessage,
      proto.Welcome.WelcomeResponse> getWelcomeMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Welcome",
      requestType = proto.Welcome.WelcomeMessage.class,
      responseType = proto.Welcome.WelcomeResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<proto.Welcome.WelcomeMessage,
      proto.Welcome.WelcomeResponse> getWelcomeMethod() {
    io.grpc.MethodDescriptor<proto.Welcome.WelcomeMessage, proto.Welcome.WelcomeResponse> getWelcomeMethod;
    if ((getWelcomeMethod = ManagerGrpc.getWelcomeMethod) == null) {
      synchronized (ManagerGrpc.class) {
        if ((getWelcomeMethod = ManagerGrpc.getWelcomeMethod) == null) {
          ManagerGrpc.getWelcomeMethod = getWelcomeMethod =
              io.grpc.MethodDescriptor.<proto.Welcome.WelcomeMessage, proto.Welcome.WelcomeResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Welcome"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.Welcome.WelcomeMessage.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.Welcome.WelcomeResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ManagerMethodDescriptorSupplier("Welcome"))
              .build();
        }
      }
    }
    return getWelcomeMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static ManagerStub newStub(io.grpc.Channel channel) {
    return new ManagerStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static ManagerBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new ManagerBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static ManagerFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new ManagerFutureStub(channel);
  }

  /**
   * <pre>
   *Service used when a drone enters the network
   * </pre>
   */
  public static abstract class ManagerImplBase implements io.grpc.BindableService {

    /**
     */
    public void welcome(proto.Welcome.WelcomeMessage request,
        io.grpc.stub.StreamObserver<proto.Welcome.WelcomeResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getWelcomeMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getWelcomeMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                proto.Welcome.WelcomeMessage,
                proto.Welcome.WelcomeResponse>(
                  this, METHODID_WELCOME)))
          .build();
    }
  }

  /**
   * <pre>
   *Service used when a drone enters the network
   * </pre>
   */
  public static final class ManagerStub extends io.grpc.stub.AbstractStub<ManagerStub> {
    private ManagerStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ManagerStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ManagerStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ManagerStub(channel, callOptions);
    }

    /**
     */
    public void welcome(proto.Welcome.WelcomeMessage request,
        io.grpc.stub.StreamObserver<proto.Welcome.WelcomeResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getWelcomeMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * <pre>
   *Service used when a drone enters the network
   * </pre>
   */
  public static final class ManagerBlockingStub extends io.grpc.stub.AbstractStub<ManagerBlockingStub> {
    private ManagerBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ManagerBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ManagerBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ManagerBlockingStub(channel, callOptions);
    }

    /**
     */
    public proto.Welcome.WelcomeResponse welcome(proto.Welcome.WelcomeMessage request) {
      return blockingUnaryCall(
          getChannel(), getWelcomeMethod(), getCallOptions(), request);
    }
  }

  /**
   * <pre>
   *Service used when a drone enters the network
   * </pre>
   */
  public static final class ManagerFutureStub extends io.grpc.stub.AbstractStub<ManagerFutureStub> {
    private ManagerFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ManagerFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ManagerFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ManagerFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<proto.Welcome.WelcomeResponse> welcome(
        proto.Welcome.WelcomeMessage request) {
      return futureUnaryCall(
          getChannel().newCall(getWelcomeMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_WELCOME = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final ManagerImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(ManagerImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_WELCOME:
          serviceImpl.welcome((proto.Welcome.WelcomeMessage) request,
              (io.grpc.stub.StreamObserver<proto.Welcome.WelcomeResponse>) responseObserver);
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

  private static abstract class ManagerBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    ManagerBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return proto.Welcome.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("Manager");
    }
  }

  private static final class ManagerFileDescriptorSupplier
      extends ManagerBaseDescriptorSupplier {
    ManagerFileDescriptorSupplier() {}
  }

  private static final class ManagerMethodDescriptorSupplier
      extends ManagerBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    ManagerMethodDescriptorSupplier(String methodName) {
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
      synchronized (ManagerGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new ManagerFileDescriptorSupplier())
              .addMethod(getWelcomeMethod())
              .build();
        }
      }
    }
    return result;
  }
}
