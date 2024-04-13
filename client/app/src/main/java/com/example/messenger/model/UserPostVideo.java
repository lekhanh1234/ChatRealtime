package com.example.messenger.model;

public class UserPostVideo extends GeneralUserInfomation{
    private byte[] postVideo;

    public byte[] getPostVideo() {
        return postVideo;
    }

    public UserPostVideo(int idUser, byte[] avatar, String accountName, byte[] postVideo) {
        super(idUser, avatar, accountName);
        this.postVideo = postVideo;
    }
}
