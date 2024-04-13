package InteractiveUser;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import DAO.FriendRequestDAO;

/**
 * Servlet implementation class RequestFriendToUser
 */
public class SendRequestFriendToUser extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SendRequestFriendToUser() {
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
		int userIdSend = Integer.parseInt(request.getParameter("userIdSend"));
		int userIdReceive = Integer.parseInt(request.getParameter("userIdReceive"));
		// chèn thêm trong bảng 
		boolean result = new FriendRequestDAO().sendFriendRequest(userIdSend, userIdReceive);
		if(result == true) response.setStatus(200);
		else response.setStatus(503);
	}

}
