package at.ac.univie.rscm.spring.api.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import at.ac.univie.rscm.model.Applicantgroup;

public interface ApplicantgroupRepository extends JpaRepository<Applicantgroup, Integer>{

	Collection<Applicantgroup> findAllByApplicantgroupName(String groupName);
	

	@Query(value = "SELECT * FROM rscmdatabase.applicantgroups WHERE applicantgroup_name LIKE %:substring%" , nativeQuery = true)
	List<Applicantgroup> findByContainsInName(@Param("substring") String substring);
}
