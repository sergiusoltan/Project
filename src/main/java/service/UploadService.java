package main.java.service;

import com.sun.jersey.multipart.FormDataParam;
import main.java.model.auth.Authorization;
import main.java.util.ContactServiceUtil;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.io.InputStream;

import static main.java.util.Utils.noAuthResponse;
import static main.java.util.Utils.oKResponse;
import static main.java.util.Utils.response;

/**
 * User: Sergiu Soltan
 */
@Path("/upload")
public class UploadService {
//    @POST
//    @Path("/")
//    @Consumes(MediaType.MULTIPART_FORM_DATA)
//    public Response uploadFile(
//            @FormDataParam("file") InputStream uploadedInputStream,
//            @FormDataParam("file") com.sun.jersey.core.header.FormDataContentDisposition fileDetail) {
//
//        String uploadedFileLocation = "d://uploaded/" + fileDetail.getFileName();
//
//        return Response.status(200).entity("").build();
//
//    }

}
