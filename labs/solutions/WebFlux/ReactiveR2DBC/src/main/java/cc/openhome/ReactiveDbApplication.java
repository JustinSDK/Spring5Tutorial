package cc.openhome;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import io.r2dbc.h2.H2ConnectionConfiguration;
import io.r2dbc.h2.H2ConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import org.springframework.data.r2dbc.function.DatabaseClient;


import org.springframework.data.r2dbc.repository.support.R2dbcRepositoryFactory;
import org.springframework.data.relational.core.mapping.RelationalMappingContext;

@SpringBootApplication
public class ReactiveDbApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReactiveDbApplication.class, args);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public Scheduler scheduler() {
		return Schedulers.elastic();
	}
	
	@Bean
	public H2ConnectionFactory connectionFactoru() {
		return new H2ConnectionFactory(
			H2ConnectionConfiguration.builder()
			.inMemory("testdb")
			.username("sa")
		    .build()
		);
	}
	
    @Bean
    public DatabaseClient databaseClient(ConnectionFactory factory) {
        return DatabaseClient.create(factory);
    }
    
    @Bean
    public R2dbcRepositoryFactory factory(DatabaseClient client) {
    	RelationalMappingContext context = new RelationalMappingContext();
        context.afterPropertiesSet();
        return new R2dbcRepositoryFactory(client, context);
    }
    
    @Bean
    public AccountDAO accountRepository(R2dbcRepositoryFactory factory)  {
        return factory.getRepository(AccountDAO.class);
    }
    
    @Bean
    public MessageDAO messageRepository(R2dbcRepositoryFactory factory)  {
        return factory.getRepository(MessageDAO.class);
    }    
}

