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
@Table(name = "applicantgroups")
public class Applicantgroup {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int applicantgroupId;
	private String applicantgroupName;
	private String applicantgroupDescription;
	@ManyToMany(mappedBy = "applicantgroups", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Set<Applicant> applicants = new HashSet<>();
}
