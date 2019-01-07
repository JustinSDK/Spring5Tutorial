package cc.openhome.model;

import java.util.List;

public interface MessageService {
    List<Message> messages(String username);
    void addMessage(String username, String blabla);
    void deleteMessage(String username, String millis);
    List<Message> newestMessages(int n);
}
