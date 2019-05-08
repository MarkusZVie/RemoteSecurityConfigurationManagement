package at.ac.univie.rscm.spring.api;

import java.io.File;
import java.io.IOException;

import org.springframework.boot.context.properties.ConfigurationProperties;

import at.ac.univie.rscm.application.global.GlobalSettingsAndVariables;
import at.ac.univie.rscm.application.global.GlobalSettingsAndVariablesInterface;

//https://www.callicoder.com/spring-boot-file-upload-download-rest-api-example/
@ConfigurationProperties(prefix = "file")
public class FileStorageProperties {
    private String uploadDir;
    
    private GlobalSettingsAndVariablesInterface gsav;
    
    

    public FileStorageProperties() {
		gsav = GlobalSettingsAndVariables.getInstance();
	}

	public String getUploadDir() {
        return uploadDir;
    }

    public void setUploadDir(String uploadDir) {
    	this.uploadDir = gsav.getFileDownloadDirectory();
        
    }
}