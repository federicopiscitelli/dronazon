package GRPC;

import modules.Drone;
import proto.Welcome;
import proto.ManagerGrpc;
import io.grpc.stub.StreamObserver;

public class ManagerServiceImpl extends ManagerGrpc.ManagerImplBase {

    Drone d;

    public ManagerServiceImpl(Drone d){
        this.d = d;
    }

    @Override
    public void welcome(Welcome.WelcomeMessage request, StreamObserver<Welcome.WelcomeResponse> responseObserver) {
        Welcome.WelcomeResponse response = Welcome.WelcomeResponse
                .newBuilder()
                .setId(Integer.parseInt(d.getId()))
                .setMaster(d.isMaster())
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

}
