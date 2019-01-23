package cc.openhome.feign;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import cc.openhome.model.Credentials;
import feign.RequestInterceptor;

public class AccessTokenConfig {
    @Bean
    public RequestInterceptor requestInterceptor(@Autowired Credentials credentials) {
         return template -> template.header("Authorization",  credentials.bearerToken());
    }
}
