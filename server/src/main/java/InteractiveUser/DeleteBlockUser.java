package InteractiveUser;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import DAO.BlockUserDao;

/**
 * Servlet implementation class DeleteBlockUser
 */
public class DeleteBlockUser extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteBlockUser() {
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
		int userBlocked = Integer.parseInt(request.getParameter("userBlocked")); 
		// kết quả trả về là 200 nghĩa là xóa block thành công. ngược lại 503 là k thể xóa vì chưa đủ 48 h
		// tiến hành xóa user bị block
		boolean result = new BlockUserDao().deleteBlockUser(userId, userBlocked); // -->>>
		if(result == true) response.setStatus(200);
		else response.setStatus(503);
	}

}
