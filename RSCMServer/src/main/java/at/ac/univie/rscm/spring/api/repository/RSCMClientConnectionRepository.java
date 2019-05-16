package at.ac.univie.rscm.spring.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import at.ac.univie.rscm.model.RSCMClientConnection;

public interface RSCMClientConnectionRepository extends JpaRepository<RSCMClientConnection, Integer>{

}
