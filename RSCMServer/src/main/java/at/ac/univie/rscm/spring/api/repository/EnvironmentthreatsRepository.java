package at.ac.univie.rscm.spring.api.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import at.ac.univie.rscm.model.Applicant;
import at.ac.univie.rscm.model.Environmentthreat;

public interface EnvironmentthreatsRepository extends JpaRepository<Environmentthreat, Integer>{

	Collection<Environmentthreat> findAllByThreatTitle(String threatTitle);
	
	@Query(value = "SELECT * FROM rscmdatabase.environmentthreats WHERE threat_title LIKE %:substring%" , nativeQuery = true)
	List<Environmentthreat> findByContainsInTitle(@Param("substring") String substring);
	
	@Query(value= "SELECT rscmdatabase.environmentthreats.environmentthreat_id, rscmdatabase.environmentthreats.threat_level,rscmdatabase.environmentthreats.threat_title,rscmdatabase.environmentthreats.threat_description,rscmdatabase.environmentthreats.expected_problem " + 
			"FROM rscmdatabase.environmentthreats INNER JOIN rscmdatabase.has_environment_environmentthreat ON rscmdatabase.has_environment_environmentthreat.environmentthreat_id = rscmdatabase.environmentthreats.environmentthreat_id " + 
			"WHERE environment_id = :id", nativeQuery = true)
	List<Environmentthreat> findByEnvironmentId(@Param("id") int id);
	
	
}
