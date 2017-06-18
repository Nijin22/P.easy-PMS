package controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import filters.LoginFilter;
import models.beans.Milestone;
import models.beans.PeasyUser;
import models.beans.Project;
import models.beans.ProjectStatus;
import models.beans.Task;
import models.manager.OrganisationManager;
import models.manager.ProjectManager;
import models.manager.UserManager;
import ninja.Context;
import ninja.FilterWith;
import ninja.Result;
import ninja.Results;
import ninja.exceptions.BadRequestException;
import ninja.params.PathParam;
import ninja.session.Session;

@Singleton
@FilterWith(LoginFilter.class)
public class ProjectController {

	@Inject
	private ProjectManager projectManager;
	@Inject
	private UserManager userManager;
	@Inject
	OrganisationManager organisationManager;

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

	public Result changeProjectname(@PathParam("projectId") String projectId, @PathParam("newName") String newName,
			Context context) {
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

	public Result changeTaskName(@PathParam("taskId") String taskId, @PathParam("newName") String newName,
			Context context) {
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

	public Result changeTaskEffort(@PathParam("taskId") String taskId, @PathParam("effort") String effort,
			Context context) {
		Task task = projectManager.changeTaskEffort(Long.parseLong(taskId), effort);

		verifyAccessOrBreak(task.getProject().getProjectId().toString(), context);

		if (task.getEffort().equals(effort)) {
			return Results.ok();
		} else {
			return Results.badRequest();
		}
	}

	public Result changeTaskProgress(@PathParam("taskId") String taskId, @PathParam("progress") String progress,
			Context context) {
		Task task = projectManager.changeTaskProgress(Long.parseLong(taskId), progress);

		verifyAccessOrBreak(task.getProject().getProjectId().toString(), context);

		if (String.valueOf(task.getProgress()).equals(progress)) {
			return Results.ok();
		} else {
			return Results.badRequest();
		}
	}

	public Result addTaskDep(@PathParam("taskId") String taskId, @PathParam("taskDepId") String taskDepId,
			Context context) {
		Task task = projectManager.getTask(Long.parseLong(taskId));
		verifyAccessOrBreak(task.getProject().getProjectId().toString(), context);

		projectManager.addTaskDependency(Long.parseLong(taskId), Long.parseLong(taskDepId));
		return Results.ok();
	}

	public Result removeTaskDep(@PathParam("taskId") String taskId, @PathParam("taskDepId") String taskDepId,
			Context context) {
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

	public Result deleteUserFromProject(@PathParam("projectId") String projectId, @PathParam("email") String email,
			Context context) {
		verifyAccessOrBreak(projectId, context);

		Project project = projectManager.unassignUserFromProject(Long.parseLong(projectId), email);
		PeasyUser peasyUser = userManager.getUser(email);

		if (!project.getProjectMembers().contains(peasyUser)) {
			return Results.ok();
		} else {
			return Results.badRequest();
		}

	}

	public Result deleteUserFromTask(@PathParam("taskId") String taskId, @PathParam("email") String email,
			Context context) {
		Task task = projectManager.unassignUserFromTask(Long.parseLong(taskId), email);
		PeasyUser peasyUser = userManager.getUser(email);

		verifyAccessOrBreak(task.getProject().getProjectId().toString(), context);

		if (!task.getUsers().contains(peasyUser)) {
			return Results.ok();
		} else {
			return Results.badRequest();
		}

	}

	public Result addUsertoProject(@PathParam("projectId") String projectId, @PathParam("email") String email,
			Context context) {
		verifyAccessOrBreak(projectId, context);

		Project project = projectManager.assignUserToProject(Long.parseLong(projectId), email);
		PeasyUser peasyUser = userManager.getUser(email);

		if (project.getProjectMembers().contains(peasyUser)) {
			return Results.ok();
		} else {
			return Results.badRequest();
		}

	}

	public Result addTasktoMilestone(@PathParam("milestoneId") String milestoneId, @PathParam("taskId") String taskId,
			Context context) {
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

	public Result changeManager(@PathParam("projectId") String projectId, @PathParam("email") String email,
			Context context) {
		verifyAccessOrBreak(projectId, context);

		projectManager.changeProjectmanager(Long.parseLong(projectId), email);
		return Results.ok();

	}

	public Result changeStatus(@PathParam("projectId") String projectId, @PathParam("status") String status,
			Context context) {
		verifyAccessOrBreak(projectId, context);

		ProjectStatus enumStatus = ProjectStatus.valueOf(status);
		Project project = projectManager.changeProjectState(Long.parseLong(projectId), enumStatus);

		if (project.getStatus().name().contains(status)) {
			return Results.ok();
		} else {
			return Results.badRequest();
		}
	}

	@FilterWith(LoginFilter.class)
	public Result members(@PathParam("projectID") String projectId, Context context) {
		Result result = Results.html();
		Project project = projectManager.getProject(Long.parseLong(projectId));

		verifyAccessOrBreak(projectId, context);
		
		// Project
		result.render("project", project);

		// Project members
		result.render("members", project.getProjectMembers());
		System.out.println("Size of project-ProjectMembers: " + project.getProjectMembers().size());

		return result;
	}

	@FilterWith(LoginFilter.class)
	public Result milestones(@PathParam("projectID") String projectId) {
		Result result = Results.html();
		Project project = projectManager.getProject(Long.parseLong(projectId));

		// Project Parameters
		result.render("project", project);
		result.render("milestones", project.getMilestones());

		return result;
	}

	@FilterWith(LoginFilter.class)
	public Result milestone(@PathParam("projectID") String projectId, @PathParam("milestoneID") String milestoneId, Context context) {
		Result result = Results.html();
		Milestone milestone = projectManager.getMilestone(Long.parseLong(milestoneId));

		verifyAccessOrBreak(projectId, context);
		
		result.render("project", milestone.getProject());
		result.render("milestone", milestone);
		result.render("tasks", milestone.getTasks());

		Set<Task> taskswithoutCurrentMilestone = new HashSet<>();

		for (Task task : milestone.getProject().getTasks()) {
			int a = 0;

			if (task.getMilestone() == null) {
				taskswithoutCurrentMilestone.add(task);
			} else {
				if (task.getMilestone().getMileStoneId().equals(Long.parseLong(milestoneId))) {
					a = 1;
				}
				if (a == 0) {
					taskswithoutCurrentMilestone.add(task);
				}
			}
		}

		result.render("potentialTasks", taskswithoutCurrentMilestone);
		return result;
	}

	@FilterWith(LoginFilter.class)
	public Result project(@PathParam("projectID") String projectId, Context context) {
		Result result = Results.html();
		Project project = projectManager.getProject(Long.parseLong(projectId));
		project.getBlogEntries();

		verifyAccessOrBreak(projectId, context);
		
		// Project Parameters
		result.render("project", project);
		System.out.println("Size of project-Blogentries: " + project.getBlogEntries().size());

		// Blog entries
		result.render("blogEntries", project.getBlogEntries());
		System.out.println(project.getBlogEntries().toString());
		// Files
		System.out.println("Size of project-Files: " + project.getProjectFiles().size());
		result.render("files", project.getProjectFiles());
		result.render("members", project.getProjectMembers());

		// rendering Members
		List<PeasyUser> peasyUsers = projectManager.getAllPeasyusers();

		Set<PeasyUser> peasyUserswithoutCurrentProject = new HashSet<>();
		for (PeasyUser peasyUser : peasyUsers) {
			int a = 0;
			for (Project assignedProject : peasyUser.getProjects()) {
				if (assignedProject.getProjectId().equals(Long.parseLong(projectId))) {
					a = 1;
				}
			}
			if (a == 0) {
				peasyUserswithoutCurrentProject.add(peasyUser);
			}
		}
		result.render("users", peasyUserswithoutCurrentProject);

		Set<PeasyUser> peasyUserswithoutCurrentManager = new HashSet<>();

		for (PeasyUser peasyUser : peasyUsers) {
			int a = 0;
			for (Project projectmanager : peasyUser.getProjectsWhereUserIsManager()) {
				if (projectmanager.getProjectId().equals(Long.parseLong(projectId))) {
					a = 1;
				}
			}
			if (a == 0) {
				peasyUserswithoutCurrentManager.add(peasyUser);
				System.out.println("manager: " + peasyUser.toString());
			}

		}

		result.render("userManagers", peasyUserswithoutCurrentManager);

		return result;

	}

	@FilterWith(LoginFilter.class)
	public Result task(@PathParam("projectID") String projectId, @PathParam("taskID") String taskId, Context context) {

		verifyAccessOrBreak(projectId, context);

		Result result = Results.html();
		Task task = projectManager.getTask(Long.parseLong(taskId));

		// Project
		result.render("project", task.getProject());

		// Project task
		result.render("task", task);

		// Files
		result.render("files", task.getTaskFiles());

		// Taskmebers
		result.render("members", task.getUsers());

		// UpTasks
		result.render("UpTasks", task.getUpTasks());

		// DownTasks
		result.render("DownTasks", task.getBelowTasks());

		// rendering PotentialTasks
		result.render("potentialTasks", task.getPotentialTasks());

		Set<PeasyUser> peasyUsers = task.getProject().getProjectMembers();

		Set<PeasyUser> peasyUserswithoutCurrentTask = new HashSet<>();

		for (PeasyUser peasyUser : peasyUsers) {
			int a = 0;
			for (Task assignedtask : peasyUser.getTasks()) {
				if (assignedtask.getTaskId() == Long.parseLong(taskId)) {
					a = 1;
				}
			}
			if (a == 0) {
				peasyUserswithoutCurrentTask.add(peasyUser);
			}
		}
		result.render("potentialMembers", peasyUserswithoutCurrentTask);

		return result;
	}

	@FilterWith(LoginFilter.class)
	public Result projects(Session session) {
		Result result = Results.html();
		String email = session.get("email");

		PeasyUser peasyUser = userManager.getUser(email);
		Set<Project> distinct = new HashSet<Project>();
		distinct.addAll(peasyUser.getProject());
		distinct.addAll(peasyUser.getProjectsWhereUserIsManager());

		// Project
		result.render("projects", distinct);

		return result;
	}

	@FilterWith(LoginFilter.class)
	public Result report(@PathParam("projectID") String projectId, Context context) {
		Result result = Results.html();
		Project project = projectManager.getProject(Long.parseLong(projectId));

		verifyAccessOrBreak(projectId, context);
		
		// Project Parameters
		result.render("project", project);

		// calculate report values
		Set<Task> tasks = project.getTasks();

		double taskValue = 0;
		double counter = 0;

		for (Task task : tasks) {
			double gewichtung;
			if (!task.getUsers().isEmpty()) {
				gewichtung = Integer.parseInt(task.getEffort()) / task.getUsers().size();

			} else {
				gewichtung = Integer.parseInt(task.getEffort());
			}

			counter += gewichtung;

			taskValue += gewichtung * task.getProgress();
		}

		Double tasksFinished = taskValue / counter;
		Double tasksOpen = 100 - tasksFinished;

		result.render("finished", tasksFinished.intValue());
		result.render("open", tasksOpen.intValue());

		String start = project.getStart();
		String end = project.getDeadline();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			format.parse(start);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
		Date startDate = null;
		Date endDate = null;
		Date aktuelDate = null;
		long diffStart = 0;
		long diffEnd = 0;

		try {
			startDate = parser.parse(start);
			endDate = parser.parse(end);
			aktuelDate = parser.parse(parser.format(new Date()));
			diffStart = startDate.getTime() - aktuelDate.getTime();
			diffEnd = aktuelDate.getTime() - endDate.getTime();

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (diffStart != 0 && aktuelDate.before(startDate)) {
			result.render("projectstart", "Project starts in");
			result.render("projectend", "");
			result.render("start", TimeUnit.DAYS.convert(diffStart, TimeUnit.MILLISECONDS));
			result.render("end", 0);
			result.render("startrel", 100);
			result.render("endrel", 0);
		} else if (diffEnd != 0 && aktuelDate.after(endDate)) {
			result.render("projectstart", "");
			result.render("projectend", "Project has end since");
			result.render("start", 0);
			result.render("end", TimeUnit.DAYS.convert(diffEnd, TimeUnit.MILLISECONDS));
			result.render("startrel", 0);
			result.render("endrel", 100);
		}

		else {
			result.render("projectstart", "Project has started since");
			result.render("start", TimeUnit.DAYS.convert(diffStart, TimeUnit.MILLISECONDS) * -1);
			result.render("projectend", "Project ends in ");
			result.render("end", TimeUnit.DAYS.convert(diffEnd, TimeUnit.MILLISECONDS) * -1);

			if (diffStart == 0 & diffEnd == 0) {
				result.render("startrel", 50);
				result.render("endrel", 50);
			} else {

				Double teiler = (double) (Math.abs(TimeUnit.DAYS.convert(diffStart, TimeUnit.MILLISECONDS))
						+ Math.abs(TimeUnit.DAYS.convert(diffEnd, TimeUnit.MILLISECONDS)));
				Double startrel = Math.abs(TimeUnit.DAYS.convert(diffStart, TimeUnit.MILLISECONDS)) / teiler;
				Double endrel = 1 - startrel;

				Double startFinish = startrel * 100;
				Double endFinish = endrel * 100;

				result.render("startrel", startFinish.intValue());
				result.render("endrel", endFinish.intValue());
			}
		}

		return result;
	}

	@FilterWith(LoginFilter.class)
	public Result tasks(@PathParam("projectID") String projectId, Context context) {

		verifyAccessOrBreak(projectId, context);

		Result result = Results.html();
		Project project = projectManager.getProject(Long.parseLong(projectId));

		// Project
		result.render("project", project);

		// Tasks
		result.render("tasks", project.getTasks());

		return result;
	}

	private void verifyAccessOrBreak(String projectId, Context context) {
		Project project = projectManager.getProject(Long.parseLong(projectId));
		String email = (context.getSession().get("email"));

		// Quickfail
		if (project == null) {
			throw new BadRequestException("Attempted to access a project for which you have no access.");
		}

		if (project.getProjectManager().getEmailAddress().equals(email)) {
			return;
		}

		for (PeasyUser user : project.getProjectMembers()) {
			if (user.getEmailAddress().equals(email)) {
				return;
			}
		}

		context.getFlashScope().error("auth.noAuth");
		throw new BadRequestException("Attempted to access a project for which you have no access.");
	}

}
