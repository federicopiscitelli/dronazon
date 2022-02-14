package threads;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import modules.Drone;
import modules.Order;
import proto.ManagerGrpc;
import proto.Welcome;

public class AssignPendingDeliveries extends Thread{

    private Drone drone;
    private ManagedChannel channel;

    public AssignPendingDeliveries(Drone drone, String toIp){
        this.drone = drone;
        channel = ManagedChannelBuilder.forTarget(toIp).usePlaintext().build();
    }

    public void run(){
        ManagerGrpc.ManagerStub newStub = ManagerGrpc.newStub(channel);

        for(int i=0; i<drone.ordersQueue.size();i++){
            Order o = drone.ordersQueue.getNth(i);

            Welcome.PendingOrdersMessage orderMessage = Welcome.PendingOrdersMessage
                    .newBuilder()
                    .setOrder(
                        Welcome.Order.newBuilder()
                            .setId(o.getId())
                                .setRetire(
                                    Welcome.Position.newBuilder()
                                            .setY(o.getRetire().getY())
                                            .setX(o.getRetire().getX())
                                )
                                .setDelivery(
                                        Welcome.Position.newBuilder()
                                                .setY(o.getDelivery().getY())
                                                .setX(o.getDelivery().getX())
                                )

                    )
                    .build();

            newStub.assignPendingDeliveries(orderMessage, new StreamObserver<Welcome.PendingOrdersResponse>() {
                public void onNext(Welcome.PendingOrdersResponse pendingOrdersResponse) { }
                public void onError(Throwable throwable) { stop(); }
                public void onCompleted() {
                    channel.shutdownNow();
                }
            });
        }
    }
}
