package cc.openhome;

import java.io.IOException;

import cc.openhome.model.UserService;

public class Main {

	public static void main(String[] args) throws IOException {
		UserService userService = Service.getUserService();
        
        userService.messages("caterpillar")
                   .forEach(message -> {
                       System.out.printf("%s\t%s%n",
                           message.getLocalDateTime(),
                           message.getBlabla());
                   });

	}


}
