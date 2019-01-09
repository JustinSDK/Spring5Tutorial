package cc.openhome.gossip;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

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
public class MsgApplication {
	public static void main(String[] args) {
		SpringApplication.run(MsgApplication.class, args);
	}
}
