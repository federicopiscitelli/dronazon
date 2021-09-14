package GRPC;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import modules.Drone;
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
    public void election(Welcome.ElectionMessage request, StreamObserver<Welcome.ElectionResponse> responseObserver){
        System.out.println("> Drone "+d.getId()+" received a election message from drone "+request.getId());

        if(request.getBattery() < d.getBatteryLevel()){ //if my drone has major battery level

            d.setInElection(true); //set my drone in election

            Welcome.ElectionResponse response = Welcome.ElectionResponse
                    .newBuilder()
                    .setReceived(true)
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();

            sendElectionMessageToNext(d.getId(), d.getBatteryLevel());

        } else if(request.getBattery() > d.getBatteryLevel()){ //if the request has major battery level

            d.setInElection(true); //set my drone in election
            Welcome.ElectionResponse response = Welcome.ElectionResponse
                    .newBuilder()
                    .setReceived(true)
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();

            sendElectionMessageToNext(request.getId(), request.getBattery());

        } else if (request.getBattery() == d.getBatteryLevel()){ //same battery level compare the IDs

            if(request.getId() < d.getId()){

                d.setInElection(true); //set my drone in election
                Welcome.ElectionResponse response = Welcome.ElectionResponse
                        .newBuilder()
                        .setReceived(true)
                        .build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();

                sendElectionMessageToNext(d.getId(), d.getBatteryLevel());

            } else if(request.getId() > d.getId()){

                d.setInElection(true); //set my drone in election
                Welcome.ElectionResponse response = Welcome.ElectionResponse
                        .newBuilder()
                        .setReceived(true)
                        .build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();

                sendElectionMessageToNext(request.getId(), request.getBattery());

            } else if (request.getId() == d.getId()){
                d.setInElection(false);
                d.setMaster(true);

                sendElectedMessageToNext(d.getId());
            }

        }
    }

    @Override
    public void elected(Welcome.ElectedMessage request, StreamObserver<Welcome.ElectedResponse> responseObserver){
        System.out.println("> Drone "+d.getId()+" received a elected message. Master now is drone "+request.getId());

        d.setInElection(false);
        d.setMasterID(request.getId());
        d.setMaster(false);

        Welcome.ElectedResponse response = Welcome.ElectedResponse
                .newBuilder()
                .setReceived(true)
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();

        if(d.getId()!=request.getId())
            sendElectedMessageToNext(request.getId());
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

    public Drone findNextDrone(){
        int id = d.getId();

        List<Drone> myDronesList = d.getDronesList();
        List<Integer> idList = new ArrayList<>();

        myDronesList.sort(new Comparator<Drone>() {
            @Override
            public int compare(Drone o1, Drone o2) {
                if(o1.getId() > o2.getId()){
                    return 1;
                }
                if(o1.getId() < o2.getId()){
                    return -1;
                }
                return 0;
            }
        });

        for (Drone currentDrone : myDronesList) {
            idList.add(currentDrone.getId());
        }

        Drone next = null;
        for(int i = 0; i < idList.size(); i++){
            if(idList.get(i) == id){
                if((i+1)<idList.size()){
                    next = myDronesList.get(i+1);
                } else {
                    next = myDronesList.get(0);
                }
            }
        }

        return next;

    }

    public void sendElectionMessageToNext(int id, int levelBattery){
        Drone next = findNextDrone();
        if(next != null) {
            final ManagedChannel channel = ManagedChannelBuilder.forTarget(next.getIp()).usePlaintext(true).build();
            //creating an asynchronous stub on the channel

            System.out.println("> Drone "+d.getId() +" is sending election request to the drone "+next.getId()+" ...");

            ManagerGrpc.ManagerStub stub = ManagerGrpc.newStub(channel);

            //creating the HelloResponse object which will be provided as input to the RPC method
            Welcome.ElectionMessage request = Welcome.ElectionMessage
                    .newBuilder()
                    .setId(id)
                    .setBattery(levelBattery)
                    .build();


            //calling the RPC method. since it is asynchronous, we need to define handlers
            stub.election(request, new StreamObserver<Welcome.ElectionResponse>() {
                //this hanlder takes care of each item received in the stream
                public void onNext(Welcome.ElectionResponse electionResponse) {
                    //each item is just printed
                    if(electionResponse.getReceived())
                        System.out.println("> Ack received");
                }

                //if there are some errors, this method will be called
                public void onError(Throwable throwable) {
                    channel.shutdownNow();
                    System.out.println("> Error: " + throwable.getMessage());
                }

                //when the stream is completed (the server called "onCompleted") just close the channel
                public void onCompleted() {
                    channel.shutdownNow();
                }
            });

            //you need this. otherwise the method will terminate before that answers from the server are received
            try {
                channel.awaitTermination(10, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendElectedMessageToNext(int id){
        Drone next = findNextDrone();
        if(next != null) {
            final ManagedChannel channel = ManagedChannelBuilder.forTarget(next.getIp()).usePlaintext(true).build();
            //creating an asynchronous stub on the channel
            System.out.println("> Drone "+d.getId() +" is sending elected message to the drone "+next.getId()+" ... Master is "+id);

            ManagerGrpc.ManagerStub stub = ManagerGrpc.newStub(channel);

            //creating the HelloResponse object which will be provided as input to the RPC method
            Welcome.ElectedMessage request = Welcome.ElectedMessage
                    .newBuilder()
                    .setId(id)
                    .build();


            //calling the RPC method. since it is asynchronous, we need to define handlers
            stub.elected(request, new StreamObserver<Welcome.ElectedResponse>() {
                //this hanlder takes care of each item received in the stream
                public void onNext(Welcome.ElectedResponse electedResponse) {
                    //each item is just printed
                }

                //if there are some errors, this method will be called
                public void onError(Throwable throwable) {
                    channel.shutdownNow();
                    System.out.println("> Error: " + throwable.getMessage());
                }

                //when the stream is completed (the server called "onCompleted") just close the channel
                public void onCompleted() {
                    channel.shutdownNow();
                }
            });

            //you need this. otherwise the method will terminate before that answers from the server are received
            try {
                channel.awaitTermination(10, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
