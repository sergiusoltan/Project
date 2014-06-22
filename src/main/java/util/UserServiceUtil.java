package main.java.util;

import com.google.appengine.api.datastore.*;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.labs.repackaged.com.google.common.collect.Lists;
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

    public static Boolean isAuthorized(String sessionToken, String savedToken){
        return savedToken != null && savedToken.equals(sessionToken);
    }

    public static UserStatus findUser(String userEmail, List<String> errors){
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        try {
            Entity entity = datastoreService.get(getUserKey(userEmail));
            UserStatus userStatus = new UserStatus();
            userStatus.setEmail((String) entity.getProperty(EMAIL.getKey()));
            userStatus.setName((String) entity.getProperty(NAME.getKey()));
            userStatus.setSessionToken((String) entity.getProperty(SESSION_TOKEN.getKey()));
            return userStatus;

        } catch (EntityNotFoundException e) {
            errors.add("No user found with " + userEmail + " email");
        }
        return new UserStatus();
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
            return new UserStatus();
        }
        Map<String, Object> entityProperties = userEntity.getProperties();
        UserStatus userStatus = new UserStatus();
        String hashPassword = getPasswordHash(model.getPassword());
        if (hashPassword.equals(entityProperties.get(PASSWORD.getKey()))) {
            responses.add("Successfully logged in!");
            userStatus.setEmail((String) entityProperties.get(EMAIL.getKey()));
            userStatus.setUserUrl(getUserUrl(true));
            userStatus.setName((String) entityProperties.get(NAME.getKey()));
            String sessionToken = getPasswordHash(model.getPassword() + new Date().getTime());
            System.out.println(sessionToken);
            userEntity.setProperty(SESSION_TOKEN.getKey(), sessionToken);

            updateEntity(userEntity);

            userStatus.setSessionToken(sessionToken);
            return userStatus;
        }

        responses.add("Invalid Credentials!");
        userStatus.setEmail(model.getEmail());
        userStatus.setName(model.getName());
        userStatus.setUserUrl(getUserUrl(false));

        return userStatus;

    }

    public static UserStatus tryLogout(UserStatus model, List<String> responses) {
        Entity userEntity;
        try{
            userEntity = getEntity(Entities.USER, model.getEmail());
            userEntity.setProperty(SESSION_TOKEN.getKey(), null);
            updateEntity(userEntity);
        }catch (RuntimeException e){
            responses.add(e.getMessage());
        }

        UserStatus userStatus = new UserStatus();
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
