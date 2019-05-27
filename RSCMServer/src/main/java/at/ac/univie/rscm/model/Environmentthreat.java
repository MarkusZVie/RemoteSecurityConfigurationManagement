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

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "environmentthreats")
public class Environmentthreat {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int environmentthreatId;
	private int threatLevel;
	private String threatTitle;
	private String threatDescription;
	private String expectedProblem;
	@ManyToMany(mappedBy = "environmentthreats" , fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Set<Environment> environment = new HashSet<>();
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Class: " + this.getClass().getName() + "<br/>");
		sb.append("environmentthreatId: " + environmentthreatId + "<br/>");
		sb.append("threatLevel: " + threatLevel + "<br/>");
		sb.append("threatTitle: " + threatTitle + "<br/>");
		sb.append("threatDescription: " + threatDescription + "<br/>");
		sb.append("expectedProblem: " + expectedProblem + "<br/>");
		if(environment!=null) {
			sb.append("environment number: " + environment.size() + "<br/>");
		}else {
			sb.append("environment: null<br/>");
		}
		
		return sb.toString();
	}

	public void deleteEnvironment(int id) {
		Environment toDeleteEnvironment = null;
		for(Environment e : environment) {
			if(e.getEnvironmentId() == id) {
				toDeleteEnvironment=e;
			}
		}
		environment.remove(toDeleteEnvironment);
		
	}
	
}
