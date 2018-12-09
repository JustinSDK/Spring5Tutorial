package cc.openhome.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import cc.openhome.model.Message;
import cc.openhome.model.UserService;

@Controller
public class DisplayController {
    @Autowired
    private UserService userService;

    @GetMapping("/user/{name}")
    public String user(
        @PathVariable("name") String name,
        Model model) {
            List<Message> messages = userService.messagesBy(name);
            model.addAttribute("username", name);
            model.addAttribute("messages", messages);
        return "user";
    }
}