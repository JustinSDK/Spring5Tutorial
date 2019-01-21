package cc.openhome;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@SpringBootApplication
@EnableAuthorizationServer
public class AuthSvrApplication {
	public static void main(String[] args) {
		SpringApplication.run(AuthSvrApplication.class, args);
	}
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
    @Bean
    public WebSecurityConfigurerAdapter webSecurityConfig() {
          return new WebSecurityConfigurerAdapter() {
              @Override
              protected void configure(AuthenticationManagerBuilder builder) throws Exception {
                  builder.inMemoryAuthentication()
                         .passwordEncoder(passwordEncoder)
                         .withUser("caterpillar")
	                         .password(passwordEncoder.encode("12345678"))
	                         .roles("MEMBER");
              }
          };
    }	
    
    @Autowired
    @Qualifier("webSecurityConfig")
    private WebSecurityConfigurerAdapter webSecurityConfigurerAdapter;
	
	@Bean
	public AuthorizationServerConfigurer authorizationServerConfigurer() {
		return new AuthorizationServerConfigurerAdapter() {
			@Override
			public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
				clients.inMemory()
				       .withClient("authcodeclient")
				       .secret(passwordEncoder.encode("authcodeclient12345678"))
				       .scopes("account", "message", "email")
				       .resourceIds("resource")
				       .authorizedGrantTypes("authorization_code", "refresh_token")
				       .redirectUris("http://localhost:8082/HELLO");
			}
			
			@Override
			public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
			    oauthServer.checkTokenAccess("isAuthenticated()");    
			}

			@Override
			public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
				endpoints.accessTokenConverter(accessTokenConverter())
				         .authenticationManager(webSecurityConfigurerAdapter.authenticationManagerBean())
				         .userDetailsService(webSecurityConfigurerAdapter.userDetailsServiceBean());
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

