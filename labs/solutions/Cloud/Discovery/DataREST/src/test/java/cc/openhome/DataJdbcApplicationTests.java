package cc.openhome;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DataJdbcApplicationTests {
    @Autowired
    MessageRepository messageRepository;
    
    @Autowired
    MessageDAO messageDAO;
	
	@Test
	public void messageRepository() {
		Message message = messageRepository.save(
			new Message(
				"caterpillar",
				1518666769369L, 
				"JavaScript 名稱空間管理 https://openhome.cc/Gossip/ECMAScript/NameSpace.html"
 	        )
		);
	
	    assertNotNull(message.getId());
	}

	@Test
	public void messageDAO() {
		messageDAO.save(
			new Message(
				"caterpillar",
				1518666769369L, 
				"JavaScript 名稱空間管理 https://openhome.cc/Gossip/ECMAScript/NameSpace.html"
 	        )
		);
		
	    assertEquals(messageDAO.messagesBy("caterpillar").size(), 1);
	    
	    assertEquals(messageDAO.newestMessages(1).size(), 1);
	    
	    messageDAO.deleteMessageBy("caterpillar", String.valueOf(1518666769369L));
	    assertEquals(messageDAO.messagesBy("caterpillar").size(), 0);
	}
}

