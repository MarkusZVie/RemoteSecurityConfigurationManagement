package at.ac.univie.rscm.spring.api.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import at.ac.univie.rscm.model.Task;

public interface TaskReposiotry extends JpaRepository<Task, Integer>{

Collection<Task> findAllByTaskName(String taskName);
	
	@Query(value = "SELECT * FROM rscmdatabase.tasks WHERE task_name LIKE %:substring%" , nativeQuery = true)
	List<Task> findByContainsInName(@Param("substring") String substring);
}
