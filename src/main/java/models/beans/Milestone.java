package models.beans;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

@Entity
public class Milestone {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Long mileStoneId;
	@NotNull
	private String name;
	private Date deadline;
	private float progress;
	@ManyToOne
	private Project project;
	@OneToMany(mappedBy = "milestone")
	private Set<Task> tasks = new HashSet<>();
	
	public Long getMileStoneId() {
		return mileStoneId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getDeadline() {
		return deadline;
	}
	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}
	public float getProgress() {
		return progress;
	}
	public Project getProject() {
		return project;
	}
	public void setProject(Project project) {
		this.project = project;
	}
	public void setProgress(float progress) {
		this.progress = progress;
	}

	
	public Set<Task> getTasks() {
		return tasks;
	}
	public void setTasks(Set<Task> tasks) {
		this.tasks = tasks;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((mileStoneId == null) ? 0 : mileStoneId.hashCode());
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
		Milestone other = (Milestone) obj;
		if (mileStoneId == null) {
			if (other.mileStoneId != null)
				return false;
		} else if (!mileStoneId.equals(other.mileStoneId))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "Milestone [mileStoneId=" + mileStoneId + ", name=" + name + ", deadline=" + deadline + ", progress="
				+ progress + ", project=" + project + "]";
	}

}
