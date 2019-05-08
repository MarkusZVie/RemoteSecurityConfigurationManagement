package at.ac.univie.rscm.spring.api.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.ac.univie.rscm.application.filemanagement.ClientInstallationScriptBuilder;
import at.ac.univie.rscm.application.filemanagement.ClientInstallationScriptHelper;
import at.ac.univie.rscm.application.filemanagement.ClientInstallationScriptManager;
import at.ac.univie.rscm.application.global.GlobalSettingsAndVariables;
import at.ac.univie.rscm.application.global.GlobalSettingsAndVariablesInterface;
import at.ac.univie.rscm.spring.api.repository.RSCMClientRepository;

@RestController
@RequestMapping("/Downloads")
public class InstallFileDownloadController {
	
	@Autowired
	private RSCMClientRepository rcsmClientRepository;
	
	private GlobalSettingsAndVariablesInterface gsav;
	private ClientInstallationScriptBuilder cisb;
	
	

	public InstallFileDownloadController() {
		gsav = GlobalSettingsAndVariables.getInstance();
		cisb = ClientInstallationScriptManager.getInstance();
		gsav.addDownload("RSCMExternClientInstaller.exe", "Install Application for World-Wide usage");
		gsav.addDownload("RSCMInternClientInstaller.exe", "Install Application for Local usage");
	}



	@RequestMapping("/{fileName:.+}") // https://howtodoinjava.com/spring-mvc/spring-mvc-download-file-controller-example/
	public void downloadResource(HttpServletRequest request, HttpServletResponse response, @PathVariable("fileName") String fileName) {
		
		
		if(fileName.contentEquals("RSCMExternClientInstaller.exe")) {
			
			gsav.setRSCMClientRepository(rcsmClientRepository);
			
			
			File file = cisb.getClientInstallProgram(true);
			response.addHeader("Content-Disposition", "attachment; filename=" + file.getName());
			try {
				Path path = file.toPath();
				Files.copy(path, response.getOutputStream());
				response.getOutputStream().flush();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			
		}else {
			String dataDirectory = request.getServletContext().getRealPath("/WEB-INF/downloads/");
			File file = new File(dataDirectory + fileName);
			if(file.exists()) {
				//response.setContentType("application/pdf");
				response.addHeader("Content-Disposition", "attachment; filename=" + fileName);
				try {
					Path path = file.toPath();
					Files.copy(path, response.getOutputStream());
					response.getOutputStream().flush();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
			
			
		}
		
		if(fileName.contentEquals("RSCMInternClientInstaller.exe")) {
			gsav = GlobalSettingsAndVariables.getInstance();
			gsav.setRSCMClientRepository(rcsmClientRepository);
			
			cisb = ClientInstallationScriptManager.getInstance();
			File file = cisb.getClientInstallProgram(false);
			response.addHeader("Content-Disposition", "attachment; filename=" + file.getName());
			try {
				Path path = file.toPath();
				Files.copy(path, response.getOutputStream());
				response.getOutputStream().flush();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			
		}else {
			String dataDirectory = request.getServletContext().getRealPath("/WEB-INF/downloads/");
			File file = new File(dataDirectory + fileName);
			if(file.exists()) {
				//response.setContentType("application/pdf");
				response.addHeader("Content-Disposition", "attachment; filename=" + fileName);
				try {
					Path path = file.toPath();
					Files.copy(path, response.getOutputStream());
					response.getOutputStream().flush();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
			
			
		}
		
		
		
	}

}
