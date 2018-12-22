package cc.openhome.model;

import reactor.core.publisher.Mono;

public interface EmailService {
    public Mono<Void> validationLink(Account acct);
    public Mono<Void> failedRegistration(String acctName, String acctEmail);
    public Mono<Void> passwordResetLink(Account account);
}
