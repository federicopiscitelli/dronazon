package GRPC;

import io.grpc.Context;
import modules.Drone;
import modules.Position;
import proto.Welcome;
import proto.ManagerGrpc;
import io.grpc.stub.StreamObserver;

import java.util.List;

public class ManagerServiceImpl extends ManagerGrpc.ManagerImplBase {

    Drone drone;

    public ManagerServiceImpl(Drone d){
        this.drone = d;
    }


    @Override
    public void welcome(Welcome.WelcomeMessage request, StreamObserver<Welcome.WelcomeResponse> responseObserver) {

        List<Drone> myDronesList = drone.getDronesList();
        System.out.println("> Adding drone "+request.getId()+" to my topology ...");
        Drone droneToInsert = new Drone(request.getId(), request.getIp(), request.getPort());
        droneToInsert.setPosition(new Position(request.getPosition().getX(), request.getPosition().getY()));
        //adding the drone to my list is equivalent to adding the drone to my topology of the network
        myDronesList.add(droneToInsert);

        drone.setDronesList(myDronesList);

        Welcome.WelcomeResponse response = Welcome.WelcomeResponse
                .newBuilder()
                .setId(drone.getId())
                .setMaster(drone.isMaster())
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }


    @Override
    public void election(Welcome.ElectionMessage request, StreamObserver<Welcome.ElectionResponse> responseObserver){
        System.out.println("Entry");

        Welcome.ElectionResponse response = Welcome.ElectionResponse
                .newBuilder()
                .setReceived(true)
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();

        //set my drone in election
        drone.setInElection(true);

        drone.stopMasterLifeChecker();

        drone.removeDroneFromList(drone.getMasterID());

        int droneBatteryLevel = drone.getBatteryLevel();
        if(drone.isInDelivery()){
            droneBatteryLevel -= 10;
        }

        if (request.getBattery() < droneBatteryLevel) { //if my drone has major battery level
            drone.sendElectionMessageToNext(drone.getId(), droneBatteryLevel);
        } else if (request.getBattery() > droneBatteryLevel) { //if the request has major battery level
            drone.sendElectionMessageToNext(request.getId(), request.getBattery());
        } else if (request.getBattery() == droneBatteryLevel) { //same battery level compare the IDs
            if (request.getId() < drone.getId()) {
                drone.sendElectionMessageToNext(drone.getId(), droneBatteryLevel);
            } else if (request.getId() > drone.getId()) {
                drone.sendElectionMessageToNext(request.getId(), request.getBattery());
            } else if (request.getId() == drone.getId()) {
                System.out.println("> New master found");
                drone.setMaster(true);
                drone.setInElection(false);
                drone.sendElectedMessageToNext(drone.getId());
            }

        }
    }

    @Override
    public void elected(Welcome.ElectedMessage request, StreamObserver<Welcome.ElectedResponse> responseObserver){

        Welcome.ElectedResponse response = Welcome.ElectedResponse
                .newBuilder()
                .setReceived(true)
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();

        System.out.println("> Master now is drone "+request.getId());

        drone.setInElection(false);

        if(drone.getId()!=request.getId()) { //if i'm not the master
            drone.setMasterID(request.getId());
            drone.setMaster(false);
            drone.startMasterLifeChecker();
            drone.sendElectedMessageToNext(request.getId());
        } else {
            drone.setMasterID(request.getId());
            drone.setMaster(true);
        }
        
    }

    @Override
    public void alive(Welcome.AliveMessage request, StreamObserver<Welcome.AliveResponse> responseObserver) {
        if(drone.isMaster()){
            Welcome.AliveResponse response = Welcome.AliveResponse
                    .newBuilder()
                    .setIsAlive(true)
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }

    @Override
    public void delivery(Welcome.DeliveryMessage request, StreamObserver<Welcome.DeliveryResponse> responseObserver){
        Welcome.DeliveryResponse response = Welcome.DeliveryResponse
                .newBuilder()
                .setReceived(true)
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();

        Position retire = new Position(request.getRetire().getX(), request.getRetire().getY());
        Position delivery = new Position(request.getDelivery().getX(), request.getDelivery().getY());

        drone.doDelivery(retire, delivery);

    }
}
