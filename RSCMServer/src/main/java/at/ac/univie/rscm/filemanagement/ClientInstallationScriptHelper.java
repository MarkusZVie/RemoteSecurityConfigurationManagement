package at.ac.univie.rscm.filemanagement;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Random;

public class ClientInstallationScriptHelper extends Thread{

	private int availabilityTime;
	private String apiKey;
	private File scriptFile;
	private Date createnDate;
	private ClientInstallationScriptManager cism;
	
	public ClientInstallationScriptHelper(int availabilityTime) {
		this.availabilityTime = availabilityTime;
		cism=ClientInstallationScriptManager.getInstance();
		apiKey = generateAPIKey(32);
		while(cism.checkIfApiKeyIsInUse(apiKey)) {
			apiKey = generateAPIKey(32);
		}
		createnDate = new Date();
	}
	
	@Override
	public void run() {
		try {
			Thread.sleep(availabilityTime);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cism.removeActiveInstallation(this);
	}
	
	private String generateAPIKey(int length) {
		//https://stackoverflow.com/questions/20536566/creating-a-random-string-with-a-z-and-0-9-in-java/20536597
		String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890abcdefghijklmnopqrstuvwxyz!=";
        StringBuilder sb = new StringBuilder();
        Random rnd = new Random();
        while (sb.length() < length) { // length of the random string.
            int index = (int) (rnd.nextFloat() * chars.length());
            sb.append(chars.charAt(index));
        }
        String returnString = sb.toString();
        return returnString;
	}

	public File getFile() {
		File ps1Script = getInstallPS1Script();
		return null;
	}
	
	private File getInstallPS1Script() {
		StringBuilder sb = new StringBuilder();
		
		
		
		return null;
	}

	public String getApiKey() {
		return apiKey;
	}
	

	public String writeFileToTempFiles(File file) {
		String path = getApplicationPath() + "/src/main/webapp/WEB-INF/files/tempFiles";
		System.out.println(path);
		return "";
	}
	
	public String getApplicationPath() {
		String path = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		path = path.substring(1, path.lastIndexOf("/target"));
		return path;
	}
	
	public File createFile(String content, String filename, String fileExtention) {
		File file = null;
		try {
			file = File.createTempFile(filename, "."+fileExtention, null);
			BufferedWriter out = new BufferedWriter(new FileWriter(file));
			out.write(content);
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return file;
	}

}
