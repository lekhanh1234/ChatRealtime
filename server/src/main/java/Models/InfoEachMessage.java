package Models;

public class InfoEachMessage {
	private int senderId;
	private int recipientId;
	private int state;
	private String imageContent;
	private String textContent;
	private int contentType;
	private String sentTime;
	private int seeMessage;
	public InfoEachMessage(int senderId, int recipientId, int state, String imageContent, String textContent,
			int contentType, String sentTime,int seeMessage) {
		super();
		this.senderId = senderId;
		this.recipientId = recipientId;
		this.state = state;
		this.imageContent = imageContent;
		this.textContent = textContent;
		this.contentType = contentType;
		this.sentTime = sentTime;
		this.seeMessage = seeMessage;
	}
	
	public int getSeeMessage() {
		return seeMessage;
	}

	public void setSeeMessage(int seeMessage) {
		this.seeMessage = seeMessage;
	}

	public int getSenderId() {
		return senderId;
	}
	public void setSenderId(int senderId) {
		this.senderId = senderId;
	}
	public int getRecipientId() {
		return recipientId;
	}
	public void setRecipientId(int recipientId) {
		this.recipientId = recipientId;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getImageContent() {
		return imageContent;
	}
	public void setImageContent(String imageContent) {
		this.imageContent = imageContent;
	}
	public String getTextContent() {
		return textContent;
	}
	public void setTextContent(String textContent) {
		this.textContent = textContent;
	}
	public int getContentType() {
		return contentType;
	}
	public void setContentType(int contentType) {
		this.contentType = contentType;
	}
	public String getSentTime() {
		return sentTime;
	}
	public void setSentTime(String sentTime) {
		this.sentTime = sentTime;
	}
	
	
}
