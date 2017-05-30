package conf;

import java.security.GeneralSecurityException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.inject.Singleton;

import ninja.lifecycle.Start;
import ninja.utils.NinjaProperties;

import com.google.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;

import models.beans.Milestone;
import models.beans.Organisation;

import models.beans.PeasyUser;
import models.beans.Project;
import models.beans.Task;
import models.manager.FileManager;
import models.manager.OrganisationManager;
import models.manager.ProjectManager;
import models.manager.UserManager;
import models.manager.exceptions.UserAlreadyExistsException;

@Singleton
public class StartupActions {

	private NinjaProperties ninjaProperties;
	private UserManager userManager;
        private ProjectManager projectManager;
        private FileManager fileManager;
        private OrganisationManager organisationManager;

	@Inject
	public StartupActions(NinjaProperties ninjaProperties, UserManager userManager, ProjectManager projectManager, FileManager fileManager,OrganisationManager organisationManager) {
		this.ninjaProperties = ninjaProperties;
		this.userManager = userManager;
                this.projectManager = projectManager;
                this.fileManager = fileManager;
                this.organisationManager = organisationManager;
                
	}

	/**
	 * Generates example data (users, project, ...) when using dev or test mode.
	 * @throws ParseException 
	 */
	@Start(order = 50)
	public void generateDummyDataWhenInTest() throws ParseException {
		if (!ninjaProperties.isProd()) {
			// as long as we are not in a production environment...
			try {
				PeasyUser user = userManager.createUser("exampleuser@peasy.com", "John", "Doe", "123");
				System.out.println("Example User created: " + user.toString());
                              
                                //Project Controller Example data
                                PeasyUser manager = userManager.createUser("manageruser@peasy.com", "Tug", "Bug", "123");               
                                Project project = projectManager.createProject(manager, "Project 1");
                                Project project2 = projectManager.createProject(user, "Project 2");
                                projectManager.updateProject(project.getProjectId(), "Project Description");
                                projectManager.updateProject(project2.getProjectId(), "Project Description");
                                projectManager.updateProjectParameters(project.getProjectId(),"2017-08-19","2018-08-19","100000");
                                projectManager.updateProjectParameters(project2.getProjectId(),"2017-08-19","2018-08-19","100000");
                               
                                projectManager.assignUserToProject(project.getProjectId(), user.getEmailAddress());
                                projectManager.assignUserToProject(project2.getProjectId(), manager.getEmailAddress());

                                //create 3 Blogentries
                                projectManager.createBlogEntry(project.getProjectId(), manager.getEmailAddress(), "TitleManager", "TextManager");
                                projectManager.createBlogEntry(project.getProjectId(), user.getEmailAddress(), "TitleUser1", "TextUser1");
                                projectManager.createBlogEntry(project.getProjectId(), user.getEmailAddress(), "TitleUser2", "TextUser2");
                                
                                //add 3 Users to Project
                                PeasyUser member1 = userManager.createUser("manageruser1@peasy.com", "Tug", "Bug", "123");       
                                PeasyUser member2 = userManager.createUser("manageruser2@peasy.com", "Tug", "Bug", "123");
                                PeasyUser member3 = userManager.createUser("manageruser3@peasy.com", "Tug", "Bug", "123");
                                projectManager.addMemberToProject(project.getProjectId(), member1.getEmailAddress());
                                projectManager.addMemberToProject(project.getProjectId(), member2.getEmailAddress());
                                projectManager.addMemberToProject(project.getProjectId(), member3.getEmailAddress());
                                
                                //create Milestones
//                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//                                Date d1 = sdf.parse("21/12/2017");
//                                Date d2 = sdf.parse("01/12/2017");
                                Milestone milestone1  = projectManager.createMilestone(project.getProjectId(), "Milestone1 ", "2017-08-19");
                                Milestone milestone2  = projectManager.createMilestone(project.getProjectId(), "Milestone2 ", "2017-08-19");

                                //create Tasks
                                Task task = projectManager.createTask(project.getProjectId(), "Task 1");
                                projectManager.updateTask(task.getTaskId(), "Task 1", "Description Task 1 ", 80, milestone1.getMileStoneId());
                                
                                Task task2 = projectManager.createTask(project.getProjectId(), "Task 2");
                                projectManager.updateTask(task2.getTaskId(), "Task 2", "Description Task 1 ", 20, milestone1.getMileStoneId());
                                
                                Task task3 = projectManager.createTask(project.getProjectId(), "Task 3");
                                projectManager.updateTask(task3.getTaskId(), "Task 3", "Description Task 1 ", 10, milestone2.getMileStoneId());
                                
                                Task task4 = projectManager.createTask(project.getProjectId(), "Task 4");
                                projectManager.updateTask(task4.getTaskId(), "Task 4", "Description Task 1 ", 0, milestone2.getMileStoneId());
                                projectManager.assignUserToTask(task.getTaskId(), member1.getEmailAddress());
                                //Upload 2 Files
                                File file1 = new File("src/test/resources/StartupFile1.pdf");
                                File file2 = new File("src/test/resources/StartupFile2.pdf");
                                
                                //provide deleting file when system is in test only
                                File delteFile = new File("target/tmp/");
                                deleteDirectory(delteFile);
                                
                                fileManager.uploadFile(file1,"projectFile1.pdf", "project", String.valueOf(project.getProjectId()));
                                fileManager.uploadFile(file2,"projectFile2.pdf", "project", String.valueOf(project.getProjectId()));
                    
                                fileManager.uploadFile(file1,"taskFile1.pdf", "task", String.valueOf(task.getTaskId()));
                                fileManager.uploadFile(file2,"taskFile2.pdf", "task", String.valueOf(task.getTaskId()));
                                
                                //create Organizaiton geht noicht wenn obige files angelegt werden
                                Organisation organisation = organisationManager.createOrganisation("P.easy", manager);
                                Organisation o = organisationManager.addUser(organisation.getOrganisationId(), user);
                                System.out.println("Example Organisation created: " + organisation.toString()); 
                                System.out.println("Example Organisation created: " + o.getUsers().size()); 


			} catch (GeneralSecurityException | UserAlreadyExistsException e) {
				// This really shouldn't be possible unless something is
				// not configured correctly.
				throw new RuntimeException(e);
			}catch (IOException | IllegalArgumentException | NoSuchElementException ex) {
                                Logger.getLogger(StartupActions.class.getName()).log(Level.SEVERE, "Startup Create Test Data Fail!", ex);
                                throw new RuntimeException(ex);
                  }

		}
		
	}
	
		/**
		 * Delets a dir recursively deleting anything inside it.
		 * @param dir The dir to delete
		 * @return true if the dir was successfully deleted
		 */
		public static boolean deleteDirectory(File dir) {
		    if(! dir.exists() || !dir.isDirectory())    {
		        return false;
		    }
	
		    String[] files = dir.list();
		    for(int i = 0, len = files.length; i < len; i++)    {
		        File f = new File(dir, files[i]);
		        if(f.isDirectory()) {
		            deleteDirectory(f);
		        }else   {
		            f.delete();
		        }
		    }
		    return dir.delete();
		}

}
