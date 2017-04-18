/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import com.google.inject.Inject;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.manager.FileManager;
import ninja.Context;
import ninja.Renderable;
import ninja.Result;
import ninja.Results;
import ninja.params.PathParam;
import ninja.uploads.DiskFileItemProvider;
import ninja.uploads.FileItem;
import ninja.uploads.FileProvider;
import ninja.utils.ResponseStreams;

/**
 *
 * @author Tugrul
 */

public class FileController {
    
@Inject
FileManager fileManager;

private static final Logger LOG = Logger.getLogger(FileController.class.getName());

@FileProvider(DiskFileItemProvider.class)
public Result uploadFinish(Context context) throws Exception {
    LOG.info("Start method");
    //get parameters
    FileItem upfile = context.getParameterAsFileItem("upfile");
    String type = context.getParameter("type");
    String path = fileManager.uploadFile(upfile,type);
    
    LOG.log(Level.INFO, "Uploaded File in following Path: {0}", path); 
    
    return Results.redirect("/upload"); 
   }




    public Result downloadFinish(@PathParam("fileId") String id) {
        LOG.info("Start method");
        
                
        Renderable renderable = new Renderable() { 

        @Override 
        public void render(Context context, Result result) { 
          ResponseStreams responseStreams = context.finalizeHeaders(result); 
            try {
                OutputStream outputstream = responseStreams.getOutputStream(); // do what you want with the streams
                LOG.log(Level.INFO, "Result: {0} stream: {1}", new Object[]{result, outputstream});
                fileManager.downloadFile(outputstream,id);       
            
            } catch (IOException ex) {
                Logger.getLogger(ApplicationController.class.getName()).log(Level.SEVERE, null, ex);
            }
          } 
        }; 
      //  return new Results.ok.render(renderable); 
        return Results.redirect("/upload").render(renderable);

        
    }
    
}
