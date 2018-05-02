package util;
public class MessageType { 
	public static final int Login = 1;
	//登陆信息
	public static final int C2SMessage = 2;
	//客户端聊天信息 
	public static final int S2CMessage = 3;
	//服务器转发聊天信息 
	public static final int UserList = 4;
	//用户列表信息 
	public static final int ServerClosing = 5;
	//服务器关闭信息
	public static final int ClientClosing = 6;
	//客户端关闭信息 
	public static final int ClientClosingConfirm = 7;
	//对客户端关闭的响应 
	public static final int Response = 8;
	//登陆响应类型 
}
