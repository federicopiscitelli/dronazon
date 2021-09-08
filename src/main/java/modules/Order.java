package modules;

public class Order {
    private int id;
    private Position retire;
    private Position delivery;

    public Order(int id, Position retire, Position delivery) {
        this.id = id;
        this.retire = retire;
        this.delivery = delivery;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Position getRetire() {
        return retire;
    }

    public void setRetire(Position retire) {
        this.retire = retire;
    }

    public Position getDelivery() {
        return delivery;
    }

    public void setDelivery(Position delivery) {
        this.delivery = delivery;
    }
}
