package GRPC;

import modules.Drone;
import proto.Welcome;
import proto.ManagerGrpc;
import io.grpc.stub.StreamObserver;

import java.util.List;

public class ManagerServiceImpl extends ManagerGrpc.ManagerImplBase {

    Drone d;

    public ManagerServiceImpl(Drone d){
        this.d = d;
    }


    @Override
    public void welcome(Welcome.WelcomeMessage request, StreamObserver<Welcome.WelcomeResponse> responseObserver) {

        List<Drone> myDronesList = d.getDronesList();
        int originalSize = myDronesList.size(); //original size of the list used to avoid infinite loop

        for(int i=0; i<originalSize; i++){
            if(request.getId()<Integer.parseInt(myDronesList.get(i).getId())){
                Drone droneToInsert = new Drone(String.valueOf(request.getId()), request.getIp(), request.getPort());
                //adding the drone to my list is equivalent to adding the drone to my topology of the network
                myDronesList.add(i,droneToInsert);
            }
        }

        d.setDronesList(myDronesList);

        Welcome.WelcomeResponse response = Welcome.WelcomeResponse
                .newBuilder()
                .setId(Integer.parseInt(d.getId()))
                .setMaster(d.isMaster())
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }


}
