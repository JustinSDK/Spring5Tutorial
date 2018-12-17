package cc.openhome.model;

import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface AccountDAO extends CrudRepository<Account, Integer> {
	@Query("SELECT * FROM account WHERE name = :name")
    Optional<Account> accountByUsername(@Param("name") String name);
	
	@Query("SELECT * FROM account WHERE email = :email")
    Optional<Account> accountByEmail(@Param("email") String email);
	
	@Modifying
	@Query("UPDATE account SET enabled = 1 WHERE name = :name")
    void activateAccount(@Param("name") String name);
	
	@Modifying
	@Query("UPDATE t_account SET password = :password WHERE name = :name")	
    void updatePassword(@Param("name") String name, @Param("password") String password);
}
