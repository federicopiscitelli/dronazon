package GRPC;

import modules.DeliveryStatistics;
import modules.Drone;
import modules.Position;
import proto.Welcome;
import proto.ManagerGrpc;
import io.grpc.stub.StreamObserver;
import threads.*;

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
                electionThread.start();
        } else if (request.getBattery() < droneBatteryLevel && !drone.isInElection()) { //if drone has major battery level
                drone.setInElection(true);
                System.err.println("2) " + request.getBattery() + " < " + droneBatteryLevel);
                ElectionThread electionThread = new ElectionThread(drone, drone.getId(), droneBatteryLevel);
                electionThread.start();
        } else if (request.getBattery() == droneBatteryLevel) { //same battery level compare the IDs
                System.err.println("3) " + request.getBattery() + " == " + droneBatteryLevel);
                if (request.getId() > drone.getId()) {
                        drone.setInElection(true);
                        System.err.println("4) " + request.getId() + " " + drone.getId());
                        ElectionThread electionThread = new ElectionThread(drone, request.getId(), request.getBattery());
                        electionThread.start();
                } else if (request.getId() < drone.getId() && !drone.isInElection()) {
                        drone.setInElection(true);
                        System.err.println("5) " + request.getId() + " " + drone.getId());
                        ElectionThread electionThread = new ElectionThread(drone, drone.getId(), droneBatteryLevel);
                        electionThread.start();
                } else if (request.getId() == drone.getId()) { //test drone.isInElection()
                        drone.setMaster(true);
                        drone.setInElection(false);
                        System.err.println("6) " + request.getId() + " " + drone.getId());
                        System.out.println("> I'M THE NEW MASTER");
                        ElectedThread electedThread = new ElectedThread(drone,drone.getId());
                        electedThread.start();
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
            electedThread.start();
            UpdatePosition up = new UpdatePosition(drone);
            up.start();
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

        //drone.doDelivery(retire, delivery);
        DeliveryThread dt = new DeliveryThread(drone,retire,delivery);
        dt.start();
    }

    @Override
    public void delivered(Welcome.DeliveredMessage request, StreamObserver<Welcome.DeliveredResponse> responseObserver){
        if(drone.isMaster()){

            Welcome.DeliveredResponse response = Welcome.DeliveredResponse
                    .newBuilder()
                    .setReceived(true)
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();

            DeliveryStatistics ds = new DeliveryStatistics(request.getId(),request.getKm(),
                                            request.getAvgPollution(), request.getBattery());
            drone.addDeliveryStatistic(ds);
            drone.updateDroneInListAfterDelivery(request.getId(),
                                                    new Position(request.getNewPosition().getX(),
                                                                     request.getNewPosition().getY()),
                                                    request.getBattery());

            //System.out.println(drone.getDeliveryStatistics().toString());
        }
    }

    @Override
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

    @Override
    public void rechargeCompleted(Welcome.RechargeComplete request, StreamObserver<Welcome.RechargeCompleteResponse> responseObserver){
        if(drone.isMaster()){
            drone.updateDroneInListAfterRecharge(request.getId(),
                                                    request.getBatteryLevel(),
                                                    new Position(request.getNewPosition().getX(), request.getNewPosition().getY()));

            System.out.println("> Drone "+request.getId()+" ended recharging and I updated its position");
            Welcome.RechargeCompleteResponse response  = Welcome.RechargeCompleteResponse
                    .newBuilder()
                    .setReceived(true)
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }

    @Override
    public void unavailableDrone(Welcome.UnavailableDroneMessage request, StreamObserver<Welcome.UnavailableDroneResponse> responseObserver){
        Welcome.UnavailableDroneResponse response = Welcome.UnavailableDroneResponse
                .newBuilder()
                .setReceived(true)
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();

        if(drone.getId()!=request.getStartingDroneID()) {
            drone.removeDroneFromList(request.getId());
            DroneUnavailable du = new DroneUnavailable(drone, request.getId(), request.getStartingDroneID());
            du.start();
        } else {
            System.out.println("> All drones have deleted "+request.getId()+" from their local network");
        }
    }

    @Override
    public void sendPosition(Welcome.PositionMessage request, StreamObserver<Welcome.PositionResponse> responseObserver){
        if(drone.isMaster()){
            Position p = new Position(request.getPosition().getX(), request.getPosition().getY());
            drone.updateDroneInListAfterElection(request.getId(), p);
            System.out.println("> Updating position of "+request.getId());
        }

        Welcome.PositionResponse response = Welcome.PositionResponse
                .newBuilder()
                .setReceived(true)
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

}
