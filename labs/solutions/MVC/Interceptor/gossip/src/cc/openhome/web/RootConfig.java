package cc.openhome.web;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@PropertySource("classpath:mail.properties")
@ComponentScan("cc.openhome.model")
public class RootConfig {
    @Bean
    public DataSource dataSource() {
        try {
            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:/comp/env");
           return (DataSource) envContext.lookup("jdbc/gossip");
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }    

    @Bean
    public static PropertySourcesPlaceholderConfigurer 
                       propertySourcesPlaceholderConfigurer() {
       return new PropertySourcesPlaceholderConfigurer();
    }
}