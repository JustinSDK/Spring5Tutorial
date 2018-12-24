package cc.openhome;

import org.springframework.data.annotation.Id;

public class Message {
	@Id
	private Integer id;
    private String username;
    private Long millis;
    private String blabla;
    
    public Message(String username, Long millis, String blabla) {
        this.username = username;
        this.millis = millis;
        this.blabla = blabla;
    }
    
    public Integer getId() {
    	return id;
    }
    
    public String getUsername() {
        return username;
    }

    public Long getMillis() {
        return millis;
    }

    public String getBlabla() {
        return blabla;
    }
}
