package cc.openhome.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import org.springframework.web.bind.annotation.SessionAttributes;

import cc.openhome.model.Account;
import cc.openhome.model.EmailService;
import cc.openhome.model.AccountService;

@Controller
@SessionAttributes("token")
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
	private AccountService accountService;
	
	@Autowired
	private EmailService emailService;
	
    @GetMapping("register")
    public String registerForm() {
        return REGISTER_FORM_PATH;
    }
    
    @PostMapping("register")
    public String register(
            @Valid RegisterForm form,
            BindingResult bindingResult,
            Model model) {

        List<String> errors = toList(bindingResult);
        
        String path;
        if(errors.isEmpty()) {
            path = REGISTER_SUCCESS_PATH;
            
            Optional<Account> optionalAcct = Optional.ofNullable(accountService.tryCreateUser(
                    form.getEmail(), form.getUsername(), form.getPassword()).getContent());
            System.out.println(optionalAcct);
            if(optionalAcct.isPresent()) {
                emailService.validationLink(optionalAcct.get());
            } else {
                emailService.failedRegistration(
                    form.getUsername(), form.getEmail());
            }
        } else {
            path = REGISTER_FORM_PATH;
            model.addAttribute("errors", errors);
        }

        return path;
    }
    
    @GetMapping("verify")
    public String verify(
            @RequestParam String email, 
            @RequestParam String token, 
            Model model)  {
        model.addAttribute("acct", accountService.verify(email, token));
        return VERIFY_PATH;
    }
    
    @PostMapping("forgot")
    public String forgot(
            @RequestParam String name,
            @RequestParam String email,
            Model model) {
        
        Optional<Account> optionalAcct = Optional.ofNullable(accountService.accountByNameEmail(name, email).getContent());
        
        if(optionalAcct.isPresent()) {
            emailService.passwordResetLink(optionalAcct.get());
        }
        
        model.addAttribute("email", email);
        return FORGOT_PATH;
    }
    
    @GetMapping("reset_password")
    public String resetPasswordForm(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String token,
            Model model) {

        Optional<Account> optionalAcct = Optional.ofNullable(accountService.accountByNameEmail(name, email).getContent());
        
        if(optionalAcct.isPresent()) {
            Account acct = optionalAcct.get();
            if(acct.getPassword().equals(token)) {
                model.addAttribute("acct", acct);
                model.addAttribute("token", token);
                return RESET_PASSWORD_FORM_PATH;
            }
        }
        return REDIRECT_INDEX_PATH;
    }
    
    @PostMapping("reset_password")
    public String resetPassword( 
            @Valid ResetPasswordForm form,
            BindingResult bindingResult,
            @SessionAttribute(name = "token") String storedToken,
            Model model) {
        
        if(storedToken == null || !storedToken.equals(form.getToken())) {
            return REDIRECT_INDEX_PATH;
        }
        
        List<String> errors = toList(bindingResult);
        
        if(!errors.isEmpty()) {
            Optional<Account> optionalAcct =
            		Optional.ofNullable(accountService.accountByNameEmail(form.getName(), form.getEmail()).getContent());
            model.addAttribute("errors", errors);
            model.addAttribute("acct", optionalAcct.get());
            return RESET_PASSWORD_FORM_PATH;
        } else {
            accountService.resetPassword(form.getName(), form.getPassword());
            return RESET_PASSWORD_SUCCESS_PATH;
        }    
    } 
    
    private List<String> toList(BindingResult bindingResult) {
        List<String> errors = new ArrayList<>(); 
        if(bindingResult.hasErrors()) {
            bindingResult.getFieldErrors().forEach(err -> {
                errors.add(err.getDefaultMessage());
            });
        }
        return errors;
    }    
}
