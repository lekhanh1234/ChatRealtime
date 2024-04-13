package com.example.messenger.model;

public class ChatUserMessages {
    private int idMessage;
    private int chatUserId;
    private String messageType; // cho nay ta them mot type moi la LikeImage
    private String Content;

    public ChatUserMessages(int idMessage, int chatUserId, String messageType, String content) {
        this.idMessage = idMessage;
        this.chatUserId = chatUserId;
        this.messageType = messageType;
        Content = content;
    }

    public int getIdMessage() {
        return idMessage;
    }

    public void setIdMessage(int idMessage) {
        this.idMessage = idMessage;
    }

    public int getChatUserId() {
        return chatUserId;
    }

    public void setChatUserId(int chatUserId) {
        this.chatUserId = chatUserId;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }
}
