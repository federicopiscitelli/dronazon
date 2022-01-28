package modules;

import java.util.ArrayList;
import java.util.List;

public class OrdersQueue {
    private List<Order> queue = new ArrayList<>();

    public OrdersQueue(){};

    public synchronized Order getOrder(){
        Order orderToReturn = queue.get(0);
        queue.remove(0);
        return orderToReturn;
    }

    public synchronized void putOrder(Order o){
        queue.add(o);
    }

    public synchronized int size(){
        return this.queue.size();
    }
}
