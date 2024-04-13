package UserActivityWithAccount;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.websocket.Session;
import DAO.OpenConnectDatabase;
import WebSocket.ManagementSockets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONObject;

import DAO.ChatUserDAO;
import DAO.FriendDAO;
import DAO.UsersDAO;

/**
 * Servlet implementation class changePrivacyInfo
 */
public class ChangePrivacyInfo extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public ChangePrivacyInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		String content = getContentByClientSend(request.getInputStream());
		JSONObject jsonObject = new JSONObject(content);
		int userId = (int) jsonObject.get("userId");
		String myName = jsonObject.getString("myName");
		int sex = jsonObject.getString("sex").equals("Nam") ? 1 : 0;
		String birthday = jsonObject.getString("birthday");
		String accountName = jsonObject.getString("accountName");
		
		new UsersDAO().changePrivacyInfo(myName, sex, birthday, accountName, userId);
		
		List<Integer> friendIdList = new FriendDAO().getFriendIdByUserId(userId, Integer.MAX_VALUE);
		List<Integer> userIdHasSendMessage = new ChatUserDAO().getAllUserSendMessageToUser(userId);
		List<Integer> containResult = new ArrayList<>();
		if(friendIdList != null)containResult.addAll(friendIdList);
		if(userIdHasSendMessage != null)containResult.addAll(userIdHasSendMessage);
		Set<Integer> setKey = ManagementSockets.sockets.keySet();
		for (int x : setKey) {
			if (containResult.contains(x)) {
				// gửi thông điệp đó về ngay cho các user có tương tác với tài khoản account này
				JSONObject jsonObject2 = new JSONObject();
				jsonObject2.put("type", "4");
				jsonObject2.put("id", userId);
				jsonObject2.put("accountName", accountName);
				try {
					ManagementSockets.sockets.get(x).session.getBasicRemote().sendText(jsonObject2.toString());
				} catch (Exception e) {
					System.out.println("xay ra ngoai le gui message ve client = websocket trong ham ChangePrivacyInfo");
				}
			}
		}

	}
	public	String getContentByClientSend(InputStream is) {
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
		String s = "";
		StringBuilder builder = new StringBuilder();
		try {
			while ((s = bufferedReader.readLine()) != null) {
				builder.append(s).append("\n");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(builder.toString().length() == 0) return "";
		return builder.toString().substring(0, builder.toString().length() - 1); // bỏ kí tự /n ở cuối
	}

}
