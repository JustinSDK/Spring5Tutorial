package cc.openhome;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface MessageDAO extends CrudRepository<Message, Integer> {
    @Query("SELECT * FROM message m WHERE m.username = :username")
    List<Message> messagesBy(@Param("username") String username);

    @Modifying
    @Query("DELETE FROM message WHERE username = :username AND millis = :millis")   
    void deleteMessageBy(@Param("username") String username, @Param("millis") String millis);

    @Query("SELECT * FROM message ORDER BY millis DESC LIMIT :n")
    List<Message> newestMessages(@Param("n") int n);
}