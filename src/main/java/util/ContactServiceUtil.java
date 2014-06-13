package main.java.util;

import com.google.appengine.api.datastore.*;
import com.google.appengine.labs.repackaged.com.google.common.base.Function;
import com.google.appengine.labs.repackaged.com.google.common.collect.Iterables;
import com.google.appengine.labs.repackaged.com.google.common.collect.Lists;
import main.java.model.auth.Authorization;
import main.java.model.people.ClientModel;
import main.java.model.people.ContactModel;
import main.java.model.auth.UserStatus;
import main.java.model.people.MemberModel;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import static main.java.util.PeopleProperties.*;
import static main.java.util.Utils.*;

/**
 * User: Sergiu Soltan
 */
public class ContactServiceUtil {

    public static final int CLIENT = 1;
    public static final int MEMBER = 2;
    public static final int CONTACT = 3;

    public static Boolean isAuthorizedRequest(Authorization auth) {
        if (auth.getEmail() == null || auth.getSessionToken() == null) {
            return false;
        }
        List<String> errors = Lists.newArrayList();
        UserStatus userStatus = UserServiceUtil.findUser(auth.getEmail(), errors);
        if (!errors.isEmpty() || !UserServiceUtil.isAuthorized(auth.getSessionToken(), userStatus.getSessionToken())) {
            return false;
        }
        return true;
    }

    public static String getAllContacts(String owner) {
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        Key userKey = Utils.getUserKey(owner);
        Query query = new Query(Entities.CONTACT.getId(), userKey);
        List<Entity> entityList = datastoreService.prepare(query).asList(FetchOptions.Builder.withDefaults());
        Collection<ContactModel> contactModels = Lists.transform(entityList, Utils.entityToContact);

        return getInstance().toJson(contactModels);
    }

    public static String getAllMembers(String owner) {
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        Key userKey = Utils.getUserKey(owner);
        Query query = new Query(Entities.CONTACT.getId(), userKey)
                .setFilter(new Query.FilterPredicate(TYPE.getKey(), Query.FilterOperator.EQUAL, "Member"));
        List<Entity> entityList = datastoreService.prepare(query).asList(FetchOptions.Builder.withDefaults());
        Collection<MemberModel> contactModels = Lists.transform(entityList, Utils.entityToMember);

        return getInstance().toJson(contactModels);
    }

    public static String getAllClients(String owner) {
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        Key userKey = Utils.getUserKey(owner);
        Query query = new Query(Entities.CONTACT.getId(), userKey)
                .setFilter(new Query.FilterPredicate(TYPE.getKey(), Query.FilterOperator.EQUAL, "Client"));
        List<Entity> entityList = datastoreService.prepare(query).asList(FetchOptions.Builder.withDefaults());
        Collection<ClientModel> contactModels = Lists.transform(entityList, Utils.entityToClient);

        return getInstance().toJson(contactModels);
    }

    public static String saveContacts(String owner, String contactModel) {
        ContactModel contact = contactFromString(contactModel);
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        contact.setId(new Date().getTime());

        Key userKey = Utils.getUserKey(owner);
        Entity newContact = new Entity(Entities.CONTACT.getId(), userKey);
        newContact.setProperty(ID.getKey(), contact.getId());
        newContact.setProperty(NAME.getKey(), contact.getName());
        newContact.setProperty(DATE.getKey(), contact.getDate());
        newContact.setProperty(PHONE.getKey(), contact.getPhone());
        newContact.setProperty(RECOMENDED_BY.getKey(), contact.getRecomendedBy());
        newContact.setProperty(TYPE.getKey(), contact.getType());
        datastoreService.put(newContact);

        return getAllContacts(owner);
    }

    public static String saveClient(String owner, String contactModel) {
        ClientModel contact = clientFromString(contactModel);
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();

        contact.setId(new Date().getTime());
        Key userKey = Utils.getUserKey(owner);
        Entity newContact = new Entity(Entities.CONTACT.getId(), userKey);
        newContact.setProperty(ID.getKey(), contact.getId());
        newContact.setProperty(NAME.getKey(), contact.getName());
        newContact.setProperty(DATE.getKey(), contact.getDate());
        newContact.setProperty(PHONE.getKey(), contact.getPhone());
        newContact.setProperty(RECOMENDED_BY.getKey(), contact.getRecomendedBy());
        newContact.setProperty(TYPE.getKey(), contact.getType());
        newContact.setProperty(EMAIL.getKey(), contact.getEmail());
        datastoreService.put(newContact);

        return getAllClients(owner);
    }

    public static String saveMember(String owner, String contactModel) {
        MemberModel contact = memberFromString(contactModel);
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();

        contact.setId(new Date().getTime());
        Key userKey = Utils.getUserKey(owner);
        Entity newContact = new Entity(Entities.CONTACT.getId(), userKey);
        newContact.setProperty(ID.getKey(), contact.getId());
        newContact.setProperty(NAME.getKey(), contact.getName());
        newContact.setProperty(DATE.getKey(), contact.getDate());
        newContact.setProperty(PHONE.getKey(), contact.getPhone());
        newContact.setProperty(RECOMENDED_BY.getKey(), contact.getRecomendedBy());
        newContact.setProperty(TYPE.getKey(), contact.getType());
        newContact.setProperty(EMAIL.getKey(), contact.getEmail());
        datastoreService.put(newContact);

        return getAllMembers(owner);
    }

    public static String updateContacts(String owner, String contactModel) {
        ContactModel contact = contactFromString(contactModel);
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        Key userKey = Utils.getUserKey(owner);
        Query query = new Query(Entities.CONTACT.getId(), userKey).setFilter(
                new Query.FilterPredicate(ID.getKey(), Query.FilterOperator.EQUAL, contact.getId())
        );
        Entity foundEntity = datastoreService.prepare(query).asSingleEntity();
        if (foundEntity == null) {
            return null;
        }
        foundEntity.setProperty(NAME.getKey(), contact.getName());
        foundEntity.setProperty(DATE.getKey(), contact.getDate());
        foundEntity.setProperty(PHONE.getKey(), contact.getPhone());
        foundEntity.setProperty(RECOMENDED_BY.getKey(), contact.getRecomendedBy());
        foundEntity.setProperty(TYPE.getKey(), contact.getType());
        datastoreService.put(foundEntity);
        return getAllContacts(owner);
    }

    public static String updateClient(String owner, String contactModel) {
        ClientModel contact = clientFromString(contactModel);
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        Key userKey = Utils.getUserKey(owner);
        Query query = new Query(Entities.CONTACT.getId(), userKey).setFilter(
                new Query.FilterPredicate(ID.getKey(), Query.FilterOperator.EQUAL, contact.getId())
        );
        Entity foundEntity = datastoreService.prepare(query).asSingleEntity();
        if (foundEntity == null) {
            return null;
        }
        foundEntity.setProperty(NAME.getKey(), contact.getName());
        foundEntity.setProperty(DATE.getKey(), contact.getDate());
        foundEntity.setProperty(PHONE.getKey(), contact.getPhone());
        foundEntity.setProperty(RECOMENDED_BY.getKey(), contact.getRecomendedBy());
        foundEntity.setProperty(TYPE.getKey(), contact.getType());
        foundEntity.setProperty(EMAIL.getKey(), contact.getEmail());
        datastoreService.put(foundEntity);
        return getAllClients(owner);
    }

    public static String updateMember(String owner, String contactModel) {
        MemberModel contact = memberFromString(contactModel);
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        Key userKey = Utils.getUserKey(owner);
        Query query = new Query(Entities.CONTACT.getId(), userKey).setFilter(
                new Query.FilterPredicate(ID.getKey(), Query.FilterOperator.EQUAL, contact.getId())
        );
        Entity foundEntity = datastoreService.prepare(query).asSingleEntity();
        if (foundEntity == null) {
            return null;
        }
        foundEntity.setProperty(NAME.getKey(), contact.getName());
        foundEntity.setProperty(DATE.getKey(), contact.getDate());
        foundEntity.setProperty(PHONE.getKey(), contact.getPhone());
        foundEntity.setProperty(RECOMENDED_BY.getKey(), contact.getRecomendedBy());
        foundEntity.setProperty(TYPE.getKey(), contact.getType());
        foundEntity.setProperty(EMAIL.getKey(), contact.getEmail());
        datastoreService.put(foundEntity);
        return getAllClients(owner);
    }

    public static String deleteContacts(String owner, String deleteList, Integer type) {
        List<Long> contactIds = fromListType(deleteList);
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        Key userKey = Utils.getUserKey(owner);
        Query query = new Query(Entities.CONTACT.getId(), userKey);
        query.setFilter(new Query.FilterPredicate(ID.getKey(), Query.FilterOperator.IN, contactIds));
        List<Entity> entityList = datastoreService.prepare(query).asList(FetchOptions.Builder.withDefaults());
        datastoreService.delete(Iterables.transform(entityList, new Function<Entity, Key>() {
            @Override
            public Key apply(com.google.appengine.api.datastore.Entity entity) {
                return entity.getKey();
            }
        }));

        Query recomendedByUpdate = new Query(Entities.CONTACT.getId(), userKey);
        query.setFilter(new Query.FilterPredicate(RECOMENDED_BY_ID.getKey(), Query.FilterOperator.IN, contactIds));
        List<Entity> usages = datastoreService.prepare(recomendedByUpdate).asList(FetchOptions.Builder.withDefaults());
        datastoreService.put(Iterables.transform(usages, new Function<Entity, Entity>() {
            @Override
            public Entity apply(com.google.appengine.api.datastore.Entity entity) {
                entity.setProperty(RECOMENDED_BY_ID.getKey(),null);
                entity.setProperty(RECOMENDED_BY.getKey(),null);
                return entity;
            }
        }));


        switch (type){
            case CLIENT:
                return getAllClients(owner);
            case MEMBER:
                return getAllMembers(owner);
            default:
                return getAllContacts(owner);
        }
    }

}
