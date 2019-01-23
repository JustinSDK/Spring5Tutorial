package cc.openhome.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import cc.openhome.model.Account;
import cc.openhome.model.AccountService;

@RestController
public class AcctController {
	@Autowired
	private AccountService accountService;
	
	@PostMapping("tryCreateUser")
	public Resource<Optional<Account>> tryCreateUser(
			@RequestParam("email") String email, 
			@RequestParam("username") String username, 
			@RequestParam("password") String password) {
		
		Optional<Account> acct = accountService.tryCreateUser(email, username, password);

		String uri = String.format("%s/tryCreateUser?email=%s&username=%s&password=%s", linkTo(AcctController.class), email, username, password);
		return new Resource<>(acct, new Link(uri));
	}
	
	@GetMapping("userExisted")
	public Resource<Boolean> userExisted(@RequestParam("username") String username) {
		String uri = String.format("%s/userExisted?username=%s", linkTo(AcctController.class), username);
		return new Resource<>(accountService.userExisted(username), new Link(uri));
	}
	
	@PutMapping("verify")
	public Resource<Optional<Account>> verify(
			@RequestParam("email") String email, 
			@RequestParam("token") String token) {
		
		Optional<Account> acct = accountService.verify(email, token);

		String uri = String.format("%s/verify?email=%s&token=%s", linkTo(AcctController.class), email, token);
		return new Resource<>(acct, new Link(uri));
	}
	
	@GetMapping("accountByName")
	public Resource<Optional<Account>> accountByNameEmail(@RequestParam("username") String username) {
		String uri = String.format("%s/accountByNameEmail?username=%s", linkTo(AcctController.class), username);
		return new Resource<>(accountService.accountByName(username), new Link(uri));
	}
	
	@GetMapping("accountByNameEmail")
	public Resource<Optional<Account>> accountByNameEmail(@RequestParam("username") String username, @RequestParam String email) {
		String uri = String.format("%s/accountByNameEmail?username=%s&email=%s", linkTo(AcctController.class), username, email);
		return new Resource<>(accountService.accountByNameEmail(username, email), new Link(uri));
	}
	
	@PutMapping("resetPassword")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void resetPassword(@RequestParam("username") String username, @RequestParam("password") String password) {
		accountService.resetPassword(username, password);
	}
}
