package cc.openhome.model;

import javax.ws.rs.core.MediaType;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import cc.openhome.feign.AuthServiceConfig;

@FeignClient(name = "authsvr", configuration = AuthServiceConfig.class)
public interface AuthService {
	@PostMapping(value = "oauth/token?grant_type=client_credentials",  consumes = MediaType.APPLICATION_FORM_URLENCODED)
    TokenInfo token();
}