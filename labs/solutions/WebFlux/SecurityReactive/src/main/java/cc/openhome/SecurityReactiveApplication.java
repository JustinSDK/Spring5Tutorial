package cc.openhome;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

import reactor.core.publisher.Mono;

@SpringBootApplication
public class SecurityReactiveApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecurityReactiveApplication.class, args);
	}
	
	@Bean
	public SecurityWebFilterChain securitygWebFilterChain(ServerHttpSecurity http) {

	    return http
//	    		 .authenticationManager(auth -> {
//	    			 String name = auth.getName();
//	    	         String password = auth.getCredentials().toString();
//
//	    	         if(name.equals("caterpillar") && password.equals("12345678")) {
//	    	        	 return Mono.just(
//	    	        	    new UsernamePasswordAuthenticationToken("caterpillar", "12345678", 
//	    	                    AuthorityUtils.createAuthorityList("ROLE_MEMBER")
//	    	        	    )
//	    	        	);
//	    	         }
//	    	         
//	    	         if(name.equals("admin") && password.equals("admin12345678")) {
//	    	        	 return Mono.just(
//	    	        	    new UsernamePasswordAuthenticationToken("admin", "admin12345678", 
//	    	                    AuthorityUtils.createAuthorityList("ROLE_MEMBER", "ROLE_ADMIN")
//	    	        	    )
//	    	        	);
//	    	         }
//	    	         
//	    	         return null;
//	    		 })
	    		 .authorizeExchange()
    		         .pathMatchers("/member.html").hasRole("MEMBER")
	    		     .pathMatchers("/admin.html").hasRole("ADMIN")
	    		 .anyExchange().permitAll()
	    		 .and()
	    		 .formLogin()
	    		 .and().build();
	}
	
	@Bean
	public ReactiveUserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
	    UserDetails admin = User
		      .withUsername("admin")
		      .password(passwordEncoder.encode("admin12345678"))
		      .roles("ADMIN", "MEMBER")
		      .build();

	    UserDetails caterpillar = User
		      .withUsername("caterpillar")
		      .password(passwordEncoder.encode("12345678"))
		      .roles("MEMBER")
		      .build();
	    
	    return new MapReactiveUserDetailsService(admin, caterpillar);
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}

