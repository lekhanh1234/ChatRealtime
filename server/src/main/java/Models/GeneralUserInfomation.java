package Models;

public class GeneralUserInfomation {
	private int idUser;
    private String avatar;
    private String accountName;

    public GeneralUserInfomation(int idUser, String avatar, String accountName) {
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
}
