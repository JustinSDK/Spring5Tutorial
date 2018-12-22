package cc.openhome.controller;

import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import cc.openhome.model.UserService;
import reactor.core.publisher.Mono;


@Controller
public class DisplayController {
	@Value("${path.view.index}")
    private String INDEX_PATH;
	
	@Value("${path.view.user}")
    private String USER_PATH;
	
	@Autowired
	private UserService userService;
    
//    @GetMapping("/")
//    public String index(Model model) {
//        model.addAttribute("newest", reactiveUserService.newestMessages(10));
//        return INDEX_PATH;
//    }

    @GetMapping("/")
    public Mono<String> index(Model model) {
    	return userService
		    	    .newestMessages(10)
		    	    .collectList()
		    	    .map(messages -> model.addAttribute("newest", messages))
		    	    .then(Mono.just(INDEX_PATH));
    	    
    }
	
    @GetMapping("user/{username}")
    public Mono<String> user(
            @PathVariable("username") String username,
            Model model) {

    	return userService
		           .userExisted(username)
                   .doOnNext(isExisted -> {
                	   if(isExisted) {
                		   model.addAttribute("messages", userService.messages(username));
                	   }
                	   else {
                		   model.addAttribute("errors", Arrays.asList(String.format("%s 還沒有發表訊息", username)));
                	   }
                   })
                   .then(Mono.just(USER_PATH));
    }
}
