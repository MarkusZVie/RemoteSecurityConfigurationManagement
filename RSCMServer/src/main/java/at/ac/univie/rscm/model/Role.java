package at.ac.univie.rscm.model;

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

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "roles")
public class Role {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int roleId;
	private String roleName;
	private String roleDescription;
	@ManyToMany(mappedBy = "roles", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Set<Applicant> applicants = new HashSet<>();
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Class: " + this.getClass().getName() + "<br/>");
		sb.append("roleId: " + roleId + "<br/>");
		sb.append("roleName: " + roleName + "<br/>");
		sb.append("roleDescription: " + roleDescription + "<br/>");
		
		if(applicants!=null) {
			sb.append("applicants number: " + applicants.size() + "<br/>");
		}else {
			sb.append("applicants: null<br/>");
		}
		
		return sb.toString();
	}
	
}

