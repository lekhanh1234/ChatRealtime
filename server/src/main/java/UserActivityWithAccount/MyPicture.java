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

import DAO.PictureDAO;

/**
 * Servlet implementation class MyPicture
 */
public class MyPicture extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MyPicture() {
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
		String content = getContentByClientSend(request.getInputStream());
		JSONObject jsonObject = new JSONObject(content);
		
		int userId = jsonObject.getInt("userId");
		String picture = jsonObject.getString("picture");
		System.out.println("co anh :"+picture.length());
		new PictureDAO().insertPicture(userId, picture);
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
