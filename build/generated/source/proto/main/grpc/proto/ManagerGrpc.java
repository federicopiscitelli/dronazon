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
    if ((getElectionMethod = ManagerGrpc.getElectionMethod) == null) {
      synchronized (ManagerGrpc.class) {
        if ((getElectionMethod = ManagerGrpc.getElectionMethod) == null) {
          ManagerGrpc.getElectionMethod = getElectionMethod =
              io.grpc.MethodDescriptor.<proto.Welcome.ElectionMessage, proto.Welcome.ElectionResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Election"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.Welcome.ElectionMessage.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.Welcome.ElectionResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ManagerMethodDescriptorSupplier("Election"))
              .build();
        }
      }
    }
    return getElectionMethod;
  }

  private static volatile io.grpc.MethodDescriptor<proto.Welcome.ElectedMessage,
      proto.Welcome.ElectedResponse> getElectedMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Elected",
      requestType = proto.Welcome.ElectedMessage.class,
      responseType = proto.Welcome.ElectedResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<proto.Welcome.ElectedMessage,
      proto.Welcome.ElectedResponse> getElectedMethod() {
    io.grpc.MethodDescriptor<proto.Welcome.ElectedMessage, proto.Welcome.ElectedResponse> getElectedMethod;
    if ((getElectedMethod = ManagerGrpc.getElectedMethod) == null) {
      synchronized (ManagerGrpc.class) {
        if ((getElectedMethod = ManagerGrpc.getElectedMethod) == null) {
          ManagerGrpc.getElectedMethod = getElectedMethod =
              io.grpc.MethodDescriptor.<proto.Welcome.ElectedMessage, proto.Welcome.ElectedResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Elected"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.Welcome.ElectedMessage.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.Welcome.ElectedResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ManagerMethodDescriptorSupplier("Elected"))
              .build();
        }
      }
    }
    return getElectedMethod;
  }

  private static volatile io.grpc.MethodDescriptor<proto.Welcome.AliveMessage,
      proto.Welcome.AliveResponse> getAliveMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Alive",
      requestType = proto.Welcome.AliveMessage.class,
      responseType = proto.Welcome.AliveResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<proto.Welcome.AliveMessage,
      proto.Welcome.AliveResponse> getAliveMethod() {
    io.grpc.MethodDescriptor<proto.Welcome.AliveMessage, proto.Welcome.AliveResponse> getAliveMethod;
    if ((getAliveMethod = ManagerGrpc.getAliveMethod) == null) {
      synchronized (ManagerGrpc.class) {
        if ((getAliveMethod = ManagerGrpc.getAliveMethod) == null) {
          ManagerGrpc.getAliveMethod = getAliveMethod =
              io.grpc.MethodDescriptor.<proto.Welcome.AliveMessage, proto.Welcome.AliveResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Alive"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.Welcome.AliveMessage.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.Welcome.AliveResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ManagerMethodDescriptorSupplier("Alive"))
              .build();
        }
      }
    }
    return getAliveMethod;
  }

  private static volatile io.grpc.MethodDescriptor<proto.Welcome.DeliveryMessage,
      proto.Welcome.DeliveryResponse> getDeliveryMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Delivery",
      requestType = proto.Welcome.DeliveryMessage.class,
      responseType = proto.Welcome.DeliveryResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<proto.Welcome.DeliveryMessage,
      proto.Welcome.DeliveryResponse> getDeliveryMethod() {
    io.grpc.MethodDescriptor<proto.Welcome.DeliveryMessage, proto.Welcome.DeliveryResponse> getDeliveryMethod;
    if ((getDeliveryMethod = ManagerGrpc.getDeliveryMethod) == null) {
      synchronized (ManagerGrpc.class) {
        if ((getDeliveryMethod = ManagerGrpc.getDeliveryMethod) == null) {
          ManagerGrpc.getDeliveryMethod = getDeliveryMethod =
              io.grpc.MethodDescriptor.<proto.Welcome.DeliveryMessage, proto.Welcome.DeliveryResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Delivery"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.Welcome.DeliveryMessage.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.Welcome.DeliveryResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ManagerMethodDescriptorSupplier("Delivery"))
              .build();
        }
      }
    }
    return getDeliveryMethod;
  }

  private static volatile io.grpc.MethodDescriptor<proto.Welcome.DeliveredMessage,
      proto.Welcome.DeliveredResponse> getDeliveredMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Delivered",
      requestType = proto.Welcome.DeliveredMessage.class,
      responseType = proto.Welcome.DeliveredResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<proto.Welcome.DeliveredMessage,
      proto.Welcome.DeliveredResponse> getDeliveredMethod() {
    io.grpc.MethodDescriptor<proto.Welcome.DeliveredMessage, proto.Welcome.DeliveredResponse> getDeliveredMethod;
    if ((getDeliveredMethod = ManagerGrpc.getDeliveredMethod) == null) {
      synchronized (ManagerGrpc.class) {
        if ((getDeliveredMethod = ManagerGrpc.getDeliveredMethod) == null) {
          ManagerGrpc.getDeliveredMethod = getDeliveredMethod =
              io.grpc.MethodDescriptor.<proto.Welcome.DeliveredMessage, proto.Welcome.DeliveredResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Delivered"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.Welcome.DeliveredMessage.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.Welcome.DeliveredResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ManagerMethodDescriptorSupplier("Delivered"))
              .build();
        }
      }
    }
    return getDeliveredMethod;
  }

  private static volatile io.grpc.MethodDescriptor<proto.Welcome.RechargeRequest,
      proto.Welcome.RechargeResponse> getRechargeMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Recharge",
      requestType = proto.Welcome.RechargeRequest.class,
      responseType = proto.Welcome.RechargeResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<proto.Welcome.RechargeRequest,
      proto.Welcome.RechargeResponse> getRechargeMethod() {
    io.grpc.MethodDescriptor<proto.Welcome.RechargeRequest, proto.Welcome.RechargeResponse> getRechargeMethod;
    if ((getRechargeMethod = ManagerGrpc.getRechargeMethod) == null) {
      synchronized (ManagerGrpc.class) {
        if ((getRechargeMethod = ManagerGrpc.getRechargeMethod) == null) {
          ManagerGrpc.getRechargeMethod = getRechargeMethod =
              io.grpc.MethodDescriptor.<proto.Welcome.RechargeRequest, proto.Welcome.RechargeResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Recharge"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.Welcome.RechargeRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.Welcome.RechargeResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ManagerMethodDescriptorSupplier("Recharge"))
              .build();
        }
      }
    }
    return getRechargeMethod;
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
   */
  public static abstract class ManagerImplBase implements io.grpc.BindableService {

    /**
     * <pre>
     *Service used when a drone enters the network
     * </pre>
     */
    public void welcome(proto.Welcome.WelcomeMessage request,
        io.grpc.stub.StreamObserver<proto.Welcome.WelcomeResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getWelcomeMethod(), responseObserver);
    }

    /**
     * <pre>
     *Service used for the election
     * </pre>
     */
    public void election(proto.Welcome.ElectionMessage request,
        io.grpc.stub.StreamObserver<proto.Welcome.ElectionResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getElectionMethod(), responseObserver);
    }

    /**
     * <pre>
     *Service used after the election
     * </pre>
     */
    public void elected(proto.Welcome.ElectedMessage request,
        io.grpc.stub.StreamObserver<proto.Welcome.ElectedResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getElectedMethod(), responseObserver);
    }

    /**
     * <pre>
     *Service used to check if master is alive
     * </pre>
     */
    public void alive(proto.Welcome.AliveMessage request,
        io.grpc.stub.StreamObserver<proto.Welcome.AliveResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getAliveMethod(), responseObserver);
    }

    /**
     * <pre>
     *Service used to assign the delivery
     * </pre>
     */
    public void delivery(proto.Welcome.DeliveryMessage request,
        io.grpc.stub.StreamObserver<proto.Welcome.DeliveryResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getDeliveryMethod(), responseObserver);
    }

    /**
     * <pre>
     *Service used to comunicate the success on the delivery
     * </pre>
     */
    public void delivered(proto.Welcome.DeliveredMessage request,
        io.grpc.stub.StreamObserver<proto.Welcome.DeliveredResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getDeliveredMethod(), responseObserver);
    }

    /**
     * <pre>
     *Service used to request the availability of the recharge station
     * </pre>
     */
    public void recharge(proto.Welcome.RechargeRequest request,
        io.grpc.stub.StreamObserver<proto.Welcome.RechargeResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getRechargeMethod(), responseObserver);
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
          .addMethod(
            getElectionMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                proto.Welcome.ElectionMessage,
                proto.Welcome.ElectionResponse>(
                  this, METHODID_ELECTION)))
          .addMethod(
            getElectedMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                proto.Welcome.ElectedMessage,
                proto.Welcome.ElectedResponse>(
                  this, METHODID_ELECTED)))
          .addMethod(
            getAliveMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                proto.Welcome.AliveMessage,
                proto.Welcome.AliveResponse>(
                  this, METHODID_ALIVE)))
          .addMethod(
            getDeliveryMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                proto.Welcome.DeliveryMessage,
                proto.Welcome.DeliveryResponse>(
                  this, METHODID_DELIVERY)))
          .addMethod(
            getDeliveredMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                proto.Welcome.DeliveredMessage,
                proto.Welcome.DeliveredResponse>(
                  this, METHODID_DELIVERED)))
          .addMethod(
            getRechargeMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                proto.Welcome.RechargeRequest,
                proto.Welcome.RechargeResponse>(
                  this, METHODID_RECHARGE)))
          .build();
    }
  }

  /**
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
     * <pre>
     *Service used when a drone enters the network
     * </pre>
     */
    public void welcome(proto.Welcome.WelcomeMessage request,
        io.grpc.stub.StreamObserver<proto.Welcome.WelcomeResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getWelcomeMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     *Service used for the election
     * </pre>
     */
    public void election(proto.Welcome.ElectionMessage request,
        io.grpc.stub.StreamObserver<proto.Welcome.ElectionResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getElectionMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     *Service used after the election
     * </pre>
     */
    public void elected(proto.Welcome.ElectedMessage request,
        io.grpc.stub.StreamObserver<proto.Welcome.ElectedResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getElectedMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     *Service used to check if master is alive
     * </pre>
     */
    public void alive(proto.Welcome.AliveMessage request,
        io.grpc.stub.StreamObserver<proto.Welcome.AliveResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getAliveMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     *Service used to assign the delivery
     * </pre>
     */
    public void delivery(proto.Welcome.DeliveryMessage request,
        io.grpc.stub.StreamObserver<proto.Welcome.DeliveryResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getDeliveryMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     *Service used to comunicate the success on the delivery
     * </pre>
     */
    public void delivered(proto.Welcome.DeliveredMessage request,
        io.grpc.stub.StreamObserver<proto.Welcome.DeliveredResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getDeliveredMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     *Service used to request the availability of the recharge station
     * </pre>
     */
    public void recharge(proto.Welcome.RechargeRequest request,
        io.grpc.stub.StreamObserver<proto.Welcome.RechargeResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getRechargeMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
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
     * <pre>
     *Service used when a drone enters the network
     * </pre>
     */
    public proto.Welcome.WelcomeResponse welcome(proto.Welcome.WelcomeMessage request) {
      return blockingUnaryCall(
          getChannel(), getWelcomeMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     *Service used for the election
     * </pre>
     */
    public proto.Welcome.ElectionResponse election(proto.Welcome.ElectionMessage request) {
      return blockingUnaryCall(
          getChannel(), getElectionMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     *Service used after the election
     * </pre>
     */
    public proto.Welcome.ElectedResponse elected(proto.Welcome.ElectedMessage request) {
      return blockingUnaryCall(
          getChannel(), getElectedMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     *Service used to check if master is alive
     * </pre>
     */
    public proto.Welcome.AliveResponse alive(proto.Welcome.AliveMessage request) {
      return blockingUnaryCall(
          getChannel(), getAliveMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     *Service used to assign the delivery
     * </pre>
     */
    public proto.Welcome.DeliveryResponse delivery(proto.Welcome.DeliveryMessage request) {
      return blockingUnaryCall(
          getChannel(), getDeliveryMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     *Service used to comunicate the success on the delivery
     * </pre>
     */
    public proto.Welcome.DeliveredResponse delivered(proto.Welcome.DeliveredMessage request) {
      return blockingUnaryCall(
          getChannel(), getDeliveredMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     *Service used to request the availability of the recharge station
     * </pre>
     */
    public proto.Welcome.RechargeResponse recharge(proto.Welcome.RechargeRequest request) {
      return blockingUnaryCall(
          getChannel(), getRechargeMethod(), getCallOptions(), request);
    }
  }

  /**
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
     * <pre>
     *Service used when a drone enters the network
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<proto.Welcome.WelcomeResponse> welcome(
        proto.Welcome.WelcomeMessage request) {
      return futureUnaryCall(
          getChannel().newCall(getWelcomeMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     *Service used for the election
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<proto.Welcome.ElectionResponse> election(
        proto.Welcome.ElectionMessage request) {
      return futureUnaryCall(
          getChannel().newCall(getElectionMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     *Service used after the election
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<proto.Welcome.ElectedResponse> elected(
        proto.Welcome.ElectedMessage request) {
      return futureUnaryCall(
          getChannel().newCall(getElectedMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     *Service used to check if master is alive
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<proto.Welcome.AliveResponse> alive(
        proto.Welcome.AliveMessage request) {
      return futureUnaryCall(
          getChannel().newCall(getAliveMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     *Service used to assign the delivery
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<proto.Welcome.DeliveryResponse> delivery(
        proto.Welcome.DeliveryMessage request) {
      return futureUnaryCall(
          getChannel().newCall(getDeliveryMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     *Service used to comunicate the success on the delivery
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<proto.Welcome.DeliveredResponse> delivered(
        proto.Welcome.DeliveredMessage request) {
      return futureUnaryCall(
          getChannel().newCall(getDeliveredMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     *Service used to request the availability of the recharge station
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<proto.Welcome.RechargeResponse> recharge(
        proto.Welcome.RechargeRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getRechargeMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_WELCOME = 0;
  private static final int METHODID_ELECTION = 1;
  private static final int METHODID_ELECTED = 2;
  private static final int METHODID_ALIVE = 3;
  private static final int METHODID_DELIVERY = 4;
  private static final int METHODID_DELIVERED = 5;
  private static final int METHODID_RECHARGE = 6;

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
        case METHODID_ELECTION:
          serviceImpl.election((proto.Welcome.ElectionMessage) request,
              (io.grpc.stub.StreamObserver<proto.Welcome.ElectionResponse>) responseObserver);
          break;
        case METHODID_ELECTED:
          serviceImpl.elected((proto.Welcome.ElectedMessage) request,
              (io.grpc.stub.StreamObserver<proto.Welcome.ElectedResponse>) responseObserver);
          break;
        case METHODID_ALIVE:
          serviceImpl.alive((proto.Welcome.AliveMessage) request,
              (io.grpc.stub.StreamObserver<proto.Welcome.AliveResponse>) responseObserver);
          break;
        case METHODID_DELIVERY:
          serviceImpl.delivery((proto.Welcome.DeliveryMessage) request,
              (io.grpc.stub.StreamObserver<proto.Welcome.DeliveryResponse>) responseObserver);
          break;
        case METHODID_DELIVERED:
          serviceImpl.delivered((proto.Welcome.DeliveredMessage) request,
              (io.grpc.stub.StreamObserver<proto.Welcome.DeliveredResponse>) responseObserver);
          break;
        case METHODID_RECHARGE:
          serviceImpl.recharge((proto.Welcome.RechargeRequest) request,
              (io.grpc.stub.StreamObserver<proto.Welcome.RechargeResponse>) responseObserver);
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
              .addMethod(getElectionMethod())
              .addMethod(getElectedMethod())
              .addMethod(getAliveMethod())
              .addMethod(getDeliveryMethod())
              .addMethod(getDeliveredMethod())
              .addMethod(getRechargeMethod())
              .build();
        }
      }
    }
    return result;
  }
}
