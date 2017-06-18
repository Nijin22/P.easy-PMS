package controllers;

import java.util.HashSet;
import java.util.Set;

import com.google.inject.Inject;

import filters.LoginFilter;
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
	public Result organization(@PathParam("id") String id) {
		Result result = Results.html();
		// eigentlich org vom user auslesen.
		Organisation organization = organisationManager.getOrganisation(Integer.parseInt(id));

		System.out.println("Size of users =) " + organization.getUsers().size());

		result.render("members", organization.getUsers());
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