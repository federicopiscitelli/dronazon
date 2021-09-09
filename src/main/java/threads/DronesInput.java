package threads;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/*
 *
 * Class that implements a thread to manage standard input of the drone
 *
 */

public class DronesInput extends Thread{

    public DronesInput(){}

    public void run() {
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("> Digit exit to leave the network with this drone: ");
        try {
            if(inFromUser.readLine().equals("exit")){
                System.exit(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
