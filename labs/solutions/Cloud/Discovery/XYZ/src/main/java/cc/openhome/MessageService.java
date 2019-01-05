package cc.openhome;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("msg-service")
public interface MessageService {
	@GetMapping(value = "messages/{id}")
	Resource<Message> messageById(@PathVariable("id") String id);
	
	@GetMapping(value = "messages/search/messagesBy?username={username}")
	Resources<Message> messagesByUsername(@PathVariable("username") String username);
}