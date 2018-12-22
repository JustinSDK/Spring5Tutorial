package cc.openhome.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ServerWebExchange;

import cc.openhome.model.UserService;
import reactor.core.publisher.Mono;

@Controller
public class MemberController {
	@Value("${path.view.member}")
    private String MEMBER_PATH;
	
	@Value( "#{'redirect:' + '${path.url.member}'}")
    private String REDIRECT_MEMBER_PATH;
	
	@Autowired
	private UserService userService;	
    
    @GetMapping("member")
    @PostMapping("member")
    public Mono<String> member(
    		Principal principal, 
            Model model) {
    	return userService
    	           .messages(principal.getName())
		    	   .collectList()
		    	   .map(messages -> model.addAttribute("messages", messages))
		    	   .then(Mono.just(MEMBER_PATH));
    }
    
    @PostMapping("new_message")
    protected Mono<String> newMessage(
    		ServerWebExchange webExchange, 
            Principal principal, 
            Model model)  {
    	
    	return webExchange
    			   .getFormData()
    	           .map(valueMap -> valueMap.getFirst("blabla"))
    	           .flatMap(blabla -> {
    	               if(blabla.length() == 0) {
    	                   return Mono.just(REDIRECT_MEMBER_PATH);
    	               }     
    	               
    	               String username = principal.getName();
    	               if(blabla.length() <= 140) {
    	                   return userService
    	                   		    .addMessage(username, blabla)
    	                   		    .then(Mono.just(REDIRECT_MEMBER_PATH));
    	               }
    	               else {
    	               	   return userService.messages(username)
    	       	        			.collectList()
    	       	        			.doOnSuccess(messages -> {
    	       	        				model.addAttribute("messages", messages);
    	       	        				model.addAttribute("blabla", blabla);
    	       	        			})
    	       					    .then(Mono.just(MEMBER_PATH));
    	               }  
    	           });
    } 
    
    @PostMapping("del_message")
    protected Mono<String> delMessage(
    		ServerWebExchange webExchange, 
            Principal principal) {
    	
    	return webExchange
    			   .getFormData()
    	           .flatMap(valueMap -> userService.deleteMessage(principal.getName(), valueMap.getFirst("millis")))
    	           .then(Mono.just(REDIRECT_MEMBER_PATH));
    }   
}
