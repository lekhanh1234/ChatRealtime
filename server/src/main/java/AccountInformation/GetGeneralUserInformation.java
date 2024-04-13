package AccountInformation;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static org.bytedeco.opencv.global.opencv_core.CV_8UC1;
import static org.bytedeco.opencv.global.opencv_imgcodecs.imwrite;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Base64;
import java.util.List;

import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_videoio.VideoCapture;
import org.json.JSONArray;
import org.json.JSONObject;

import DAO.BlockUserDao;
import DAO.FriendDAO;
import DAO.PictureDAO;
import DAO.PostsDAO;
import DAO.UsersDAO;
import DAO.FriendRequestDAO;
import DAO.OpenConnectDatabase;
import Models.GeneralUserInfomation;
import Models.UserPicture;

/**
 * Servlet implementation class GetGeneralUserInformation
 */
public class GetGeneralUserInformation extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GetGeneralUserInformation() {
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
		int userId = Integer.parseInt(request.getParameter("userId"));
		int type = Integer.parseInt(request.getParameter("type"));
		switch (type) {	
		case 1:
			getMyPostByPostId(userId,request,response);
			break;
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		int userId = Integer.parseInt(request.getParameter("userId"));
		int type = Integer.parseInt(request.getParameter("type"));
		System.out.println("id and type :"+userId+" :"+type);

		switch (type) {
		case 1:
			getAvatar(userId, request, response);
			break;
		case 2:
			getCoverImage(userId, request, response);
			break;
		case 3:
			getIntroduction(userId, request, response);
			break;
		case 4:
			getUserIsBlocked(userId, request, response);
			break;
		case 5:
			getShowStates(userId, request, response);
			break;
		case 6:
			getUsersInteractive(userId, request, response);
			break;
		case 7:
			getTopUserPicture(userId, request, response);
			break;
		case 8:
			getPrivacyInfo(userId, request, response);
			break;
		case 9:
			getAccountName(userId, request, response);
			break;
		case 10:
			getUserProfilePost(userId,request,response);
			break;
		case 11:
			getAllUserPicture(userId,request,response);
			break;
		}

	}

	public void getAvatar(int userId, HttpServletRequest request, HttpServletResponse response) throws IOException {
		UsersDAO dao = new UsersDAO();
		String imagePath = dao.getAvataPathById(userId);
		String base64AvataImage = imageToBase64(imagePath);
		response.getWriter().print(base64AvataImage);
	}

	public void getCoverImage(int userId, HttpServletRequest request, HttpServletResponse response) throws IOException {
		String imagePath = new UsersDAO().getCoverImagePathById(userId);
		response.getWriter().print(imageToBase64(imagePath));
	}

	public void getIntroduction(int userId, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String introduce = new UsersDAO().getIntroduceById(userId);
		if(introduce == null) introduce = "";
		response.getWriter().print(introduce);
	}
	
	
	public void getUserIsBlocked(int userId, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		List<GeneralUserInfomation> list = new BlockUserDao().getAllUserBlock(userId);
		JSONArray jsonArray = new JSONArray();
		if (list != null) {
			for (GeneralUserInfomation x : list) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("userId", x.getIdUser());
				jsonObject.put("accountName", x.getAccountName());
				jsonObject.put("avatarInBase64", x.getAvatar());
				jsonArray.put(jsonObject);
			}
		}
		response.getWriter().print(jsonArray.toString());
	}


	public void getShowStates(int userId, HttpServletRequest request, HttpServletResponse response) throws IOException {
		int states = new UsersDAO().getShowStatesById(userId);
		response.getWriter().print("" + states);
	}

	public void getUsersInteractive(int userId, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		int result = new UsersDAO().getStateInteractiveById(userId);
		response.getWriter().print(result + "");
	}

	public void getTopUserPicture(int userId, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		int amount = Integer.parseInt(request.getParameter("amount"));
		System.out.println("so luong la :"+amount);
		List<UserPicture> result = new PictureDAO().getAllPictureById(userId);
		JSONArray jsonArray = new JSONArray();
		if (result != null) {
			for (int i = 0; i < result.size() && i < amount; i++) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("pictureInBase64Format", result.get(i).getPicture());
				jsonArray.put(jsonObject);
			}
		}
		response.getWriter().print(jsonArray.toString());
	}

	
	public void getPrivacyInfo(int userId, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		UsersDAO dao = new UsersDAO();
		String userName = dao.getUserName(userId);
		int sex = dao.getSex(userId); // nếu trả về client 0 -> có thể hiểu là k co value, vi 1 nam -- 2 nu
		String birthday = dao.getBirthDay(userId);
		if (birthday == null)
			birthday = ""; // nếu trả về client "" - > có thể hiểu là k có value
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("userName", userName);
		jsonObject.put("sex", sex);
		jsonObject.put("birthday", birthday);
		response.getWriter().print(jsonObject.toString());
	}
	
	public void getAccountName(int userId, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String accountName = new UsersDAO().getAccountNameById(userId);
		response.getWriter().print(accountName);
	}
	
	public void getUserProfilePost(int userId,HttpServletRequest request,HttpServletResponse response)throws IOException {
		JSONArray jsonArray = new JSONArray();		
		for(int i = 1;i<=10;i++) {
			String fileName = userId+"video1index" +i+ ".mp4";
			String path = "C:\\AllMessengerProjectFile\\Post\\" +fileName;
			File file = new File(path);
			if(file.exists()) {
				byte image[] = splitFirstFramProfileVideo(path,userId);
				String postInBase64 = Base64.getEncoder().encodeToString(image);
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("path", path);
				jsonObject.put("postInBase64", postInBase64);
				jsonArray.put(jsonObject);
			}
		}
		if(jsonArray.length() > 0) response.getWriter().print(jsonArray.toString());
	}
	
	public void getMyPostByPostId(int userId, HttpServletRequest request, HttpServletResponse response) throws IOException {
		int postId = Integer.parseInt(request.getParameter("postId"));
		response.setContentType("video/mp4");
		String contentInBase64 = new PostsDAO().getMyPostContentByPostId(postId);
		if(contentInBase64 == null) return;
		byte[] content = Base64.getDecoder().decode(contentInBase64);
		OutputStream out = response.getOutputStream();
		out.write(content);
	}		
	

	public void getAllUserPicture(int userId, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		List<UserPicture> result = new PictureDAO().getAllPictureById(userId);
	//	System.out.print("size la :"+result.size());
		JSONArray jsonArray = new JSONArray();
		if (result != null) {
			for (int i = 0; i < result.size(); i++) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("userId", result.get(i).getUserId());
				jsonObject.put("pictureId", result.get(i).getPictureId());
				jsonObject.put("pictureInBase64Format", result.get(i).getPicture());
				jsonArray.put(jsonObject);
			}
		}
		response.getWriter().print(jsonArray.toString());
	}
	
	private String imageToBase64(String imagePath) throws IOException {
		System.out.println("duong dan file :"+imagePath);
		File file = new File(imagePath);
		FileInputStream imageInFile = new FileInputStream(file);
		byte[] imageData = new byte[(int) file.length()];
		imageInFile.read(imageData);
		return Base64.getEncoder().encodeToString(imageData);
	}

	private byte[] splitFirstFramProfileVideo(String path, int userId) throws IOException {
		int height = 240;
		int width = 320;
		Mat frame = new Mat(height, width, CV_8UC1);
		VideoCapture capture = new VideoCapture(path);
		if (capture.isOpened()) {
			capture.read(frame);
		}
		if (frame != null) { //
			imwrite("E:\\saveProfileFileMP4" + userId + ".jpg", frame);
		}
		File file = new File("E:\\saveProfileFileMP4" + userId + ".jpg");
		FileInputStream fis = new FileInputStream(file);
		byte read[] = new byte[fis.available()];
		fis.read(read);
		file.delete();
		return read;
	}

}
