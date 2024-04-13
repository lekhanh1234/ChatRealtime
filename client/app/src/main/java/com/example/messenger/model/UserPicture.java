package com.example.messenger.model;

public class UserPicture {
    private int userId;
    private byte[] picture;
    private int pictureId;

    public UserPicture(int userId, byte[] picture, int pictureId) {
        this.userId = userId;
        this.picture = picture;
        this.pictureId = pictureId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public int getPictureId() {
        return pictureId;
    }

    public void setPictureId(int pictureId) {
        this.pictureId = pictureId;
    }
}
