package cc.openhome;

public class Message {
	private String text;
	
	public Message() {
		
	}

	public Message(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return "Message [text=" + text + "]";
	}
}
