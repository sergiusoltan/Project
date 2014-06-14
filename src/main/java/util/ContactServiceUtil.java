package main.java.util;

import com.google.appengine.api.datastore.*;
import com.google.appengine.labs.repackaged.com.google.common.base.Function;
import com.google.appengine.labs.repackaged.com.google.common.collect.Iterables;
import com.google.appengine.labs.repackaged.com.google.common.collect.Lists;
import com.google.appengine.labs.repackaged.com.google.common.collect.Maps;
import com.google.appengine.repackaged.org.joda.time.DateTime;
import main.java.model.auth.Authorization;
import main.java.model.people.ClientModel;
import main.java.model.people.ContactModel;
import main.java.model.auth.UserStatus;
import main.java.model.people.DashBoardModel;
import main.java.model.people.MemberModel;

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
        newContact.setProperty(RECOMENDED_BY_ID.getKey(), contact.getRecomendedById());
        newContact.setProperty(TYPE.getKey(), contact.getType());
        datastoreService.put(newContact);

        return getString(getAllContacts(owner));
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
        newContact.setProperty(RECOMENDED_BY_ID.getKey(), contact.getRecomendedById());
        newContact.setProperty(TYPE.getKey(), contact.getType());
        newContact.setProperty(EMAIL.getKey(), contact.getEmail());
        datastoreService.put(newContact);

        return getString(getAllClients(owner));
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
        newContact.setProperty(RECOMENDED_BY_ID.getKey(), contact.getRecomendedById());
        newContact.setProperty(TYPE.getKey(), contact.getType());
        newContact.setProperty(EMAIL.getKey(), contact.getEmail());
        datastoreService.put(newContact);

        return getString(getAllMembers(owner));
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
        foundEntity.setProperty(RECOMENDED_BY_ID.getKey(), contact.getRecomendedById());
        foundEntity.setProperty(TYPE.getKey(), contact.getType());
        datastoreService.put(foundEntity);
        return getString(getAllContacts(owner));
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
        foundEntity.setProperty(RECOMENDED_BY_ID.getKey(), contact.getRecomendedById());
        foundEntity.setProperty(TYPE.getKey(), contact.getType());
        foundEntity.setProperty(EMAIL.getKey(), contact.getEmail());
        datastoreService.put(foundEntity);
        return getString(getAllClients(owner));
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
        foundEntity.setProperty(RECOMENDED_BY_ID.getKey(), contact.getRecomendedById());
        foundEntity.setProperty(TYPE.getKey(), contact.getType());
        foundEntity.setProperty(EMAIL.getKey(), contact.getEmail());
        datastoreService.put(foundEntity);
        return getString(getAllClients(owner));
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
                entity.setProperty(RECOMENDED_BY_ID.getKey(), null);
                entity.setProperty(RECOMENDED_BY.getKey(), null);
                return entity;
            }
        }));


        switch (type) {
            case CLIENT:
                return getString(getAllClients(owner));
            case MEMBER:
                return getString(getAllMembers(owner));
            default:
                return getString(getAllContacts(owner));
        }
    }

    public static String getContact(String owner, Long id, Integer type) {
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        Key userKey = Utils.getUserKey(owner);
        Query query = new Query(Entities.CONTACT.getId(), userKey).setFilter(
                new Query.FilterPredicate(ID.getKey(), Query.FilterOperator.EQUAL, id)
        );
        Entity foundEntity = datastoreService.prepare(query).asSingleEntity();
        if (foundEntity == null) {
            return null;
        }
        return fromEntityToString(type, foundEntity);
    }

    public static String getTrimesterStatistics(String owner, Integer type) {
        int currentMonth = DateTime.now().getMonthOfYear();
        Map<String, Integer> getResults = getNumberByMonths(Lists.newLinkedList(currentMonth-1,currentMonth-2,currentMonth-3), type, owner);
        List<DashBoardModel> results = Lists.newArrayList();
        for (Map.Entry<String, Integer> stringIntegerEntry : getResults.entrySet()) {
            results.add(new DashBoardModel(stringIntegerEntry.getValue(),stringIntegerEntry.getKey()));
        }
        return getString(results);
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
//        System.out.println(interval);
        return interval;
    }
//
//    public static void main(String[] args) {
//        List<String> intervals = getInterval(DateTime.now().getMonthOfYear());
//        String adate = "2014-01-03";
//        System.out.println(intervals.get(0).compareTo(adate) < 0 && intervals.get(1).compareTo(adate) > 0);
//        System.out.println(intervals.get(0).compareTo(adate));
//        System.out.println(intervals.get(1).compareTo(adate));
//        System.out.println(DateTime.now().getMonthOfYear());
//    }

}
