package cc.openhome;

import javax.sql.DataSource;

import org.h2.jdbcx.JdbcDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

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
		    
    @Bean(destroyMethod="shutdown")
    public DataSource dataSource(){
        return new EmbeddedDatabaseBuilder()
        		.setType(EmbeddedDatabaseType.H2)
        		.addScript("classpath:schema.sql")
        		.addScript("classpath:testData.sql")
        		.build();
    }
    
    @Bean
    public static PropertySourcesPlaceholderConfigurer
                       propertySourcesPlaceholderConfigurer() {
       return new PropertySourcesPlaceholderConfigurer();
    }    
}
