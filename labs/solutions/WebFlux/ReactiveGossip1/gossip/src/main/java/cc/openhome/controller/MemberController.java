package cc.openhome.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ServerWebExchange;

import cc.openhome.model.Message;
import cc.openhome.model.UserService;

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
    public String member(
    		Principal principal, 
            Model model) {
        List<Message> messages = userService.messages(principal.getName());
        model.addAttribute("messages", messages);
        return MEMBER_PATH;
    }
    
    @PostMapping("new_message")
    protected String newMessage(
    		ServerWebExchange webExchange, 
            Principal principal, 
            Model model)  {
    	String blabla = param(webExchange, "blabla");
        if(blabla.length() == 0) {
            return REDIRECT_MEMBER_PATH;
        }        
        
        String username = principal.getName();
        if(blabla.length() <= 140) {
            userService.addMessage(username, blabla);
            return REDIRECT_MEMBER_PATH;
        }
        else {
        	model.addAttribute("blabla", blabla);
            model.addAttribute("messages", userService.messages(username));
            return MEMBER_PATH;
        }
    } 
    
    
    @PostMapping("del_message")
    protected String delMessage(
    		ServerWebExchange webExchange, 
            Principal principal) {
    	
    	String millis = param(webExchange, "millis");
        if(millis != null) {
            userService.deleteMessage(principal.getName(), millis);
        }
        return REDIRECT_MEMBER_PATH;
    }   
    
    private String param(ServerWebExchange webExchange, String paramName) {
    	return webExchange.getFormData().block().getFirst(paramName);
    }
}
