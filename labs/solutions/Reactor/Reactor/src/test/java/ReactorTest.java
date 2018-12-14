import org.junit.Test;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class ReactorTest {
	
	private Flux<String> skills() {
		return Flux.just("java", "python", "javascript");
	}
	
    @Test
    public void testSkills() {
    	StepVerifier.create(skills())
    	            .expectNext("java", "python", "javascript")
    	            .expectComplete()
    	            .verify();
    }
    
    @Test
    public void testUpperSkills() {
    	StepVerifier.create(skills().map(String::toUpperCase))
			        .expectNext("JAVA", "PYTHON", "JAVASCRIPT")
			        .expectComplete()
			        .verify();

    }
}
