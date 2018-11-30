package cc.openhome.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import cc.openhome.model.Account;
import cc.openhome.model.EmailService;
import cc.openhome.model.UserService;

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
	private UserService userService;
	
	@Autowired
	private EmailService emailService;
	
    private final Pattern emailRegex = Pattern.compile(
            "^[_a-z0-9-]+([.][_a-z0-9-]+)*@[a-z0-9-]+([.][a-z0-9-]+)*$");
    private final Pattern passwdRegex = Pattern.compile("^\\w{8,16}$");
    private final Pattern usernameRegex = Pattern.compile("^\\w{1,16}$");
        
    @GetMapping("register")
    public String registerForm() {
        return REGISTER_FORM_PATH;
    }
    
    @PostMapping("register")
    public String register(
            @RequestParam String email,
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String password2,
            Model model) {

        List<String> errors = new ArrayList<>(); 
        if (!validateEmail(email)) {
            errors.add("未填寫郵件或格式不正確");
        }
        if(!validateUsername(username)) {
            errors.add("未填寫使用者名稱或格式不正確");
        }
        if (!validatePassword(password, password2)) {
            errors.add("請確認密碼符合格式並再度確認密碼");
        }
        
        String path;
        if(errors.isEmpty()) {
            path = REGISTER_SUCCESS_PATH;
            
            Optional<Account> optionalAcct = userService.tryCreateUser(email, username, password);
            if(optionalAcct.isPresent()) {
                emailService.validationLink(optionalAcct.get());
            } else {
                emailService.failedRegistration(username, email);
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
        model.addAttribute("acct", userService.verify(email, token));
        return VERIFY_PATH;
    }
    
    @PostMapping("forgot")
    public String forgot(
            @RequestParam String name,
            @RequestParam String email,
            Model model) {
        
        Optional<Account> optionalAcct = userService.accountByNameEmail(name, email);
        
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

        Optional<Account> optionalAcct = userService.accountByNameEmail(name, email);
        
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
            @RequestParam String token,
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String password2,
            @SessionAttribute(name = "token") String storedToken,
            Model model) {
        
        if(storedToken == null || !storedToken.equals(token)) {
            return REDIRECT_INDEX_PATH;
        }
         
        if (!validatePassword(password, password2)) {
            Optional<Account> optionalAcct = userService.accountByNameEmail(name, email);
            model.addAttribute("errors", Arrays.asList("請確認密碼符合格式並再度確認密碼"));
            model.addAttribute("acct", optionalAcct.get());
            return RESET_PASSWORD_FORM_PATH;
        } else {
            userService.resetPassword(name, password);
            return RESET_PASSWORD_SUCCESS_PATH;
        }    
    }
    
    private boolean validateEmail(String email) {
        return email != null && emailRegex.matcher(email).find();
    }
    
    private boolean validateUsername(String username) {
        return username != null && usernameRegex.matcher(username).find();
    }

    
    private boolean validatePassword(String password, String password2) {
        return password != null && 
               passwdRegex.matcher(password).find() && 
               password.equals(password2);
    }    
}
