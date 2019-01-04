package cc.openhome;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.hal.Jackson2HalModule;
import org.springframework.hateoas.mvc.TypeConstrainedMappingJackson2HttpMessageConverter;
import org.springframework.hateoas.mvc.TypeReferences;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@EnableDiscoveryClient
@SpringBootTest
public class RestTmplApplicationTests {
	@Autowired
	private DiscoveryClient client;
	
	private String serviceUri;
	
	@Before
	public void setUp() {
		serviceUri = client.getInstances("msg-service").get(0).getUri().toString();
	}
	
	private RestTemplate restTemplate = restTemplate();
	
	@Test
	public void show() {
		RequestEntity<Void> request = RequestEntity
				.get(URI.create(String.format("%s/messages/%s", serviceUri, "1")))
				.build();
		
		 ResponseEntity<Resource<Message>> response = 
				 restTemplate.exchange(request, new ParameterizedTypeReference<Resource<Message>>(){});
		
		assertNotNull(response.getBody().getContent());
	}
	
	@Test
	public void index() {
		RequestEntity<Void> request = RequestEntity
				.get(URI.create(String.format("%s/messages", serviceUri)))
				.build();
		
		ResponseEntity<PagedResources<Message>> response = 
				restTemplate.exchange(request, new TypeReferences.PagedResourcesType<Message>() {});

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

