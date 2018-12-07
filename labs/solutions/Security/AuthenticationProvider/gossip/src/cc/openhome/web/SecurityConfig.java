package cc.openhome.web;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;

import cc.openhome.model.Account;
import cc.openhome.model.AccountDAO;
import cc.openhome.model.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter  {
    @Autowired
    private AccountDAO accountDAO;
    
    @Autowired
    private UserService userService;
         
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
            .antMatchers("/member", "/new_message", "/del_message", "/logout").hasRole("MEMBER")
            .anyRequest().permitAll()
            .and()
                .formLogin().loginPage("/").loginProcessingUrl("/login")
                .successHandler((request, response, auth) -> {
                    request.getSession().setAttribute("login", auth.getName());
                    response.sendRedirect("/gossip/member");
                })
                .failureHandler((request, response, ex) -> {
                    response.sendRedirect("/gossip?username=" + request.getParameter("username") + "&error");
                })
            .and()
                .logout().logoutUrl("/logout")
                .addLogoutHandler((request, response, auth) -> {
                    request.getSession().invalidate();
                    try {
                        response.sendRedirect("/gossip");
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                })
            .and()
                .csrf().disable();
    }
    
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(new GossipAuthenticationProvider());
	}
	
    private class GossipAuthenticationProvider implements AuthenticationProvider {
    	   
        @Override
        public Authentication authenticate(Authentication authentication) 
          throws AuthenticationException {
            String name = authentication.getName();
            String password = authentication.getCredentials().toString();
            
            Optional<Account> acct = accountDAO.accountByUsername(name);
            
            if(acct.isPresent() && userService.login(name, password)) {
                return new UsernamePasswordAuthenticationToken(
                    name, 
                    password, 
                    AuthorityUtils.createAuthorityList("ROLE_MEMBER")
                );
            }
            
            return null;
        }
     
        @Override
        public boolean supports(Class<?> authentication) {
            return authentication.equals(UsernamePasswordAuthenticationToken.class);
        }
    }
}

