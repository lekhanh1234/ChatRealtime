package DAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PostsDAO {

	public String getPost24HContentByUserId(int userId) {
		try {
			Connection connection = new OpenConnectDatabase().openConnectDatabase();
			Statement statement = connection.createStatement();
			String queryExecute = "select post from Posts where userId = " + userId + " and type = 2"; // 1 là profile. 2 là các post -> friend in 24h.
			ResultSet resultSet = statement.executeQuery(queryExecute);
			if (resultSet.next()) {
				return resultSet.getString(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block ..
			e.printStackTrace();
		}  //
		return null;
	}
	
	public void deletePost24H(int userId) {
		try {
			Connection connection = new OpenConnectDatabase().openConnectDatabase();
			Statement statement = connection.createStatement();
			String execute = "delete from Posts where userId = " + userId + " and type = 2"; // 1 là profile. 2 là các post -> friend in 24h.
			statement.executeUpdate(execute);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void deleteMyPost(int postId) {
		try {
			Connection connection = new OpenConnectDatabase().openConnectDatabase();
			Statement statement = connection.createStatement();
			String execute = "delete from Posts where postId = " + postId; // 1 là profile. 2 là các post -> friend in 24h.
			statement.executeQuery(execute);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getMyPostContentByPostId(int postId){
		try {
			Connection connection = new OpenConnectDatabase().openConnectDatabase();
			Statement statement = connection.createStatement();
			String queryExecute = "select post from Posts where postId = "+postId; 
			ResultSet resultSet = statement.executeQuery(queryExecute);
			if (resultSet.next()) {
				return resultSet.getString(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	
	public List<Integer> getFriendIdPost24H(int userId) {
		try {
			Connection connection = new OpenConnectDatabase().openConnectDatabase();
			Statement statement = connection.createStatement();
			String queryExecute = "select Posts.userId from Posts inner join Friends on Friends.userFriendId = Posts.userId where Friends.userId = "+userId;
			ResultSet resultSet = statement.executeQuery(queryExecute);
			List<Integer> result = new ArrayList<>();
			while(resultSet.next()) {
				result.add(resultSet.getInt(1));
			}
			return result.size() > 0 ? result : null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public List<Integer> getUserPostId(int userId){
		try {
			Connection connection = new OpenConnectDatabase().openConnectDatabase();
			Statement statement = connection.createStatement();
			String queryExecute = "select postId from Posts where userId ="+userId+" and type = 1";
			ResultSet resultSet = statement.executeQuery(queryExecute);
			List<Integer> result = new ArrayList<>();
			while(resultSet.next()){
				result.add(resultSet.getInt(1));
			}
			return result.size() > 0 ? result : null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public void addPost(int userId,String post,int type){
		try {
			Connection connection = new OpenConnectDatabase().openConnectDatabase();
			Statement statement = connection.createStatement();
			if(type == 2) { // post nay gui den ban be( chi ton tai trong 24h) , -> xoa post truoc do neu ton tai
			String delete = "delete from Posts where userId ="+userId+" and type = 2";
			statement.executeUpdate(delete);
			}
			String insert = "insert into Posts values("+userId+",'"+post+"',"+type+")";
			statement.executeUpdate(insert);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
