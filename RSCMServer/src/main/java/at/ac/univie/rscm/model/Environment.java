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
	@LazyCollection(LazyCollectionOption.TRUE)
	@OneToMany(mappedBy="environment",cascade=CascadeType.ALL, orphanRemoval = true)
	private List<RSCMClientConnection> rSCMClientConnections;
}
