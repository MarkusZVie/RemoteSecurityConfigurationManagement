package at.ac.univie.rscm.spring.api.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import at.ac.univie.rscm.model.Applicant;
import at.ac.univie.rscm.model.Environment;
import at.ac.univie.rscm.model.Role;
import at.ac.univie.rscm.model.Scriptexecution;

public interface ApplicantRepository extends JpaRepository<Applicant, Integer>{

	Applicant findByApplicantName(String applicantName);
	
	@Query(value = "SELECT applicant_id FROM rscmdatabase.applicants WHERE applicant_lastname LIKE :applicantLastname AND applicant_firstname LIKE :applicantFirstname" , nativeQuery = true)
	Collection<Integer> findByApplicantFullName(@Param("applicantFirstname") String applicantFirstname, @Param("applicantLastname") String applicantLastname);

	@Query(value = "SELECT * FROM rscmdatabase.applicants WHERE applicant_name LIKE %:substring%" , nativeQuery = true)
	List<Applicant> findByContainsInName(@Param("substring") String substring);
	

	
	@Query(value= "SELECT rscmdatabase.applicants.applicant_id, rscmdatabase.applicants.applicant_firstname, rscmdatabase.applicants.applicant_lastname , rscmdatabase.applicants.applicant_name, rscmdatabase.applicants.applicant_password, rscmdatabase.applicants.applicant_email "
			+ "FROM rscmdatabase.applicants INNER JOIN has_applicant_job ON rscmdatabase.applicants.applicant_id = rscmdatabase.has_applicant_job.applicant_id "
			+ "WHERE rscmdatabase.has_applicant_job.job_id = :id", nativeQuery = true)
	List<Applicant> findByJobId(@Param("id") int id);
	
	@Query(value= "SELECT rscmdatabase.applicants.applicant_id, rscmdatabase.applicants.applicant_firstname, rscmdatabase.applicants.applicant_lastname , rscmdatabase.applicants.applicant_name, rscmdatabase.applicants.applicant_password, rscmdatabase.applicants.applicant_email "
			+ "FROM rscmdatabase.applicants INNER JOIN has_applicant_task ON rscmdatabase.applicants.applicant_id = rscmdatabase.has_applicant_task.applicant_id "
			+ "WHERE rscmdatabase.has_applicant_task.task_id = :id", nativeQuery = true)
	List<Applicant> findByTaskId(@Param("id") int id);
	
	
	
	@Query(value= "SELECT rscmdatabase.applicants.applicant_id, rscmdatabase.applicants.applicant_firstname, rscmdatabase.applicants.applicant_lastname , rscmdatabase.applicants.applicant_name, rscmdatabase.applicants.applicant_password, rscmdatabase.applicants.applicant_email "
			+ "FROM rscmdatabase.applicants INNER JOIN has_applicantgroup_applicant ON rscmdatabase.applicants.applicant_id = rscmdatabase.has_applicantgroup_applicant.applicant_id "
			+ "WHERE rscmdatabase.has_applicantgroup_applicant.applicantgroup_id = :id", nativeQuery = true)
	List<Applicant> findByApplicantgroupId(@Param("id") int id);
	
	@Query(value= "SELECT rscmdatabase.applicants.applicant_id, rscmdatabase.applicants.applicant_firstname, rscmdatabase.applicants.applicant_lastname , rscmdatabase.applicants.applicant_name, rscmdatabase.applicants.applicant_password, rscmdatabase.applicants.applicant_email "
			+ "FROM rscmdatabase.applicants INNER JOIN has_role_applicant ON rscmdatabase.applicants.applicant_id = rscmdatabase.has_role_applicant.applicant_id "
			+ "WHERE rscmdatabase.has_role_applicant.role_id = :id", nativeQuery = true)
	List<Applicant> findByRoleId(@Param("id") int id);

	
}
