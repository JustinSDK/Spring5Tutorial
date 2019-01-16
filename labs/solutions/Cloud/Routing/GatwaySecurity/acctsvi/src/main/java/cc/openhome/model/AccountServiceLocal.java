package cc.openhome.model;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceLocal implements AccountService {
	@Autowired
    private AccountDAO accountDAO;
	
	@Autowired
	private PasswordEncoder passwordEncoder;


    public Optional<Account> tryCreateUser(String email, String username, String password)  {
        if(emailExisted(email) || userExisted(username)) {
            return Optional.empty();
        }
        return Optional.of(createUser(username, email, password));
    }

    private Account createUser(String username, String email, String password) {
        Account acct = new Account(username, email, passwordEncoder.encode(password), 0, "ROLE_MEMBER");
        accountDAO.save(acct);
        return acct;
    }
    
    public boolean userExisted(String username) {
        return accountDAO.accountByUsername(username).isPresent();
    }
    
    public boolean emailExisted(String email) {
        return accountDAO.accountByEmail(email).isPresent();
    }
    
    public Optional<Account> verify(String email, String token) {
        Optional<Account> optionalAcct= accountDAO.accountByEmail(email);
        if(optionalAcct.isPresent()) {
            Account acct = optionalAcct.get();
            if(acct.getPassword().equals(token)) {
                accountDAO.activateAccount(acct.getName());
                return Optional.of(acct);
            }
        }
        return Optional.empty();
    }

    public Optional<Account> accountByNameEmail(String name, String email) {
        Optional<Account> optionalAcct = accountDAO.accountByUsername(name);
        if(optionalAcct.isPresent() && optionalAcct.get().getEmail().equals(email)) {
            return optionalAcct;
        }
        return Optional.empty();
    }

    public void resetPassword(String name, String password) {
        accountDAO.updatePassword(name, passwordEncoder.encode(password));
    }

	@Override
	public Optional<Account> accountByName(String name) {
        return accountDAO.accountByUsername(name);
	}
}
