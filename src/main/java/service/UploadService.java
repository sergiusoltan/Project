package main.java.service;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;
import main.java.model.auth.Authorization;
import main.java.model.people.ProductModel;
import main.java.util.UploadServiceUtil;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.Collection;

import static main.java.util.ContactServiceUtil.isAuthorizedRequest;
import static main.java.util.Utils.noAuthResponse;
import static main.java.util.Utils.oKResponse;

/**
 * User: Sergiu Soltan
 */
@Path("/uploads")
public class UploadService {

    @Path("/url")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getClient(@HeaderParam("authorization") String authParam) {
        Authorization auth = new Authorization(authParam);
        if(!isAuthorizedRequest(auth)){
            return noAuthResponse();
        }

        BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
        String response = blobstoreService.createUploadUrl("/rest/uploads/create");
        ProductModel productModel = new ProductModel(response);
        return oKResponse(productModel);
    }

    @Path("/getAll")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllProducts(@HeaderParam("authorization") String authParam) {
        Authorization auth = new Authorization(authParam);
        if(!isAuthorizedRequest(auth)){
            return noAuthResponse();
        }

        Collection<ProductModel> availableProducts = UploadServiceUtil.getAllProducts(auth.getEmail());
        return oKResponse(availableProducts);
    }

    @Path("/image/{key}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getImage(@HeaderParam("authorization") String authParam, @PathParam("key") String keyString) {
        Authorization auth = new Authorization(authParam);
        if(!isAuthorizedRequest(auth)){
            return noAuthResponse();
        }
        BlobKey blobKey1 = new BlobKey(keyString);
        ImagesService imagesService = ImagesServiceFactory.getImagesService();
        String imageUrl = imagesService.getServingUrl(ServingUrlOptions.Builder.withBlobKey(blobKey1));

        return Response.ok().entity(imageUrl).build();
    }


    @POST
    @Path("/create")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile(@Context HttpServletRequest req, @HeaderParam("authorization") String authParam) {

        Authorization auth = new Authorization(authParam);
        if(!isAuthorizedRequest(auth)){
            return noAuthResponse();
        }
        UploadServiceUtil.createOrUpdate(req, auth.getEmail());
        return oKResponse("");

    }


}
