package cc.openhome.model;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
 
@Service
public class GmailService implements EmailService {
    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void validationLink(Account acct) {
        String link = String.format(
            "http://localhost:8080/verify?email=%s&token=%s", 
             uriEncode(acct.getEmail()), uriEncode(acct.getPassword())
        );
        
        String anchor = String.format("<a href='%s'>驗證郵件</a>", link);
        String html = String.format("請按 %s 啟用帳戶或複製鏈結至網址列：<br><br> %s", anchor, link);
        
        sendMessage(acct.getEmail(), "Gossip 註冊結果", html);
    }

    @Override
    public void failedRegistration(String acctName, String acctEmail) {
    	sendMessage(
			acctEmail, 
			"Gossip 註冊結果", 
			String.format("帳戶申請失敗，使用者名稱 %s 或郵件 %s 已存在！", acctName, acctEmail)
    	);
    }
    

    @Override
    public void passwordResetLink(Account acct) {
        String link = String.format(
            "http://localhost:8080/reset_password?name=%s&email=%s&token=%s", 
            uriEncode(acct.getName()), 
            uriEncode(acct.getEmail()), 
            uriEncode(acct.getPassword())
        );
        
        String anchor = String.format("<a href='%s'>重設密碼</a>", link);
        String html = String.format("請按 %s 或複製鏈結至網址列：<br><br> %s", anchor, link);
        
    	sendMessage(
			acct.getEmail(), 
			"Gossip 重設密碼", 
			html
        );            
    }  

    private void sendMessage(String to, String subject, String text)  {
    	MimeMessage message = mailSender.createMimeMessage();
        try {
        	MimeMessageHelper helper = new MimeMessageHelper(message, false);
        	message.setContent(text, "text/html; charset=UTF-8");
	        helper.setTo(to);
	        helper.setSubject(subject);
	        helper.setSentDate(new Date());
	        mailSender.send(message);
	    } catch (MessagingException e) {
	        throw new RuntimeException(e);
	    }
    }

    
    private String uriEncode(String text) {
    	try {
			return URLEncoder.encode(text, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		} 
    }
}
