package cc.openhome;


import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class RestTmplApplication {
	public static void main(String[] args) {
		SpringApplication.run(RestTmplApplication.class, args);
	}
	
	List<Message> messages = new ArrayList<Message>() {{
		add(new Message("msg1"));
		add(new Message("msg2"));
	}};
	
	@GetMapping("messages")
	public List<Message> index() {
		return messages;
	}
	
	@GetMapping("messages/{id}")
	public Message show(@PathVariable("id") String id) {
		return messages.get(Integer.parseInt(id) - 1);
	}
	
	@PostMapping("messages")
	public Message create(@RequestBody Message message) {
		messages.add(message);
		return message;
	}
	
	@DeleteMapping("messages/{id}")
	public Message delete(@PathVariable("id") String id) {
		return messages.remove(Integer.parseInt(id) - 1);
	}
}

