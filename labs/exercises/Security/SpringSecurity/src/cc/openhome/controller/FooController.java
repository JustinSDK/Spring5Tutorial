package cc.openhome.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class FooController {
	@GetMapping("/")
	public String index(Model model) {
		return fooResponse("/", model);
	}
	
	@GetMapping("/{path}")
	public String foo(@PathVariable String path, Model model) {
		return fooResponse(path, model);
	}
	
	private String fooResponse(String path, Model model) {
		model.addAttribute("path", path);
		return "foo";
	}
}
