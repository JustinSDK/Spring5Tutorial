package cc.openhome.gossip;

import java.net.URI;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import cc.openhome.model.Account;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EmailServiceTests {
	private RestTemplate restTemplate = new RestTemplate();	
	
	@Test
	public void validationLink() {
		Account acct = new Account("123", "456", "789");
		RequestEntity<Account> request = RequestEntity
				.post(URI.create("http://localhost:8081/validationLink/"))
				.contentType(MediaType.APPLICATION_JSON)
				.body(acct);
		
		restTemplate.exchange(request, String.class);
	}
	
	@Test
	public void failedRegistration() {
		RequestEntity<Void> request = RequestEntity
				.post(URI.create("http://localhost:8081/failedRegistration/123/456"))
				.build();
		
		restTemplate.exchange(request, String.class);
	}
	
	
	@Test
	public void passwordResetLink() {
		Account acct = new Account("123", "456", "789");
		RequestEntity<Account> request = RequestEntity
				.post(URI.create("http://localhost:8081/passwordResetLink/"))
				.contentType(MediaType.APPLICATION_JSON)
				.body(acct);
		
		restTemplate.exchange(request, String.class);
	}
	

}
