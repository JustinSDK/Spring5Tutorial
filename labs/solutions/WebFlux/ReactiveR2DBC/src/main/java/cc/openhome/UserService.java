package cc.openhome;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserService {
	@Autowired
    private AccountDAO accountDAO;
	
	@Autowired
	private MessageDAO messageDAO;
	
    public Flux<Message> messages(String username) {
    	return messageDAO.messagesBy(username);
    }   
    
    public Mono<Boolean> userExisted(String username) {
    	return accountDAO.accountByUsername(username)
    	                 .map(acct -> true)
    	                 .switchIfEmpty(Mono.just(false));
    }
    
    public Flux<Message> newestMessages(int n) {
    	return messageDAO.newestMessages(n);
    }
    
    public Mono<Boolean> emailExisted(String email) {
    	return accountDAO.accountByEmail(email)
                .map(acct -> true)
                .switchIfEmpty(Mono.just(false));
    }

    public Mono<Account> accountByNameEmail(String name, String email) {
    	return accountDAO.accountByUsername(name)
    	                 .flatMap(acct -> acct.getEmail().equals(email) ? Mono.just(acct) : Mono.empty());
    }
}
