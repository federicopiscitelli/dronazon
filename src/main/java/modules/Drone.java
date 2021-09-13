package modules;

import org.codehaus.jackson.annotate.JsonIgnore;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
public class Drone {

    private String id;
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
    private String masterID;
    @JsonIgnore
    private boolean inElection;

    public Drone(){}

    public Drone(String id, String ip, int port) {
        this.id = id;
        this.ip = ip;
        this.port = port;
        this.master = false;
        dronesList = new ArrayList<Drone>();
        this.inElection = false;
    }

    public boolean isMaster() {
        return master;
    }

    public void setMaster(boolean master) {
        this.master = master;
    }

    public Drone getNext() {
        return next;
    }

    public void setNext(Drone next) {
        this.next = next;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
        this.dronesList = dronesList;
    }

    public int getBatteryLevel() {
        return batteryLevel;
    }

    public void setBatteryLevel(int batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    public String getMasterID() {
        return masterID;
    }

    public void setMasterID(String masterID) {
        this.masterID = masterID;
    }

    public boolean isInElection() {
        return inElection;
    }

    public void setInElection(boolean inElection) {
        this.inElection = inElection;
    }

    @Override
    public String toString() {
        return "modules.Drone{" +
                    "id='" + id + '\'' +
                    ", ip='" + ip + '\'' +
                    ", port=" + port +
                    ", position=" + (position == null ? "null" : position.toString()) +
                    ", dronesList=" + (dronesList == null ? "null" : dronesList) +
                '}';
    }

}
