package com.example.messenger.model;

public class MyPost {
    private byte[] post;
    private String path;

    public byte[] getPost() {
        return post;
    }

    public void setPost(byte[] post) {
        this.post = post;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public MyPost(byte[] post, String path) {
        this.post = post;
        this.path = path;
    }
}
