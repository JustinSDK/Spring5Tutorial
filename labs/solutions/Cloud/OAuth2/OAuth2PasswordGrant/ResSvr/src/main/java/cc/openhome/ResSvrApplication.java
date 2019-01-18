package cc.openhome;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableResourceServer
@RestController
public class ResSvrApplication {

	public static void main(String[] args) {
		SpringApplication.run(ResSvrApplication.class, args);
	}

	@GetMapping("/hello")
	public String hello(OAuth2Authentication oauth) {
		return "hello " + oauth.getPrincipal();
	}

	@Bean
	public ResourceServerConfigurer resourceServerConfigurer() {
		return new ResourceServerConfigurer() {
			@Override
			public void configure(HttpSecurity http) throws Exception {
				http.authorizeRequests().antMatchers("/hello").access("#oauth2.hasAnyScope('account', 'message', 'email')");
			}

			@Override
			public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
				resources.resourceId("resource");
			}
		};
	}
	
}
