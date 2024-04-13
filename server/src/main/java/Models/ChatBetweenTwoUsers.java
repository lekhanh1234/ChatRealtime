package Models;

public class ChatBetweenTwoUsers {
	private int messageId;
	private int senderId;
	private String messageType;
	private String content;

	public ChatBetweenTwoUsers(int messageId, int senderId, String messageType, String content) {
		super();
		this.messageId = messageId;
		this.senderId = senderId;
		this.messageType = messageType;
		this.content = content;
	}

	public int getMessageId() {
		return messageId;
	}

	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}

	public int getSenderId() {
		return senderId;
	}

	public void setSenderId(int senderId) {
		this.senderId = senderId;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		content = content;
	}

}
