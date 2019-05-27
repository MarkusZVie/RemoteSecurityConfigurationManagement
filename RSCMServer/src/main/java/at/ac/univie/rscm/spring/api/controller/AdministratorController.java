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

import at.ac.univie.rscm.model.User;
import at.ac.univie.rscm.spring.api.repository.UserRepository;

@RestController
@RequestMapping("/admin")
public class AdministratorController {

	
	
	@Autowired
	private UserRepository userRepository;
	

	

	//@PreAuthorize("hasAnyRole('ADMIN')")
	@PostMapping("/user/add")
	public String addUserByAdmin(@RequestBody User user) {
		String userPassword = user.getUserPassword();
		String encrypedUserPassword = new BCryptPasswordEncoder().encode(userPassword);
		user.setUserPassword(encrypedUserPassword);
		System.out.println(user);
		userRepository.save(user);
		return "user added ";
	}
	
	@GetMapping("/user/test")
	public String testtt() {
		return "jojojoj";
	}
}
