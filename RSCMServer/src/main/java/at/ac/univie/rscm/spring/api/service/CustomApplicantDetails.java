package at.ac.univie.rscm.spring.api.service;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import at.ac.univie.rscm.model.Applicant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomApplicantDetails implements UserDetails{

	private Applicant applicant;
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return applicant.getRoles().stream().map(role-> new SimpleGrantedAuthority("ROLE_"+role.getRoleName())).collect(Collectors.toList());
		
	}

	@Override
	public String getPassword() {
		return applicant.getApplicantPassword();
	}

	@Override
	public String getUsername() {
		return applicant.getApplicantName();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
