package cc.openhome.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

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
            @SessionAttribute("login") String username, 
            Model model) {
        List<Message> messages = userService.messages(username);
        model.addAttribute("messages", messages);
        return MEMBER_PATH;
    }
    
    @PostMapping("new_message")
    protected String newMessage(
            @RequestParam String blabla, 
            @SessionAttribute("login") String username, 
            Model model)  {
        
        if(blabla.length() == 0) {
            return REDIRECT_MEMBER_PATH;
        }        
       
        if(blabla.length() <= 140) {
            userService.addMessage(username, blabla);
            return REDIRECT_MEMBER_PATH;
        }
        else {
            model.addAttribute("messages", userService.messages(username));
            return MEMBER_PATH;
        }
    } 
    
    
    @PostMapping("del_message")
    protected String delMessage(
            @RequestParam String millis, 
            @SessionAttribute("login") String username) {
        
        if(millis != null) {
            userService.deleteMessage(username, millis);
        }
        return REDIRECT_MEMBER_PATH;
    }   
}
