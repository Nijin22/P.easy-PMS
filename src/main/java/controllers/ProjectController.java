package controllers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import filters.LoginFilter;
import models.beans.Milestone;
import models.beans.PeasyUser;
import models.beans.Project;
import models.beans.ProjectStatus;
import models.beans.Task;
import models.manager.ProjectManager;
import models.manager.UserManager;
import ninja.Context;
import ninja.FilterWith;
import ninja.Result;
import ninja.Results;
import ninja.exceptions.BadRequestException;
import ninja.params.PathParam;

@Singleton
@FilterWith(LoginFilter.class)
public class ProjectController {

	@Inject
	private ProjectManager projectManager;
	@Inject
	private UserManager userManager;

	public Result createProject(@PathParam("email") String email) {
		Project project = projectManager.createMasterProject(email);

		return Results.redirect("/projects/" + project.getProjectId());
	}

	public Result createMilestone(@PathParam("projectId") String projectId, Context context) {
		verifyAccessOrBreak(projectId, context);
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate localDate = LocalDate.now();

		Milestone milestone = projectManager.createMilestone(Long.parseLong(projectId), "initial Milestone",
				dtf.format(localDate));

		return Results.redirect("/projects/" + projectId + "/milestones/" + milestone.getMileStoneId());
	}

	public Result deleteMilestone(@PathParam("projectId") String projectId,
			@PathParam("milestoneId") String milestoneId, Context context) {
		verifyAccessOrBreak(projectId, context);
		
		projectManager.deleteMilestone(Long.parseLong(milestoneId));

		return Results.redirect("/projects/" + projectId + "/milestones");
	}

	public Result createTask(@PathParam("projectId") String projectId, Context context) {
		verifyAccessOrBreak(projectId, context);
		
		Task task = projectManager.createTask(Long.parseLong(projectId), "initial Task");
		return Results.redirect("/projects/" + projectId + "/tasks/" + task.getTaskId());
	}

	public Result changeProjectname(@PathParam("projectId") String projectId, @PathParam("newName") String newName, Context context) {
		verifyAccessOrBreak(projectId, context);
		
		Project project = projectManager.changeProjectname(Long.parseLong(projectId), newName);

		if (project.getName().equals(newName)) {
			return Results.ok();
		} else {
			return Results.badRequest();
		}
	}

	public Result addBlogEntry(@PathParam("projectId") String projectId, @PathParam("email") String email,
			@PathParam("title") String title, @PathParam("text") String text, Context context) {
		verifyAccessOrBreak(projectId, context);
		
		projectManager.createBlogEntry(Long.parseLong(projectId), email, title, text);
		return Results.ok();
	}

	public Result deleteBlogEntry(@PathParam("projectId") String projectId,
			@PathParam("blogEntryId") String blogEntryId, Context context) {
		verifyAccessOrBreak(projectId, context);
		
		projectManager.deleteBlogEntry(Integer.parseInt(blogEntryId));
		return Results.ok();
	}

	public Result changeTaskName(@PathParam("taskId") String taskId, @PathParam("newName") String newName, Context context) {
		Task task = projectManager.changeTaskname(Long.parseLong(taskId), newName);
		
		verifyAccessOrBreak(task.getProject().getProjectId().toString(), context);
		
		if (task.getName().equals(newName)) {
			return Results.ok();
		} else {
			return Results.badRequest();
		}
	}

	public Result changeProjectDescription(@PathParam("projectId") String projectId,
			@PathParam("description") String description, Context context) {
		verifyAccessOrBreak(projectId, context);
		
		Project project = projectManager.changeProjectDescription(Long.parseLong(projectId), description);

		if (project.getDescription().equals(description)) {
			return Results.ok();
		} else {
			return Results.badRequest();
		}
	}

	public Result changeTaskDescription(@PathParam("taskId") String taskId,
			@PathParam("description") String description, Context context) {
		
		Task task = projectManager.changeTaskDescription(Long.parseLong(taskId), description);
		
		verifyAccessOrBreak(task.getProject().getProjectId().toString(), context);

		if (task.getDescription().equals(description)) {
			return Results.ok();
		} else {
			return Results.badRequest();
		}
	}

	public Result changeTaskEffort(@PathParam("taskId") String taskId, @PathParam("effort") String effort, Context context) {
		Task task = projectManager.changeTaskEffort(Long.parseLong(taskId), effort);
		
		verifyAccessOrBreak(task.getProject().getProjectId().toString(), context);

		if (task.getEffort().equals(effort)) {
			return Results.ok();
		} else {
			return Results.badRequest();
		}
	}

	public Result changeTaskProgress(@PathParam("taskId") String taskId, @PathParam("progress") String progress, Context context) {
		Task task = projectManager.changeTaskProgress(Long.parseLong(taskId), progress);
		
		verifyAccessOrBreak(task.getProject().getProjectId().toString(), context);

		if (String.valueOf(task.getProgress()).equals(progress)) {
			return Results.ok();
		} else {
			return Results.badRequest();
		}
	}

	public Result addTaskDep(@PathParam("taskId") String taskId, @PathParam("taskDepId") String taskDepId, Context context) {
		Task task = projectManager.getTask(Long.parseLong(taskId));
		verifyAccessOrBreak(task.getProject().getProjectId().toString(), context);
		
		projectManager.addTaskDependency(Long.parseLong(taskId), Long.parseLong(taskDepId));
		return Results.ok();
	}

	public Result removeTaskDep(@PathParam("taskId") String taskId, @PathParam("taskDepId") String taskDepId, Context context) {
		Task task = projectManager.getTask(Long.parseLong(taskId));
		verifyAccessOrBreak(task.getProject().getProjectId().toString(), context);
		
		projectManager.removeTaskDependency(Long.parseLong(taskId), Long.parseLong(taskDepId));
		return Results.ok();
	}

	public Result changeProjectStart(@PathParam("projectId") String projectId,
			@PathParam("projectStart") String projectStart, Context context) {
		verifyAccessOrBreak(projectId, context);
		
		Project project = projectManager.changeProjectStart(Long.parseLong(projectId), projectStart);

		if (project.getStart().equals(projectStart)) {
			return Results.ok();
		} else {
			return Results.badRequest();
		}

	}

	public Result changeProjectEnd(@PathParam("projectId") String projectId,
			@PathParam("projectDeadline") String projectDeadline, Context context) {
		verifyAccessOrBreak(projectId, context);
		
		Project project = projectManager.changeProjectEnd(Long.parseLong(projectId), projectDeadline);

		if (project.getDeadline().equals(projectDeadline)) {
			return Results.ok();
		} else {
			return Results.badRequest();
		}

	}

	public Result changeMilestoneEnd(@PathParam("mileStoneId") String milestoneId,
			@PathParam("milestoneDeadline") String milestoneDeadline, Context context) {
		
		Milestone milestone = projectManager.changeMilestoneEnd(Long.parseLong(milestoneId), milestoneDeadline);
		
		verifyAccessOrBreak(milestone.getProject().getProjectId().toString(), context);

		if (milestone.getDeadline().equals(milestoneDeadline)) {
			return Results.ok();
		} else {
			return Results.badRequest();
		}

	}

	public Result changeMilestoneName(@PathParam("mileStoneId") String milestoneId,
			@PathParam("milestoneName") String milestoneName, Context context) {
		
		Milestone milestone = projectManager.changeMilestoneName(Long.parseLong(milestoneId), milestoneName);

		verifyAccessOrBreak(milestone.getProject().getProjectId().toString(), context);
		
		if (milestone.getName().equals(milestoneName)) {
			return Results.ok();
		} else {
			return Results.badRequest();
		}

	}

	public Result changeProjectBudget(@PathParam("projectId") String projectId,
			@PathParam("projectBudget") String projectBudget, Context context) {
		verifyAccessOrBreak(projectId, context);
		
		Project project = projectManager.changeProjectBudget(Long.parseLong(projectId), projectBudget);

		if (project.getDescription().equals(projectBudget)) {
			return Results.ok();
		} else {
			return Results.badRequest();
		}

	}

	public Result deleteUserFromProject(@PathParam("projectId") String projectId, @PathParam("email") String email, Context context) {
		verifyAccessOrBreak(projectId, context);
		
		Project project = projectManager.unassignUserFromProject(Long.parseLong(projectId), email);
		PeasyUser peasyUser = userManager.getUser(email);

		if (!project.getProjectMembers().contains(peasyUser)) {
			return Results.ok();
		} else {
			return Results.badRequest();
		}

	}

	public Result deleteUserFromTask(@PathParam("taskId") String taskId, @PathParam("email") String email, Context context) {
		Task task = projectManager.unassignUserFromTask(Long.parseLong(taskId), email);
		PeasyUser peasyUser = userManager.getUser(email);
		
		verifyAccessOrBreak(task.getProject().getProjectId().toString(), context);

		if (!task.getUsers().contains(peasyUser)) {
			return Results.ok();
		} else {
			return Results.badRequest();
		}

	}

	public Result addUsertoProject(@PathParam("projectId") String projectId, @PathParam("email") String email, Context context) {
		verifyAccessOrBreak(projectId, context);
		
		Project project = projectManager.assignUserToProject(Long.parseLong(projectId), email);
		PeasyUser peasyUser = userManager.getUser(email);

		if (project.getProjectMembers().contains(peasyUser)) {
			return Results.ok();
		} else {
			return Results.badRequest();
		}

	}

	public Result addTasktoMilestone(@PathParam("milestoneId") String milestoneId, @PathParam("taskId") String taskId, Context context) {
		Milestone milestone = projectManager.addTasktoMilestone(Long.parseLong(milestoneId), Long.parseLong(taskId));
		Task task = projectManager.getTask(Long.parseLong(taskId));

		verifyAccessOrBreak(milestone.getProject().getProjectId().toString(), context);
		
		if (milestone.getTasks().contains(task)) {
			return Results.ok();
		} else {
			return Results.badRequest();
		}

	}

	public Result deleteTaskfromMilestone(@PathParam("milestoneId") String milestoneId,
			@PathParam("taskId") String taskId, Context context) {
		
		Milestone milestone = projectManager.deleteTaskfromMilestone(Long.parseLong(milestoneId),
				Long.parseLong(taskId));
		Task task = projectManager.getTask(Long.parseLong(taskId));
		
		verifyAccessOrBreak(milestone.getProject().getProjectId().toString(), context);

		if (!milestone.getTasks().contains(task)) {
			return Results.ok();
		} else {
			return Results.badRequest();
		}

	}

	public Result addUsertoTask(@PathParam("taskId") String taskId, @PathParam("email") String email, Context context) {
		Task task = projectManager.assignUserToTask(Long.parseLong(taskId), email);
		PeasyUser peasyUser = userManager.getUser(email);
		
		
		verifyAccessOrBreak(task.getProject().getProjectId().toString(), context);

		if (task.getUsers().contains(peasyUser)) {
			return Results.ok();
		} else {
			return Results.badRequest();
		}

	}

	public Result changeManager(@PathParam("projectId") String projectId, @PathParam("email") String email, Context context) {
		verifyAccessOrBreak(projectId, context);
		
		projectManager.changeProjectmanager(Long.parseLong(projectId), email);
		return Results.ok();

	}

	public Result changeStatus(@PathParam("projectId") String projectId, @PathParam("status") String status, Context context) {
		verifyAccessOrBreak(projectId, context);
		
		ProjectStatus enumStatus = ProjectStatus.valueOf(status);
		Project project = projectManager.changeProjectState(Long.parseLong(projectId), enumStatus);

		if (project.getStatus().name().contains(status)) {
			return Results.ok();
		} else {
			return Results.badRequest();
		}
	}
	
	private void verifyAccessOrBreak(String projectId, Context context){
		Project project = projectManager.getProject(Long.parseLong(projectId));
		String email = (context.getSession().get("email"));
		
		// Quickfail
		if (project == null) {
			throw new BadRequestException("Attempted to access a project for which you have no access.");
		}
		
		if (project.getProjectManager().getEmailAddress().equals(email)){
			return;
		}
		
		
		for (PeasyUser user : project.getProjectMembers()){
			if (user.getEmailAddress().equals(email)){
				return;
			}
		}
		
		context.getFlashScope().error("auth.noAuth");
		throw new BadRequestException("Attempted to access a project for which you have no access.");
	}

}
