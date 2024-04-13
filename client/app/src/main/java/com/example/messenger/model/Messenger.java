package com.example.messenger.model;

public class Messenger {
    private int userId;
    private String accountName;
    private String lastMessage;
    private String MessageType;
    private String lastReceiveMessageDate;
    private byte[] avata;
    private boolean seeMessage;
    private int userIdSendMessage;

    public Messenger(int userId, String accountName, String lastMessage, String messageType, String lastReceiveMessageDate, byte[] avata,int userIdSendMessage, boolean seeMessage) {
        this.userId = userId;
        this.accountName = accountName;
        this.lastMessage = lastMessage;
        MessageType = messageType;
        this.lastReceiveMessageDate = lastReceiveMessageDate;
        this.avata = avata;
        this.userIdSendMessage = userIdSendMessage;
        this.seeMessage = seeMessage;
    }

    public int getUserIdSendMessage() {
        return userIdSendMessage;
    }

    public void setUserIdSendMessage(int userIdSendMessage) {
        this.userIdSendMessage = userIdSendMessage;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getMessageType() {
        return MessageType;
    }

    public void setMessageType(String messageType) {
        MessageType = messageType;
    }

    public String getLastReceiveMessageDate() {
        return lastReceiveMessageDate;
    }

    public void setLastReceiveMessageDate(String lastReceiveMessageDate) {
        this.lastReceiveMessageDate = lastReceiveMessageDate;
    }

    public byte[] getAvata() {
        return avata;
    }

    public void setAvata(byte[] avata) {
        this.avata = avata;
    }

    public boolean isSeeMessage() {
        return seeMessage;
    }

    public void setSeeMessage(boolean seeMessage) {
        this.seeMessage = seeMessage;
    }
}