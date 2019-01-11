package cc.openhome.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import cc.openhome.model.AccountService;
import cc.openhome.model.Message;
import cc.openhome.model.MessageService;

@Controller
public class DisplayController {
	@Value("${path.view.index}")
    private String INDEX_PATH;
	
	@Value("${path.view.user}")
    private String USER_PATH;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private MessageService messageService;

    @GetMapping("/")
    public String index(Model model) {
        List<Message> newest = new ArrayList<>(messageService.newestMessages(10).getContent());
        model.addAttribute("newest", newest);
        return INDEX_PATH;
    }
    
    @GetMapping("user/{username}")
    public String user(
            @PathVariable("username") String username,
            Model model) {

        model.addAttribute("username", username);
        if(accountService.userExisted(username)) {
            List<Message> messages = new ArrayList<>(messageService.messages(username).getContent());
            model.addAttribute("messages", messages);
        } else {
            model.addAttribute("errors", Arrays.asList(String.format("%s 還沒有發表訊息", username)));
        }
        return USER_PATH;
    }
}
