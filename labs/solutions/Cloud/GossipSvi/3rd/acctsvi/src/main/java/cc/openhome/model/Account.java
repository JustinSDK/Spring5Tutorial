package cc.openhome.model;

import org.springframework.data.annotation.Id;

public class Account {
	@Id
	private Integer id; 
    private String name;
    private String email;
    private String password;
    private Integer enabled;
    private String role;
    
    public Account() {
    }
    
    public Account(String name, String email, String password, Integer enabled, String role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.enabled = enabled;
        this.role = role;
    }    
    
    public Account(String name, String email, String password) {
    	this(name, email, password, 0, "ROLE_MEMBER");
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

	public void setName(String name) {
		this.name = name;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
