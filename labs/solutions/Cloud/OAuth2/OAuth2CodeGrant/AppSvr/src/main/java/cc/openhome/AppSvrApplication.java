package cc.openhome;

import java.net.URI;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


@SpringBootApplication
@RestController
public class AppSvrApplication {
	public static void main(String[] args) {
		SpringApplication.run(AppSvrApplication.class, args);
	}
	
	@Autowired
	private RestTemplate restTemplate;
	
	@GetMapping("HELLO")
	public String app(@RequestParam("code") String code) {
		String accessToken = accessToken(code);
		
        RequestEntity<Void> request = 
        		RequestEntity.get(URI.create("http://localhost:8080/hello"))
		                     .header(HttpHeaders.AUTHORIZATION, baerer(accessToken))
		                     .build();
        
		return restTemplate.exchange(request, String.class).getBody().toUpperCase();
	}

	private String baerer(String accessToken) {
		return "Bearer " + accessToken;
	}

	private String accessToken(String code) {
		MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
		map.add("code", code);
		map.add("grant_type", "authorization_code");
		map.add("redirect_uri", "http://localhost:8082/HELLO");
		
		RequestEntity<MultiValueMap<String, String>> request =
				RequestEntity.post(URI.create("http://localhost:8081/oauth/token"))
				             .contentType(MediaType.MULTIPART_FORM_DATA)
				             .header(HttpHeaders.AUTHORIZATION, basic("authcodeclient", "authcodeclient12345678"))
				             .body(map);
		            
		return restTemplate.exchange(request, Token.class).getBody().getAccess_token();
	}
	
    private String basic(String username, String password) {
        return "Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
    }
    
    @Bean
    public RestTemplate restTemplate() {
    	return new RestTemplate();
    }
}

