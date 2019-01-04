package cc.openhome;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.TypeConstrainedMappingJackson2HttpMessageConverter;
import org.springframework.hateoas.mvc.TypeReferences;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@Controller
public class XyzApplication {

	public static void main(String[] args) {
		SpringApplication.run(XyzApplication.class, args);
	}

	@Autowired
	private RestTemplate restTemplate;
	
	@GetMapping("messages/{id}")
	public String user(@PathVariable("id") String id, Model model) {
		RequestEntity<Void> request = RequestEntity
				.get(URI.create(String.format("http://localhost:8080/messages/%s", id)))
				.build();
		
		 ResponseEntity<Resource<Message>> response = 
				 restTemplate.exchange(request, new ParameterizedTypeReference<Resource<Message>>(){});
		
		model.addAttribute("title", String.format("第 %s 筆訊息", id));
		model.addAttribute("messages", Arrays.asList(response.getBody().getContent()));
		return "show";
	}

	@GetMapping("{username}/messages")
	public String userMessages(@PathVariable("username") String username, Model model) {
		RequestEntity<Void> request = RequestEntity
				.get(URI.create(String.format("http://localhost:8080/messages/search/messagesBy?username=%s", username)))
				.build();
		
		ResponseEntity<PagedResources<Message>> response = 
				restTemplate.exchange(request, new TypeReferences.PagedResourcesType<Message>() {});
				
		model.addAttribute("title", String.format("%s 的訊息", username));
		model.addAttribute("messages", new ArrayList<>(response.getBody().getContent()));
		return "user";
	}


	@Bean
	public RestTemplate restTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		List<HttpMessageConverter<?>> existingConverters = restTemplate.getMessageConverters();
		List<HttpMessageConverter<?>> newConverters = new ArrayList<>();
		newConverters.add(getHalMessageConverter());
		newConverters.addAll(existingConverters);
		restTemplate.setMessageConverters(newConverters);
		return restTemplate;
	}
	
	@Autowired
	@Qualifier("halJacksonHttpMessageConverter")
	private TypeConstrainedMappingJackson2HttpMessageConverter halConverter;
	 
	public MappingJackson2HttpMessageConverter getHalMessageConverter() {
		return halConverter;
	}
}
