package at.ac.univie.rscm.spring.api.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import at.ac.univie.rscm.model.Applicant;
import at.ac.univie.rscm.model.Role;

public interface ApplicantRepository extends JpaRepository<Applicant, Integer>{

	Applicant findByApplicantName(String applicantName);
	
	@Query(value = "SELECT applicant_id FROM rscmdatabase.applicants WHERE applicant_lastname LIKE :applicantLastname AND applicant_firstname LIKE :applicantFirstname" , nativeQuery = true)
	Collection<Integer> findByApplicantFullName(@Param("applicantFirstname") String applicantFirstname, @Param("applicantLastname") String applicantLastname);

	@Query(value = "SELECT * FROM rscmdatabase.applicants WHERE applicant_name LIKE %:substring%" , nativeQuery = true)
	List<Applicant> findByContainsInName(@Param("substring") String substring);
	
	
}
