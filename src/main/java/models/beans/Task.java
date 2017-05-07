package models.beans;

import java.io.Serializable;
import java.lang.String;
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
    private float progress;
    @ManyToOne
    private Project project;
    @ManyToMany
    private Set<PeasyUser> users;
    @OneToMany(mappedBy = "task")
    private Set<TaskBlogEntry> blogEntries;
    private static final long serialVersionUID = 1L;
    @OneToMany(mappedBy = "task",fetch = FetchType.EAGER)
    private Set<TaskFile> taskFiles;
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
