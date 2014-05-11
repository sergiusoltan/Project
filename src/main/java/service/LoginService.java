package main.java.service;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import main.java.model.UserStatus;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.Path;

import static main.java.model.UserProperties.*;

/**
 * User: Sergiu Soltan
 */
// The Java class will be hosted at the URI path "/helloworld"
@Path("/login")
public class LoginService {
    // The Java method will process HTTP GET requests
    @GET
    // The Java method will produce content identified by the MIME Media type "text/plain"
    @Produces("text/plain")
    public String getLoggedUser() throws JSONException {
        Gson gson = new GsonBuilder().serializeNulls().create();
        UserService userService = UserServiceFactory.getUserService();
        boolean userLoggedIn = userService.isUserLoggedIn();
        UserStatus userStatus = new UserStatus(userLoggedIn);
        if (userLoggedIn) {
            User user = userService.getCurrentUser();
            userStatus.setEmail(user.getEmail());
            userStatus.setName(user.getNickname());
            userStatus.setId(user.getUserId());
        }
        userStatus.setUserUrl(userLoggedIn
                ? userService.createLogoutURL("/login")
                : userService.createLoginURL("/"));

//        return gson.toJsonTree(userStatus, UserStatus.class).toString();

        //default user
        UserStatus defaultMock = new UserStatus(Boolean.TRUE);
        defaultMock.setName("Mock User");
        defaultMock.setEmail("mock@home.com");
        defaultMock.setUserUrl("/logout");
        defaultMock.setId("someID");
        return gson.toJsonTree(defaultMock, UserStatus.class).toString();
    }
}