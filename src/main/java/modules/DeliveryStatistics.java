package modules;

public class DeliveryStatistics {
    private int idDrone;
    private double km;
    private double pollutionAvg;
    private int battery;

    public DeliveryStatistics(int idDrone, double km, double pollutionAvg, int battery) {
        this.idDrone = idDrone;
        this.km = km;
        this.pollutionAvg = pollutionAvg;
        this.battery = battery;
    }

    public int getIdDrone() {
        return idDrone;
    }

    public void setIdDrone(int idDrone) {
        this.idDrone = idDrone;
    }

    public double getKm() {
        return km;
    }

    public void setKm(double km) {
        this.km = km;
    }

    public double getPollutionAvg() {
        return pollutionAvg;
    }

    public void setPollutionAvg(double pollutionAvg) {
        this.pollutionAvg = pollutionAvg;
    }

    public int getBattery() {
        return battery;
    }

    public void setBattery(int battery) {
        this.battery = battery;
    }

    @Override
    public String toString() {
        return "DeliveryStatistics{" +
                "idDrone=" + idDrone +
                ", km=" + km +
                ", pollutionAvg=" + pollutionAvg +
                ", battery=" + battery +
                '}';
    }
}
