/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managers;

import java.util.Set;
import models.beans.PeasyUser;
import models.beans.Project;
import models.beans.ProjectBlogEntry;
import models.beans.ProjectStatus;
import models.beans.Task;
import models.manager.ProjectManager;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Tugrul
 */
public class ProjectManagerTest {
    
    public ProjectManagerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of createProject method, of class ProjectManager.
     */
    @Test
    public void testCreateProject() {
        System.out.println("createProject");
        PeasyUser projectManager = null;
        String name = "";
        ProjectManager instance = new ProjectManager();
        Project expResult = null;
        Project result = instance.createProject(projectManager, name);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getProject method, of class ProjectManager.
     */
    @Test
    public void testGetProject() {
        System.out.println("getProject");
        long projectId = 0L;
        ProjectManager instance = new ProjectManager();
        Project expResult = null;
        Project result = instance.getProject(projectId);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of updateProject method, of class ProjectManager.
     */
    @Test
    public void testUpdateProject() {
        System.out.println("updateProject");
        long projectId = 0L;
        String description = "";
        ProjectManager instance = new ProjectManager();
        Project expResult = null;
        Project result = instance.updateProject(projectId, description);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of changeProjectState method, of class ProjectManager.
     */
    @Test
    public void testChangeProjectState() {
        System.out.println("changeProjectState");
        long projectId = 0L;
        ProjectStatus status = null;
        ProjectManager instance = new ProjectManager();
        Project expResult = null;
        Project result = instance.changeProjectState(projectId, status);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of deleteProject method, of class ProjectManager.
     */
    @Test
    public void testDeleteProject() {
        System.out.println("deleteProject");
        long projectId = 0L;
        ProjectManager instance = new ProjectManager();
        instance.deleteProject(projectId);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getProjectsTasks method, of class ProjectManager.
     */
    @Test
    public void testGetProjectsTasks() {
        System.out.println("getProjectsTasks");
        long projectId = 0L;
        ProjectManager instance = new ProjectManager();
        Set<Task> expResult = null;
        Set<Task> result = instance.getProjectsTasks(projectId);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addMemberToProject method, of class ProjectManager.
     */
    @Test
    public void testAddMemberToProject() {
        System.out.println("addMemberToProject");
        Project project = null;
        PeasyUser user = null;
        ProjectManager instance = new ProjectManager();
        Project expResult = null;
        Project result = instance.addMemberToProject(project, user);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of removeMemberFromProject method, of class ProjectManager.
     */
    @Test
    public void testRemoveMemberFromProject() {
        System.out.println("removeMemberFromProject");
        long projectId = 0L;
        PeasyUser user = null;
        ProjectManager instance = new ProjectManager();
        instance.removeMemberFromProject(projectId, user);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of createTask method, of class ProjectManager.
     */
    @Test
    public void testCreateTask() {
        System.out.println("createTask");
        Project project = null;
        String name = "";
        ProjectManager instance = new ProjectManager();
        Task expResult = null;
        Task result = instance.createTask(project, name);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getTask method, of class ProjectManager.
     */
    @Test
    public void testGetTask() {
        System.out.println("getTask");
        long taskId = 0L;
        ProjectManager instance = new ProjectManager();
        Task expResult = null;
        Task result = instance.getTask(taskId);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of updateTask method, of class ProjectManager.
     */
    @Test
    public void testUpdateTask() {
        System.out.println("updateTask");
        long taskId = 0L;
        String name = "";
        String description = "";
        int progress = 0;
        ProjectManager instance = new ProjectManager();
        Task expResult = null;
        Task result = instance.updateTask(taskId, name, description, progress);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of deleteTask method, of class ProjectManager.
     */
    @Test
    public void testDeleteTask() {
        System.out.println("deleteTask");
        long taskId = 0L;
        ProjectManager instance = new ProjectManager();
        instance.deleteTask(taskId);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of assignUserToTask method, of class ProjectManager.
     */
    @Test
    public void testAssignUserToTask() {
        System.out.println("assignUserToTask");
        Task task = null;
        PeasyUser user = null;
        ProjectManager instance = new ProjectManager();
        Task expResult = null;
        Task result = instance.assignUserToTask(task, user);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of unassignUserFromTask method, of class ProjectManager.
     */
    @Test
    public void testUnassignUserFromTask() {
        System.out.println("unassignUserFromTask");
        Task task = null;
        PeasyUser user = null;
        ProjectManager instance = new ProjectManager();
        instance.unassignUserFromTask(task, user);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of createBlogEntry method, of class ProjectManager.
     */
    @Test
    public void testCreateBlogEntry() {
        System.out.println("createBlogEntry");
        Project project = null;
        PeasyUser author = null;
        String title = "";
        String text = "";
        ProjectManager instance = new ProjectManager();
        ProjectBlogEntry expResult = null;
        ProjectBlogEntry result = instance.createBlogEntry(project, author, title, text);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of updateBlogEntry method, of class ProjectManager.
     */
    @Test
    public void testUpdateBlogEntry() {
        System.out.println("updateBlogEntry");
        long blogEntryId = 0L;
        String title = "";
        String text = "";
        ProjectManager instance = new ProjectManager();
        ProjectBlogEntry expResult = null;
        ProjectBlogEntry result = instance.updateBlogEntry(blogEntryId, title, text);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of deleteBlogEntry method, of class ProjectManager.
     */
    @Test
    public void testDeleteBlogEntry() {
        System.out.println("deleteBlogEntry");
        long blogEntryId = 0L;
        ProjectManager instance = new ProjectManager();
        instance.deleteBlogEntry(blogEntryId);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
