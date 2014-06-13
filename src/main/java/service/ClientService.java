package main.java.service;

import com.google.appengine.labs.repackaged.com.google.common.collect.Lists;
import main.java.model.auth.Authorization;
import main.java.model.auth.UserStatus;
import main.java.util.ContactServiceUtil;
import main.java.util.UserServiceUtil;
import main.java.util.Utils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.List;

import static main.java.util.Utils.noAuthResponse;
import static main.java.util.Utils.oKResponse;
import static main.java.util.Utils.response;

/**
 * @author Serghei Soltan (soltan@spmsoftware.com)
 */
@Path("/client")
public class ClientService {

    @Path("/findAll")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllContacts(@HeaderParam("authorization") String authorization) {
        Authorization auth = new Authorization(authorization);
        if(!ContactServiceUtil.isAuthorizedRequest(auth)){
            return noAuthResponse();
        }
        return oKResponse(ContactServiceUtil.getAllClients(auth.getEmail()));
    }

    @Path("/{id}")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getClient(@PathParam("id") Long id,@HeaderParam("authorization") String authParam) {
        Authorization auth = new Authorization(authParam);
        if(!ContactServiceUtil.isAuthorizedRequest(auth)){
            return noAuthResponse();
        }

        String response = ContactServiceUtil.getContact(auth.getEmail(), id , ContactServiceUtil.CLIENT);
        if(response == null){
            return response("Failed to find client "+id+"!", Response.Status.CONFLICT);
        }
        return oKResponse(response);
    }

    @POST
    @Path("/save")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveContact(@HeaderParam("authorization") String authParam, String contactModel) {
        Authorization auth = new Authorization(authParam);
        if(!ContactServiceUtil.isAuthorizedRequest(auth)){
            return noAuthResponse();
        }
        return oKResponse(ContactServiceUtil.saveClient(auth.getEmail(), contactModel));
    }

    @POST
    @Path("/update")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateContact(@HeaderParam("authorization") String authParam, String contactModel) {
        Authorization auth = new Authorization(authParam);
        if(!ContactServiceUtil.isAuthorizedRequest(auth)){
            return noAuthResponse();
        }
        String response = ContactServiceUtil.updateClient(auth.getEmail(), contactModel);
        if(response == null){
            return response("Failed to update!", Response.Status.CONFLICT);
        }
        return oKResponse(response);
    }

    @POST
    @Path("/remove")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateCustomer(@HeaderParam("authorization") String authParam, String deleteList) {
        Authorization auth = new Authorization(authParam);
        if(!ContactServiceUtil.isAuthorizedRequest(auth)){
            return noAuthResponse();
        }
        String response = ContactServiceUtil.deleteContacts(auth.getEmail(), deleteList, ContactServiceUtil.CLIENT);
        if(response == null){
            return response("Failed to delete!", Response.Status.CONFLICT);
        }
        return oKResponse(response);
    }

}
