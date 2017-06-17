package controllers;

import com.google.inject.Inject;

import filters.LoginFilter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import models.beans.Milestone;
import models.beans.Organisation;
import models.beans.PeasyUser;
import models.beans.Project;
import models.beans.Task;
import models.manager.OrganisationManager;
import models.manager.ProjectManager;
import models.manager.UserManager;
import ninja.FilterWith;
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

	@FilterWith(LoginFilter.class)
	public Result dashboard(Session session) {
		Result result = Results.html();

		String email = session.get("email");
		PeasyUser peasyUser = userManager.getUser(email);

		// Projects of user
		Set<Project> usersProjects = new HashSet<Project>();
		usersProjects.addAll(peasyUser.getProject());
		usersProjects.addAll(peasyUser.getProjectsWhereUserIsManager());
		result.render("projects", usersProjects);
		result.render("projectsCount", usersProjects.size());

		// Tasks of user
		Set<Task> usersTasks = new HashSet<Task>();
		for (Project currentProject : usersProjects) {
			for (Task currentTask : currentProject.getTasks()) {
				if (currentTask.getUsers().contains(peasyUser)) {
					usersTasks.add(currentTask);
				}
			}
		}
		result.render("tasks", usersTasks);
		result.render("tasksCount", usersTasks.size());

		// User themself
		result.render("user", peasyUser);
		return result;

	}

	public Result impress() {
		return Results.html();

	}

	public Result login() {
		return Results.html();

	}

	@FilterWith(LoginFilter.class)
	public Result members(@PathParam("projectID") String projectId) {
		Result result = Results.html();
		Project project = projectManager.getProject(Long.parseLong(projectId));

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
	public Result milestone(@PathParam("milestoneID") String milestoneId) {
		Result result = Results.html();
		Milestone milestone = projectManager.getMilestone(Long.parseLong(milestoneId));

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
	public Result organization(@PathParam("id") String id) {
		Result result = Results.html();
		// eigentlich org vom user auslesen.
		Organisation organization = organisationManager.getOrganisation(Integer.parseInt(id));

		System.out.println("Size of users =) " + organization.getUsers().size());

		result.render("members", organization.getUsers());
		return result;

	}

	@FilterWith(LoginFilter.class)
	public Result project(@PathParam("projectID") String projectId) {
		Result result = Results.html();
		Project project = projectManager.getProject(Long.parseLong(projectId));
		project.getBlogEntries();

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
	public Result task(@PathParam("projectID") String projectId, @PathParam("taskID") String taskId) {

		// projectId is not needed until now

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
	public Result report(@PathParam("projectID") String projectId) {

		Result result = Results.html();
		Project project = projectManager.getProject(Long.parseLong(projectId));

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
	public Result tasks(@PathParam("projectID") String projectId) {

		// projectId is not needed until now

		Result result = Results.html();
		Project project = projectManager.getProject(Long.parseLong(projectId));

		// Project
		result.render("project", project);

		// Tasks
		result.render("tasks", project.getTasks());

		return result;
	}

	@FilterWith(LoginFilter.class)
	public Result fileUpload() {
		return Results.html();

	}

	public Result index() {
		return Results.html();

	}
}