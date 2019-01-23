package cc.openhome.feign;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import feign.RequestInterceptor;
import feign.auth.BasicAuthRequestInterceptor;

public class AuthServiceConfig {
    @Bean
    public RequestInterceptor requestInterceptor(
    		@Value("${client.web.name}") String clientName, 
    		@Value("${client.web.secret}") String clientSecret) {
         return new BasicAuthRequestInterceptor(clientName, clientSecret);
    }
}
