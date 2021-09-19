package modules;

import MQTT.DroneSubscriber;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.codehaus.jackson.annotate.JsonIgnore;
import proto.ManagerGrpc;
import proto.Welcome;
import threads.MasterLifeChecker;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

@XmlRootElement
public class Drone {

    private int id;
    private String ip;
    private int port;
    @JsonIgnore
    private boolean master;
    @JsonIgnore
    private Position position;
    @JsonIgnore
    private List<Drone> dronesList;
    @JsonIgnore
    private int batteryLevel = 100;
    @JsonIgnore
    private Drone next;
    @JsonIgnore
    private int masterID;
    @JsonIgnore
    private transient boolean inElection;
    @JsonIgnore
    private transient boolean inDelivery;
    @JsonIgnore
    private transient MasterLifeChecker masterLifeChecker;
    @JsonIgnore
    private transient DroneSubscriber subscriberMQTT;
    @JsonIgnore
    private transient OrdersQueue ordersQueue;


    public Drone(){}

    public Drone(int id, String ip, int port) {
        this.id = id;
        this.ip = ip;
        this.port = port;
        this.master = false;
        dronesList = new ArrayList<>();
        this.inElection = false;
        this.inDelivery = false;
        ordersQueue = OrdersQueue.getInstance();
    }

    public boolean isMaster() {
        return master;
    }

    public synchronized void setMaster(boolean master) {
        this.master = master;
        //start or stop the correct thread
        if(!this.inElection && this.master){
            this.startSubscriberMQTT();
        } else if(!this.inElection && !this.master){
            this.startMasterLifeChecker();
        } else if(this.inElection) {
            this.stopMasterLifeChecker();
        }
    }

    public Drone getNext() {
        return next;
    }

    public void setNext(Drone next) {
        this.next = next;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        System.out.println(position.toString());
        this.position = position;
    }

    public List<Drone> getDronesList() {
        return dronesList;
    }

    public void setDronesList(List<Drone> dronesList) {
        this.dronesList = dronesList;
    }

    public void updateDroneInList(Drone toUpdate){
        for(int i=0;i<this.dronesList.size();i++){
            if(toUpdate.getId() == this.dronesList.get(i).getId()){
                this.dronesList.set(i, toUpdate);
            }
        }
    }

    public int getBatteryLevel() {
        return batteryLevel;
    }

    public void setBatteryLevel(int batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    public int getMasterID() {
        return masterID;
    }

    public synchronized void setMasterID(int masterID) {
        this.masterID = masterID;
    }

    public synchronized boolean isInElection() {
        return inElection;
    }

    public synchronized void setInElection(boolean inElection) {
        this.inElection = inElection;
    }

    public synchronized boolean isInDelivery() {
        return inDelivery;
    }

    public synchronized void setInDelivery(boolean inDelivery) {
        this.inDelivery = inDelivery;
    }

    public synchronized boolean removeDroneFromList(int id){
        boolean result = false;
        if(id != this.getId()) {
            List<Drone> newList = new ArrayList<>();
            for (Drone d : this.dronesList) {
                if (d.getId() == id) {
                    result = true;
                } else {
                    newList.add(d);
                }
            }
            setDronesList(newList);
        }

        return result;
    }

    public synchronized Drone findNextDrone(){
        int id = this.getId();

        List<Drone> myDronesList = this.getDronesList();
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

    @Override
    public String toString() {
        return "modules.Drone{" +
                    "id='" + id + '\'' +
                    ", ip='" + ip + '\'' +
                    ", port=" + port +
                    ", position=" + (position == null ? "null" : position.toString()) +
                '}';
    }

    public synchronized void sendElectionMessageToNext(int id, int levelBattery){
        Drone next = this.findNextDrone();
        if(next != null) {
            final ManagedChannel channel = ManagedChannelBuilder.forTarget(next.getIp()).usePlaintext(true).build();
            //creating an asynchronous stub on the channel
            System.out.println("> Drone "+this.getId() +" is sending election REQUEST to the drone "+next.getId()+" ...");

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
                    //removeDroneFromList(next.getId());
                    //TODO: if next drone is disconnected?
                    System.err.println("> Error: " + throwable.getMessage()); //Error: CANCELLED: io.grpc.Context was cancelled without error
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
        Drone next = this.findNextDrone();
        if(next != null) {
            final ManagedChannel channel = ManagedChannelBuilder.forTarget(next.getIp()).usePlaintext(true).build();
            //creating an asynchronous stub on the channel
            System.out.println("> Drone "+this.getId() +" is sending elected MESSAGE to the drone "+next.getId()+" ... Master is now "+id);

            ManagerGrpc.ManagerStub stub = ManagerGrpc.newStub(channel);

            //creating the HelloResponse object which will be provided as input to the RPC method
            Welcome.ElectedMessage request = Welcome.ElectedMessage
                    .newBuilder()
                    .setId(id)
                    .build();


            //calling the RPC method. since it is asynchronous, we need to define handlers
            stub.elected(request, new StreamObserver<Welcome.ElectedResponse>() {
                //this hanlder takes care of each item received in the stream
                public void onNext(Welcome.ElectedResponse electedResponse) { }

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

    public void startMasterLifeChecker(){
        masterLifeChecker = new MasterLifeChecker(this);
        masterLifeChecker.run();
    }

    public void stopMasterLifeChecker(){
        masterLifeChecker.stop();
    }

    public void startSubscriberMQTT(){
        subscriberMQTT = new DroneSubscriber(this);
        subscriberMQTT.run();
    }

    public void stopSubscriberMQTT(){
        subscriberMQTT.stop();
    }

    public synchronized void assignDelivery(Order order){
        boolean assigned = false;
        Drone selected = this;
        List<Drone> availableDrones = new ArrayList<>();

        for(Drone drone : this.dronesList){
            if(!drone.isInDelivery()){
                availableDrones.add(drone);
            }
        }

        if(availableDrones.size() == 0){
            ordersQueue.putOrder(order);
        } else if (availableDrones.size() == 1){
            //TODO: grpc call to assign delivery
            selected = availableDrones.get(0);
        } else {

            //Compare distances
            double distance = 100.0;

            List<Drone> nearest = new ArrayList<>();
            for(Drone drone : availableDrones){
                System.out.println(drone.toString());

                double newDistance = Math.sqrt(
                        Math.pow(drone.getPosition().getX()-order.getRetire().getX(),2) +
                        Math.pow(drone.getPosition().getY()-order.getRetire().getY(),2)
                );
                System.out.println("entry-2");
                if(newDistance < distance){
                    nearest = new ArrayList<>();
                    nearest.add(drone);
                    distance = newDistance;
                } else if (newDistance == distance){
                    nearest.add(drone);
                }
            }

            if(nearest.size() == 0){
                ordersQueue.putOrder(order);
            } else if (nearest.size() == 1){
                selected = nearest.get(0);
            } else {
                //Compare battery level
                int maxBatteryLevel = 0;
                List<Drone> highestBattery = new ArrayList<>();
                for (Drone drone : nearest){
                    if(drone.getBatteryLevel() > maxBatteryLevel){
                        highestBattery = new ArrayList<>();
                        highestBattery.add(drone);
                        maxBatteryLevel = drone.getBatteryLevel();
                    } else if(drone.getBatteryLevel() == maxBatteryLevel){
                        highestBattery.add(drone);
                    }
                }
                if(highestBattery.size() == 0){
                    ordersQueue.putOrder(order);
                } else if(highestBattery.size() == 1){
                    selected = highestBattery.get(0);
                } else {
                    //Compare ids
                    int maxId = -1;

                    for (Drone drone : highestBattery){
                        if(drone.getId()>maxId){
                            selected = drone;
                            maxId = drone.getId();
                        }
                    }
                }
            }
        }

        System.out.println("> Assigned to: "+selected.getId());

        sendAssignedDeliveryMessage(selected.getIp(), order.getRetire(),order.getDelivery());
    }

    public void sendAssignedDeliveryMessage(String ip, Position retire, Position delivery){

        final ManagedChannel channel = ManagedChannelBuilder.forTarget(ip).usePlaintext(true).build();
        //creating an asynchronous stub on the channel

        ManagerGrpc.ManagerStub stub = ManagerGrpc.newStub(channel);

        //creating the HelloResponse object which will be provided as input to the RPC method
        Welcome.DeliveryMessage request = Welcome.DeliveryMessage
                .newBuilder()
                .setRetire(Welcome.Position
                        .newBuilder()
                        .setX(retire.getX())
                        .setY(retire.getY())
                        .build())
                .setDelivery(Welcome.Position
                        .newBuilder()
                        .setX(delivery.getX())
                        .setY(delivery.getY())
                        .build())
                .build();


        //calling the RPC method. since it is asynchronous, we need to define handlers
        stub.delivery(request, new StreamObserver<Welcome.DeliveryResponse>() {
            //this hanlder takes care of each item received in the stream
            public void onNext(Welcome.DeliveryResponse deliveryResponse) {
                //each item is just printed
                if(deliveryResponse.getReceived()) {
                    System.out.println("> Ack received");
                }
            }

            //if there are some errors, this method will be called
            public void onError(Throwable throwable) {
                channel.shutdownNow();
                //removeDroneFromList(next.getId());
                //TODO: if next drone is disconnected?
                System.err.println("> Error: " + throwable.getMessage()); //Error: CANCELLED: io.grpc.Context was cancelled without error
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

    public void doDelivery(Position retire, Position delivery){
        System.out.println("> Doing the delivery .... ");
    }

}
