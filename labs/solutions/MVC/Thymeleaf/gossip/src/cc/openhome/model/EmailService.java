package cc.openhome.model;

public interface EmailService {
    public void validationLink(Account acct);
    public void failedRegistration(String acctName, String acctEmail);
    public void passwordResetLink(Account account);
}
