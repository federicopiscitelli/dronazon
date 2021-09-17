package REST.server;

import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;

public class Administrator {

    private static final String HOST = "localhost";
    private static final int PORT = 1337;

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServerFactory.create("http://"+HOST+":"+PORT+"/");
        server.start();

        System.out.println("> REST server running!");
        System.out.println("> REST server started on: http://"+HOST+":"+PORT);
        System.out.println("> Hit return to stop...");
        System.in.read();
        System.out.println("> Stopping REST server");
        server.stop(0);
        System.out.println("> REST server stopped");
    }
}
