import modules.Position;
import org.codehaus.jackson.annotate.JsonIgnore;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
public class Drone {

    private String id;
    private String ip;
    private int port;
    @JsonIgnore
    private Position position;
    @JsonIgnore
    private List<Drone> dronesList;


    public Drone(){}

    public Drone(String id, String ip, int port) {
        this.id = id;
        this.ip = ip;
        this.port = port;
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

    @Override
    public String toString() {
        return "Drone{" +
                    "id='" + id + '\'' +
                    ", ip='" + ip + '\'' +
                    ", port=" + port +
                    ", position=" + (position == null ? "null" : position.toString()) +
                    ", dronesList=" + (dronesList == null ? "null" : dronesList) +
                '}';
    }
}
