package at.ac.univie.rscm.spring.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.ac.univie.rscm.model.RSAClientPubKey;

@RestController
@RequestMapping("/ClientAuthentication")
public class RSARegisterController {

	
	

	private StringBuilder sb;
	
	//@Autowired
	//private ApplicantRepository applicantRepository;
	
	public RSARegisterController() {
		sb= new StringBuilder();
	}
	
	@PostMapping("/SendPublicKey")
	public String addClientPublicKeyToKnowenHosts(@RequestBody RSAClientPubKey rsaClientKey) {
		sb.append(rsaClientKey);
		return "user added ";
	}
	
	@GetMapping("/SendPublicKey")
	public String testtt() {
		return sb.toString();
	}
	
}
