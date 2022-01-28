package modules;

import simulators.Buffer;
import simulators.Measurement;

import java.util.ArrayList;
import java.util.List;

public class MyBuffer implements Buffer {

    private static final int WINDOW_DIMENSION = 8;
    private static final double OVERLAP = 0.5;

    private List<Measurement> measurements;
    private Drone drone;

    public MyBuffer(Drone drone){
        measurements = new ArrayList<>();
        this.drone = drone;
        System.out.println("> Buffer inizialized ...");
    }

    @Override
    public void addMeasurement(Measurement m) {
        //if my buffer has less than WINDOW_DIMENSION elements an element can be added
        //otherwise reset the buffer with sliding windows method
        if(measurements.size()<WINDOW_DIMENSION) {
            measurements.add(m);
            //System.out.println("> Measurement added: "+m.toString()+". Now size is: "+measurements.size());
        } else {
            readAllAndClean();
        }
    }

    @Override
    public List<Measurement> readAllAndClean() {
        double sum = 0.0d;
        for(Measurement measurement :measurements){
            sum += measurement.getValue();
        }
        double avg = sum/8;
        drone.addAverageToAverages(avg);
        int overlappingElement = (int)(WINDOW_DIMENSION*OVERLAP);
        for(int i=0;i<overlappingElement;i++){
            //System.out.println("> Removing the"+i+" element");
            measurements.remove(i);
        }
        return null;
    }
}
