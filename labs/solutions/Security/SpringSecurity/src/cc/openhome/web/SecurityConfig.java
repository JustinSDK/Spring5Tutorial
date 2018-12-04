package cc.openhome.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
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
				    .roles("USER");
	}
}
