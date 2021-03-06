package main.java.service;

import com.google.appengine.labs.repackaged.com.google.common.collect.Lists;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import main.java.model.auth.AuthModel;
import main.java.model.auth.Authorization;
import main.java.model.auth.UserStatus;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

import static com.google.appengine.labs.repackaged.com.google.common.collect.Lists.newArrayList;
import static main.java.util.UserServiceUtil.*;
import static main.java.util.Utils.*;

/**
 * User: Sergiu Soltan
 */
@Path("/auth")
public class AuthService {
    @GET
    @Path("/find")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLoggedUser(@HeaderParam("authorization") String authorization) throws JSONException {
        if (authorization == null) {
            return noAuthResponse();
        }
        Authorization auth = new Authorization(authorization);
        List<String> error = Lists.newArrayList();
        UserStatus status = findUser(auth.getEmail(), error);
        if (!error.isEmpty() || !isAuthorized(auth.getSessionToken(), status.getSessionToken())) {
            return noAuthResponse();
        }
        return response(status, Response.Status.OK);
    }

    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response loginUser(String formData) throws JSONException {
        AuthModel authModel = AuthModel.getAuthModel(formData);
        List<String> messages = newArrayList();
        UserStatus userStatus = tryLogin(authModel, messages);
        return oKResponse(userStatus, messages);
    }

    @POST
    @Path("/save")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveUser(String formData) throws JSONException {
        AuthModel authModel = AuthModel.getAuthModel(formData);
        List<String> messages = newArrayList();
        Boolean success = trySave(authModel, messages);
        return success ? oKResponse(messages) : response(messages, Response.Status.CONFLICT);
    }

    @POST
    @Path("/logout")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response logoutUser(String currentUser) throws JSONException {
        UserStatus userStatus = UserStatus.getUserStatus(currentUser);
        List<String> responses = newArrayList();
        userStatus = tryLogout(userStatus, responses);
        return oKResponse(userStatus, responses);
    }
}