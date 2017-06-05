package models.manager;

import java.util.Set;

import javax.persistence.EntityManager;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import models.beans.Milestone;
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
	 * @param email
	 * @param name
	 * @return created Master Project
	 * @throws IllegalArgumentException
	 */
	@Transactional
	public Project createMasterProject(String email) throws IllegalArgumentException {

		if (email == null) {
			throw new IllegalArgumentException("Projectmanager can't be null");
		}

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate localDate = LocalDate.now();

		// Get Manager to persist the project object
		EntityManager entityManager = entitiyManagerProvider.get();
		PeasyUser projectManager = entityManager.find(PeasyUser.class, email);

		// Create project bean
		Project project = new Project();
		project.setProjectManager(projectManager);
		project.setName("Initial Project");
		project.setDescription("Initial Description");
		project.setBudget("0");
		project.setStart(dtf.format(localDate));
		project.setDeadline(dtf.format(localDate));
		project.setStatus(ProjectStatus.CREATED);

		// create the Task bean
		Task task = new Task();
		task.setName("Initial Task");
		task.setDescription("Initial Description");
		//Setting from Master
		task.setStartFromMaster(dtf.format(localDate));
		task.setEffortFromMaster("1");
		task.setProgress(0);
		task.setProject(project);
		task.setInitialTask(true);
		//for initial Task level is 1
		task.setLevel();

		// initial Task can't be deleted
		task.setInitialTask(true);

		project.getTasks().add(task);

		// Persist the project
		entityManager.persist(project);

		// Persist the task
		entityManager.persist(task);

		return project;
	}

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
	 * @param taskId
	 * @param description
	 * @return Task
	 * @throws NoSuchElementException
	 */
	@Transactional
	public void addTaskDependency(long taskIdCurrent, long taskIdDepend) throws NoSuchElementException {

		EntityManager entityManager = entitiyManagerProvider.get();
		Task taskCurrent = entityManager.find(Task.class, taskIdCurrent);
		Task taskDepend = entityManager.find(Task.class, taskIdDepend);

		if (taskCurrent == null) {
			throw new NoSuchElementException("Task with taskId " + taskIdCurrent + "is not in the database");
		} else if (taskDepend == null) {
			throw new NoSuchElementException("Task with taskId " + taskIdDepend + "is not in the database");
		} else {
			// überarbeiten
			taskCurrent.getUpTasks().add(taskDepend);
			taskDepend.getBelowTasks().add(taskCurrent);

			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date latestDate = null;
			Calendar c = Calendar.getInstance();
			try {
				c.setTime(format.parse(taskDepend.getStart()));
				c.add(Calendar.DATE, Integer.parseInt(taskDepend.getEffort()));
				latestDate = c.getTime();
				for (Task task : taskCurrent.getUpTasks()) {
					c.setTime(format.parse(task.getStart()));
					c.add(Calendar.DATE, Integer.parseInt(task.getEffort()));
					Date dateStart = c.getTime();
					if (latestDate.before(dateStart)) {
						latestDate = c.getTime();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			taskCurrent.setLevel();
			taskCurrent.setStart(format.format(latestDate));

		}

	}
	
	/**
	 *
	 * @param taskId
	 * @param description
	 * @return Task
	 * @throws NoSuchElementException
	 */
	@Transactional
	public void removeTaskDependency(long taskIdCurrent, long taskIdDepend) throws NoSuchElementException {

		EntityManager entityManager = entitiyManagerProvider.get();
		Task taskCurrent = entityManager.find(Task.class, taskIdCurrent);
		Task taskDepend = entityManager.find(Task.class, taskIdDepend);

		if (taskCurrent == null) {
			throw new NoSuchElementException("Task with taskId " + taskIdCurrent + "is not in the database");
		} else if (taskDepend == null) {
			throw new NoSuchElementException("Task with taskId " + taskIdDepend + "is not in the database");
		} else {
			
			taskCurrent.getUpTasks().remove(taskDepend);
			taskDepend.getBelowTasks().remove(taskCurrent);

			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date latestDate = null;
			Calendar c = Calendar.getInstance();
			try {
				latestDate = format.parse("1900-10-23");
				for (Task task : taskCurrent.getUpTasks()) {
					c.setTime(format.parse(task.getStart()));
					c.add(Calendar.DATE, Integer.parseInt(task.getEffort()));
					Date dateStart = c.getTime();
					if (latestDate.before(dateStart)) {
						latestDate = c.getTime();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			taskCurrent.setLevel();
			taskCurrent.setStart(format.format(latestDate));

		}

	}

	/**
	 *
	 * @param taskId
	 * @param newname
	 * @return task
	 * @throws NoSuchElementException
	 */
	@Transactional
	public Task changeTaskname(long taskId, String newname) throws NoSuchElementException {
		EntityManager entityManager = entitiyManagerProvider.get();
		Task task = entityManager.find(Task.class, taskId);

		if (task == null) {
			throw new NoSuchElementException("Task with taskId " + taskId + "is not in the database");
		} else {
			task.setName(newname);
			return task;
		}

	}

	/**
	 *
	 * @param projectId
	 * @param newname
	 * @return Project
	 * @throws NoSuchElementException
	 */
	@Transactional
	public Project changeProjectname(long projectId, String newname) throws NoSuchElementException {
		EntityManager entityManager = entitiyManagerProvider.get();
		Project project = entityManager.find(Project.class, projectId);

		if (project == null) {
			throw new NoSuchElementException("Project with projectId " + projectId + "is not in the database");
		} else {
			project.setName(newname);
			return project;
		}

	}

	/**
	 *
	 * @param projectId
	 * @param start
	 * @return Project
	 * @throws NoSuchElementException
	 */
	@Transactional
	public Project changeProjectStart(long projectId, String start) throws NoSuchElementException {
		EntityManager entityManager = entitiyManagerProvider.get();
		Project project = entityManager.find(Project.class, projectId);

		if (project == null) {
			throw new NoSuchElementException("Project with projectId " + projectId + "is not in the database");
		} else {
			project.setStart(start);
			return project;
		}

	}

	/**
	 *
	 * @param projectId
	 * @param end
	 * @return Project
	 * @throws NoSuchElementException
	 */
	@Transactional
	public Project changeProjectEnd(long projectId, String end) throws NoSuchElementException {
		EntityManager entityManager = entitiyManagerProvider.get();
		Project project = entityManager.find(Project.class, projectId);

		if (project == null) {
			throw new NoSuchElementException("Project with projectId " + projectId + "is not in the database");
		} else {
			project.setDeadline(end);
			return project;
		}

	}

	/**
	 *
	 * @param projectId
	 * @param end
	 * @return Project
	 * @throws NoSuchElementException
	 */
	@Transactional
	public Milestone changeMilestoneEnd(long milestoneID, String end) throws NoSuchElementException {
		EntityManager entityManager = entitiyManagerProvider.get();
		Milestone milestone = entityManager.find(Milestone.class, milestoneID);

		if (milestone == null) {
			throw new NoSuchElementException("Milestone with milestoneID " + milestoneID + "is not in the database");
		} else {
			milestone.setDeadline(end);
			return milestone;
		}

	}

	/**
	 *
	 * @param projectId
	 * @param end
	 * @return Project
	 * @throws NoSuchElementException
	 */
	@Transactional
	public Milestone changeMilestoneName(long milestoneID, String name) throws NoSuchElementException {
		EntityManager entityManager = entitiyManagerProvider.get();
		Milestone milestone = entityManager.find(Milestone.class, milestoneID);

		if (milestone == null) {
			throw new NoSuchElementException("Milestone with milestoneID " + milestoneID + "is not in the database");
		} else {
			milestone.setName(name);
			return milestone;
		}

	}

	/**
	 *
	 * @return List<PeasyUser>
	 */
	@Transactional
	public List<PeasyUser> getAllPeasyusers() {
		EntityManager entityManager = entitiyManagerProvider.get();
		List<PeasyUser> listPersons = entityManager.createQuery("SELECT p FROM PeasyUser p").getResultList();

		return listPersons;

	}

	/**
	 *
	 * @param projectId
	 * @param end
	 * @return Project
	 * @throws NoSuchElementException
	 */
	@Transactional
	public Project changeProjectBudget(long projectId, String budget) throws NoSuchElementException {
		EntityManager entityManager = entitiyManagerProvider.get();
		Project project = entityManager.find(Project.class, projectId);

		if (project == null) {
			throw new NoSuchElementException("Project with projectId " + projectId + "is not in the database");
		} else {
			project.setBudget(budget);
			return project;
		}

	}

	/**
	 *
	 * @param projectId
	 * @param description
	 * @return Project
	 * @throws NoSuchElementException
	 */
	@Transactional
	public Project changeProjectDescription(long projectId, String description) throws NoSuchElementException {
		EntityManager entityManager = entitiyManagerProvider.get();
		Project project = entityManager.find(Project.class, projectId);

		if (project == null) {
			throw new NoSuchElementException("Project with projectId " + projectId + "is not in the database");
		} else {
			project.setDescription(description);
			return project;
		}

	}

	/**
	 *
	 * @param taskId
	 * @param description
	 * @return Task
	 * @throws NoSuchElementException
	 */
	@Transactional
	public Task changeTaskDescription(long taskId, String description) throws NoSuchElementException {
		EntityManager entityManager = entitiyManagerProvider.get();
		Task task = entityManager.find(Task.class, taskId);

		if (task == null) {
			throw new NoSuchElementException("Task with taskId " + taskId + "is not in the database");
		} else {
			task.setDescription(description);
			return task;
		}

	}

	/**
	 *
	 * @param taskId
	 * @param description
	 * @return Task
	 * @throws NoSuchElementException
	 */
	@Transactional
	public Task changeTaskProgress(long taskId, String progress) throws NoSuchElementException {
		EntityManager entityManager = entitiyManagerProvider.get();
		Task task = entityManager.find(Task.class, taskId);

		if (task == null) {
			throw new NoSuchElementException("Task with taskId " + taskId + "is not in the database");
		} else {
			task.setProgress(Integer.parseInt(progress));
			;
			return task;
		}

	}

	/**
	 *
	 * @param taskId
	 * @param description
	 * @return Task
	 * @throws NoSuchElementException
	 */
	@Transactional
	public Task changeTaskEffort(long taskId, String effort) throws NoSuchElementException {
		EntityManager entityManager = entitiyManagerProvider.get();
		Task task = entityManager.find(Task.class, taskId);

		if (task == null) {
			throw new NoSuchElementException("Task with taskId " + taskId + "is not in the database");
		} else {
			task.setEffort(effort);
			return task;
		}

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
		// description can be null, so no exception handling is requiered

		EntityManager entityManager = entitiyManagerProvider.get();
		Project project = entityManager.find(Project.class, projectId);

		if (project == null) {
			throw new NoSuchElementException("Project with projectId " + projectId + "is not in the database");
		} else {
			// set new description to project
			project.setDescription(description);
			return project;
		}
	}

	/**
	 *
	 * @param projectId
	 * @param name
	 * @param deadline
	 * @return created Milestone
	 * @throws NoSuchElementException
	 */
	@Transactional
	public Milestone createMilestone(long projectId, String name, String deadline) throws NoSuchElementException {
		// description can be null, so no exception handling is requiered

		EntityManager entityManager = entitiyManagerProvider.get();
		Project project = entityManager.find(Project.class, projectId);

		if (project == null) {
			throw new NoSuchElementException("Project with projectId " + projectId + "is not in the database");
		} else {
			// create Milestone
			Milestone milestone = new Milestone();
			milestone.setName(name);
			milestone.setDeadline(deadline);
			milestone.setProject(project);
			entityManager.persist(milestone);

			// assign Milestone to User
			project.getMilestones().add(milestone);
			return milestone;
		}
	}

	/**
	 *
	 * @param milestoneId
	 * @return void
	 * @throws NoSuchElementException
	 */
	@Transactional
	public void deleteMilestone(long milestoneId) throws NoSuchElementException {
		// description can be null, so no exception handling is requiered

		EntityManager entityManager = entitiyManagerProvider.get();
		Milestone milestone = entityManager.find(Milestone.class, milestoneId);

		if (milestone == null) {
			throw new NoSuchElementException("Milestone with milestoneId " + milestoneId + "is not in the database");
		} else {
			// delete Milestone
			entityManager.remove(milestone);
		}
	}

	/**
	 *
	 * @param milestoneId
	 * @return Milestone
	 * @throws NoSuchElementException
	 */
	@Transactional
	public Milestone getMilestone(long milestoneId) throws NoSuchElementException {
		EntityManager entityManager = entitiyManagerProvider.get();
		Milestone milestone = entityManager.find(Milestone.class, milestoneId);

		if (milestone == null) {
			throw new NoSuchElementException("Milestone with milestoneId " + milestoneId + "is not in the database");
		} else {
			return milestone;
		}

	}

	/**
	 *
	 * @param milestoneId
	 * @param name
	 * @param deadline
	 * @return updated Milestone
	 * @throws NoSuchElementException
	 */
	@Transactional
	public Milestone updateMilestone(long milestoneId, String name, String deadline) throws NoSuchElementException {

		EntityManager entityManager = entitiyManagerProvider.get();
		Milestone milestone = entityManager.find(Milestone.class, milestoneId);

		if (milestone == null) {
			throw new NoSuchElementException("Milestone with milestoneId " + milestoneId + "is not in the database");
		} else {
			// set new description to project
			milestone.setDeadline(deadline);
			milestone.setName(name);
			return milestone;
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
	public Project updateProjectParameters(long projectId, String start, String deadline, String budget)
			throws NoSuchElementException {
		// description can be null, so no exception handling is requiered

		EntityManager entityManager = entitiyManagerProvider.get();
		Project project = entityManager.find(Project.class, projectId);

		if (project == null) {
			throw new NoSuchElementException("Project with projectId " + projectId + "is not in the database");
		} else {
			// set new description to project
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
	public Project changeProjectState(long projectId, ProjectStatus status)
			throws IllegalArgumentException, NoSuchElementException {
		if (status == null) {
			throw new IllegalArgumentException("status can't be null");
		}

		EntityManager entityManager = entitiyManagerProvider.get();
		Project project = entityManager.find(Project.class, projectId);

		if (project == null) {
			throw new NoSuchElementException("Project with projectId " + projectId + "is not in the database");
		} else {
			// set new status to project
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
	@Transactional
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
	public Project addMemberToProject(long projectId, String email) throws NoSuchElementException {

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
	public Project removeMemberFromProject(long projectId, String email)
			throws IllegalArgumentException, NoSuchElementException {
		if (email == null) {
			throw new IllegalArgumentException("User can't be null");
		}
		EntityManager entityManager = entitiyManagerProvider.get();
		Project project = entityManager.find(Project.class, projectId);
		PeasyUser user = entityManager.find(PeasyUser.class, email);

		if (project == null) {
			throw new NoSuchElementException("Project with projectId " + projectId + "is not in the database");
		} else if (user == null) {
			throw new NoSuchElementException("User with email " + email + "is not in the database");
		} else {
			// remove user from project
			project.getUsers().remove(user);
			// remove project from user
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



			// set initial Task attributes
			task.setProject(project);
			task.setName("Initial Task");
			task.setProgress(1);
			task.setInitalEffort("1");
			task.setInitialTask(false);
			task.setDescription("Initial Description");
			

			// set initial Task Dependency
			long initialTaskId = -1;
			for (Task initialTaskFinder : project.getTasks()) {
				if (initialTaskFinder.isInitialTask()) {
					initialTaskId = initialTaskFinder.getTaskId();
				}
			}
			Task initialTask = null;
			SimpleDateFormat dtf = new SimpleDateFormat("yyyy-MM-dd");
			
			if (initialTaskId != -1) {
			    initialTask = entityManager.find(Task.class, initialTaskId);
				task.getUpTasks().add(initialTask);
				initialTask.getBelowTasks().add(task);
				task.setLevel();
				
				// set startdate of task before effort
				Date startDate = null;
				try {
					startDate = dtf.parse(initialTask.getStart());
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				Calendar c = Calendar.getInstance();
				c.setTime(startDate);
				c.add(Calendar.DATE, Integer.parseInt(initialTask.getEffort()));
				task.setInitialStart(dtf.format(c.getTime()));
				
			}else{
				// if here it is a inital task
				DateTimeFormatter dateformat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				LocalDate localDate = LocalDate.now();
				task.setInitialStart(dateformat.format(localDate));
				task.setLevel();
			}
			


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
	 * @param milestone
	 * @return Updated Task
	 * @throws NoSuchElementException
	 */
	@Transactional
	public Task updateTask(long taskId, String name, String description, int progress, long milestoneId, String effort)
			throws NoSuchElementException {
		// only name can't be null, description and progress can be null
		if (name == null || name.isEmpty()) {
			throw new IllegalArgumentException("Name can't be empty");
		}

		EntityManager entityManager = entitiyManagerProvider.get();
		Task task = entityManager.find(Task.class, taskId);
		Milestone milestone = entityManager.find(Milestone.class, milestoneId);

		if (task == null) {
			throw new NoSuchElementException("Task with taskId " + taskId + "is not in the database");
		} else {
			task.setName(name);
			task.setDescription(description);
			task.setProgress(progress);
			task.setMilestone(milestone);
			task.setEffort(effort);
			milestone.getTasks().add(task);
			return task;
		}
	}

	/**
	 *
	 * @param taskId
	 * @param initial
	 * @return Updated Task
	 * @throws NoSuchElementException
	 */
	@Transactional
	public Task setInitialTask(long taskId, boolean initial) throws NoSuchElementException {

		EntityManager entityManager = entitiyManagerProvider.get();
		Task task = entityManager.find(Task.class, taskId);

		if (task == null) {
			throw new NoSuchElementException("Task with taskId " + taskId + "is not in the database");
		} else {
			task.setInitialTask(initial);
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

	/**
	 *
	 * @param projectId
	 * @param email
	 * @return updated Project
	 * @throws NoSuchElementException
	 */
	@Transactional
	public Project assignUserToProject(long projectId, String email) throws NoSuchElementException {

		EntityManager entityManager = entitiyManagerProvider.get();
		Project project = entityManager.find(Project.class, projectId);
		PeasyUser user = entityManager.find(PeasyUser.class, email);

		if (project == null) {
			throw new NoSuchElementException("Project with projectId: " + projectId + "doesn't exist in the database");
		}
		if (user == null) {
			throw new NoSuchElementException("User with email: " + email + "doesn't exist in the database");
		}

		project.getUsers().add(user);
		user.getProject().add(project);

		return project;

	}

	/**
	 *
	 * @param milestoneId
	 * @param taskId
	 * @return updated Milestone
	 * @throws NoSuchElementException
	 */
	@Transactional
	public Milestone addTasktoMilestone(long milestoneId, long taskId) throws NoSuchElementException {

		EntityManager entityManager = entitiyManagerProvider.get();
		Milestone milestone = entityManager.find(Milestone.class, milestoneId);
		Task task = entityManager.find(Task.class, taskId);

		if (milestone == null) {
			throw new NoSuchElementException(
					"Milestone with milestoneId: " + milestoneId + "doesn't exist in the database");
		}
		if (task == null) {
			throw new NoSuchElementException("Task with taskId: " + taskId + "doesn't exist in the database");
		}

		milestone.getTasks().add(task);
		task.setMilestone(milestone);

		return milestone;

	}

	/**
	 *
	 * @param milestoneId
	 * @param taskId
	 * @return updated Milestone
	 * @throws NoSuchElementException
	 */
	@Transactional
	public Milestone deleteTaskfromMilestone(long milestoneId, long taskId) throws NoSuchElementException {

		EntityManager entityManager = entitiyManagerProvider.get();
		Milestone milestone = entityManager.find(Milestone.class, milestoneId);
		Task task = entityManager.find(Task.class, taskId);

		if (milestone == null) {
			throw new NoSuchElementException(
					"Milestone with milestoneId: " + milestoneId + "doesn't exist in the database");
		}
		if (task == null) {
			throw new NoSuchElementException("Task with taskId: " + taskId + "doesn't exist in the database");
		}

		milestone.getTasks().remove(task);
		task.setMilestone(null);

		return milestone;

	}

	/**
	 *
	 * @param projectId
	 * @param email
	 * @return updated Project
	 * @throws NoSuchElementException
	 */
	@Transactional
	public Project changeProjectmanager(long projectId, String email) throws NoSuchElementException {

		EntityManager entityManager = entitiyManagerProvider.get();
		Project project = entityManager.find(Project.class, projectId);
		PeasyUser user = entityManager.find(PeasyUser.class, email);

		if (project == null) {
			throw new NoSuchElementException("Project with projectId: " + projectId + "doesn't exist in the database");
		}
		if (user == null) {
			throw new NoSuchElementException("User with email: " + email + "doesn't exist in the database");
		}

		project.setProjectManager(user);
		user.getProjectsWhereUserIsManager().add(project);

		return project;

	}

	/**
	 *
	 * @param projectId
	 * @param email
	 * @return Updated Project
	 * @throws NoSuchElementException
	 */
	@Transactional
	public Project unassignUserFromProject(long projectId, String email) throws IllegalArgumentException {

		EntityManager entityManager = entitiyManagerProvider.get();
		Project project = entityManager.find(Project.class, projectId);
		PeasyUser user = entityManager.find(PeasyUser.class, email);

		if (project == null) {
			throw new NoSuchElementException("Project with projectId: " + projectId + "doesn't exist in the database");
		}
		if (user == null) {
			throw new NoSuchElementException("User with email: " + email + "doesn't exist in the database");
		}
		project.getUsers().remove(user);
		user.getProjects().remove(project);

		return project;
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
	public ProjectBlogEntry createBlogEntry(long projectId, String email, String title, String text)
			throws NoSuchElementException {

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
