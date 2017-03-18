package models.beans;

import java.io.Serializable;
import java.lang.String;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: Calendar
 *
 */
@Entity

public class Calendar implements Serializable {

	@Id
	private String ownerMailAddress;
	private static final long serialVersionUID = 1L;

	public Calendar() {
		super();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ownerMailAddress == null) ? 0 : ownerMailAddress.hashCode());
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
		if (ownerMailAddress == null) {
			if (other.ownerMailAddress != null)
				return false;
		} else if (!ownerMailAddress.equals(other.ownerMailAddress))
			return false;
		return true;
	}

	public String getOwnerMailAddress() {
		return this.ownerMailAddress;
	}

	public void setOwnerMailAddress(String ownerMailAddress) {
		this.ownerMailAddress = ownerMailAddress;
	}

}
