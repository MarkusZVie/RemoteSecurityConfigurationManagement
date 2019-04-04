package at.ac.univie.rscm.spring.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import at.ac.univie.rscm.spring.api.repository.RSCMClientRepository;

@Component("jspSupporterBean")
public class JSPSupporterBean {

	@Autowired
	private RSCMClientRepository rcr;
	
	public RSCMClientRepository getRSCMClientRepository() {
		return rcr;
	}
	
}
