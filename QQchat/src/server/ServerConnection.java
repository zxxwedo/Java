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
		try { // ����serverSocket, ����ɹ�,�޸�Server��״̬��ǩΪ�����С�, ���ʧ��, �������� ��Ϣ�Ի���
			this.serverSocket = new ServerSocket(port);
			server.getCurState().setText("����");
			server.getStart().setEnabled(false);
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this.server, "�˿ڱ�ռ��", "�� ��",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	// �ж��û����Ƿ��Ѿ�����
	private boolean isUserExist(String userName) {
		if (this.server.getUsers().containsKey(userName))
			return true;
		else
			return false;
	}

	// @Override
	public void run() {
		while (true) {
			try { // ��������
				this.socket = serverSocket.accept();
				InputStream is = socket.getInputStream();
				OutputStream os = socket.getOutputStream();
				byte[] buf = new byte[5000];
				int length = is.read(buf);

				// �ͻ��˷�����������Ϣ(�������û���)
				String loginXML = new String(buf, 0, length);
				// �ӿͻ��˵�½��������ȡ���û���Ϣ(userName)
				String userName = XMLUtil.extractUsername(loginXML);
				if (this.isUserExist(userName)) // �������, ����ʧ����Ϣ.
					os.write(XMLUtil.constructResponse("fail").getBytes());
				else {
					// �����ڷ��سɹ���Ϣ
					os.write(XMLUtil.constructResponse("success").getBytes());
					// �½���Ϣ�������
					DeallingMessage deallingMessage = new DeallingMessage(
							this.server, socket);
					this.server.getUsers().put(userName, deallingMessage);
					// �����û��б�(������+)
					deallingMessage.updateUserList();
					deallingMessage.start();
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
