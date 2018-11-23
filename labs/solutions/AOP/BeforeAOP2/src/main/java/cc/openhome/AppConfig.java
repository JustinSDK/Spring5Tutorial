package cc.openhome;

import java.lang.reflect.Proxy;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import cc.openhome.model.AccountDAO;
import cc.openhome.proxy.LoggingProxy;

@Configuration
@ComponentScan
public class AppConfig {     
	@Autowired
	@Qualifier("accountDAOJdbcImpl")
	private AccountDAO accountDAO;
	
    @Bean(destroyMethod="shutdown")
    public DataSource dataSource(){
        return new EmbeddedDatabaseBuilder()
        		.setType(EmbeddedDatabaseType.H2)
        		.addScript("classpath:schema.sql")
        		.addScript("classpath:testData.sql")
        		.build();
    }
    
    @Bean
    public AccountDAO accountDAO() {
    	return (AccountDAO) Proxy.newProxyInstance( 
    			accountDAO.getClass().getClassLoader(), 
    			accountDAO.getClass().getInterfaces(), 
                new LoggingProxy(accountDAO)
            ); 
    }
    
    @Bean
    public static PropertySourcesPlaceholderConfigurer
                       propertySourcesPlaceholderConfigurer() {
       return new PropertySourcesPlaceholderConfigurer();
    }    
}
