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
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter { 
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication()
    		.passwordEncoder(new BCryptPasswordEncoder())
		    .dataSource(dataSource())
		    .usersByUsernameQuery("select name, password, enabled from t_account where name=?")
		    .authoritiesByUsernameQuery("select name, role from t_account_role where name=?");
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
                .addScript("classpath:db.sql")
                .build();
    }
    
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl repo = new JdbcTokenRepositoryImpl();
        repo.setDataSource(dataSource());
        return repo;
    }
}
