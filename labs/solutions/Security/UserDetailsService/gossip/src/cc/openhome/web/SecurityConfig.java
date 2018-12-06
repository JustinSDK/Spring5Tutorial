package cc.openhome.web;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;

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
		auth.userDetailsService(username -> {
			return new User(
				username, 
				username + ":" + accountDAO.accountByUsername(username).get().getPassword(), 
				Arrays.asList(new SimpleGrantedAuthority("ROLE_MEMBER"))
	        );
		})
    	.passwordEncoder(new UserServiceBasedPasswordEncoder());
	}
	
	private class UserServiceBasedPasswordEncoder implements PasswordEncoder {
		@Override
		public String encode(CharSequence rawPassword) {
			// 不用編碼，因為後續實際上會使用 UserService 的 encryptedPassword 方法
			return rawPassword.toString();
		}

		@Override
		public boolean matches(CharSequence rawPassword, String passwordFromUserDetails) {
			String[] namePassword = passwordFromUserDetails.split(":");
			String name = namePassword[0];
			String password = namePassword[1];
			return userService.encryptedPassword(name, rawPassword.toString()).get().equals(password);
		}
    }
}

