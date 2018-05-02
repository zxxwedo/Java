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
		// ���� login ������, Ϊ�����ܹ��� login Ϊ parent ���, ������ʾ�Ի���.
		if (this.connect2Server(serverAddress, port))
			this.login();
	}

	// �������ӵ���������, ��ʼ��, socket �� ���������. ʧ�ܵ�����Ϣ�Ի���.
	private boolean connect2Server(String serverAddress, int port) {
		try {
			this.socket = new Socket(serverAddress, port);
			is = socket.getInputStream();
			os = socket.getOutputStream();
			return true;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(login, "���������ܻ�û����, ��ȷ������������",
					"�޷����ӵ�������", JOptionPane.ERROR_MESSAGE);
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

	// ���ӷ������ɹ�, ���������������������֤��Ϣ. �����֤ͨ��,���� Login ����, �������촰��
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
				this.chatting = new Chatting("Ⱥ��", this);
				//System.out.println(response);
				this.start();
			} else {
				JOptionPane.showMessageDialog(this.login, "�û����Ѿ�����,������ ����",
						"��½���", JOptionPane.WARNING_MESSAGE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ������Ϣ����, ���������˷���ָ����ʽ����Ϣ.
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

	// ���� run ����, ����ִ��, ֱ���˳�. ������տͻ��˷�������Ϣ
	public void run() {
		try {
			while (true) {
				byte[] buf = new byte[5000];
				int length = is.read(buf);
				String message = new String(buf, 0, length);
				// ���������յ���Ϣ������
				int messageType = Integer
						.parseInt(XMLUtil.extractType(message));
				
				// ����������Ϣ, ���� Chatting(���촰��)���б�.
				if (MessageType.UserList == messageType) {
					List<String> list = XMLUtil.extractAllUsers(message);
					String users = "";
					for (String user : list) {
						users += user + "\n";
					}
					this.chatting.getList().setText(users);
				}
				// �����������Ϣ, ����� messages �ı���, ��ʾ����Ϣ
				if (MessageType.S2CMessage == messageType) {
					String info = "";
					info += XMLUtil.extractContent(message) + "\n";
					this.chatting.getMessages().setText(
							this.chatting.getMessages().getText() + info);
				}
				// ����ǹر���Ӧ��Ϣ, ��ر����������, socket, ͬʱ��ֹ���̵�����.
				if (MessageType.ClientClosingConfirm == messageType) {
					this.os.close();
					this.is.close();
					this.socket.close();
					break;
				}
				// ����ǽ����˳���Ϣ, ������ʾ�Ի���, �˳�ϵͳ.
				if (MessageType.ServerClosing == messageType) {
					JOptionPane.showMessageDialog(chatting, "�������ر�, ������ ��",
							"�������ر�", JOptionPane.ERROR_MESSAGE);
					System.exit(0);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.exit(0);
	}
}