package models.beans;

import java.io.Serializable;
import javax.persistence.*;
import models.beans.BlogEntry;

/**
 * Entity implementation class for Entity: TaskBlogEntry
 *
 */
@Entity

public class TaskBlogEntry extends BlogEntry implements Serializable {
	@ManyToOne
	private Task task;
	private static final long serialVersionUID = 1L;

	public TaskBlogEntry() {
		super();
	}

}
