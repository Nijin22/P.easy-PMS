package models.beans;

import java.io.Serializable;
import java.lang.String;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: Task
 *
 */
@Entity
public class Task implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long taskId;
	private String name;
	private String description;
	private String effort;
	private String start;
	private int level;
	private boolean initialTask;

	private float progress;

	@ManyToMany(mappedBy = "belowTasks")
	private Set<Task> upTasks = new HashSet<>();

	@ManyToMany
	private Set<Task> belowTasks = new HashSet<>();

	@ManyToOne
	private Project project;

	@ManyToMany
	private Set<PeasyUser> users = new HashSet<>();
	@OneToMany(mappedBy = "task")
	private Set<TaskBlogEntry> blogEntries = new HashSet<>();
	private static final long serialVersionUID = 1L;
	@OneToMany(mappedBy = "task")
	private Set<TaskFile> taskFiles = new HashSet<>();

	@ManyToOne
	Milestone milestone;

	public Task() {
		super();
	}

	public long getTaskId() {
		return this.taskId;
	}

	public void setTaskId(long taskId) {
		this.taskId = taskId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String title) {
		this.name = title;
	}

	public Set<PeasyUser> getUsers() {
		return users;
	}

	public void setUsers(Set<PeasyUser> users) {
		this.users = users;
	}

	public boolean isInitialTask() {
		return initialTask;
	}

	public void setInitialTask(boolean initialTask) {
		this.initialTask = initialTask;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public float getProgress() {
		return this.progress;
	}

	public void setProgress(float progress) {
		this.progress = progress;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public Set<TaskBlogEntry> getBlogEntries() {
		return blogEntries;
	}

	public void setBlogEntries(Set<TaskBlogEntry> blogEntries) {
		this.blogEntries = blogEntries;
	}

	public Set<TaskFile> getTaskFiles() {
		return taskFiles;
	}

	public void setTaskFiles(Set<TaskFile> taskFiles) {
		this.taskFiles = taskFiles;
	}

	public Milestone getMilestone() {
		return milestone;
	}

	public void setMilestone(Milestone milestone) {
		this.milestone = milestone;
	}

	public Set<Task> getUpTasks() {
		return upTasks;
	}

	public void setUpTasks(Set<Task> upTasks) {
		this.upTasks = upTasks;
	}

	public Set<Task> getBelowTasks() {
		return belowTasks;
	}

	public void setBelowTasks(Set<Task> belowTasks) {
		this.belowTasks = belowTasks;
	}

	public String getEffort() {
		return effort;
	}

	public void setEffort(String effort) {
		this.effort = effort;
	}

	public String getStart() {

		if (initialTask == true || upTasks.isEmpty()) {
			return project.getStart();
		} else {

			Date earliestDate = null;
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

			for (Task task : upTasks) {

				String start = task.getStart();

				// calculating end task
				Calendar c = Calendar.getInstance();
				try {
					c.setTime(format.parse(start));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				c.add(Calendar.DATE, Integer.parseInt(task.getEffort()));
				Date end = c.getTime();

				if (earliestDate == null) {
					earliestDate = end;
				} else {
					if (earliestDate.before(end)) {
						earliestDate = end;
					}
				}

			}

			return format.format(earliestDate);
		}
	}

	public void setStart(String start) {
		this.start = start;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (taskId ^ (taskId >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Task other = (Task) obj;
		if (taskId != other.taskId) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Task [taskId=" + taskId + ", name=" + name + ", description=" + description + ", progress=" + progress
				+ ", project=" + project + ", milestone=" + milestone + "]";
	}

}
