package cc.openhome.web;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter { 
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		PasswordEncoder pwdEncoder = new BCryptPasswordEncoder();
		
		auth.inMemoryAuthentication() 
		    .passwordEncoder(pwdEncoder)
				.withUser("admin") 
				    .password(pwdEncoder.encode("admin12345678"))
				    .roles("ADMIN", "MEMBER")
			.and()
				.withUser("caterpillar")
				    .password(pwdEncoder.encode("12345678"))
				    .roles("MEMBER");
	}
	
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers("/admin").hasRole("ADMIN")  
            .antMatchers("/member").hasAnyRole("ADMIN", "MEMBER")
            .antMatchers("/user").authenticated()
            .anyRequest().permitAll()
            .and()
            .rememberMe().tokenRepository(persistentTokenRepository())
            .and()
            .formLogin() 
                .loginPage("/login_page")
                .loginProcessingUrl("/perform_login")
                .failureUrl("/login_page?error")
            .and()
            .logout()
                .logoutUrl("/perform_logout")
                .logoutSuccessUrl("/login_page?logout");
    } 
    
    @Bean(destroyMethod="shutdown")
    public DataSource dataSource(){
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:persistent_logins.sql")
                .build();
    }
    
    
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl repo = new JdbcTokenRepositoryImpl();
        repo.setDataSource(dataSource());
        return repo;
    }
}
