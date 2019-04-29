package at.ac.univie.rscm.spring.api.configuration;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import at.ac.univie.rscm.spring.api.repository.ApplicantRepository;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter{

	@Autowired
	private UserDetailsService applicantDetailsService;
	
		
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(applicantDetailsService).passwordEncoder(encodePassword());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.formLogin().permitAll().and()//allow everyone to access the login page
		.authorizeRequests() //declare multiple children									
		.antMatchers("/applicants/**", "/notExsitent", "/ClientAuthentication/**").permitAll() //everyone can access when they are logged in
		.antMatchers("/admin/**").hasRole("Administrator") //role admin can get access
		.anyRequest().authenticated();
		
		
		
	}
	
	
	 
	 
	@Bean
	public PasswordEncoder encodePassword() {
		return new BCryptPasswordEncoder();
	}
	
	
	
}
