package at.ac.univie.rscm.model;

import java.util.List;
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

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "environments")
public class Environment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int environmentId;
	private String ipRangeBegin;
	private String ipRangeEnd;
	private String environmentDescription;
	@ManyToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinTable(name="hasEnvironmentEnvironmentthreat", joinColumns=@JoinColumn(name="environmentId"), inverseJoinColumns=@JoinColumn(name="environmentthreatId"))
	private Set<Environmentthreat> environmentthreats;
	//@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany(mappedBy="environment",cascade=CascadeType.ALL, orphanRemoval = true)
	private List<RSCMClientConnection> rSCMClientConnections;
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Class: " + this.getClass().getName() + "<br/>");
		sb.append("environmentId: " + environmentId + "<br/>");
		sb.append("ipRangeBegin: " + ipRangeBegin + "<br/>");
		sb.append("ipRangeEnd: " + ipRangeEnd + "<br/>");
		sb.append("environmentDescription: " + environmentDescription + "<br/>");
		if(rSCMClientConnections!=null) {
			sb.append("rSCMClientConnections number: " + rSCMClientConnections.size() + "<br/>");
			//sb.append("rSCMClientConnections number: " +"asdkjafdhbjfaebhjkaf" + "<br/>");
		}else {
			sb.append("rSCMClientConnections: null<br/>");
		}
		
		if(environmentthreats!=null) {
			sb.append("environmentthreats: [");
			for(Environmentthreat et : environmentthreats) {
				sb.append(et.getThreatTitle() +", ");
			}
			
			return sb.toString().substring(0, sb.toString().length()-1)+"]<br/>";
		}else {
			sb.append("environmentthreats: [] <br/>");
			return sb.toString();
		}
		
	}
	
}
