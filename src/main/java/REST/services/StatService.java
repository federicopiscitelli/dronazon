package REST.services;


import modules.*;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.sql.Timestamp;

@Path("stats")
public class StatService {

    //insert a stat
    @Path("add")
    @POST
    @Consumes({"application/json", "application/xml"})
    public void addStat(Stat s){
        Stats.getInstance().add(s);
    }

    //get last n stats
    @Path("/{n}")
    @GET
    @Produces({"application/json", "application/xml"})
    public Response getById(@PathParam("n") int n){
        if(n>0)
            return Response.ok(Stats.getInstance().getLastN(n)).build();
        else
            return Response.status(Response.Status.BAD_REQUEST).build();
    }

    //get average delivery
    @Path("avg/delivery")
    @POST
    @Consumes({"application/json", "application/xml"})
    @Produces({"application/json", "application/xml"})
    public Response avgDelivery(Timestamps ts){
        Timestamp t1 = ts.timestamp1;
        Timestamp t2 = ts.timestamp2;
        //System.out.println(t1+" "+t2);
        if(t1 != null && t2 != null){
            return Response.ok(Stats.getInstance().getAvgDelivery(t1,t2)).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

    }

    //get average distance
    @Path("avg/distance")
    @POST
    @Consumes({"application/json", "application/xml"})
    @Produces({"application/json", "application/xml"})
    public Response avgDistance(Timestamps ts){
        Timestamp t1 = ts.timestamp1;
        Timestamp t2 = ts.timestamp2;
        if(t1 != null && t2 != null){
            return Response.ok(Stats.getInstance().getAvgDistance(t1,t2)).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }



}
