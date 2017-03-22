package models.beans;

import java.io.Serializable;
import java.lang.String;
import java.util.Set;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: Calendar
 *
 */
@Entity

public class Calendar implements Serializable {

	@Id
	@OneToOne()
	private PeasyUser owner;
	@OneToMany(mappedBy = "calendar")
	private Set<Event> events;
	private static final long serialVersionUID = 1L;

	public Calendar() {
		super();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((owner == null) ? 0 : owner.hashCode());
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
		Calendar other = (Calendar) obj;
		if (owner == null) {
			if (other.owner != null)
				return false;
		} else if (!owner.equals(other.owner))
			return false;
		return true;
	}

	public PeasyUser getOwnerMailAddress() {
		return this.owner;
	}

	public void setOwnerMailAddress(PeasyUser ownerMailAddress) {
		this.owner = ownerMailAddress;
	}

	public Set<Event> getEvents() {
		return events;
	}

	public void setEvents(Set<Event> events) {
		this.events = events;
	}

}
