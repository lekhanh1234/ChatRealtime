package AccountInformation;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static org.bytedeco.opencv.global.opencv_core.CV_8UC1;
import static org.bytedeco.opencv.global.opencv_imgcodecs.imwrite;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.time.Instant;
import java.util.Base64;
import java.util.List;

import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_videoio.VideoCapture;
import org.json.JSONArray;
import org.json.JSONObject;

import DAO.BlockUserDao;
import DAO.ChatUserDAO;
import DAO.FriendDAO;
import DAO.UsersDAO;
import DAO.FriendRequestDAO;
import DAO.PostsDAO;
import Models.GeneralUserInfomation;
import WebSocket.ManagementSockets;

/**
 * Servlet implementation class GetUserInteraction
 */
public class GetUserInteraction extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GetUserInteraction() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		int userId = Integer.parseInt(request.getParameter("userId"));
		int type = Integer.parseInt(request.getParameter("type"));
		/////
		switch (type) {
		case 1:
			getAllOnlineFriend(userId, request, response);
			break;
		case 2:
			getOnlineChatUser(userId, request, response);
			break;
		case 3:
			getFriendRequestUserSend(userId, request, response);
			break;
		case 4:
			getFriendRequestUserReceive(userId, request, response);
			break;
		case 5:
			getMutualFriends(userId, request, response);
			break;
		case 6:
			searchUser(userId,request, response);
			break;
		case 7:
			checkRelationshipUser(userId, request, response);
			break;
		case 8:
			getFriendOfUser(userId, request, response);
			break;
		case 9:
			getUserAmountFollow(userId, request, response);
			break;
		case 10:
			getAmountFriend(userId, request, response);
			break;
		case 11:
			getUsersPost24H(userId, request, response);
			break;
		default:
			break;
		}
	}

	public void getAllOnlineFriend(int userId, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		List<Integer> list = new FriendDAO().getFriendIdByUserId(userId, Integer.MAX_VALUE);
		JSONArray jsonArray = new JSONArray();
		if (list != null) {
			// moi dong next la lay ra mot id
			for (int i = 0; i < list.size(); i++) {
				JSONObject jsonObject = new JSONObject();
				int userFriendId = list.get(i);
				if (ManagementSockets.stateClientOnline.containsKey(userFriendId) == false)
					continue;
				long lastTimeUserOnline = ManagementSockets.stateClientOnline.get(userFriendId);
				long currentTimeSeconds = Instant.now().getEpochSecond();
				if (currentTimeSeconds - lastTimeUserOnline > 60)
					continue;
				jsonObject.put("friendId", userFriendId);
				String avatarPath = new UsersDAO().getAvataPathById(userFriendId);
				jsonObject.put("avatar", Base64Image(avatarPath));
				String accountName = new UsersDAO().getAccountNameById(userFriendId);
				jsonObject.put("accountName", accountName);
				jsonArray.put(jsonObject);
			}
		}
		System.out.println("size la :"+jsonArray.length());
		response.getWriter().print(jsonArray.toString());
	}

	public void getOnlineChatUser(int userId, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		List<Integer> list = new ChatUserDAO().getTopUsersSendMessages(userId);
		JSONArray jsonArray = new JSONArray();
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				JSONObject jsonObject = new JSONObject();
				int userUserId = list.get(i);
				if(new BlockUserDao().checkIsBlockUser(userUserId, userId)) continue;
				if (ManagementSockets.stateClientOnline.containsKey(userUserId) == false)
					continue;
				long lastTimeUserOnline = ManagementSockets.stateClientOnline.get(userUserId);
				long currentTimeSeconds = Instant.now().getEpochSecond();
				if (currentTimeSeconds - lastTimeUserOnline > 60)
					continue;
				jsonObject.put("userId", userUserId);
				String avatarPath = new UsersDAO().getAvataPathById(userUserId);
				jsonObject.put("avatar", Base64Image(avatarPath));
				String accountName = new UsersDAO().getAccountNameById(userUserId);
				jsonObject.put("accountName", accountName);
				jsonArray.put(jsonObject);
			}
		}
		response.getWriter().print(jsonArray.toString());
	}

	public void getFriendRequestUserSend(int userId, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		List<GeneralUserInfomation> list = new FriendRequestDAO().getRequestFriendUserSend(userId);
		JSONArray jsonArray = new JSONArray();
		if (list != null) {
			for (GeneralUserInfomation x : list) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("userId", x.getIdUser());
				jsonObject.put("accountName", x.getAccountName());
				jsonObject.put("avatarInBase64", x.getAvatar());
				jsonArray.put(jsonObject);
			}
		}
		response.getWriter().print(jsonArray);
	}

	public void getFriendRequestUserReceive(int userId, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		List<GeneralUserInfomation> list = new FriendRequestDAO().getRequestFriendToUser(userId);
		JSONArray jsonArray = new JSONArray();
		if (list != null) {
			for (GeneralUserInfomation x : list) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("userId", x.getIdUser());
				jsonObject.put("accountName", x.getAccountName());
				jsonObject.put("avatarInBase64", x.getAvatar());
				jsonArray.put(jsonObject);
			}
		}
		response.getWriter().print(jsonArray);
	}

	public void getMutualFriends(int userId, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		int user2Id = Integer.parseInt(request.getParameter("user2Id"));
		List<Integer> list = new FriendDAO().getMutualFriends(userId, user2Id);
		if (list == null)
			response.getWriter().print("0");
		else
			response.getWriter().print(list.size() + "");
	}

	public void searchUser(int userId,HttpServletRequest request, HttpServletResponse response) throws IOException {
		String userAccountName = request.getParameter("userAccountName");
		List<GeneralUserInfomation> list = new UsersDAO().getInfoSearchByUser(userAccountName);
		// gui du lieu ve cho client
		JSONArray jsonArray = new JSONArray();
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				JSONObject jsonObject = new JSONObject();
				GeneralUserInfomation userSearch = list.get(i);
				if(userSearch.getIdUser() == userId) continue;
				jsonObject.put("userId", userSearch.getIdUser());
				jsonObject.put("userAccountName", userSearch.getAccountName());
				jsonObject.put("avatarInBase64Format", userSearch.getAvatar());
				jsonArray.put(jsonObject);
			}
		}
		response.getWriter().print(jsonArray.toString());
	}

	public void checkRelationshipUser(int userId, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		int user2Id = Integer.parseInt(request.getParameter("user2Id"));
		PrintWriter pw = response.getWriter();
		// check xem có phải bị block hay không. -> dù là ta block hay ta bị block => -1
		if (new BlockUserDao().checkIsBlockUser(userId, user2Id) == true) { // ta bị block , vì tham số thứ 2 là người
																			// thực hiện block
			pw.print(-1 + "");
		} else if (new BlockUserDao().checkIsBlockUser(user2Id, userId) == true) { // ta block user kia
			pw.print(-1 + "");
		} else if (new FriendDAO().checkIsFriend(user2Id, userId)) { // nếu bạn bè => 3
			pw.print(3 + "");
		} else if (new FriendRequestDAO().checkFriendRequest(userId, user2Id)) { // ta gửi đi một yêu cầu kết bạn
			pw.print(2 + "");
		} else if (new FriendRequestDAO().checkFriendRequest(user2Id, userId)) { // ta nhận một lời mời kết bạn
			pw.print(1 + "");
		} else
			pw.print(0 + ""); // 2 người chưa có bất kì tương tác nào
	}

	public void getFriendOfUser(int userId, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		int amount = Integer.parseInt(request.getParameter("amount"));
		List<Integer> list = new FriendDAO().getFriendIdByUserId(userId, amount);
		// lấy ra friend
		JSONArray jsonArray = new JSONArray();
		if (list != null) {
			for (int i = 0; i < list.size() && i < amount; i++) {
				JSONObject jsonObject = new JSONObject();
				int userFriendId = list.get(i);
				jsonObject.put("friendId", userFriendId);
				String avatarPath = new UsersDAO().getAvataPathById(userFriendId);
				jsonObject.put("avatar", Base64Image(avatarPath));
				String accountName = new UsersDAO().getAccountNameById(userFriendId);
				jsonObject.put("accountName", accountName);
				jsonArray.put(jsonObject);
			}
		}
		response.getWriter().print(jsonArray.toString());
	}

	public void getUserAmountFollow(int userId, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		int result = new FriendRequestDAO().getAmountFollowUser(userId);
		response.getWriter().print(result + "");
	}

	public void getAmountFriend(int userId, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		int amountFriend = new FriendDAO().getAmountFriendIdByUserId(userId);
		response.getWriter().print(amountFriend + "");
	}

	public void getUsersPost24H(int userId, HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		JSONArray jsonArray = new JSONArray();
		String myPathPost = "C:\\AllMessengerProjectFile\\Post\\" + userId
				+ "video2.mp4";
		if (new File(myPathPost).exists()){
			JSONObject jsonObject = new JSONObject();
			String accountName = new UsersDAO().getAccountNameById(userId);
			String path = new UsersDAO().getAvataPathById(userId);
			String avatarInBase64 = Base64Image(path);
			byte[] post = splitFirstFramVideo(myPathPost, userId);
			String postInBase64 = Base64.getEncoder().encodeToString(post);
			jsonObject.put("userId", userId);
			jsonObject.put("accountName", accountName);
			jsonObject.put("avatarInBase64", avatarInBase64);
			jsonObject.put("postInBase64", postInBase64);
			jsonArray.put(jsonObject);
		}
		List<Integer> list = new FriendDAO().getFriendIdByUserId(userId, Integer.MAX_VALUE);
		if (list != null) {
			for (int friendId : list) {
				String myFriendPostPath = "C:\\AllMessengerProjectFile\\Post\\" + friendId
						+ "video2.mp4";
				if (new File(myFriendPostPath).exists()){
					JSONObject jsonObject = new JSONObject();
					String accountName = new UsersDAO().getAccountNameById(friendId);
					String path = new UsersDAO().getAvataPathById(friendId);
					String avatarInBase64 = Base64Image(path);
					byte[] post = splitFirstFramVideo(myFriendPostPath, userId);
					String postInBase64 = Base64.getEncoder().encodeToString(post);
					jsonObject.put("userId", friendId);
					jsonObject.put("accountName", accountName);
					jsonObject.put("avatarInBase64", avatarInBase64);
					jsonObject.put("postInBase64", postInBase64);
					jsonArray.put(jsonObject);
				}
			}
		}
		response.getWriter().print(jsonArray.toString());

	}

	public String Base64Image(String path) {
		File file = new File(path);
		try {
			FileInputStream fis = new FileInputStream(file);
			byte avatar[] = new byte[(int) file.length()];
			fis.read(avatar, 0, avatar.length);
			fis.close();
			return Base64.getEncoder().encodeToString(avatar);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private byte[] splitFirstFramVideo(String path, int userId) throws IOException {
		int height = 240;
		int width = 320;
		Mat frame = new Mat(height, width, CV_8UC1);
		VideoCapture capture = new VideoCapture(path);
		if (capture.isOpened()) {
			capture.read(frame);
		}
		if (frame != null) { //
			imwrite("E:\\saveFileMP4" + userId + ".jpg", frame);
		}
		File file = new File("E:\\saveFileMP4" + userId + ".jpg");
		FileInputStream fis = new FileInputStream(file);
		byte read[] = new byte[fis.available()];
		fis.read(read);
		file.delete();
		return read;
	}

}
