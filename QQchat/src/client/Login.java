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
	/** * 登陆窗口 */
	private static final long serialVersionUID = 1L;
	private JLabel jLabel1, jLabel2, jLabel3;
	private JTextField userName, serverAddress, port;
	private JButton jLogin, jReset;
	private JPanel panel;
	// 初始化各组件
	private void init() {
		jLabel1 = new JLabel();
		jLabel2 = new JLabel();
		jLabel3 = new JLabel();
		jLabel1.setText("用户名");
		jLabel2.setText("服务器");
		jLabel3.setText("端口号");
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
		jLogin.setText("登陆");
		jReset.setText("重置");
		// 为登陆按钮注册监听事件
		jLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Login.this.excute(e);
			}
		});
		// 为重置按钮注册监听事件
		jReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Login.this.reset(e);
			}
		});
		
		Toolkit tool=this.getToolkit(); //得到一个Toolkit对象
		Image myimage=tool.getImage("D:\\eclipse\\workspace\\QQchat\\1.jpg"); //由tool获取图像
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
		panel.setBorder(BorderFactory.createTitledBorder(null, "用户登陆 ",
				TitledBorder.TOP, TitledBorder.ABOVE_TOP, new Font(
						Font.DIALOG, Font.ITALIC, 20), Color.blue));
		this.setAlwaysOnTop(true);
		this.setVisible(true);
		this.setSize(300, 200);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.getContentPane().add(panel);
		this.setResizable(false);
	}

	// 重置按钮的处理方法
	private void reset(ActionEvent e) {
		userName.setText(null);
		serverAddress.setText(null);
		port.setText("5050");
	}

	// 登陆按钮处理方法
	private void excute(ActionEvent e) {
		// 新建登陆进程, 处理登陆相关的事宜
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