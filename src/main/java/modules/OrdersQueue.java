package modules;

import java.util.ArrayList;
import java.util.List;

public class OrdersQueue {
    private List<Order> queue = new ArrayList<>();

    public OrdersQueue(){};

    public synchronized Order getOrder(){
        Order orderToReturn = queue.get(0);
        queue.remove(0);
        System.out.println("> Removed from queue. Now queue is:"+queue.toString());
        return orderToReturn;
    }

    public synchronized void putOrder(Order o){
        queue.add(o);
        System.out.println("> Added in queue. Now queue is:"+queue.toString());
    }

    public synchronized int size(){
        return this.queue.size();
    }
}
