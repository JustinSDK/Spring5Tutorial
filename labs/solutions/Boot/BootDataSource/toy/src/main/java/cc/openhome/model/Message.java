package cc.openhome.model;

import java.time.*;

public class Message {
    private String username;
    private Long millis;
    private String blabla;
    
    public Message(String username, Long millis, String blabla) {
        this.username = username;
        this.millis = millis;
        this.blabla = blabla;
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
    
    public LocalDateTime getLocalDateTime() {
        return Instant.ofEpochMilli(millis)
                      .atZone(ZoneId.of("Asia/Taipei"))
                      .toLocalDateTime();
    }
}
