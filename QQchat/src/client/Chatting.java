package client;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

@SuppressWarnings("serial")

public class Chatting extends JFrame {
	private JTextArea messages = new JTextArea();
	private JTextArea list = new JTextArea();
	private JTextField message = new JTextField();
	private JButton send = new JButton();
	private JButton clrs = new JButton();
	private JScrollPane jSP1 = new JScrollPane();
	private JScrollPane jSP2 = new JScrollPane();
	private JPanel panel1 = new JPanel();
	private JPanel paneBtn = new JPanel();
	private LoginConnection loginConnection;

	public Chatting(String name, LoginConnection loginConnection) {
		super(name);
		this.loginConnection = loginConnection;
		this.init();
	}

	// 进行初始化操作.
	private void init() {
		messages.setColumns(30);
		messages.setRows(25);
		messages.setEditable(false);
		messages.setWrapStyleWord(true);
		list.setColumns(10);
		list.setRows(20);
		list.setEditable(false);
		message.setColumns(15);
		send.setText("Enter");
		send.setToolTipText("回车发送");
		clrs.setText("Clear");
		clrs.setToolTipText("F5清屏");
		
		Toolkit tool=this.getToolkit(); //得到一个Toolkit对象
		Image myimage=tool.getImage("D:\\eclipse\\workspace\\QQchat\\1.jpg"); //由tool获取图像
		this.setIconImage(myimage);
		
		// 给发送按钮注册监听事件, 当按钮按下就调用 sendMessage()发送消息.
		send.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Chatting.this.sendMessage(e);
			}
		});
		
		// 为清屏按钮注册监听事件.
		clrs.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Chatting.this.clrScreen(e);
			}
		});
		
		// 为本窗口注册监听事件
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				try {
					// 当窗口关闭时, 向服务器端发送关闭消息. 服务器端根据消息经行相应的操作.
					Chatting.this.loginConnection.sendMessage(
							Chatting.this.loginConnection.getUserName(), "6");
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		// 给发送按钮添加按键监听, 以便用户按下回车时, 可以直接发送消息.
		this.message.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (KeyEvent.VK_ENTER == e.getKeyCode()) {
					Chatting.this.sendMessage2(e);
				}
			}
		});
		this.message.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (KeyEvent.VK_F5 == e.getKeyCode()) {
					Chatting.this.clrScreen2(e);
				}
			}
		});
		
		jSP1.setViewportView(messages);
		jSP2.setViewportView(list);

		panel1.setLayout(new BorderLayout());
		paneBtn.add(message);
		paneBtn.add(send);
		paneBtn.add(clrs);
		panel1.add(jSP1, BorderLayout.NORTH);
		panel1.add(paneBtn, BorderLayout.SOUTH);
		jSP2.setBorder(BorderFactory.createTitledBorder("在线用户列表"));
		panel1.setBorder(BorderFactory.createTitledBorder("聊天消息"));
		this.getContentPane().add(panel1, BorderLayout.WEST);
		this.getContentPane().add(jSP2, BorderLayout.EAST);
		this.pack();
		this.setVisible(true);
		this.setResizable(false);
	}

	// 发送消息: 获得 message 文本框的内容, 然后将文本框清空, 然后调用 loginConnection 的 sendMessage方法, 发送消息.
	private void sendMessage(ActionEvent e) {
		String strMessage = message.getText();
		this.message.setText(null);
		this.loginConnection.sendMessage(strMessage, "2");
	}
	private void sendMessage2(KeyEvent e) {
		String strMessage = message.getText();
		this.message.setText(null);
		this.loginConnection.sendMessage(strMessage, "2");
	}

	private void clrScreen(ActionEvent e) {
		messages.setText(null);
	}
	private void clrScreen2(KeyEvent e) {
		messages.setText(null);
	}

	public JTextArea getMessages() {
		return messages;
	}

	public JTextArea getList() {
		return list;
	}

	public JTextField getMessage() {
		return message;
	}
}
