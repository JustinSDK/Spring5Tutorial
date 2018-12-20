package cc.openhome;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import reactor.core.publisher.Mono;

public interface AccountDAO extends ReactiveCrudRepository<Account, Integer> {
	@Query("SELECT * FROM account WHERE name = $1")
    Mono<Account> accountByUsername(String name);
	
	@Query("SELECT * FROM account WHERE email = $1")
	Mono<Account> accountByEmail(String email);
}
