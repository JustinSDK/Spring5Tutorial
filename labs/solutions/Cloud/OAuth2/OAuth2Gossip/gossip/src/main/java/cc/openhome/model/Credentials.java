package cc.openhome.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Credentials {
    @Autowired
	private AuthService authService;
	
	private Long expirationSeconds = 0L;
	private String bearerToken;
	
	public String bearerToken() {
		if(expirationSeconds < java.time.Instant.now().getEpochSecond()) {
			TokenInfo token= authService.token();
			expirationSeconds = java.time.Instant.now().getEpochSecond() + Long.parseLong(token.getExpires_in());
			bearerToken = "Bearer " + token.getAccess_token();
		}		
		return bearerToken;
	}
}

