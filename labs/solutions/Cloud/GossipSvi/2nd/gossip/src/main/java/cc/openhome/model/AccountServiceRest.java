package cc.openhome.model;

import java.net.URI;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.TypeReferences;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AccountServiceRest implements AccountService {
	@Autowired
    private RestTemplate restTemplate;
	
    public Optional<Account> tryCreateUser(String email, String username, String password)  {
		RequestEntity<Void> request = RequestEntity
				.post(URI.create(String.format("http://localhost:8084/tryCreateUser?email=%s&username=%s&password=%s", email, username, password)))
				.build();
		ResponseEntity<Resource<Account>> response = 
				restTemplate.exchange(request, new TypeReferences.ResourceType<Account>() {});
		return Optional.ofNullable(response.getBody().getContent());
    }
    
    public boolean userExisted(String username) {
		RequestEntity<Void> request = RequestEntity
				.post(URI.create(String.format("http://localhost:8084/userExisted?username=%s", username)))
				.build();
		ResponseEntity<Resource<Boolean>> response = 
				restTemplate.exchange(request, new TypeReferences.ResourceType<Boolean>() {});
		return response.getBody().getContent();
    }
    
    public Optional<Account> verify(String email, String token) {
		RequestEntity<Void> request = RequestEntity
				.put(URI.create(String.format("http://localhost:8084/verify?email=%s&token=%s", email, token)))
				.build();
		ResponseEntity<Resource<Account>> response = 
				restTemplate.exchange(request, new TypeReferences.ResourceType<Account>() {});
		return Optional.ofNullable(response.getBody().getContent());
    }

    public Optional<Account> accountByNameEmail(String name, String email) {
		RequestEntity<Void> request = RequestEntity
				.get(URI.create(String.format("http://localhost:8084/accountByNameEmail?username=%s&email=%s", name, email)))
				.build();
		ResponseEntity<Resource<Account>> response = 
				restTemplate.exchange(request, new TypeReferences.ResourceType<Account>() {});
		return Optional.ofNullable(response.getBody().getContent());
    }

    public void resetPassword(String name, String password) {
		RequestEntity<Void> request = RequestEntity
				.post(URI.create(String.format("http://localhost:8084/resetPassword?username=%s&password=%s", name, password)))
				.build();
		
		restTemplate.exchange(request, String.class);
    }
}
