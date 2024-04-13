package com.example.messenger.model;

public class MyPrivacyInfo {
    private String myName;
    private int Sex;
    private String birthday;

    public MyPrivacyInfo(String myName, int boySex, String birthday) {
        this.myName = myName;
        this.Sex = boySex;
        this.birthday = birthday;
    }

    public String getMyName() {
        return myName;
    }

    public void setMyName(String myName) {
        this.myName = myName;
    }

    public int getSex() {
        return Sex;
    }

    public void setSex(int Sex) {
        this.Sex = Sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
}
