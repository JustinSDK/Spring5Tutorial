package cc.openhome;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.hal.Jackson2HalModule;
import org.springframework.hateoas.mvc.TypeConstrainedMappingJackson2HttpMessageConverter;
import org.springframework.hateoas.mvc.TypeReferences;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RestTmplApplicationTests {
	private RestTemplate restTemplate = restTemplate();
	
	@Test
	public void show() {
		RequestEntity<Void> request = RequestEntity
				.get(URI.create(String.format("http://localhost:8080/messages/%s", "1")))
				.build();
		
		 ResponseEntity<Resource<Message>> response = 
				 restTemplate.exchange(request, new ParameterizedTypeReference<Resource<Message>>(){});
		
		assertNotNull(response.getBody().getContent().getText());
	}
	
	@Test
	public void create() {
		Message message = new Message("new message");
		
		RequestEntity<Message> request = RequestEntity
				.post(URI.create("http://localhost:8080/messages/"))
				.contentType(MediaType.APPLICATION_JSON)
				.body(message);
		
		ResponseEntity<Resource<Message>> response = 
				 restTemplate.exchange(request, new ParameterizedTypeReference<Resource<Message>>(){});
		
		assertEquals(response.getBody().getContent().getText(), "new message");
	}

	@Test
	public void delete() {
		RequestEntity<Void> request = RequestEntity
				.delete(URI.create(String.format("http://localhost:8080/messages/%s", "1")))
				.build();
		
		ResponseEntity<Resource<Message>> response = 
				 restTemplate.exchange(request, new ParameterizedTypeReference<Resource<Message>>(){});
		
		assertEquals(response.getBody().getContent().getText(), "msg1");
	}
	
	@Test
	public void index() {
		RequestEntity<Void> request = RequestEntity
				.get(URI.create("http://localhost:8080/messages/"))
				.build();
		
		ResponseEntity<Resources<Message>> response = 
				restTemplate.exchange(request, new TypeReferences.ResourcesType<Message>() {});

		assertTrue(response.getBody().getContent().size() > 0);
	}
	
	private MappingJackson2HttpMessageConverter getHalMessageConverter() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.registerModule(new Jackson2HalModule());
		MappingJackson2HttpMessageConverter halConverter = new TypeConstrainedMappingJackson2HttpMessageConverter(
				ResourceSupport.class);
		halConverter.setSupportedMediaTypes(Arrays.asList(MediaTypes.HAL_JSON));
		halConverter.setObjectMapper(objectMapper);
		return halConverter;
	}
	
	public RestTemplate restTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		List<HttpMessageConverter<?>> existingConverters = restTemplate.getMessageConverters();
		List<HttpMessageConverter<?>> newConverters = new ArrayList<>();
		newConverters.add(getHalMessageConverter());
		newConverters.addAll(existingConverters);
		restTemplate.setMessageConverters(newConverters);
		return restTemplate;
	}
}

