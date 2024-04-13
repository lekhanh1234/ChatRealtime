package AccountInformation;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import DAO.ChatUserDAO;
import DAO.UsersDAO;
import Models.ChatBetweenTwoUsers;
import Models.InfoEachMessage;

/**
 * Servlet implementation class GetMessage
 */
public class GetMessage extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetMessage() {
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
		int userId = Integer.parseInt(request.getParameter("userId"));
        int type = Integer.parseInt(request.getParameter("type"));
        switch(type) {
        case 1 :
        	getMessageForFg1(userId,request,response);
        	break;
        case 2 :
        	chatBetweenTwoUsers(userId,request,response);
        	break;
        case 3 :
        	getMessageListWating(userId,request,response);
        	break;	
        }
	}
	
	
	public void getMessageForFg1(int userId, HttpServletRequest request, HttpServletResponse response) throws IOException {
		List<InfoEachMessage> list = new ChatUserDAO().GetLastMessageListEachUser(userId);
		sendLastUserMessageToUser(userId, list, request, response);
	}
	
	

	public void getMessageListWating(int userId, HttpServletRequest request, HttpServletResponse response) throws IOException {
		List<InfoEachMessage> list = new ChatUserDAO().GetListMessageWating(userId);
		if(list != null) System.out.println("size message wating :"+list.size());
		else System.out.println("size message wating :"+0);
		sendLastUserMessageToUser(userId, list, request, response);
		
	}
	
	
	
	public void chatBetweenTwoUsers(int userId, HttpServletRequest request, HttpServletResponse response) throws IOException {
		int user2Id = Integer.parseInt(request.getParameter("user2Id"));
		List<ChatBetweenTwoUsers> list = new ChatUserDAO().getMessageBetweenTwoUser(userId, user2Id);
		JSONArray jsonArray = new JSONArray();
        if(list != null) {
        	for(ChatBetweenTwoUsers x : list) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("messageId", x.getMessageId());
				jsonObject.put("senderId", x.getSenderId());
				jsonObject.put("messageType", x.getMessageType());
				jsonObject.put("content", x.getContent());
                jsonArray.put(jsonObject);
        	}
        }
        response.getWriter().print(jsonArray.toString());
        /// 
        new ChatUserDAO().setSeenAllMessagesOfUser(userId, user2Id);
	}
	
	public void sendLastUserMessageToUser(int userId,List<InfoEachMessage> list,HttpServletRequest request, HttpServletResponse response) throws IOException {
		JSONArray JsonArray = new JSONArray();
		if (list == null) {
			response.getWriter().print(JsonArray.toString());
			return;
		}
		for (InfoEachMessage x : list) {
			JSONObject jsonObject = new JSONObject();
			int chatUserId = x.getSenderId(); // bien chatUserId la id cua user chat voi ta
			if (chatUserId == userId)
				chatUserId = x.getRecipientId();
			// => co duoc id cua dung dung dang chat .

			String imagePath = new UsersDAO().getAvataPathById(chatUserId);
			String base64AvataImage = imageToBase64(imagePath);
			// => co duoc avata

			String userAccountName = new UsersDAO().getAccountNameById(chatUserId);
			// => co duoc ten account nguoi dang chat voi chung ta
			//
			jsonObject.put("chatUserId", chatUserId);
			jsonObject.put("avatar", base64AvataImage);
			jsonObject.put("userAccountName", userAccountName);
			jsonObject.put("lastTimeChat", x.getSentTime());
			jsonObject.put("userIdSendMessage", x.getSenderId());
			jsonObject.put("seeMessage", x.getSeeMessage());

			// con thuc hien lan cuoi. lay tin nhan cuoi cung
			if (x.getTextContent() != null) {
				String content = new String(Base64.getDecoder().decode(x.getTextContent()), StandardCharsets.UTF_8);
				jsonObject.put("lastMessageContent", content);
				jsonObject.put("lastMessageType", "Text");
			} else {
				if (x.getSenderId() == userId)
					jsonObject.put("lastMessageContent", "Bạn đã gửi 1 bức ảnh");
				else
					jsonObject.put("lastMessageContent", "Bạn đã nhận 1 bức ảnh");
				jsonObject.put("lastMessageType", "Image");

			}
			JsonArray.put(jsonObject);
		}
		response.getWriter().print(JsonArray.toString());
	}

	private String imageToBase64(String imagePath) throws IOException {
		File file = new File(imagePath);
		FileInputStream imageInFile = new FileInputStream(file);
		byte[] imageData = new byte[(int) file.length()];
		imageInFile.read(imageData);
		return Base64.getEncoder().encodeToString(imageData);
	}


}
