package cc.openhome;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import reactor.test.StepVerifier;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTests {
    @Autowired
    private UserService userService;
	

	@Test
	public void userExisted() {
		StepVerifier.create(userService.userExisted("caterpillar"))
                    .expectNext(true)
                    .expectComplete()
                    .verify();
		StepVerifier.create(userService.userExisted("ooo"))
		            .expectNext(false)
			        .expectComplete()
			        .verify();
	}

	@Test
	public void emailExisted() {
		StepVerifier.create(userService.emailExisted("caterpillar@openhome.cc"))
		            .expectNext(true)
		            .expectComplete()
                    .verify();
		StepVerifier.create(userService.emailExisted("ooo@openhome.cc"))
		            .expectNext(false)
			        .expectComplete()
			        .verify();		
	}
	
	@Test
	public void messages() {
		StepVerifier.create(userService.messages("caterpillar"))
		            .expectNextCount(3)
		            .expectComplete()
		            .verify();
	}
	
	@Test
	public void newestMessages() {
		StepVerifier.create(userService.newestMessages(10))
		            .expectNextCount(3)
		            .expectComplete()
		            .verify();
	}	
}

