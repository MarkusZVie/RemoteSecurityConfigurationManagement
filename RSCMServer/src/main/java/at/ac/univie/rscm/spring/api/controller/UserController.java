package at.ac.univie.rscm.spring.api.controller;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import at.ac.univie.rscm.application.global.GlobalSettingsAndVariablesInterface;
import at.ac.univie.rscm.model.User;
import at.ac.univie.rscm.model.Role;
import at.ac.univie.rscm.spring.api.repository.UserRepository;
import at.ac.univie.rscm.spring.api.repository.RoleRepository;

@RestController
@RequestMapping("/users")
public class UserController {

//	private GlobalSettingsAndVariablesInterface gsav;
//	
//	public UserController() {
//		super();
//	}

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;

	@GetMapping(value = "/all")
	public List<User> findAll() {
		return userRepository.findAll();
	}

	@GetMapping(value = "/{userName}")
	public User findByName(@PathVariable final String userName) {
		return userRepository.findByUserName(userName);
	}

	@PostMapping(value = "/load")
	public User load(@RequestBody final User user) {
		userRepository.save(user);
		return userRepository.findByUserName(user.getUserName());
	}

	@PostMapping("/pushUserRegistration")
	public String pushUserRegistration(@RequestParam("formVars") String[] formVars) {
		HashMap<String, String> formVarsMap = new HashMap<String, String>();
		assert (formVars.length % 2 == 0);
		for (int i = 0; i < formVars.length;) {
			formVarsMap.put(formVars[i++], formVars[i++]);
		}
		User exsistingUser = userRepository.findByUserName(formVarsMap.get("userName"));
		if(exsistingUser!=null) {
			return "User cannot be added, because the username is already used";
		}
		
		Collection<Integer> keysOfUserWithSameName = userRepository.findByUserFullName(formVarsMap.get("userFirstname"), formVarsMap.get("userLastname"));
		if(keysOfUserWithSameName.size()>0) {
			return "User cannot be added, because the firstname and the secondname are already used";
		}
		
		User newUser = new User();
		newUser.setUserEmail(formVarsMap.get("userEmail"));
		newUser.setUserName(formVarsMap.get("userName"));
		newUser.setUserLastname(formVarsMap.get("userLastname"));
		newUser.setUserFirstname(formVarsMap.get("userFirstname"));
		newUser.setUserPassword(new BCryptPasswordEncoder().encode(formVarsMap.get("userPassword")));
		
		Collection<Role> roleList = roleRepository.findAllByRoleName("User");
		assert(roleList.size()==1);
		System.out.println(Arrays.toString(roleList.toArray()));
		Role r = roleList.iterator().next();
		System.out.println(r.getRoleName());
		newUser.addRole(r);
		
		userRepository.save(newUser);
		return "Registration successful, here back to <a href='index.jsp'>home</a>, or to  <a href='login'>login</a>";
		
		
	}

}
