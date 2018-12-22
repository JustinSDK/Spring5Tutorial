package cc.openhome.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.server.ServerWebExchange;

import cc.openhome.model.EmailService;
import cc.openhome.model.UserService;
import reactor.core.publisher.Mono;

@Controller
public class AccountController {
	@Value("#{'redirect:' + '${path.url.index}'}")
    private String REDIRECT_INDEX_PATH;
	
	@Value("${path.view.register_success}")
    private String REGISTER_SUCCESS_PATH;
	
	@Value("${path.view.register_form}")
    private String REGISTER_FORM_PATH;
	
	@Value("${path.view.verify}")
    private String VERIFY_PATH;
	
	@Value("${path.view.forgot}")
    private String FORGOT_PATH;
	
	@Value("${path.view.reset_password_form}")
    private String RESET_PASSWORD_FORM_PATH;
	
	@Value("${path.view.reset_password_success}")
    private String RESET_PASSWORD_SUCCESS_PATH;

	@Autowired
	private UserService userService;
	
	@Autowired
	private EmailService emailService;
	
    @GetMapping("register")
    public String registerForm() {
        return REGISTER_FORM_PATH;
    }
    
    @PostMapping("register")
    public Mono<String> register(
            @Valid RegisterForm form,
            BindingResult bindingResult,
            Model model) {

    	return toList(bindingResult)
		    	   .flatMap(errors -> {
		    	       if(errors.isEmpty()) {
		    	       	    return userService
		    			        	    .tryCreateUser(form.getEmail(), form.getUsername(), form.getPassword())
		    			        	    .flatMap(acct -> emailService.validationLink(acct))
		    			        	    .switchIfEmpty(emailService.failedRegistration(form.getUsername(), form.getEmail()))
		    			        	    .then(Mono.just(REGISTER_SUCCESS_PATH));
		    	       } 
		    	       model.addAttribute("errors", errors);
		    	       return Mono.just(REGISTER_FORM_PATH);
		    	   });
    }
    
    @GetMapping("verify")
    public Mono<String> verify(
    		@RequestParam String email,
    		@RequestParam String token,
            Model model) {
    	
		return userService.verify(email, token)
		                  .doOnNext(acct -> model.addAttribute("acct", acct))
		                  .then(Mono.just(VERIFY_PATH));
    }
    
    @PostMapping("forgot")
    public Mono<String> forgot(
    		ServerWebExchange webExchange, 
            Model model) {
    	
    	return webExchange
    			   .getFormData()
    	           .flatMap(formData -> {
	    	           	String name = formData.getFirst("name");
	    	        	String email = formData.getFirst("email");
	    	        	model.addAttribute("email", email);
	    	        	return userService.accountByNameEmail(name, email);
    	           })
    	           .flatMap(acct -> emailService.passwordResetLink(acct))
    	           .then(Mono.just(FORGOT_PATH));
    }
    
    @GetMapping("reset_password")
    public Mono<String> resetPasswordForm(
    		@RequestParam String name,
    		@RequestParam String email,
    		@RequestParam String token,
    		ServerWebExchange webExchange, 
            Model model) {
    	
    	return userService
		    	    .accountByNameEmail(name, email)
		    	    .filter(acct -> acct.getPassword().equals(token))
		    	    .flatMap(acct -> {    	    	
		                model.addAttribute("acct", acct);
		                return webExchange.getSession()
		                            .map(webSession -> webSession.getAttributes().put("token", token))
		                            .then(Mono.just(RESET_PASSWORD_FORM_PATH));
		    	    })
		    	    .switchIfEmpty(Mono.just(REDIRECT_INDEX_PATH));
    }
    
    @PostMapping("reset_password")
    public Mono<String> resetPassword( 
            @Valid ResetPasswordForm form,
            BindingResult bindingResult,
            @SessionAttribute(name = "token") String storedToken,
            Model model) {
        
        if(storedToken == null || !storedToken.equals(form.getToken())) {
            return Mono.just(REDIRECT_INDEX_PATH);
        }
        
        return toList(bindingResult)
		            .flatMap(errors -> {
		            	if(!errors.isEmpty()) {
		                    model.addAttribute("errors", errors);
		                    model.addAttribute("name", form.getName());
		                    model.addAttribute("email", form.getEmail());
		                    return Mono.just(RESET_PASSWORD_FORM_PATH);
		                } 
		                return userService
		            		         .resetPassword(form.getName(), form.getPassword())
		                             .then(Mono.just(RESET_PASSWORD_SUCCESS_PATH)); 
		            });
    } 
    
    private Mono<List<String>> toList(BindingResult bindingResult) {
    	return Mono.fromCallable(() -> {
    		List<String> errors = new ArrayList<>(); 
            if(bindingResult.hasErrors()) {
                bindingResult.getFieldErrors().forEach(err -> {
                    errors.add(err.getDefaultMessage());
                });
            }
            return errors;
    	});
    }    
}
