package com.example.messenger.MyAccount;

public class InfoMyAccount {
    public static int  accountId;
    public static String nameAccount;
    public static String introduce;

    public static byte[] avata;
    public static byte[] coverImage;

    public static String getIntroduce() {
        return introduce;
    }

    public static void setIntroduce(String introduce) {
        InfoMyAccount.introduce = introduce;
    }

    public static void setAccountId(int accountId) {
        InfoMyAccount.accountId = accountId;
    }

    public static String getNameAccount() {
        return nameAccount;
    }

    public static void setNameAccount(String nameAccount) {
        InfoMyAccount.nameAccount = nameAccount;
    }

    public static byte[] getAvata() {
        return avata;
    }

    public static void setAvata(byte[] avata) {
        InfoMyAccount.avata = avata;
    }

    public static byte[] getCoverImage() {
        return coverImage;
    }

    public static void setCoverImage(byte[] coverImage) {
        InfoMyAccount.coverImage = coverImage;
    }

    public static int getAccountId() {
        return accountId;
    }
}
