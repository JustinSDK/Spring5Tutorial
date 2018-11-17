package cc.openhome;

import javax.sql.DataSource;

import org.h2.jdbcx.JdbcDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@ComponentScan
@PropertySource("classpath:jdbc.properties")
public class AppConfig { 
	@Value("${cc.openhome.jdbcUrl}")
	private String jdbcUrl;
	
	@Value("${cc.openhome.user}")
	private String user;
	
	@Value("${cc.openhome.password}")
	private String password;
		
    @Bean
    public DataSource getDataSource() {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setURL(jdbcUrl);
        dataSource.setUser(user);
        dataSource.setPassword(password);
        return dataSource;
    }
    
    @Bean
    public static PropertySourcesPlaceholderConfigurer
                       propertySourcesPlaceholderConfigurer() {
       return new PropertySourcesPlaceholderConfigurer();
    }    
}
