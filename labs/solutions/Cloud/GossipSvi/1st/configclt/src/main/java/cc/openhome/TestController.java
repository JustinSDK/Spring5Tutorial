package cc.openhome;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
public class TestController {
	@Value("${spring.datasource.username}")
	private String dbUser;
	
	@GetMapping("dbUser")
	public String dbUser() {
		return dbUser;
	}
}
