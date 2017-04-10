package models.beans;

import java.io.Serializable;
import java.lang.String;
import java.util.Set;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: Organisation
 *
 */
@Entity

public class Organisation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int organisationId;
    private String name;
    @OneToOne
    private PeasyUser organisationAdmin;
    @OneToMany(mappedBy = "organisation")
    private Set<PeasyUser> users;
    private static final long serialVersionUID = 1L;

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

    public PeasyUser getOrganisationAdmin() {
        return organisationAdmin;
    }

    public void setOrganisationAdmin(PeasyUser organisationAdmin) {
        this.organisationAdmin = organisationAdmin;
    }

    public Set<PeasyUser> getUsers() {
        return users;
    }

    public void setUsers(Set<PeasyUser> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "Organisation{" + "organisationId=" + organisationId + ", name=" + name + ", organisationAdmin=" + organisationAdmin.toString() + ", userSize=" + users.size() + '}';
    }
    
      @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + organisationId;
        return result;
    }
    
        @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Organisation other = (Organisation) obj;
        if (organisationId != other.organisationId) {
            return false;
        }
        return true;
    }

}
