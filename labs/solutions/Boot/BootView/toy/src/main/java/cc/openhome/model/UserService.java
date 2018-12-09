package cc.openhome.model;

import java.util.List;
import java.util.Arrays;
import org.springframework.stereotype.Component;

@Component
public class UserService {
	public List<Message> messagesBy(String name) {
		return Arrays.asList(
            new Message(name, 1516842964223L, "我是一隻弱小的毛毛蟲，想像有天可以成為強壯的挖土機，擁有挖掘夢想的神奇手套。。。XD"),
            new Message(name, 1516844514031L, "碁峰把《Java SE 9 技術手冊》電子書放上去囉！"), 
            new Message(name, 1516844548728L, "JavaScript 名稱空間管理 https://openhome.cc/Gossip/ECMAScript/NameSpace.html")
        ); 
	}

}