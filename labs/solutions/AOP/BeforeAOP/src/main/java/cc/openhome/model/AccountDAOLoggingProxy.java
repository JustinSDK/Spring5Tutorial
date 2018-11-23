package cc.openhome.model;

import java.util.Optional;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class AccountDAOLoggingProxy implements AccountDAO {
	private AccountDAO target;
	private Logger logger;
	
	@Autowired
	public AccountDAOLoggingProxy(@Qualifier("accountDAOJdbcImpl") AccountDAO target) {
		this.target = target;
		logger = Logger.getLogger(target.getClass().getName());
	}
	
	@Override
	public void createAccount(Account acct) {
		logger.info(String.format("%s.createAccount(%s)",
				target.getClass().getName(), acct));
		target.createAccount(acct);
	}

	@Override
	public Optional<Account> accountByUsername(String name) {
		logger.info(String.format("%s.accountByUsername(%s)", 
				target.getClass().getName(), name));
		return target.accountByUsername(name);
	}

	@Override
	public Optional<Account> accountByEmail(String email) {
		logger.info(String.format("%s.accountByEmail(%s)",
                 target.getClass().getName(), email));
		return target.accountByEmail(email);
	}

	@Override
	public void activateAccount(Account acct) {
		logger.info(String.format("%s.activateAccount(%s)",
				target.getClass().getName(), acct));
		target.activateAccount(acct);
	}

	@Override
	public void updatePasswordSalt(String name, String password, String salt) {
		logger.info(String.format("%s.updatePasswordSalt(%s, %s, %s)",
				target.getClass().getName(), name, password, salt));
		target.updatePasswordSalt(name, password, salt);
	}

}
