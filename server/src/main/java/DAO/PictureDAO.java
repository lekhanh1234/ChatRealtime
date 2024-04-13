package DAO;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;

import Models.UserPicture;

public class PictureDAO {
    public List<UserPicture> getAllPictureById(int userId){
    	// tra ve cua ham nay la anh theo dinh dang base64
    	List<UserPicture> list = new ArrayList<>();
    	try {
    	Connection connection = OpenConnectDatabase.openConnectDatabase();
    	Statement statement = connection.createStatement();
    	String query = "select * from Picture where userId = "+userId;
    	ResultSet resultSet = statement.executeQuery(query);
    	while(resultSet.next()) {
    		int idPicture = resultSet.getInt(1);
    		String picture = resultSet.getString(3);
    		list.add(new UserPicture(userId, picture, idPicture));
    	}
    }catch(Exception e) {} 	
    return list.size() > 0 ? list : null;
    }
    
    public void insertPicture(int userId,String picture){
    	try {
    	Connection connection = OpenConnectDatabase.openConnectDatabase();
    	Statement statement = connection.createStatement();
    	String update = "insert into Picture values("+userId+",'"+picture+"')";
    	statement.executeUpdate(update);
    }catch(Exception e) {
    } 	
    }
    
    public void deletePicture(int pictureId){
    	try {
    	Connection connection = OpenConnectDatabase.openConnectDatabase();
    	Statement statement = connection.createStatement();
    	String query = "delete from Picture where pictureId ="+pictureId;
    	statement.executeUpdate(query);
    }catch(Exception e) {} 	
    }
    
    private static String imageToBase64(String imagePath) throws IOException {
		File file = new File(imagePath);
		FileInputStream imageInFile = new FileInputStream(file);
		byte[] imageData = new byte[(int) file.length()];
		imageInFile.read(imageData);
		return Base64.getEncoder().encodeToString(imageData);
	}
}
