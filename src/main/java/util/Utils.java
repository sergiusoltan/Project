package main.java.util;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.labs.repackaged.com.google.common.base.Function;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import main.java.model.auth.UserStatus;
import main.java.model.people.ClientModel;
import main.java.model.people.ContactModel;
import main.java.model.people.MemberModel;

import javax.ws.rs.core.Response;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static main.java.util.Entities.*;
import static main.java.util.PeopleProperties.*;
import static main.java.util.PeopleProperties.RECOMENDED_BY;
import static main.java.util.PeopleProperties.TYPE;

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

    public static List<Long> fromListType(String list){
        Type listType = new TypeToken<List<Long>>(){}.getType();
        try{
            return getInstance().fromJson(list, listType);
        }catch (JsonSyntaxException ignore){
            return null;
        }
    }

    public static ClientModel clientFromString(String clientModel){
        ClientModel contact = getInstance().fromJson(clientModel, ClientModel.class);
        ClientModel recomendedBy = null;
        if(contact.getRecomendedBy() != null){
            recomendedBy = getInstance().fromJson(contact.getRecomendedBy(),ClientModel.class);
            recomendedBy.setRecomendedBy(null);
            contact.setRecomendedById(recomendedBy.getId());
            contact.setRecomendedBy(getInstance().toJson(recomendedBy));
        }
        return contact;
    }

    public static MemberModel memberFromString(String clientModel){
        MemberModel contact = getInstance().fromJson(clientModel, MemberModel.class);
        MemberModel recomendedBy = null;
        if(contact.getRecomendedBy() != null){
            recomendedBy = getInstance().fromJson(contact.getRecomendedBy(),MemberModel.class);
            recomendedBy.setRecomendedBy(null);
            contact.setRecomendedById(recomendedBy.getId());
            contact.setRecomendedBy(getInstance().toJson(recomendedBy));
        }
        return contact;
    }

    public static ContactModel contactFromString(String clientModel){
        ContactModel contact = getInstance().fromJson(clientModel, ContactModel.class);
        ContactModel recomendedBy = null;
        if(contact.getRecomendedBy() != null){
            recomendedBy = getInstance().fromJson(contact.getRecomendedBy(),ContactModel.class);
            recomendedBy.setRecomendedBy(null);
            contact.setRecomendedById(recomendedBy.getId());
            contact.setRecomendedBy(getInstance().toJson(recomendedBy));
        }
        return contact;
    }

    public static String parseDate(String inputDate){
        String date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(inputDate).toString();
        } catch (ParseException ignore) {}
        return date;
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

    public static Function<Entity, ContactModel> entityToContact = new Function<Entity, ContactModel>() {
        @Override
        public ContactModel apply(com.google.appengine.api.datastore.Entity entity) {
            ContactModel contactModel = new ContactModel();
            contactModel.setId((Long) entity.getProperty(ID.getKey()));
            contactModel.setName((String) entity.getProperty(NAME.getKey()));
            contactModel.setDate((String) entity.getProperty(DATE.getKey()));
            contactModel.setPhone((Long) entity.getProperty(PHONE.getKey()));
            contactModel.setRecomendedBy((String) entity.getProperty(RECOMENDED_BY.getKey()));
            contactModel.setRecomendedById((Long) entity.getProperty(RECOMENDED_BY_ID.getKey()));
            contactModel.setType((String) entity.getProperty(TYPE.getKey()));
            return contactModel;
        }
    };

    public static Function<Entity, ClientModel> entityToClient = new Function<Entity, ClientModel>() {
        @Override
        public ClientModel apply(com.google.appengine.api.datastore.Entity entity) {
            ClientModel contactModel = new ClientModel();
            contactModel.setId((Long) entity.getProperty(ID.getKey()));
            contactModel.setName((String) entity.getProperty(NAME.getKey()));
            contactModel.setDate((String) entity.getProperty(DATE.getKey()));
            contactModel.setPhone((Long) entity.getProperty(PHONE.getKey()));
            contactModel.setRecomendedBy((String) entity.getProperty(RECOMENDED_BY.getKey()));
            contactModel.setRecomendedById((Long) entity.getProperty(RECOMENDED_BY_ID.getKey()));
            contactModel.setType((String) entity.getProperty(TYPE.getKey()));
            contactModel.setEmail((String) entity.getProperty(EMAIL.getKey()));
            return contactModel;
        }
    };

    public static Function<Entity, MemberModel> entityToMember = new Function<Entity, MemberModel>() {
        @Override
        public MemberModel apply(com.google.appengine.api.datastore.Entity entity) {
            MemberModel contactModel = new MemberModel();
            contactModel.setId((Long) entity.getProperty(ID.getKey()));
            contactModel.setName((String) entity.getProperty(NAME.getKey()));
            contactModel.setDate((String) entity.getProperty(DATE.getKey()));
            contactModel.setPhone((Long) entity.getProperty(PHONE.getKey()));
            contactModel.setRecomendedBy((String) entity.getProperty(RECOMENDED_BY.getKey()));
            contactModel.setRecomendedById((Long) entity.getProperty(RECOMENDED_BY_ID.getKey()));
            contactModel.setType((String) entity.getProperty(TYPE.getKey()));
            contactModel.setEmail((String) entity.getProperty(EMAIL.getKey()));
            return contactModel;
        }
    };
}
