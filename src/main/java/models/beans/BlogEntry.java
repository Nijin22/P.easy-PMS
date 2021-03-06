package models.beans;

import java.io.Serializable;
import java.lang.String;
import java.util.Date;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: BlogEntry
 *
 */
@Entity

public abstract class BlogEntry implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int blogEntryId;
	private String title;
	private String text;
	private String creationDate;
	@ManyToOne(fetch = FetchType.EAGER)
	private PeasyUser author;
	private static final long serialVersionUID = 1L;

	public BlogEntry() {
		super();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + blogEntryId;
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
		BlogEntry other = (BlogEntry) obj;
		if (blogEntryId != other.blogEntryId)
			return false;
		return true;
	}

	public int getBlogEntryId() {
		return this.blogEntryId;
	}

	public void setBlogEntryId(int BlogEntryId) {
		this.blogEntryId = BlogEntryId;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getCreationDate() {
		return this.creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	public PeasyUser getAuthor() {
		return author;
	}

	public void setAuthor(PeasyUser author) {
		this.author = author;
	}

}
