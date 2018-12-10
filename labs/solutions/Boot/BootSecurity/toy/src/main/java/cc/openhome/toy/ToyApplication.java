package cc.openhome.toy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
	scanBasePackages={
		"cc.openhome.controller",
		"cc.openhome.model",
		"cc.openhome.toy"
	}
)
public class ToyApplication {
	public static void main(String[] args) {
		SpringApplication.run(ToyApplication.class, args);
	}
}
