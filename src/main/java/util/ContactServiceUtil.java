package main.java.util;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.datastore.*;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;
import com.google.appengine.labs.repackaged.com.google.common.base.Function;
import com.google.appengine.labs.repackaged.com.google.common.collect.Iterables;
import com.google.appengine.labs.repackaged.com.google.common.collect.Lists;
import com.google.appengine.labs.repackaged.com.google.common.collect.Maps;
import com.google.appengine.repackaged.org.joda.time.DateTime;
import main.java.model.auth.Authorization;
import main.java.model.people.*;
import main.java.model.auth.UserStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

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

    public static Collection<ContactModel> getAllContacts(String owner, Query.Filter... filters) {
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        Key userKey = Utils.getUserKey(owner);
        Query query = new Query(Entities.CONTACT.getId(), userKey);
        List<Query.Filter> filterList = Lists.newArrayList(filters);
        if(!filterList.isEmpty()){
            Query.Filter composite = new Query.CompositeFilter(Query.CompositeFilterOperator.AND, filterList);
            query.setFilter(composite);
        }

        List<Entity> entityList = datastoreService.prepare(query).asList(FetchOptions.Builder.withDefaults());
        Collection<ContactModel> contactModels = Lists.transform(entityList, Utils.entityToContact);

        return contactModels;
    }

    public static Collection<MemberModel> getAllMembers(String owner, Query.Filter... filters) {
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        Key userKey = Utils.getUserKey(owner);
        Query query = new Query(Entities.CONTACT.getId(), userKey)
                .setFilter(new Query.FilterPredicate(TYPE.getKey(), Query.FilterOperator.EQUAL, "Member"));

        List<Query.Filter> filterList = Lists.newArrayList(filters);
        if(!filterList.isEmpty()){
            filterList.add(query.getFilter());
            Query.Filter composite = new Query.CompositeFilter(Query.CompositeFilterOperator.AND, filterList);
            query.setFilter(composite);
        }
        List<Entity> entityList = datastoreService.prepare(query).asList(FetchOptions.Builder.withDefaults());
        Collection<MemberModel> contactModels = Lists.transform(entityList, Utils.entityToMember);

        return contactModels;
    }

    public static Collection<ClientModel> getAllClients(String owner, Query.Filter... filters) {
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        Key userKey = Utils.getUserKey(owner);
        Query query = new Query(Entities.CONTACT.getId(), userKey)
                .setFilter(new Query.FilterPredicate(TYPE.getKey(), Query.FilterOperator.EQUAL, "Client"));

        List<Query.Filter> filterList = Lists.newArrayList(filters);
        if(!filterList.isEmpty()){
            filterList.add(query.getFilter());
            Query.Filter composite = new Query.CompositeFilter(Query.CompositeFilterOperator.AND, filterList);
            query.setFilter(composite);
        }
        List<Entity> entityList = datastoreService.prepare(query).asList(FetchOptions.Builder.withDefaults());
        Collection<ClientModel> contactModels = Lists.transform(entityList, Utils.entityToClient);

        return contactModels;
    }

    public static Collection<ContactModel> saveContacts(String owner, String contactModel) {
        ContactModel contact = contactFromString(contactModel);
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        contact.setId(new Date().getTime());

        Key userKey = Utils.getUserKey(owner);
        Entity newContact = new Entity(Entities.CONTACT.getId(), userKey);
        setFromContact(newContact,contact, false);
        datastoreService.put(newContact);

        return getAllContacts(owner);
    }

    public static Collection<ClientModel> saveClient(String owner, String contactModel) {
        ClientModel client = clientFromString(contactModel);
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();

        client.setId(new Date().getTime());
        Key userKey = Utils.getUserKey(owner);
        Entity newContact = new Entity(Entities.CONTACT.getId(), userKey);
        setFromClient(newContact,client, false);
        datastoreService.put(newContact);

        return getAllClients(owner);
    }

    public static Collection<MemberModel> saveMember(String owner, String contactModel) {
        MemberModel memberModel = memberFromString(contactModel);
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();

        memberModel.setId(new Date().getTime());
        Key userKey = Utils.getUserKey(owner);
        Entity newMember = new Entity(Entities.CONTACT.getId(), userKey);
        setFromMember(newMember, memberModel, false);
        datastoreService.put(newMember);

        return getAllMembers(owner);
    }

    public static Collection<ContactModel> updateContact(String owner, HttpServletRequest request) {
        BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
        Map<String, List<BlobKey>> uploadedKeys = blobstoreService.getUploads(request);
        ContactModel contactModel = contactFromString(request.getParameter("item"));
        if(contactModel.getImageBlobKey() != null){
            BlobstoreServiceFactory.getBlobstoreService().delete(new BlobKey(contactModel.getImageBlobKey()));
        }
        BlobKey blobKey = uploadedKeys.get("file").get(0);
        String servingUrl = ImagesServiceFactory.getImagesService().getServingUrl(ServingUrlOptions.Builder
                .withBlobKey(blobKey)
                .crop(false));
        contactModel.setContactImageUrl(servingUrl);
        contactModel.setImageBlobKey(blobKey.getKeyString());

        return updateContacts(owner, contactModel);
    }

    public static Collection<ContactModel> updateContacts(String owner, ContactModel contactModel) {
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        Key userKey = Utils.getUserKey(owner);
        Query query = new Query(Entities.CONTACT.getId(), userKey).setFilter(
                new Query.FilterPredicate(ID.getKey(), Query.FilterOperator.EQUAL, contactModel.getId())
        );
        Entity foundEntity = datastoreService.prepare(query).asSingleEntity();
        if (foundEntity == null) {
            return null;
        }
        setFromContact(foundEntity, contactModel, true);
        datastoreService.put(foundEntity);
        return getAllContacts(owner);
    }

    public static Collection<ClientModel> updateClient(String owner, HttpServletRequest request) {
        BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
        Map<String, List<BlobKey>> uploadedKeys = blobstoreService.getUploads(request);
        ClientModel clientModel = clientFromString(request.getParameter("item"));
        if(clientModel.getImageBlobKey() != null){
            BlobstoreServiceFactory.getBlobstoreService().delete(new BlobKey(clientModel.getImageBlobKey()));
        }
        BlobKey blobKey = uploadedKeys.get("file").get(0);
        String servingUrl = ImagesServiceFactory.getImagesService().getServingUrl(ServingUrlOptions.Builder
                .withBlobKey(blobKey)
                .crop(false));
        clientModel.setContactImageUrl(servingUrl);
        clientModel.setImageBlobKey(blobKey.getKeyString());

        return updateClient(owner, clientModel);
    }

    public static Collection<ClientModel> updateClient(String owner, ClientModel contactModel) {
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        Key userKey = Utils.getUserKey(owner);
        Query query = new Query(Entities.CONTACT.getId(), userKey).setFilter(
                new Query.FilterPredicate(ID.getKey(), Query.FilterOperator.EQUAL, contactModel.getId())
        );
        Entity foundEntity = datastoreService.prepare(query).asSingleEntity();
        if (foundEntity == null) {
            return null;
        }
        setFromClient(foundEntity, contactModel, true);
        datastoreService.put(foundEntity);
        return getAllClients(owner);
    }

    public static Collection<MemberModel> updateMember(String owner, HttpServletRequest request) {
        BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
        Map<String, List<BlobKey>> uploadedKeys = blobstoreService.getUploads(request);
        MemberModel memberModel = memberFromString(request.getParameter("item"));
        if(memberModel.getImageBlobKey() != null){
            BlobstoreServiceFactory.getBlobstoreService().delete(new BlobKey(memberModel.getImageBlobKey()));
        }
        BlobKey blobKey = uploadedKeys.get("file").get(0);
        String servingUrl = ImagesServiceFactory.getImagesService().getServingUrl(ServingUrlOptions.Builder
                .withBlobKey(blobKey)
                .crop(false));
        memberModel.setContactImageUrl(servingUrl);
        memberModel.setImageBlobKey(blobKey.getKeyString());

        return updateMember(owner, memberModel);
    }

    public static Collection<MemberModel> updateMember(String owner, MemberModel memberModel) {
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        Key userKey = Utils.getUserKey(owner);
        Query query = new Query(Entities.CONTACT.getId(), userKey).setFilter(
                new Query.FilterPredicate(ID.getKey(), Query.FilterOperator.EQUAL, memberModel.getId())
        );
        Entity foundEntity = datastoreService.prepare(query).asSingleEntity();
        if (foundEntity == null) {
            return null;
        }
        setFromMember(foundEntity, memberModel, true);
        datastoreService.put(foundEntity);
        return getAllMembers(owner);
    }

    public static Collection<? extends ContactModel> deleteContacts(String owner, String deleteList, Integer type) {
        List<Long> contactIds = fromListType(deleteList);
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        Key userKey = Utils.getUserKey(owner);
        Query query = new Query(Entities.CONTACT.getId(), userKey);
        query.setFilter(new Query.FilterPredicate(ID.getKey(), Query.FilterOperator.IN, contactIds));
        List<Entity> entityList = datastoreService.prepare(query).asList(FetchOptions.Builder.withDefaults());
        List<ContactModel> contactModels = Lists.transform(entityList, entityToContact);
        datastoreService.delete(Iterables.transform(entityList, new Function<Entity, Key>() {
            @Override
            public Key apply(com.google.appengine.api.datastore.Entity entity) {
                return entity.getKey();
            }
        }));

        final BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
        for (ContactModel contactModel : contactModels) {
            if(contactModel.getImageBlobKey() != null){
                blobstoreService.delete(new BlobKey(contactModel.getImageBlobKey()));
            }
        }
        Query recomendedByUpdate = new Query(Entities.CONTACT.getId(), userKey);
        recomendedByUpdate.setFilter(new Query.FilterPredicate(RECOMENDED_BY_ID.getKey(), Query.FilterOperator.IN, contactIds));
        List<Entity> usages = datastoreService.prepare(recomendedByUpdate).asList(FetchOptions.Builder.withDefaults());
        datastoreService.put(Iterables.transform(usages, new Function<Entity, Entity>() {
            @Override
            public Entity apply(com.google.appengine.api.datastore.Entity entity) {
                entity.setProperty(RECOMENDED_BY_ID.getKey(), null);
                entity.setProperty(RECOMENDED_BY.getKey(), null);
                return entity;
            }
        }));


        switch (type) {
            case CLIENT:
                return getAllClients(owner);
            case MEMBER:
                return getAllMembers(owner);
            default:
                return getAllContacts(owner);
        }
    }

    public static ContactModel getContact(String owner, Long id, Integer type) {
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        Key userKey = Utils.getUserKey(owner);
        Query query = new Query(Entities.CONTACT.getId(), userKey).setFilter(
                new Query.FilterPredicate(ID.getKey(), Query.FilterOperator.EQUAL, id)
        );
        Entity foundEntity = datastoreService.prepare(query).asSingleEntity();
        if (foundEntity == null) {
            return null;
        }
        return fromEntityToModel(type, foundEntity);
    }

    public static Collection<DashBoardModel> getTrimesterStatistics(String owner, Integer type) {
        int currentMonth = DateTime.now().getMonthOfYear();
        Map<String, Integer> getResults = getNumberByMonths(Lists.newLinkedList(currentMonth-1,currentMonth-2,currentMonth-3), type, owner);
        List<DashBoardModel> results = Lists.newArrayList();
        for (Map.Entry<String, Integer> stringIntegerEntry : getResults.entrySet()) {
            results.add(new DashBoardModel(stringIntegerEntry.getValue(),stringIntegerEntry.getKey()));
        }
        return results;
    }

    private static Map<String, Integer> getNumberByMonths(List<Integer> months, Integer type, String owner) {
        Map<String, Integer> monthToNumber = Maps.newLinkedHashMap();
        for (Integer month : months) {
            List<String> interval = getInterval(month);
            Query.Filter f1 = new Query.FilterPredicate(DATE.getKey(), Query.FilterOperator.GREATER_THAN_OR_EQUAL, interval.get(0));
            Query.Filter f2 = new Query.FilterPredicate(DATE.getKey(), Query.FilterOperator.LESS_THAN_OR_EQUAL, interval.get(1));
            switch (type) {
                case CLIENT:
                    monthToNumber.put(interval.get(2), getAllClients(owner, f1, f2).size());
                    break;
                case MEMBER:
                    monthToNumber.put(interval.get(2), getAllMembers(owner, f1, f2).size());
                    break;
                default:
                    monthToNumber.put(interval.get(2), getAllContacts(owner, f1, f2).size());
            }
        }
        return monthToNumber;
    }

    private static List<String> getInterval(int month) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        DateFormat monthName = new SimpleDateFormat("MMMM", Locale.ENGLISH);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);

        List<String> interval = Lists.newLinkedList();
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, 1);

        interval.add(dateFormat.format(cal.getTime()));


        cal.add(Calendar.MONTH, 1);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        interval.add(dateFormat.format(cal.getTime()));

        interval.add(monthName.format(cal.getTime()));
        return interval;
    }

}
