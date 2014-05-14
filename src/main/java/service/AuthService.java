package main.java.service;

import com.google.appengine.api.users.User;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import main.java.model.AuthModel;
import main.java.model.UserStatus;
import main.java.util.UserServiceUtil;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.List;

import static com.google.appengine.labs.repackaged.com.google.common.collect.Lists.newArrayList;
import static main.java.util.Utils.*;

/**
 * User: Sergiu Soltan
 */
@Path("/auth")
public class AuthService {
    @GET
    @Path("/find")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLoggedUser() throws JSONException {
        Boolean isLogged = UserServiceUtil.isLoggedUser();
        UserStatus userStatus = new UserStatus(isLogged);
        if (isLogged) {
            User user = UserServiceUtil.getCurrentUser();
            userStatus.setEmail(user.getEmail());
            userStatus.setName(user.getNickname());
            userStatus.setId(user.getUserId());
        }
        userStatus.setUserUrl(UserServiceUtil.getUserUrl(isLogged));
        String responseEntity = getInstance().toJsonTree(userStatus, UserStatus.class).toString();
        return oKResponse(responseEntity);
    }

    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response loginUser(String formData) throws JSONException {
        AuthModel authModel = AuthModel.getAuthModel(formData);
        List<String> messages = newArrayList();
        UserStatus userStatus = UserServiceUtil.tryLogin(authModel, messages);
        return oKResponse(userStatus, messages);
    }

    @POST
    @Path("/save")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveUser(String formData) throws JSONException {
        AuthModel authModel = AuthModel.getAuthModel(formData);
        List<String> messages = newArrayList();
        UserServiceUtil.trySave(authModel, messages);
        return oKResponse(messages);
    }

    @POST
    @Path("/logout")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response logoutUser(String currentUser) throws JSONException {
        UserStatus userStatus = UserStatus.getUserStatus(currentUser);
        List<String> responses = newArrayList();
        userStatus = UserServiceUtil.tryLogout(userStatus, responses);
        return oKResponse(userStatus, responses);
    }
}