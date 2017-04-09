package models.beans;

import java.io.Serializable;
import java.lang.String;
import javax.persistence.*;
import models.beans.Project;
import java.util.Set;

/**
 * Entity implementation class for Entity: User
 *
 */
@Entity
public class PeasyUser implements Serializable {

    @Id
    private String emailAddress;
    private String firstName;
    private String lastName;
    private String formOfAddress;
    private String passwordInDb;
    private static final long serialVersionUID = 1L;
    @ManyToMany(mappedBy = "projectMembers")
    private Set<Project> projects;
    @ManyToMany(mappedBy = "users")
    private Set<Task> tasks;
    @OneToMany(mappedBy = "projectManager")
    private Set<Project> projectsWhereUserIsManager;
    @ManyToOne()
    private Organisation organisation;

    public PeasyUser() {
        super();
    }

    /**
     * @param emailAddress
     * @param firstName
     * @param lastName
     * @param password
     */
    public PeasyUser(String emailAddress, String firstName, String lastName, String pwHashSalt) {
        super();
        this.emailAddress = emailAddress;
        this.firstName = firstName;
        this.lastName = lastName;

        this.passwordInDb = pwHashSalt;

        formOfAddress = firstName;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((emailAddress == null) ? 0 : emailAddress.hashCode());
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
        PeasyUser other = (PeasyUser) obj;
        if (emailAddress == null) {
            if (other.emailAddress != null) {
                return false;
            }
        } else if (!emailAddress.equals(other.emailAddress)) {
            return false;
        }
        return true;
    }

    public String getEmailAddress() {
        return this.emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Set<Task> getTasks() {
        return tasks;
    }

    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }

    public String getFormOfAddress() {
        return this.formOfAddress;
    }

    public void setFormOfAddress(String formOfAddress) {
        this.formOfAddress = formOfAddress;
    }

    public Set<Project> getProjects() {
        return projects;
    }

    public void setProjects(Set<Project> projects) {
        this.projects = projects;
    }

    public Set<Project> getProjectsWhereUserIsManager() {
        return projectsWhereUserIsManager;
    }

    public void setProjectsWhereUserIsManager(Set<Project> projectsWhereUserIsManager) {
        this.projectsWhereUserIsManager = projectsWhereUserIsManager;
    }

    public String getPasswordInDb() {
        return this.passwordInDb;
    }

    public void setPasswordInDb(String password) {
        this.passwordInDb = password;
    }

    public Set<Project> getProject() {
        return projects;
    }

    public void setProject(Set<Project> param) {
        this.projects = param;
    }

    public Organisation getOrganisation() {
        return organisation;
    }

    public void setOrganisation(Organisation organisation) {
        this.organisation = organisation;
    }

}
