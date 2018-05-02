package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JOptionPane;
import util.XMLUtil;

public class ServerConnection extends Thread {
	private Server server;
	private ServerSocket serverSocket;
	private Socket socket;

	public ServerConnection(Server server, int port) {
		this.server = server;
		try { // 创建serverSocket, 如果成功,修改Server的状态标签为”运行”, 如果失败, 弹出错误 信息对话框
			this.serverSocket = new ServerSocket(port);
			server.getCurState().setText("运行");
			server.getStart().setEnabled(false);
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this.server, "端口被占用", "错 误",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	// 判断用户名是否已经存在
	private boolean isUserExist(String userName) {
		if (this.server.getUsers().containsKey(userName))
			return true;
		else
			return false;
	}

	// @Override
	public void run() {
		while (true) {
			try { // 接受连接
				this.socket = serverSocket.accept();
				InputStream is = socket.getInputStream();
				OutputStream os = socket.getOutputStream();
				byte[] buf = new byte[5000];
				int length = is.read(buf);

				// 客户端发来的连接信息(包括了用户名)
				String loginXML = new String(buf, 0, length);
				// 从客户端登陆数据中提取出用户信息(userName)
				String userName = XMLUtil.extractUsername(loginXML);
				if (this.isUserExist(userName)) // 如果存在, 返回失败信息.
					os.write(XMLUtil.constructResponse("fail").getBytes());
				else {
					// 不存在返回成功信息
					os.write(XMLUtil.constructResponse("success").getBytes());
					// 新建信息处理进程
					DeallingMessage deallingMessage = new DeallingMessage(
							this.server, socket);
					this.server.getUsers().put(userName, deallingMessage);
					// 更新用户列表(服务器+)
					deallingMessage.updateUserList();
					deallingMessage.start();
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
