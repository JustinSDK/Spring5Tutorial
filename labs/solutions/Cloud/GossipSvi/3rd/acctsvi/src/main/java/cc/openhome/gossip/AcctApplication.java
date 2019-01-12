package cc.openhome.gossip;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

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
public class AcctApplication {
	public static void main(String[] args) {
		SpringApplication.run(AcctApplication.class, args);
	}
    
    @Bean
    public PasswordEncoder passwordEncoder() {
    	return new BCryptPasswordEncoder();
    }
	
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
