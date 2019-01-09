package cc.openhome.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import cc.openhome.model.Account;
import cc.openhome.model.EmailService;

@RestController
public class MailController {
	@Autowired
	private EmailService emailService;
	
	@PostMapping("validationLink")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void validationLink(@RequestBody Account acct) {
		emailService.validationLink(acct);
	}
	
	@PostMapping("failedRegistration/{acctName}/{acctEmail}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void failedRegistration(@PathVariable("acctName") String acctName, @PathVariable("acctEmail") String acctEmail) {
		emailService.failedRegistration(acctName, acctEmail);
	}
	
	@PostMapping("passwordResetLink")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void passwordResetLink(@RequestBody Account acct) {
		emailService.passwordResetLink(acct);
	}
}
