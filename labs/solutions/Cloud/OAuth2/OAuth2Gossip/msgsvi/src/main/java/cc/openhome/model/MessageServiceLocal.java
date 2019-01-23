package cc.openhome.model;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageServiceLocal implements MessageService {
	@Autowired
	private MessageDAO messageDAO;
	
    public List<Message> messages(String username) {
        List<Message> messages = messageDAO.messagesBy(username);
        messages.sort(Comparator.comparing(Message::getMillis).reversed());
        return messages;
    }   
    
    public void addMessage(String username, String blabla) {
    	messageDAO.save(new Message(
                username, Instant.now().toEpochMilli(), blabla));
    }    
    
    public void deleteMessage(String username, String millis) {
        messageDAO.deleteMessageBy(username, millis);
    }
    
    public List<Message> newestMessages(int n) {
        return messageDAO.newestMessages(n);
    }
}
