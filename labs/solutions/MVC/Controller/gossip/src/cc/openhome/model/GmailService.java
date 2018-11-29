package cc.openhome.model;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.*; 

public class GmailService implements EmailService {
    private final Properties props = new Properties();
    private final String mailUser;
    private final String mailPassword;
    
    public GmailService(String mailUser, String mailPassword) {
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.port", 587);
        this.mailUser = mailUser;
        this.mailPassword = mailPassword;
    }

    @Override
    public void validationLink(Account acct) {
        try {
            String link = String.format(
                "http://localhost:8080/gossip/verify?email=%s&token=%s", 
                acct.getEmail(), acct.getPassword()
            );
            
            String anchor = String.format("<a href='%s'>驗證郵件</a>", link);
            
            String html = String.format("請按 %s 啟用帳戶或複製鏈結至網址列：<br><br> %s", anchor, link);
            
            javax.mail.Message message = createMessage(
                    mailUser, acct.getEmail(), "Gossip 註冊結果", html);
            Transport.send(message);
        } catch (MessagingException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void failedRegistration(String acctName, String acctEmail) {
        try {
            javax.mail.Message message = createMessage(mailUser, acctEmail, "Gossip 註冊結果", 
                    String.format("帳戶申請失敗，使用者名稱 %s 或郵件 %s 已存在！", 
                            acctName, acctEmail));
            Transport.send(message);
        } catch (MessagingException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private javax.mail.Message createMessage(
            String from, String to, String subject, String text) 
                    throws MessagingException, AddressException, IOException {
        Session session = Session.getDefaultInstance(props, new Authenticator(){
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(mailUser, mailPassword);
            }}
        );
        
        Multipart multiPart = multiPart(text);
        
        javax.mail.Message message = new MimeMessage(session);  
        message.setFrom(new InternetAddress(from));
        message.setRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(to));
        message.setSubject(subject);
        message.setSentDate(new Date());
        message.setContent(multiPart);
        
        return message;
    }

    private Multipart multiPart(String text)
            throws MessagingException, UnsupportedEncodingException, IOException {
       
        MimeBodyPart htmlPart = new MimeBodyPart(); 
        htmlPart.setContent(text, "text/html;charset=UTF-8");
        
        Multipart multiPart = new MimeMultipart();
        multiPart.addBodyPart(htmlPart);

        return multiPart;
    }

    @Override
    public void passwordResetLink(Account acct) {
        try {
            String link = String.format(
                "http://localhost:8080/gossip/reset_password?name=%s&email=%s&token=%s", 
                acct.getName(), acct.getEmail(), acct.getPassword()
            );
            
            String anchor = String.format("<a href='%s'>重設密碼</a>", link);
            
            String html = String.format("請按 %s 或複製鏈結至網址列：<br><br> %s", anchor, link);
            
            javax.mail.Message message = createMessage(
                    mailUser, acct.getEmail(), "Gossip 重設密碼", html);
            Transport.send(message);
        } catch (MessagingException | IOException e) {
            throw new RuntimeException(e);
        }
    }    
}
