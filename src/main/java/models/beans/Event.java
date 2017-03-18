package models.beans;

import java.io.Serializable;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: Event
 *
 */
@Entity

public class Event implements Serializable {

	@Id
	private int Id;
	private static final long serialVersionUID = 1L;

	public Event() {
		super();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Id;
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
		Event other = (Event) obj;
		if (Id != other.Id)
			return false;
		return true;
	}

	public int getId() {
		return this.Id;
	}

	public void setId(int Id) {
		this.Id = Id;
	}

}
