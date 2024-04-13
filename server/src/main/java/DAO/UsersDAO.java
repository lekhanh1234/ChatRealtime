package DAO;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.json.JSONObject;

import Models.GeneralUserInfomation;

public class UsersDAO {

	// lay thong tin toan bo danh sach id ban be theo ClientId
	//
	public String getAccountNameById(int userId) {
		try {
			Connection connection = new OpenConnectDatabase().openConnectDatabase();
			Statement statement = connection.createStatement();
			String queryExecute = "select accountName from Users where userId = " + userId;
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

	public String getIntroduceById(int userId) {
		try {
			Connection connection = new OpenConnectDatabase().openConnectDatabase();
			Statement statement = connection.createStatement();
			String queryExecute = "select Introduce from Users where userId = " + userId;
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

	public String getUserNameLoginById(int userId) {
		try {
			Connection connection = new OpenConnectDatabase().openConnectDatabase();
			Statement statement = connection.createStatement();
			String queryExecute = "select userNameLogin from Users where userId = " + userId;
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

	public int getShowStatesById(int userId) {
		try {
			Connection connection = new OpenConnectDatabase().openConnectDatabase();
			Statement statement = connection.createStatement();
			String queryExecute = "select showState from Users where userId = " + userId;
			ResultSet resultSet = statement.executeQuery(queryExecute);
			if (resultSet.next()) {
				return resultSet.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}

	public int getStateInteractiveById(int userId) {
		try {
			Connection connection = new OpenConnectDatabase().openConnectDatabase();
			Statement statement = connection.createStatement();
			String queryExecute = "select usersInteractive from Users where userId = " + userId;
			ResultSet resultSet = statement.executeQuery(queryExecute);
			if (resultSet.next()) {
				return resultSet.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}

	public boolean checkPasswordByUserId(int userId, String passWord) {
		try {
			Connection connection = new OpenConnectDatabase().openConnectDatabase();
			Statement statement = connection.createStatement();
			String queryExecute = "select * from Users where userId = " + userId
					+ " and passwordLogin COLLATE Latin1_General_CS_AS ='" + passWord + "'";
			ResultSet resultSet = statement.executeQuery(queryExecute);
			if (resultSet.next()) {
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public int relationshipBetweenUser(int userId, int user2Id) {
		BlockUserDao blockUserDao = new BlockUserDao();
		FriendDAO friendDAO = new FriendDAO();
		if (blockUserDao.checkIsBlockUser(userId, user2Id))
			return -1;
		if (blockUserDao.checkIsBlockUser(user2Id, userId))
			return -1;
		if (friendDAO.checkIsFriend(userId, user2Id))
			return 1;
		return 0;
	}

	public String getAvataPathById(int userId) {
		try {
			Connection connection = new OpenConnectDatabase().openConnectDatabase();
			Statement statement = connection.createStatement();
			String queryExecute = "select avataPath from Users where userId = " + userId;
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

	public String getCoverImagePathById(int userId) {
		try {
			Connection connection = new OpenConnectDatabase().openConnectDatabase();
			Statement statement = connection.createStatement();
			String queryExecute = "select coverImagePath from Users where userId = " + userId;
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

	public int getIdbyUserNameAndPassword(String userName, String password) {
		try {
			Connection connection = new OpenConnectDatabase().openConnectDatabase();
			Statement statement = connection.createStatement();
			String queryExecute = "select * from Users where userNameLogin COLLATE Latin1_General_CS_AS = '" + userName
					+ "' and passwordLogin COLLATE Latin1_General_CS_AS = '" + password + "'";
			ResultSet resultSet = statement.executeQuery(queryExecute);
			if (resultSet.next()) {
				return resultSet.getInt(1);
			} else
				return 0;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	public List<GeneralUserInfomation> getInfoSearchByUser(String userAccountName) {
		List<GeneralUserInfomation> list = new ArrayList<>();
		try {
			Connection connection = new OpenConnectDatabase().openConnectDatabase();
			Statement statement = connection.createStatement();
			String queryExecute = "select top 30 userId,accountName,avataPath from Users where accountName LIKE '"
					+ userAccountName + "%'";
			ResultSet resultSet = statement.executeQuery(queryExecute);
			while (resultSet.next()) {
				int userId = resultSet.getInt(1);
				String accountName = resultSet.getString(2);
				String filePath = resultSet.getString(3);
				FileInputStream fis = new FileInputStream(filePath);
				byte file[] = new byte[fis.available()];
				fis.read(file);
				// chuyen ve dang base 64
				String avatarInBase64;
				avatarInBase64 = Base64.getEncoder().encodeToString(file);
				list.add(new GeneralUserInfomation(userId, avatarInBase64, accountName));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list.size() > 0 ? list : null;
	}

	public void changePasswordById(int userId, String newPassword) {
		try {
			Connection connection = new OpenConnectDatabase().openConnectDatabase();
			Statement statement = connection.createStatement();
			String queryExecute = "update Users set passwordLogin = '" + newPassword + "' where userId =" + userId;
			statement.executeUpdate(queryExecute);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int getSex(int userId) {
		try {
			Connection connection = new OpenConnectDatabase().openConnectDatabase();
			Statement statement = connection.createStatement();
			String query = "select * from Users where userId = " + userId;
			ResultSet resultSet = statement.executeQuery(query);
			resultSet.next();
			int sex = resultSet.getInt("Sex"); // nếu trả về client 0 -> có thể hiểu là k co value
			return sex;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public String getUserName(int userId) {
		try {
			Connection connection = new OpenConnectDatabase().openConnectDatabase();
			Statement statement = connection.createStatement();
			String query = "select * from Users where userId = " + userId;
			ResultSet resultSet = statement.executeQuery(query);
			resultSet.next();
			return resultSet.getString("userName");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public String getBirthDay(int userId) {
		try {
			Connection connection = new OpenConnectDatabase().openConnectDatabase();
			Statement statement = connection.createStatement();
			String query = "select * from Users where userId = " + userId;
			ResultSet resultSet = statement.executeQuery(query);
			resultSet.next();
			return resultSet.getString("Birthday");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public void changeInteractiveUser(int userId, int Interactive) {
		try {
			Connection connection = new OpenConnectDatabase().openConnectDatabase();
			Statement statement = connection.createStatement();
			String queryExecute = "update Users set usersInteractive = " + Interactive + " where userId =" + userId;
			statement.executeUpdate(queryExecute);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void changeShowState(int userId, int state) {
		try {
			Connection connection = new OpenConnectDatabase().openConnectDatabase();
			Statement statement = connection.createStatement();
			String queryExecute = "update Users set showState = " + state + " where userId =" + userId;
			statement.executeUpdate(queryExecute);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void changeIntroduce(int userId, String newIntroduce) {
		try {
			Connection connection = new OpenConnectDatabase().openConnectDatabase();
			Statement statement = connection.createStatement();
			String queryExecute = null;
			if (newIntroduce.length() == 0) {
				queryExecute = "update Users set Introduce = null where userId =" + userId;
			} else
				queryExecute = "update Users set Introduce = '" + newIntroduce + "' where userId =" + userId;
			statement.executeUpdate(queryExecute);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void changePrivacyInfo(String myName, int sex, String birthday, String accountName, int userId) {
		try {
			Connection connection = new OpenConnectDatabase().openConnectDatabase();
			Statement statement = connection.createStatement();
			String queryExecute = "update users set userName ='" + myName + "', sex =" + sex + ",birthday = '"
					+ birthday + "',accountName = '" + accountName + "' where userId =" + userId;
			statement.executeUpdate(queryExecute);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void changeAvataPathById(int userId, String avataPath) {
		try {
			Connection connection = new OpenConnectDatabase().openConnectDatabase();
			Statement statement = connection.createStatement();
			String queryExecute = "update Users set avataPath = '" + avataPath + "' where userId =" + userId;
			statement.executeUpdate(queryExecute);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void changeCoverImagePathById(int userId, String coverImagePath) {
		try {
			Connection connection = new OpenConnectDatabase().openConnectDatabase();
			Statement statement = connection.createStatement();
			String queryExecute = "update Users set coverImagePath = '" + coverImagePath + "' where userId =" + userId;
			statement.executeUpdate(queryExecute);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void addNewUser(String userName, String password) {
		try {
			Connection connection = new OpenConnectDatabase().openConnectDatabase();
			Statement statement = connection.createStatement();
			String insert = "insert into Users values('" + userName + "','" + password
					+ "','anonymous',1,3,'anonymous',null,null,null,null,null)";
			statement.executeUpdate(insert);
			int idAdded = new UsersDAO().getIdbyUserNameAndPassword(userName, password);
			Path sourcePath = Paths.get("C:\\AllMessengerProjectFile\\Picture\\Profile\\profiledefault\\avatar.jpg");
			Path source2Path = Paths
					.get("C:\\AllMessengerProjectFile\\Picture\\Profile\\profiledefault\\coverImage.jpg");
			String avatarFileName = "avatar" + idAdded + ".jpg";
			String coverImageFileName = "coverImage" + idAdded + ".jpg";
			Path destinationPath = Paths.get("C:\\AllMessengerProjectFile\\Picture\\Profile\\" + avatarFileName);
			Path destination2Path = Paths.get("C:\\AllMessengerProjectFile\\Picture\\Profile\\" + coverImageFileName);
			Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
			Files.copy(source2Path, destination2Path, StandardCopyOption.REPLACE_EXISTING);
			changeAvataPathById(idAdded,"C:\\AllMessengerProjectFile\\Picture\\Profile\\" + avatarFileName);
			changeCoverImagePathById(idAdded,"C:\\AllMessengerProjectFile\\Picture\\Profile\\" + coverImageFileName);
			

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
