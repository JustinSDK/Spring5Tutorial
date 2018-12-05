package cc.openhome.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		PasswordEncoder pwdEncoder = new BCryptPasswordEncoder();
		auth.inMemoryAuthentication() 
		    .passwordEncoder(pwdEncoder)
				.withUser("admin") 
				    .password(pwdEncoder.encode("admin12345678"))
				    .roles("ADMIN", "MEMBER")
			.and()
				.withUser("caterpillar")
				    .password(pwdEncoder.encode("12345678"))
				    .roles("MEMBER");
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
		    .antMatchers("/login_page", "/logout_page", "/perform_login", "/perform_logout").permitAll()
			.antMatchers("/**").authenticated()
	        .and()
			.formLogin() 
				.loginPage("/login_page")
				.loginProcessingUrl("/perform_login")
				.failureUrl("/login_page?error")
			.and()
			.logout()
			    .logoutUrl("/perform_logout")
			    .logoutSuccessUrl("/login_page?logout");
	}
}
