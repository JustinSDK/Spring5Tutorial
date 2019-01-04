package cc.openhome;

import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableFeignClients
@Controller
public class XyzApplication {

	public static void main(String[] args) {
		SpringApplication.run(XyzApplication.class, args);
	}
	
	@Autowired
	private MessageService messageService;

	@GetMapping("messages/{id}")
	public String user(@PathVariable("id") String id, Model model) {
		model.addAttribute("title", String.format("第 %s 筆訊息", id));
		model.addAttribute("messages", Arrays.asList(messageService.messageById(id).getContent()));
		return "show";
	}

	@GetMapping("{username}/messages")
	public String userMessages(@PathVariable("username") String username, Model model) {
		model.addAttribute("title", String.format("%s 的訊息", username));
		model.addAttribute("messages", new ArrayList<>(messageService.messagesByUsername(username).getContent()));
		return "user";
	}
	
	@Bean
	@LoadBalanced
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
