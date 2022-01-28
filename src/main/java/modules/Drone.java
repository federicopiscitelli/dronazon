package modules;

import MQTT.DroneSubscriber;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.codehaus.jackson.annotate.JsonIgnore;
import proto.ManagerGrpc;
import proto.Welcome;
import simulators.PM10Simulator;
import threads.DeliveryThread;
import threads.ElectedThread;
import threads.ElectionThread;
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
    private transient boolean recharging;
    @JsonIgnore
    private transient MasterLifeChecker masterLifeChecker;
    @JsonIgnore
    private transient DroneSubscriber subscriberMQTT;
    @JsonIgnore
    public transient OrdersQueue ordersQueue;
    @JsonIgnore
    private transient MyBuffer myPM10Buffer;
    @JsonIgnore
    private transient PM10Simulator pm10Simulator;
    @JsonIgnore
    private transient List<Double> averagesPM10;
    @JsonIgnore
    private transient List<DeliveryStatistics> deliveryStatistics;


    public Drone(){}

    public Drone(int id, String ip, int port) {
        this.id = id;
        this.ip = ip;
        this.port = port;
        this.master = false;
        dronesList = new ArrayList<>();
        this.inElection = false;
        this.inDelivery = false;
        averagesPM10 = new ArrayList<>();
        deliveryStatistics = new ArrayList<>();
        ordersQueue = new OrdersQueue();
        next = this;
        //dronesList.add(this);
    }

    public boolean isMaster() {
        return master;
    }

    public void setMaster(boolean isMaster) {

        master = isMaster;
        //start or stop the correct thread
        if(master){
            this.startPM10Sensor();
            this.startSubscriberMQTT();
        } else {
            this.startPM10Sensor();
            this.startMasterLifeChecker();
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
        this.position = position;
    }

    public List<Drone> getDronesList() {
        return dronesList;
    }

    public void setDronesList(List<Drone> dronesList) {
        synchronized (next) {
            this.dronesList = dronesList;
            this.next = findNextDrone();
        }
    }

    public synchronized void addDroneToList(Drone d){
        synchronized (next) {
            this.dronesList.add(d);
            next = findNextDrone();
        }
    }

    public void updateDroneInList(Drone toUpdate){
        for(int i=0;i<this.dronesList.size();i++){
            if(toUpdate.getId() == this.dronesList.get(i).getId()){
                this.dronesList.set(i, toUpdate);
                break;
            }
        }
    }

    public void updateDroneInListAfterRecharge(int id, int batteryLevel, Position position){
        for(int i=0;i<this.dronesList.size();i++){
            if(id == this.dronesList.get(i).getId()){
                this.dronesList.get(i).setPosition(position);
                this.dronesList.get(i).setBatteryLevel(batteryLevel);
                break;
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

    public void setMasterID(int masterID) {
        this.masterID = masterID;
    }

    public boolean isInElection() {
        return inElection;
    }

    public void setInElection(boolean inElection) {
        this.inElection = inElection;
    }

    public boolean isInDelivery() {
        return inDelivery;
    }

    public void setInDelivery(boolean inDelivery) {
        this.inDelivery = inDelivery;
    }

    public boolean removeDroneFromList(int id){

        synchronized (next) {
            if(dronesList != null && dronesList.size()>0) {
                System.out.println("> Removing " + id + " from list");
                if (id != this.getId()) {
                    dronesList.removeIf(d -> (d.getId() == id));
                }
                next = findNextDrone();
            }
        }

        return true;
    }

    public Drone findNextDrone(){
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

        Drone next = this;
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

    public void sendElectionMessageToNext(int id, int levelBattery){
        ElectionThread electionThread = new ElectionThread(this,id,levelBattery);
        electionThread.start();
    }

    public void sendElectedMessageToNext(int id){
        ElectedThread electedThread = new ElectedThread(this, id);
        electedThread.start();
    }

    public void startMasterLifeChecker(){
        masterLifeChecker = new MasterLifeChecker(this);
        masterLifeChecker.start();
    }

    public void stopMasterLifeChecker(){
        masterLifeChecker.stopExecution();
    }

    public void startSubscriberMQTT(){
        subscriberMQTT = new DroneSubscriber(this);
        subscriberMQTT.start();
    }

    public void stopSubscriberMQTT(){
        subscriberMQTT.stopExecution();
    }



    public void doDelivery(Position retire, Position delivery){
        DeliveryThread deliveryThread = new DeliveryThread(this, retire, delivery);
        deliveryThread.start();
    }

    public boolean isRecharging(){
        return this.recharging;
    }

    public void setInCharge(boolean inCharge){
        this.recharging = inCharge;
    }

    public synchronized void addAverageToAverages(double avg){
        this.averagesPM10.add(avg);
    }

    public List<Double> getAveragesPM10(){
        return this.averagesPM10;
    }

    public void startPM10Sensor(){
        myPM10Buffer = new MyBuffer(this);
        pm10Simulator = new PM10Simulator(myPM10Buffer);
        pm10Simulator.start();
    }

    public List<DeliveryStatistics> getDeliveryStatistics(){
        return this.deliveryStatistics;
    }

    public synchronized void addDeliveryStatistic(DeliveryStatistics deliveryStatistic){
        this.deliveryStatistics.add(deliveryStatistic);
    }

}
