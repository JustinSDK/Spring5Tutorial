package test.cc.openhome;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cc.openhome.AppConfig;
import cc.openhome.model.AccountDAO;
import cc.openhome.model.UserService;
import cc.openhome.proxy.Nullable;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class)
public class MainTest {
	@Autowired
	private AccountDAO accountDAO;

    @Test(expected = IllegalArgumentException.class)
    public void testNotNullable() {
    	accountDAO.accountByEmail(null);
    }
    
    @Test public void testNullable() {
    	((Nullable) accountDAO).enable();
    	assertTrue(accountDAO.accountByEmail(null).isEmpty());
    }
}
