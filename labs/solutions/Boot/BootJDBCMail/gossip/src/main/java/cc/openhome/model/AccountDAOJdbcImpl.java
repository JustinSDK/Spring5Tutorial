package cc.openhome.model;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AccountDAOJdbcImpl implements AccountDAO {   
	@Autowired
	private JdbcTemplate jdbcTemplate;

    @Override
    public void createAccount(Account acct) {
    	jdbcTemplate.update("INSERT INTO t_account(name, email, password, enabled) VALUES(?, ?, ?, 0)", 
                acct.getName(), acct.getEmail(), acct.getPassword());
    	jdbcTemplate.update("INSERT INTO t_account_role(name, role) VALUES(?, 'ROLE_MEMBER')", 
                acct.getName());
    }

    @Override
    public Optional<Account> accountByUsername(String name) {
        return jdbcTemplate.queryForList("SELECT * FROM t_account WHERE name = ?", name)
			                .stream()
			                .findFirst()
			                .map(row -> {
			                   return new Account(
			                           row.get("NAME").toString(),
			                           row.get("EMAIL").toString(),
			                           row.get("PASSWORD").toString()
			                       );
			                });
    }

    @Override
    public Optional<Account> accountByEmail(String email) {
        return jdbcTemplate.queryForList("SELECT * FROM t_account WHERE email = ?", email)
                .stream()
                .findFirst()
                .map(row -> {
                   return new Account(
                           row.get("NAME").toString(),
                           row.get("EMAIL").toString(),
                           row.get("PASSWORD").toString()
                       );
                });
    }
    
    public void activateAccount(Account acct) {
    	jdbcTemplate.update("UPDATE t_account SET enabled = ? WHERE name = ?", 1, acct.getName());
    }

    @Override
    public void updatePassword(String name, String password) {
    	jdbcTemplate.update("UPDATE t_account SET password = ? WHERE name = ?", password, name);
    }
}
