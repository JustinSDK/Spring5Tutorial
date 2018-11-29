package cc.openhome.model;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class UserService {
    private final AccountDAO acctDAO;
    private final MessageDAO messageDAO;

    public UserService(AccountDAO acctDAO, MessageDAO messageDAO) {
        this.acctDAO = acctDAO;
        this.messageDAO = messageDAO;
    }

    public Optional<Account> tryCreateUser(String email, String username, String password)  {
        if(emailExisted(email) || userExisted(username)) {
            return Optional.empty();
        }
        return Optional.of(createUser(username, email, password));
    }

    private Account createUser(String username, String email, String password) {
        int salt = (int) (Math.random() * 100);
        int encrypt = salt + password.hashCode();
        Account acct = new Account(username, email, 
                String.valueOf(encrypt), String.valueOf(salt));
        acctDAO.createAccount(acct);
        return acct;
    }
    
    public Optional<String> encryptedPassword(String username, String password) {
        Optional<Account> optionalAcct = acctDAO.accountByUsername(username);
        if(optionalAcct.isPresent()) {
            Account acct = optionalAcct.get();
            int salt = Integer.parseInt(acct.getSalt());
            return Optional.of(String.valueOf(password.hashCode() + salt));
        }
        return Optional.empty();
    }

    public boolean login(String username, String password) {
        if(username == null || username.trim().length() == 0) {
            return false;
        }
        
        Optional<Account> optionalAcct = acctDAO.accountByUsername(username);
        if(optionalAcct.isPresent()) {
            Account acct = optionalAcct.get();
            int encrypt = Integer.parseInt(acct.getPassword());
            int salt = Integer.parseInt(acct.getSalt());
            return password.hashCode() + salt == encrypt;
        }
        return false;
    }
    
    public List<Message> messages(String username) {
        List<Message> messages = messageDAO.messagesBy(username);
        messages.sort(Comparator.comparing(Message::getMillis).reversed());
        return messages;
    }   
    
    public void addMessage(String username, String blabla) {
        messageDAO.createMessage(
                new Message(
                        username, Instant.now().toEpochMilli(), blabla));
    }    
    
    public void deleteMessage(String username, String millis) {
        messageDAO.deleteMessageBy(username, millis);
    }
    
    public boolean userExisted(String username) {
        return acctDAO.accountByUsername(username).isPresent();
    }
    
    public List<Message> newestMessages(int n) {
        return messageDAO.newestMessages(n);
    }
    
    public boolean emailExisted(String email) {
        return acctDAO.accountByEmail(email).isPresent();
    }
    
    public Optional<Account> verify(String email, String token) {
        Optional<Account> optionalAcct= acctDAO.accountByEmail(email);
        if(optionalAcct.isPresent()) {
            Account acct = optionalAcct.get();
            if(acct.getPassword().equals(token)) {
                acctDAO.activateAccount(acct);
                return Optional.of(acct);
            }
        }
        return Optional.empty();
    }

    public Optional<Account> accountByNameEmail(String name, String email) {
        Optional<Account> optionalAcct = acctDAO.accountByUsername(name);
        if(optionalAcct.isPresent() && optionalAcct.get().getEmail().equals(email)) {
            return optionalAcct;
        }
        return Optional.empty();
    }

    public void resetPassword(String name, String password) {
        int salt = (int) (Math.random() * 100);
        int encrypt = salt + password.hashCode();
        acctDAO.updatePasswordSalt(name, String.valueOf(encrypt), String.valueOf(salt));
    }
}
