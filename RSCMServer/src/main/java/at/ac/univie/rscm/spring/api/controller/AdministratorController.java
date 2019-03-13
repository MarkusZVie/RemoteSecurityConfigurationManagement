package at.ac.univie.rscm.spring.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.ac.univie.rscm.model.Applicant;
import at.ac.univie.rscm.spring.api.repository.ApplicantRepository;

@RestController
@RequestMapping("/admin")
public class AdministratorController {

	
	
	@Autowired
	private ApplicantRepository applicantRepository;
	

	

	//@PreAuthorize("hasAnyRole('ADMIN')")
	@PostMapping("/user/add")
	public String addApplicantByAdmin(@RequestBody Applicant applicant) {
		String applicantPassword = applicant.getApplicantPassword();
		String encrypedApplicantPassword = new BCryptPasswordEncoder().encode(applicantPassword);
		applicant.setApplicantPassword(encrypedApplicantPassword);
		System.out.println(applicant);
		applicantRepository.save(applicant);
		return "user added ";
	}
	
	@GetMapping("/user/test")
	public String testtt() {
		return "jojojoj";
	}
}
