package cc.openhome;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;

@SpringBootApplication
@EnableAuthorizationServer
public class AuthSvrApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthSvrApplication.class, args);
	}

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Bean
	public AuthorizationServerConfigurer authorizationServerConfigurer() {
		return new AuthorizationServerConfigurerAdapter() {

			@Override
			public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
				clients.inMemory()
				       .withClient("webclient")
				       .secret(passwordEncoder.encode("webclient12345678"))
				       .scopes("account", "message", "email")
				       .resourceIds("resource")
				       .authorizedGrantTypes("client_credentials");
			}
			
			@Override
			public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
			   oauthServer.checkTokenAccess("isAuthenticated()");    
			}
		};
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
	  return new BCryptPasswordEncoder();
	}
}

