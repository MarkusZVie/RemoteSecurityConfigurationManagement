package at.ac.univie.rscm.spring.api.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import at.ac.univie.rscm.model.RSCMClient;

@Component("rscmClientRepositoryBean")
public interface RSCMClientRepository extends JpaRepository<RSCMClient, Integer>{

	@Query(value = "SELECT max(client_port) FROM rscmdatabase.clientkeys" , nativeQuery = true)
	Collection<Integer> getHighestPort();
}