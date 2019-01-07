package cc.openhome.model;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
 
@Service
public class EMailServiceRest implements EmailService {
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void validationLink(Account acct) {
		RequestEntity<Account> request = RequestEntity
				.post(URI.create("http://localhost:8081/validationLink/"))
				.contentType(MediaType.APPLICATION_JSON)
				.body(acct);
		
		restTemplate.exchange(request, String.class);
    }

    @Override
    public void failedRegistration(String acctName, String acctEmail) {
		RequestEntity<Void> request = RequestEntity
				.post(URI.create(String.format("http://localhost:8081/failedRegistration/%s/%s", acctName, acctEmail)))
				.build();
		
		restTemplate.exchange(request, String.class);
    }
    

    @Override
    public void passwordResetLink(Account acct) {
		RequestEntity<Account> request = RequestEntity
				.post(URI.create("http://localhost:8081/passwordResetLink/"))
				.contentType(MediaType.APPLICATION_JSON)
				.body(acct);
		
		restTemplate.exchange(request, String.class);       
    }  

}
