package at.ac.univie.rscm.spring.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import at.ac.univie.rscm.model.Applicant;
import at.ac.univie.rscm.spring.api.repository.ApplicantRepository;

@Service
public class CustomApplicantDetailsService implements UserDetailsService{

	@Autowired
	private ApplicantRepository applicantRepository;
	
	@Override
	public UserDetails loadUserByUsername(String applicantName) throws UsernameNotFoundException {
		Applicant applicant = applicantRepository.findByApplicantName(applicantName);
		CustomApplicantDetails applicantDetails = null;
		if(applicant!=null) {
			applicantDetails = new CustomApplicantDetails();
			applicantDetails.setApplicant(applicant);
			return applicantDetails;
		}else {
			throw new UsernameNotFoundException("Applicant cannot be found: " + applicantName);
		}
	}

	
}
