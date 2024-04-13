package InteractiveUser;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import DAO.OpenConnectDatabase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

import org.json.JSONObject;

import DAO.BlockUserDao;
import DAO.ChatUserDAO;
import DAO.FriendDAO;
import DAO.UsersDAO;
import WebSocket.ManagementSockets;

/**
 * Servlet implementation class MessageByClient
 */
public class MessageSentByClient extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MessageSentByClient() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		// class nay de thuc hien doc cac tin nhan tu client len. sau do luu vao co so du lieu 
		// dong thoi luu cac thong bao cho user nhan 
		String json = getContentByClientSend(request.getInputStream());
		JSONObject jsonObject = new JSONObject(json);
		int userSendId = (int) jsonObject.get("userSendId");
		int userReceiveId = (int) jsonObject.get("userReceiveId");
		int typeContent = (int) jsonObject.get("typeContent");
		String content = jsonObject.getString("content");
		Connection connection = new OpenConnectDatabase().openConnectDatabase();
		LocalDateTime currentTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
		String formattedTime = currentTime.format(formatter);
		// chung ta mac dinh rang phia client. no da dinh dang thong diep gui len la
		// dang base64;		
		//check xem co phai user bị block hay không. nếu có hủy luôn thông điêp;
		boolean isBlockFriend = new BlockUserDao().checkIsBlockUser(userSendId, userReceiveId);
		if(isBlockFriend == true) {
			response.setStatus(505);
			return;
		}
		
		FriendDAO friendDAO = new FriendDAO();
		boolean checkIsFriend = friendDAO.checkIsFriend(userSendId, userReceiveId);
		int firstMessageState = new ChatUserDAO().checkStateFirstMessage(userSendId, userReceiveId); // kiem tra xem co message nao co state bang 1 khong.
		boolean exitsMessageReceiveId = new ChatUserDAO().exitsMessageReceiveId(userSendId,userReceiveId);
		
		String statementInsert = null;
		if (checkIsFriend == true || firstMessageState == 1) {
			System.out.println("la ban be hoac 2 ben da gui tin nhan cho nhau");
			if (typeContent == 1)
				statementInsert = "insert into chatUser values(" + userSendId + "," + userReceiveId + ",1,null,'"
						+ content + "',1,'" + formattedTime + "',0)";
			if (typeContent == 0)
				statementInsert = "insert into chatUser values(" + userSendId + "," + userReceiveId + ",1,'" + content
						+ "',null,0,'" + formattedTime + "',0)";
			if (typeContent == 2)
				statementInsert = "insert into chatUser values(" + userSendId + "," + userReceiveId + ",1,'" + content
						+ "',null,2,'" + formattedTime + "',0)";
		} else {
			System.out.println("khong phai ban be đồng thời chỉ a ->gửi b || chỉ b -> gửi a || chưa gửi tin nhắn");
			 // neu gia tri tra ve la -1 nghia la truoc do chua co bất kì tin nhan nao. 
			// neu la 1 thi truoc do 2 ben đều đã gửi tin nhắn cho nhau
			// neu la 0 thi chi có đúng 1 user từng gửi tin nhắn đi 
			if (firstMessageState == -1 ||(firstMessageState == 0 && exitsMessageReceiveId == false)) { // chua gui tin nhan cho nhau || chỉ a -> b  
				if (typeContent == 1) {
					statementInsert = "insert into chatUser values(" + userSendId + "," + userReceiveId + ",0,null,'"
							+ content + "',1,'" + formattedTime + "',0)";
				}
				if (typeContent == 0) {
					statementInsert = "insert into chatUser values(" + userSendId + "," + userReceiveId + ",0,'"
							+ content + "',null,0,'" + formattedTime + "',0)";
				}
				if (typeContent == 2) {
					statementInsert = "insert into chatUser values(" + userSendId + "," + userReceiveId + ",0,'"
							+ content + "',null,2,'" + formattedTime + "',0)";
				}
				System.out.println(" states k thay doi");
				System.out.println(" s :"+firstMessageState);
				System.out.println(" s :"+firstMessageState);
				System.out.println(" s :"+exitsMessageReceiveId);

			}
			else{
					if (typeContent == 1) {
						statementInsert = "insert into chatUser values(" + userSendId + "," + userReceiveId + ",1,null,'"
								+ content + "',1,'" + formattedTime + "',0)";
					}
					if (typeContent == 0) {
						statementInsert = "insert into chatUser values(" + userSendId + "," + userReceiveId + ",1,'"
								+ content + "',null,0,'" + formattedTime + "',0)";
					}
					if (typeContent == 2) {
						statementInsert = "insert into chatUser values(" + userSendId + "," + userReceiveId + ",1,'"
								+ content + "',null,2,'" + formattedTime + "',0)";
					}
					// sua lai toan bo tin nhan cua 2 nguoi va doi gia tri truong states = 1;
					new ChatUserDAO().changeStatesChat(userSendId,userReceiveId);
					System.out.println(" set all states = 1");
			}
		}
		System.out.println("tien hanh chen vao co so du lieu");
		ChatUserDAO chatUser = new ChatUserDAO();
		chatUser.insertMessage(statementInsert);
		response.setStatus(200);
		int idMessage = new ChatUserDAO().getLastMessageIdBySender(userSendId);
		// gui thong diep do ve ngay cho client 
		if(ManagementSockets.sockets.containsKey(userReceiveId)) {  // nghia la client da da dang nhap // tuy nhien co the da ngat internet
			JSONObject jsObject = new JSONObject();
			String pathAvatar = new UsersDAO().getAvataPathById(userSendId);
			String accountName = new UsersDAO().getAccountNameById(userSendId);

			jsObject.put("type",1); // type 1 nghia la thong diep tin nhan moi
			///////////////////////////////////////////////////////////////////////////////////////////
			jsObject.put("messageId",idMessage);  // chung
			jsObject.put("sendUserId", userSendId);
			if (typeContent == 0) {
				jsObject.put("messageType", "Image");
			} else if(typeContent == 2) {
				jsObject.put("messageType", "LikeImage");
			}else 	jsObject.put("messageType", "Text");
			
			if(typeContent == 1) {
				 // nếu nôi dung là dạng text, chuyển về base64 và gửi về client
				 jsObject.put("content",new String(Base64.getDecoder().decode(content),StandardCharsets.UTF_8)); // gui ve cho userChat
			}
			else jsObject.put("content",content);  // ngược lại không cần vì dữ liệu gửi lên đã là base64
			////////////////////////////////////////////////////////////////////////////////////////////
			jsObject.put("isFriend", checkIsFriend);
			jsObject.put("accountName", accountName);
			jsObject.put("avatarInBase64", imageToBase64(pathAvatar));
			//, nameAccount người gửi, và avatar. trong tình huống đó là bạn bè và chưa có trong list danh sách
			if(typeContent == 1) {
			  String decoderContent = new String(Base64.getDecoder().decode(content),StandardCharsets.UTF_8);
			  jsObject.put("contentToFg1",decoderContent);
			}else 	jsObject.put("contentToFg1","Bạn đã nhận 1 bức ảnh");	
			jsObject.put("sendingTime", formattedTime);
			try {
			ManagementSockets.sockets.get(userReceiveId).session.getBasicRemote().sendText(jsObject.toString());
			}catch(Exception e) {}
		}
		response.getWriter().print(idMessage+"");
		
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
	
	private String imageToBase64(String imagePath) throws IOException {
		File file = new File(imagePath);
		FileInputStream imageInFile = new FileInputStream(file);
		byte[] imageData = new byte[(int) file.length()];
		imageInFile.read(imageData);
		return Base64.getEncoder().encodeToString(imageData);
	}

}
