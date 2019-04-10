package at.ac.univie.rscm.spring.api.controller;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import at.ac.univie.rscm.application.global.GlobalSettingsAndVariables;
import at.ac.univie.rscm.application.global.GlobalSettingsAndVariablesInterface;
import at.ac.univie.rscm.spring.api.repository.ApplicantRepository;
import at.ac.univie.rscm.spring.api.repository.RSCMClientRepository;
import at.ac.univie.rscm.spring.api.repository.RoleRepository;

@Component("jspSupporterBean")
public class JSPSupporterBean {

	private GlobalSettingsAndVariablesInterface gsav;
	
	public JSPSupporterBean() {
		gsav = GlobalSettingsAndVariables.getInstance();
	}

	@Autowired
	private RSCMClientRepository rcr;
	
	@Autowired
	private RoleRepository rr;
	
	@Autowired
	private ApplicantRepository ar;
	
	public RSCMClientRepository getRSCMClientRepository() {
		return rcr;
	}
	
	public RoleRepository getRoleRepository() {
		return rr;
	}
	
	public ApplicantRepository getApplicantRepository() {
		return ar;
	}
	
	
	
	@PostConstruct
    public void init() {
		gsav.setRSCMClientRepository(rcr);
		gsav.setRoleRepository(rr);
		gsav.setApplicantRepository(ar);
		gsav.initPortScanner();
    }
}
