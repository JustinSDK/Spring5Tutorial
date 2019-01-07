package cc.openhome.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cc.openhome.model.Message;
import cc.openhome.model.MessageService;

@Controller
public class MemberController {
	@Value("${path.view.member}")
    private String MEMBER_PATH;
	
	@Value( "#{'redirect:' + '${path.url.member}'}")
    private String REDIRECT_MEMBER_PATH;
	
	@Autowired
	private MessageService messageService;
	
    @GetMapping("member")
    @PostMapping("member")
    public String member(
    		Principal principal, 
            Model model) {
        List<Message> messages = messageService.messages(principal.getName());
        model.addAttribute("messages", messages);
        return MEMBER_PATH;
    }
    
    @PostMapping("new_message")
    protected String newMessage(
            @RequestParam String blabla, 
            Principal principal, 
            Model model)  {
        
        if(blabla.length() == 0) {
            return REDIRECT_MEMBER_PATH;
        }        
        
        String username = principal.getName();
        if(blabla.length() <= 140) {
        	messageService.addMessage(username, blabla);
            return REDIRECT_MEMBER_PATH;
        }
        else {
            model.addAttribute("messages", messageService.messages(username));
            return MEMBER_PATH;
        }
    } 
    
    
    @PostMapping("del_message")
    protected String delMessage(
            @RequestParam String millis, 
            Principal principal) {
        
        if(millis != null) {
        	messageService.deleteMessage(principal.getName(), millis);
        }
        return REDIRECT_MEMBER_PATH;
    }   
}
