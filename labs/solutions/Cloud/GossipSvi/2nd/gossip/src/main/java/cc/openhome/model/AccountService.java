package cc.openhome.model;

import java.util.Optional;

public interface AccountService {
	Optional<Account> tryCreateUser(String email, String username, String password);
    boolean userExisted(String username);
    Optional<Account> verify(String email, String token);
    Optional<Account> accountByNameEmail(String name, String email);
    void resetPassword(String name, String password);
}
