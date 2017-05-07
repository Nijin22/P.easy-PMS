package controllers;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import models.manager.ProjectManager;
import ninja.Result;
import ninja.Results;
import ninja.params.PathParam;

@Singleton
public class ProjectController {

	@Inject
	private ProjectManager projectManager;
	
	public Result addBlogEntry(@PathParam("projectId") String projectId,@PathParam("email") String email,@PathParam("title") String title,@PathParam("text") String text){
		projectManager.createBlogEntry( Long.parseLong(projectId), email, title, text);
		return Results.redirect("/projects/" + projectId);
	}
	
	public Result addBlogEntry(@PathParam("projectId") String projectId,@PathParam("blogEntryId") String blogEntryId){
		projectManager.deleteBlogEntry(Integer.parseInt(blogEntryId));
		return Results.redirect("/projects/" + projectId);
	}
}
