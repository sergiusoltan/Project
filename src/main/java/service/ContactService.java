package main.java.service;

import main.java.model.auth.Authorization;
import main.java.util.ContactServiceUtil;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static main.java.util.Utils.*;

/**
 * @author Serghei Soltan (soltan@spmsoftware.com)
 */
@Path("/contact")
public class ContactService {

    @Path("/findAll")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllContacts(@HeaderParam("authorization") String authorization) {
        Authorization auth = new Authorization(authorization);
        if(!ContactServiceUtil.isAuthorizedRequest(auth)){
            return noAuthResponse();
        }
        return oKResponse(getString(ContactServiceUtil.getAllContacts(auth.getEmail())));
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

        String response = ContactServiceUtil.getContact(auth.getEmail(), id , ContactServiceUtil.CONTACT);
        if(response == null){
            return response("Failed to find contact "+id+"!", Response.Status.CONFLICT);
        }
        return oKResponse(response);
    }

    @Path("/trimester")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTrimester(@HeaderParam("authorization") String authParam) {
        Authorization auth = new Authorization(authParam);
        if(!ContactServiceUtil.isAuthorizedRequest(auth)){
            return noAuthResponse();
        }

        return oKResponse(ContactServiceUtil.getTrimesterStatistics(auth.getEmail(), ContactServiceUtil.CONTACT));
    }

    @POST
    @Path("/save")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveContact(@HeaderParam("authorization") String authParam, String contactModel) {
        Authorization auth = new Authorization(authParam);
        if(!ContactServiceUtil.isAuthorizedRequest(auth)){
            return noAuthResponse();
        }
        return oKResponse(ContactServiceUtil.saveContacts(auth.getEmail(), contactModel));
    }

    @POST
    @Path("/update")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateContact(@HeaderParam("authorization") String authParam, String contactModel) {
        Authorization auth = new Authorization(authParam);
        if(!ContactServiceUtil.isAuthorizedRequest(auth)){
            return noAuthResponse();
        }
        String response = ContactServiceUtil.updateContacts(auth.getEmail(), contactModel);
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
        String response = ContactServiceUtil.deleteContacts(auth.getEmail(), deleteList, ContactServiceUtil.CONTACT);
        if(response == null){
            return response("Failed to delete!", Response.Status.CONFLICT);
        }
        return oKResponse(response);
    }

}
