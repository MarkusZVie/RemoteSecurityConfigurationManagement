package at.ac.univie.rscm.spring.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import at.ac.univie.rscm.model.Role;



public interface RoleRepository extends JpaRepository<Role, Integer> {

}
