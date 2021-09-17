package modules;

import java.util.ArrayList;
import java.util.List;

public class OrdersQueue {
    private static OrdersQueue instance = null;
    private List<Order> queue = new ArrayList<>();

    private OrdersQueue(){};

    public static OrdersQueue getInstance()
    {
        if (instance == null)
            instance = new OrdersQueue();

        return instance;
    }

    public synchronized Order getOrder(){
        Order orderToReturn = queue.get(0);
        queue.remove(0);

        return orderToReturn;
    }

    public synchronized void putOrder(Order o){
        queue.add(o);
    }
}
