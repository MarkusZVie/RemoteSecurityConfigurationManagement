package at.ac.univie.rscm.spring.api.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import at.ac.univie.rscm.model.Applicantgroup;
import at.ac.univie.rscm.model.Job;
import at.ac.univie.rscm.model.Scriptexecution;

public interface ScriptexecutionRepository extends JpaRepository<Scriptexecution, Integer>{

	Collection<Scriptexecution> findAllByScriptName(String scriptName);
	
	@Query(value = "SELECT * FROM rscmdatabase.scriptexecution WHERE rscmclient_fs = :rscmclientId AND script_name LIKE :scriptName" , nativeQuery = true)
	List<Scriptexecution> findByAllFileAssignByClient(@Param("scriptName") String scriptName,@Param("rscmclientId") int rscmclientId);
	
	@Query(value = "SELECT * FROM rscmdatabase.scriptexecution WHERE script_name LIKE %:substring%" , nativeQuery = true)
	List<Scriptexecution> findByContainsInName(@Param("substring") String substring);
	
	@Query(value = "SELECT * FROM rscmdatabase.scriptexecution WHERE script_name LIKE :scriptName AND scriptexecution_executiondate IS NOT NULL" , nativeQuery = true)
	List<Scriptexecution> findByScriptNameAndExDateNotNull(@Param("scriptName") String scriptName);
	
	@Query(value= "SELECT * FROM rscmdatabase.scriptexecution WHERE rscmclient_fs = :rscmclientId AND scriptexecution_executiondate IS NULL", nativeQuery = true)
	List<Scriptexecution> findAllToExecuteScriptAssigns(@Param("rscmclientId") int rscmclientId);
	
	
	@Query(value= "SELECT * FROM rscmdatabase.scriptexecution WHERE applicantgroup_fs = :id AND script_name LIKE :scriptName", nativeQuery = true)
	List<Scriptexecution> findByApplicantgroup(@Param("id") int id,@Param("scriptName") String scriptName);
	
	@Query(value= "SELECT * FROM rscmdatabase.scriptexecution WHERE rscmclient_fs = :id AND script_name LIKE :scriptName", nativeQuery = true)
	List<Scriptexecution> findByRscmclient(@Param("id") int id,@Param("scriptName") String scriptName);
	
	@Query(value= "SELECT * FROM rscmdatabase.scriptexecution WHERE task_fs = :id AND script_name LIKE :scriptName", nativeQuery = true)
	List<Scriptexecution> findByTasktask(@Param("id") int id,@Param("scriptName") String scriptName);
	
	@Query(value= "SELECT * FROM rscmdatabase.scriptexecution WHERE environment_fs = :id AND script_name LIKE :scriptName", nativeQuery = true)
	List<Scriptexecution> findByEnvironment(@Param("id") int id,@Param("scriptName") String scriptName);
	
	@Query(value= "SELECT * FROM rscmdatabase.scriptexecution WHERE environmentthreat_fs = :id AND script_name LIKE :scriptName", nativeQuery = true)
	List<Scriptexecution> findByEnvironmentthreat(@Param("id") int id,@Param("scriptName") String scriptName);
	
	@Query(value= "SELECT * FROM rscmdatabase.scriptexecution WHERE job_fs = :id AND script_name LIKE :scriptName", nativeQuery = true)
	List<Scriptexecution> findByJob(@Param("id") int id,@Param("scriptName") String scriptName);
	
	@Query(value= "SELECT * FROM rscmdatabase.scriptexecution WHERE role_fs = :id AND script_name LIKE :scriptName", nativeQuery = true)
	List<Scriptexecution> findByRole(@Param("id") int id,@Param("scriptName") String scriptName);
	
	@Query(value= "SELECT * FROM rscmdatabase.scriptexecution WHERE applicant_fs = :id AND script_name LIKE :scriptName", nativeQuery = true)
	List<Scriptexecution> findByApplicant(@Param("id") int id,@Param("scriptName") String scriptName);
	


	
	
	@Query(value= "SELECT count(*) FROM rscmdatabase.scriptexecution WHERE applicantgroup_fs = :id AND script_name LIKE :scriptName AND scriptexecution_executiondate IS NOT NULL", nativeQuery = true)
	int countByApplicantgroup(@Param("id") int id,@Param("scriptName") String scriptName);
	
	@Query(value= "SELECT count(*) FROM rscmdatabase.scriptexecution WHERE rscmclient_fs = :id AND script_name LIKE :scriptName AND scriptexecution_executiondate IS NOT NULL", nativeQuery = true)
	int countByRscmclient(@Param("id") int id,@Param("scriptName") String scriptName);
	
	@Query(value= "SELECT count(*) FROM rscmdatabase.scriptexecution WHERE task_fs = :id AND script_name LIKE :scriptName AND scriptexecution_executiondate IS NOT NULL", nativeQuery = true)
	int countByTasktask(@Param("id") int id,@Param("scriptName") String scriptName);
	
	@Query(value= "SELECT count(*) FROM rscmdatabase.scriptexecution WHERE environment_fs = :id AND script_name LIKE :scriptName AND scriptexecution_executiondate IS NOT NULL", nativeQuery = true)
	int countByEnvironment(@Param("id") int id,@Param("scriptName") String scriptName);
	
	@Query(value= "SELECT count(*) FROM rscmdatabase.scriptexecution WHERE environmentthreat_fs = :id AND script_name LIKE :scriptName AND scriptexecution_executiondate IS NOT NULL", nativeQuery = true)
	int countByEnvironmentthreat(@Param("id") int id,@Param("scriptName") String scriptName);
	
	@Query(value= "SELECT count(*) FROM rscmdatabase.scriptexecution WHERE job_fs = :id AND script_name LIKE :scriptName AND scriptexecution_executiondate IS NOT NULL", nativeQuery = true)
	int countByJob(@Param("id") int id,@Param("scriptName") String scriptName);
	
	@Query(value= "SELECT count(*) FROM rscmdatabase.scriptexecution WHERE role_fs = :id AND script_name LIKE :scriptName AND scriptexecution_executiondate IS NOT NULL", nativeQuery = true)
	int countByRole(@Param("id") int id,@Param("scriptName") String scriptName);
	
	@Query(value= "SELECT count(*) FROM rscmdatabase.scriptexecution WHERE applicant_fs = :id AND script_name LIKE :scriptName AND scriptexecution_executiondate IS NOT NULL", nativeQuery = true)
	int countByApplicant(@Param("id") int id,@Param("scriptName") String scriptName);
	
	
	
	@Query(value= "SELECT * FROM rscmdatabase.scriptexecution WHERE script_name LIKE :scriptName AND rscmclient_fs = :rscmclientId AND task_fs =:entityId AND scriptexecution_executiondate IS NULL", nativeQuery = true)
	List<Scriptexecution> getExsistingExecutionPlanBasedOnTask(@Param("scriptName") String scriptName,@Param("rscmclientId") int rscmclientId, @Param("entityId") int entityId);
	
	@Query(value= "SELECT * FROM rscmdatabase.scriptexecution WHERE script_name LIKE :scriptName AND environment_fs =:entityId AND scriptexecution_executiondate IS NULL", nativeQuery = true)
	List<Scriptexecution> getExsistingExecutionPlanBasedOnEnvironment(@Param("scriptName") String scriptName, @Param("entityId") int entityId);
	
	@Query(value= "SELECT * FROM rscmdatabase.scriptexecution WHERE script_name LIKE :scriptName AND environmentthreat_fs =:entityId AND scriptexecution_executiondate IS NULL", nativeQuery = true)
	List<Scriptexecution> getExsistingExecutionPlanBasedOnEnvironmentthreat(@Param("scriptName") String scriptName,@Param("entityId") int entityId);
	
	@Query(value= "SELECT * FROM rscmdatabase.scriptexecution WHERE script_name LIKE :scriptName AND rscmclient_fs = :rscmclientId AND job_fs =:entityId AND scriptexecution_executiondate IS NULL", nativeQuery = true)
	List<Scriptexecution> getExsistingExecutionPlanBasedOnJob(@Param("scriptName") String scriptName,@Param("rscmclientId") int rscmclientId, @Param("entityId") int entityId);
	
	@Query(value= "SELECT * FROM rscmdatabase.scriptexecution WHERE script_name LIKE :scriptName AND rscmclient_fs = :rscmclientId AND applicantgroup_fs =:entityId AND scriptexecution_executiondate IS NULL", nativeQuery = true)
	List<Scriptexecution> getExsistingExecutionPlanBasedOnApplicantgroup(@Param("scriptName") String scriptName,@Param("rscmclientId") int rscmclientId, @Param("entityId") int entityId);
	
	@Query(value= "SELECT * FROM rscmdatabase.scriptexecution WHERE script_name LIKE :scriptName AND rscmclient_fs = :rscmclientId AND applicant_fs =:entityId AND scriptexecution_executiondate IS NULL", nativeQuery = true)
	List<Scriptexecution> getExsistingExecutionPlanBasedOnApplicant(@Param("scriptName") String scriptName,@Param("rscmclientId") int rscmclientId, @Param("entityId") int entityId);
	
	@Query(value= "SELECT * FROM rscmdatabase.scriptexecution WHERE script_name LIKE :scriptName AND rscmclient_fs = :rscmclientId AND role_fs =:entityId AND scriptexecution_executiondate IS NULL", nativeQuery = true)
	List<Scriptexecution> getExsistingExecutionPlanBasedOnRole(@Param("scriptName") String scriptName,@Param("rscmclientId") int rscmclientId, @Param("entityId") int entityId);
	
	
	
	@Query(value= "SELECT * FROM rscmdatabase.scriptexecution WHERE script_name LIKE :scriptName AND task_fs = :entityId AND scriptexecution_executiondate IS NULL", nativeQuery = true)
	List<Scriptexecution> getAssignedExecutionBasedOnTask(@Param("scriptName") String scriptName, @Param("entityId") int entityId);

	@Query(value= "SELECT * FROM rscmdatabase.scriptexecution WHERE script_name LIKE :scriptName AND environment_fs = :entityId AND scriptexecution_executiondate IS NULL", nativeQuery = true)
	List<Scriptexecution> getAssignedExecutionBasedOnEnvironment(@Param("scriptName") String scriptName, @Param("entityId") int entityId);

	@Query(value= "SELECT * FROM rscmdatabase.scriptexecution WHERE script_name LIKE :scriptName AND environmentthreat_fs = :entityId AND scriptexecution_executiondate IS NULL", nativeQuery = true)
	List<Scriptexecution> getAssignedExecutionBasedOnEnvironmentthreat(@Param("scriptName") String scriptName, @Param("entityId") int entityId);
	
	@Query(value= "SELECT * FROM rscmdatabase.scriptexecution WHERE script_name LIKE :scriptName AND job_fs = :entityId AND scriptexecution_executiondate IS NULL", nativeQuery = true)
	List<Scriptexecution> getAssignedExecutionBasedOnJob(@Param("scriptName") String scriptName, @Param("entityId") int entityId);
	
	@Query(value= "SELECT * FROM rscmdatabase.scriptexecution WHERE script_name LIKE :scriptName AND applicantgroup_fs = :entityId AND scriptexecution_executiondate IS NULL", nativeQuery = true)
	List<Scriptexecution> getAssignedExecutionBasedOnApplicantgroup(@Param("scriptName") String scriptName, @Param("entityId") int entityId);
	
	@Query(value= "SELECT * FROM rscmdatabase.scriptexecution WHERE script_name LIKE :scriptName AND applicant_fs = :entityId AND scriptexecution_executiondate IS NULL", nativeQuery = true)
	List<Scriptexecution> getAssignedExecutionBasedOnApplicant(@Param("scriptName") String scriptName, @Param("entityId") int entityId);
	
	@Query(value= "SELECT * FROM rscmdatabase.scriptexecution WHERE script_name LIKE :scriptName AND role_fs = :entityId AND scriptexecution_executiondate IS NULL", nativeQuery = true)
	List<Scriptexecution> getAssignedExecutionBasedOnRole(@Param("scriptName") String scriptName, @Param("entityId") int entityId);

	@Query(value= "SELECT * FROM rscmdatabase.scriptexecution WHERE scriptexecution_executiondate IS NULL AND (environment_fs IS NOT NULL OR environmentthreat_fs IS NOT NULL)", nativeQuery = true)
	List<Scriptexecution> findAllToExecuteScriptEnvironment();
	
	
}
