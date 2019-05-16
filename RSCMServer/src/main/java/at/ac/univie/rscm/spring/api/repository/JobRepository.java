package at.ac.univie.rscm.spring.api.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import at.ac.univie.rscm.model.Job;

public interface JobRepository extends JpaRepository<Job, Integer>{

	Collection<Job> findAllByJobName(String jobName);
	
	@Query(value = "SELECT * FROM rscmdatabase.jobs WHERE job_name LIKE %:substring%" , nativeQuery = true)
	List<Job> findByContainsInName(@Param("substring") String substring);
	
}
