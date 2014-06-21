package main.java.service;

import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import main.java.model.auth.Authorization;
import main.java.model.people.ContactModel;
import main.java.util.ContactServiceUtil;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.Collection;

import static main.java.util.ContactServiceUtil.*;
import static main.java.util.Utils.*;

/**
 * @author Serghei Soltan (soltan@spmsoftware.com)
 */
@Path("/member")
public class MemberService {

    @Path("/findAll")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllContacts(@HeaderParam("authorization") String authorization) {
        Authorization auth = new Authorization(authorization);
        if(!isAuthorizedRequest(auth)){
            return noAuthResponse();
        }
        return oKResponse(getAllMembers(auth.getEmail()));
    }

    @Path("/{id}")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getClient(@PathParam("id") Long id,@HeaderParam("authorization") String authParam) {
        Authorization auth = new Authorization(authParam);
        if(!isAuthorizedRequest(auth)){
            return noAuthResponse();
        }

        ContactModel response = getContact(auth.getEmail(), id, MEMBER);
        if(response == null){
            return response("Failed to find member "+id+"!", Response.Status.CONFLICT);
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

        return oKResponse(ContactServiceUtil.getTrimesterStatistics(auth.getEmail(), ContactServiceUtil.MEMBER));
    }

    @POST
    @Path("/save")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveContact(@HeaderParam("authorization") String authParam, String contactModel) {
        Authorization auth = new Authorization(authParam);
        if(!isAuthorizedRequest(auth)){
            return noAuthResponse();
        }
        return oKResponse(saveMember(auth.getEmail(), contactModel));
    }

    @POST
    @Path("/update")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateContact(@HeaderParam("authorization") String authParam, String contactModel) {
        Authorization auth = new Authorization(authParam);
        if(!isAuthorizedRequest(auth)){
            return noAuthResponse();
        }
        Collection response = updateMember(auth.getEmail(), memberFromString(contactModel));
        if(response == null){
            return response("Failed to update!", Response.Status.CONFLICT);
        }
        return oKResponse(response);
    }

    @Path("/url")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getClient(@HeaderParam("authorization") String authParam) {
        Authorization auth = new Authorization(authParam);
        if(!isAuthorizedRequest(auth)){
            return noAuthResponse();
        }

        BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
        String response = blobstoreService.createUploadUrl("/rest/member/update/image");
        return Response.ok().entity(response).build();
    }

    @POST
    @Path("/update/image")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile(@Context HttpServletRequest req, @HeaderParam("authorization") String authParam) {

        Authorization auth = new Authorization(authParam);
        if(!isAuthorizedRequest(auth)){
            return noAuthResponse();
        }
        Collection response = ContactServiceUtil.updateMember(auth.getEmail(), req);
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
        if(!isAuthorizedRequest(auth)){
            return noAuthResponse();
        }
        Collection response = deleteContacts(auth.getEmail(), deleteList, MEMBER);
        if(response == null){
            return response("Failed to delete!", Response.Status.CONFLICT);
        }
        return oKResponse(response);
    }

}
