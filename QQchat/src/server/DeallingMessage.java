package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Collection;
import java.util.Set;
import util.MessageType;
import util.XMLUtil;

public class DeallingMessage extends Thread {
	private Server server;
	private InputStream is;
	private OutputStream os;
	private Socket socket;

	public DeallingMessage(Server server, Socket socket) {
		this.server = server;
		this.socket = socket;
		try {
			this.is = socket.getInputStream();
			this.os = socket.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void updateUserList() {
		Set<String> allUsers = this.server.getUsers().keySet();
		String str = "";
		for (String user : allUsers) {
			str += user + "\n";
		}
		// 更新服务器端用户列表
		this.server.getTlist().setText(str);
		Collection<DeallingMessage> allClients = this.server.getUsers()
				.values();
		for (DeallingMessage deallingMessage : allClients) {
			deallingMessage.sendMessage(XMLUtil.constructUserList(this.server
					.getUsers().keySet()));
		}

	} // 向客户端发送数据

	public void sendMessage(String message) {
		try {
			os.write(message.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 接收客户端发来的数据 
		@Override
	public void run() {
		try {
			while (true) {
				byte[] buf = new byte[5000];
				int length = is.read(buf);
				String info = new String(buf, 0, length);
				System.out.println(info);
				String userName = XMLUtil.extractUsername(info);
				int type = Integer.parseInt(XMLUtil.extractType(info));
				// 判断接受到消息的类型
				// 当消息是从客户端发来的聊天信息时, 将发送的用户名和消息内容进行组合,然后转发给每一个用户.
				if (MessageType.C2SMessage == type) {
					String content = XMLUtil.extractContent(info);
					String S2CMessage = XMLUtil.constructS2CMessage(userName
							+ ": " + content);
					Collection<DeallingMessage> collection = this.server
							.getUsers().values();
					for (DeallingMessage dM : collection) {
						dM.sendMessage(S2CMessage);
					}
				}
				// 当消息是客户端关闭的信息时,在列表中删除该客户端对应项
				// 关闭当前进程的输入输出流,同时更新列表
				if (MessageType.ClientClosing == type) {
					DeallingMessage delm = this.server.getUsers().get(userName);
					delm.sendMessage(XMLUtil.constructClientClosingConfirm());
					this.server.getUsers().remove(userName);
					this.is.close();
					this.os.close();
					this.socket.close();
					this.updateUserList();
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}