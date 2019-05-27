package at.ac.univie.rscm.model;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.ws.rs.ConstrainedTo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int userId;
	private String userName;
	private String userPassword;
	private String userEmail;
	private String userFirstname;
	private String userLastname;
	
	
	@ManyToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinTable(name="hasRoleUser", joinColumns=@JoinColumn(name="userId"), inverseJoinColumns=@JoinColumn(name="roleId"))
	private Set<Role> roles;
	@ManyToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinTable(name="hasUsergroupUser", joinColumns=@JoinColumn(name="userId"), inverseJoinColumns=@JoinColumn(name="usergroupId"))
	private Set<Usergroup> usergroups;
	@ManyToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinTable(name="hasUserJob", joinColumns=@JoinColumn(name="userId"), inverseJoinColumns=@JoinColumn(name="jobId"))
	private Set<Job> jobs;
	@ManyToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinTable(name="hasUserRscmclient", joinColumns=@JoinColumn(name="userId"), inverseJoinColumns=@JoinColumn(name="rscmclientId"))
	private Set<RSCMClient> rscmclients;
	@ManyToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinTable(name="hasUserTask", joinColumns=@JoinColumn(name="userId"), inverseJoinColumns=@JoinColumn(name="taskId"))
	private Set<Task> tasks; 
	


	public void addRole(Role role) {
		if(roles==null) {
			roles = new HashSet<Role>();
		}
		roles.add(role);
		
	}
	
	public void addRSCMClient(RSCMClient rscmClient) {
		if(rscmClient==null) {
			rscmclients = new HashSet<RSCMClient>();
		}
		rscmclients.add(rscmClient);
		
	}
	



	@Override
	public String toString() {
		int i;
		StringBuilder sb = new StringBuilder();
		sb.append("Class: " + this.getClass().getName() + "<br/>");
		sb.append("userId: " +userId + "<br/>");
		sb.append("userName: " + userName + "<br/>");
		sb.append("userEmail: " + userEmail + "<br/>");
		sb.append("userFirstname: " + userFirstname + "<br/>");
		sb.append("userLastname: " + userLastname + "<br/>");
		
		sb.append("usergroups: [");
		i =0;
		for(Usergroup a:usergroups) {
			sb.append(a.getUsergroupName());
			if(++i<usergroups.size()) {
				sb.append(",");
			}
				
		}
		sb.append("] <br/>");
		
		sb.append("jobs: [");
		i =0;
		for(Job j:jobs) {
			sb.append(j.getJobName());
			if(++i<jobs.size()) {
				sb.append(",");
			}
				
		}
		sb.append("] <br/>");
		
		sb.append("rscmclients: [");
		i =0;
		for(RSCMClient r:rscmclients) {
			sb.append(r.getRscmclientId() + " " + r.getClientPort() + " " + new SimpleDateFormat("dd-MM-yyyy").format(r.getCreatedOn()));
			if(++i<rscmclients.size()) {
				sb.append(",");
			}
				
		}
		sb.append("] <br/>");
		
		sb.append("ammount of tasks: " + tasks.size() + "<br/>");
		
		return sb.toString();
		}

	public void deletejob(int id) {
		for(Job j : jobs) {
			if(j.getJobId()==id) {
				jobs.remove(j);
			}
		}
		
	}

	public void deleteGroup(int id) {
		for(Usergroup g : usergroups) {
			if(g.getUsergroupId()==id) {
				usergroups.remove(g);
			}
		}
		
	}

	public void deleteRole(int id) {
		Role targetR = null;
		for(Role r : roles) {
			if(r.getRoleId() == id) {
				targetR = r;
			}
			
		}
		roles.remove(targetR);
		
	}

	public void deleteTask(int id) {
		Task targetT = null;
		for(Task t : tasks) {
			if(t.getTaskId() == id) {
				targetT = t;
			}
			
		}
		tasks.remove(targetT);
		
	}
}
