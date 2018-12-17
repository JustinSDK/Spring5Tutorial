package cc.openhome.gossip;

import java.io.IOException;
import java.io.UncheckedIOException;

import javax.sql.DataSource;

import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

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
	
	@Autowired
	private DataSource dataSource;
	
    @Autowired
    private PasswordEncoder passwordEncoder;
    
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
        	        auth.jdbcAuthentication()
        	            .passwordEncoder(passwordEncoder)
        	            .dataSource(dataSource)
        	            .usersByUsernameQuery("select name, password, enabled from account where name=?")
        	            .authoritiesByUsernameQuery("select name, role from account where name=?");
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
}
