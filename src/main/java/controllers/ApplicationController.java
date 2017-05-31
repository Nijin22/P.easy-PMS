package controllers;

import com.google.inject.Inject;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import models.beans.Milestone;
import models.beans.Organisation;
import models.beans.PeasyUser;
import models.beans.Project;
import models.beans.ProjectBlogEntry;
import models.beans.Task;
import models.manager.OrganisationManager;
import models.manager.ProjectManager;
import models.manager.UserManager;
import ninja.Result;
import ninja.Results;
import ninja.params.PathParam;
import ninja.session.Session;

public class ApplicationController {
    @Inject
    ProjectManager projectManager;
    @Inject
    OrganisationManager organisationManager;
    @Inject
    UserManager userManager;
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
	public Result editProject() {
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
	
	
	public Result milestone(@PathParam("milestoneID") String milestoneId) {
        Result result = Results.html();
        Milestone milestone = projectManager.getMilestone(Long.parseLong(milestoneId));
        
        result.render("project",milestone.getProject());
        result.render("milestone",milestone);
        result.render("tasks",milestone.getTasks());
        
  
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
            result.render("members", project.getProjectMembers());
            
    		List<PeasyUser> peasyUsers = projectManager.getAllPeasyusers();
    		
    		Set<PeasyUser> peasyUserswithoutCurrentProject = new HashSet<>();
    		for(PeasyUser peasyUser : peasyUsers){
    			int a = 0;
    			for(Project assignedProject : peasyUser.getProjects()){
    				if(assignedProject.getProjectId().equals(Long.parseLong(projectId))){
    					 a = 1;
    				}	
    			}
    			if(a==0){
    				peasyUserswithoutCurrentProject.add(peasyUser);
    			}    			
    		}
            result.render("users",peasyUserswithoutCurrentProject);
            
    		Set<PeasyUser> peasyUserswithoutCurrentManager = new HashSet<>();

    		for(PeasyUser peasyUser : peasyUsers){
    			int a = 0;
    			for(Project projectmanager : peasyUser.getProjectsWhereUserIsManager()){
    				if(projectmanager.getProjectId().equals(Long.parseLong(projectId))){
    					 a = 1;
    				}	
    			}
    			if(a==0){
    				peasyUserswithoutCurrentManager.add(peasyUser);
    				System.out.println("manager: " + peasyUser.toString());
    			}	
    			
    		}
              
            result.render("userManagers",peasyUserswithoutCurrentManager);

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
         
         //Taskmebers
         result.render("members", task.getUsers());
         
         return result;
	}

	public Result projects(Session session) {

	    Result result = Results.html();
	    String email = session.get("email");

        PeasyUser peasyUser = userManager.getUser(email);
        Set<Project> distinct = new HashSet<Project>(); 
        distinct.addAll(peasyUser.getProject());
        distinct.addAll(peasyUser.getProjectsWhereUserIsManager());

        //Project 
        result.render("projects", distinct);      
        
        return result;
	}

	public Result report(@PathParam("projectID") String projectId) {
		
        Result result = Results.html();
        Project project = projectManager.getProject(Long.parseLong(projectId));
        
        //Project Parameters
        result.render("project", project);
       
        return result;
	}

	public Result tasks(@PathParam("projectID") String projectId) {
		
		//projectId is not needed until now
		
		 Result result = Results.html();
         Project project = projectManager.getProject(Long.parseLong(projectId));
       
         //Project 
         result.render("project", project);      
        
         //Tasks 
         result.render("tasks", project.getTasks());      
        
         
         return result;
	}



	public Result fileUpload() {
		return Results.html();

	}

	public Result index() {
		return Results.html();

	}
}