package cc.openhome.model;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import cc.openhome.hystrix.MessageServiceFallback;

@FeignClient(name = "gateway/api/msg", fallback = MessageServiceFallback.class)
public interface MessageService {
	@GetMapping("messagesBy?username={username}")
    Resources<Message> messages(@PathVariable("username") String username);
	
	@PostMapping("addMessage?username={username}&blabla={blabla}")
    void addMessage(@PathVariable("username") String username, @PathVariable("blabla") String blabla);
	
	@DeleteMapping("deleteMessage?username={username}&millis={millis}")
    void deleteMessage(@PathVariable("username")String username, @PathVariable("millis") String millis);

	@GetMapping("newestMessages?n={n}")
	Resources<Message> newestMessages(@PathVariable("n") int n);
}
