package cc.openhome;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@SpringBootApplication
public class GatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityWebFilterChain securitygWebFilterChain(ServerHttpSecurity http) {
	    return http.authorizeExchange()
	    		   .anyExchange().authenticated()
	    		   .and()
	    		   .httpBasic()
	    		   .and()
	    		   .csrf().disable()
	    		   .build();
	}
	
	@Value("${client.web.name}") 
	private String clientName;
	
	@Value("${client.web.secret}") 
	private String clientSecret;
	
	@Bean
	public ReactiveUserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
	    UserDetails webclient = User
	          .withUsername(clientName)
	          .password(passwordEncoder.encode(clientSecret))
	          .roles("account", "message", "email")
	          .build();

	    return new MapReactiveUserDetailsService(webclient);
	}
}

