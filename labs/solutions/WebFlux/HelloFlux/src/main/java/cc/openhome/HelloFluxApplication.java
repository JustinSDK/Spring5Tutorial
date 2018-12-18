package cc.openhome;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@SpringBootApplication
@RestController
public class HelloFluxApplication {

	public static void main(String[] args) {
		SpringApplication.run(HelloFluxApplication.class, args);
	}

	@GetMapping("/hello/{name}") 
	public Mono<User> hello(@PathVariable("name") String name) {
		return Mono.just(new User(name));
	}
}



