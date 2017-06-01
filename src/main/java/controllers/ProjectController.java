package controllers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import models.beans.Milestone;
import models.beans.PeasyUser;
import models.beans.Project;
import models.beans.ProjectStatus;
import models.beans.Task;
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
	
	public Result createProject(@PathParam("email") String email){
		
		Project project = projectManager.createMasterProject(email);
		
	    return Results.redirect("/projects/"+project.getProjectId());
	}
	
	
	public Result createMilestone(@PathParam("projectId") String projectId){
	   
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	    LocalDate localDate = LocalDate.now();
		
	    Milestone milestone = projectManager.createMilestone(Long.parseLong(projectId), "initial Milestone", dtf.format(localDate));
		
		return Results.redirect("/projects/"+projectId+"/milestones/"+milestone.getMileStoneId());
	}
	
	
	public Result createTask(@PathParam("projectId") String projectId){
		
	    Task task = projectManager.createTask(Long.parseLong(projectId), "initial Task");
		
		return Results.redirect("/projects/"+projectId+"/tasks/"+task.getTaskId());
	}
	
	
	
	public Result changeProjectname(@PathParam("projectId") String projectId,@PathParam("newName") String newName){
		Project project = projectManager.changeProjectname(Long.parseLong(projectId), newName);
		
		if(project.getName().equals(newName)){
			return Results.ok();
		}else{
			return Results.badRequest();
		}
		
	}

	public Result addBlogEntry(@PathParam("projectId") String projectId,@PathParam("email") String email,@PathParam("title") String title,@PathParam("text") String text){
		System.out.println(projectId + " " + email + " " + title + " " + text + " ");
		projectManager.createBlogEntry( Long.parseLong(projectId), email, title, text);
		return Results.ok();
		}
	
	public Result deleteBlogEntry(@PathParam("projectId") String projectId,@PathParam("blogEntryId") String blogEntryId){
		projectManager.deleteBlogEntry(Integer.parseInt(blogEntryId));
		return Results.ok();
		}
	
	public Result changeTaskName(@PathParam("taskId") String taskId,@PathParam("newName") String newName){
		System.out.println("Bin hier");
		Task task = projectManager.changeTaskname(Long.parseLong(taskId), newName);
		System.out.println("Bin hier" +  task.getName() +newName );
		if(task.getName().equals(newName)){
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
	
	
	public Result changeTaskDescription(@PathParam("taskId") String taskId,@PathParam("description") String description){
		Task task = projectManager.changeTaskDescription(Long.parseLong(taskId), description);
		
		if(task.getDescription().equals(description)){
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
	
	public Result deleteUserFromTask(@PathParam("taskId") String taskId,@PathParam("email") String email){
		Task task = projectManager.unassignUserFromTask(Long.parseLong(taskId), email);
		PeasyUser peasyUser = userManager.getUser(email);
		
		if(!task.getUsers().contains(peasyUser)){
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
	
	public Result addTasktoMilestone(@PathParam("milestoneId") String milestoneId,@PathParam("taskId") String taskId){
		Milestone milestone = projectManager.addTasktoMilestone(Long.parseLong(milestoneId),Long.parseLong(taskId));
		Task task = projectManager.getTask(Long.parseLong(taskId));
		
		if(milestone.getTasks().contains(task)){
			return Results.ok();
		}else{
			return Results.badRequest();
		}
		
	}
	
	
	public Result deleteTaskfromMilestone(@PathParam("milestoneId") String milestoneId,@PathParam("taskId") String taskId){
		System.out.println("Ready to delete " +milestoneId+ " " +taskId);
		Milestone milestone = projectManager.deleteTaskfromMilestone(Long.parseLong(milestoneId),Long.parseLong(taskId));
		Task task = projectManager.getTask(Long.parseLong(taskId));
		
		if(!milestone.getTasks().contains(task)){
			return Results.ok();
		}else{
			return Results.badRequest();
		}
		
	}
	
	
	public Result addUsertoTask(@PathParam("taskId") String taskId,@PathParam("email") String email){
		Task task = projectManager.assignUserToTask(Long.parseLong(taskId), email);
		PeasyUser peasyUser = userManager.getUser(email);
		
		if(task.getUsers().contains(peasyUser)){
			return Results.ok();
		}else{
			return Results.badRequest();
		}
		
	}
	
	public Result changeManager(@PathParam("projectId") String projectId,@PathParam("email") String email){
		Project project = projectManager.changeProjectmanager(Long.parseLong(projectId), email);
		
	    return Results.ok();
	
	}
	
	public Result changeStatus(@PathParam("projectId") String projectId,@PathParam("status") String status){
		ProjectStatus enumStatus = ProjectStatus.valueOf(status);
		Project project = projectManager.changeProjectState(Long.parseLong(projectId), enumStatus);
		
		if(project.getStatus().name().contains(status)){
			return Results.ok();
		}else{
			return Results.badRequest();
		}	
	}
	
}
