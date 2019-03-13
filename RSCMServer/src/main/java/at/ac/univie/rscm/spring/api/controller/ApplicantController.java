package at.ac.univie.rscm.spring.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.ac.univie.rscm.model.Applicant;
import at.ac.univie.rscm.spring.api.repository.ApplicantRepository;

@RestController
@RequestMapping("/applicants")
public class ApplicantController {

	@Autowired
	private ApplicantRepository applicantRepository;
	
	@GetMapping(value = "/all")
	public List<Applicant> findAll(){
		return applicantRepository.findAll();
	}
	
	@GetMapping(value = "/{applicantName}")
	public Applicant findByName(@PathVariable final String applicantName){
		return applicantRepository.findByApplicantName(applicantName);
	}
	
	@PostMapping(value ="/load")
	public Applicant load(@RequestBody final Applicant applicant) {
		applicantRepository.save(applicant);
		return applicantRepository.findByApplicantName(applicant.getApplicantName());
	}
	
}
