package UserActivityWithAccount;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONObject;

import DAO.UsersDAO;

/**
 * Servlet implementation class ChangePassword
 */
public class ChangePassword extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ChangePassword() {
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
		
		String valueFromClient = getContentByClientSend(request.getInputStream());
		JSONObject jsonObject = new JSONObject(valueFromClient);
		String password = jsonObject.getString("password");
		String newPassword = jsonObject.getString("newPassword");
		int userId = jsonObject.getInt("userId");
		// đầu tiên kiểm tra xem password có đúng hay chưa
		boolean checkPassword = new UsersDAO().checkPasswordByUserId(userId,password);
		if(checkPassword == false) {
			response.getWriter().print(-1+""); // mật khẩu không khớp
			return;
		}
		
		String userNameLogin = new UsersDAO().getUserNameLoginById(userId);
		int checkExistUser = new UsersDAO().getIdbyUserNameAndPassword(userNameLogin, newPassword);
		if(checkExistUser != -1) {
			// đã tồn tai user 
		    response.getWriter().print(0+"");
		    return;
		}
		// tiến hành thay đổi mật khẩu
		new UsersDAO().changePasswordById(userId, newPassword);
	    response.getWriter().print(1+"");
		
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
