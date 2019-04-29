package at.ac.univie.rscm.spring.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import at.ac.univie.rscm.application.global.GlobalSettingsAndVariables;
import at.ac.univie.rscm.application.global.GlobalSettingsAndVariablesInterface;

@SpringBootApplication
@EntityScan("at.ac.univie.rscm.model")
@ComponentScan(basePackages = "at.ac.univie.rscm.spring.api")
public class RscmServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(RscmServerApplication.class, args);
		// Admin Passoword okv00obb11kfz22
		// https certificate:
		// https://drissamri.be/blog/java/enable-https-in-spring-boot/
		// password: Ot3K-m.tXM,jHS!!
		/*
		"C:\Program Files\Java\jdk1.8.0_202\bin\keytool.exe" -genkey -alias tomcat -storetype PKCS12 -keyalg RSA -keysize 2048 -keystore keystore.p12 -validity 3650
		Keystore-Kennwort eingeben:
		Neues Kennwort erneut eingeben:
		Wie lautet Ihr Vor- und Nachname?
		  [Unknown]:  Markus Zila
		Wie lautet der Name Ihrer organisatorischen Einheit?
		  [Unknown]:  University of Vienna
		Wie lautet der Name Ihrer Organisation?
		  [Unknown]:  Student Master Project
		Wie lautet der Name Ihrer Stadt oder Gemeinde?
		  [Unknown]:  Vienna
		Wie lautet der Name Ihres Bundeslands?
		  [Unknown]:  Vienna
		Wie lautet der Ländercode (zwei Buchstaben) für diese Einheit?
		  [Unknown]:  AT
		Ist CN=Markus Zila, OU=University of Vienna, O=Student Master Project, L=Vienna, ST=Vienna, C=AT richtig?
		  [Nein]:  Ja


		 */

	}

}
