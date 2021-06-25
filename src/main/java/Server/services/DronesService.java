package Server.services;

import Server.beans.Drone;
import Server.beans.Drones;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("drones")
public class DronesService {

    //return the drones list
    @GET
    @Produces({"application/json", "application/xml"})
    public Response getDronesList(){
        return Response.ok(Drones.getInstance()).build();
    }

    //insert a drone
    @Path("add")
    @POST
    @Consumes({"application/json", "application/xml"})
    public Response addDrone(Drone d){
        Drones.getInstance().add(d);
        return Response.ok().build();
    }


}
