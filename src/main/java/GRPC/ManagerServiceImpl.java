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
    public synchronized void election(Welcome.ElectionMessage request, StreamObserver<Welcome.ElectionResponse> responseObserver){
        drone.stopMasterLifeChecker();

        if(!drone.isInElection()) {
            drone.removeDroneFromList(drone.getMasterID());
            //set my drone in election
            drone.setInElection(true);

            if (request.getBattery() < drone.getBatteryLevel()) { //if my drone has major battery level

                Welcome.ElectionResponse response = Welcome.ElectionResponse
                        .newBuilder()
                        .setReceived(true)
                        .build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();

                drone.sendElectionMessageToNext(drone.getId(), drone.getBatteryLevel());

            } else if (request.getBattery() > drone.getBatteryLevel()) { //if the request has major battery level

                Welcome.ElectionResponse response = Welcome.ElectionResponse
                        .newBuilder()
                        .setReceived(true)
                        .build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();

                drone.sendElectionMessageToNext(request.getId(), request.getBattery());

            } else if (request.getBattery() == drone.getBatteryLevel()) { //same battery level compare the IDs

                if (request.getId() < drone.getId()) {
                    
                    Welcome.ElectionResponse response = Welcome.ElectionResponse
                            .newBuilder()
                            .setReceived(true)
                            .build();
                    responseObserver.onNext(response);
                    responseObserver.onCompleted();

                    drone.sendElectionMessageToNext(drone.getId(), drone.getBatteryLevel());

                } else if (request.getId() > drone.getId()) {

                    Welcome.ElectionResponse response = Welcome.ElectionResponse
                            .newBuilder()
                            .setReceived(true)
                            .build();
                    responseObserver.onNext(response);
                    responseObserver.onCompleted();

                    drone.sendElectionMessageToNext(request.getId(), request.getBattery());

                } else if (request.getId() == drone.getId()) {
                    System.out.println("> New master found");

                    drone.setMaster(true);
                    drone.setInElection(false);
                    drone.sendElectedMessageToNext(drone.getId());
                }

            }
        }
    }

    @Override
    public synchronized void elected(Welcome.ElectedMessage request, StreamObserver<Welcome.ElectedResponse> responseObserver){
        System.out.println("> Master now is drone "+request.getId());

        drone.setInElection(false);

        if(drone.getId()!=request.getId()) {
            drone.setMasterID(request.getId());
            drone.setMaster(false);

            Welcome.ElectedResponse response = Welcome.ElectedResponse
                    .newBuilder()
                    .setReceived(true)
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            drone.startMasterLifeChecker();
            Context context = Context.current().fork();
            context.run(() -> drone.sendElectedMessageToNext(request.getId()));
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
