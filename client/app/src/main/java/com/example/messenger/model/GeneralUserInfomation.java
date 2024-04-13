package com.example.messenger.model;

public class GeneralUserInfomation {
    private int idUser;
    private byte[] avatar;
    private String accountName;

    public GeneralUserInfomation(int idUser, byte[] avatar, String accountName) {
        this.idUser = idUser;
        this.avatar = avatar;
        this.accountName = accountName;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
}
