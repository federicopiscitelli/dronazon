package Server.services;

import Server.Drone;
import Server.beans.Drones;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

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
    @Produces({"application/json", "application/xml"})
    public Response addDrone(Drone d){
        if(Drones.getInstance().add(d)){
            return Response.ok(Drones.getInstance()).build();
        } else {
            return Response.status(Response.Status.CONFLICT).build();
        }
    }

    //get a drone by id
    @Path("get/{id}")
    @GET
    @Produces({"application/json", "application/xml"})
    public Response getById(@PathParam("id") String id){
        Drone d = Drones.getInstance().getById(id);
        if(d!=null)
            return Response.ok(d).build();
        else
            return Response.status(Response.Status.NOT_FOUND).build();
    }



}
