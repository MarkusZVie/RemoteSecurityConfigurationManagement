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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Downloads")
public class InstallFileDownloadController {

	@RequestMapping("/{fileName:.+}") // https://howtodoinjava.com/spring-mvc/spring-mvc-download-file-controller-example/
	public void downloadResource(HttpServletRequest request, HttpServletResponse response, @PathVariable("fileName") String fileName) {
		System.out.println(fileName);
		
		if(fileName.contentEquals("RSCMClientInstaller.exe")) {
			File file = createClientInstaller();
			response.addHeader("Content-Disposition", "attachment; filename=" + fileName);
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

	private File createClientInstaller() {
		File f = null;
		try {
			f = File.createTempFile("test", ".txt", null);
			BufferedWriter out = new BufferedWriter(new FileWriter(f));
			out.write("aString");
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return f;
	}

}
