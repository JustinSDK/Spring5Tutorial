package cc.openhome;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import javax.sql.DataSource;
//import org.springframework.context.annotation.Bean;
//import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
//import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

@SpringBootApplication
public class DataJdbcApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataJdbcApplication.class, args);
	}

//    // 如果 *.sql 存為其他名稱的話，可使用這個 Bean 
//    @Bean(destroyMethod="shutdown")
//    public DataSource dataSource(){
//        return new EmbeddedDatabaseBuilder()
//                .setType(EmbeddedDatabaseType.H2)
//                .addScript("classpath:schema_message.sql")
//                .build();
//    }
}

