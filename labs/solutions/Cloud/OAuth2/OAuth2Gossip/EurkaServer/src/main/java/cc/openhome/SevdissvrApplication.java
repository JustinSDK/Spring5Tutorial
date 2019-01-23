package cc.openhome;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class SevdissvrApplication {

	public static void main(String[] args) {
		SpringApplication.run(SevdissvrApplication.class, args);
	}

}

