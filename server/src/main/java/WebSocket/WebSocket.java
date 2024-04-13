package WebSocket;

import java.io.IOException;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

@ServerEndpoint("/websocket")
public class WebSocket {
	public Session session;
	public String clientCodeAtServer;

	@OnOpen
	public void onOpen(Session session) {
		this.session = session;
	}

	@OnClose
	public void onClose(Session session) {
		// Code xử lý khi một kết nối đóng //
	}

	@OnMessage
	public void onMessage(String message, Session session) {
		if (message.contains("T0485845DIGS8D7GMD8XKGM7DEMGUFKG")) { // thành lập kết nối
			int clientId = Integer.parseInt(message.split("-")[1]);
			System.out.println("userId luc them : " + clientId);
			try {
				if (ManagementSockets.sockets.get(clientId) != null) {
					System.out.println("da co client ket noi. => thoat client do");
					ManagementSockets.sockets.get(clientId).session.getBasicRemote()
							.sendText("8JNGM7DEMGUFKG9SKD7N8DN8D8D");
				}
			} catch (Exception e) {
				System.out.println("xay ra ngoai le gui message ve client = websocket" );
			}
			System.out.println("abcdef : " + clientId);

			ManagementSockets.sockets.put(clientId, this);

			// tao mot chuoi ngau nhien tra ve cho client
			String random = clientId + "SV" + ((int) (Math.random() * 100000000)) + "SV"
					+ ((int) (Math.random() * 100000000));
			clientCodeAtServer = random;
			System.out.println("ma client server tạo ra:" + clientCodeAtServer);
			try {
				session.getBasicRemote().sendText("8JUFKG9SKD8D8NGM7DE7N8DNMGD-" + random);
			} catch (IOException e) {
				System.out.println("xay ra ngoai le 2" );
			}
		}
	}

	@OnError
	public void onError(Throwable error) {
		// Code xử lý khi xảy ra lỗi
	}
}
