package models.beans;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import models.beans.PeasyUser;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long projectId;
    @NotNull
    private String name;
    private String description;
    private ProjectStatus status;
    private String start;
    private String deadline;
    private String budget;
    @OneToMany(mappedBy = "project",fetch = FetchType.EAGER)
    private Set<Task> tasks;
    @OneToMany(mappedBy = "project",fetch = FetchType.EAGER)
    private Set<ProjectBlogEntry> blogEntries;
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<PeasyUser> projectMembers;
    @ManyToOne
    private PeasyUser projectManager;
    @OneToMany(mappedBy = "project",fetch = FetchType.EAGER)
    private Set<ProjectFile> projectFiles;

    public Project() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Task> getTasks() {
        return tasks;
    }

    public Set<PeasyUser> getUsers() {
        return projectMembers;
    }

    public void SetProjectMembers(Set<PeasyUser> param) {
        this.projectMembers = param;
    }

    public ProjectStatus getStatus() {
        return status;
    }

    public void setStatus(ProjectStatus status) {
        this.status = status;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Set<ProjectBlogEntry> getBlogEntries() {
        return blogEntries;
    }

    public void setBlogEntries(Set<ProjectBlogEntry> blogEntries) {
        this.blogEntries = blogEntries;
    }

    public Set<PeasyUser> getProjectMembers() {
        return projectMembers;
    }

    public void setProjectMembers(Set<PeasyUser> projectMembers) {
        this.projectMembers = projectMembers;
    }

    public PeasyUser getProjectManager() {
        return projectManager;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public void setProjectManager(PeasyUser projectManager) {
        this.projectManager = projectManager;
    }

    public Set<ProjectFile> getProjectFiles() {
        return projectFiles;
    }

    public void setProjectFiles(Set<ProjectFile> projectFiles) {
        this.projectFiles = projectFiles;
    }
    
    

    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((projectId == null) ? 0 : projectId.hashCode());
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
        Project other = (Project) obj;
        if (projectId == null) {
            if (other.projectId != null) {
                return false;
            }
        } else if (!projectId.equals(other.projectId)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Project{" + "projectId=" + projectId + ", name=" + name + ", description=" + description + ", status=" + status + ", start=" + start + ", deadline=" + deadline + ", budget=" + budget + '}';
    }




    
    
}
