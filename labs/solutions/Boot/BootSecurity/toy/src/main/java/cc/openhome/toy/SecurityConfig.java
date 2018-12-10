package cc.openhome.toy;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter { 
	@Autowired
	private DataSource dataSource;
	
    @Override
    protected void configure(HttpSecurity http) throws Exception {
  	  http.authorizeRequests()
            .antMatchers("/user/**").hasRole("MEMBER")
            .and()
            .formLogin();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder builder) throws Exception {
        builder.jdbcAuthentication()
               .passwordEncoder(new BCryptPasswordEncoder())
               .dataSource(dataSource)
               .usersByUsernameQuery("select name, password, enabled from t_account where name=?")
               .authoritiesByUsernameQuery("select name, role from t_account_role where name=?");

    }	
}
