package at.ac.univie.rscm.model;

import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.ws.rs.ConstrainedTo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "applicants")
public class Applicant {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int applicantId;
	private String applicantName;
	private String applicantPassword;
	private String applicantEmail;
	private String applicantFirstname;
	private String applicantLastname;
	
	
	@ManyToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinTable(name="hasRoleApplicant", joinColumns=@JoinColumn(name="applicantId"), inverseJoinColumns=@JoinColumn(name="roleId"))
	private Set<Role> roles;
	@ManyToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinTable(name="hasApplicantgroupApplicant", joinColumns=@JoinColumn(name="applicantId"), inverseJoinColumns=@JoinColumn(name="applicantgroupId"))
	private Set<Applicantgroup> applicantgroups;
	@ManyToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinTable(name="hasApplicantJob", joinColumns=@JoinColumn(name="applicantId"), inverseJoinColumns=@JoinColumn(name="jobId"))
	private Set<Job> jobs;
	@ManyToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinTable(name="hasApplicantRscmclient", joinColumns=@JoinColumn(name="applicantId"), inverseJoinColumns=@JoinColumn(name="rscmclientId"))
	private Set<RSCMClient> rscmclients;
	@ManyToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinTable(name="hasApplicantTask", joinColumns=@JoinColumn(name="applicantId"), inverseJoinColumns=@JoinColumn(name="taskId"))
	private Set<Task> tasks; 
	


	public void addRole(Role role) {
		if(roles==null) {
			roles = new HashSet<Role>();
		}
		roles.add(role);
		
	}
	



	@Override
	public String toString() {
		int i;
		StringBuilder sb = new StringBuilder();
		sb.append("Class: " + this.getClass().getName() + "<br/>");
		sb.append("applicantId: " +applicantId + "<br/>");
		sb.append("applicantName: " + applicantName + "<br/>");
		sb.append("applicantEmail: " + applicantEmail + "<br/>");
		sb.append("applicantFirstname: " + applicantFirstname + "<br/>");
		sb.append("applicantLastname: " + applicantLastname + "<br/>");
		
		sb.append("applicantgroups: [");
		i =0;
		for(Applicantgroup a:applicantgroups) {
			sb.append(a.getApplicantgroupName());
			if(++i<applicantgroups.size()) {
				sb.append(",");
			}
				
		}
		sb.append("] <br/>");
		
		sb.append("jobs: [");
		i =0;
		for(Job j:jobs) {
			sb.append(j.getJobName());
			if(++i<jobs.size()) {
				sb.append(",");
			}
				
		}
		sb.append("] <br/>");
		
		sb.append("rscmclients: [");
		i =0;
		for(RSCMClient r:rscmclients) {
			sb.append(r.getRscmclientId() + " " + r.getClientPort() + " " + new SimpleDateFormat("dd-MM-yyyy").format(r.getCreatedOn()));
			if(++i<rscmclients.size()) {
				sb.append(",");
			}
				
		}
		sb.append("] <br/>");
		
		sb.append("ammount of tasks: " + tasks.size() + "<br/>");
		
		return sb.toString();
		}
}
