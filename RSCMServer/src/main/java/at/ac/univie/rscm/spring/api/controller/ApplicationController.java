package at.ac.univie.rscm.spring.api.controller;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import at.ac.univie.rscm.model.Applicant;
import at.ac.univie.rscm.model.Applicantgroup;
import at.ac.univie.rscm.model.Environment;
import at.ac.univie.rscm.model.Environmentthreat;
import at.ac.univie.rscm.model.Job;
import at.ac.univie.rscm.model.Role;
import at.ac.univie.rscm.model.Task;
import at.ac.univie.rscm.spring.api.repository.ApplicantRepository;
import at.ac.univie.rscm.spring.api.repository.ApplicantgroupRepository;
import at.ac.univie.rscm.spring.api.repository.EnvironmentRepository;
import at.ac.univie.rscm.spring.api.repository.EnvironmentthreatsRepository;
import at.ac.univie.rscm.spring.api.repository.JobRepository;
import at.ac.univie.rscm.spring.api.repository.RoleRepository;
import at.ac.univie.rscm.spring.api.repository.TaskReposiotry;



@RestController
@RequestMapping("/Application/")
public class ApplicationController {
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private ApplicantRepository applicantRepository;
	
	@Autowired
	private ApplicantgroupRepository groupRepository;
	
	@Autowired
	private JobRepository jobRepository;
	
	@Autowired
	private TaskReposiotry taskRepository;

	@Autowired
	private EnvironmentRepository environmentRepository;
	
	@Autowired
	private EnvironmentthreatsRepository environmentthreatRepository;
	
	@PostMapping("/pushGroup")
	public String pushApplicantGroupRegistration(@RequestParam("formVars") String[] formVars) {
		HashMap<String, String> formVarsMap = new HashMap<String, String>();
		assert (formVars.length % 2 == 0);
		for (int i = 0; i < formVars.length;) {
			formVarsMap.put(formVars[i++], formVars[i++]);
		}
		
		Collection<Applicantgroup> exsistingGroupList = groupRepository.findAllByApplicantgroupName(formVarsMap.get("groupName"));
		if(exsistingGroupList.size()>0) {
			return "Group cannot be added, because the Groupname '"+formVarsMap.get("groupName")+"' is already used";
		}
		Applicantgroup newGroup = new Applicantgroup();
		newGroup.setApplicantgroupName(formVarsMap.get("groupName"));
		newGroup.setApplicantgroupDescription(formVarsMap.get("groupDescription"));
				
		groupRepository.save(newGroup);
		return "Group '"+formVarsMap.get("groupName")+"' was saved successfully";
	}
	
	@PostMapping("/pushRole")
	public String pushRole(@RequestParam("formVars") String[] formVars) {
		HashMap<String, String> formVarsMap = new HashMap<String, String>();
		assert (formVars.length % 2 == 0);
		for (int i = 0; i < formVars.length;) {
			formVarsMap.put(formVars[i++], formVars[i++]);
		}
		
		Collection<Role> exsistingRoleList = roleRepository.findAllByRoleName(formVarsMap.get("roleName"));
		if(exsistingRoleList.size()>0) {
			return "Role cannot be added, because the rolename '"+formVarsMap.get("roleName")+"' is already used";
		}
		Role newRole = new Role();
		newRole.setRoleName(formVarsMap.get("roleName"));
		newRole.setRoleDescription(formVarsMap.get("roleDescription"));
		
		roleRepository.save(newRole);
		return "Role '"+formVarsMap.get("roleName")+"' was saved successfully";
	}
	
	@PostMapping("/deleteRole")
	public String deleteRole(@RequestParam("roleId") String roleId) {
		roleRepository.deleteById(Integer.parseInt(roleId));
		return "The Role with id: " + roleId + "was deleted successfully";
	}
	
	@PostMapping("/deleteGroup")
	public String deleteGroup(@RequestParam("groupId") String groupId) {
		groupRepository.deleteById(Integer.parseInt(groupId));
		return "The Group with id: " + groupId + "was deleted successfully";
	}
	
	@PostMapping("/getApplicantList")
	public String getApplicantList(@RequestParam("searchString") String searchString) {
		List<Applicant> applicantList;
		if(searchString == null) {searchString="";}
		
		applicantList = applicantRepository.findByContainsInName(searchString);
			
		
		
		JSONArray ja = new JSONArray();
		for(Applicant a:applicantList) {
			JSONObject jo = new JSONObject();
			jo.put("applicantId", a.getApplicantId());
			jo.put("applicantName", a.getApplicantName());
			jo.put("applicantFirstname", a.getApplicantFirstname());
			jo.put("applicantLastname", a.getApplicantLastname());
			jo.put("applicantEmail", a.getApplicantEmail());
			ja.put(jo);
		}
		return ja.toString();
	}
	
	@PostMapping("/updateAssignedToRole")
	public String updateAssignedToRole(@RequestParam("roleIds") int[] roleId,@RequestParam("applicantId") int applicantId) {
		if(applicantId<0) {
			return "Applicant ID is not valid, it is: "+ applicantId;
		}else {
			Optional<Applicant> optionalApplicant = applicantRepository.findById(applicantId);
			if(!optionalApplicant.isPresent()) {
				return "Cannot find ApplicantID, it is: "+ applicantId;
			}else {
				Applicant applicant = optionalApplicant.get();
				Set<Role> setRoles = applicant.getRoles();
				
				//create Set of roles
				Set<Integer> newRoleIds = new HashSet<Integer>();
				for(int id:roleId) {
					newRoleIds.add(id);
				}
				Set<Integer> oldRoleIds = new HashSet<Integer>();
				for(Role r: setRoles) {
					oldRoleIds.add(r.getRoleId());
				}
				
				//check if there are roles that dont appier in the new setting
				for(Role r: setRoles) {
					if(!newRoleIds.contains(r.getRoleId())) {
						setRoles.remove(r);
					}
				}
				
				
				
				//add new Roles
				for(int id:roleId) {
					if(!oldRoleIds.contains(id)) {
						Optional<Role> optionalRole= roleRepository.findById(id);
						if(optionalRole.isPresent()) {
							setRoles.add(optionalRole.get());
						}
						
					}
				}
				
				applicantRepository.save(applicant);
				return"Roles are updated";
			}
		}
		
	}
	
	@PostMapping("/updateAssignedToGroup")
	public String updateAssignedToGroup(@RequestParam("groupIds") int[] groupId,@RequestParam("applicantId") int applicantId) {
		if(applicantId<0) {
			return "Applicant ID is not valid, it is: "+ applicantId;
		}else {
			Optional<Applicant> optionalApplicant = applicantRepository.findById(applicantId);
			if(!optionalApplicant.isPresent()) {
				return "Cannot find ApplicantID, it is: "+ applicantId;
			}else {
				Applicant applicant = optionalApplicant.get();
				Set<Applicantgroup> setGroups = applicant.getApplicantgroups();
				
				//create Set of roles
				Set<Integer> newGroupIds = new HashSet<Integer>();
				for(int id:groupId) {
					newGroupIds.add(id);
				}
				Set<Integer> oldGroupIds = new HashSet<Integer>();
				for(Applicantgroup g: setGroups) {
					oldGroupIds.add(g.getApplicantgroupId());
				}
				
				//check if there are roles that dont appier in the new setting
				for(Applicantgroup g: setGroups) {
					if(!newGroupIds.contains(g.getApplicantgroupId())) {
						setGroups.remove(g);
					}
				}
				
				
				
				//add new Roles
				for(int id:groupId) {
					if(!oldGroupIds.contains(id)) {
						Optional<Applicantgroup> optionalGroup= groupRepository.findById(id);
						if(optionalGroup.isPresent()) {
							setGroups.add(optionalGroup.get());
						}
						
					}
				}
				
				applicantRepository.save(applicant);
				return"Groups are updated";
			}
		}
		
	}
	
	
	
	@PostMapping("/getRoleList")
	public String getRoleList(@RequestParam("searchString") String searchString,@RequestParam("applicantIdParameter") String applicantIdParameter) {
		List<Role> roleList;
		if(searchString == null || searchString.equals("")) {
			roleList = roleRepository.findAll();
		}else {
			roleList = roleRepository.findByContainsInName(searchString);
		}
		int applicantId = Integer.parseInt(applicantIdParameter);
		Applicant applicant = null;
		if(applicantId!=-1) {
			Optional<Applicant> applicantOpt = applicantRepository.findById(applicantId);
			if(applicantOpt.isPresent()) {
				applicant=applicantOpt.get();
			}
		}
		
		JSONArray ja = new JSONArray();
		for(Role r:roleList) {
			JSONObject jo = new JSONObject();
			jo.put("roleId", r.getRoleId());
			jo.put("roleName", r.getRoleName());
			jo.put("roleDescription", r.getRoleDescription());
			if(applicant==null) {
				jo.put("isAssignetTo", "null");
			}else{
				if(applicant.getRoles().contains(r)){
					jo.put("isAssignetTo", "yes");
				}else {
					jo.put("isAssignetTo", "no");
				}
			}
			
			ja.put(jo);
		}
		return ja.toString();
	}
	
	@PostMapping("/getGroupList")
	public String getGroupList(@RequestParam("searchString") String searchString,@RequestParam("applicantIdParameter") String applicantIdParameter) {
		List<Applicantgroup> groupList;
		if(searchString == null || searchString.equals("")) {
			groupList = groupRepository.findAll();
		}else {
			groupList = groupRepository.findByContainsInName(searchString);
		}
		int applicantId = Integer.parseInt(applicantIdParameter);
		Applicant applicant = null;
		if(applicantId!=-1) {
			Optional<Applicant> applicantOpt = applicantRepository.findById(applicantId);
			if(applicantOpt.isPresent()) {
				applicant=applicantOpt.get();
			}
		}
		
		JSONArray ja = new JSONArray();
		for(Applicantgroup g:groupList) {
			JSONObject jo = new JSONObject();
			jo.put("groupId", g.getApplicantgroupId());
			jo.put("groupName", g.getApplicantgroupName());
			jo.put("groupDescription", g.getApplicantgroupDescription());
			if(applicant==null) {
				jo.put("isAssignetTo", "null");
			}else{
				if(applicant.getApplicantgroups().contains(g)){
					jo.put("isAssignetTo", "yes");
				}else {
					jo.put("isAssignetTo", "no");
				}
			}
			
			ja.put(jo);
		}
		return ja.toString();
	}
	

	@PostMapping("/pushJob")
	public String pushJob(@RequestParam("formVars") String[] formVars) {
		HashMap<String, String> formVarsMap = new HashMap<String, String>();
		assert (formVars.length % 2 == 0);
		for (int i = 0; i < formVars.length;) {
			formVarsMap.put(formVars[i++], formVars[i++]);
		}
		
		Collection<Job> exsistingJobList = jobRepository.findAllByJobName(formVarsMap.get("jobName"));
		if(exsistingJobList.size()>0) {
			return "Job cannot be added, because the jobname '"+formVarsMap.get("jobName")+"' is already used";
		}
		Job newJob = new Job();
		newJob.setJobName(formVarsMap.get("jobName"));
		newJob.setJobDescription(formVarsMap.get("jobDescription"));
		
		jobRepository.save(newJob);
		return "Job '"+formVarsMap.get("jobName")+"' was saved successfully";
	}
	
	@PostMapping("/deleteJob")
	public String deleteJob(@RequestParam("jobId") String jobId) {
		jobRepository.deleteById(Integer.parseInt(jobId));
		return "The Job with id: " + jobId + "was deleted successfully";
	}
	
	
	@PostMapping("/updateAssignedToJob")
	public String updateAssignedToJob(@RequestParam("jobIds") int[] jobId,@RequestParam("applicantId") int applicantId) {
		if(applicantId<0) {
			return "Applicant ID is not valid, it is: "+ applicantId;
		}else {
			Optional<Applicant> optionalApplicant = applicantRepository.findById(applicantId);
			if(!optionalApplicant.isPresent()) {
				return "Cannot find ApplicantID, it is: "+ applicantId;
			}else {
				Applicant applicant = optionalApplicant.get();
				Set<Job> setJobs = applicant.getJobs();
				
				//create Set of jobs
				Set<Integer> newJobIds = new HashSet<Integer>();
				for(int id:jobId) {
					newJobIds.add(id);
				}
				Set<Integer> oldJobIds = new HashSet<Integer>();
				for(Job r: setJobs) {
					oldJobIds.add(r.getJobId());
				}
				
				//check if there are jobs that dont appier in the new setting
				for(Job r: setJobs) {
					if(!newJobIds.contains(r.getJobId())) {
						setJobs.remove(r);
					}
				}
				
				
				
				//add new Jobs
				for(int id:jobId) {
					if(!oldJobIds.contains(id)) {
						Optional<Job> optionalJob= jobRepository.findById(id);
						if(optionalJob.isPresent()) {
							setJobs.add(optionalJob.get());
						}
						
					}
				}
				
				applicantRepository.save(applicant);
				return"Jobs are updated";
			}
		}
		
	}
	
	
	@PostMapping("/getJobList")
	public String getJobList(@RequestParam("searchString") String searchString,@RequestParam("applicantIdParameter") String applicantIdParameter) {
		List<Job> jobList;
		if(searchString == null || searchString.equals("")) {
			jobList = jobRepository.findAll();
		}else {
			jobList = jobRepository.findByContainsInName(searchString);
		}
		int applicantId = Integer.parseInt(applicantIdParameter);
		Applicant applicant = null;
		if(applicantId!=-1) {
			Optional<Applicant> applicantOpt = applicantRepository.findById(applicantId);
			if(applicantOpt.isPresent()) {
				applicant=applicantOpt.get();
			}
		}
		
		JSONArray ja = new JSONArray();
		for(Job r:jobList) {
			JSONObject jo = new JSONObject();
			jo.put("jobId", r.getJobId());
			jo.put("jobName", r.getJobName());
			jo.put("jobDescription", r.getJobDescription());
			if(applicant==null) {
				jo.put("isAssignetTo", "null");
			}else{
				if(applicant.getJobs().contains(r)){
					jo.put("isAssignetTo", "yes");
				}else {
					jo.put("isAssignetTo", "no");
				}
			}
			
			ja.put(jo);
		}
		return ja.toString();
	}
	

	@PostMapping("/pushTask")
	public String pushApplicantRegistration(@RequestParam("formVars") String[] formVars) {
		HashMap<String, String> formVarsMap = new HashMap<String, String>();
		assert (formVars.length % 2 == 0);
		for (int i = 0; i < formVars.length;) {
			formVarsMap.put(formVars[i++], formVars[i++]);
		}
		
		Collection<Task> exsistingTaskList = taskRepository.findAllByTaskName(formVarsMap.get("taskName"));
		if(exsistingTaskList.size()>0) {
			return "Task cannot be added, because the taskname '"+formVarsMap.get("taskName")+"' is already used";
		}
		Task newTask = new Task();
		newTask.setTaskName(formVarsMap.get("taskName"));
		newTask.setTaskDescription(formVarsMap.get("taskDescription"));
		newTask.setTaskOutcome(formVarsMap.get("taskOutcome"));
		SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
		System.out.println(">>"+formVarsMap.get("taskPlanBegindate")+"<<");
		System.out.println(">>"+formVarsMap.get("taskPlanBegintime")+"<<");
		try {
			if(!formVarsMap.get("taskPlanBegindate").equals("")) {
				if(!formVarsMap.get("taskPlanBegintime").equals("")) {
					newTask.setTaskPlanBegindate(sdfDateTime.parse(formVarsMap.get("taskPlanBegindate") + " " + formVarsMap.get("taskPlanBegintime")));
				}else {
						newTask.setTaskPlanBegindate(sdfDate.parse(formVarsMap.get("taskPlanBegindate")));
				}
				
			}
		} catch (ParseException e) {
			return "wrong Taskbegin Date format";
		}	
		
		try {
			if(!formVarsMap.get("taskPlanEnddate").equals("")) {
				if(!formVarsMap.get("taskPlanEndtime").equals("")) {
					newTask.setTaskPlanEnddate(sdfDateTime.parse(formVarsMap.get("taskPlanEnddate") + " " + formVarsMap.get("taskPlanEndtime")));
				}else {
						newTask.setTaskPlanEnddate(sdfDate.parse(formVarsMap.get("taskPlanEnddate")));
				}
				
			}
		} catch (ParseException e) {
			return "wrong Taskend Date format";
		}	
		
		taskRepository.save(newTask);
		return "Task '"+formVarsMap.get("taskName")+"' was saved successfully";
	}
	
	@PostMapping("/deleteTask")
	public String deleteTask(@RequestParam("taskId") String taskId) {
		taskRepository.deleteById(Integer.parseInt(taskId));
		return "The Task with id: " + taskId + "was deleted successfully";
	}
	
	
	@PostMapping("/updateAssignedToTask")
	public String updateAssignedToTask(@RequestParam("taskIds") int[] taskId,@RequestParam("applicantId") int applicantId) {
		if(applicantId<0) {
			return "Applicant ID is not valid, it is: "+ applicantId;
		}else {
			Optional<Applicant> optionalApplicant = applicantRepository.findById(applicantId);
			if(!optionalApplicant.isPresent()) {
				return "Cannot find ApplicantID, it is: "+ applicantId;
			}else {
				Applicant applicant = optionalApplicant.get();
				Set<Task> setTasks = applicant.getTasks();
				
				//create Set of tasks
				Set<Integer> newTaskIds = new HashSet<Integer>();
				for(int id:taskId) {
					newTaskIds.add(id);
				}
				Set<Integer> oldTaskIds = new HashSet<Integer>();
				for(Task r: setTasks) {
					oldTaskIds.add(r.getTaskId());
				}
				
				//check if there are tasks that dont appier in the new setting
				for(Task r: setTasks) {
					if(!newTaskIds.contains(r.getTaskId())) {
						setTasks.remove(r);
					}
				}
				
				
				
				//add new Tasks
				for(int id:taskId) {
					if(!oldTaskIds.contains(id)) {
						Optional<Task> optionalTask= taskRepository.findById(id);
						if(optionalTask.isPresent()) {
							setTasks.add(optionalTask.get());
						}
						
					}
				}
				
				applicantRepository.save(applicant);
				return"Tasks are updated";
			}
		}
		
	}
	
	
	@PostMapping("/getTaskList")
	public String getTaskList(@RequestParam("searchString") String searchString,@RequestParam("applicantIdParameter") String applicantIdParameter) {
		List<Task> taskList;
		if(searchString == null || searchString.equals("")) {
			taskList = taskRepository.findAll();
		}else {
			taskList = taskRepository.findByContainsInName(searchString);
		}
		int applicantId = Integer.parseInt(applicantIdParameter);
		Applicant applicant = null;
		if(applicantId!=-1) {
			Optional<Applicant> applicantOpt = applicantRepository.findById(applicantId);
			if(applicantOpt.isPresent()) {
				applicant=applicantOpt.get();
			}
		}
		
		
		SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		JSONArray ja = new JSONArray();
		for(Task r:taskList) {
			JSONObject jo = new JSONObject();
			jo.put("taskId", r.getTaskId());
			jo.put("taskName", r.getTaskName());
			if(r.getTaskPlanBegindate() != null) {
				jo.put("taskPlanBegindate", sdfDateTime.format(r.getTaskPlanBegindate()));
			}else {
				jo.put("taskPlanBegindate", "not defined");
			}
			if(r.getTaskPlanEnddate() != null) {
				jo.put("taskPlanEnddate",  sdfDateTime.format(r.getTaskPlanEnddate()));
			}else {
				jo.put("taskPlanEnddate", "not defined");
			}
			
			
			jo.put("taskDescription", r.getTaskDescription());
			jo.put("taskOutcome", r.getTaskOutcome());
			if(applicant==null) {
				jo.put("isAssignetTo", "null");
			}else{
				if(applicant.getTasks().contains(r)){
					jo.put("isAssignetTo", "yes");
				}else {
					jo.put("isAssignetTo", "no");
				}
			}
			
			ja.put(jo);
		}
		return ja.toString();
	}
	
	@PostMapping("/pushEnvironment")
	public String pushEnvironment(@RequestParam("formVars") String[] formVars) {
		HashMap<String, String> formVarsMap = new HashMap<String, String>();
		assert (formVars.length % 2 == 0);
		for (int i = 0; i < formVars.length;) {
			formVarsMap.put(formVars[i++], formVars[i++]);
		}
		
		if(!validateIPAddress(formVarsMap.get("ipRangeBegin"))) {
			return "The begin IPv4 Address: '"+formVarsMap.get("ipRangeBegin")+"' is not valid";
		}
		
		Collection<Environment> exsistingEnvironmentList = environmentRepository.findAllByIpRangeBegin(formVarsMap.get("ipRangeBegin"));
		if(exsistingEnvironmentList.size()>0) {
			return "Environment cannot be added, because the ipRangeBegin '"+formVarsMap.get("ipRangeBegin")+"' is already used";
		}
		Environment newEnvironment = new Environment();
		newEnvironment.setIpRangeBegin(formVarsMap.get("ipRangeBegin"));
		if(!formVarsMap.get("ipEangeEnd").equals("")) {
			if(!validateIPAddress(formVarsMap.get("ipEangeEnd"))) {
				return "The end IPv4 Address: '"+formVarsMap.get("ipEangeEnd")+"' is not valid"; 
			}
			newEnvironment.setIpRangeEnd(formVarsMap.get("ipEangeEnd"));
		}
		newEnvironment.setEnvironmentDescription(formVarsMap.get("environmentDescription"));
		
		environmentRepository.save(newEnvironment);
		return "Environment '"+formVarsMap.get("ipRangeBegin")+"' was saved successfully";
	}
	
	@PostMapping("/deleteEnvironment")
	public String deleteEnvironment(@RequestParam("environmentId") String environmentId) {
		environmentRepository.deleteById(Integer.parseInt(environmentId));
		return "The Environment with id: " + environmentId + "was deleted successfully";
	}
	
	@PostMapping("/getEnvironmentList")
	public String getEnvironmentList(@RequestParam("searchString") String searchString) {
		List<Environment> environmentList;
		if(searchString == null || searchString.equals("")) {
			environmentList = environmentRepository.findAll();
		}else {
			environmentList = environmentRepository.findByContainsInIp(searchString);
		}
		
		JSONArray ja = new JSONArray();
		for(Environment r:environmentList) {
			JSONObject jo = new JSONObject();
			jo.put("environmentId", r.getEnvironmentId());
			jo.put("ipRangeBegin", r.getIpRangeBegin());
			jo.put("ipEangeEnd", r.getIpRangeEnd());
			jo.put("environmentDescription", r.getEnvironmentDescription());
			
			ja.put(jo);
		}
		return ja.toString();
	}
	
	private boolean validateIPAddress( String ipAddress ){ 
		
		try {
			Inet4Address.getByName(ipAddress);
			return true;
		} catch (UnknownHostException e) {
			return false;
		}
		
	}
	
	@PostMapping("/pushEnvironmentthreat")
	public String pushEnvironmentthreat(@RequestParam("formVars") String[] formVars) {
		HashMap<String, String> formVarsMap = new HashMap<String, String>();
		assert (formVars.length % 2 == 0);
		for (int i = 0; i < formVars.length;) {
			formVarsMap.put(formVars[i++], formVars[i++]);
		}
		
		Collection<Environmentthreat> exsistingEnvironmentthreatList = environmentthreatRepository.findAllByThreatTitle(formVarsMap.get("threatTitle"));
		if(exsistingEnvironmentthreatList.size()>0) {
			return "Environmentthreat cannot be added, because the threat title '"+formVarsMap.get("threatTitle")+"' is already used";
		}
		Environmentthreat newEnvironmentthreat = new Environmentthreat();
		newEnvironmentthreat.setThreatTitle(formVarsMap.get("threatTitle"));
		
		if(!formVarsMap.get("expectedProblem").equals("")) {
			newEnvironmentthreat.setExpectedProblem(formVarsMap.get("expectedProblem"));
		}
		if(!formVarsMap.get("threatDescription").equals("")) {
			newEnvironmentthreat.setThreatDescription(formVarsMap.get("threatDescription"));
		}
		try {
			if(!formVarsMap.get("threatLevel").equals("")) {
				newEnvironmentthreat.setThreatLevel(Integer.parseInt(formVarsMap.get("threatLevel")));
			}
		} catch (Exception e) {
			return "The Threatlevel is not a valid Integer Number ("+formVarsMap.get("threatLevel")+")";
		}
				
		environmentthreatRepository.save(newEnvironmentthreat);
		return "Environmentthreat '"+formVarsMap.get("environmentthreatName")+"' was saved successfully";
	}
	
	@PostMapping("/deleteEnvironmentthreat")
	public String deleteEnvironmentthreat(@RequestParam("environmentthreatId") String environmentthreatId) {
		environmentthreatRepository.deleteById(Integer.parseInt(environmentthreatId));
		return "The Environmentthreat with id: " + environmentthreatId + "was deleted successfully";
	}
	
	
	@PostMapping("/updateAssignedToEnvironmentthreat")
	public String updateAssignedToEnvironmentthreat(@RequestParam("environmentthreatIds") int[] environmentthreatId,@RequestParam("environmentId") int environmentId) {
		if(environmentId<0) {
			return "Environment ID is not valid, it is: "+ environmentId;
		}else {
			Optional<Environment> optionalEnvironment = environmentRepository.findById(environmentId);
			if(!optionalEnvironment.isPresent()) {
				return "Cannot find EnvironmentID, it is: "+ environmentId;
			}else {
				Environment environment = optionalEnvironment.get();
				Set<Environmentthreat> setEnvironmentthreats = environment.getEnvironmentthreats();
				
				//create Set of environmentthreats
				Set<Integer> newEnvironmentthreatIds = new HashSet<Integer>();
				for(int id:environmentthreatId) {
					newEnvironmentthreatIds.add(id);
				}
				Set<Integer> oldEnvironmentthreatIds = new HashSet<Integer>();
				for(Environmentthreat r: setEnvironmentthreats) {
					oldEnvironmentthreatIds.add(r.getEnvironmentthreatId());
				}
				
				//check if there are environmentthreats that dont appier in the new setting
				for(Environmentthreat r: setEnvironmentthreats) {
					if(!newEnvironmentthreatIds.contains(r.getEnvironmentthreatId())) {
						setEnvironmentthreats.remove(r);
					}
				}
				
				
				
				//add new Environmentthreats
				for(int id:environmentthreatId) {
					if(!oldEnvironmentthreatIds.contains(id)) {
						Optional<Environmentthreat> optionalEnvironmentthreat= environmentthreatRepository.findById(id);
						if(optionalEnvironmentthreat.isPresent()) {
							setEnvironmentthreats.add(optionalEnvironmentthreat.get());
						}
						
					}
				}
				
				environmentRepository.save(environment);
				return"Environmentthreats are updated";
			}
		}
		
	}
	
	
	@PostMapping("/getEnvironmentthreatList")
	public String getEnvironmentthreatList(@RequestParam("searchString") String searchString,@RequestParam("environmentIdParameter") String environmentIdParameter) {
		List<Environmentthreat> environmentthreatList;
		if(searchString == null || searchString.equals("")) {
			environmentthreatList = environmentthreatRepository.findAll();
		}else {
			environmentthreatList = environmentthreatRepository.findByContainsInTitle(searchString);
		}
		int environmentId = Integer.parseInt(environmentIdParameter);
		Environment environment = null;
		if(environmentId!=-1) {
			Optional<Environment> environmentOpt = environmentRepository.findById(environmentId);
			if(environmentOpt.isPresent()) {
				environment=environmentOpt.get();
			}
		}
		
		JSONArray ja = new JSONArray();
		for(Environmentthreat r:environmentthreatList) {
			JSONObject jo = new JSONObject();
			jo.put("environmentthreatId", r.getEnvironmentthreatId());
			jo.put("threatTitle", r.getThreatTitle());
			jo.put("threatDescription", r.getThreatDescription());
			jo.put("expectedProblem", r.getExpectedProblem());
			jo.put("threatLevel", r.getThreatLevel());
			if(environment==null) {
				jo.put("isAssignetTo", "null");
			}else{
				if(environment.getEnvironmentthreats().contains(r)){
					jo.put("isAssignetTo", "yes");
				}else {
					jo.put("isAssignetTo", "no");
				}
			}
			
			ja.put(jo);
		}
		return ja.toString();
	}
	
	
}
