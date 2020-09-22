package spring.mvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BaseController {

	@RequestMapping(value = {"/login","/"},method = RequestMethod.GET)
	public String login(@RequestParam(name = "message", required = false) String message, final Model model) {
		if (message != null && !message.isEmpty()) {
			model.addAttribute("message", "Login Failed!");
		}
		return "login";
	}
	
	@RequestMapping(value = "/admin",method=RequestMethod.GET)
	public String admin() {
		return "admin";
	}
	
	@RequestMapping(value = "/logout",method=RequestMethod.GET)
	public String logout(final Model model) {
		model.addAttribute("message", "Logged out!");
		return "login";
	}
}
