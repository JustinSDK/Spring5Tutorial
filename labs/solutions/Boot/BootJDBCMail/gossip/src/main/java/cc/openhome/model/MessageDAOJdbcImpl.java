package cc.openhome.model;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class MessageDAOJdbcImpl implements MessageDAO {
	@Autowired
	private JdbcTemplate jdbcTemplate;

    @Override
    public List<Message> messagesBy(String username) {
        return jdbcTemplate.queryForList("SELECT * FROM t_message WHERE name = ?", username).stream()
                .map(row -> new Message(
                     row.get("NAME").toString(),
                     Long.valueOf(row.get("TIME").toString()),
                     row.get("BLABLA").toString()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public void createMessage(Message message) {
    	jdbcTemplate.update("INSERT INTO t_message(name, time, blabla) VALUES(?, ?, ?)", 
                message.getUsername(),
                message.getMillis(),
                message.getBlabla()
            );
    }

    @Override
    public void deleteMessageBy(String username, String millis) {
    	jdbcTemplate.update("DELETE FROM t_message WHERE name = ? AND time = ?", 
                username,
                Long.parseLong(millis)
            );
    }

    @Override
    public List<Message> newestMessages(int n) {
        return jdbcTemplate.queryForList("SELECT * FROM t_message ORDER BY time DESC LIMIT ?", n).stream()
                .map(row -> new Message(
                     row.get("NAME").toString(),
                     Long.valueOf(row.get("TIME").toString()),
                     row.get("BLABLA").toString()
                ))
                .collect(Collectors.toList());
    }

}
