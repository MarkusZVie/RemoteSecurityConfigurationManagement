package at.ac.univie.rscm.spring.api.controller;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.jcraft.jsch.JSchException;

import at.ac.univie.rscm.application.connection.SSHConnectionBuilder;
import at.ac.univie.rscm.application.connection.SSHConnectionManager;
import at.ac.univie.rscm.application.connection.SSHConnectionManagerInterface;
import at.ac.univie.rscm.application.global.data.ShellComand;


@RestController
@RequestMapping("/ajax/")
public class AjaxRemoteShellController {
	// https://www.boraji.com/spring-4-mvc-jquery-ajax-form-submit-example
	@GetMapping("/postComand")
	public String employeeForm() {
		System.out.println("sdsdöäüfäüafäa");
		return "employeeForm";
	}
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@PostMapping(value = "/postComand", produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public ArrayList<String> postComand(@Valid @RequestBody ShellComand shellComand) {
		System.out.println("ssdsd");
		String comand = shellComand.getComand();
		int keyId = Integer.parseInt(shellComand.getKeyID());
		
		SSHConnectionManagerInterface sshConnectionManager = SSHConnectionManager.getInstance();
		SSHConnectionBuilder cb = sshConnectionManager.getConnection(keyId);
		ArrayList<String> returnContent = new ArrayList<String>();
		if(cb == null) {
			returnContent.add("Could not create connection, please check if client is active");
			returnContent.add("You access ClientID: " + keyId);
			return returnContent;
		}else {
			try {
				returnContent.add(cb.sendComand(comand));
				return returnContent;
			} catch (JSchException | IOException e) {
				returnContent.add("The comand could not send [JSchException | IOException]");
				return returnContent;
			}
			
		}
		
	}
}

