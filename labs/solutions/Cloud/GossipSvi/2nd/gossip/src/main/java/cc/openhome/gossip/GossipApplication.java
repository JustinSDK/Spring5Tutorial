package cc.openhome.gossip;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.Optional;

import javax.sql.DataSource;

import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import cc.openhome.model.Account;
import cc.openhome.model.AccountService;

@SpringBootApplication(
    scanBasePackages={
        "cc.openhome.controller",
        "cc.openhome.model",
        "cc.openhome.aspect"
    }
)
@PropertySource("classpath:path.properties")
public class GossipApplication {

	public static void main(String[] args) {
		SpringApplication.run(GossipApplication.class, args);
	}
	
	@Autowired
	private DataSource dataSource;
	
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private AccountService accountService;
    
    @Bean
    public PasswordEncoder passwordEncoder() {
    	return new BCryptPasswordEncoder();
    }
	
    @Bean
    public WebSecurityConfigurerAdapter webSecurityConfig() {
          return new WebSecurityConfigurerAdapter() {
        	    @Override
        	    protected void configure(HttpSecurity http) throws Exception {
        	        http
        	            .authorizeRequests()
        	            .antMatchers("/member", "/new_message", "/del_message", "/logout").hasRole("MEMBER")
        	            .anyRequest().permitAll()
        	            .and()
        	                .formLogin().loginPage("/").loginProcessingUrl("/login")
        	                .successHandler((request, response, auth) -> {
        	                    response.sendRedirect("/member");
        	                })
        	                .failureHandler((request, response, ex) -> {
        	                    response.sendRedirect("/?username=" + request.getParameter("username") + "&error");
        	                })
        	            .and()
        	                .logout().logoutUrl("/logout")
        	                .addLogoutHandler((request, response, auth) -> {
        	                    request.getSession().invalidate();
        	                    try {
        	                        response.sendRedirect("/");
        	                    } catch (IOException e) {
        	                        throw new UncheckedIOException(e);
        	                    }
        	                })
        	            .and()
        	                .csrf().disable();
        	    }
        	    
        	    @Override
        	    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        	        auth.userDetailsService(username -> {
        	        	Optional<Account> maybeAcct = accountService.accountByName(username);
        	        	if(maybeAcct.isPresent()) {
        	        		Account acct = maybeAcct.get();
        	        		return new User(
            	                username, 
            	                acct.getPassword(), 
            	                Arrays.asList(new SimpleGrantedAuthority("ROLE_MEMBER"))
                	        );
        	        	}
        	        	return null;
        	        });
        	    }
          };
    }	
    

	@Bean
	public PolicyFactory htmlPolicy() {
		return new HtmlPolicyBuilder()
			        .allowElements("a", "b", "i", "del", "pre", "code")
			        .allowUrlProtocols("http", "https")
			        .allowAttributes("href").onElements("a")
			        .requireRelNofollowOnLinks()
			        .toFactory();
	}
	
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
