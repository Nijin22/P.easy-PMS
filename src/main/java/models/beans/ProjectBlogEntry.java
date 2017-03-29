package models.beans;

import java.io.Serializable;
import javax.persistence.*;
import models.beans.BlogEntry;

/**
 * Entity implementation class for Entity: ProjectBlogEntry
 *
 */
@Entity

public class ProjectBlogEntry extends BlogEntry implements Serializable {
	@ManyToOne
	private Project project;

	private static final long serialVersionUID = 1L;

	public ProjectBlogEntry() {
		super();
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

}
