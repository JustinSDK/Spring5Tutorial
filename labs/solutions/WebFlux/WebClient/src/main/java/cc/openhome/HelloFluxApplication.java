package cc.openhome;

import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

@SpringBootApplication
@Controller
public class HelloFluxApplication {

	public static void main(String[] args) {
		SpringApplication.run(HelloFluxApplication.class, args);
	}
	
	@GetMapping("/hi/{name}") 
	public Mono<String> hi(@PathVariable("name") String name, Model model) {
		model.addAttribute("name", Mono.just(name));
		return Mono.just("hi");
	}
	
    @GetMapping("/hello/{name}") 
    @ResponseBody
    public Mono<User> hello(@PathVariable("name") String name) {
        return Mono.just(new User(name));
    }
	
	@GetMapping("/randomNumber")
	@ResponseBody
    public Flux<ServerSentEvent<Long>> randomNumbers() {
		return Flux.interval(Duration.ofSeconds(1))
				   .map(tick -> Tuples.of(tick, ThreadLocalRandom.current().nextLong()))
				   .map(this::randomNumberEvent)
				   .take(5);
	}
	
	public ServerSentEvent<Long> randomNumberEvent(Tuple2<Long, Long> data) {
		return ServerSentEvent.<Long>builder()
						      .event("randomNumber")
						      .id(Long.toString(data.getT1()))
						      .data(data.getT2())
						      .build();
	}
}



