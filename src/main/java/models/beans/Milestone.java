package models.beans;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

@Entity
public class Milestone {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Long mileStoneId;
	@NotNull
	private String name;
	private String deadline;
	private float progress;
	@ManyToOne
	private Project project;
	@OneToMany(mappedBy = "milestone")
	private Set<Task> tasks = new HashSet<>();

	public Long getMileStoneId() {
		return mileStoneId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public int getTasksSize() {
		if(tasks.isEmpty()){
			return 0;
		}else{
			return tasks.size();
		}
	}

	public String getDeadline() {

		Date latestDate = null;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		if (getTasks().isEmpty()) {

			return getProject().getDeadline();

		} else {

			for (Task task : getTasks()) {

				String start = task.getStart();

				// calculating end task
				Calendar c = Calendar.getInstance();
				try {
					c.setTime(format.parse(start));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				c.add(Calendar.DATE, Integer.parseInt(task.getEffort()));
				Date end = c.getTime();

				if (latestDate == null) {
					latestDate = end;
				} else {
					if (latestDate.before(end)) {
						latestDate = end;
					}
				}
			}
		}

		return format.format(latestDate);
	}

	public void setDeadline(String deadline) {
		this.deadline = deadline;
	}

	public String getProgress() {
        double taskValue = 0;
        double counter =0;
        if(tasks.isEmpty()){
        	return "100";
        }
		for(Task task : tasks){
        	double gewichtung;
        	if(!task.getUsers().isEmpty()){
            	 gewichtung = Integer.parseInt(task.getEffort())/task.getUsers().size();

        	}else{
        		gewichtung =  Integer.parseInt(task.getEffort());
        	}
        	
        	counter += gewichtung;
        	
        	taskValue += gewichtung * task.getProgress(); 
        }
        
		NumberFormat n = NumberFormat.getInstance();
		n.setMaximumFractionDigits(2);
		
		double tasksFinished = taskValue/counter;
        System.out.println("deadline Milestone " + n.format(tasksFinished));
		return n.format(tasksFinished);
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public void setProgress(float progress) {
		this.progress = progress;
	}

	public Set<Task> getTasks() {
		return tasks;
	}

	public void setTasks(Set<Task> tasks) {
		this.tasks = tasks;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((mileStoneId == null) ? 0 : mileStoneId.hashCode());
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
		Milestone other = (Milestone) obj;
		if (mileStoneId == null) {
			if (other.mileStoneId != null)
				return false;
		} else if (!mileStoneId.equals(other.mileStoneId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Milestone [mileStoneId=" + mileStoneId + ", name=" + name + ", deadline=" + deadline + ", progress="
				+ progress + ", project=" + project + "]";
	}

}
