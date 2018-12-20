package cc.openhome;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

@Service
public class UserService {
	@Autowired
    private AccountDAO accountDAO;
	
	@Autowired
	private MessageDAO messageDAO;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private Scheduler scheduler;


    public Mono<Account> tryCreateUser(String email, String username, String password)  {
    	return Mono.defer(() -> {
    		if(accountDAO.accountByEmail(email).isPresent() || accountDAO.accountByUsername(username).isPresent()) {
    			return Mono.empty();
    		}
    		return createUser(username, email, password);
    	}).subscribeOn(scheduler);
    }

    private Mono<Account> createUser(String username, String email, String password) {
    	return Mono.defer(
    		() -> Mono.just(
    			accountDAO.save(
    				new Account(
    					username, 
    					email, 
    					passwordEncoder.encode(password), 
    					0, 
    					"ROLE_MEMBER"
    				)
    	       )
    	   )
    	);
    }

    public Flux<Message> messages(String username) {
    	return Flux.defer(() -> {
    		List<Message> messages = messageDAO.messagesBy(username);
            messages.sort(Comparator.comparing(Message::getMillis).reversed());
            return Flux.fromStream(messages.stream());
    	}).subscribeOn(scheduler);
    }   
    
    public Mono<Message> addMessage(String username, String blabla) {
    	return Mono.defer(() -> Mono.just(
				messageDAO.save(
					new Message(username, Instant.now().toEpochMilli(), blabla)
				)
			)
    	).subscribeOn(scheduler);
    	
    }    
    
    public Mono<Void> deleteMessage(String username, String millis) {
    	return Mono.defer(() -> {
    		messageDAO.deleteMessageBy(username, millis);
    		return Mono.<Void>empty();
    	}).subscribeOn(scheduler);
    }
    
    public Mono<Boolean> userExisted(String username) {
        return Mono.defer(() -> {
        	return Mono.just(accountDAO.accountByUsername(username).isPresent());
        }).subscribeOn(scheduler);
    }
    
    public Flux<Message> newestMessages(int n) {
    	return Flux.defer(() -> Flux.fromStream(messageDAO.newestMessages(n).stream()))
    			   .subscribeOn(scheduler);
    }
    
    public Mono<Boolean> emailExisted(String email) {
        return Mono.defer(() -> {
        	return Mono.just(accountDAO.accountByEmail(email).isPresent());
        }).subscribeOn(scheduler);
        		
    }
    
    public Mono<Account> verify(String email, String token) {
    	return Mono.defer(() -> {
            Optional<Account> optionalAcct= accountDAO.accountByEmail(email);
            if(optionalAcct.isPresent()) {
                Account acct = optionalAcct.get();
                if(acct.getPassword().equals(token)) {
                    accountDAO.activateAccount(acct.getName());
                    return Mono.just(acct);
                }
            }
            return Mono.empty();
    	}).subscribeOn(scheduler);
    }

    public Mono<Account> accountByNameEmail(String name, String email) {
    	return Mono.defer(() -> {
            Optional<Account> optionalAcct = accountDAO.accountByUsername(name);
            if(optionalAcct.isPresent() && optionalAcct.get().getEmail().equals(email)) {
                return Mono.just(optionalAcct.get());
            }
            return Mono.empty();
    	}).subscribeOn(scheduler);
    }

    public Mono<Void> resetPassword(String name, String password) {
    	return Mono.defer(() -> {
    		accountDAO.updatePassword(name, passwordEncoder.encode(password));
    		return Mono.<Void>empty();
    	}).subscribeOn(scheduler);
    }
}
