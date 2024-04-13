package Models;

public class UserPicture {
    private int userId;
    private String picture;
    private int pictureId;

    public UserPicture(int userId, String picture, int pictureId) {
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

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public int getPictureId() {
        return pictureId;
    }

    public void setPictureId(int pictureId) {
        this.pictureId = pictureId;
    }
}
