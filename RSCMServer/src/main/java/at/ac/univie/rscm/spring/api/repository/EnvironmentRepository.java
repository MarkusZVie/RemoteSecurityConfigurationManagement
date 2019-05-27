package at.ac.univie.rscm.spring.api.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import at.ac.univie.rscm.model.Environment;
import at.ac.univie.rscm.model.Environmentthreat;

public interface EnvironmentRepository extends JpaRepository<Environment, Integer>{


	Collection<Environment> findAllByIpRangeBegin(String ipRangeBegin);
	
	@Query(value = "SELECT * FROM rscmdatabase.environments WHERE ip_range_begin LIKE %:substring%" , nativeQuery = true)
	List<Environment> findByContainsInIp(@Param("substring") String substring);
	
	@Query(value = "SELECT * FROM rscmdatabase.environments WHERE ip_range_begin LIKE :ipRangeBegin AND ip_range_end LIKE :ipRangeEnd" , nativeQuery = true)
	List<Environment> findAllByIpRangeBeginAndEnd(String ipRangeBegin, String ipRangeEnd);

	@Query(value = "SELECT * FROM rscmdatabase.environments WHERE ip_range_begin LIKE :ipRangeBegin AND ip_range_end LIKE :ipRangeEnd AND environment_description LIKE :environmentDescription" , nativeQuery = true)
	List<Environment> findByIpAndDescriptin(String ipRangeBegin, String ipRangeEnd, String environmentDescription);

	@Query(value= "SELECT rscmdatabase.environments.environment_id, rscmdatabase.environments.ip_range_begin, rscmdatabase.environments.ip_range_end, rscmdatabase.environments.environment_description " + 
			"FROM rscmdatabase.environments INNER JOIN rscmdatabase.has_environment_environmentthreat ON rscmdatabase.has_environment_environmentthreat.environment_id = rscmdatabase.environments.environment_id " + 
			"WHERE rscmdatabase.has_environment_environmentthreat.environmentthreat_id = :id", nativeQuery = true)
	List<Environment> findByEnvironmentthreatId(@Param("id") int id);
	
}
