package Server;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("drones")
public class DronesService {

    //return the drones list
    @GET
    @Produces({"application/json", "application/xml"})
    public Response getDronesList(){
        return Response.ok().build();
    }

}
