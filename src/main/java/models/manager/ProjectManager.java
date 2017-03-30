package models.manager;

import java.util.Set;

import models.beans.PeasyUser;
import models.beans.Project;
import models.beans.ProjectBlogEntry;
import models.beans.ProjectStatus;
import models.beans.Task;

public class ProjectManager {
	
	// BEGIN Project specific Methods
	public Project createProject(PeasyUser projectManager, String name){
		// TODO: Implement
		return null;
	}
	
	public Project getProject(long projectId){
		// TODO: Implement
		return null;
	}
	
	public Project updateProject(String name, String description){
		// TODO: Implement
		return null;
	}
	
	public Project changeProjectState(Long projectId, ProjectStatus status){
		// TODO: Implement
		return null;
	}
	
	public void deleteProject(long projectId){
		// TODO: Implement
		// TODO: Watch out that project deletion doesn't cascade to deletion of project member or manager!
	}
	
	public Set<Task> getProjectsTasks(long projectId){
		// TODO: Implement
		return null;
	}
	
	public Project addMemberToProject(Project project, PeasyUser user){
		// TODO: Implement
		return null;
	}
	
	public void removeMemberFromProject(Project project, PeasyUser user){
		// TODO: Implement
	}
	// END Project specific Methods
	
	// BEGIN project & task specific Methods
	public Task createTask(Project project, String name){
		// TODO: Implement
		return null;
	}
	
	public Task getTask(long taskId){
		// TODO: Implement
		return null;
	}
	
	public Task updateTask(String name, String description, int progress){
		// TODO: Implement
		return null;
	}
	
	public void deleteTask(long taskId){
		// TODO: Implement
	}
	
	public Task assignUserToTask(Task task, PeasyUser user){
		// TODO: Implement
		return null;
	}
	
	public void unassignUserFromTask(Task task, PeasyUser user){
		// TODO: Implement
	}
	// END project & task specific Methods
	
	
	// BEGIN project & blog specific Methods
	public ProjectBlogEntry createBlogEntry(Project project){
		// TODO: Implement
		// TODO: Author as parameter (also in BlogEntry class)
		return null;
	}
	
	public ProjectBlogEntry updateBlogEntry(String title, String text){
		// TODO: Implement
		return null;
	}
	// END project & blog specific Methods
	
	
	
}
