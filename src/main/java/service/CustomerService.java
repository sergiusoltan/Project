package main.java.service;

import main.java.util.Utils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static main.java.util.Utils.oKResponse;

/**
 * @author Serghei Soltan (soltan@spmsoftware.com)
 */
@Path("/customers")
public class CustomerService {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllCustomers(){
        return oKResponse("");
    }

    @Path("/{id}")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCustomer(){
        return oKResponse("");
    }

    @PUT
    @Path("/save")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveCustomer(String data){
        return oKResponse("");
    }

    @POST
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateCustomer(String data){
        return oKResponse("");
    }

}
