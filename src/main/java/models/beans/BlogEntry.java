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
	private int BlogEntryId;
	private String title;
	private String text;
	private Date creationDate;
	@ManyToOne(fetch = FetchType.LAZY)
	private PeasyUser author;
	private static final long serialVersionUID = 1L;

	public BlogEntry() {
		super();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + BlogEntryId;
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
		if (BlogEntryId != other.BlogEntryId)
			return false;
		return true;
	}

	public int getBlogEntryId() {
		return this.BlogEntryId;
	}

	public void setBlogEntryId(int BlogEntryId) {
		this.BlogEntryId = BlogEntryId;
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

	public Date getCreationDate() {
		return this.creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public PeasyUser getAuthor() {
		return author;
	}

	public void setAuthor(PeasyUser author) {
		this.author = author;
	}

}
