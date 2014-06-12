package main.java.util;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import main.java.model.auth.UserStatus;

import javax.ws.rs.core.Response;

import java.util.List;

import static main.java.util.Entities.*;

/**
 * @author Serghei Soltan (soltan@spmsoftware.com)
 */
public class Utils {

    public static Key getUserKey(String userId){
        return KeyFactory.createKey(USER.getId(), userId);
    }

    private static Gson gson;

    public static Gson getInstance(){
        if(gson == null){
            gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .create();
        }
        return gson;
    }

    public static Response oKResponse(Object object){
        return Response.ok()
                .entity(getInstance().toJson(object))
                .build();
    }

    public static Response oKResponse(Object data, List<String> messages) throws JSONException {
        Response.ResponseBuilder entity = Response.ok();
        JSONObject response = new JSONObject();
        response.put("data",data);
        response.put("messages",messages.toString());
        return entity.entity(getInstance().toJson(response)).build();
    }

    public static Response response(Object object, Response.Status code){
        return Response
                .status(code)
                .entity(getInstance().toJson(object))
                .build();
    }

    public static Response noAuthResponse(){
        return Response
                .status(Response.Status.UNAUTHORIZED)
                .entity(getInstance().toJson(new UserStatus()))
                .build();
    }
}
