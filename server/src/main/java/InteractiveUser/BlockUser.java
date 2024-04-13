package InteractiveUser;

import java.io.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.json.JSONObject;

import DAO.BlockUserDao;
import WebSocket.ManagementSockets;
import WebSocket.WebSocket;


/**
 * Servlet implementation class blockUser
 */
public class BlockUser extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BlockUser() {
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
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		// TODO Auto-generated method stub
		int userId = Integer.parseInt(request.getParameter("userId"));
		int userIdToBlock = Integer.parseInt(request.getParameter("userIdToBlock"));
		boolean result = new BlockUserDao().BlockUser(userId, userIdToBlock); // false khi đã block trước đó rồi
		if(result == true) {
			response.setStatus(200);
			if(ManagementSockets.sockets.containsKey(userIdToBlock))
			{
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("type",2);
				jsonObject.put("userId", userId);
				try {
					ManagementSockets.sockets.get(userIdToBlock).session.getBasicRemote().sendText(jsonObject.toString());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		else response.setStatus(505);
		
		
		// sử dụng websocket để gửi ngay thông điệp về cho user kia để kiểm tra xem ở trong activity nào. để yêu cầu thoát ra ngay lập tức
	}

}
