package cc.openhome.gossip;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
    scanBasePackages={
        "cc.openhome.controller",
        "cc.openhome.model"
    }
)
public class EmailApplication {
	public static void main(String[] args) {
		SpringApplication.run(EmailApplication.class, args);
	}
	
}
