/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managers;

import java.security.GeneralSecurityException;
import java.util.NoSuchElementException;
import java.util.logging.Logger;
import models.beans.PeasyUser;
import models.beans.Project;
import models.beans.ProjectStatus;
import models.beans.Task;
import models.manager.ProjectManager;
import models.manager.UserManager;
import models.manager.exceptions.UserAlreadyExistsException;
import ninja.NinjaTest;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Tugrul
 */
public class ProjectManagerTest extends NinjaTest {

    private static Logger log = null;

    public ProjectManagerTest() {
    }

    @Before
    public void setUp() {
        log = Logger.getLogger(ProjectManagerTest.class.getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateProjectException1() {
        ProjectManager pm = getInjector().getInstance(ProjectManager.class);
        log.info("Start Testing Fail Test 1: Projectmanager is null");
        pm.createProject(null, "p1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateProjectException2() {
        ProjectManager pm = getInjector().getInstance(ProjectManager.class);
        log.info("Start Testing Fail Test 1: name is null");
        pm.createProject(new PeasyUser(), "");
    }

    @Test(expected = NoSuchElementException.class)
    public void testGetProjectException2() {
        ProjectManager pm = getInjector().getInstance(ProjectManager.class);
        log.info("Start Testing Fail Test 1: project does not exist in database");
        pm.getProject(111222333);
    }

    @Test(expected = NoSuchElementException.class)
    public void testUpdateProjectDescriptionException2() {
        ProjectManager pm = getInjector().getInstance(ProjectManager.class);
        log.info("Start Testing Fail Test 1: project does not exist in database");
        pm.updateProject(111222333, "Description");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateProjectStateException1() {
        ProjectManager pm = getInjector().getInstance(ProjectManager.class);
        log.info("Start Testing Fail Test 1: state is null");
        pm.changeProjectState(111222333, null);
    }

    @Test(expected = NoSuchElementException.class)
    public void testUpdateProjectStateException2() {
        ProjectManager pm = getInjector().getInstance(ProjectManager.class);
        log.info("Start Testing Fail Test 1: project does not exist in database");
        pm.changeProjectState(111222333, ProjectStatus.CREATED);
    }

    @Test(expected = NoSuchElementException.class)
    public void testUpdateProjectMemberAddException1() {
        ProjectManager pm = getInjector().getInstance(ProjectManager.class);
        log.info("Start Testing Fail Test 1: project does not exist in database");
        pm.addMemberToProject(111222333, "email");
    }
    
    @Test(expected = NoSuchElementException.class)
    public void testUpdateProjectMemberRemoveException1() {
        ProjectManager pm = getInjector().getInstance(ProjectManager.class);
        log.info("Start Testing Fail Test 1: project does not exist in database");
        pm.removeMemberFromProject(111222333, "email");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testUpdateProjectMemberRemoveException2() {
        ProjectManager pm = getInjector().getInstance(ProjectManager.class);
        log.info("Start Testing Fail Test 1: email is null");
        pm.removeMemberFromProject(111222333, null);
    }
    
    @Test(expected = NoSuchElementException.class)
    public void testCreateTaskException1() {
        ProjectManager pm = getInjector().getInstance(ProjectManager.class);
        log.info("Start Testing Fail Test 1: project does not exist in database");
        pm.removeMemberFromProject(111222333, "email");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateTaskException2() {
        ProjectManager pm = getInjector().getInstance(ProjectManager.class);
        log.info("Start Testing Fail Test 1: name is null");
        pm.removeMemberFromProject(111222333, null);
    }

   @Test(expected = NoSuchElementException.class)
    public void testGetTaskException1() {
        ProjectManager pm = getInjector().getInstance(ProjectManager.class);
        log.info("Start Testing Fail Test 1: task does not exist in database");
        pm.getTask(111222333);
    }
    
    @Test
    public void testCreateUpadteProject() {
        try {
            ProjectManager pm = getInjector().getInstance(ProjectManager.class);
            UserManager um = getInjector().getInstance(UserManager.class);

            log.info("Start Testing Succesfull Test: createUpadteOrganisation");

            //test create Project
            PeasyUser manager = um.createUser("tugi@peasy.de", "Tugi", "Bugi", "123456789");
            Project createdProject = pm.createProject(manager, "Project 1");
            assertEquals(createdProject.getName(), "Project 1");

            //test get Project
            Project getProject = pm.getProject(createdProject.getProjectId());
            assertEquals(createdProject.getProjectId(), getProject.getProjectId());

            //test update Project description
            Project projectUpadteDescription = pm.updateProject(createdProject.getProjectId(), "Description");
            assertEquals(projectUpadteDescription.getDescription(), "Description");

            // test update Project, change Project state
            Project projectUpadteState = pm.changeProjectState(createdProject.getProjectId(), ProjectStatus.CREATED);
            assertEquals(projectUpadteState.getStatus(), ProjectStatus.CREATED);

            // test update Project, add member to project
            Project projectUpadteMemberAdd = pm.addMemberToProject(createdProject.getProjectId(), manager.getEmailAddress());
            assertEquals(projectUpadteMemberAdd.getProjectMembers().size(), 1);
            
            // test update Project, remove member from project
            Project projectUpadteMemberRemove = pm.removeMemberFromProject(createdProject.getProjectId(), manager.getEmailAddress());
            assertEquals(projectUpadteMemberRemove.getProjectMembers().size(), 0);
            
            //test create task 
            Task projectCreatedTask = pm.createTask(createdProject.getProjectId(), "Task 1");
            assertEquals(projectCreatedTask.getName(), "Task 1");
            
            //test get task
            Task projectgetTask = pm.getTask(projectCreatedTask.getTaskId());
            assertEquals(projectgetTask.getName(), projectCreatedTask.getName());


            
        } catch (GeneralSecurityException | UserAlreadyExistsException e) {
            fail(e.getMessage());
        }

    }

}
