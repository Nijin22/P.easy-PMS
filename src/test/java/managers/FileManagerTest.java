/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managers;

import com.google.inject.Inject;
import com.google.inject.Provider;
import java.io.File;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import models.beans.PeasyUser;
import models.beans.Project;
import models.beans.ProjectFile;
import models.manager.FileManager;
import models.manager.ProjectManager;
import models.manager.UserManager;
import ninja.NinjaTest;
import ninja.uploads.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author Tugrul
 */
public class FileManagerTest extends NinjaTest {

    private static Logger log = null;

    public FileManagerTest() {
    }

    @Before
    public void setUp() {
         //initialize Logger to log Test
        log = Logger.getLogger(FileManagerTest.class.getName());
        
    }
      

    /**
     * Test of uploadFile and deleteFile methods, of class FileManager.
     * @throws java.lang.Exception
     */
    @Test
    public void testUploadDeleteFile() throws Exception {
        log.info("Start Testing Succesfull Test: uploadFile");
       
        FileManager fm = getInjector().getInstance(FileManager.class);
        ProjectManager pm = getInjector().getInstance(ProjectManager.class);
        UserManager um = getInjector().getInstance(UserManager.class);

        //create Project and manager
        PeasyUser manager = um.createUser("tugi@peasy.de", "Tugi", "Bugi", "123456789");
        Project createdProject = pm.createProject(manager, "Project 1");

        //uploadFile
        File file = new File("src/test/resources/TestfileForUpload.pdf");
        String uploadedFileLocation = fm.uploadFile(file, "test.pdf", "project", String.valueOf(createdProject.getProjectId()));
        
        //test Objects
        Project newProject = pm.getProject(createdProject.getProjectId());        
        File uploadedFile = new File(uploadedFileLocation);

        assertEquals(1, newProject.getProjectFiles().size());
        assertTrue(uploadedFile.exists());
        
        //get First and the only Object in Set
        ProjectFile projectFile = newProject.getProjectFiles().iterator().next();
        
        //test deleteMethod CAUTION After changing logic use getID instead of getTitle!!!!!
        //fm.deleteFile(projectFile.getTitle(), "project");
        //assertFalse(uploadedFile.exists());
        
        //start testing delete<
        fm.deleteFile(projectFile.getFileId(), "project");
        Project AfterDelProject = pm.getProject(createdProject.getProjectId());    
        assertEquals(0, AfterDelProject.getProjectFiles().size());
        
        //rmove this part after implementing logic, that file is saved with id not with name and uncomment part in deleteFile
        uploadedFile.delete();

        
    }
}
