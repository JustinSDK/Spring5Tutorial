package cc.openhome.gossip;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@SpringBootApplication(
    scanBasePackages={
        "cc.openhome.controller",
        "cc.openhome.model"
    }
)
@EnableJdbcRepositories(
	basePackages= {
		"cc.openhome.model"
	}
)
@EnableResourceServer
public class MsgApplication {
	public static void main(String[] args) {
		SpringApplication.run(MsgApplication.class, args);
	}
	

    @Bean
    public ResourceServerConfigurer resourceServerConfigurer() {
        return new ResourceServerConfigurer() {
            @Override
            public void configure(HttpSecurity http) throws Exception {
                http.authorizeRequests()
                    .anyRequest().access("#oauth2.hasScope('message')");
            }

            @Override
            public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
                resources.tokenStore(tokenStore());
            }
        };
    }
	
    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey("CATERPILLAR_KEY");
        return converter;
    }	
}
