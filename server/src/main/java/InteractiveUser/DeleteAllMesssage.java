package InteractiveUser;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.json.JSONObject;

import DAO.ChatUserDAO;
import WebSocket.ManagementSockets;
import WebSocket.WebSocket;

/**
 * Servlet implementation class DeleteAllMesssage
 */
public class DeleteAllMesssage extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteAllMesssage() {
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
		doGet(request, response);
	}
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		int userId = Integer.parseInt(req.getParameter("userId"));
		int userIdToDelete = Integer.parseInt(req.getParameter("userIdToDelete"));
		new ChatUserDAO().deleteAllMessageWithUser(userId, userIdToDelete);
		// gửi 1 thông điệp về cho client còn lại
		WebSocket webSocket = (WebSocket) ManagementSockets.sockets.get(userIdToDelete);
		if(webSocket != null) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("type",3); // xóa toàn bộ message
			jsonObject.put("userId",userId);
			try {
				webSocket.session.getBasicRemote().sendText(jsonObject.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
