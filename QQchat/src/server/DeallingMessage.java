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
		// ���·��������û��б�
		this.server.getTlist().setText(str);
		Collection<DeallingMessage> allClients = this.server.getUsers()
				.values();
		for (DeallingMessage deallingMessage : allClients) {
			deallingMessage.sendMessage(XMLUtil.constructUserList(this.server
					.getUsers().keySet()));
		}

	} // ��ͻ��˷�������

	public void sendMessage(String message) {
		try {
			os.write(message.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ���տͻ��˷��������� 
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
				// �жϽ��ܵ���Ϣ������
				// ����Ϣ�Ǵӿͻ��˷�����������Ϣʱ, �����͵��û�������Ϣ���ݽ������,Ȼ��ת����ÿһ���û�.
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
				// ����Ϣ�ǿͻ��˹رյ���Ϣʱ,���б���ɾ���ÿͻ��˶�Ӧ��
				// �رյ�ǰ���̵����������,ͬʱ�����б�
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