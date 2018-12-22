package cc.openhome.gossip;

import java.net.URI;

import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.DefaultServerRedirectStrategy;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.ServerRedirectStrategy;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.authentication.logout.RedirectServerLogoutSuccessHandler;
import org.springframework.web.server.ServerWebExchange;

import cc.openhome.model.AccountDAO;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

@SpringBootApplication(
    scanBasePackages={
        "cc.openhome.controller",
        "cc.openhome.model",
        "cc.openhome.aspect"
    }
)
@EnableJdbcRepositories(
	basePackages= {
		"cc.openhome.model"
	}
)
public class GossipApplication {

	public static void main(String[] args) {
		SpringApplication.run(GossipApplication.class, args);
	}
	
    @Bean
    public PasswordEncoder passwordEncoder() {
    	return new BCryptPasswordEncoder();
    }
    
    @Bean
    public SecurityWebFilterChain securitygWebFilterChain(ServerHttpSecurity http) {
    	ServerRedirectStrategy redirectStrategy = new DefaultServerRedirectStrategy();
    	
    	RedirectServerLogoutSuccessHandler logoutSuccessHandler = new RedirectServerLogoutSuccessHandler();
    	logoutSuccessHandler.setLogoutSuccessUrl(URI.create("/?logout"));
        
    	return http
                 .authorizeExchange()
                     .pathMatchers("/member", "/new_message", "/del_message", "/logout").hasRole("MEMBER")
                 .anyExchange().permitAll()
                 .and()
                 .formLogin()
                     .loginPage("/")
                         .authenticationSuccessHandler(new RedirectServerAuthenticationSuccessHandler("/member"))
                         .authenticationFailureHandler((webFilterExchange, ex) -> {
                        	 ServerWebExchange webExchange = webFilterExchange.getExchange();
                        	 return webExchange
	            	                  .getFormData()
	            	                  .map(formData -> URI.create(String.format("/?username=%s&error", formData.getFirst("username"))))
	            	                  .flatMap(uri -> redirectStrategy.sendRedirect(webExchange, uri));
                         })
                 .and()
                     .logout()
                         .logoutUrl("/logout")
                         .logoutSuccessHandler(logoutSuccessHandler)
                 .and().csrf().disable()
                 .build();
    }
    
    @Bean
    public ReactiveUserDetailsService userDetailsService(AccountDAO accountDAO, Scheduler scheduler) {
        return username -> {
        	return Mono.defer(() -> {
        		return Mono.justOrEmpty(accountDAO.accountByUsername(username))
        		    .map(acct -> {
        		    	return User.withUsername(username)
		        		           .password(acct.getPassword())
		        		           .roles("MEMBER")
		        		           .build();
        		    });
        		    
        	}).subscribeOn(scheduler);
        };
    }    
    
    @Bean
    public ReactiveAuthenticationManager authenticationManager(ReactiveUserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
    	UserDetailsRepositoryReactiveAuthenticationManager manager = new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);
    	manager.setPasswordEncoder(passwordEncoder);
    	return manager;
    }    
    
    @Bean 
    public Scheduler scheduler() {
    	return Schedulers.elastic();
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
}
