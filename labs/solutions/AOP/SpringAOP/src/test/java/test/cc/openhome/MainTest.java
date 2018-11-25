
package test.cc.openhome;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cc.openhome.AppConfig;
import cc.openhome.model.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class)
public class MainTest {
	@Autowired
	private UserService userService;
	
    @Test public void testMessages() {
    	userService.messages("caterpillar")
        .forEach(message -> {
        	assertEquals("username should return 'caterpillar'", 
        			"caterpillar", message.getUsername());
        });
    }
    
    @Test public void testUserExisted() {
    	assertTrue(userService.userExisted("caterpillar"));
    }
}