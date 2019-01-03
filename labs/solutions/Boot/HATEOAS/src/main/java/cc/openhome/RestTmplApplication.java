package cc.openhome;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static java.util.stream.Collectors.toList;
import java.util.ArrayList;
import java.util.List;

import java.util.stream.IntStream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@RequestMapping("messages")
public class RestTmplApplication {
	public static void main(String[] args) {
		SpringApplication.run(RestTmplApplication.class, args);
	}
	
	List<Message> messages = new ArrayList<Message>() {{
		add(new Message("msg1"));
		add(new Message("msg2"));
	}};
	
	@GetMapping("/")
	public Resources<Resource<Message>> index() {
		List<Resource<Message>> reslt = 
				IntStream.range(0, messages.size())
				         .mapToObj(idx -> new Resource<>(messages.get(idx), link(String.valueOf(idx + 1))))
				         .collect(toList());
				                   
		return new Resources<>(reslt, linkTo(RestTmplApplication.class).withSelfRel());
	}
	
	@GetMapping("/{id}")
	public Resource<Message> show(@PathVariable("id") String id) {
		return new Resource<>(messages.get(Integer.parseInt(id) - 1), link(id));
	}
	
	@PostMapping("/")
	public Resource<Message> create(@RequestBody Message message) {
		messages.add(message);
		return new Resource<>(message);
	}
	
	@DeleteMapping("/{id}")
	public Resource<Message> delete(@PathVariable("id") String id) {
		return new Resource<>(messages.remove(Integer.parseInt(id) - 1));
	}
	
	private Link link(String id) {
		return linkTo(RestTmplApplication.class).slash(id).withSelfRel();
	}
}

