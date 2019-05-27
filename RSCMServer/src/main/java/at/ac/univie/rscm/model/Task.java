package at.ac.univie.rscm.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tasks")
public class Task {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int taskId;
	private String taskName;
	private Date taskCreationdate;
	private Date taskPlanBegindate;
	private Date taskPlanEnddate;
	private Date taskActualBegindate;
	private Date taskActualEnddate;
	private String taskDescription;
	private String taskOutcome;
	@ManyToMany(mappedBy = "tasks", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Set<User> users = new HashSet<>();

	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Class: " + this.getClass().getName() + "<br/>");
		sb.append("taskId: " + taskId + "<br/>");
		sb.append("taskName: " + taskName + "<br/>");
		if(taskCreationdate!=null) {
			sb.append("taskCreationdate: " + new SimpleDateFormat("dd-MM-yyyy HH:mm").format(taskCreationdate) + "<br/>");
		}else {
			sb.append("taskCreationdate: null<br/>");
		}
		if(taskPlanBegindate!=null) {
			sb.append("taskPlanBegindate: " + new SimpleDateFormat("dd-MM-yyyy HH:mm").format(taskPlanBegindate) + "<br/>");
		}else {
			sb.append("taskPlanBegindate: null<br/>");
		}
		if(taskPlanEnddate!=null) {
			sb.append("taskPlanEnddate: " + new SimpleDateFormat("dd-MM-yyyy HH:mm").format(taskPlanEnddate) + "<br/>");
		}else {
			sb.append("taskPlanEnddate: null<br/>");
		}
		if(taskActualBegindate!=null) {
			sb.append("taskActualBegindate: " + new SimpleDateFormat("dd-MM-yyyy HH:mm").format(taskActualBegindate) + "<br/>");
		}else {
			sb.append("taskActualBegindate: null<br/>");
		}
		if(taskActualEnddate!=null) {
			sb.append("taskActualEnddate: " + new SimpleDateFormat("dd-MM-yyyy HH:mm").format(taskActualEnddate) + "<br/>");
		}else {
			sb.append("taskActualEnddate: null<br/>");
		}
		sb.append("taskDescription: " + taskDescription + "<br/>");
		sb.append("taskOutcome: " + taskOutcome + "<br/>");
		if(users!=null) {
			sb.append("users: " +users.size() + "<br/>");
		}else {
			sb.append("users: null<br/>");
		}
		
		return sb.toString();
	}
	
}
