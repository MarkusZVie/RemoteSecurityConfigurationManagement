package at.ac.univie.rscm.spring.api.controller;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
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


import at.ac.univie.rscm.model.User;
import at.ac.univie.rscm.model.Usergroup;
import at.ac.univie.rscm.model.Environment;
import at.ac.univie.rscm.model.Environmentthreat;
import at.ac.univie.rscm.model.Job;
import at.ac.univie.rscm.model.RSCMClient;
import at.ac.univie.rscm.model.RSCMClientConnection;
import at.ac.univie.rscm.model.Role;
import at.ac.univie.rscm.model.Scriptexecution;
import at.ac.univie.rscm.model.Task;
import at.ac.univie.rscm.spring.api.repository.UserRepository;
import at.ac.univie.rscm.spring.api.repository.UsergroupRepository;
import at.ac.univie.rscm.spring.api.repository.EnvironmentRepository;
import at.ac.univie.rscm.spring.api.repository.EnvironmentthreatsRepository;
import at.ac.univie.rscm.spring.api.repository.JobRepository;
import at.ac.univie.rscm.spring.api.repository.RSCMClientConnectionRepository;
import at.ac.univie.rscm.spring.api.repository.RoleRepository;
import at.ac.univie.rscm.spring.api.repository.ScriptexecutionRepository;
import at.ac.univie.rscm.spring.api.repository.TaskReposiotry;



@RestController
@RequestMapping("/Application/")
public class ApplicationController {
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UsergroupRepository groupRepository;
	
	@Autowired
	private JobRepository jobRepository;
	
	@Autowired
	private TaskReposiotry taskRepository;

	@Autowired
	private EnvironmentRepository environmentRepository;
	
	@Autowired
	private EnvironmentthreatsRepository environmentthreatRepository;
	
	@Autowired
	private ScriptexecutionRepository scriptexecutionRepository;
	
	@Autowired
	private RSCMClientConnectionRepository rscmClientConnectionRepository;
	
	@PostMapping("/pushGroup")
	public String pushUserGroupRegistration(@RequestParam("formVars") String[] formVars) {
		HashMap<String, String> formVarsMap = new HashMap<String, String>();
		assert (formVars.length % 2 == 0);
		for (int i = 0; i < formVars.length;) {
			formVarsMap.put(formVars[i++], formVars[i++]);
		}
		
		Collection<Usergroup> exsistingGroupList = groupRepository.findAllByUsergroupName(formVarsMap.get("groupName"));
		if(exsistingGroupList.size()>0) {
			return "Group cannot be added, because the Groupname '"+formVarsMap.get("groupName")+"' is already used";
		}
		Usergroup newGroup = new Usergroup();
		newGroup.setUsergroupName(formVarsMap.get("groupName"));
		newGroup.setUsergroupDescription(formVarsMap.get("groupDescription"));
				
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
		int id = Integer.parseInt(roleId);
		if(id <=4) {
			return "the role cannot be deleted, its a system role";
		}
		List<Scriptexecution> scriptList = scriptexecutionRepository.findByRoleId(id);
		Role j = roleRepository.findById(id).get();
		if(scriptList.size()==0 && j.getUsers().size()==0) {
			roleRepository.deleteById(Integer.parseInt(roleId));
			return "The Role with id: " + roleId + " was deleted successfully";
		}else {
			return "There are other objects that depend on the role. <br>"
					+ "There are "+scriptList.size() +" ScriptExecutions and "+j.getUsers().size()+ " Users that depend on this role <br>"
					+ "If you really want to delete this role press here:<br>"
					+ "<button class=\"w3-button w3-red\" onclick=\"forceDeleteRole('"+roleId+"')\">force delete</button>";
		}
		
	}
	
	@PostMapping("/forceDeleteRole")
	public String forceDeleteRole(@RequestParam("roleId") String roleId) {
		int id = Integer.parseInt(roleId);
		List<Scriptexecution> scriptList = scriptexecutionRepository.findByRoleId(id);
		Role j = roleRepository.findById(id).get();
		if(scriptList.size()==0 && j.getUsers().size()==0) {
			roleRepository.deleteById(Integer.parseInt(roleId));
			return "The Role with id: " + roleId + "was deleted successfully";
		}else {
			for(Scriptexecution se : scriptList) {
				if(se.getScriptexecutionExecutiondate()==null) {
					scriptexecutionRepository.delete(se);
				}else {
					se.setRole(null);
					se.setDescription("Assigned By [Role], but Role with id: "+roleId+" is deleted");
					scriptexecutionRepository.save(se);
				}
			}
			j.setUsers(new HashSet<User>());
			roleRepository.save(j);
			
			List<User> userList = userRepository.findByRoleId(id);
			for(User a : userList) {
				a.deleteRole(id);
				userRepository.save(a);
			}
			
			return deleteRole(roleId);
		}
	}
	
	
		
	@PostMapping("/deleteGroup")
	public String deleteGroup(@RequestParam("groupId") String groupId) {
		int id = Integer.parseInt(groupId);
		List<Scriptexecution> scriptList = scriptexecutionRepository.findByUsergroupId(id);
		Usergroup j = groupRepository.findById(id).get();
		if(scriptList.size()==0 && j.getUsers().size()==0) {
			groupRepository.deleteById(Integer.parseInt(groupId));
			return "The Group with id: " + groupId + " was deleted successfully";
		}else {
			return "There are other objects that depend on the group. <br>"
					+ "There are "+scriptList.size() +" ScriptExecutions and "+j.getUsers().size()+ " Users that depend on this group <br>"
					+ "If you really want to delete this group press here:<br>"
					+ "<button class=\"w3-button w3-red\" onclick=\"forceDeleteGroup('"+groupId+"')\">force delete</button>";
		}
		
	}
	
	@PostMapping("/forceDeleteGroup")
	public String forceDeleteGroup(@RequestParam("groupId") String groupId) {
		int id = Integer.parseInt(groupId);
		List<Scriptexecution> scriptList = scriptexecutionRepository.findByUsergroupId(id);
		Usergroup j = groupRepository.findById(id).get();
		if(scriptList.size()==0 && j.getUsers().size()==0) {
			groupRepository.deleteById(Integer.parseInt(groupId));
			return "The Group with id: " + groupId + "was deleted successfully";
		}else {
			for(Scriptexecution se : scriptList) {
				if(se.getScriptexecutionExecutiondate()==null) {
					scriptexecutionRepository.delete(se);
				}else {
					se.setUsergroup(null);
					se.setDescription("Assigned By [Group], but Group with id: "+groupId+" is deleted");
					scriptexecutionRepository.save(se);
				}
			}
			j.setUsers(new HashSet<User>());
			groupRepository.save(j);
			
			List<User> userList = userRepository.findByUsergroupId(id);
			for(User a : userList) {
				a.deleteGroup(id);
				userRepository.save(a);
			}
			
			return deleteGroup(groupId);
		}
		
	}
	
	@PostMapping("/getUserList")
	public String getUserList(@RequestParam("searchString") String searchString) {
		List<User> userList;
		if(searchString == null) {searchString="";}
		
		userList = userRepository.findByContainsInName(searchString);
			
		
		
		JSONArray ja = new JSONArray();
		for(User a:userList) {
			JSONObject jo = new JSONObject();
			jo.put("userId", a.getUserId());
			jo.put("userName", a.getUserName());
			jo.put("userFirstname", a.getUserFirstname());
			jo.put("userLastname", a.getUserLastname());
			jo.put("userEmail", a.getUserEmail());
			ja.put(jo);
		}
		return ja.toString();
	}
	
	@PostMapping("/updateAssignedToRole")
	public String updateAssignedToRole(@RequestParam("roleIds") int[] roleId,@RequestParam("userId") int userId) {
		if(userId<0) {
			return "User ID is not valid, it is: "+ userId;
		}else {
			Optional<User> optionalUser = userRepository.findById(userId);
			if(!optionalUser.isPresent()) {
				return "Cannot find UserID, it is: "+ userId;
			}else {
				User user = optionalUser.get();
				Set<Role> setRoles = user.getRoles();
				
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
				Set<Role> toDeleteRoleSet = new HashSet<Role>();
				for(Role r: setRoles) {
					if(!newRoleIds.contains(r.getRoleId())) {
						toDeleteRoleSet.add(r);
					}
				}
				setRoles.removeAll(toDeleteRoleSet);
				
				
				//add new Roles
				for(int id:roleId) {
					if(!oldRoleIds.contains(id)) {
						Optional<Role> optionalRole= roleRepository.findById(id);
						if(optionalRole.isPresent()) {
							setRoles.add(optionalRole.get());
						}
						
					}
				}
				
				userRepository.save(user);
				return"Roles are updated";
			}
		}
		
	}
	
	@PostMapping("/updateAssignedToGroup")
	public String updateAssignedToGroup(@RequestParam("groupIds") int[] groupId,@RequestParam("userId") int userId) {
		if(userId<0) {
			return "User ID is not valid, it is: "+ userId;
		}else {
			Optional<User> optionalUser = userRepository.findById(userId);
			if(!optionalUser.isPresent()) {
				return "Cannot find UserID, it is: "+ userId;
			}else {
				User user = optionalUser.get();
				Set<Usergroup> setGroups = user.getUsergroups();
				
				//create Set of roles
				Set<Integer> newGroupIds = new HashSet<Integer>();
				for(int id:groupId) {
					newGroupIds.add(id);
				}
				Set<Integer> oldGroupIds = new HashSet<Integer>();
				for(Usergroup g: setGroups) {
					oldGroupIds.add(g.getUsergroupId());
				}
				
				//check if there are roles that dont appier in the new setting
				Set<Usergroup> toDeleteUsergroupSet = new HashSet<Usergroup>();
				for(Usergroup g: setGroups) {
					if(!newGroupIds.contains(g.getUsergroupId())) {
						toDeleteUsergroupSet.add(g);
					}
				}
				setGroups.retainAll(toDeleteUsergroupSet);
				
				
				
				
				//add new Roles
				for(int id:groupId) {
					if(!oldGroupIds.contains(id)) {
						Optional<Usergroup> optionalGroup= groupRepository.findById(id);
						if(optionalGroup.isPresent()) {
							setGroups.add(optionalGroup.get());
						}
						
					}
				}
				
				userRepository.save(user);
				return"Groups are updated";
			}
		}
		
	}
	
	
	
	@PostMapping("/getRoleList")
	public String getRoleList(@RequestParam("searchString") String searchString,@RequestParam("userIdParameter") String userIdParameter) {
		List<Role> roleList;
		if(searchString == null || searchString.equals("")) {
			roleList = roleRepository.findAll();
		}else {
			roleList = roleRepository.findByContainsInName(searchString);
		}
		int userId = Integer.parseInt(userIdParameter);
		User user = null;
		if(userId!=-1) {
			Optional<User> userOpt = userRepository.findById(userId);
			if(userOpt.isPresent()) {
				user=userOpt.get();
			}
		}
		
		JSONArray ja = new JSONArray();
		for(Role r:roleList) {
			JSONObject jo = new JSONObject();
			jo.put("roleId", r.getRoleId());
			jo.put("roleName", r.getRoleName());
			jo.put("roleDescription", r.getRoleDescription());
			if(user==null) {
				jo.put("isAssignetTo", "null");
			}else{
				if(user.getRoles().contains(r)){
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
	public String getGroupList(@RequestParam("searchString") String searchString,@RequestParam("userIdParameter") String userIdParameter) {
		List<Usergroup> groupList;
		if(searchString == null || searchString.equals("")) {
			groupList = groupRepository.findAll();
		}else {
			groupList = groupRepository.findByContainsInName(searchString);
		}
		int userId = Integer.parseInt(userIdParameter);
		User user = null;
		if(userId!=-1) {
			Optional<User> userOpt = userRepository.findById(userId);
			if(userOpt.isPresent()) {
				user=userOpt.get();
			}
		}
		
		JSONArray ja = new JSONArray();
		for(Usergroup g:groupList) {
			JSONObject jo = new JSONObject();
			jo.put("groupId", g.getUsergroupId());
			jo.put("groupName", g.getUsergroupName());
			jo.put("groupDescription", g.getUsergroupDescription());
			if(user==null) {
				jo.put("isAssignetTo", "null");
			}else{
				if(user.getUsergroups().contains(g)){
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
		int id = Integer.parseInt(jobId);
		List<Scriptexecution> scriptList = scriptexecutionRepository.findByJobId(id);
		Job j = jobRepository.findById(id).get();
		if(scriptList.size()==0 && j.getUsers().size()==0) {
			jobRepository.deleteById(Integer.parseInt(jobId));
			return "The Job with id: " + jobId + " was deleted successfully";
		}else {
			return "There are other objects that depend on the job. <br>"
					+ "There are "+scriptList.size() +" ScriptExecutions and "+j.getUsers().size()+ " Users that depend on this job <br>"
					+ "If you really want to delete this job press here:<br>"
					+ "<button class=\"w3-button w3-red\" onclick=\"forceDeleteJob('"+jobId+"')\">force delete</button>";
		}
		
	}
	
	@PostMapping("/forceDeleteJob")
	public String forceDeleteJob(@RequestParam("jobId") String jobId) {
		int id = Integer.parseInt(jobId);
		List<Scriptexecution> scriptList = scriptexecutionRepository.findByJobId(id);
		Job j = jobRepository.findById(id).get();
		if(scriptList.size()==0 && j.getUsers().size()==0) {
			jobRepository.deleteById(Integer.parseInt(jobId));
			return "The Job with id: " + jobId + "was deleted successfully";
		}else {
			for(Scriptexecution se : scriptList) {
				if(se.getScriptexecutionExecutiondate()==null) {
					scriptexecutionRepository.delete(se);
				}else {
					se.setJob(null);
					se.setDescription("Assigned By [Job], but Job with id: "+jobId+" is deleted");
					scriptexecutionRepository.save(se);
				}
			}
			j.setUsers(new HashSet<User>());
			jobRepository.save(j);
			
			List<User> userList = userRepository.findByJobId(id);
			for(User a : userList) {
				a.deletejob(id);
				userRepository.save(a);
			}
			
			return deleteJob(jobId);
		}
		
	}
	
	
	@PostMapping("/updateAssignedToJob")
	public String updateAssignedToJob(@RequestParam("jobIds") int[] jobId,@RequestParam("userId") int userId) {
		if(userId<0) {
			return "User ID is not valid, it is: "+ userId;
		}else {
			Optional<User> optionalUser = userRepository.findById(userId);
			if(!optionalUser.isPresent()) {
				return "Cannot find UserID, it is: "+ userId;
			}else {
				User user = optionalUser.get();
				Set<Job> setJobs = user.getJobs();
				
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
				//System.out.println(Arrays.toString(setJobs.toArray()));
				
				Set<Job> toDeleteJobs = new HashSet<Job>();
				for(Job r: setJobs) {
					if(!newJobIds.contains(r.getJobId())) {
						toDeleteJobs.add(r);
					}
				}
				setJobs.removeAll(toDeleteJobs);
				
				
				//add new Jobs
				for(int id:jobId) {
					if(!oldJobIds.contains(id)) {
						Optional<Job> optionalJob= jobRepository.findById(id);
						if(optionalJob.isPresent()) {
							setJobs.add(optionalJob.get());
						}
						
					}
				}
				
				userRepository.save(user);
				return"Jobs are updated";
			}
		}
		
	}
	
	
	@PostMapping("/getJobList")
	public String getJobList(@RequestParam("searchString") String searchString,@RequestParam("userIdParameter") String userIdParameter) {
		List<Job> jobList;
		if(searchString == null || searchString.equals("")) {
			jobList = jobRepository.findAll();
		}else {
			jobList = jobRepository.findByContainsInName(searchString);
		}
		int userId = Integer.parseInt(userIdParameter);
		User user = null;
		if(userId!=-1) {
			Optional<User> userOpt = userRepository.findById(userId);
			if(userOpt.isPresent()) {
				user=userOpt.get();
			}
		}
		
		JSONArray ja = new JSONArray();
		for(Job r:jobList) {
			JSONObject jo = new JSONObject();
			jo.put("jobId", r.getJobId());
			jo.put("jobName", r.getJobName());
			jo.put("jobDescription", r.getJobDescription());
			if(user==null) {
				jo.put("isAssignetTo", "null");
			}else{
				if(user.getJobs().contains(r)){
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
	public String pushUserRegistration(@RequestParam("formVars") String[] formVars) {
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
		newTask.setTaskCreationdate(new Date());
		SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
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
		int id = Integer.parseInt(taskId);
		List<Scriptexecution> scriptList = scriptexecutionRepository.findByTaskId(id);
		Task j = taskRepository.findById(id).get();
		if(scriptList.size()==0 && j.getUsers().size()==0) {
			taskRepository.deleteById(Integer.parseInt(taskId));
			return "The Task with id: " + taskId + " was deleted successfully";
		}else {
			return "There are other objects that depend on the task. <br>"
					+ "There are "+scriptList.size() +" ScriptExecutions and "+j.getUsers().size()+ " Users that depend on this task <br>"
					+ "If you really want to delete this task press here:<br>"
					+ "<button class=\"w3-button w3-red\" onclick=\"forceDeleteTask('"+taskId+"')\">force delete</button>";
		}
		
	}
	
	@PostMapping("/forceDeleteTask")
	public String forceDeleteTask(@RequestParam("taskId") String taskId) {
		int id = Integer.parseInt(taskId);
		List<Scriptexecution> scriptList = scriptexecutionRepository.findByTaskId(id);
		Task j = taskRepository.findById(id).get();
		if(scriptList.size()==0 && j.getUsers().size()==0) {
			taskRepository.deleteById(Integer.parseInt(taskId));
			return "The Task with id: " + taskId + "was deleted successfully";
		}else {
			for(Scriptexecution se : scriptList) {
				if(se.getScriptexecutionExecutiondate()==null) {
					scriptexecutionRepository.delete(se);
				}else {
					se.setTask(null);
					se.setDescription("Assigned By [Task], but Task with id: "+taskId+" is deleted");
					scriptexecutionRepository.save(se);
				}
			}
			
			
			List<User> userList = userRepository.findByTaskId(id);
			for(User a : userList) {
				a.deleteTask(id);
				userRepository.save(a);
				System.out.println(Arrays.toString(a.getTasks().toArray()));
			}
			
			System.out.println(j.toString());
			j.setUsers(new HashSet<User>());
			taskRepository.save(j);
			System.out.println(j.toString());
			
			return deleteTask(taskId);
		}
	}
	
	@PostMapping("/updateAssignedToTask")
	public String updateAssignedToTask(@RequestParam("taskIds") int[] taskId,@RequestParam("userId") int userId) {
		if(userId<0) {
			return "User ID is not valid, it is: "+ userId;
		}else {
			Optional<User> optionalUser = userRepository.findById(userId);
			if(!optionalUser.isPresent()) {
				return "Cannot find UserID, it is: "+ userId;
			}else {
				User user = optionalUser.get();
				Set<Task> setTasks = user.getTasks();
				
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
				Set<Task> toDeleteTask = new HashSet<Task>();
				for(Task r: setTasks) {
					if(!newTaskIds.contains(r.getTaskId())) {
						toDeleteTask.add(r);
					}
				}
				setTasks.removeAll(toDeleteTask);
				
				
				//add new Tasks
				for(int id:taskId) {
					if(!oldTaskIds.contains(id)) {
						Optional<Task> optionalTask= taskRepository.findById(id);
						if(optionalTask.isPresent()) {
							setTasks.add(optionalTask.get());
						}
						
					}
				}
				
				userRepository.save(user);
				return"Tasks are updated";
			}
		}
		
	}
	
	
	@PostMapping("/getTaskList")
	public String getTaskList(@RequestParam("searchString") String searchString,@RequestParam("userIdParameter") String userIdParameter) {
		List<Task> taskList;
		if(searchString == null || searchString.equals("")) {
			taskList = taskRepository.findAll();
		}else {
			taskList = taskRepository.findByContainsInName(searchString);
		}
		int userId = Integer.parseInt(userIdParameter);
		User user = null;
		if(userId!=-1) {
			Optional<User> userOpt = userRepository.findById(userId);
			if(userOpt.isPresent()) {
				user=userOpt.get();
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
			if(user==null) {
				jo.put("isAssignetTo", "null");
			}else{
				if(user.getTasks().contains(r)){
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
		int id = Integer.parseInt(environmentId);
		List<Scriptexecution> scriptList = scriptexecutionRepository.findByEnvironmentId(id);
		Environment j = environmentRepository.findById(id).get();
		if(scriptList.size()==0 && j.getEnvironmentthreats().size()==0 && j.getRSCMClientConnections().size()==0) {
			environmentRepository.deleteById(Integer.parseInt(environmentId));
			return "The Environment with id: " + environmentId + " was deleted successfully";
		}else {
			return "There are other objects that depend on the environment. <br>"
					+ "There are "+scriptList.size() +" ScriptExecutions and <br>" +
					j.getEnvironmentthreats().size()+ " Environmentthreats and <br> "+
					j.getRSCMClientConnections().size() + " Connections depend on this environment  <br>"
					+ "If you really want to delete this environment press here:<br>"
					+ "<button class=\"w3-button w3-red\" onclick=\"forceDeleteEnvironment('"+environmentId+"')\">force delete</button>";
		}
		
	}
	
	@PostMapping("/forceDeleteEnvironment")
	public String forceDeleteEnvironment(@RequestParam("environmentId") String environmentId) {
		int id = Integer.parseInt(environmentId);
		List<Scriptexecution> scriptList = scriptexecutionRepository.findByEnvironmentId(id);
		Environment j = environmentRepository.findById(id).get();
		if(scriptList.size()==0 && j.getEnvironmentthreats().size()==0 && j.getRSCMClientConnections().size()==0) {
			environmentRepository.deleteById(Integer.parseInt(environmentId));
			return "The Environment with id: " + environmentId + "was deleted successfully";
		}else {
			//manage script dependencies
			for(Scriptexecution se : scriptList) {
				if(se.getScriptexecutionExecutiondate()==null) {
					scriptexecutionRepository.delete(se);
				}else {
					se.setEnvironment(null);
					se.setDescription("Assigned By [Environment], but Environment with id: "+environmentId+" is deleted");
					scriptexecutionRepository.save(se);
				}
			}
			//manage script Connection dependencies
			for(RSCMClientConnection c : j.getRSCMClientConnections()) {
				c.setEnvironment(null);
				c.setConnectionDescription("Environment now Deleted, connected from ["+j.getIpRangeBegin()+"] with id ["+j.getEnvironmentId()+"]");
				rscmClientConnectionRepository.save(c);
				
			}
			j.getRSCMClientConnections().removeAll(j.getRSCMClientConnections());
			environmentRepository.save(j);
			// manage Environmentthreat dependencies
			j.setEnvironmentthreats(new HashSet<Environmentthreat>());
			environmentRepository.save(j);
			
			List<Environmentthreat> environmentthreatList = environmentthreatRepository.findByEnvironmentId(id);
			for(Environmentthreat e : environmentthreatList) {
				e.deleteEnvironment(id);
				environmentthreatRepository.save(e);
			}
			
			return deleteEnvironment(environmentId);
		}
		
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
		int id = Integer.parseInt(environmentthreatId);
		List<Scriptexecution> scriptList = scriptexecutionRepository.findByEnvironmentthreatId(id);
		Environmentthreat j = environmentthreatRepository.findById(id).get();
		if(scriptList.size()==0 && j.getEnvironment().size()==0) {
			environmentthreatRepository.deleteById(Integer.parseInt(environmentthreatId));
			return "The Environmentthreat with id: " + environmentthreatId + " was deleted successfully";
		}else {
			return "There are other objects that depend on the environmentthreat. <br>"
					+ "There are "+scriptList.size() +" ScriptExecutions and "+j.getEnvironment().size()+ " Environments that depend on this environmentthreat <br>"
					+ "If you really want to delete this environmentthreat press here:<br>"
					+ "<button class=\"w3-button w3-red\" onclick=\"forceDeleteEnvironmentthreat('"+environmentthreatId+"')\">force delete</button>";
		}
		
	}
	
	@PostMapping("/forceDeleteEnvironmentthreat")
	public String forceDeleteEnvironmentthreat(@RequestParam("environmentthreatId") String environmentthreatId) {
		int id = Integer.parseInt(environmentthreatId);
		List<Scriptexecution> scriptList = scriptexecutionRepository.findByEnvironmentthreatId(id);
		Environmentthreat j = environmentthreatRepository.findById(id).get();
		if(scriptList.size()==0 && j.getEnvironment().size()==0) {
			environmentthreatRepository.deleteById(Integer.parseInt(environmentthreatId));
			return "The Environmentthreat with id: " + environmentthreatId + "was deleted successfully";
		}else {
			for(Scriptexecution se : scriptList) {
				if(se.getScriptexecutionExecutiondate()==null) {
					scriptexecutionRepository.delete(se);
				}else {
					se.setEnvironmentthreat(null);
					se.setDescription("Assigned By [Environmentthreat], but Environmentthreat with id: "+environmentthreatId+" is deleted");
					scriptexecutionRepository.save(se);
				}
			}
			
			j.getEnvironment().removeAll(j.getEnvironment());
			environmentthreatRepository.save(j);
			
			List<Environment> environmentList = environmentRepository.findByEnvironmentthreatId(id);
			for(Environment e : environmentList) {
				e.deleteenvironmentthreat(id);
				environmentRepository.save(e);
			}
			
			return deleteEnvironmentthreat(environmentthreatId);
		}
		
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
				Set<Environmentthreat> toDeleteEnvironmentthreats = new HashSet<Environmentthreat>();
				for(Environmentthreat r: setEnvironmentthreats) {
					if(!newEnvironmentthreatIds.contains(r.getEnvironmentthreatId())) {
						toDeleteEnvironmentthreats.add(r);
					}
				}
				setEnvironmentthreats.removeAll(toDeleteEnvironmentthreats);
				
				
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
