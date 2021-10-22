package GRPC;

import io.grpc.Context;
import modules.Drone;
import modules.Position;
import proto.Welcome;
import proto.ManagerGrpc;
import io.grpc.stub.StreamObserver;
import threads.ElectedThread;
import threads.ElectionThread;

import java.util.List;

public class ManagerServiceImpl extends ManagerGrpc.ManagerImplBase {

    Drone drone;

    public ManagerServiceImpl(Drone d){
        this.drone = d;
    }


    @Override
    public void welcome(Welcome.WelcomeMessage request, StreamObserver<Welcome.WelcomeResponse> responseObserver) {

        Welcome.WelcomeResponse response = Welcome.WelcomeResponse
                .newBuilder()
                .setId(drone.getId())
                .setMaster(drone.isMaster())
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();

        System.out.println("> Adding drone "+request.getId()+" to my topology ...");
        Drone droneToInsert = new Drone(request.getId(), request.getIp(), request.getPort());
        droneToInsert.setPosition(new Position(request.getPosition().getX(), request.getPosition().getY()));

        //adding the drone to my list is equivalent to adding the drone to my topology of the network
        drone.addDroneToList(droneToInsert);
    }


    @Override
    public void election(Welcome.ElectionMessage request, StreamObserver<Welcome.ElectionResponse> responseObserver){

        Welcome.ElectionResponse response = Welcome.ElectionResponse
                .newBuilder()
                .setReceived(true)
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();

        drone.stopMasterLifeChecker();

        int droneBatteryLevel = drone.getBatteryLevel();
        if(drone.isInDelivery()){
            droneBatteryLevel -= 10;
        }

        if (request.getBattery() > droneBatteryLevel) {  //if request has major battery level
                drone.setInElection(true);
                System.err.println("1) " + request.getBattery() + " > " + droneBatteryLevel);
                ElectionThread electionThread = new ElectionThread(drone, request.getId(), request.getBattery());
                electionThread.run();
        } else if (request.getBattery() < droneBatteryLevel && !drone.isInElection()) { //if drone has major battery level
                drone.setInElection(true);
                System.err.println("2) " + request.getBattery() + " < " + droneBatteryLevel);
                ElectionThread electionThread = new ElectionThread(drone, drone.getId(), droneBatteryLevel);
                electionThread.run();
        } else if (request.getBattery() == droneBatteryLevel) { //same battery level compare the IDs
                System.err.println("3) " + request.getBattery() + " == " + droneBatteryLevel);
                if (request.getId() > drone.getId()) {
                        drone.setInElection(true);
                        System.err.println("4) " + request.getId() + " " + drone.getId());
                        ElectionThread electionThread = new ElectionThread(drone, request.getId(), request.getBattery());
                        electionThread.run();
                } else if (request.getId() < drone.getId() && !drone.isInElection()) {
                        drone.setInElection(true);
                        System.err.println("5) " + request.getId() + " " + drone.getId());
                        ElectionThread electionThread = new ElectionThread(drone, drone.getId(), droneBatteryLevel);
                        electionThread.run();
                } else if (request.getId() == drone.getId()) { //test drone.isInElection()
                        drone.setMaster(true);
                        drone.setInElection(false);
                        System.err.println("6) " + request.getId() + " " + drone.getId());
                        System.out.println("> I'M THE NEW MASTER");
                        ElectedThread electedThread = new ElectedThread(drone,drone.getId());
                        electedThread.run();
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


        if(drone.getId() != request.getId() && drone.isInElection()) { //if i'm not the master
            drone.setInElection(false);
            drone.setMasterID(request.getId());
            System.err.println("> Master id in the request is: "+request.getId());
            drone.setMaster(false);
            ElectedThread electedThread = new ElectedThread(drone,request.getId());
            electedThread.run();
        } else {
            System.out.println("> Election ended");
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

    public void recharge(Welcome.RechargeRequest request, StreamObserver<Welcome.RechargeResponse> responseObserver){
        if(!drone.isRecharging()){
            Welcome.RechargeResponse response = Welcome.RechargeResponse
                    .newBuilder()
                    .setFree(true)
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }
}
