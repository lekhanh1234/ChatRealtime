package DAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class FriendDAO {
	public List<Integer> getFriendIdByUserId(int userId,int amount){
		
		List<Integer> friendIdList = new ArrayList<>();
		try {
			Connection connection = new OpenConnectDatabase().openConnectDatabase();
			Statement statement = connection.createStatement();
			String queryExecute = "select top "+amount+ " * from Friends where userId = "+userId;
			ResultSet resultSet = statement.executeQuery(queryExecute);
			while(resultSet.next()) {
				friendIdList.add(resultSet.getInt(3));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return friendIdList.size() > 0 ? friendIdList : null;
	}
	
public int getAmountFriendIdByUserId(int userId){
		try {
			Connection connection = new OpenConnectDatabase().openConnectDatabase();
			Statement statement = connection.createStatement();
			String queryExecute = "select count(*) from Friends where userId = "+userId;
			ResultSet resultSet = statement.executeQuery(queryExecute);
			resultSet.next();
			return resultSet.getInt(1);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
	
	
   public boolean checkIsFriend(int userId,int user2Id){
		
		try {
			Connection connection = new OpenConnectDatabase().openConnectDatabase();
			Statement statement = connection.createStatement();
			String queryExecute = "select * from Friends where userId = "+userId +" and userFriendId="+user2Id;
			ResultSet resultSet = statement.executeQuery(queryExecute);
			return resultSet.next();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
   
   public void addFriend(int userId,int user2Id){
		
		try {
			Connection connection = new OpenConnectDatabase().openConnectDatabase();
			Statement statement = connection.createStatement();
			String insertStatement = "insert into friends values("+userId+","+user2Id+")";
			String insertStatement2 = "insert into friends values("+user2Id+","+userId+")";
			statement.executeQuery(insertStatement);
			statement.executeQuery(insertStatement2);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
   
   public void deleteFriend(int userId,int user2Id){
		
		try {
			Connection connection = new OpenConnectDatabase().openConnectDatabase();
			Statement statement = connection.createStatement();
			String deleteStatement = "delete from friends where userId ="+userId+" and userFriendId ="+user2Id;
			String deleteStatement2 = "delete from friends where userId ="+user2Id+" and userFriendId ="+userId;
			statement.executeUpdate(deleteStatement);
			statement.executeUpdate(deleteStatement2);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
   
   public List<Integer> getMutualFriends(int userId,int user2Id){
		List<Integer> list3 = new ArrayList<>();
 		try {
 			Connection connection = new OpenConnectDatabase().openConnectDatabase();
 			Statement statement = connection.createStatement();
 			List<Integer> list1 = getFriendIdByUserId(userId,Integer.MAX_VALUE);
 			List<Integer> list2 = getFriendIdByUserId(user2Id,Integer.MAX_VALUE);
 			if(list1 == null || list2 == null) return null;
 			for(int x : list1) {
 				if(list2.contains(x)) {
 					list3.add(x);
 				}
 			}

 		} catch (SQLException e) {
 			// TODO Auto-generated catch block
 			list3.clear();
 		}
 		return list3.size() > 0 ? list3 : null;
 	}
   
}
