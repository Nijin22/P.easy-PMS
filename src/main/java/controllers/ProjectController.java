package controllers;

import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import models.beans.PeasyUser;
import models.beans.Project;
import models.manager.ProjectManager;
import models.manager.UserManager;
import ninja.Result;
import ninja.Results;
import ninja.params.PathParam;

@Singleton
public class ProjectController {

	@Inject
	private ProjectManager projectManager;
	@Inject
	private UserManager userManager;
	
	public Result addBlogEntry(@PathParam("projectId") String projectId,@PathParam("email") String email,@PathParam("title") String title,@PathParam("text") String text){
		projectManager.createBlogEntry( Long.parseLong(projectId), email, title, text);
		return Results.redirect("/projects/" + projectId);
	}
	
	public Result addBlogEntry(@PathParam("projectId") String projectId,@PathParam("blogEntryId") String blogEntryId){
		projectManager.deleteBlogEntry(Integer.parseInt(blogEntryId));
		return Results.redirect("/projects/" + projectId);
	}
	
	public Result changeProjectname(@PathParam("projectId") String projectId,@PathParam("newName") String newName){
		Project project = projectManager.changeProjectname(Long.parseLong(projectId), newName);
		
		if(project.getName().equals(newName)){
			return Results.ok();
		}else{
			return Results.badRequest();
		}
		
	}
	
	public Result changeProjectDescription(@PathParam("projectId") String projectId,@PathParam("description") String description){
		Project project = projectManager.changeProjectDescription(Long.parseLong(projectId), description);
		
		if(project.getDescription().equals(description)){
			return Results.ok();
		}else{
			return Results.badRequest();
		}
		
	}
	
	public Result changeProjectStart(@PathParam("projectId") String projectId,@PathParam("projectStart") String projectStart){
		Project project = projectManager.changeProjectStart(Long.parseLong(projectId), projectStart);
		
		if(project.getDescription().equals(projectStart)){
			return Results.ok();
		}else{
			return Results.badRequest();
		}
		
	}
	
	public Result changeProjectEnd(@PathParam("projectId") String projectId,@PathParam("projectDeadline") String projectDeadline){
		Project project = projectManager.changeProjectEnd(Long.parseLong(projectId), projectDeadline);
		
		if(project.getDescription().equals(projectDeadline)){
			return Results.ok();
		}else{
			return Results.badRequest();
		}
		
	}
	
	public Result changeProjectBudget(@PathParam("projectId") String projectId,@PathParam("projectBudget") String projectBudget){
		Project project = projectManager.changeProjectBudget(Long.parseLong(projectId), projectBudget);
		
		if(project.getDescription().equals(projectBudget)){
			return Results.ok();
		}else{
			return Results.badRequest();
		}
		
	}
	
	
	public Result deleteUserFromProject(@PathParam("projectId") String projectId,@PathParam("email") String email){
		Project project = projectManager.unassignUserFromProject(Long.parseLong(projectId), email);
		PeasyUser peasyUser = userManager.getUser(email);
		
		if(!project.getProjectMembers().contains(peasyUser)){
			return Results.ok();
		}else{
			return Results.badRequest();
		}
		
	}
	
	public Result addUsertoProject(@PathParam("projectId") String projectId,@PathParam("email") String email){
		Project project = projectManager.assignUserToProject(Long.parseLong(projectId), email);
		PeasyUser peasyUser = userManager.getUser(email);
		
		if(project.getProjectMembers().contains(peasyUser)){
			return Results.ok();
		}else{
			return Results.badRequest();
		}
		
	}
	
	
}
