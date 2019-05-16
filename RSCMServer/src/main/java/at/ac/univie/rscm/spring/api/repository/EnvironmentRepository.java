package at.ac.univie.rscm.spring.api.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import at.ac.univie.rscm.model.Environment;

public interface EnvironmentRepository extends JpaRepository<Environment, Integer>{


	Collection<Environment> findAllByIpRangeBegin(String ipRangeBegin);
	
	@Query(value = "SELECT * FROM rscmdatabase.environments WHERE ip_range_begin LIKE %:substring%" , nativeQuery = true)
	List<Environment> findByContainsInIp(@Param("substring") String substring);
	
}
