package at.ac.univie.rscm.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "applicants")
public class Applicant {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int applicantId;
	private String applicantName;
	private String applicantPassword;
	private String applicantEmail;
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinTable(name="hasRoleApplicant", joinColumns=@JoinColumn(name="applicantId"), inverseJoinColumns=@JoinColumn(name="roleId"))
	private Set<Role> roles;
	
	
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
}
