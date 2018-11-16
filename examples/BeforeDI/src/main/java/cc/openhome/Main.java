package cc.openhome;

import java.io.IOException;
import java.util.Properties;

import org.h2.jdbcx.JdbcDataSource;

import cc.openhome.model.AccountDAO;
import cc.openhome.model.AccountDAOJdbcImpl;
import cc.openhome.model.MessageDAO;
import cc.openhome.model.MessageDAOJdbcImpl;
import cc.openhome.model.UserService;

public class Main {

	public static void main(String[] args) throws IOException {
		Properties prop = new Properties();
		prop.load(Main.class.getClassLoader().getResourceAsStream("jdbc.properties"));		
		
		JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setURL(prop.getProperty("cc.openhome.jdbcUrl"));
        dataSource.setUser(prop.getProperty("cc.openhome.user"));
        dataSource.setPassword(prop.getProperty("cc.openhome.password"));
        
        AccountDAO acctDAO = new AccountDAOJdbcImpl(dataSource);
        MessageDAO messageDAO = new MessageDAOJdbcImpl(dataSource);
        
        UserService userService = new UserService(acctDAO, messageDAO);
        
        userService.messages("caterpillar")
                   .forEach(message -> {
                       System.out.printf("%s\t%s%n",
                           message.getLocalDateTime(),
                           message.getBlabla());
                   });

	}

}
