package client;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

public class Login extends JFrame {
	/** * ��½���� */
	private static final long serialVersionUID = 1L;
	private JLabel jLabel1, jLabel2, jLabel3;
	private JTextField userName, serverAddress, port;
	private JButton jLogin, jReset;
	private JPanel panel;
	// ��ʼ�������
	private void init() {
		jLabel1 = new JLabel();
		jLabel2 = new JLabel();
		jLabel3 = new JLabel();
		jLabel1.setText("�û���");
		jLabel2.setText("������");
		jLabel3.setText("�˿ں�");
		userName = new JTextField();
		serverAddress = new JTextField();
		port = new JTextField();
		userName.setColumns(20);
		serverAddress.setColumns(20);
		port.setColumns(20);
		userName.setText("admin");
		serverAddress.setText("127.0.0.1");
		port.setText("5050");
		jLogin = new JButton();
		jReset = new JButton();
		jLogin.setText("��½");
		jReset.setText("����");
		// Ϊ��½��ťע������¼�
		jLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Login.this.excute(e);
			}
		});
		// Ϊ���ð�ťע������¼�
		jReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Login.this.reset(e);
			}
		});
		
		Toolkit tool=this.getToolkit(); //�õ�һ��Toolkit����
		Image myimage=tool.getImage("D:\\eclipse\\workspace\\QQchat\\1.jpg"); //��tool��ȡͼ��
		this.setIconImage(myimage);
		
		panel = new JPanel();
		panel.add(jLabel1);
		panel.add(userName);
		panel.add(jLabel2);
		panel.add(serverAddress);
		panel.add(jLabel3);
		panel.add(port);
		panel.add(jLogin);
		panel.add(jReset);
		panel.setBorder(BorderFactory.createTitledBorder(null, "�û���½ ",
				TitledBorder.TOP, TitledBorder.ABOVE_TOP, new Font(
						Font.DIALOG, Font.ITALIC, 20), Color.blue));
		this.setAlwaysOnTop(true);
		this.setVisible(true);
		this.setSize(300, 200);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.getContentPane().add(panel);
		this.setResizable(false);
	}

	// ���ð�ť�Ĵ�����
	private void reset(ActionEvent e) {
		userName.setText(null);
		serverAddress.setText(null);
		port.setText("5050");
	}

	// ��½��ť������
	private void excute(ActionEvent e) {
		// �½���½����, �����½��ص�����
		new LoginConnection(userName.getText(), serverAddress.getText(),
				Integer.parseInt(port.getText()), this);
	}
	public Login(String name) {
		super(name);
		this.init();
	}
	public static void main(String[] args) {
		new Login("Client");
	}
}