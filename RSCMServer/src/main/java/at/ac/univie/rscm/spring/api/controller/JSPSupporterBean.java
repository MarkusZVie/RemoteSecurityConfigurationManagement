package at.ac.univie.rscm.spring.api.controller;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import at.ac.univie.rscm.application.global.GlobalSettingsAndVariables;
import at.ac.univie.rscm.application.global.GlobalSettingsAndVariablesInterface;
import at.ac.univie.rscm.spring.api.repository.ApplicantRepository;
import at.ac.univie.rscm.spring.api.repository.EnvironmentRepository;
import at.ac.univie.rscm.spring.api.repository.RSCMClientRepository;
import at.ac.univie.rscm.spring.api.repository.RoleRepository;
import at.ac.univie.rscm.spring.api.repository.ScriptexecutionRepository;
import lombok.Getter;

@Getter
@Component("jspSupporterBean")
public class JSPSupporterBean {

	private GlobalSettingsAndVariablesInterface gsav;
	
	public JSPSupporterBean() {
		gsav = GlobalSettingsAndVariables.getInstance();
	}

	@Autowired
	private RSCMClientRepository rSCMClientRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private ApplicantRepository applicantRepository;
	
	@Autowired 
	private ScriptexecutionRepository scriptexecutionRepository;
	
	@Autowired
	private EnvironmentRepository environmentRepository;
	
	@PostConstruct
    public void init() {
		
		gsav.setRSCMClientRepository(rSCMClientRepository);
		gsav.setRoleRepository(roleRepository);
		gsav.setApplicantRepository(applicantRepository);
		gsav.setScriptexecutionRepository(scriptexecutionRepository);
		gsav.setEnvironmentRepository(environmentRepository);
		gsav.initPortScanner();
		
    }
}
