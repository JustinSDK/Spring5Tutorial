package cc.openhome;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;
import javax.xml.transform.stream.StreamSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import cc.openhome.model.AccountDAO;
import cc.openhome.proxy.Nullable;
import cc.openhome.proxy.NullableProxy;

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
    	List<Class<?>> interfaces = new ArrayList<>(
    	    Arrays.asList(accountDAO.getClass().getInterfaces())
    	);
    	interfaces.add(Nullable.class);
    	return (AccountDAO) Proxy.newProxyInstance( 
    			accountDAO.getClass().getClassLoader(), 
    			interfaces.toArray(new Class[interfaces.size()]), 
                new NullableProxy(accountDAO)
            ); 
    } 
}
