package at.ac.univie.rscm.spring.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@EntityScan("at.ac.univie.rscm.model")
@ComponentScan(basePackages = "at.ac.univie.rscm.spring.api") 
public class RscmServerApplication {
	
	
	public static void main(String[] args) {
		SpringApplication.run(RscmServerApplication.class, args);
		
		System.out.println(new BCryptPasswordEncoder().encode("okv00obb11kfz22"));
		
	}

}
