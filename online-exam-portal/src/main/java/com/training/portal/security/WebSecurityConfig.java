package com.training.portal.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	@Autowired
	private UserDetailsService jwtUserDetailsService;
	@Autowired
	private JwtRequestFilter jwtRequestFilter;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
// configure AuthenticationManager so that it knows from where to load
// user for matching credentials
// Use BCryptPasswordEncoder
		auth.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoder());
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
// We don't need CSRF for this example
		httpSecurity.csrf().disable()
// dont authenticate this particular request
				.authorizeRequests()
				.antMatchers("/online-training-portal-application/trainer-signup**").permitAll()
				.antMatchers("/online-training-portal-application/trainee-signup**").permitAll()
				.antMatchers("/online-training-portal-application/login**").permitAll()
				.antMatchers("/examination/all-questions**").permitAll()//favicon-32x32.png
				.antMatchers("/v3/api-docs/swagger-config**").permitAll()
				.antMatchers("/v3/api-docs/**").permitAll()//
				.antMatchers("/swagger-ui/favicon-32x32.png**").permitAll()
				.antMatchers("/swagger-ui/index.html**").permitAll()
				.antMatchers("/swagger-ui/swagger-ui.css**").permitAll()///swagger-ui/swagger-ui.css
				.antMatchers("/swagger-ui/index.css**").permitAll()
				.antMatchers("/swagger-ui/swagger-ui-bundle.js**").permitAll()
				.antMatchers("/swagger-ui/swagger-ui-standalone-preset.js**").permitAll()
				.antMatchers("/swagger-ui/swagger-initializer.js**").permitAll()///v3/api-docs
				.antMatchers("/swagger-ui/favicon-16x16.png**").permitAll() //v3/api-docs/swagger-config
				.antMatchers("/online-training-portal-application/health-check").permitAll()


// all other requests need to be authenticated ///health-check
				.anyRequest().authenticated().and().
// make sure we use stateless session; session won't be used to
// store user's state.
				exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
// Add a filter to validate the tokens with every request
		httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
	}

}
