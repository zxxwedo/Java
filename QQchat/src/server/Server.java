package server;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import util.XMLUtil;

public class Server extends JFrame {
	private static final long serialVersionUID = 1L;
	private JLabel label1, state, label2;
	private JTextField port;
	private JButton start;
	private JPanel pInfo, pList;
	private JTextArea tlist;
	private Map<String, DeallingMessage> users = new HashMap<String, DeallingMessage>();

	// ����ӳ��, ���������û����Ͷ��ڵĴ������(DeallingMessage ����).
	public Map<String, DeallingMessage> getUsers() {
		return users;
	}

	public Server(String name) {
		super(name);
		this.init();
	}

	// �Դ��ڽ��г�ʼ��.
	public void init() {
		Toolkit tool=this.getToolkit(); //�õ�һ��Toolkit����
		Image myimage=tool.getImage("D:\\eclipse\\workspace\\QQchat\\1.jpg"); //��tool��ȡͼ��
		this.setIconImage(myimage);
		label1 = new JLabel();
		state = new JLabel();
		label2 = new JLabel();
		port = new JTextField();
		start = new JButton();
		pInfo = new JPanel();
		pList = new JPanel();
		label1.setText("������״̬");
		state.setText("ֹͣ");
		state.setForeground(Color.RED);
		label2.setText("�˿ں�");
		start.setText("start");
		start.setToolTipText("�������������");

		// Ϊstart��ť��Ӽ����¼�
		start.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Server.this.excute(e);
			}
		}

		);
		
		port.setColumns(8);
		port.setToolTipText("����˿ں�");
		port.setText("5050");
		pInfo.setBorder(BorderFactory.createTitledBorder("��������Ϣ "));
		pList.setBorder(BorderFactory.createTitledBorder("�����û�"));
		pInfo.add(label1);
		pInfo.add(state);
		pInfo.add(label2);
		pInfo.add(port);
		pInfo.add(start);
		tlist = new JTextArea();
		tlist.setEditable(false);
		tlist.setForeground(Color.BLUE);
		tlist.setRows(15);
		tlist.setColumns(30);

		JScrollPane jScrollpane = new JScrollPane();
		jScrollpane.setViewportView(tlist);
		pList.add(jScrollpane);
		this.getContentPane().add(pInfo, BorderLayout.NORTH);
		this.getContentPane().add(pList, BorderLayout.SOUTH);

		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.pack();
		this.setAlwaysOnTop(true);
		this.setResizable(false);
		this.setVisible(true);

		// Ϊ�����ڵĹرղ�����Ӽ���
		this.addWindowListener(new WindowAdapter() {
			// �����ڹر�ʱ,��ÿһ�����߿ͻ��˷��������ر���Ϣ.
			@Override
			public void windowClosing(WindowEvent e) {
				try {
					Collection<DeallingMessage> collection = Server.this.users
							.values();
					for (DeallingMessage dM : collection) {
						dM.sendMessage(XMLUtil.constructServerClosingXML());
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				} finally {
					System.exit(0);
				}
			}
		});
	}

	public JLabel getCurState() {
		return state;
	}

	public JButton getStart() {
		return start;
	}

	public JTextArea getTlist() {
		return tlist;
	}

	// ��start�㰴�¼�����ķ���
	private void excute(ActionEvent e) {
		int port = Integer.parseInt(Server.this.port.getText());
		// �½����Ӵ������(�������ػ�����)
		new ServerConnection(this, port).start();
	}

	public static void main(String[] args) {
		new Server("Server");
	}
}
