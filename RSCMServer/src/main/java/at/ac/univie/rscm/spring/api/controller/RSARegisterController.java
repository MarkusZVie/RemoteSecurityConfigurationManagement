package at.ac.univie.rscm.spring.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.ac.univie.rscm.application.filemanagement.ClientInstallationScriptBuilder;
import at.ac.univie.rscm.application.filemanagement.ClientInstallationScriptManager;
import at.ac.univie.rscm.model.RSCMClient;
import at.ac.univie.rscm.spring.api.repository.RSCMClientRepository;

@RestController
@RequestMapping("/ClientAuthentication")
public class RSARegisterController {

	private StringBuilder sb;
	
	@Autowired
	private RSCMClientRepository rcrClientRepository;
	
	public RSARegisterController() {
		sb= new StringBuilder();
	}
	
	@PostMapping("/SendPublicKey")
	public String addClientPublicKeyToKnowenHosts(@RequestBody RSCMClient rsaClientKey) {
		
		
		ClientInstallationScriptBuilder cisb = ClientInstallationScriptManager.getInstance();
		cisb.confirmAppKey(rsaClientKey);
		sb.append(rsaClientKey);
		return "user added ";
	}
	
	@GetMapping("/SendPublicKey")
	public String testtt() {
		if(rcrClientRepository == null) {
			System.out.println("sdsdsdage4e");
		}else {
			System.out.println("sd");
		}
		return sb.toString();
	}
	
}
