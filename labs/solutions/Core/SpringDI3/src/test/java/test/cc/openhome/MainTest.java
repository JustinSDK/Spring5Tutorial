package test.cc.openhome;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cc.openhome.AppConfig;
import cc.openhome.model.UserService;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class)
@ActiveProfiles("test")
public class MainTest {
	@Autowired
	private UserService userService;
	
    @Test public void testMain() {
    	userService.messages("caterpillar")
        .forEach(message -> {
        	assertEquals("username should return 'caterpillar'", 
        			"caterpillar", message.getUsername());
        });
    }
}
