package modules;

import com.sun.tools.corba.se.idl.constExpr.Times;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Stats {

    @XmlElement(name="stats")
    private List<Stat> statsList;
    private static Stats instance;

    private Stats() {
        statsList = new ArrayList<Stat>();
    }


    //singleton
    public synchronized static Stats getInstance(){
        if(instance==null)
            instance = new Stats();
        return instance;
    }

    public synchronized void add(Stat s){
        statsList.add(s);
    }

    public synchronized List<Stat> getLastN(int n){
        return statsList.subList(statsList.size()-n, statsList.size()-1);
    }

    public float getAvgDelivery(Timestamp t1, Timestamp t2){
        float avg = 0.0f;
        int n = 0;
        for(Stat s: statsList){
            if(t1.before(s.getTimestamp()) && t2.after(s.getTimestamp())) {
                avg += s.getDeliveryAvg();
                n++;
            }
        }
        if(n>0){
            avg /= n;
        }
        return avg;
    }

    public float getAvgDistance(Timestamp t1, Timestamp t2){
        float avg = 0.0f;
        int n = 0;
        for(Stat s: statsList){
            if(t1.before(s.getTimestamp()) && t2.after(s.getTimestamp())) {
                avg += s.getKmAvg();
                n++;
            }
        }
        if(n>0){
            avg /= n;
        }
        return avg;
    }



}
