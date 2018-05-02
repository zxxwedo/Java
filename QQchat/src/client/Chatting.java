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

	// ���г�ʼ������.
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
		send.setToolTipText("�س�����");
		clrs.setText("Clear");
		clrs.setToolTipText("F5����");
		
		Toolkit tool=this.getToolkit(); //�õ�һ��Toolkit����
		Image myimage=tool.getImage("D:\\eclipse\\workspace\\QQchat\\1.jpg"); //��tool��ȡͼ��
		this.setIconImage(myimage);
		
		// �����Ͱ�ťע������¼�, ����ť���¾͵��� sendMessage()������Ϣ.
		send.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Chatting.this.sendMessage(e);
			}
		});
		
		// Ϊ������ťע������¼�.
		clrs.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Chatting.this.clrScreen(e);
			}
		});
		
		// Ϊ������ע������¼�
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				try {
					// �����ڹر�ʱ, ��������˷��͹ر���Ϣ. �������˸�����Ϣ������Ӧ�Ĳ���.
					Chatting.this.loginConnection.sendMessage(
							Chatting.this.loginConnection.getUserName(), "6");
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		// �����Ͱ�ť��Ӱ�������, �Ա��û����»س�ʱ, ����ֱ�ӷ�����Ϣ.
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
		jSP2.setBorder(BorderFactory.createTitledBorder("�����û��б�"));
		panel1.setBorder(BorderFactory.createTitledBorder("������Ϣ"));
		this.getContentPane().add(panel1, BorderLayout.WEST);
		this.getContentPane().add(jSP2, BorderLayout.EAST);
		this.pack();
		this.setVisible(true);
		this.setResizable(false);
	}

	// ������Ϣ: ��� message �ı��������, Ȼ���ı������, Ȼ����� loginConnection �� sendMessage����, ������Ϣ.
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
