package cc.openhome.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class AccountDAOJdbcImpl implements AccountDAO {
    private DataSource dataSource;
    
    @Autowired
    public AccountDAOJdbcImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void createAccount(Account acct) {
        try(Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
               "INSERT INTO t_account(name, email, password, enabled) VALUES(?, ?, ?, 0)");
            PreparedStatement stmt2 = conn.prepareStatement(
               "INSERT INTO t_account_role(name, role) VALUES(?, 'ROLE_MEMBER')")) {
            
            stmt.setString(1, acct.getName());
            stmt.setString(2, acct.getEmail());
            stmt.setString(3, acct.getPassword());
            stmt.executeUpdate();
            
            stmt2.setString(1, acct.getName());
            stmt2.executeUpdate();
        } catch (SQLException e) {
           throw new RuntimeException(e);
        } 
    }

    @Override
    public Optional<Account> accountByUsername(String name) {
        try(Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(
                        "SELECT name, email, password FROM t_account WHERE name = ?")) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                return Optional.of(new Account(
                    rs.getString(1),
                    rs.getString(2),
                    rs.getString(3)
                ));
            } else {
                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Account> accountByEmail(String email) {
        try(Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(
                        "SELECT name, email, password FROM t_account WHERE email = ?")) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                return Optional.of(new Account(
                    rs.getString(1),
                    rs.getString(2),
                    rs.getString(3)
                ));
            } else {
                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    public void activateAccount(Account acct) {
        try(Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
               "UPDATE t_account SET enabled = ? WHERE name = ?")) {
            stmt.setInt(1, 1);
            stmt.setString(2, acct.getName());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } 
    }

    @Override
    public void updatePassword(String name, String password) {
        try(Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
               "UPDATE t_account SET password = ? WHERE name = ?")) {
            stmt.setString(1, password);
            stmt.setString(2, name);
            stmt.executeUpdate();
        } catch (SQLException e) {
           throw new RuntimeException(e);
        } 
    }
}
