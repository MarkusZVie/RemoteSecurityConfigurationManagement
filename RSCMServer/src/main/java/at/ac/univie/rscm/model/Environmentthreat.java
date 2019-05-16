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
}
