package threads;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import modules.Drone;
import modules.Position;
import proto.ManagerGrpc;
import proto.Welcome;

public class RechargeLockServer {

    Object lock;

    public RechargeLockServer(){
        lock = new Object();
    }

    public void block() {
        synchronized (lock) {
            try {
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void wakeUp(){
        synchronized (lock) {
            lock.notifyAll();
        }
    }
}
