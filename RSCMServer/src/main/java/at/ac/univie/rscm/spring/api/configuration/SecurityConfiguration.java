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
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

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
		.antMatchers("/ClientAuthentication/**",//for accept RSA Key with application key
				"/RegisterAccount.jsp", //register new Applicants
				"/index.jsp", //basic home Page
				"/", //pattern that forward to index.jsp
				"/WebressourcenImport/**", //css and html imports
				"/applicants/pushApplicantRegistration").permitAll()//register new Applicatn Service
		.antMatchers("/admin/**").access("hasRole('Administrator') and hasRole('User')") 
		.antMatchers("/ajax/**").access("hasRole('User')") 
		.antMatchers("/Administration/**").access("hasRole('Administrator') or hasRole('Securitymanager')") 
		.antMatchers("/Application/**").access("hasRole('Administrator') or hasRole('Applicationmanager')") 
		.antMatchers("/WebPage/**").access("hasRole('Administrator') or hasRole('User')") 
		.antMatchers("/ScriptExecution/**").access("hasRole('Administrator') or hasRole('Securitymanager')") 
		.antMatchers("/FileManager/**").access("hasRole('Administrator') or hasRole('Securitymanager') or hasRole('Applicationmanager')") 
		.anyRequest().authenticated();
		
		
		
	}
	
	
	@Bean(name = "multipartResolver")
	public CommonsMultipartResolver multipartResolver() {
	    CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
	    multipartResolver.setMaxUploadSize(100000);
	    return multipartResolver;
	} 
	 
	@Bean
	public PasswordEncoder encodePassword() {
		return new BCryptPasswordEncoder();
	}
	
	
	
}
