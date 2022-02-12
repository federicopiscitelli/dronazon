package modules;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.sql.Timestamp;
import java.text.ParseException;

@XmlRootElement
public class Timestamps {
    @XmlElement public Timestamp timestamp1;
    @XmlElement public Timestamp timestamp2;

    public Timestamps(String timestamp1, String timestamp2){
        this.timestamp1 = Timestamp.valueOf(timestamp1);
        this.timestamp2 = Timestamp.valueOf(timestamp2);
    }

    public Timestamps() {
    }
}
