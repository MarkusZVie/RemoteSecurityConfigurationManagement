package at.ac.univie.rscm.spring.api.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import at.ac.univie.rscm.model.Usergroup;

public interface UsergroupRepository extends JpaRepository<Usergroup, Integer>{

	Collection<Usergroup> findAllByUsergroupName(String groupName);
	

	@Query(value = "SELECT * FROM rscmdatabase.usergroups WHERE usergroup_name LIKE %:substring%" , nativeQuery = true)
	List<Usergroup> findByContainsInName(@Param("substring") String substring);
	
	
}
