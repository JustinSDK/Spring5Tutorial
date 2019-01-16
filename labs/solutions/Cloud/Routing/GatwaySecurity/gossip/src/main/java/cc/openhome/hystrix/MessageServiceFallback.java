package cc.openhome.hystrix;

import java.util.Arrays;

import org.springframework.hateoas.Resources;
import org.springframework.stereotype.Service;

import cc.openhome.model.Message;
import cc.openhome.model.MessageService;

@Service
public class MessageServiceFallback implements MessageService {
	@Override
	public Resources<Message> messages(String username) {
		return fallbackMessages();
	}

	@Override
	public void addMessage(String username, String blabla) {
		// nothing to do
	}

	@Override
	public void deleteMessage(String username, String millis) {
		// nothing to do
	}

	@Override
	public Resources<Message> newestMessages(int n) {
		return fallbackMessages();
	}

	private Resources<Message> fallbackMessages() {
		return new Resources<>(Arrays.asList(new Message("gossiper", 0L, "啊嗚！發生問題，請稍後再試！… XD")));
	}
}
