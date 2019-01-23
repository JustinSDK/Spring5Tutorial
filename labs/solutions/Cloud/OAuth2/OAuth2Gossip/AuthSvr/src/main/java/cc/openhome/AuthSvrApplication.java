package cc.openhome;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

@SpringBootApplication
@EnableAuthorizationServer
public class AuthSvrApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthSvrApplication.class, args);
	}

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Bean
	public AuthorizationServerConfigurer authorizationServerConfigurer(
			@Value("${client.web.name}") String clientName, 
    		@Value("${client.web.secret}") String clientSecret) {
		return new AuthorizationServerConfigurerAdapter() {

			@Override
			public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
				clients.inMemory()
				       .withClient(clientName)
				       .secret(passwordEncoder.encode(clientSecret))
				       .scopes("account", "message", "email")
				       .authorizedGrantTypes("client_credentials");
			}
			
			@Override
			public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
			    oauthServer.checkTokenAccess("isAuthenticated()");    
			}

			@Override
			public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
				endpoints.accessTokenConverter(accessTokenConverter());
			}
		};
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
	  return new BCryptPasswordEncoder();
	}
	
    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey("CATERPILLAR_KEY");
        return converter;
    }
}

