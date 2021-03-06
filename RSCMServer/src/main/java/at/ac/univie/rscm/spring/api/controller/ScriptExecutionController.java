package at.ac.univie.rscm.spring.api.controller;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.util.TypeKey;

import at.ac.univie.rscm.application.global.GlobalSettingsAndVariables;
import at.ac.univie.rscm.application.global.GlobalSettingsAndVariablesInterface;
import at.ac.univie.rscm.application.global.data.DownloadFileInfo;
import at.ac.univie.rscm.model.User;
import at.ac.univie.rscm.model.Usergroup;
import at.ac.univie.rscm.model.Environment;
import at.ac.univie.rscm.model.Environmentthreat;
import at.ac.univie.rscm.model.Job;
import at.ac.univie.rscm.model.RSCMClient;
import at.ac.univie.rscm.model.Role;
import at.ac.univie.rscm.model.Scriptexecution;
import at.ac.univie.rscm.model.Task;
import at.ac.univie.rscm.spring.api.repository.UserRepository;
import at.ac.univie.rscm.spring.api.repository.UsergroupRepository;
import at.ac.univie.rscm.spring.api.repository.EnvironmentRepository;
import at.ac.univie.rscm.spring.api.repository.EnvironmentthreatsRepository;
import at.ac.univie.rscm.spring.api.repository.JobRepository;
import at.ac.univie.rscm.spring.api.repository.RoleRepository;
import at.ac.univie.rscm.spring.api.repository.ScriptexecutionRepository;
import at.ac.univie.rscm.spring.api.repository.TaskReposiotry;


@RestController
@RequestMapping("/ScriptExecution/")
public class ScriptExecutionController {

	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UsergroupRepository usergroupRepository;
	
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
	
	
	private GlobalSettingsAndVariablesInterface gsav;
	
	
	
	public ScriptExecutionController() {
		gsav = GlobalSettingsAndVariables.getInstance();
	}

	
	
	@PostMapping("/getFileList")
	public String getFileList(@RequestParam("searchString") String searchString,@RequestParam("table") String table,@RequestParam("identifier") String identifier) {
		int id = Integer.parseInt(identifier);
		
		File f = new File(gsav.getFileDownloadDirectory()); // download directory
		//build json
		JSONArray ja = new JSONArray();
		
		File[] files = f.listFiles();
		for (File file : files) {
			if (!file.isDirectory()) {
				if (searchString.equals("") || file.getName().contains(searchString)) {
					try {
						// https://www.programcreek.com/java-api-examples/?class=java.nio.file.Files&method=readAttributes
						Path filePath = file.toPath();
						BasicFileAttributes attr = Files.readAttributes(filePath, BasicFileAttributes.class);
						JSONObject jo = new JSONObject();
						
						List<Scriptexecution> executionLog =null;
						int numberOfExecutions = -1;
						switch (table) {
						case "usergroup":
							executionLog = scriptexecutionRepository.findByUsergroup(id, file.getName());
							numberOfExecutions = scriptexecutionRepository.countByUsergroup(id, file.getName());
							break;
						case "rscmclient":
							executionLog = scriptexecutionRepository.findByRscmclient(id, file.getName());
							numberOfExecutions = scriptexecutionRepository.countByRscmclient(id, file.getName());
							break;	
						case "task":
							executionLog = scriptexecutionRepository.findByTasktask(id, file.getName());
							numberOfExecutions = scriptexecutionRepository.countByTasktask(id, file.getName());
							break;	
						case "environment":
							executionLog = scriptexecutionRepository.findByEnvironment(id, file.getName());
							numberOfExecutions = scriptexecutionRepository.countByEnvironment(id, file.getName());
							break;	
						case "environmentthreat":
							executionLog = scriptexecutionRepository.findByEnvironmentthreat(id, file.getName());
							numberOfExecutions = scriptexecutionRepository.countByEnvironmentthreat(id, file.getName());
							break;	
						case "job":
							executionLog = scriptexecutionRepository.findByJob(id, file.getName());
							numberOfExecutions = scriptexecutionRepository.countByJob(id, file.getName());
							break;	
						case "role":
							executionLog = scriptexecutionRepository.findByRole(id, file.getName());
							numberOfExecutions = scriptexecutionRepository.countByRole(id, file.getName());
							break;	
						case "user":
							executionLog = scriptexecutionRepository.findByUser(id, file.getName());
							numberOfExecutions = scriptexecutionRepository.countByUser(id, file.getName());
							break;	
						default:
							return null;
						}
						 
						//System.out.println(table + " " +id + " " + file.getName());
						if(executionLog.size()>0) {
							jo.put("executionAssignDate", gsav.getDateTime().format(executionLog.get(0).getScriptexecutionAssigneddate()));
							jo.put("executionPercentageNumbers", "(" +numberOfExecutions + "/"
									+ "" + executionLog.size() + ")");
							jo.put("executionPercentage", (((double)((double)(numberOfExecutions*1000) / (double)(executionLog.size())))/10)+"%");
						}else {
							jo.put("executionAssignDate", "Not Assigned");
							jo.put("executionPercentageNumbers", "");
							jo.put("executionPercentage", "");
						}
						
						
						jo.put("fileName", file.getName());
						jo.put("fileCreationDate", gsav.getDateTime().format(new Date(attr.creationTime().toMillis())));
						jo.put("fileDownloadPath", file.getCanonicalPath());
						jo.put("fileSize", attr.size() + "<br>Byte");
						ja.put(jo);
						
					} catch (IOException e) {
						//file not exists should not happen, because we look for it in this function
						e.printStackTrace();
					}
				}

			}

		}

		return ja.toString();
	}
	
	
	
	@PostMapping("/removeAssignment")
	public String removeAssignment(@RequestParam("uncheckedFileNameAssignments") String[] uncheckedFileNameAssignments,@RequestParam("entityID") int entityID,@RequestParam("table") String table) {
		Set<Scriptexecution> forRemoveList = new HashSet<Scriptexecution>();
		switch (table) {
		case "usergroup":
			for(String fileName: uncheckedFileNameAssignments) {
				forRemoveList.addAll(scriptexecutionRepository.getAssignedExecutionBasedOnUsergroup(fileName, entityID));
			}
			break;
		case "task":
			for(String fileName: uncheckedFileNameAssignments) {
				forRemoveList.addAll(scriptexecutionRepository.getAssignedExecutionBasedOnTask(fileName, entityID));
			}
			break;	
		case "environment":
			for(String fileName: uncheckedFileNameAssignments) {
				forRemoveList.addAll(scriptexecutionRepository.getAssignedExecutionBasedOnEnvironment(fileName, entityID));
			}
			break;	
		case "environmentthreat":
			for(String fileName: uncheckedFileNameAssignments) {
				forRemoveList.addAll(scriptexecutionRepository.getAssignedExecutionBasedOnEnvironmentthreat(fileName, entityID));
			}
			break;	
		case "job":
			for(String fileName: uncheckedFileNameAssignments) {
				forRemoveList.addAll(scriptexecutionRepository.getAssignedExecutionBasedOnJob(fileName, entityID));
			}
			break;	
		case "role":
			for(String fileName: uncheckedFileNameAssignments) {
				forRemoveList.addAll(scriptexecutionRepository.getAssignedExecutionBasedOnRole(fileName, entityID));
			}
			break;	
		case "user":
			for(String fileName: uncheckedFileNameAssignments) {
				forRemoveList.addAll(scriptexecutionRepository.getAssignedExecutionBasedOnUser(fileName, entityID));
			}
			break;	
		default:
			return null;
		}
		scriptexecutionRepository.deleteAll(forRemoveList);
		return forRemoveList.size() + " Scriptassignments are successfully removed.";

			
	}
	
	@PostMapping("/showExecutionDetails")
	public String showExecutionDetails(@RequestParam("entityID") String entityID,@RequestParam("table") String table,@RequestParam("fileName") String fileName) {
		int id = Integer.parseInt(entityID);
		try {
			File f = new File(gsav.getFileDownloadDirectory()); // download directory
			//build json
			
			
			// https://www.programcreek.com/java-api-examples/?class=java.nio.file.Files&method=readAttributes
			File file = new File(gsav.getFileDownloadDirectory()+"/"+fileName);
			Path filePath = file.toPath();
			BasicFileAttributes attr;
			
				attr = Files.readAttributes(filePath, BasicFileAttributes.class);
			
			
			List<Scriptexecution> executionLog =null;
			int numberOfExecutions = -1;
			String entityDetails="";
			
			switch (table) {
			case "usergroup":
				executionLog = scriptexecutionRepository.findByUsergroup(id, file.getName());
				numberOfExecutions = scriptexecutionRepository.countByUsergroup(id, file.getName());
				entityDetails = usergroupRepository.findById(id).get().toString();
				break;
			case "rscmclient":
				executionLog = scriptexecutionRepository.findByRscmclient(id, file.getName());
				numberOfExecutions = scriptexecutionRepository.countByRscmclient(id, file.getName());
				entityDetails = roleRepository.findById(id).get().toString();
				break;	
			case "task":
				executionLog = scriptexecutionRepository.findByTasktask(id, file.getName());
				numberOfExecutions = scriptexecutionRepository.countByTasktask(id, file.getName());
				entityDetails = taskRepository.findById(id).get().toString();
				break;	
			case "environment":
				executionLog = scriptexecutionRepository.findByEnvironment(id, file.getName());
				numberOfExecutions = scriptexecutionRepository.countByEnvironment(id, file.getName());
				entityDetails = environmentRepository.findById(id).get().toString();
				break;	
			case "environmentthreat":
				executionLog = scriptexecutionRepository.findByEnvironmentthreat(id, file.getName());
				numberOfExecutions = scriptexecutionRepository.countByEnvironmentthreat(id, file.getName());
				entityDetails = environmentthreatRepository.findById(id).get().toString();
				break;	
			case "job":
				executionLog = scriptexecutionRepository.findByJob(id, file.getName());
				numberOfExecutions = scriptexecutionRepository.countByJob(id, file.getName());
				entityDetails = jobRepository.findById(id).get().toString();
				break;	
			case "role":
				executionLog = scriptexecutionRepository.findByRole(id, file.getName());
				numberOfExecutions = scriptexecutionRepository.countByRole(id, file.getName());
				entityDetails = roleRepository.findById(id).get().toString();
				break;	
			case "user":
				executionLog = scriptexecutionRepository.findByUser(id, file.getName());
				numberOfExecutions = scriptexecutionRepository.countByUser(id, file.getName());
				entityDetails = userRepository.findById(id).get().toString();
				break;	
			default:
				return null;
			}

			
			JSONObject parent = new JSONObject();
			parent.put("entityDetails", entityDetails);
			parent.put("fileName", file.getName());
			parent.put("fileCreationDate", gsav.getDateTime().format(new Date(attr.creationTime().toMillis())));
			parent.put("fileDownloadPath", file.getCanonicalPath());
			parent.put("fileSize", attr.size() + " Byte");
			if(executionLog.size()>0) {
				parent.put("executionAssignDate", gsav.getDateTime().format(executionLog.get(0).getScriptexecutionAssigneddate()));
				parent.put("executionPercentageNumbers", "(" +numberOfExecutions + "/"	+ "" + executionLog.size() + ")");
				parent.put("executionPercentage", (((double)((double)(numberOfExecutions*1000) / (double)(executionLog.size())))/10)+"%");
			}else {
				parent.put("executionAssignDate", "Not Assigned");
				parent.put("executionPercentageNumbers", "");
				parent.put("executionPercentage", "");
			}
			JSONArray ja = new JSONArray();
			for(Scriptexecution se: executionLog) {
				JSONObject jo = new JSONObject();
				jo.put("scriptexecutionId", se.getScriptexecutionId());
				jo.put("scriptexecutionAssigneddate", gsav.getDateTime().format(se.getScriptexecutionAssigneddate()));
				if(se.getScriptexecutionExecutiondate()!=null) {
					jo.put("scriptexecutionExecutiondate", gsav.getDateTime().format(se.getScriptexecutionExecutiondate()));
				}else {
					jo.put("scriptexecutionExecutiondate", "not executed");
				}
				
				RSCMClient rscmClient = se.getRSCMClient();
				if(rscmClient!=null) {
					jo.put("rscmclientId", rscmClient.getRscmclientId());
					jo.put("createdOn", gsav.getDateTime().format(rscmClient.getCreatedOn()));
					jo.put("clientPort", rscmClient.getClientPort());
					jo.put("executed", rscmClient.getClientPort());				
					User user = null;
					if(rscmClient.getUsers().iterator().hasNext()) {
						user = rscmClient.getUsers().iterator().next();
						jo.put("userId", user.getUserId());
						jo.put("userName", user.getUserName());
						jo.put("userEmail", user.getUserEmail());
						jo.put("userFirstname", user.getUserFirstname());
						jo.put("userLastname", user.getUserLastname());
					}else {
						jo.put("userId", "null");
						jo.put("userName", "null");
						jo.put("userEmail", "null");
						jo.put("userFirstname", "null");
						jo.put("userLastname", "null");
					}
				}else {
					jo.put("rscmclientId", "null");
					jo.put("createdOn", "null");
					jo.put("clientPort", "null");
					jo.put("userId", "null");
					jo.put("userName", "null");
					jo.put("userEmail", "null");
					jo.put("userFirstname", "null");
					jo.put("userLastname", "null");
				
				}
				
				
				
				HashMap<String, Set<Integer>> assignedBy = new HashMap<String, Set<Integer>>();

				Method[] methods = Scriptexecution.class.getMethods();
				
				int rscmClientId=-1;
				if(rscmClient!=null) {
					rscmClientId= rscmClient.getRscmclientId();
				}
				List<Scriptexecution> allAssignments= scriptexecutionRepository.findByAllFileAssignByClient(fileName, rscmClientId);
				for(Scriptexecution seAllAsignment: allAssignments) {
					for (Method m : methods) {
						if(m.getName().contains("_fs") && m.getName().contains("get")) {
							String cleanedMethodName = m.getName().substring(3, m.getName().length()-3);
							
							try {
								Integer localTempId = (Integer)m.invoke(seAllAsignment, null);
								if(localTempId!= null) {
									if(!assignedBy.containsKey(cleanedMethodName)){
										assignedBy.put(cleanedMethodName, new HashSet<Integer>());
									}
									assignedBy.get(cleanedMethodName).add(localTempId);
								}
								
							} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}
				
				Set<String> keySet = assignedBy.keySet();
				StringBuilder sb = new StringBuilder();
				int j=0;
				for(String s: keySet) {
					sb.append(s + " [");
					Set<Integer> localIdSet = assignedBy.get(s);
					int i=0;
					for(int localId: localIdSet) {
						sb.append(localId);
						if(i<(localIdSet.size()-1)) {
							sb.append(",");
						}
						i++;
					}
					sb.append("]");
					if(j<(keySet.size()-1)) {
						sb.append(", ");
					}
					j++;
				}
				
				jo.put("Assignedby", sb.toString());
				ja.put(jo);
				
				
			}
			parent.put("ClientArray",ja);
			
		

		

		return parent.toString();
		} catch (IOException e) {
			JSONObject parent = new JSONObject();
			parent.put("fileName", "File Not Found");
			return parent.toString();
		}
	}
	
	@PostMapping("/getUsergroupList")
	public String getUsergroupList(@RequestParam("searchString") String searchString) {
		
		//get DataList depending on Search String if "" than get all
		List<Usergroup> usergroupList = usergroupRepository.findByContainsInName(searchString);
		
		//build json
		JSONArray ja = new JSONArray();
		for(Usergroup g:usergroupList) {
			JSONObject jo = new JSONObject();
			jo.put("usergroupId", g.getUsergroupId());
			jo.put("usergroupName", g.getUsergroupName());
			jo.put("usergroupDescription", g.getUsergroupDescription());
			jo.put("usergroupMembersUser", g.getUsers().size());
			int countRSCMClients=0;
			for(User a : g.getUsers()) {
				countRSCMClients+=a.getRscmclients().size();
			}
			jo.put("usergroupMembersRSCMClients", countRSCMClients);
			ja.put(jo);
		}
		return ja.toString();
	}
	
	@PostMapping("/updateUsergroupAssignment")
	public String updateUsergroupAssignment(@RequestParam("entityAssignment") int[] entityAssignment,@RequestParam("fileAssignment") String[] fileAssignment) {
		
		if(entityAssignment.length<1 || fileAssignment.length <1) {
			return "The Assignment is only possible when you choose at least one usergroup and one file";
		}
		assert(entityAssignment.length>0);
		assert(fileAssignment.length>0);
		
		int countUsers=0;
		int countClients=0;
		int countExsisting=0;
		for(int usergroupId: entityAssignment) {
			for(String fileName:fileAssignment) {
				Usergroup usergroup = usergroupRepository.findById(usergroupId).get();
				Set<User> userSet = usergroup.getUsers();
				for(User a: userSet) {
					Set<RSCMClient> rscmClients = a.getRscmclients();
					countUsers++;
					for(RSCMClient rscmClient: rscmClients) {
						List<Scriptexecution> seList = scriptexecutionRepository.getExsistingExecutionPlanBasedOnUsergroup(fileName, rscmClient.getRscmclientId(),usergroupId);
						if(seList.size()>0) {
							for(Scriptexecution seExsisting: seList) {
								seExsisting.setScriptexecutionAssigneddate(new Date());
								seExsisting.setUsergroup(usergroup);
								countExsisting++;
							}
						}else {
							Scriptexecution se = new Scriptexecution();
							se.setUsergroup(usergroupRepository.findById(usergroupId).get());
							se.setScriptName(fileName);
							se.setScriptexecutionAssigneddate(new Date());
							se.setRSCMClient(rscmClient);
							countClients++;
							scriptexecutionRepository.save(se);
							gsav.getPortScanner().scriptsAssignmentHasChaneged();
						}
						
						
					}
				}
			}
		}
		return "the script assignment effects "+countUsers+" users,<br> and " + countClients +" clients has an new script assignment, <br> and " + countExsisting + " existing script assignments are updated";
		
	}
	
	@PostMapping("/getTaskList")
	public String getTaskList(@RequestParam("searchString") String searchString) {
		
		//get DataList depending on Search String if "" than get all
		List<Task> taskList = taskRepository.findByContainsInName(searchString);
		
		//build json
		JSONArray ja = new JSONArray();
		for(Task g:taskList) {
			JSONObject jo = new JSONObject();
			jo.put("taskId", g.getTaskId());
			jo.put("taskName", g.getTaskName());
			jo.put("taskDescription", g.getTaskDescription());
			jo.put("taskOutcome", g.getTaskOutcome());
			if(g.getTaskCreationdate()!=null) {
				jo.put("taskCreationdate", gsav.getDateTime().format(g.getTaskCreationdate()));
			}else {
				jo.put("taskCreationdate", "null");
			}
			if(g.getTaskPlanBegindate()!=null) {
				jo.put("taskPlanBegindate", gsav.getDateTime().format(g.getTaskPlanBegindate()));
			}else {
				jo.put("taskPlanBegindate", "null");
			}
			if(g.getTaskPlanEnddate()!=null) {
				jo.put("taskPlanEnddate", gsav.getDateTime().format(g.getTaskPlanEnddate()));
			}else {
				jo.put("taskPlanEnddate", "null");
			}
			
			
			jo.put("taskMembersUser", g.getUsers().size());
			int countRSCMClients=0;
			for(User a : g.getUsers()) {
				countRSCMClients+=a.getRscmclients().size();
			}
			jo.put("taskMembersRSCMClients", countRSCMClients);
			ja.put(jo);
		}
		return ja.toString();
	}
	
	
	@PostMapping("/updateTaskAssignment")
	public String updateTaskAssignment(@RequestParam("entityAssignment") int[] entityAssignment,@RequestParam("fileAssignment") String[] fileAssignment) {
		if(entityAssignment.length<1 || fileAssignment.length <1) {
			return "The Assignment is only possible when you choose at least one task and one file";
		}
		assert(entityAssignment.length>0);
		assert(fileAssignment.length>0);
		
		int countUsers=0;
		int countClients=0;
		int countExsisting=0;
		for(int taskId: entityAssignment) {
			for(String fileName:fileAssignment) {
				Task task = taskRepository.findById(taskId).get();
				Set<User> userSet = task.getUsers();
				for(User a: userSet) {
					Set<RSCMClient> rscmClients = a.getRscmclients();
					countUsers++;
					for(RSCMClient rscmClient: rscmClients) {
						List<Scriptexecution> seList = scriptexecutionRepository.getExsistingExecutionPlanBasedOnTask(fileName, rscmClient.getRscmclientId(),taskId);
						if(seList.size()>0) {
							for(Scriptexecution seExsisting: seList) {
								seExsisting.setScriptexecutionAssigneddate(new Date());
								seExsisting.setTask(task);
								countExsisting++;
							}
						}else {
							Scriptexecution se = new Scriptexecution();
							se.setTask(taskRepository.findById(taskId).get());
							se.setScriptName(fileName);
							se.setScriptexecutionAssigneddate(new Date());
							se.setRSCMClient(rscmClient);
							countClients++;
							scriptexecutionRepository.save(se);
							gsav.getPortScanner().scriptsAssignmentHasChaneged();
						}
						
						
					}
				}
			}
		}
		return "the script assignment effects "+countUsers+" users,<br> and " + countClients +" clients has an new script assignment, <br> and " + countExsisting + " existing script assignments are updated";
		
	}
	

	@PostMapping("/getRoleList")
	public String getRoleList(@RequestParam("searchString") String searchString) {
		
		//get DataList depending on Search String if "" than get all
		List<Role> roleList = roleRepository.findByContainsInName(searchString);
		
		//build json
		JSONArray ja = new JSONArray();
		for(Role g:roleList) {
			JSONObject jo = new JSONObject();
			jo.put("roleId", g.getRoleId());
			jo.put("roleName", g.getRoleName());
			jo.put("roleDescription", g.getRoleDescription());
			jo.put("roleMembersUser", g.getUsers().size());
			int countRSCMClients=0;
			for(User a : g.getUsers()) {
				countRSCMClients+=a.getRscmclients().size();
			}
			jo.put("roleMembersRSCMClients", countRSCMClients);
			ja.put(jo);
		}
		return ja.toString();
	}
	
	
	@PostMapping("/updateRoleAssignment")
	public String updateRoleAssignment(@RequestParam("entityAssignment") int[] entityAssignment,@RequestParam("fileAssignment") String[] fileAssignment) {
		if(entityAssignment.length<1 || fileAssignment.length <1) {
			return "The Assignment is only possible when you choose at least one role and one file";
		}
		assert(entityAssignment.length>0);
		assert(fileAssignment.length>0);
		
		int countUsers=0;
		int countClients=0;
		int countExsisting=0;
		for(int roleId: entityAssignment) {
			for(String fileName:fileAssignment) {
				Role role = roleRepository.findById(roleId).get();
				Set<User> userSet = role.getUsers();
				for(User a: userSet) {
					Set<RSCMClient> rscmClients = a.getRscmclients();
					countUsers++;
					for(RSCMClient rscmClient: rscmClients) {
						List<Scriptexecution> seList = scriptexecutionRepository.getExsistingExecutionPlanBasedOnRole(fileName, rscmClient.getRscmclientId(),roleId);
						if(seList.size()>0) {
							for(Scriptexecution seExsisting: seList) {
								seExsisting.setScriptexecutionAssigneddate(new Date());
								seExsisting.setRole(role);
								countExsisting++;
							}
						}else {
							Scriptexecution se = new Scriptexecution();
							se.setRole(roleRepository.findById(roleId).get());
							se.setScriptName(fileName);
							se.setScriptexecutionAssigneddate(new Date());
							se.setRSCMClient(rscmClient);
							countClients++;
							scriptexecutionRepository.save(se);
							gsav.getPortScanner().scriptsAssignmentHasChaneged();
						}
						
						
					}
				}
			}
		}
		return "the script assignment effects "+countUsers+" users,<br> and " + countClients +" clients has an new script assignment, <br> and " + countExsisting + " existing script assignments are updated";
		
	}
	

	@PostMapping("/getJobList")
	public String getJobList(@RequestParam("searchString") String searchString) {
		
		//get DataList depending on Search String if "" than get all
		List<Job> jobList = jobRepository.findByContainsInName(searchString);
		
		//build json
		JSONArray ja = new JSONArray();
		for(Job g:jobList) {
			JSONObject jo = new JSONObject();
			jo.put("jobId", g.getJobId());
			jo.put("jobName", g.getJobName());
			jo.put("jobDescription", g.getJobDescription());
			jo.put("jobMembersUser", g.getUsers().size());
			int countRSCMClients=0;
			for(User a : g.getUsers()) {
				countRSCMClients+=a.getRscmclients().size();
			}
			jo.put("jobMembersRSCMClients", countRSCMClients);
			ja.put(jo);
		}
		return ja.toString();
	}
	
	
	@PostMapping("/updateJobAssignment")
	public String updateJobAssignment(@RequestParam("entityAssignment") int[] entityAssignment,@RequestParam("fileAssignment") String[] fileAssignment) {
		if(entityAssignment.length<1 || fileAssignment.length <1) {
			return "The Assignment is only possible when you choose at least one job and one file";
		}
		assert(entityAssignment.length>0);
		assert(fileAssignment.length>0);
		
		int countUsers=0;
		int countClients=0;
		int countExsisting=0;
		for(int jobId: entityAssignment) {
			for(String fileName:fileAssignment) {
				Job job = jobRepository.findById(jobId).get();
				Set<User> userSet = job.getUsers();
				for(User a: userSet) {
					Set<RSCMClient> rscmClients = a.getRscmclients();
					countUsers++;
					for(RSCMClient rscmClient: rscmClients) {
						List<Scriptexecution> seList = scriptexecutionRepository.getExsistingExecutionPlanBasedOnJob(fileName, rscmClient.getRscmclientId(),jobId);
						if(seList.size()>0) {
							for(Scriptexecution seExsisting: seList) {
								seExsisting.setScriptexecutionAssigneddate(new Date());
								seExsisting.setJob(job);
								countExsisting++;
							}
						}else {
							//System.out.println(jobId + "sdssd");
							Scriptexecution se = new Scriptexecution();
							se.setJob(jobRepository.findById(jobId).get());
							se.setScriptName(fileName);
							se.setScriptexecutionAssigneddate(new Date());
							se.setRSCMClient(rscmClient);
							countClients++;
							scriptexecutionRepository.save(se);
							gsav.getPortScanner().scriptsAssignmentHasChaneged();
						}
						
						
					}
				}
			}
		}
		return "the script assignment effects "+countUsers+" users,<br> and " + countClients +" clients has an new script assignment, <br> and " + countExsisting + " existing script assignments are updated";
		
	}
	
	@PostMapping("/getUserList")
	public String getUserList(@RequestParam("searchString") String searchString) {
		
		//get DataList depending on Search String if "" than get all
		List<User> userList = userRepository.findByContainsInName(searchString);
		
		//build json
		JSONArray ja = new JSONArray();
		for(User g:userList) {
			JSONObject jo = new JSONObject();
			jo.put("userId", g.getUserId());
			jo.put("userName", g.getUserName());
			jo.put("userFirstname", g.getUserFirstname());
			jo.put("userLastname", g.getUserLastname());
			jo.put("userEmail", g.getUserEmail());
			jo.put("userMembersRSCMClients", g.getRscmclients().size());
			ja.put(jo);
		}
		return ja.toString();
	}
	
	
	@PostMapping("/updateUserAssignment")
	public String updateUserAssignment(@RequestParam("entityAssignment") int[] entityAssignment,@RequestParam("fileAssignment") String[] fileAssignment) {
		if(entityAssignment.length<1 || fileAssignment.length <1) {
			return "The Assignment is only possible when you choose at least one user and one file";
		}
		assert(entityAssignment.length>0);
		assert(fileAssignment.length>0);
		
		int countUsers=0;
		int countClients=0;
		int countExsisting=0;
		for(int userId: entityAssignment) {
			for(String fileName:fileAssignment) {
				User user = userRepository.findById(userId).get();
				Set<User> userSet = new HashSet<User>();
				userSet.add(user);
				for(User a: userSet) {
					Set<RSCMClient> rscmClients = a.getRscmclients();
					countUsers++;
					for(RSCMClient rscmClient: rscmClients) {
						List<Scriptexecution> seList = scriptexecutionRepository.getExsistingExecutionPlanBasedOnUser(fileName, rscmClient.getRscmclientId(),userId);
						if(seList.size()>0) {
							for(Scriptexecution seExsisting: seList) {
								seExsisting.setScriptexecutionAssigneddate(new Date());
								seExsisting.setUser(user);
								countExsisting++;
							}
						}else {
							Scriptexecution se = new Scriptexecution();
							se.setUser(userRepository.findById(userId).get());
							se.setScriptName(fileName);
							se.setScriptexecutionAssigneddate(new Date());
							se.setRSCMClient(rscmClient);
							countClients++;
							scriptexecutionRepository.save(se);
							gsav.getPortScanner().scriptsAssignmentHasChaneged();
						}
						
						
					}
				}
			}
		}
		return "the script assignment effects "+countUsers+" users,<br> and " + countClients +" clients has an new script assignment, <br> and " + countExsisting + " existing script assignments are updated";
		
	}
	
	@PostMapping("/getEnvironmentList")
	public String getEnvironmentList(@RequestParam("searchString") String searchString) {
		
		//get DataList depending on Search String if "" than get all
		List<Environment> environmentList = environmentRepository.findByContainsInIp(searchString);
		
		//build json
		JSONArray ja = new JSONArray();
		for(Environment g:environmentList) {
			JSONObject jo = new JSONObject();
			jo.put("environmentId", g.getEnvironmentId());
			jo.put("environmentIpRangeBegin", g.getIpRangeBegin());
			jo.put("environmentRangeEnd", g.getIpRangeEnd());
			jo.put("environmentDescription", g.getEnvironmentDescription());
			jo.put("environmentConnections", g.getRSCMClientConnections().size());
			jo.put("environmentEnvironmentthreats", g.getEnvironmentthreats().size());
			ja.put(jo);
		}
		return ja.toString();
	}
	
	
	@PostMapping("/updateEnvironmentAssignment")
	public String updateEnvironmentAssignment(@RequestParam("entityAssignment") int[] entityAssignment,@RequestParam("fileAssignment") String[] fileAssignment) {
		if(entityAssignment.length<1 || fileAssignment.length <1) {
			return "The Assignment is only possible when you choose at least one environment and one file";
		}
		assert(entityAssignment.length>0);
		assert(fileAssignment.length>0);
		
		int countUsers=0;
		int countClients=0;
		int countExsisting=0;
		for(int environmentId: entityAssignment) {
			for(String fileName:fileAssignment) {
				Environment environment = environmentRepository.findById(environmentId).get();
				
				List<Scriptexecution> seList = scriptexecutionRepository.getExsistingExecutionPlanBasedOnEnvironment(fileName, environmentId);
				if(seList.size()>0) {
					for(Scriptexecution seExsisting: seList) {
						seExsisting.setScriptexecutionAssigneddate(new Date());
						seExsisting.setEnvironment(environment);
						countExsisting++;
					}
				}else {
					Scriptexecution se = new Scriptexecution();
					se.setEnvironment(environmentRepository.findById(environmentId).get());
					se.setScriptName(fileName);
					se.setScriptexecutionAssigneddate(new Date());
					countClients++;
					System.out.println("asdöasdasldasdasldlsdlsdl");
					scriptexecutionRepository.save(se);
					gsav.getPortScanner().scriptsAssignmentHasChaneged();
				}
				
				
			}
		}
		return "the script assignment effects "+countUsers+" users,<br> and " + countClients +" clients has an new script assignment, <br> and " + countExsisting + " existing script assignments are updated";
		
	}
	
	@PostMapping("/getEnvironmentthreatList")
	public String getEnvironmentthreatList(@RequestParam("searchString") String searchString) {
		
		//get DataList depending on Search String if "" than get all
		List<Environmentthreat> environmentthreatList = environmentthreatRepository.findByContainsInTitle(searchString);
		
		//build json
		JSONArray ja = new JSONArray();
		for(Environmentthreat g:environmentthreatList) {
			JSONObject jo = new JSONObject();
			jo.put("environmentthreatId", g.getEnvironmentthreatId());
			jo.put("environmentthreatTitle", g.getThreatTitle());
			jo.put("environmentthreatDescription", g.getThreatDescription());
			jo.put("environmentthreatExpectedProblem", g.getExpectedProblem());
			jo.put("environmentthreatThreatLevel", g.getThreatLevel());
			jo.put("environmentthreatEnvironments", g.getEnvironment().size());
			ja.put(jo);
		}
		return ja.toString();
	}
	
	
	@PostMapping("/updateEnvironmentthreatAssignment")
	public String updateEnvironmentthreatAssignment(@RequestParam("entityAssignment") int[] entityAssignment,@RequestParam("fileAssignment") String[] fileAssignment) {
		if(entityAssignment.length<1 || fileAssignment.length <1) {
			return "The Assignment is only possible when you choose at least one environmentthreat and one file";
		}
		assert(entityAssignment.length>0);
		assert(fileAssignment.length>0);
		
		int countUsers=0;
		int countClients=0;
		int countExsisting=0;
		for(int environmentthreatId: entityAssignment) {
			for(String fileName:fileAssignment) {
				Environmentthreat environmentthreat = environmentthreatRepository.findById(environmentthreatId).get();
				
				List<Scriptexecution> seList = scriptexecutionRepository.getExsistingExecutionPlanBasedOnEnvironmentthreat(fileName, environmentthreatId);
				if(seList.size()>0) {
					for(Scriptexecution seExsisting: seList) {
						seExsisting.setScriptexecutionAssigneddate(new Date());
						seExsisting.setEnvironmentthreat(environmentthreat);
						countExsisting++;
					}
				}else {
					Scriptexecution se = new Scriptexecution();
					se.setEnvironmentthreat(environmentthreatRepository.findById(environmentthreatId).get());
					se.setScriptName(fileName);
					se.setScriptexecutionAssigneddate(new Date());
					countClients++;
					scriptexecutionRepository.save(se);
					gsav.getPortScanner().scriptsAssignmentHasChaneged();
				}
				
				
			}
		}
		return "the script assignment effects "+countUsers+" users,<br> and " + countClients +" clients has an new script assignment, <br> and " + countExsisting + " existing script assignments are updated";
		
	}
	
	
}
