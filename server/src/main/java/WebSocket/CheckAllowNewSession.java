package WebSocket;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet implementation class CheckAllowNewSession
 */
public class CheckAllowNewSession extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CheckAllowNewSession() {
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
		// TODO Auto-generated method stub
		int userId = Integer.parseInt(request.getParameter("userId"));
		String clientCode = request.getParameter("clientCode");
		System.out.println("client code la :"+clientCode);
		if (ManagementSockets.sockets.containsKey(userId) == false) {
			response.setStatus(501);
			System.out.println("111");

		} else {
			if (ManagementSockets.sockets.get(userId).clientCodeAtServer.equals(clientCode)) {
				ManagementSockets.sockets.remove(userId);
				response.setStatus(200);
				System.out.println("200");

			} else {
				response.setStatus(501);
				System.out.println("232323");
			}
		}
	}

}
