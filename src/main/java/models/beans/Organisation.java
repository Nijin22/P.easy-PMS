package models.beans;

import java.io.Serializable;
import java.lang.String;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: Organisation
 *
 */
@Entity

public class Organisation implements Serializable {

	@Id
	private int organisationId;
	private String name;
	@OneToOne
	private PeasyUser organisationAdmin;
	private static final long serialVersionUID = 1L;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + organisationId;
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
		Organisation other = (Organisation) obj;
		if (organisationId != other.organisationId)
			return false;
		return true;
	}

	public Organisation() {
		super();
	}

	public int getOrganisationId() {
		return this.organisationId;
	}

	public void setOrganisationId(int organisationId) {
		this.organisationId = organisationId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
