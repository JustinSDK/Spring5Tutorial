package cc.openhome.controller;

import static java.util.stream.Collectors.toList;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import java.util.List;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import cc.openhome.model.Message;
import cc.openhome.model.MessageService;

@RestController
public class MsgController {
	@Autowired
	private MessageService messageService;
	
	@GetMapping("messagesBy")
	public Resources<Resource<Message>> messagesBy(@RequestParam("username") String username) {
		List<Message> messages = messageService.messages(username);
		
		List<Resource<Message>> result = 
				IntStream.range(0, messages.size())
				         .mapToObj(idx -> new Resource<>(messages.get(idx)))
				         .collect(toList());

		String uri = String.format("%s/messagesBy?username=%s", linkTo(MsgController.class), username);
		return new Resources<>(result, new Link(uri));
	}
	
	@PostMapping("addMessage")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void addMessage(@RequestParam("username") String username, @RequestParam("blabla") String blabla) {
		messageService.addMessage(username, blabla);
	}
	
	@DeleteMapping("deleteMessage")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void deleteMessage(@RequestParam("username") String username, @RequestParam("millis") String millis) {
		messageService.deleteMessage(username, millis);
	}
	
	@GetMapping("newestMessages")
	public Resources<Resource<Message>> newestMessages(@RequestParam("n") int n) {
		List<Message> messages = messageService.newestMessages(n);
		
		List<Resource<Message>> result = 
				IntStream.range(0, messages.size())
				         .mapToObj(idx -> new Resource<>(messages.get(idx)))
				         .collect(toList());

		String uri = String.format("%s/newestMessages?n=%d", linkTo(MsgController.class), n);
		return new Resources<>(result, new Link(uri));
	}
}
