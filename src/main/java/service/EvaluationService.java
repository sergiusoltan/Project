package main.java.service;

import main.java.model.auth.Authorization;
import main.java.model.people.ContactModel;
import main.java.model.people.EvaluationModel;
import main.java.util.ContactServiceUtil;
import main.java.util.EvaluationServiceUtil;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.Collection;

import static main.java.util.Utils.noAuthResponse;
import static main.java.util.Utils.oKResponse;
import static main.java.util.Utils.response;

/**
 * User: Sergiu Soltan
 */
@Path("/evaluation")
public class EvaluationService {

    @Path("/findAll/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllContacts(@HeaderParam("authorization") String authorization, @PathParam("id") Long id) {
        Authorization auth = new Authorization(authorization);
        if(!ContactServiceUtil.isAuthorizedRequest(auth)){
            return noAuthResponse();
        }
        return oKResponse(EvaluationServiceUtil.getAllEvaluations(auth.getEmail(), id));
    }

    @Path("/save/{id}")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getClient(@PathParam("id") Long id,@HeaderParam("authorization") String authParam, String evaluationModel) {
        Authorization auth = new Authorization(authParam);
        if(!ContactServiceUtil.isAuthorizedRequest(auth)){
            return noAuthResponse();
        }

        Collection<EvaluationModel> response = EvaluationServiceUtil.saveEvaluation(auth.getEmail(), id, evaluationModel);
        if(response == null){
            return response("Failed save evaluation for "+id+"!", Response.Status.CONFLICT);
        }
        return oKResponse(response);
    }
}
