package DAO;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import InteractiveUser.BlockUser;
import Models.GeneralUserInfomation;

public class FriendRequestDAO {
	public List<GeneralUserInfomation> getRequestFriendToUser(int userId) throws IOException{
		List<GeneralUserInfomation> list = new ArrayList<>();
		String statementQuery = "select * from friendRequest where userIdReceive = "+userId;
		Connection openDB = new OpenConnectDatabase().openConnectDatabase();
		try {
			Statement statement = openDB.createStatement();
			ResultSet resultSet = statement.executeQuery(statementQuery);
			while(resultSet.next()){
				int userIdSend = resultSet.getInt(2);
				// có id. tiếp theo là lấy tên và ảnh đại diện
				UsersDAO usersDAO = new UsersDAO();
				String accountName = usersDAO.getAccountNameById(userId);
				String pathAvatar = usersDAO.getAvataPathById(userId);
				String avatar = imageToBase64(pathAvatar);
				list.add(new GeneralUserInfomation(userIdSend, avatar, accountName));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			list.clear();
		}
		return list.size() > 0 ? list : null;
	}
	
	public boolean acceptFriendRequest(int userId,int user2Id){
		Connection openDB = new OpenConnectDatabase().openConnectDatabase();
		try {
			String statementQuery = "delete from friendRequest where userIdReceive = "+userId+" and userIdSent = "+user2Id;
			Statement statement = openDB.createStatement();
			int rowsAffected = statement.executeUpdate(statementQuery);
			if(rowsAffected == 0) return false;
			else {
				// tiến hành chèn vào cơ sở dữ liệu
				FriendDAO dao =new FriendDAO();
				dao.addFriend(userId, user2Id);
				dao.addFriend(user2Id, userId);
				return true;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public int getAmountFollowUser(int userId){
		try {
			Connection connection = new OpenConnectDatabase().openConnectDatabase();
			Statement statement = connection.createStatement();
			String queryExecute = "select count(*) from friendRequest where userIdReceive = "+userId; 
			ResultSet resultSet = statement.executeQuery(queryExecute);
			if(resultSet.next()) {
				return resultSet.getInt(1);
			}
			else return 0;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
	
	public List<GeneralUserInfomation> getRequestFriendUserSend(int userId) throws IOException{
		List<GeneralUserInfomation> list = new ArrayList<>();
		String statementQuery = "select * from friendRequest where userIdSent = "+userId;
		Connection openDB = new OpenConnectDatabase().openConnectDatabase();
		try {
			Statement statement = openDB.createStatement();
			ResultSet resultSet = statement.executeQuery(statementQuery);
			while(resultSet.next()){
				int userIdReceive = resultSet.getInt(3);
				// có id. tiếp theo là lấy tên và ảnh đại diện //
				UsersDAO usersDAO = new UsersDAO();
				String accountName = usersDAO.getAccountNameById(userIdReceive);
				String pathAvatar = usersDAO.getAvataPathById(userIdReceive);
				String avatar = imageToBase64(pathAvatar);
				list.add(new GeneralUserInfomation(userIdReceive, avatar, accountName));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list.size() > 0 ? list : null;
	}
	
	public boolean cancelFriendRequest(int userId,int user2Id){
		Connection openDB = new OpenConnectDatabase().openConnectDatabase();
		try {
			String statementQuery = "delete from friendRequest where userIdSent = "+userId+" and userIdReceive = "+user2Id; // nếu user bên kia đã block hay chấp nhận lời mời thì hủy hành động
			Statement statement = openDB.createStatement();
			int rowsAffected = statement.executeUpdate(statementQuery); 
			if(rowsAffected == 0) return false;
			else return true;
			
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean checkFriendRequest(int userIdSend,int userIdReceive){
		Connection openDB = new OpenConnectDatabase().openConnectDatabase();
		try {
			String statementQuery = "select * from friendRequest where userIdSent = "+userIdSend+" and userIdReceive = "+userIdReceive;
			Statement statement = openDB.createStatement();
			ResultSet result = statement.executeQuery(statementQuery); 
			return result.next();		
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean sendFriendRequest(int userIdSend,int userIdReceive){
		Connection openDB = new OpenConnectDatabase().openConnectDatabase();
		if(checkFriendRequest(userIdSend, userIdReceive) == true) return false;
		if(checkFriendRequest(userIdReceive, userIdSend) == true) return false;
		if(new BlockUserDao().checkIsBlockUser(userIdSend, userIdReceive) == true) return false;
		try {
			Statement statement = openDB.createStatement();
			String insert = "insert into friendRequest values("+userIdSend+","+userIdReceive+")";
			statement.executeUpdate(insert); 
			return true;		
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	private String imageToBase64(String imagePath) throws IOException {
		File file = new File(imagePath);
		FileInputStream imageInFile = new FileInputStream(file);
		byte[] imageData = new byte[(int) file.length()];
		imageInFile.read(imageData);
		return Base64.getEncoder().encodeToString(imageData);
	}
	
	

}
