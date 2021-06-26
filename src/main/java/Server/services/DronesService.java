package Server.services;

import Server.Drone;
import Server.modules.AddResponse;
import Server.modules.Drones;
import Server.modules.Position;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Random;

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
            Random rand = new Random();
            int x = rand.nextInt(10);
            int y = rand.nextInt(10);
            AddResponse response = new AddResponse(Drones.getInstance().getDronesList(), new Position(x,y));
            return Response.ok(response).build();
        } else {
            return Response.status(Response.Status.CONFLICT).build();
        }
    }

    //get a drone by id
    @Path("/{id}")
    @GET
    @Produces({"application/json", "application/xml"})
    public Response getById(@PathParam("id") String id){
        Drone d = Drones.getInstance().getById(id);
        if(d!=null)
            return Response.ok(d).build();
        else
            return Response.status(Response.Status.NOT_FOUND).build();
    }

    //delete a drone by id
    @Path("/{id}")
    @DELETE
    @Produces({"application/json", "application/xml"})
    public Response deleteById(@PathParam("id") String id){
        if(Drones.getInstance().deleteById(id)) {
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
