package cc.openhome;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource
public interface MessageDAO extends CrudRepository<Message, Integer> {
	@RestResource(exported = true)
    @Query("SELECT * FROM message m WHERE m.username = :username")
    List<Message> messagesBy(@Param("username") String username);

	@RestResource(exported = true)
    @Modifying
    @Query("DELETE FROM message WHERE username = :username AND millis = :millis")   
    void deleteMessageBy(@Param("username") String username, @Param("millis") String millis);

	@RestResource(exported = true)
    @Query("SELECT * FROM message ORDER BY millis DESC LIMIT :n")
    List<Message> newestMessages(@Param("n") int n);

	@Override
	@RestResource(exported = false)
	void delete(Message message);
}