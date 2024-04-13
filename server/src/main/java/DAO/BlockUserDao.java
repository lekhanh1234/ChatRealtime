package DAO;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import Models.GeneralUserInfomation;


public class BlockUserDao {
public boolean checkIsBlockUser(int userId,int user2Id){
		
		try {
			Connection connection = new OpenConnectDatabase().openConnectDatabase();
			Statement statement = connection.createStatement();
			String queryExecute = "select * from blockUser where ( userId = "+user2Id +" and userBlockedId="+userId +") or( userId = "+userId +" and userBlockedId = "+user2Id+" )";
			ResultSet resultSet = statement.executeQuery(queryExecute);
			return resultSet.next();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public boolean BlockUser(int userId, int userIdToBlock) {

		try {
			Connection connection = new OpenConnectDatabase().openConnectDatabase();
			Statement statement = connection.createStatement();
			
			// nếu hiện tại đã block thì trả về false. vì đã block rồi
			String queryExecute = "select * from blockUser where userId = "+userId +" and userBlockedId="+userIdToBlock;
			ResultSet resultSet =statement.executeQuery(queryExecute);
			if(resultSet.next() == true) return false;
			// ngược lại tiến hành thao tác block
			
			
			// thực hiện 2 thao tác. xóa bạn bè nếu là bạn bè. xóa lời mời kết bạn cả 2 phía
			FriendDAO daoFriend = new FriendDAO();
			daoFriend.deleteFriend(userId, userIdToBlock); // ta se thuc hien hanh dong xoa ban be ma k can kiem tra
															// truoc do co ket ban hay khong
			daoFriend.deleteFriend(userIdToBlock, userId);
			FriendRequestDAO friendRequestDao = new FriendRequestDAO();
			friendRequestDao.cancelFriendRequest(userId, userIdToBlock);
			friendRequestDao.cancelFriendRequest(userIdToBlock, userId);
			LocalDateTime currentTime = LocalDateTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
			String formattedTime = currentTime.format(formatter);
			System.out.println("current time :"+formattedTime);
			// sau khi thuc hien xong ta se tien hanh block user vao trong bang block user
			String insert = "insert into blockUser values(" + userId + "," + userIdToBlock + ",'" + formattedTime + "')";
			statement.executeUpdate(insert);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean deleteBlockUser(int userId, int userBlockedId) {

		try {
			Connection connection = new OpenConnectDatabase().openConnectDatabase();
			Statement statement = connection.createStatement();
			// kiểm tra xem có phải 2 ngày đã trôi qua hay không
			String checkTime = "select timeBlock from blockUser where userId = " + userId + " and userBlockedId = "
					+ userBlockedId;

			ResultSet resultSet = statement.executeQuery(checkTime);
			resultSet.next();
			// lay ra thoi gian block
			String strThoiGianTuCSDL = resultSet.getString(1);
			strThoiGianTuCSDL = strThoiGianTuCSDL.split("\\.")[0];
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			LocalDateTime thoiDiemTuCSDL = LocalDateTime.parse(strThoiGianTuCSDL, formatter);
						
			LocalDateTime thoiDiemHienTai = LocalDateTime.now();
			Duration khoangCach = Duration.between(thoiDiemTuCSDL, thoiDiemHienTai);
			// Lấy số ngày đã trôi qua
			long soNgay = khoangCach.toDays();
			// Kiểm tra xem số ngày đã trôi qua có lớn hơn hoặc bằng 2 hay không
			if (soNgay >= 2) {
				System.out.println("Đã trôi qua hai ngày");
			} else {
				System.out.println("Chưa trôi qua hai ngày");
				return false;
			}

			// lenh xoa user bị block trong csdl
			String deleteStament = "delete from blockUser where userId =" + userId + " and userBlockedId = "
					+ userBlockedId;
			statement.executeUpdate(deleteStament);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public List<GeneralUserInfomation> getAllUserBlock(int userId) throws IOException{ // tất cả user mà tài khoản của ta block
		List<GeneralUserInfomation> list = new ArrayList<>();
		String statementQuery = "select * from blockUser where userId = "+userId;
		Connection openDB = new OpenConnectDatabase().openConnectDatabase();
		try {
			Statement statement = openDB.createStatement();
			ResultSet resultSet = statement.executeQuery(statementQuery);
			while(resultSet.next()){
				int userIdBlock = resultSet.getInt(3);
				// có id. tiếp theo là lấy tên và ảnh đại diện
				UsersDAO usersDAO = new UsersDAO();
				String accountName = usersDAO.getAccountNameById(userIdBlock);
				String pathAvatar = usersDAO.getAvataPathById(userIdBlock);
				String avatar = imageToBase64(pathAvatar);
				list.add(new GeneralUserInfomation(userIdBlock, avatar, accountName));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list.size() > 0 ? list : null;
	}
	
	private String imageToBase64(String imagePath) throws IOException {
		File file = new File(imagePath);
		FileInputStream imageInFile = new FileInputStream(file);
		byte[] imageData = new byte[(int) file.length()];
		imageInFile.read(imageData);
		return Base64.getEncoder().encodeToString(imageData);
	}
}
