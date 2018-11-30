package cc.openhome.controller;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cc.openhome.model.Message;
import cc.openhome.model.UserService;

@Controller
public class AccessController {
	@Value("#{'redirect:' + '${path.url.member}'}")
    private String REDIRECT_MEMBER_PATH;
	
	@Value("#{'redirect:' + '${path.url.index}'}")
    private String REDIRECT_INDEX_PATH;
	
	@Value("${path.view.index}")
    private String INDEX_PATH;
	
	@Autowired
	private UserService userService;
    
	
    @PostMapping("login")
    public String login(
    		@RequestParam String username, 
            @RequestParam String password,
            HttpServletRequest request) {
        
        Optional<String> optionalPasswd = userService.encryptedPassword(username, password);
        
        try {
            request.login(username, optionalPasswd.get());
            request.getSession().setAttribute("login", username);
            return REDIRECT_MEMBER_PATH;
        } catch(NoSuchElementException | ServletException e) {
            request.setAttribute("errors", Arrays.asList("登入失敗"));
            List<Message> newest = userService.newestMessages(10);
            request.setAttribute("newest", newest);
            return INDEX_PATH;
        }
    }
    
    @GetMapping("logout")
    public String logout(HttpServletRequest request) throws ServletException {
        request.logout(); 
        return REDIRECT_INDEX_PATH;
    }
}
