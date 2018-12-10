package cc.openhome.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserService {
	@Autowired
	private MessageDAO messageDAO;
	
	public List<Message> messagesBy(String name) {
		messageDAO.messagesBy(name);
		return messageDAO.messagesBy(name);
	}

}