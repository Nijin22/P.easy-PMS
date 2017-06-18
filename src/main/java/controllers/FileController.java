/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import com.google.common.io.ByteStreams;
import com.google.inject.Inject;

import filters.LoginFilter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.beans.ProfilePicture;
import models.beans.ProjectFile;
import models.beans.Task;
import models.beans.TaskFile;
import models.manager.FileManager;
import models.manager.ProjectManager;
import models.manager.enums.FileType;
import ninja.Context;
import ninja.FilterWith;
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
@FilterWith(LoginFilter.class)
public class FileController {

    @Inject
    FileManager fileManager;

    @Inject
    ProjectManager projectManager;

    @Inject 
    NinjaProperties ninjaProperties;
    
    private static final Logger LOG = Logger.getLogger(FileController.class.getName());

    @FileProvider(DiskFileItemProvider.class)
    public Result uploadFinish(Context context, @PathParam("idOfOwner") String idOfOwner) throws Exception {
        //get parameters
        FileItem upfile = context.getParameterAsFileItem("file");
        String type = context.getParameter("type");
       
        LOG.log(Level.INFO, "Start method uploadFinish with parameters: File: {0}, type: {1}, IdOfOwner:{2}", new Object[]{upfile, type, idOfOwner});
        if(!upfile.getFileName().isEmpty()){
        //String ownerId = context.getParameter("id");
        String path = fileManager.uploadFile(upfile.getFile(), upfile.getFileName(), type, idOfOwner);
        LOG.log(Level.INFO, "Uploaded File in following Path: {0}", path);
        }
        	
        

        
        if(type.equals("project")){
        	return Results.redirect("/projects/"+idOfOwner);
        } else if(type.equals("task")){
        	Task task = projectManager.getTask(Long.parseLong(idOfOwner));
        	return Results.redirect("/projects/" + task.getProject().getProjectId() + "/tasks/" + idOfOwner);
        }else{
        	//it is a profilePicture
        	return null;
        }
        
        
        

    }

    public Result downloadFinish(@PathParam("fileId") String id, @PathParam("type") String type) throws IOException {
        LOG.log(Level.INFO, "Start method downloadFinish for type {0}", type);
        
        //Dummyyyyy value
//      File directory = new File("C:/Users/Tugrul/Desktop/TIM/TIM SoSe17.pdf");
//      File downloadFile = new File(directory.getCanonicalPath());
       
        String fileSystemId ="";
        String fileName="";
        
        if(type.equals(FileType.project.name())){
        	ProjectFile projectFile = fileManager.getProjectFile(id);
        	fileSystemId = id + projectFile.getFileType();
        	fileName =  projectFile.getTitle();
        	
        }else if(type.equals(FileType.task.name())){
        	
        	TaskFile taskFile = fileManager.getTaskFile(id); 
        	fileSystemId = id + taskFile.getFileType();
        	fileName =  taskFile.getTitle();
        	
        }else if(type.equals(FileType.picture.name())){
        	
        	ProfilePicture picture = fileManager.getProfilePicture(id); 
        	fileSystemId = id + picture.getFileType();
        	fileName =  picture.getTitle();
        	
        }else{
        	
        	throw new IllegalArgumentException("Filetype " + type +  " is not provided!");
        
        }
        
        File directory = new File(ninjaProperties.get("UploadDirectoryPath") + File.separator + type);
        File downloadFile = new File(directory.getCanonicalPath(),fileSystemId);
       
        System.out.println("Path : " + downloadFile.getAbsolutePath());

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

        return new Result(200).render(renderable).addHeader("Content-Transfer-Encoding", "binary").addHeader("Content-Disposition", "attachment; filename=\"" + fileName);
    }

    public Result deleteFinish(@PathParam("fileId") String fileId, @PathParam("type") String type, @PathParam("idOfOwner") String idOfOwner) throws IOException {
                
        fileManager.deleteFile(Long.parseLong(fileId), type);
        
        if(type.equals("project")){
            return Results.redirect("/projects/"+idOfOwner);
        }else if(type.equals("task")){
        	Task task = projectManager.getTask(Long.parseLong(idOfOwner));
        	return Results.redirect("/projects/" + task.getProject().getProjectId() + "/tasks/" + idOfOwner);       
        }else{
        	return null;
        }

    }

}
