package main.java.util;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.labs.repackaged.com.google.common.base.Function;
import com.google.appengine.labs.repackaged.com.google.common.collect.Lists;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import main.java.model.auth.UserStatus;
import main.java.model.people.*;

import javax.ws.rs.core.Response;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

    public static ContactModel fromEntityToModel(Integer type, Entity entity){
        switch (type){
            case ContactServiceUtil.CLIENT:
                return  Lists.transform(Lists.newArrayList(entity), entityToClient).get(0);
            case ContactServiceUtil.MEMBER:
                return  Lists.transform(Lists.newArrayList(entity), entityToMember).get(0);
            default:
                return  Lists.transform(Lists.newArrayList(entity), entityToContact).get(0);
        }
    }

    public static List<Integer> parseDate(String inputDate){
        Date date = null;
        try {
            date =new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(inputDate);
        } catch (ParseException ignore) {
            return Lists.newArrayList();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return Lists.newLinkedList(year,month,day);
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

    public static void setFromContact(Entity entity, ContactModel contactModel, boolean update){
        if(!update){
            entity.setProperty(ID.getKey(), contactModel.getId());
        }
        entity.setProperty(NAME.getKey(), contactModel.getName());
        entity.setProperty(DATE.getKey(), contactModel.getDate());
        entity.setProperty(PHONE.getKey(), contactModel.getPhone());
        entity.setProperty(RECOMENDED_BY.getKey(), contactModel.getRecomendedBy());
        entity.setProperty(RECOMENDED_BY_ID.getKey(), contactModel.getRecomendedById());
        entity.setProperty(TYPE.getKey(), contactModel.getType());
        entity.setProperty(AGE.getKey(), contactModel.getAge());
        entity.setProperty(HEIGHT.getKey(), contactModel.getHeight());
        entity.setProperty(WEIGHT.getKey(), contactModel.getWeight());
    }

    public static void setFromClient(Entity entity, ClientModel clientModel, boolean update){
        setFromContact(entity, clientModel, update);
        entity.setProperty(EMAIL.getKey(), clientModel.getEmail());
    }

    public static void setFromMember(Entity entity, MemberModel memberModel, boolean update){
        setFromContact(entity, memberModel, update);
        entity.setProperty(EMAIL.getKey(), memberModel.getEmail());
        entity.setProperty(POSITION.getKey(), memberModel.getPosition());
    }

    public static void setFromEvaluation(Entity entity, EvaluationModel evaluationModel){
        entity.setProperty(EvaluationsProperties.VISCERAL_FAT.getId(), evaluationModel.getVisceralfat());
        entity.setProperty(EvaluationsProperties.FAT.getId(), evaluationModel.getFat());
        entity.setProperty(EvaluationsProperties.MUSCLE_MASS.getId(), evaluationModel.getMuscle());
        entity.setProperty(EvaluationsProperties.HYDRATION.getId(), evaluationModel.getHydration());
        entity.setProperty(EvaluationsProperties.IMC.getId(), evaluationModel.getImc());
        entity.setProperty(EvaluationsProperties.METABOLIC_AGE.getId(), evaluationModel.getMetabolicage());
        entity.setProperty(EvaluationsProperties.MINERALIZATION.getId(), evaluationModel.getMineralization());
        entity.setProperty(EvaluationsProperties.WEIGHT.getId(), evaluationModel.getWeight());
        entity.setProperty(EvaluationsProperties.DATE.getId(), evaluationModel.getDate());
        entity.setProperty(EvaluationsProperties.ID.getId(), evaluationModel.getId());
        entity.setProperty(EvaluationsProperties.CONTACT_ID.getId(), evaluationModel.getContactId());
    }

    public static void setFromProduct(Entity entity, ProductModel productModel, Boolean update){
        if (!update){
            entity.setProperty(ProductProperties.PRODUCT_ID.getId(), productModel.getProductId());
        }
        entity.setProperty(ProductProperties.PRODUCT_DESCRIPTION.getId(), productModel.getDescription());
        entity.setProperty(ProductProperties.PRODUCT_NAME.getId(), productModel.getProductName());
        entity.setProperty(ProductProperties.PRODUCT_BLOB.getId(), productModel.getProductBlobKey());
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
            contactModel.setAge((Long) entity.getProperty(AGE.getKey()));
            contactModel.setWeight((Long) entity.getProperty(WEIGHT.getKey()));
            contactModel.setHeight((Long) entity.getProperty(HEIGHT.getKey()));
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
            contactModel.setAge((Long) entity.getProperty(AGE.getKey()));
            contactModel.setWeight((Long) entity.getProperty(WEIGHT.getKey()));
            contactModel.setHeight((Long) entity.getProperty(HEIGHT.getKey()));
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
            contactModel.setPosition((String) entity.getProperty(POSITION.getKey()));
            contactModel.setRecomendedBy((String) entity.getProperty(RECOMENDED_BY.getKey()));
            contactModel.setRecomendedById((Long) entity.getProperty(RECOMENDED_BY_ID.getKey()));
            contactModel.setType((String) entity.getProperty(TYPE.getKey()));
            contactModel.setAge((Long) entity.getProperty(AGE.getKey()));
            contactModel.setWeight((Long) entity.getProperty(WEIGHT.getKey()));
            contactModel.setHeight((Long) entity.getProperty(HEIGHT.getKey()));
            contactModel.setEmail((String) entity.getProperty(EMAIL.getKey()));
            return contactModel;
        }
    };

    public static Function<Entity, EvaluationModel> entityToEvaluation = new Function<Entity, EvaluationModel>() {
        @Override
        public EvaluationModel apply(com.google.appengine.api.datastore.Entity entity) {
            EvaluationModel evaluationModel = new EvaluationModel();
            evaluationModel.setId((Long) entity.getProperty(ID.getKey()));
            evaluationModel.setContactId((Long) entity.getProperty(EvaluationsProperties.CONTACT_ID.getId()));

            evaluationModel.setDate((String) entity.getProperty(DATE.getKey()));
            List<Integer> details = parseDate(evaluationModel.getDate());
            if(!details.isEmpty()){
                evaluationModel.setDateYear(details.get(0));
                evaluationModel.setDateMonth(details.get(1));
                evaluationModel.setDateDay(details.get(2));
            }

            evaluationModel.setWeight((Long) entity.getProperty(EvaluationsProperties.WEIGHT.getId()));
            evaluationModel.setFat((Long) entity.getProperty(EvaluationsProperties.FAT.getId()));
            evaluationModel.setHydration((Long) entity.getProperty(EvaluationsProperties.HYDRATION.getId()));
            evaluationModel.setImc((Long) entity.getProperty(EvaluationsProperties.IMC.getId()));
            evaluationModel.setMetabolicage((Long) entity.getProperty(EvaluationsProperties.METABOLIC_AGE.getId()));
            evaluationModel.setMineralization((Double) entity.getProperty(EvaluationsProperties.MINERALIZATION.getId()));
            evaluationModel.setMuscle((Long) entity.getProperty(EvaluationsProperties.MUSCLE_MASS.getId()));
            evaluationModel.setVisceralfat((Long) entity.getProperty(EvaluationsProperties.VISCERAL_FAT.getId()));
            return evaluationModel;
        }
    };

    public static Function<Entity, ProductModel> entityToProduct = new Function<Entity, ProductModel>() {
        @Override
        public ProductModel apply(com.google.appengine.api.datastore.Entity entity) {
            ProductModel productModel = new ProductModel();
            productModel.setProductId((Long) entity.getProperty(ProductProperties.PRODUCT_ID.getId()));
            productModel.setProductName((String) entity.getProperty(ProductProperties.PRODUCT_NAME.getId()));
            productModel.setDescription((String) entity.getProperty(ProductProperties.PRODUCT_DESCRIPTION.getId()));
            productModel.setProductBlobKey((String) entity.getProperty(ProductProperties.PRODUCT_BLOB.getId()));
            return productModel;
        }
    };
}
