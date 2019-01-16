package cc.openhome.model;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("gateway/api/email")
public interface EmailService {
	@PostMapping("validationLink")
    public void validationLink(@RequestBody Account acct);
	
	@PostMapping("failedRegistration/{acctName}/{acctEmail}")
    public void failedRegistration(@PathVariable("acctName") String acctName, @PathVariable("acctEmail") String acctEmail);
	
	@PostMapping("passwordResetLink")
    public void passwordResetLink(@RequestBody Account account);
}
