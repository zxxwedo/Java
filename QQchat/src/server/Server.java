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

	// 构造映射, 用来保存用户名和对于的处理进程(DeallingMessage 进程).
	public Map<String, DeallingMessage> getUsers() {
		return users;
	}

	public Server(String name) {
		super(name);
		this.init();
	}

	// 对窗口进行初始化.
	public void init() {
		Toolkit tool=this.getToolkit(); //得到一个Toolkit对象
		Image myimage=tool.getImage("D:\\eclipse\\workspace\\QQchat\\1.jpg"); //由tool获取图像
		this.setIconImage(myimage);
		label1 = new JLabel();
		state = new JLabel();
		label2 = new JLabel();
		port = new JTextField();
		start = new JButton();
		pInfo = new JPanel();
		pList = new JPanel();
		label1.setText("服务器状态");
		state.setText("停止");
		state.setForeground(Color.RED);
		label2.setText("端口号");
		start.setText("start");
		start.setToolTipText("点击启动服务器");

		// 为start按钮添加监听事件
		start.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Server.this.excute(e);
			}
		}

		);
		
		port.setColumns(8);
		port.setToolTipText("输入端口号");
		port.setText("5050");
		pInfo.setBorder(BorderFactory.createTitledBorder("服务器信息 "));
		pList.setBorder(BorderFactory.createTitledBorder("在线用户"));
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

		// 为本窗口的关闭操作添加监听
		this.addWindowListener(new WindowAdapter() {
			// 当窗口关闭时,向每一个在线客户端发服务器关闭消息.
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

	// 对start点按事件处理的方法
	private void excute(ActionEvent e) {
		int port = Integer.parseInt(Server.this.port.getText());
		// 新建连接处理进程(服务器守护进程)
		new ServerConnection(this, port).start();
	}

	public static void main(String[] args) {
		new Server("Server");
	}
}
