package DAO;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import Models.ChatBetweenTwoUsers;
import Models.InfoEachMessage;

public class ChatUserDAO {

	public void insertMessage(String insert) {
		try {
			Connection connection = new OpenConnectDatabase().openConnectDatabase();
			Statement statement = connection.createStatement();
			statement.executeUpdate(insert);
		} catch (SQLException e) {
			System.out.println("co ngoai le khi them message");
		}
	}

	public int checkStateFirstMessage(int userId, int user2Id) {
		try {
			Connection connection = new OpenConnectDatabase().openConnectDatabase();
			Statement statement = connection.createStatement();
			String queryExecute = "select States from chatUser where (userId =" + userId + " and user2Id = " + user2Id
					+ ") or(userId =" + user2Id + " and user2Id = " + userId + ")";
			ResultSet resultSet = statement.executeQuery(queryExecute);
			if (resultSet.next() == false)
				return -1;
			else {
				return resultSet.getInt(1);
			}
		} catch (SQLException e) {
		}
		return -1;
	}

	public boolean exitsMessageReceiveId(int userId, int user2Id) {
		try {
			Connection connection = new OpenConnectDatabase().openConnectDatabase();
			Statement statement = connection.createStatement();
			String queryExecute = "select * from chatUser where userId =" + user2Id + " and user2Id = " + userId;
			ResultSet resultSet = statement.executeQuery(queryExecute);
			return resultSet.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public InfoEachMessage getMessageByChatUserId(int chatUserId) { // lay ra toan bo thong tin message theo Id message
																	// => khoa chính cua bang
		try {
			Connection connection = new OpenConnectDatabase().openConnectDatabase();
			Statement statement = connection.createStatement();
			String queryExecute = "select * from chatUser where chatUserId = " + chatUserId;
			ResultSet resultSet = statement.executeQuery(queryExecute);
			if (resultSet.next() == false)
				return null;
			else {
				int senderId = resultSet.getInt(2);
				int recipientId = resultSet.getInt(3);
				int state = resultSet.getInt(4);
				String imageContent = resultSet.getString(5);
				String textContent = resultSet.getString(6);
				int contentType = resultSet.getInt(7);
				String sentTime = resultSet.getString(8);
				int seeMessage = resultSet.getInt(9);
				return new InfoEachMessage(senderId, recipientId, state, imageContent, textContent, contentType,
						sentTime, seeMessage);
			}
		} catch (SQLException e) {
			return null;
		}
	}

	public int getLastMessageIdBySender(int userId) { // id cua message moi nhat them vao co so du lieu. de gui ve cho										// user
		try {
			Connection connection = new OpenConnectDatabase().openConnectDatabase();
			Statement statement = connection.createStatement();
			String queryExecute = "select * from chatUser where userId = " + userId + " order by chatUserId desc";
			ResultSet resultSet = statement.executeQuery(queryExecute);
			return resultSet.next() == true ? resultSet.getInt(1) : -1;
		} catch (SQLException e) {
			return -1;
		}
	}

	public void changeStatesChat(int userId, int user2Id) {
		try {
			Connection connection = new OpenConnectDatabase().openConnectDatabase();
			Statement statement = connection.createStatement();
			String update = "update chatUser set States = 1 where (userId =" + userId + " and user2Id = " + user2Id
					+ ") or(userId =" + user2Id + " and user2Id = " + userId + ")";
			statement.executeUpdate(update);

		} catch (SQLException e) {
		}
	}

	public void deleteAllMessageWithUser(int userId, int userIdToDelete) {
		try {
			Connection connection = new OpenConnectDatabase().openConnectDatabase();
			Statement statement = connection.createStatement();
			String update = "delete from chatUser where (userId =" + userId + " and user2Id = " + userIdToDelete
					+ ") or(userId =" + userIdToDelete + " and user2Id = " + userId + ")";
			statement.executeUpdate(update);

		} catch (SQLException e) {
		}
	}

	public List<Integer> getAllUserSendMessageToUser(int userIdReceive) {
		try {
			List<Integer> result = new ArrayList<>();
			Set<Integer> check = new HashSet<>();
			Connection connection = new OpenConnectDatabase().openConnectDatabase();
			Statement statement = connection.createStatement();
			String query = "select userId from chatUser where user2Id = " + userIdReceive;
			ResultSet resultSet = statement.executeQuery(query);
			while (resultSet.next()) {
				int value = resultSet.getInt(1);
				if(result.add(value) == false) continue;
				result.add(resultSet.getInt(1));
			}
			return result.size() > 0 ? result : null;

		} catch (SQLException e) {
			return null;
		}
	}

	public List<InfoEachMessage> GetLastMessageListEachUser(int userId) {
		try {
			Connection connection = OpenConnectDatabase.openConnectDatabase();
			Statement statement = (Statement) connection.createStatement();
			// tai day , chung ta them mot dieu kien nua la cac doan chat k phai dang cho
			// co truong state trong database la 1;
			String query = "select * from chatUser where userId = " + userId + " or ( user2Id =" + userId
					+ " and states = 1 )";
			ResultSet resultSet = statement.executeQuery(query);
			List<InfoEachMessage> list = new ArrayList<>();
			tt: while (resultSet.next()) {
				// tiep theo chung ta se dua no vao trong 1 set =>
				int senderId = resultSet.getInt(2);
				int recipientId = resultSet.getInt(3);
				int state = 1; // cot 4
				String imageContent = resultSet.getString(5);
				String textContent = resultSet.getString(6);
				int contentType = resultSet.getInt(7);
				String sentTime = resultSet.getString(8);
				int seeMessage = resultSet.getInt(9);

				for (int i = 0; i < list.size(); i++) {
					if ((list.get(i).getSenderId() == senderId && list.get(i).getRecipientId() == recipientId)
							|| (list.get(i).getSenderId() == recipientId && list.get(i).getRecipientId() == senderId)) {
						InfoEachMessage infoEachMessage = new InfoEachMessage(senderId, recipientId, state,
								imageContent, textContent, contentType, sentTime, seeMessage);
						list.set(i, infoEachMessage);
						continue tt;
					}
				}
				// neu chay het vong lap for ma khong thay co dieu kien thoa man, chung to tin
				// nhan cua 2 nguoi la dau tien khi ta truy van. them vao arraylist
				list.add(new InfoEachMessage(senderId, recipientId, state, imageContent, textContent, contentType,
						sentTime, seeMessage));
			}
			return list.size() > 0 ? list : null;
		} catch (Exception e) {
			return null;
		}
	}

	
	public List<InfoEachMessage> GetListMessageWating(int userId) { 
		// cac tin nhan dang cho la chung ta chua gui di den user do 1 tin nhan nao.
		// do do chi co user do gui den . dong thoi cung k phai ban be => states = 0
		try {
			Connection connection = OpenConnectDatabase.openConnectDatabase();
			Statement statement = (Statement) connection.createStatement();
			// tai day , chung ta them mot dieu kien nua la cac doan chat k phai dang cho
			// co truong state trong database la 1;
			String query = "select * from chatUser where user2Id =" + userId
					+ " and states = 0 ";
			ResultSet resultSet = statement.executeQuery(query);
            System.out.println("00000");
			List<InfoEachMessage> list = new ArrayList<>();
			tt: while (resultSet.next()) { // danh sach cac user gui tin nhan den cho chung ta , mot chieu
				// tiep theo chung ta se dua no vao trong 1 set =>
				int senderId = resultSet.getInt(2);
				int recipientId = resultSet.getInt(3);
				int state = 0; // cot 4
				String imageContent = resultSet.getString(5);
				String textContent = resultSet.getString(6);
				int contentType = resultSet.getInt(7);
				String sentTime = resultSet.getString(8);
				int seeMessage = resultSet.getInt(9);
                System.out.println("11111");
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i).getSenderId() == senderId) {
						InfoEachMessage infoEachMessage = new InfoEachMessage(senderId, recipientId, state,
								imageContent, textContent, contentType, sentTime, seeMessage);
						list.set(i, infoEachMessage);
						continue tt;
					}
				}
				// neu chay het vong lap for ma khong thay co dieu kien thoa man, chung to tin nhan nguoi kia gui den ta la tin nhan dau tien .
				list.add(new InfoEachMessage(senderId, recipientId, state, imageContent, textContent, contentType,
						sentTime, seeMessage));
			}
			return list.size() > 0 ? list : null;
		} catch (Exception e) {
            System.out.println("co ngoai le khi truy van message wating");
			return null;
		}
	}

	
	public List<ChatBetweenTwoUsers> getMessageBetweenTwoUser(int userId, int user2Id) {
		try {
			Connection connection = OpenConnectDatabase.openConnectDatabase();
			Statement statement = connection.createStatement();
			String query = "select *  from chatUser where ( userId = " + userId + " and user2Id = " + user2Id
					+ ") or ( userId = " + user2Id + " and user2Id = " + userId + ")";
			ResultSet resultSet = statement.executeQuery(query);
			JSONArray JSONArray = new JSONArray();
			List<ChatBetweenTwoUsers> list = new ArrayList<>();
			while (resultSet.next()) {
				int messageId = resultSet.getInt(1);
				int senderId = -1;
				if (resultSet.getInt(2) == userId) {
					senderId = userId;
				} else
					senderId = user2Id;
				String messageType = "";
				String content = "";
				if (resultSet.getString(5) != null) {
					// check tiep o cot 7 xem kieu la gi . neu la 0 thi gui ve Image. neu la 2 thi
					// gui ve LikeImage
					if (resultSet.getInt(7) == 0) {
						messageType = "Image";
					} else {
						messageType = "LikeImage";
					}
					content = resultSet.getString(5);
				} else {
					messageType = "Text";
					content = new String(Base64.getDecoder().decode(resultSet.getString(6)), StandardCharsets.UTF_8);
				}
				list.add(new ChatBetweenTwoUsers(messageId, senderId, messageType, content));
			}
			return list.size() > 0 ? list : null;

		} catch (Exception e) {
			return null;
		}
	}

	public List<Integer> getTopUsersSendMessages(int userId) {
		try {
			List<Integer> result = new ArrayList<>();
			Connection connection = new OpenConnectDatabase().openConnectDatabase();
			Statement statement = connection.createStatement();
			String query = "select top 30 users.userId from users inner join  chatUser on users.userId = chatUser.userId or"
					+ "	 users.userId = chatUser.user2Id where (chatUser.userId = "+userId+" or chatUser.user2Id = "+userId+" ) and users.userId <> "+userId+" and "
					+ "	 users.showState = 1 group by users.userId order by count(*) desc";
			ResultSet resultSet = statement.executeQuery(query);			
			while (resultSet.next()) {
				result.add(resultSet.getInt(1));
			}
			return result.size() > 0 ? result : null;

		} catch (SQLException e) {
			return null;
		}
	}

	public void setSeenAllMessagesOfUser(int userReceive, int userSent) {
		try {
			Connection connection = OpenConnectDatabase.openConnectDatabase();
			Statement statement = connection.createStatement();
			String statementUpdate = "update  chatUser set seeMessage = 1 where( userId = " + userSent + " and user2Id = " + userReceive + ")";
			statement.executeUpdate(statementUpdate);
	    } catch(Exception e) {}
	}
}
