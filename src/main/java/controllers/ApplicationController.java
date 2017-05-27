package controllers;

import com.google.inject.Inject;

import java.util.Set;
import models.beans.Organisation;
import models.beans.Project;
import models.beans.ProjectBlogEntry;
import models.beans.Task;
import models.manager.OrganisationManager;
import models.manager.ProjectManager;
import ninja.Result;
import ninja.Results;
import ninja.params.PathParam;

public class ApplicationController {
    @Inject
    ProjectManager projectManager;
    @Inject
    OrganisationManager organisationManager;
	public ApplicationController() {

	}

	public Result account() {
		return Results.html();

	}

	public Result dashboard() {
		return Results.html();

	}

	public Result impress() {
		return Results.html();

	}

	public Result information() {
		return Results.html();
	}

	public Result login() {
		return Results.html();

	}

	public Result forgotPassword() {
		return Results.html();

	}

	public Result members(@PathParam("projectID") String projectId) {
        Result result = Results.html();
        Project project = projectManager.getProject(Long.parseLong(projectId));
      
        //Project 
        result.render("project", project);    
     
        //Project members
        result.render("members", project.getProjectMembers());
        System.out.println("Size of project-ProjectMembers: " + project.getProjectMembers().size());
       
        return result;
	}

	public Result milestones(@PathParam("projectID") String projectId) {
        Result result = Results.html();
        Project project = projectManager.getProject(Long.parseLong(projectId));
        
        //Project Parameters
        result.render("project", project);
        result.render("milestones", project.getMilestones());
       
        return result;
	}

	public Result myCalender() {
		return Results.html();

	}

	public Result organization(@PathParam("id") String id) {
                Result result = Results.html();
                //eigentlich org vom user auslesen.
                Organisation organization = organisationManager.getOrganisation(Integer.parseInt(id));
                
                System.out.println("Size of users =) " +  organization.getUsers().size());
                
                result.render("members",organization.getUsers());  
                return result;

	}

	public Result project(@PathParam("projectID") String projectId) {
            Result result = Results.html();
            Project project = projectManager.getProject(Long.parseLong(projectId));
            Set<ProjectBlogEntry> blogEntries = project.getBlogEntries();
            
            //Project Parameters
            result.render("project", project);
            System.out.println("Size of project-Blogentries: " + project.getBlogEntries().size());
            //Blog entries
            result.render("blogEntries",project.getBlogEntries());
            //Files
            System.out.println("Size of project-Files: " + project.getProjectFiles().size());
            result.render("files", project.getProjectFiles());
            
           
            return result;

	}
	
	public Result task(@PathParam("projectID") String projectId, @PathParam("taskID") String taskId) {
		
		//projectId is not needed until now
		
		 Result result = Results.html();
         Task task = projectManager.getTask(Long.parseLong(taskId));
       
         //Project 
         result.render("project", task.getProject());      
        
         //Project task
         result.render("task", task);
      
         //Files
         result.render("files", task.getTaskFiles());
         
        
         return result;
	}

	public Result projects() {
		return Results.html();

	}

	public Result report(@PathParam("projectID") String projectId) {
		
        Result result = Results.html();
        Project project = projectManager.getProject(Long.parseLong(projectId));
        
        //Project Parameters
        result.render("project", project);
       
        return result;
	}

	public Result tasks() {
		return Results.html();

	}



	public Result fileUpload() {
		return Results.html();

	}

	public Result index() {
		return Results.html();

	}
}
