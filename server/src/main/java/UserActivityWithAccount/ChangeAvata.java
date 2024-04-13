package UserActivityWithAccount;

import jakarta.json.JsonObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONObject;

import DAO.ChatUserDAO;
import DAO.FriendDAO;
import DAO.UsersDAO;
import WebSocket.ManagementSockets;

/**
 * Servlet implementation class ChangeAvata
 */
public class ChangeAvata extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ChangeAvata() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String contentFromClient = getContentByClientSend(request.getInputStream());
		JSONObject jsonObject = new JSONObject(contentFromClient);
		String fileType = jsonObject.getString("fileType");
		String avatar = jsonObject.getString("avataText");
		int userId = jsonObject.getInt("userId");
		byte[] a = Base64.getDecoder().decode(avatar);
		// lay ra duong dan avata va xoa file
		UsersDAO dao = new UsersDAO();
		String avataPath = dao.getAvataPathById(userId);
		if(avataPath!=null){
		File file = new File(avataPath);
		if(file.exists()) file.delete();
		}
		// chung ta can luu y rang. avata o tren la avata da duoc ma hoa sang dang co so 64;
		String newAvataPath = "C:\\AllMessengerProjectFile\\Picture\\Profile\\avatar"+userId+"."+fileType;
		FileOutputStream fileOutputStream = new FileOutputStream(newAvataPath);
		fileOutputStream.write(a);
		UsersDAO usersDao = new UsersDAO();
		usersDao.changeAvataPathById(userId, newAvataPath);
		
		////////thông báo cho các user đã gửi tin nhắn đến cho account chúng ta hoặc là bạn bè
		List<Integer> friendIdList = new FriendDAO().getFriendIdByUserId(userId,Integer.MAX_VALUE);
		List<Integer> userIdHasSendMessage = new ChatUserDAO().getAllUserSendMessageToUser(userId);
		Set<Integer> containResult = new HashSet<>();
		if(friendIdList != null ) containResult.addAll(friendIdList);
		if(userIdHasSendMessage != null ) containResult.addAll(userIdHasSendMessage);
		Set<Integer> setKey = ManagementSockets.sockets.keySet();
		for(int x : setKey) {
			if(containResult.contains(x)) {
				// gửi thông điệp đó về ngay cho user đó
				JSONObject jsonObject2 = new JSONObject();
				jsonObject2.put("type", "5");
				jsonObject2.put("id",userId);
				jsonObject2.put("avatarUser", avatar);
				try {
				ManagementSockets.sockets.get(x).session.getBasicRemote().sendText(jsonObject2.toString());
				}catch(Exception e) {
					System.out.println("ngoai le thong bao user thay doi avatar den users");
				}
			}
		}
	
		}
		
        // lay ra them thong tin nhung nguoi tung gui tin nhan cho chung ta va gui thong diep ve cho cac user do
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
