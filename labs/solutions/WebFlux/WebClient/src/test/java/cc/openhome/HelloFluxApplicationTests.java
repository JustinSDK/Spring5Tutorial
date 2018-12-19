package cc.openhome;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HelloFluxApplicationTests {
	@Test
	public void testHello() {
        Mono<String> userName =
    		WebClient.create("http://localhost:8080/")
    		         .get()
		             .uri("hello/caterpillar")
		             .accept(MediaType.APPLICATION_JSON)
		             .exchange()
		             .flatMap(response -> response.bodyToMono(User.class))
		             .map(User::getName);
        
        StepVerifier.create(userName)
                    .expectNext("caterpillar")
                    .expectComplete()
                    .verify();
	}
	

	@Test
	public void serverSentEvent() {
		 Flux<Long> numbers = 
		     WebClient.create("http://localhost:8080")
		              .get()
		              .uri("/randomNumber")
		              .accept(MediaType.TEXT_EVENT_STREAM)
		              .retrieve()
		              .bodyToFlux(new ParameterizedTypeReference<ServerSentEvent<String>>() {})
		              .map(event -> Long.parseLong(event.data()));

	    StepVerifier.create(numbers)
	                .expectNextCount(5)
	                .expectComplete()
	                .verify();
	}
	
	@Test
	public void htmlHi() {
		WebClient.create("http://localhost:8080/")
		         .get()
		         .uri("hi/caterpillar")
		         .accept(MediaType.TEXT_HTML)
		         .exchange()
		         .flatMap(resp -> resp.bodyToMono(String.class))
		         .subscribe(html -> assertTrue(html.contains("caterpillar")));		         
	}
}

