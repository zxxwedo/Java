package client;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;
import javax.swing.JOptionPane;
import util.MessageType;
import util.XMLUtil;

public class LoginConnection extends Thread {
	private String userName;
	private String serverAddress;
	private int port;
	private Login login;
	private InputStream is;
	private OutputStream os;
	private Chatting chatting;
	private Socket socket;

	public LoginConnection(String userName, String serverAddress, int port,Login login) {
		this.userName = userName;
		this.serverAddress = serverAddress;
		this.port = port;
		this.login = login;
		// 持有 login 的引用, 为的是能够以 login 为 parent 组件, 弹出提示对话框.
		if (this.connect2Server(serverAddress, port))
			this.login();
	}

	// 负责连接到服务器端, 初始化, socket 和 输入输出流. 失败弹出消息对话框.
	private boolean connect2Server(String serverAddress, int port) {
		try {
			this.socket = new Socket(serverAddress, port);
			is = socket.getInputStream();
			os = socket.getOutputStream();
			return true;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(login, "服务器可能还没启动, 请确定服务器正常",
					"无法连接到服务器", JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}

	public String getUserName() {
		return userName;
	}

	public InputStream getIs() {
		return is;
	}

	public OutputStream getOs() {
		return os;
	}

	// 连接服务器成功, 处理服务器发来的链接验证信息. 如果验证通过,隐藏 Login 窗口, 弹出聊天窗口
	private void login() {
		try {
			String info = XMLUtil.constructLoginXML(userName);
			os.write(info.getBytes());
			byte[] buf = new byte[5000];
			int length = is.read(buf);
			String response = new String(buf, 0, length);
			String result = XMLUtil.extractResult(response);
			if ("success".equals(result)) {
				this.login.setVisible(false);
				this.chatting = new Chatting("群聊", this);
				//System.out.println(response);
				this.start();
			} else {
				JOptionPane.showMessageDialog(this.login, "用户名已经存在,请重新 输入",
						"登陆结果", JOptionPane.WARNING_MESSAGE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 发送消息窗口, 负责向服务端发送指定格式的消息.
	public void sendMessage(String message, String type) {
		try {
			int t = Integer.parseInt(type);
			String xml = null;
			if (MessageType.C2SMessage == t) {
				xml = XMLUtil.constructeC2SMessageXML(userName, message);
			}
			if (MessageType.ClientClosing == t) {
				xml = XMLUtil.constructClientClosingXML(message);
			}
			os.write(xml.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 进程 run 方法, 反复执行, 直到退出. 负责接收客户端发来的信息
	public void run() {
		try {
			while (true) {
				byte[] buf = new byte[5000];
				int length = is.read(buf);
				String message = new String(buf, 0, length);
				// 解析出接收到信息的类型
				int messageType = Integer
						.parseInt(XMLUtil.extractType(message));
				
				// 如果是类表信息, 更新 Chatting(聊天窗口)的列表.
				if (MessageType.UserList == messageType) {
					List<String> list = XMLUtil.extractAllUsers(message);
					String users = "";
					for (String user : list) {
						users += user + "\n";
					}
					this.chatting.getList().setText(users);
				}
				// 如果是聊天信息, 则更新 messages 文本区, 显示新消息
				if (MessageType.S2CMessage == messageType) {
					String info = "";
					info += XMLUtil.extractContent(message) + "\n";
					this.chatting.getMessages().setText(
							this.chatting.getMessages().getText() + info);
				}
				// 如果是关闭相应信息, 则关闭输入输出流, socket, 同时终止进程的运行.
				if (MessageType.ClientClosingConfirm == messageType) {
					this.os.close();
					this.is.close();
					this.socket.close();
					break;
				}
				// 如果是进程退出信息, 弹出提示对话框, 退出系统.
				if (MessageType.ServerClosing == messageType) {
					JOptionPane.showMessageDialog(chatting, "服务器关闭, 即将推 出",
							"服务器关闭", JOptionPane.ERROR_MESSAGE);
					System.exit(0);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.exit(0);
	}
}