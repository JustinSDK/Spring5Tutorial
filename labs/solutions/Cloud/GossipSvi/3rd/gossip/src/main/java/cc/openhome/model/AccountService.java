package cc.openhome.model;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.hateoas.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient("acctsvi")
public interface AccountService {
	@PostMapping("tryCreateUser?email={email}&username={username}&password={password}")
	Resource<Account> tryCreateUser(@PathVariable("email") String email, @PathVariable("username") String username, @PathVariable("password") String password);
    
	@GetMapping("userExisted?username={username}")
	boolean userExisted(@PathVariable("username") String username);
	
	@PutMapping("verify?email={email}&token={token}")
	Resource<Account> verify(@PathVariable("email") String email, @PathVariable("token") String token);
	
	@GetMapping("accountByNameEmail?username={username}&email={email}")
	Resource<Account> accountByNameEmail(@PathVariable("username") String name, @PathVariable("email") String email);
    
	@PutMapping("resetPassword?username={username}&password={password}")
    void resetPassword(@PathVariable("username") String name, @PathVariable("password") String password);
}
