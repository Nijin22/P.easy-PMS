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
	private int taskId;
	private String title;
	private String description;
	private float progress;
	@ManyToOne
	private Project project;
	@OneToMany(mappedBy="task")
	private Set<TaskBlogEntry> blogEntries;
	private static final long serialVersionUID = 1L;

	public Task() {
		super();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + taskId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Task other = (Task) obj;
		if (taskId != other.taskId)
			return false;
		return true;
	}

	public int getTaskId() {
		return this.taskId;
	}

	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
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

}
