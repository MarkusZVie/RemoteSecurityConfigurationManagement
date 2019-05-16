package at.ac.univie.rscm.spring.api.controller;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import at.ac.univie.rscm.application.global.GlobalSettingsAndVariablesInterface;
import at.ac.univie.rscm.model.Applicant;
import at.ac.univie.rscm.model.Role;
import at.ac.univie.rscm.spring.api.repository.ApplicantRepository;
import at.ac.univie.rscm.spring.api.repository.RoleRepository;

@RestController
@RequestMapping("/applicants")
public class ApplicantController {

//	private GlobalSettingsAndVariablesInterface gsav;
//	
//	public ApplicantController() {
//		super();
//	}

	@Autowired
	private ApplicantRepository applicantRepository;
	
	@Autowired
	private RoleRepository roleRepository;

	@GetMapping(value = "/all")
	public List<Applicant> findAll() {
		return applicantRepository.findAll();
	}

	@GetMapping(value = "/{applicantName}")
	public Applicant findByName(@PathVariable final String applicantName) {
		return applicantRepository.findByApplicantName(applicantName);
	}

	@PostMapping(value = "/load")
	public Applicant load(@RequestBody final Applicant applicant) {
		applicantRepository.save(applicant);
		return applicantRepository.findByApplicantName(applicant.getApplicantName());
	}

	@PostMapping("/pushApplicantRegistration")
	public String pushApplicantRegistration(@RequestParam("formVars") String[] formVars) {
		HashMap<String, String> formVarsMap = new HashMap<String, String>();
		assert (formVars.length % 2 == 0);
		for (int i = 0; i < formVars.length;) {
			formVarsMap.put(formVars[i++], formVars[i++]);
		}
		Applicant exsistingApplicant = applicantRepository.findByApplicantName(formVarsMap.get("applicantName"));
		if(exsistingApplicant!=null) {
			return "Applicant cannot be added, because the username is already used";
		}
		
		Collection<Integer> keysOfApplicantWithSameName = applicantRepository.findByApplicantFullName(formVarsMap.get("applicantFirstname"), formVarsMap.get("applicantLastname"));
		if(keysOfApplicantWithSameName.size()>0) {
			return "Applicant cannot be added, because the firstname and the secondname are already used";
		}
		
		Applicant newApplicant = new Applicant();
		newApplicant.setApplicantEmail(formVarsMap.get("applicantEmail"));
		newApplicant.setApplicantName(formVarsMap.get("applicantName"));
		newApplicant.setApplicantLastname(formVarsMap.get("applicantLastname"));
		newApplicant.setApplicantFirstname(formVarsMap.get("applicantFirstname"));
		newApplicant.setApplicantPassword(new BCryptPasswordEncoder().encode(formVarsMap.get("applicantPassword")));
		
		Collection<Role> roleList = roleRepository.findAllByRoleName("Applicant");
		assert(roleList.size()==1);
		System.out.println(Arrays.toString(roleList.toArray()));
		Role r = roleList.iterator().next();
		System.out.println(r.getRoleName());
		newApplicant.addRole(r);
		
		applicantRepository.save(newApplicant);
		return "Registration successful, here back to <a href='index.jsp'>home</a>, or to  <a href='login'>login</a>";
		
		
	}

}
