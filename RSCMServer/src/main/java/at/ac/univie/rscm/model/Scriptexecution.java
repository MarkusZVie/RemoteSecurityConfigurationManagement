package at.ac.univie.rscm.model;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "scriptexecution")
public class Scriptexecution {

	@Getter
	@Setter
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int scriptexecutionId;
	@Getter
	@Setter
	private Date scriptexecutionAssigneddate;
	@Getter
	@Setter
	private Date scriptexecutionExecutiondate;
	@Getter
	@Setter
	private String scriptName;
	
	@Getter
	@Setter
	@ManyToOne
    @JoinColumn(name="rscmclient_fs", nullable=false)
	private RSCMClient rSCMClient;
	
	@Getter
	@Setter
	@ManyToOne
    @JoinColumn(name="task_fs", nullable=true)
	private Task task;
	
	@Getter
	@Setter
	@ManyToOne
    @JoinColumn(name="environment_fs", nullable=true)
	private Environment environment;
	
	@Getter
	@Setter
	@ManyToOne
    @JoinColumn(name="environmentthreat_fs", nullable=true)
	private Environmentthreat environmentthreat;
	
	@Getter
	@Setter
	@ManyToOne
    @JoinColumn(name="job_fs", nullable=true)
	private Job job;
	
	@Getter
	@Setter
	@ManyToOne
    @JoinColumn(name="usergroup_fs", nullable=true)
	private Usergroup usergroup;
	
	@Getter
	@Setter
	@ManyToOne
    @JoinColumn(name="user_fs", nullable=true)
	private User user;
	
	@Getter
	@Setter
	@ManyToOne
    @JoinColumn(name="role_fs", nullable=true)
	private Role role;
	
	@Getter
	@Setter
	private String description;
	

	public Integer getRSCMClient_fs() {
		if(rSCMClient == null) {
			return null;
		}
		return rSCMClient.getRscmclientId();
	}

	public Integer getTask_fs() {
		if(task == null) {
			return null;
		}
		return task.getTaskId();
	}

	public Integer getEnvironment_fs() {
		if(environment == null) {
			return null;
		}
		return environment.getEnvironmentId();
	}

	public Integer getEnvironmentthreat_fs() {
		if(environmentthreat == null) {
			return null;
		}
		return environmentthreat.getEnvironmentthreatId();
	}

	public Integer getJob_fs() {
		if(job == null) {
			return null;
		}
		return job.getJobId();
	}

	public Integer getUsergroup_fs() {
		if(usergroup == null) {
			return null;
		}
		return usergroup.getUsergroupId();
	}

	public Integer getUser_fs() {
		if(user == null) {
			return null;
		}
		return user.getUserId();
	}

	public Integer getRole_fs() {
		if(role == null) {
			return null;
		}
		return role.getRoleId();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Class: " + this.getClass().getName() + "<br/>");
		sb.append("scriptexecutionId: " + scriptexecutionId + "<br/>");
		if(scriptexecutionAssigneddate!=null) {
			sb.append("scriptexecutionAssigneddate: " + new SimpleDateFormat("dd-MM-yyyy HH:mm").format(scriptexecutionAssigneddate) + "<br/>");
		}else {
			sb.append("scriptexecutionAssigneddate: null<br/>");
		}
		if(scriptexecutionExecutiondate!=null) {
			sb.append("scriptexecutionExecutiondate: " + new SimpleDateFormat("dd-MM-yyyy HH:mm").format(scriptexecutionExecutiondate) + "<br/>");
		}else {
			sb.append("scriptexecutionExecutiondate: null<br/>");
		}
		sb.append("scriptName: " + scriptName + "<br/>");
		sb.append("assignment: [" + getAssignment() + "]<br/>");
		
		return sb.toString();
	}
	
	private String getAssignment() {
		try {
			StringBuilder sb = new StringBuilder();
			Method[] methods = this.getClass().getMethods();
			
			for (Method m : methods) {
				if(m.getName().contains("_fs") && m.getName().contains("get")) {
					Integer i;
					i = (Integer) m.invoke(this, null);
					if(i != null) {
						String cleanedMethodName = m.getName().substring(3, m.getName().length()-3);
						sb.append(cleanedMethodName + ",");
					}
				}
			}
			return sb.toString().substring(0, sb.toString().length()-1);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			return "";
		}
	}
	
	
	
	
}
