package models.beans;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import org.apache.regexp.recompile;

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
    
    @OneToMany(mappedBy = "project")
    private Set<Task> tasks = new HashSet<>();
    @OneToMany(mappedBy = "project")
    private Set<ProjectBlogEntry> blogEntries = new HashSet<>();
    @ManyToMany()
    private Set<PeasyUser> projectMembers = new HashSet<>();
    @ManyToOne
    private PeasyUser projectManager;
    @OneToMany(mappedBy = "project")
    private Set<ProjectFile> projectFiles = new HashSet<>();
    @OneToMany(mappedBy = "project")
    private Set<Milestone> milestones = new HashSet<>();

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
    
    public Set<Milestone> getMilestones() {
		return milestones;
	}

	public void setMilestones(Set<Milestone> milestones) {
		this.milestones = milestones;
	}

	public void setTasks(Set<Task> tasks) {
		this.tasks = tasks;
	}
	
	public long getTimeLeft() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date endDate = null;
        Date aktuelDate = null;
		try {
			endDate = format.parse(deadline);
			aktuelDate =  format.parse(format.format(new Date()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        long diffEnd = aktuelDate.getTime() - endDate.getTime();   

        long timeLeft = TimeUnit.DAYS.convert(diffEnd, TimeUnit.MILLISECONDS)*-1;
        System.out.println("Deadline: " +deadline);
        System.out.println("actual: " + aktuelDate.toString());
        System.out.println("end: " + endDate.toString());
        System.out.println("diff" + TimeUnit.DAYS.convert(diffEnd, TimeUnit.MILLISECONDS));

		return timeLeft;
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
