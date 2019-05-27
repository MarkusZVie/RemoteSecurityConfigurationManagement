package at.ac.univie.rscm.spring.api.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import at.ac.univie.rscm.model.User;
import at.ac.univie.rscm.model.Environment;
import at.ac.univie.rscm.model.Role;
import at.ac.univie.rscm.model.Scriptexecution;

public interface UserRepository extends JpaRepository<User, Integer>{

	User findByUserName(String userName);
	
	@Query(value = "SELECT user_id FROM rscmdatabase.users WHERE user_lastname LIKE :userLastname AND user_firstname LIKE :userFirstname" , nativeQuery = true)
	Collection<Integer> findByUserFullName(@Param("userFirstname") String userFirstname, @Param("userLastname") String userLastname);

	@Query(value = "SELECT * FROM rscmdatabase.users WHERE user_name LIKE %:substring%" , nativeQuery = true)
	List<User> findByContainsInName(@Param("substring") String substring);
	

	
	@Query(value= "SELECT rscmdatabase.users.user_id, rscmdatabase.users.user_firstname, rscmdatabase.users.user_lastname , rscmdatabase.users.user_name, rscmdatabase.users.user_password, rscmdatabase.users.user_email "
			+ "FROM rscmdatabase.users INNER JOIN has_user_job ON rscmdatabase.users.user_id = rscmdatabase.has_user_job.user_id "
			+ "WHERE rscmdatabase.has_user_job.job_id = :id", nativeQuery = true)
	List<User> findByJobId(@Param("id") int id);
	
	@Query(value= "SELECT rscmdatabase.users.user_id, rscmdatabase.users.user_firstname, rscmdatabase.users.user_lastname , rscmdatabase.users.user_name, rscmdatabase.users.user_password, rscmdatabase.users.user_email "
			+ "FROM rscmdatabase.users INNER JOIN has_user_task ON rscmdatabase.users.user_id = rscmdatabase.has_user_task.user_id "
			+ "WHERE rscmdatabase.has_user_task.task_id = :id", nativeQuery = true)
	List<User> findByTaskId(@Param("id") int id);
	
	
	
	@Query(value= "SELECT rscmdatabase.users.user_id, rscmdatabase.users.user_firstname, rscmdatabase.users.user_lastname , rscmdatabase.users.user_name, rscmdatabase.users.user_password, rscmdatabase.users.user_email "
			+ "FROM rscmdatabase.users INNER JOIN has_usergroup_user ON rscmdatabase.users.user_id = rscmdatabase.has_usergroup_user.user_id "
			+ "WHERE rscmdatabase.has_usergroup_user.usergroup_id = :id", nativeQuery = true)
	List<User> findByUsergroupId(@Param("id") int id);
	
	@Query(value= "SELECT rscmdatabase.users.user_id, rscmdatabase.users.user_firstname, rscmdatabase.users.user_lastname , rscmdatabase.users.user_name, rscmdatabase.users.user_password, rscmdatabase.users.user_email "
			+ "FROM rscmdatabase.users INNER JOIN has_role_user ON rscmdatabase.users.user_id = rscmdatabase.has_role_user.user_id "
			+ "WHERE rscmdatabase.has_role_user.role_id = :id", nativeQuery = true)
	List<User> findByRoleId(@Param("id") int id);

	
}
