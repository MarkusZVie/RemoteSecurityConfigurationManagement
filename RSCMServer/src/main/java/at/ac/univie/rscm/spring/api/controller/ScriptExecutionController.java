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
import at.ac.univie.rscm.model.Applicant;
import at.ac.univie.rscm.model.Applicantgroup;
import at.ac.univie.rscm.model.RSCMClient;
import at.ac.univie.rscm.model.Scriptexecution;
import at.ac.univie.rscm.spring.api.repository.ApplicantRepository;
import at.ac.univie.rscm.spring.api.repository.ApplicantgroupRepository;
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
	private ApplicantRepository applicantRepository;
	
	@Autowired
	private ApplicantgroupRepository applicantgroupRepository;
	
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

	@PostMapping("/getApplicantgroupList")
	public String getApplicantgroupList(@RequestParam("searchString") String searchString) {
		
		//get DataList depending on Search String if "" than get all
		List<Applicantgroup> applicantgroupList = applicantgroupRepository.findByContainsInName(searchString);
		
		//build json
		JSONArray ja = new JSONArray();
		for(Applicantgroup g:applicantgroupList) {
			JSONObject jo = new JSONObject();
			jo.put("applicantgroupId", g.getApplicantgroupId());
			jo.put("applicantgroupName", g.getApplicantgroupName());
			jo.put("applicantgroupDescription", g.getApplicantgroupDescription());
			jo.put("applicantgroupMembersApplicant", g.getApplicants().size());
			int countRSCMClients=0;
			for(Applicant a : g.getApplicants()) {
				countRSCMClients+=a.getRscmclients().size();
			}
			jo.put("applicantgroupMembersRSCMClients", countRSCMClients);
			ja.put(jo);
		}
		return ja.toString();
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
						case "applicantgroup":
							executionLog = scriptexecutionRepository.findByApplicantgroup(id, file.getName());
							numberOfExecutions = scriptexecutionRepository.countByApplicantgroup(id, file.getName());
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
						case "applicant":
							executionLog = scriptexecutionRepository.findByApplicant(id, file.getName());
							numberOfExecutions = scriptexecutionRepository.countByApplicant(id, file.getName());
							break;	
						default:
							return null;
						}
						 
						System.out.println(table + " " +id + " " + file.getName());
						if(executionLog.size()>0) {
							jo.put("executionAssignDate", gsav.getDateTime().format(executionLog.get(0).getScriptexecutionAssigneddate()));
							jo.put("executionPercentageNumbers", "(" +numberOfExecutions + "/"
									+ "" + executionLog.size() + ")");
							jo.put("executionPercentage", (((double)((numberOfExecutions*100) / (executionLog.size()*100)))/100)+"%");
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
			case "applicantgroup":
				executionLog = scriptexecutionRepository.findByApplicantgroup(id, file.getName());
				numberOfExecutions = scriptexecutionRepository.countByApplicantgroup(id, file.getName());
				entityDetails = applicantgroupRepository.findById(id).get().toString();
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
			case "applicant":
				executionLog = scriptexecutionRepository.findByApplicant(id, file.getName());
				numberOfExecutions = scriptexecutionRepository.countByApplicant(id, file.getName());
				entityDetails = applicantRepository.findById(id).get().toString();
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
				parent.put("executionPercentageNumbers", "(" +numberOfExecutions + "/"
						+ "" + executionLog.size() + ")");
				parent.put("executionPercentage", (((double)((numberOfExecutions*100) / (executionLog.size()*100)))/100)+"%");
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
				jo.put("rscmclientId", rscmClient.getRscmclientId());
				jo.put("createdOn", gsav.getDateTime().format(rscmClient.getCreatedOn()));
				jo.put("clientPort", rscmClient.getClientPort());
				Applicant applicant = null;
				if(rscmClient.getApplicants().iterator().hasNext()) {
					applicant = rscmClient.getApplicants().iterator().next();
					jo.put("applicantId", applicant.getApplicantId());
					jo.put("applicantName", applicant.getApplicantName());
					jo.put("applicantEmail", applicant.getApplicantEmail());
					jo.put("applicantFirstname", applicant.getApplicantFirstname());
					jo.put("applicantLastname", applicant.getApplicantLastname());
				}else {
					jo.put("applicantId", "null");
					jo.put("applicantName", "null");
					jo.put("applicantEmail", "null");
					jo.put("applicantFirstname", "null");
					jo.put("applicantLastname", "null");
				}
				HashMap<String, Set<Integer>> assignedBy = new HashMap<String, Set<Integer>>();

				Method[] methods = Scriptexecution.class.getMethods();
			
				List<Scriptexecution> allAssignments= scriptexecutionRepository.findByAllFileAssignByClient(fileName, rscmClient.getRscmclientId());
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
	
	
	
	@PostMapping("/updateApplicantgroupAssignment")
	public String updateApplicantgroupAssignment(@RequestParam("entityAssignment") int[] entityAssignment,@RequestParam("fileAssignment") String[] fileAssignment) {
		if(entityAssignment.length<1 || fileAssignment.length <1) {
			return "The Assignment is only possible when you choose at least one applicantgroup and one file";
		}
		assert(entityAssignment.length>0);
		assert(fileAssignment.length>0);
		
		int countApplicants=0;
		int countClients=0;
		int countExsisting=0;
		for(int applicantgroupId: entityAssignment) {
			for(String fileName:fileAssignment) {
				Applicantgroup applicantgroup = applicantgroupRepository.findById(applicantgroupId).get();
				Set<Applicant> applicantSet = applicantgroup.getApplicants();
				for(Applicant a: applicantSet) {
					Set<RSCMClient> rscmClients = a.getRscmclients();
					countApplicants++;
					for(RSCMClient rscmClient: rscmClients) {
						List<Scriptexecution> seList = scriptexecutionRepository.getExsistingExecutionPlanBasedOnApplicantgroup(fileName, rscmClient.getRscmclientId(),applicantgroupId);
						if(seList.size()>0) {
							for(Scriptexecution seExsisting: seList) {
								seExsisting.setScriptexecutionAssigneddate(new Date());
								seExsisting.setApplicantgroup(applicantgroup);
								countExsisting++;
							}
						}else {
							Scriptexecution se = new Scriptexecution();
							se.setApplicantgroup(applicantgroupRepository.findById(applicantgroupId).get());
							se.setScriptName(fileName);
							se.setScriptexecutionAssigneddate(new Date());
							se.setRSCMClient(rscmClient);
							countClients++;
							scriptexecutionRepository.save(se);
						}
						
						
					}
				}
			}
		}
		return "the script assignment effects "+countApplicants+" applicants,<br> and " + countClients +" clients has an new script assignment, <br> and " + countExsisting + " existing script assignments are updated";
		
	}
	
}
