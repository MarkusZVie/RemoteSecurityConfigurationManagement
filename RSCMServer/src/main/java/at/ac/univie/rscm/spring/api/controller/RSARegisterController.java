package at.ac.univie.rscm.spring.api.controller;

import java.util.Set;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
	private ClientInstallationScriptBuilder cisb;
	
	public RSARegisterController() {
		sb= new StringBuilder();
		cisb = ClientInstallationScriptManager.getInstance();
	}
	
	@PostMapping("/SendPublicKey")
	public String addClientPublicKeyToKnowenHosts(@RequestBody String s) {
		
		JSONObject responseJsonObject = new JSONObject(s);
		String applikationKey = responseJsonObject.getString("applikationKey");
		String clientRSAPublicKey = responseJsonObject.getString("clientRSAPublicKey");
		return cisb.confirmAppKey(applikationKey,clientRSAPublicKey);
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
