package at.ac.univie.rscm.application.connection;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class RSAKeyManager implements RSAKeyManagerInterface {

	@Override
	public String getHostPublicKey() {
		String fileName = "C:\\Users\\rscmserver\\.ssh\\id_rsa.pub";
		String line = null;
		StringBuilder sb = new StringBuilder();
		try {
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			while ((line = bufferedReader.readLine()) != null) {
				sb.append(line);
			}
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileName + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + fileName + "'");
			// Or we could just do this:
			// ex.printStackTrace();
		}
		return sb.toString();
	}

}
