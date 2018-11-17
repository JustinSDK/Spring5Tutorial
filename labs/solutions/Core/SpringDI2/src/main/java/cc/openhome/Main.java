package cc.openhome;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import cc.openhome.model.UserService;

public class Main {
    public static void main(String[] ags) {
        ApplicationContext context = 
                new AnnotationConfigApplicationContext(cc.openhome.AppConfig.class);
        
        UserService userService = context.getBean(cc.openhome.model.UserService.class);
        
        userService.messages("caterpillar")
                   .forEach(message -> {
                       System.out.printf("%s\t%s%n",
                               message.getLocalDateTime(),
                               message.getBlabla());
                   });
    }
}
