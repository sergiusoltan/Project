package main.java.util;

import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.blobstore.UploadOptions;

/**
 * User: Sergiu Soltan
 */
public class UploadServiceUtil {

    public static void uploadImage(){
        BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
    }
}
