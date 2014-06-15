package main.java.util;

import com.google.appengine.api.datastore.*;
import com.google.appengine.labs.repackaged.com.google.common.collect.Lists;
import main.java.model.people.EvaluationModel;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import static main.java.util.Utils.setFromEvaluation;

/**
 * User: Sergiu Soltan
 */
public class EvaluationServiceUtil {

    public static Collection<EvaluationModel> getAllEvaluations(String owner, Long contactId){
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        Key userKey = Utils.getUserKey(owner);
        Query query = new Query(Entities.EVALUATIONS.getId(), userKey);
        query.setFilter(new Query.FilterPredicate(EvaluationsProperties.CONTACT_ID.getId(), Query.FilterOperator.EQUAL, contactId));

        List<Entity> entityList = datastoreService.prepare(query).asList(FetchOptions.Builder.withDefaults());
        Collection<EvaluationModel> contactModels = Lists.transform(entityList, Utils.entityToEvaluation);

        return contactModels;

    }

    public static Collection<EvaluationModel> saveEvaluation(String owner, Long contactId, String evaluationModel){
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();

        EvaluationModel evaluation = EvaluationModel.getFromString(evaluationModel);
        evaluation.setId(new Date().getTime());
        evaluation.setContactId(contactId);

        Key userKey = Utils.getUserKey(owner);
        Entity newContact = new Entity(Entities.EVALUATIONS.getId(), userKey);
        setFromEvaluation(newContact, evaluation);
        datastoreService.put(newContact);

        return getAllEvaluations(owner, contactId);
    }
}
