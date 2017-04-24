/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import com.google.common.io.ByteStreams;
import com.google.inject.Inject;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.MimetypesFileTypeMap;
import models.beans.FileClass;
import models.beans.TaskFile;
import models.manager.FileManager;
import ninja.Context;
import ninja.Renderable;
import ninja.Result;
import ninja.Results;
import ninja.params.PathParam;
import ninja.uploads.DiskFileItemProvider;
import ninja.uploads.FileItem;
import ninja.uploads.FileProvider;
import ninja.utils.NinjaProperties;
import ninja.utils.ResponseStreams;

/**
 *
 * @author Tugrul
 */
public class FileController {

    @Inject
    FileManager fileManager;

//    @Inject 
//    NinjaProperties ninjaProperties;
    private static final Logger LOG = Logger.getLogger(FileController.class.getName());

    @FileProvider(DiskFileItemProvider.class)
    public Result uploadFinish(Context context, @PathParam("idOfOwner") String idOfOwner) throws Exception {
        //get parameters
        FileItem upfile = context.getParameterAsFileItem("upfile");
        String type = context.getParameter("type");
       
        LOG.log(Level.INFO, "Start method uploadFinish with parameters: File: {0}, type: {1}, IdOfOwner:{2}", new Object[]{upfile, type, idOfOwner});
        
        //String ownerId = context.getParameter("id");
        String path = fileManager.uploadFile(upfile.getFile(), upfile.getFileName(), type, idOfOwner);

        LOG.log(Level.INFO, "Uploaded File in following Path: {0}", path);

        return Results.redirect("/upload");
    }

    public Result downloadFinish(@PathParam("fileId") String id, @PathParam("type") String type) throws IOException {
        LOG.log(Level.INFO, "Start method downloadFinish for type {0}", type);
      
        //########################
        //TODO: Provide flexible Path also in renderable and set right nam ewith extension, merge with dennis part to access the database and get TaskFike 
        //##########
       
        //TaskFile taskFile = fileManager.getTaskFile(id); 
        //getId + getFileType for download
        //File directory = new File(ninjaProperties.get("UploadDirectoryPath") + File.separator + type);
        //File downloadFile = new File(directory.getCanonicalPath(),id); 
      
        
        //Dummyyyyy value
        File directory = new File("C:/Users/Tugrul/Desktop/TIM/TIM SoSe17.pdf");
        File downloadFile = new File(directory.getCanonicalPath());

        Renderable renderable = new Renderable() {
            @Override
            public void render(Context context, Result result) {
                try {
                    //Stream File
                    InputStream stream = new FileInputStream(downloadFile);
                    ResponseStreams responseStreams = context.finalizeHeaders(result);
                    ByteStreams.copy(stream, responseStreams.getOutputStream());
                } catch (IOException ex) {
                    LOG.log(Level.SEVERE, null, ex.getMessage());
                }
            }
        };

        return new Result(200).render(renderable).addHeader("Content-Transfer-Encoding", "binary").addHeader("Content-Disposition", "attachment; filename=\"" + downloadFile.getName());
    }

    public Result deleteFinish(@PathParam("fileId") String fileId, @PathParam("type") String type) throws IOException {
                
        fileManager.deleteFile(Long.parseLong(fileId), type);
        
        //provide return ok statement if delete
        return null;
    }

}
