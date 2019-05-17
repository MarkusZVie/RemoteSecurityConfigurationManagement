package at.ac.univie.rscm.model;

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
	
	@Override
	public String toString() {
		
		StringBuilder sb = new StringBuilder();
		sb.append("ID    : " + applicantId + "\n");
		sb.append("Name  : " + applicantName + "\n");
		sb.append("Email : " + applicantEmail + "\n");
		sb.append("Passwd: " +applicantPassword + "\n");
		sb.append("Roles : ");
		for(Role r : roles) {
			sb.append("[" + r.getRoleId() +"," + r.getRoleName()+"]");
		}
		return sb.toString();
	}

	public void addRole(Role role) {
		if(roles==null) {
			roles = new HashSet<Role>();
		}
		roles.add(role);
		
	}
}
