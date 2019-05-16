package at.ac.univie.rscm.model;

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
	private Set<Applicant> applicants = new HashSet<>();

}
