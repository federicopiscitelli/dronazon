package Server.beans;

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

    public synchronized void add(Drone u){
        //if()
        dronesList.add(u);
    }


    public Drone getById(String id){
        List<Drone> dronesCopy = getDronesList();
        for(Drone d: dronesCopy)
            if(d.getId().toLowerCase().equals(id.toLowerCase()))
                return d;
        return null;
    }

}
