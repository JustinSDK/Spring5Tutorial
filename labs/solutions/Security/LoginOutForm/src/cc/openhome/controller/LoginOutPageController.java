package cc.openhome.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginOutPageController {
	@GetMapping("login_page")
	public String login_page() {
		return "login";
	}
	
	@GetMapping("logout_page")
	public String logout_page() {
		return "logout";
	}
}
