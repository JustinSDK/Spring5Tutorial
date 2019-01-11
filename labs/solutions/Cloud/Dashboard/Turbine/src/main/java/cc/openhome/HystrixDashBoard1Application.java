package cc.openhome;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.turbine.EnableTurbine;

@SpringBootApplication
@EnableTurbine
public class HystrixDashBoard1Application {

	public static void main(String[] args) {
		SpringApplication.run(HystrixDashBoard1Application.class, args);
	}

}

