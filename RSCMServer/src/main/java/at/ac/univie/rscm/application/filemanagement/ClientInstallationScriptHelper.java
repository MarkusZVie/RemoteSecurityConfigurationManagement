package at.ac.univie.rscm.application.filemanagement;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Date;
import java.util.Random;

import at.ac.univie.rscm.application.global.GlobalSettingsAndVariablesInterface;
import at.ac.univie.rscm.model.Applicant;
import lombok.Getter;
import at.ac.univie.rscm.application.global.GlobalSettingsAndVariables;

public class ClientInstallationScriptHelper extends Thread {

	private int availabilityTime;
	@Getter
	private String client_appkey_value;
	@Getter
	private File scriptFile;
	@Getter
	private File exeFile;
	@Getter
	private Date createnDate;
	private ClientInstallationScriptManager cism;
	private GlobalSettingsAndVariablesInterface gsav;
	@Getter
	private String rscm_password_value;
	@Getter
	private String rscm_keypass_value;
	@Getter
	private String client_keypass_value;
	@Getter
	private int client_specificport_value;
	@Getter
	private int loggedInApplicantId;

	public ClientInstallationScriptHelper(int availabilityTime, int loggedInApplicantId) {
		this.loggedInApplicantId = loggedInApplicantId;
		this.availabilityTime = availabilityTime;
		cism = ClientInstallationScriptManager.getInstance();
		gsav = GlobalSettingsAndVariables.getInstance();
		client_appkey_value = generateRandomString(32);
		while (cism.checkIfApiKeyIsInUse(client_appkey_value)) {
			client_appkey_value = generateRandomString(32);
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
		clearUp();

	}

	public void clearUp() {
		File configFile = new File(exeFile.getAbsolutePath() + ".config");
		configFile.delete();
		scriptFile.delete();
		exeFile.delete();
		
		
	}

	private String generateRandomString(int length) {
		// https://stackoverflow.com/questions/20536566/creating-a-random-string-with-a-z-and-0-9-in-java/20536597
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

	public File getFile(boolean isExtern) {
		scriptFile = getInstallPS1Script(isExtern);
		exeFile = createExeFromScript(scriptFile);
		return exeFile;
	}

	private File createExeFromScript(File scriptFile) {
		//source https://stackoverflow.com/questions/29545611/executing-powershell-commands-in-java-program/29545926
		try {
			String command = "powershell.exe "+getApplicationPathBackslash() + "\\ScriptManagement\\ps2exe.ps1" 
							+" -InputFile "+ scriptFile.getAbsoluteFile() 
							+" -OutputFile "+ scriptFile.getAbsolutePath().substring(0, scriptFile.getAbsolutePath().length()-3) + "exe";
			// Executing the command
						
			Process powerShellProcess;
			powerShellProcess = Runtime.getRuntime().exec(command);
			// Getting the results
			powerShellProcess.getOutputStream().close();
			
			String line;
			BufferedReader stdout = new BufferedReader(new InputStreamReader(powerShellProcess.getInputStream()));
			while ((line = stdout.readLine()) != null) {
			}
			stdout.close();
			BufferedReader stderr = new BufferedReader(new InputStreamReader(powerShellProcess.getErrorStream()));
			while ((line = stderr.readLine()) != null) {
				System.out.println(line);
			}
			stderr.close();
			

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}

		return new File(scriptFile.getAbsolutePath().substring(0, scriptFile.getAbsolutePath().length()-3) + "exe");
	}

	private File getInstallPS1Script(boolean isExtern) {
		// Server specific escapes
		final String SERVER_IP_ESCAPE = "<escape>serverIP</escape>";
		final String SERVER_RSAFINGERPRINT_ESCAPE = "<escape>ServerFingerPrint</escape>";
		final String SERVER_PUBLICKEY_ESCAPE = "<escape>serverPubKey</escape>";

		// client Specific escapes
		final String RSCM_PASSWORD_ESCAPE = "<escape>rscmClientPassword</escape>";
		final String RSCM_KEYPASS_ESCAPE = "<escape>rscmKeyPass</escape>";
		final String CLIENT_KEYPASS_ESCAPE = "<escape>userKeyPass</escape>";
		final String CLIENT_APPKEY_ESCAPE = "<escape>clientApplicationKey</escape>";
		final String CLIENT_SPECIFICPORT_ESCAPE = "<escape>serverClientSpecificPort</escape>";

		// load baseScript
		String basicScriptContent = "";
		try {
			basicScriptContent = getFileContent(getApplicationPath() + "/ScriptManagement/basicInstallScript");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String tempScriptBuilding;
		if(isExtern) {
			tempScriptBuilding = basicScriptContent.replace(SERVER_IP_ESCAPE, gsav.getServer_extern_ip_value());
		}else {
			tempScriptBuilding = basicScriptContent.replace(SERVER_IP_ESCAPE, gsav.getServer_intern_ip_value());
		}
		
		
		tempScriptBuilding = tempScriptBuilding.replace(SERVER_RSAFINGERPRINT_ESCAPE, gsav.getServer_rsafingerprint_value());
		tempScriptBuilding = tempScriptBuilding.replace(SERVER_PUBLICKEY_ESCAPE, gsav.getServer_publickey_value());

		tempScriptBuilding = tempScriptBuilding.replace(RSCM_PASSWORD_ESCAPE,
				rscm_password_value = generateRandomString(16));
		tempScriptBuilding = tempScriptBuilding.replace(RSCM_KEYPASS_ESCAPE,
				rscm_keypass_value = generateRandomString(16));
		tempScriptBuilding = tempScriptBuilding.replace(CLIENT_KEYPASS_ESCAPE,
				client_keypass_value = generateRandomString(16));
		tempScriptBuilding = tempScriptBuilding.replace(CLIENT_APPKEY_ESCAPE, client_appkey_value);
		tempScriptBuilding = tempScriptBuilding.replace(CLIENT_SPECIFICPORT_ESCAPE, (client_specificport_value=cism.getPortNumber()) + "");

		//System.out.println(tempScriptBuilding);
		
		return createFile(tempScriptBuilding, "ClientInstall_" + client_appkey_value, "ps1",
				getApplicationPath() + "/src/main/webapp/WEB-INF/files/tempFiles/");
	}

	
	
	

	private String getFileContent(String path) throws IOException {
		InputStream is = new FileInputStream(path);
		BufferedReader buffer = new BufferedReader(new InputStreamReader(is));

		String line = buffer.readLine();
		StringBuilder sb = new StringBuilder();

		while (line != null) {
			sb.append(line).append("\n");
			line = buffer.readLine();
		}
		buffer.close();
		return sb.toString();
	}

	private String writeFileToTempFiles(File file) {
		System.out.println(file.getName());
		String path = getApplicationPath() + "/src/main/webapp/WEB-INF/files/tempFiles";
		File newFile = new File(path + file.getName());

		InputStream is = null;
		OutputStream os = null;
		try {
			is = new FileInputStream(file);
			os = new FileOutputStream(newFile);
			byte[] buffer = new byte[1024];
			int length;
			while ((length = is.read(buffer)) > 0) {
				os.write(buffer, 0, length);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
				os.close();
			} catch (IOException e) {
			}

		}

		System.out.println(path);
		return file.getName();
	}

	private String getApplicationPath() {
		String path = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		path = path.substring(1, path.lastIndexOf("/target"));
		return path;
	}
	
	private String getApplicationPathBackslash() {
		String path = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		path = path.substring(1, path.lastIndexOf("/target"));
		path = path.replace('/', '\\');
		return path;
	}

	private File createFile(String content, String filename, String fileExtention, String path) {
		File file = null;
		try {
			if (path == null || path.equals("")) {
				file = File.createTempFile(filename, "." + fileExtention, null);
			} else {
				file = new File(path + filename + "." + fileExtention);
			}

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
