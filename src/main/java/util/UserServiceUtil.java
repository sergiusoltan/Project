package main.java.util;

import com.google.appengine.api.datastore.*;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import main.java.model.auth.AuthModel;
import main.java.model.auth.UserStatus;
import main.java.validations.EntityNotFoundRuntimeException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static main.java.util.UserProperties.*;
import static main.java.util.Utils.getUserKey;

/**
 * @author Serghei Soltan (soltan@spmsoftware.com)
 */
public class UserServiceUtil {

    public static Boolean isLoggedUser() {
        UserService userService = UserServiceFactory.getUserService();
        return userService.isUserLoggedIn();
    }

    public static User getCurrentUser() {
        UserService userService = UserServiceFactory.getUserService();
        return userService.getCurrentUser();
    }

    public static String getUserUrl(Boolean logoutUrl) {
        UserService userService = UserServiceFactory.getUserService();
        return logoutUrl ? userService.createLoginURL("/login") : userService.createLoginURL("/");
    }

    public static Boolean trySave(AuthModel model, List<String> responses) {
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        Boolean success = false;
        try {
            datastoreService.get(getUserKey(model.getEmail()));
        } catch (EntityNotFoundException e) {
            success = true;
            Entity entityToSave = new Entity(getUserKey(model.getEmail()));
            entityToSave.setProperty(NAME.getKey(), model.getName());
            entityToSave.setProperty(EMAIL.getKey(), model.getEmail());
            entityToSave.setProperty(DATE.getKey(), new Date().getTime());
            entityToSave.setProperty(IS_LOGGED.getKey(), false);
            entityToSave.setProperty(PASSWORD.getKey(), getPasswordHash(model.getPassword()));
            datastoreService.put(entityToSave);
            responses.add("Account have been created for " + model.getEmail());
        }
        if (!success) {
            responses.add("Failed to save user " + model.getEmail() + " because it is already registered with the same email address");
        }
        return success;
    }

    public static UserStatus tryLogin(AuthModel model, List<String> responses) {
        Entity userEntity;
        try{
            userEntity = getEntity(Entities.USER, model.getEmail());
        }catch (EntityNotFoundRuntimeException e){
            responses.add(e.getMessage());
            return new UserStatus(false);
        }
        Map<String, Object> entityProperties = userEntity.getProperties();
        Boolean isLogged = (Boolean) entityProperties.get(IS_LOGGED.getKey());
        UserStatus userStatus = new UserStatus(isLogged);
        if (isLogged) {
            responses.add("User " + model.getEmail() + " already logged in!");
            userStatus.setEmail((String) entityProperties.get(EMAIL.getKey()));
            userStatus.setName((String) entityProperties.get(NAME.getKey()));

            return userStatus;
        }

        String hashPassword = getPasswordHash(model.getPassword());
        if (hashPassword.equals(entityProperties.get(PASSWORD.getKey()))) {
            responses.add("Successfully logged in!");
            userStatus.setEmail((String) entityProperties.get(EMAIL.getKey()));
            userStatus.setUserUrl(getUserUrl(true));
            userStatus.setName((String) entityProperties.get(NAME.getKey()));

            userEntity.setProperty(IS_LOGGED.getKey(),true);
            updateEntity(userEntity);

            return userStatus;
        }

        responses.add("The specified password is not valid!");
        userStatus.setLogged(false);
        userStatus.setEmail(model.getEmail());
        userStatus.setName(model.getName());
        userStatus.setUserUrl(getUserUrl(false));

        return userStatus;

    }

    public static UserStatus tryLogout(UserStatus model, List<String> responses) {
        Entity userEntity;
        try{
            userEntity = getEntity(Entities.USER, model.getEmail());
            userEntity.setProperty(IS_LOGGED.getKey(), false);
            updateEntity(userEntity);
        }catch (EntityNotFoundRuntimeException e){
            responses.add(e.getMessage());
        }

        UserStatus userStatus = new UserStatus(false);
        responses.add("User logged out!");

        return userStatus;
    }

    public static void updateEntity(Entity entity) {
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        datastoreService.put(entity);
    }

    public static Entity getEntity(Entities kind, String emailAddress) throws EntityNotFoundRuntimeException{
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        Query query = new Query(kind.getId())
                .setFilter(new Query.FilterPredicate(EMAIL.getKey(), Query.FilterOperator.EQUAL, emailAddress.toLowerCase()));
        Entity entity = datastoreService.prepare(query).asSingleEntity();
        if(entity == null){
            throw new EntityNotFoundRuntimeException("The specified entity " + emailAddress + " does not exists!");
        }
        return entity;
    }

    private static String getPasswordHash(String password) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            //
        }
        assert md != null;
        md.update(password.getBytes());

        byte byteData[] = md.digest();
        StringBuilder hexString = new StringBuilder();
        for (byte aByteData : byteData) {
            String hex = Integer.toHexString(0xff & aByteData);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }


}
