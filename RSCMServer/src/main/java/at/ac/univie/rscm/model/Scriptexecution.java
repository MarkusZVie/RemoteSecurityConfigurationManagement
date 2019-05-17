package at.ac.univie.rscm.model;

import java.util.Date;
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
    @JoinColumn(name="applicantgroup_fs", nullable=true)
	private Applicantgroup applicantgroup;
	
	@Getter
	@Setter
	@ManyToOne
    @JoinColumn(name="applicant_fs", nullable=true)
	private Applicant applicant;
	
	@Getter
	@Setter
	@ManyToOne
    @JoinColumn(name="role_fs", nullable=true)
	private Role role;
	
	

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

	public Integer getApplicantgroup_fs() {
		if(applicantgroup == null) {
			return null;
		}
		return applicantgroup.getApplicantgroupId();
	}

	public Integer getApplicant_fs() {
		if(applicant == null) {
			return null;
		}
		return applicant.getApplicantId();
	}

	public Integer getRole_fs() {
		if(role == null) {
			return null;
		}
		return role.getRoleId();
	}
	
	
	
	
	
}
