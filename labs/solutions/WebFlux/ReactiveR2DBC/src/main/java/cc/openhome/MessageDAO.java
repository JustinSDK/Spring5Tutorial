package cc.openhome;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import reactor.core.publisher.Flux;

public interface MessageDAO extends ReactiveCrudRepository<Message, Integer> {
    @Query("SELECT * FROM message m WHERE m.username = $1")
    Flux<Message> messagesBy(String username);

    @Query("SELECT * FROM message ORDER BY millis DESC LIMIT $1")
    Flux<Message> newestMessages(int n);
}