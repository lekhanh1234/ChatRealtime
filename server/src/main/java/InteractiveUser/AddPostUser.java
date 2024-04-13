package InteractiveUser;

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
import java.util.Base64;

import org.json.JSONArray;
import org.json.JSONObject;

import DAO.PostsDAO;

/**
 * Servlet implementation class PostUser
 */
public class AddPostUser extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AddPostUser() {
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

		StringBuilder builder = new StringBuilder();
		String s;
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(request.getInputStream()));
		while ((s = bufferedReader.readLine()) != null) {
			builder.append(s).append("\n");
		}
		String content = builder.toString();
		if (content.length() != 0)
			content = content.substring(0, content.length() - 1);

		JSONObject jsonObject = new JSONObject(content);
		int userId = jsonObject.getInt("userId");
		String fileInBase64 = jsonObject.getString("fileInBase64");
		byte[] contentFile = Base64.getDecoder().decode(fileInBase64);
		int type = jsonObject.getInt("type");
		String path = null;
		// lưu vào file
		if (type == 2) {
			path = "C:\\AllMessengerProjectFile\\Post\\" + userId
					+ "video2.mp4";
	
		} else if(type == 1) {
			for(int i = 1;i<=10;i++) {
				String fileName = userId+"video1index" +i+ ".mp4";
				path = "C:\\AllMessengerProjectFile\\Post\\" +fileName;
				if(new File(path).exists() == false) break;
			}
		}
		File file = new File(path);
		if (file.exists())
			file.delete();
		file.createNewFile();
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(contentFile);
		fos.close();
	}

}
