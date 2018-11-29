package cc.openhome.web;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;

import cc.openhome.model.AccountDAO;
import cc.openhome.model.AccountDAOJdbcImpl;
import cc.openhome.model.GmailService;
import cc.openhome.model.MessageDAO;
import cc.openhome.model.MessageDAOJdbcImpl;
import cc.openhome.model.UserService;

@WebListener
public class GossipInitializer implements ServletContextListener {
    public void contextInitialized(ServletContextEvent sce) {
        DataSource dataSource = dataSource();

        ServletContext context = sce.getServletContext();
        
        AccountDAO acctDAO = new AccountDAOJdbcImpl(dataSource);
        MessageDAO messageDAO = new MessageDAOJdbcImpl(dataSource);
        context.setAttribute("userService", new UserService(acctDAO, messageDAO));
        context.setAttribute("emailService", 
                new GmailService(
                    context.getInitParameter("MAIL_USER"), 
                    context.getInitParameter("MAIL_PASSWORD")
               )
        );
    }

    private DataSource dataSource() {
        try {
            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:/comp/env");
           return (DataSource) envContext.lookup("jdbc/gossip");
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }
}
