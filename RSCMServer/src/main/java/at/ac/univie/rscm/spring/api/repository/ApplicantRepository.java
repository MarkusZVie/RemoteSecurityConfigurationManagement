package at.ac.univie.rscm.spring.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import at.ac.univie.rscm.model.Applicant;

public interface ApplicantRepository extends JpaRepository<Applicant, Integer>{

	Applicant findByApplicantName(String applicantName);

}
