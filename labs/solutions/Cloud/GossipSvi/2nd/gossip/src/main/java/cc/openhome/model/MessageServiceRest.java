package cc.openhome.model;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.mvc.TypeReferences;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MessageServiceRest implements MessageService {
    @Autowired
    private RestTemplate restTemplate;
	
    public List<Message> messages(String username) {
		RequestEntity<Void> request = RequestEntity
				.get(URI.create(String.format("http://localhost:8083/messagesBy?username=%s", username)))
				.build();
		
		ResponseEntity<Resources<Message>> response = 
				restTemplate.exchange(request, new TypeReferences.ResourcesType<Message>() {});
    	
        return new ArrayList<>(response.getBody().getContent());
    }   
    
    public void addMessage(String username, String blabla) {
		RequestEntity<Void> request = RequestEntity
				.post(URI.create(String.format("http://localhost:8083/addMessage?username=%s&blabla=%s", username, blabla)))
				.build();
		
		restTemplate.exchange(request, String.class);
    }    
    
    public void deleteMessage(String username, String millis) {
    	RequestEntity<Void> request = RequestEntity
				.delete(URI.create(String.format("http://localhost:8083/deleteMessage?username=%s&millis=%s", username, millis)))
				.build();
		
		restTemplate.exchange(request, String.class);
    }
    
    public List<Message> newestMessages(int n) {
		RequestEntity<Void> request = RequestEntity
				.get(URI.create(String.format("http://localhost:8083/newestMessages?n=%d", n)))
				.build();
		
		ResponseEntity<Resources<Message>> response = 
				restTemplate.exchange(request, new TypeReferences.ResourcesType<Message>() {});
    	
        return new ArrayList<>(response.getBody().getContent());
    }
}
