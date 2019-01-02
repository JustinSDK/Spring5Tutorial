package cc.openhome;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.URI;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RestTmplApplicationTests {
	private RestTemplate restTemplate = new RestTemplate();
	
	@Test
	public void show() {
		Message message = restTemplate.getForObject("http://localhost:8080/messages/{id}", Message.class, "1");
		assertNotNull(message.getText());
	}
	
	@Test
	public void create() {
		Message message = new Message("new message");
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    HttpEntity<Message> request = new HttpEntity<>(message, headers);
	    
	    message = restTemplate.postForObject("http://localhost:8080/messages", request, Message.class);
		assertEquals(message.getText(), "new message");
	}

	@Test
	public void delete() {
		restTemplate.delete("http://localhost:8080/messages/{id}", "1");
		Message message = restTemplate.getForObject("http://localhost:8080/messages/{id}", Message.class, "1");
		assertEquals(message.getText(), "msg2");
	}
	
	@Test
	public void index() {
		RequestEntity<Void> request = RequestEntity
				.get(URI.create("http://localhost:8080/messages/"))
				.accept(MediaType.APPLICATION_JSON)
				.build();

		ResponseEntity<List<Message>> response = restTemplate
				.exchange(request,new ParameterizedTypeReference<List<Message>>(){});
		List<Message> messages = response.getBody();
		assertTrue(messages.size() > 0);
	}
}

