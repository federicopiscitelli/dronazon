package GRPC;

import io.grpc.Context;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import modules.Drone;
import modules.Position;
import proto.Welcome;
import proto.ManagerGrpc;
import proto.ElectionerGrpc;
import io.grpc.stub.StreamObserver;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ManagerServiceImpl extends ManagerGrpc.ManagerImplBase {

    Drone d;

    public ManagerServiceImpl(Drone d){
        this.d = d;
    }


    @Override
    public void welcome(Welcome.WelcomeMessage request, StreamObserver<Welcome.WelcomeResponse> responseObserver) {

        List<Drone> myDronesList = d.getDronesList();
        System.out.println("> Adding drone "+request.getId()+" to my topology ...");
        Drone droneToInsert = new Drone(request.getId(), request.getIp(), request.getPort());
        droneToInsert.setPosition(new Position(request.getPosition().getX(), request.getPosition().getY()));
        //adding the drone to my list is equivalent to adding the drone to my topology of the network
        myDronesList.add(droneToInsert);

        d.setDronesList(myDronesList);

        Welcome.WelcomeResponse response = Welcome.WelcomeResponse
                .newBuilder()
                .setId(d.getId())
                .setMaster(d.isMaster())
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }


    @Override
    public synchronized void election(Welcome.ElectionMessage request, StreamObserver<Welcome.ElectionResponse> responseObserver){
        d.stopMasterLifeChecker();

        if(!d.isInElection()) {
            d.removeDroneFromList(d.getMasterID());
            //set my drone in election
            d.setInElection(true);

            if (request.getBattery() < d.getBatteryLevel()) { //if my drone has major battery level

                Welcome.ElectionResponse response = Welcome.ElectionResponse
                        .newBuilder()
                        .setReceived(true)
                        .build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();

                d.sendElectionMessageToNext(d.getId(), d.getBatteryLevel());

            } else if (request.getBattery() > d.getBatteryLevel()) { //if the request has major battery level

                Welcome.ElectionResponse response = Welcome.ElectionResponse
                        .newBuilder()
                        .setReceived(true)
                        .build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();

                d.sendElectionMessageToNext(request.getId(), request.getBattery());

            } else if (request.getBattery() == d.getBatteryLevel()) { //same battery level compare the IDs

                if (request.getId() < d.getId()) {
                    
                    Welcome.ElectionResponse response = Welcome.ElectionResponse
                            .newBuilder()
                            .setReceived(true)
                            .build();
                    responseObserver.onNext(response);
                    responseObserver.onCompleted();

                    d.sendElectionMessageToNext(d.getId(), d.getBatteryLevel());

                } else if (request.getId() > d.getId()) {

                    Welcome.ElectionResponse response = Welcome.ElectionResponse
                            .newBuilder()
                            .setReceived(true)
                            .build();
                    responseObserver.onNext(response);
                    responseObserver.onCompleted();

                    d.sendElectionMessageToNext(request.getId(), request.getBattery());

                } else if (request.getId() == d.getId()) {
                    System.out.println("> New master found");

                    d.setMaster(true);
                    d.setInElection(false);
                    d.sendElectedMessageToNext(d.getId());
                }

            }
        }
    }

    @Override
    public synchronized void elected(Welcome.ElectedMessage request, StreamObserver<Welcome.ElectedResponse> responseObserver){
        System.out.println("> Master now is drone "+request.getId());

        d.setInElection(false);

        if(d.getId()!=request.getId()) {
            d.setMasterID(request.getId());
            d.setMaster(false);

            Welcome.ElectedResponse response = Welcome.ElectedResponse
                    .newBuilder()
                    .setReceived(true)
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            d.startMasterLifeChecker();
            Context context = Context.current().fork();
            context.run(() -> d.sendElectedMessageToNext(request.getId()));
        } else {
            d.setMasterID(request.getId());
            d.setMaster(true);
        }
        
    }

    @Override
    public void alive(Welcome.AliveMessage request, StreamObserver<Welcome.AliveResponse> responseObserver) {
        if(d.isMaster()){
            Welcome.AliveResponse response = Welcome.AliveResponse
                    .newBuilder()
                    .setIsAlive(true)
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }
}
