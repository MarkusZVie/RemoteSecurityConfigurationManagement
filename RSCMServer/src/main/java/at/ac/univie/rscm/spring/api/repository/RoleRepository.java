package at.ac.univie.rscm.spring.api.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import at.ac.univie.rscm.model.Role;



public interface RoleRepository extends JpaRepository<Role, Integer> {
	Collection<Role> findAllByRoleName(String roleName);
	

	@Query(value = "SELECT * FROM rscmdatabase.roles WHERE role_name LIKE %:substring%" , nativeQuery = true)
	List<Role> findByContainsInName(@Param("substring") String substring);
}
