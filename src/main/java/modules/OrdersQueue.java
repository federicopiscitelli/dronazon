package modules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OrdersQueue {
    private List<Order> queue = new ArrayList<>();

    public OrdersQueue(){};

    public synchronized Order getOrder(){
        if(queue.size()>0) {
            Order orderToReturn = queue.get(0);
            queue.remove(0);
            System.out.println("> Removed from queue. Now queue is:" + Arrays.toString(queue.toArray()));
            return orderToReturn;
        } else {
            return null;
        }
    }

    public synchronized void putOrder(Order o){
        queue.add(o);
        System.out.println("> Added in queue. Now queue is:"+Arrays.toString(queue.toArray()));
    }

    public Order getNth(int index){
        return queue.get(index);
    }

    public synchronized int size(){
        return this.queue.size();
    }
}
