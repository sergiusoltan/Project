package main.java.util;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.datastore.*;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;
import com.google.appengine.labs.repackaged.com.google.common.collect.Lists;
import main.java.model.people.ProductModel;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static main.java.util.Utils.entityToProduct;
import static main.java.util.Utils.setFromProduct;

/**
 * User: Sergiu Soltan
 */
public class UploadServiceUtil {


    public static void createOrUpdate(HttpServletRequest request, String owner){
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
        Map<String, List<BlobKey>> uploadedKeys = blobstoreService.getUploads(request);

        ProductModel productModel = ProductModel.fromString(request.getParameter("item"));
        Boolean isPersisted = productModel.getProductId() != null;
        BlobKey blobKey = uploadedKeys.get("file").get(0);
        String servingUrl = ImagesServiceFactory.getImagesService().getServingUrl(ServingUrlOptions.Builder
                .withBlobKey(blobKey)
                .crop(false));
        if(isPersisted){
            blobstoreService.delete(new BlobKey(productModel.getProductBlobKey()));
        }
        productModel.setProductBlobKey(blobKey.getKeyString());
        productModel.setProductImageUrl(servingUrl);
        Key userKey = Utils.getUserKey(owner);
        Entity product = null;
        if(isPersisted){
            Query query = new Query(Entities.PRODUCTS.getId(), userKey)
                    .setFilter(new Query.FilterPredicate(ProductProperties.PRODUCT_ID.getId(), Query.FilterOperator.EQUAL, productModel.getProductId()));
            product = datastoreService.prepare(query).asSingleEntity();
            setFromProduct(product, productModel, true);
        }else {
            productModel.setProductId(new Date().getTime());
            product = new Entity(Entities.PRODUCTS.getId(), userKey);
            setFromProduct(product, productModel, false);
        }
        datastoreService.put(product);
    }

    public static Collection<ProductModel> getAllProducts(String owner){
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        Key userKey = Utils.getUserKey(owner);
        Query query = new Query(Entities.PRODUCTS.getId(), userKey);

        List<Entity> entityList = datastoreService.prepare(query).asList(FetchOptions.Builder.withDefaults());
        return Lists.transform(entityList, Utils.entityToProduct);
    }

    public static Collection<ProductModel> deleteProduct(String owner, Long productId){
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        BlobstoreService blobStoreService = BlobstoreServiceFactory.getBlobstoreService();
        Key userKey = Utils.getUserKey(owner);
        Query query = new Query(Entities.PRODUCTS.getId(), userKey);
        query.setFilter(new Query.FilterPredicate(ProductProperties.PRODUCT_ID.getId(), Query.FilterOperator.EQUAL, productId));
        Entity entity = datastoreService.prepare(query).asSingleEntity();
        ProductModel productModel = Lists.transform(Lists.newArrayList(entity),entityToProduct).get(0);

        datastoreService.delete(entity.getKey());
        blobStoreService.delete(new BlobKey(productModel.getProductBlobKey()));

        return getAllProducts(owner);
    }
}
