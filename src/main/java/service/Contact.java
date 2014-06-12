package main.java.service;

import main.java.util.Utils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static main.java.util.Utils.oKResponse;

/**
 * @author Serghei Soltan (soltan@spmsoftware.com)
 */
@Path("/contact")
public class Contact {

    @Path("/findAll")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllCustomers(String currentUser){
        return oKResponse("");
    }

    @Path("/{id}")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCustomer(String currentUser, String data){
        return oKResponse("");
    }

    @PUT
    @Path("/save")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveCustomer(String currentUser, String data){
        return oKResponse("");
    }

    @POST
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateCustomer(String currentUser, String data){
        return oKResponse("");
    }

}
