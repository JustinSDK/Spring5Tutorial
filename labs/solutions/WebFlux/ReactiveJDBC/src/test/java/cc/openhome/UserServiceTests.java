package cc.openhome;

import static org.junit.Assert.assertTrue;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import reactor.test.StepVerifier;


@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
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
	public void tryCreateUser() {
		StepVerifier.create(userService.tryCreateUser("caterpillar@openhome.cc", "caterpillar", "12345678"))
				     .expectComplete()
				     .verify();
		
		StepVerifier.create(userService.tryCreateUser("xxx@openhome.cc", "caterpillar", "12345678"))
				    .expectComplete()
				    .verify();
					
		
		StepVerifier.create(userService.tryCreateUser("xxx@openhome.cc", "xxx", "12345678"))
		            .assertNext(acct -> assertTrue(acct.getName().equals("xxx")))
		            .expectComplete()
                    .verify();
	}
	
	@Test
	public void addMessage() {
		StepVerifier.create(userService.addMessage("caterpillar", "blabla"))
		            .assertNext(message -> assertTrue(message.getBlabla().equals("blabla")))
		            .expectComplete()
		            .verify();
	}
	
	@Test
	public void deleteMessage() {
		StepVerifier.create(userService.deleteMessage("caterpillar", String.valueOf(1518666695521L)))
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
	
	@Test
	public void verify() {
		StepVerifier.create(userService.verify("caterpillar@openhome.cc", "$2a$10$yh5WJetawp2KloUtEoVzRuT4/WEeR5BhPdfRZGoAvnCtKAbFBP8Sa"))
		            .expectNextCount(1)
		            .expectComplete()
		            .verify();
	}	
	
	@Test
	public void resetPasswod() {
		StepVerifier.create(userService.resetPassword("admin", "22222222"))
		            .expectComplete()
		            .verify();
	}		
	
	@Test
	public void accountByNameEmail() {
		StepVerifier.create(userService.accountByNameEmail("caterpillar", "caterpillar@openhome.cc"))
			        .expectNextCount(1)
			        .expectComplete()
			        .verify();
	}
}

