package cc.openhome.model;

import java.util.Optional;

public interface AccountDAO {
    void createAccount(Account acct);
    Optional<Account> accountByUsername(String name);
    Optional<Account> accountByEmail(String email);
    void activateAccount(Account acct);
    void updatePasswordSalt(String name, String password, String salt);
}
