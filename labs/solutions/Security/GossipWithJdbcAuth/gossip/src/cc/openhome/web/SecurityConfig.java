package cc.openhome.web;

import java.io.IOException;
import java.io.UncheckedIOException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter  {
	@Autowired
	private DataSource dataSource;
	
    @Autowired
    private PasswordEncoder passwordEncoder;
         
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
            .antMatchers("/member", "/new_message", "/del_message", "/logout").hasRole("MEMBER")
            .anyRequest().permitAll()
            .and()
                .formLogin().loginPage("/").loginProcessingUrl("/login")
                .successHandler((request, response, auth) -> {
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
        auth.jdbcAuthentication()
            .passwordEncoder(passwordEncoder)
            .dataSource(dataSource)
            .usersByUsernameQuery("select name, password, enabled from t_account where name=?")
            .authoritiesByUsernameQuery("select name, role from t_account_role where name=?");
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
    	return new BCryptPasswordEncoder();
    }
}

