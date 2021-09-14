package modules;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType (XmlAccessType.FIELD)
public class Drones {

    @XmlElement(name="drones")
    private List<Drone> dronesList;

    private static Drones instance;

    private Drones() {
        dronesList = new ArrayList<Drone>();
    }

    //singleton
    public synchronized static Drones getInstance(){
        if(instance==null)
            instance = new Drones();
        return instance;
    }

    public synchronized List<Drone> getDronesList() {

        return new ArrayList<>(dronesList);

    }

    public synchronized void setDronesList(List<Drone> dronesList) {
        this.dronesList = dronesList;
    }

    public synchronized boolean add(Drone d){
        if(this.getById(d.getId()) == null) {
            dronesList.add(d);
            return true;
        } else {
            return false;
        }
    }


    public synchronized Drone getById(int id){
        List<Drone> dronesCopy = getDronesList();
        for(Drone d: dronesCopy)
            if(d.getId() == id)
                return d;
        return null;
    }

    public synchronized boolean deleteById(int id){
        Drone d = this.getById(id);
        if(d!=null) {
            dronesList.remove(d);
            return true;
        } else {
            return false;
        }
    }

}
