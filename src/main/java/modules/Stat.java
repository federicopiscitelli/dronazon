package modules;

import java.sql.Timestamp;

public class Stat {
    private Timestamp timestamp;
    private double deliveryAvg;
    private double kmAvg;
    private double pollutionAvg;
    private double batteryAvg;

    public Stat(){}

    public Stat(Timestamp timestamp, double deliveryAvg, double kmAvg, double pollutionAvg, double batteryAvg) {
        this.timestamp = timestamp;
        this.deliveryAvg = deliveryAvg;
        this.kmAvg = kmAvg;
        this.pollutionAvg = pollutionAvg;
        this.batteryAvg = batteryAvg;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public double getDeliveryAvg() {
        return deliveryAvg;
    }

    public void setDeliveryAvg(double deliveryAvg) {
        this.deliveryAvg = deliveryAvg;
    }

    public double getKmAvg() {
        return kmAvg;
    }

    public void setKmAvg(double kmAvg) {
        this.kmAvg = kmAvg;
    }

    public double getPollutionAvg() {
        return pollutionAvg;
    }

    public void setPollutionAvg(double pollutionAvg) {
        this.pollutionAvg = pollutionAvg;
    }

    public double getBatteryAvg() {
        return batteryAvg;
    }

    public void setBatteryAvg(double batteryAvg) {
        this.batteryAvg = batteryAvg;
    }

    @Override
    public String toString() {
        return "Stat{" +
                "timestamp=" + timestamp +
                ", deliveryAvg=" + deliveryAvg +
                ", kmAvg=" + kmAvg +
                ", pollutionAvg=" + pollutionAvg +
                ", batteryAvg=" + batteryAvg +
                '}';
    }
}
