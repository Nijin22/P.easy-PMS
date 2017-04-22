/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managers;

import java.io.File;
import java.util.logging.Logger;
import models.beans.PeasyUser;
import models.beans.Project;
import models.beans.ProjectFile;
import models.beans.Task;
import models.beans.TaskFile;
import models.manager.FileManager;
import models.manager.ProjectManager;
import models.manager.UserManager;
import ninja.NinjaTest;
import org.apache.commons.io.FilenameUtils;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Tugrul
 */
public class FileManagerTest extends NinjaTest {

    private static Logger log = null;

    /**
     *
     */
    public FileManagerTest() {
    }
    
    FileManager fm;
    ProjectManager pm;
    UserManager um;

    /**
     *
     */
    @Before
    public void setUp() {
        //initialize Logger to log Test
        log = Logger.getLogger(FileManagerTest.class.getName());

        fm = getInjector().getInstance(FileManager.class);
        pm = getInjector().getInstance(ProjectManager.class);
        um = getInjector().getInstance(UserManager.class);

    }

    /**
     * Test of uploadFile and deleteFile methods, of class FileManager.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testUploadDeleteProjectFile() throws Exception {
        log.info("Start Testing Succesfull Test: uploadFile");

        //create Project and manager
        PeasyUser manager = um.createUser("tugi@peasy.de", "Tugi", "Bugi", "123456789");
        Project createdProject = pm.createProject(manager, "Project 1");

        //uploadFile
        File file = new File("src/test/resources/TestfileForUpload.pdf");
        String uploadedFileLocation = fm.uploadFile(file, file.getName() , "project", String.valueOf(createdProject.getProjectId()));

        //test Objects
        Project newProject = pm.getProject(createdProject.getProjectId());
        File uploadedFile = new File(uploadedFileLocation);
        //test Project has a ProjectFile
        assertEquals(1, newProject.getProjectFiles().size());
        assertTrue(uploadedFile.exists());

        //get First and the only Object in Set
        ProjectFile projectFile = newProject.getProjectFiles().iterator().next();

        //start testing delete
        fm.deleteFile(projectFile.getFileId(), "project");
        Project AfterDelProject = pm.getProject(createdProject.getProjectId());
        //test Project has no ProjectFile
        assertEquals(0, AfterDelProject.getProjectFiles().size());
        assertFalse(uploadedFile.exists());
    }

    /**
     * Test of uploadFile and deleteFile methods, of class FileManager.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testUploadDeleteTaskFile() throws Exception {
        log.info("Start Testing Succesfull Test: uploadFile");
        
        //TestFile is created with id 0 after persist and delte the id is 1 ??? same problem below
        
        //create Project and manager
        PeasyUser manager = um.createUser("tugi@peasy.de", "Tugi", "Bugi", "123456789");
        Project createdProject = pm.createProject(manager, "Project 1");
        Task projectCreatedTask = pm.createTask(createdProject.getProjectId(), "Task 1");

        //uploadFile
        File file = new File("src/test/resources/TestfileForUpload.pdf");
        String uploadedFileLocation = fm.uploadFile(file, file.getName(), "task", String.valueOf(projectCreatedTask.getTaskId()));

        //test Objects
        Task newTask = pm.getTask(projectCreatedTask.getTaskId());
        File uploadedFile = new File(uploadedFileLocation);
        //test Project has a ProjectFile
        assertEquals(1, newTask.getTaskFiles().size());
        assertTrue(uploadedFile.exists());

        //get First and the only Object in Set
        TaskFile taskFile = newTask.getTaskFiles().iterator().next();
        //start testing delete
        fm.deleteFile(taskFile.getFileId(), "task");
        Task AfterDelTask = pm.getTask(projectCreatedTask.getTaskId());
        //test Project has no ProjectFile
        assertEquals(0, AfterDelTask.getTaskFiles().size());
        assertFalse(uploadedFile.exists());
    }
    
    /**
     * Test of uploadFile and deleteFile methods, of class FileManager.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testUploadDeleteUserPicture() throws Exception {
        log.info("Start Testing Succesfull Test: uploadFile");
        
        //create Project and manager
        PeasyUser manager = um.createUser("tugi@peasy.de", "Tugi", "Bugi", "123456789");

        //uploadFile
        File file = new File("src/test/resources/githubCat.png");
        String uploadedFileLocation = fm.uploadFile(file, file.getName(), "picture", String.valueOf(manager.getEmailAddress()));

        //test Objects
        PeasyUser newManager = um.getUser(manager.getEmailAddress());
        File uploadedFile = new File(uploadedFileLocation);
        //test Project has a ProjectFile
        assertNotNull(newManager.getProfilePicture());
        assertTrue(uploadedFile.exists());

        //start testing delete
        fm.deleteFile(newManager.getProfilePicture().getFileId(), "picture");
        PeasyUser AfterDelUser = um.getUser(manager.getEmailAddress());
        //test Project has no ProjectFile
        assertNull(AfterDelUser.getProfilePicture());
        assertFalse(uploadedFile.exists());
    }
    
}
