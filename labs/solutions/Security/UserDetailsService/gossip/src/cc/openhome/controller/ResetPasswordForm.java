package cc.openhome.controller;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Size;

public class ResetPasswordForm {
    private String token;
    private String name;
    private String email;
    
    @Size(min = 8, max = 16, message = "請確認密碼符合格式")
    private String password;

    private String password2;
    
    @AssertTrue(message="密碼與再次確認密碼不相符")
    private boolean isValid() {
      return password.equals(password2);
    }    
    
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getPassword2() {
        return password2;
    }
    public void setPassword2(String password2) {
        this.password2 = password2;
    }
    
    
}
