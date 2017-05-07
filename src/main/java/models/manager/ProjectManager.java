package models.manager;

import java.util.Set;

import javax.persistence.EntityManager;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.NoSuchElementException;


import models.beans.PeasyUser;
import models.beans.Project;
import models.beans.ProjectBlogEntry;
import models.beans.ProjectStatus;
import models.beans.Task;
import ninja.jpa.UnitOfWork;

/**
 *
 * @author Tugrul
 *
 */
public class ProjectManager {

    @Inject
    Provider<EntityManager> entitiyManagerProvider;

    // Begin Project specific Methods
    /**
     *
     * @param projectManager
     * @param name
     * @return created Project
     * @throws IllegalArgumentException
     */
    @Transactional
    public Project createProject(PeasyUser projectManager, String name) throws IllegalArgumentException {
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Name of Project can't be empty or null");
        }
        if (projectManager == null) {
            throw new IllegalArgumentException("Projectmanager can't be null");
        }

        // Get Manager to persist the project object
        EntityManager entityManager = entitiyManagerProvider.get();
        // Create project bean
        Project project = new Project();
        project.setProjectManager(projectManager);
        project.setName(name);
        // Persist the project
        entityManager.persist(project);

        return project;
    }

    /**
     *
     * @param projectId
     * @return Project
     * @throws NoSuchElementException
     */
    @Transactional
    public Project getProject(long projectId) throws NoSuchElementException {
        EntityManager entityManager = entitiyManagerProvider.get();
        Project project = entityManager.find(Project.class, projectId);

        if (project == null) {
            throw new NoSuchElementException("Project with projectId " + projectId + "is not in the database");
        } else {
            return project;
        }

    }

    /**
     *
     * @param projectId
     * @param description
     * @return updated Project
     * @throws NoSuchElementException
     */
    @Transactional
    public Project updateProject(long projectId, String description) throws NoSuchElementException {
        //description can be null, so no exception handling is requiered

        EntityManager entityManager = entitiyManagerProvider.get();
        Project project = entityManager.find(Project.class, projectId);

        if (project == null) {
            throw new NoSuchElementException("Project with projectId " + projectId + "is not in the database");
        } else {
            //set new description to project
            project.setDescription(description);
            return project;
        }
    }
    
    
        /**
     *
     * @param projectId
     * @param start
     * @param deadline
     * @param budget
     * @return updated Project
     * @throws NoSuchElementException
     */
    @Transactional
    public Project updateProjectParameters(long projectId, String start, String deadline, String budget) throws NoSuchElementException {
        //description can be null, so no exception handling is requiered

        EntityManager entityManager = entitiyManagerProvider.get();
        Project project = entityManager.find(Project.class, projectId);

        if (project == null) {
            throw new NoSuchElementException("Project with projectId " + projectId + "is not in the database");
        } else {
            //set new description to project
            project.setBudget(budget);
            project.setDeadline(deadline);
            project.setStart(start);
            return project;
        }
    }

    /**
     *
     * @param projectId
     * @param status
     * @return upated project
     * @throws IllegalArgumentException
     * @throws NoSuchElementException
     */
    @Transactional
    public Project changeProjectState(long projectId, ProjectStatus status) throws IllegalArgumentException, NoSuchElementException {
        if (status == null) {
            throw new IllegalArgumentException("status can't be null");
        }

        EntityManager entityManager = entitiyManagerProvider.get();
        Project project = entityManager.find(Project.class, projectId);

        if (project == null) {
            throw new NoSuchElementException("Project with projectId " + projectId + "is not in the database");
        } else {
            //set new status to project
            project.setStatus(status);

            return project;
        }
    }

    /**
     *
     * @param projectId
     * @throws NoSuchElementException
     */
    @Transactional
    public void deleteProject(long projectId) throws NoSuchElementException {

        EntityManager entityManager = entitiyManagerProvider.get();
        Project project = entityManager.find(Project.class, projectId);

        if (project == null) {
            throw new NoSuchElementException("Project with projectId " + projectId + "is not in the database");
        } else {
            entityManager.remove(project);
        }
    }

    /**
     *
     * @param projectId
     * @return Tasks of a Project
     * @throws NoSuchElementException
     */
    @UnitOfWork
    public Set<Task> getProjectTasks(long projectId) throws NoSuchElementException {
        EntityManager entityManager = entitiyManagerProvider.get();
        Project project = entityManager.find(Project.class, projectId);

        if (project == null) {
            throw new NoSuchElementException("Project with projectId " + projectId + "is not in the database");
        } else {
            return project.getTasks();
        }

    }

    /**
     *
     * @param projectId
     * @param email
     * @return updated Project
     * @throws NoSuchElementException
     */
    @Transactional
    public Project addMemberToProject(long projectId, String email) throws  NoSuchElementException{

        EntityManager entityManager = entitiyManagerProvider.get();
        Project project = entityManager.find(Project.class, projectId);
        PeasyUser user = entityManager.find(PeasyUser.class, email);

        if (project == null) {
            throw new NoSuchElementException("Project with projectId " + projectId + "is not in the database");
        }
        if (user == null) {
            throw new NoSuchElementException("User with email " + email + "is not in the database");
        }

        user.getProject().add(project);
        project.getUsers().add(user);
        return project;

    }

    /**
     *
     * @param projectId
     * @param email
     * @return updated Project
     * @throws IllegalArgumentException
     * @throws NoSuchElementException
     */
    @Transactional
    public Project removeMemberFromProject(long projectId, String email) throws IllegalArgumentException, NoSuchElementException {
        if (email == null) {
            throw new IllegalArgumentException("User can't be null");
        }
        EntityManager entityManager = entitiyManagerProvider.get();
        Project project = entityManager.find(Project.class, projectId);
        PeasyUser user = entityManager.find(PeasyUser.class, email);

        if (project == null) {
            throw new NoSuchElementException("Project with projectId " + projectId + "is not in the database");
        }
        else if(user == null){
            throw new NoSuchElementException("User with email " + email + "is not in the database");
        }
        else {
            //remove user from project
            project.getUsers().remove(user);
            //remove project from user
            user.getProject().remove(project);
            return project;
        }

    }
    // END Project specific Methods

    // BEGIN project & task specific Methods
    /**
     *
     * @param projectId
     * @param name
     * @return Created Task
     * @throws IllegalArgumentException
     */
    @Transactional
    public Task createTask(long projectId, String name) throws IllegalArgumentException {

        if (name.isEmpty()) {
            throw new IllegalArgumentException("Name can't be empty");
        }
        EntityManager entityManager = entitiyManagerProvider.get();
        Project project = entityManager.find(Project.class, projectId);
          if (project == null) {
            throw new NoSuchElementException("Project with projectId " + projectId + "is not in the database");
        } else {
        Task task = new Task();
        task.setProject(project);
        task.setName(name);
        project.getTasks().add(task);
        entityManager.persist(task);

        return task; 
          }

    }

    /**
     *
     * @param taskId
     * @return Task, which found by Id
     */
    @Transactional
    public Task getTask(long taskId) {
        EntityManager entityManager = entitiyManagerProvider.get();
        Task task = entityManager.find(Task.class, taskId);

        if (task == null) {
            throw new NoSuchElementException("Task with taskId " + taskId + "is not in the database");
        } else {
            return task;
        }
      
    }

    /**
     *
     * @param taskId
     * @param name
     * @param description
     * @param progress
     * @return Updated Task
     * @throws NoSuchElementException
     */
    @Transactional
    public Task updateTask(long taskId, String name, String description, int progress) throws NoSuchElementException {
        //only name can't be null, description and progress can be null
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Name can't be empty");
        }

        EntityManager entityManager = entitiyManagerProvider.get();
        Task task = entityManager.find(Task.class, taskId);

        if (task == null) {
            throw new NoSuchElementException("Task with taskId " + taskId + "is not in the database");
        } else {
            task.setName(name);
            task.setDescription(description);
            task.setProgress(progress);
            return task;
        }
    }

    /**
     *
     * @param taskId
     * @throws NoSuchElementException
     */
    @Transactional
    public void deleteTask(long taskId) throws NoSuchElementException {
        EntityManager entityManager = entitiyManagerProvider.get();
        Task task = entityManager.find(Task.class, taskId);
        if (task == null) {
            throw new NoSuchElementException("Task with taskId " + taskId + "is not in the database");
        } else {
            entityManager.remove(task);
        }
    }

    /**
     *
     * @param taskId
     * @param email
     * @return updated Task
     * @throws NoSuchElementException
     */
    @Transactional
    public Task assignUserToTask(long taskId, String email) throws NoSuchElementException {
       
        EntityManager entityManager = entitiyManagerProvider.get();
        Task task = entityManager.find(Task.class, taskId);
        PeasyUser user = entityManager.find(PeasyUser.class, email);  
        
        if (task == null) {
            throw new NoSuchElementException("Task with taskId: " + taskId + "doesn't exist in the database");
        }
        if (user == null) {
            throw new NoSuchElementException("User with email: " + email + "doesn't exist in the database");
        }

        task.getUsers().add(user);
        user.getTasks().add(task);

        return task;

    }

    /**
     *
     * @param taskId
     * @param email
     * @return Updated Task
     * @throws NoSuchElementException
     */
    @Transactional
    public Task unassignUserFromTask(long taskId, String email) throws IllegalArgumentException {
      
        EntityManager entityManager = entitiyManagerProvider.get();
        Task task = entityManager.find(Task.class, taskId);
        PeasyUser user = entityManager.find(PeasyUser.class, email);     
        
        if (task == null) {
            throw new NoSuchElementException("Task with taskId: " + taskId + "doesn't exist in the database");
        }
        if (user == null) {
            throw new NoSuchElementException("User with email: " + email + "doesn't exist in the database");
        }
        task.getUsers().remove(user);
        user.getTasks().remove(task);
        
        return task;
    }
    // END project & task specific Methods

    // BEGIN project & blog specific Methods
    /**
     *
     * @param projectId
     * @param email
     * @param title
     * @param text
     * @return ctreated ProjectBlogEntry
     * @throws NoSuchElementException
     */
    @Transactional
    public ProjectBlogEntry createBlogEntry(long projectId, String email, String title, String text) throws NoSuchElementException {

        EntityManager entityManager = entitiyManagerProvider.get();
        Project project = entityManager.find(Project.class, projectId);
        PeasyUser author = entityManager.find(PeasyUser.class, email);     
        
        if (project == null) {
            throw new NoSuchElementException("Project with projectId: " + projectId + "doesn't exist in the database");
        }
        if (author == null) {
            throw new NoSuchElementException("Author with email: " + email + "doesn't exist in the database");

        }

        ProjectBlogEntry blogEntry = new ProjectBlogEntry();
        
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        
        blogEntry.setCreationDate(dateFormat.format(date));
        blogEntry.setTitle(title);
        blogEntry.setText(text);
        blogEntry.setAuthor(author);
        blogEntry.setProject(project);
        
        project.getBlogEntries().add(blogEntry);

        entityManager.persist(blogEntry);

        return blogEntry;
    }

    /**
     *
     * @param blogEntryId
     * @param title
     * @param text
     * @return updated ProjectBlogEntry
     * @throws NoSuchElementException
     */
    @Transactional
    public ProjectBlogEntry updateBlogEntry(int blogEntryId, String title, String text) throws NoSuchElementException {
        EntityManager entityManager = entitiyManagerProvider.get();
        ProjectBlogEntry blogEntry = entityManager.find(ProjectBlogEntry.class, blogEntryId);

        if (blogEntry == null) {
            throw new NoSuchElementException();
        } else {
            blogEntry.setTitle(title);
            blogEntry.setText(text);

            return blogEntry;
        }
    }

    /**
     *
     * @param blogEntryId
     * @throws NoSuchElementException
     */
    @Transactional
    public void deleteBlogEntry(int blogEntryId) throws NoSuchElementException {
        EntityManager entityManager = entitiyManagerProvider.get();
        ProjectBlogEntry blogEntry = entityManager.find(ProjectBlogEntry.class, blogEntryId);
        if (blogEntry == null) {
            throw new NoSuchElementException();
        } else {
            entityManager.remove(blogEntry);
        }
    }
    // END project & blog specific Methods
}
