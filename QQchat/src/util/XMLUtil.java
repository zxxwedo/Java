package util;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class XMLUtil {
	// 供本类使用的构造方法
	private static Document constructDocument() {
		Document document = DocumentHelper.createDocument();
		Element root = DocumentHelper.createElement("message");
		document.setRootElement(root);
		return document;
	}

	// 构造登陆结果数据格式
	public static String constructResponse(String result) {
		Document document = constructDocument(); 
		Element type = document.getRootElement().addElement("type");
		type.setText("8"); 
		Element toReturn =document.getRootElement().addElement("result");
		toReturn.setText(result);
		return document.asXML(); 
		}

	// 构造客户端2服务器的消息
	public static String constructeC2SMessageXML(String userName,
			String strMessage) {
		Document document = constructDocument();
		Element type = document.getRootElement().addElement("type");
		type.setText("2");
		Element user = document.getRootElement().addElement("user");
		user.setText(userName);
		Element content = document.getRootElement().addElement("content");
		content.setText(strMessage);
		return document.asXML();
	}

	public static String constructS2CMessage(String message) {
		Document document = constructDocument();
		Element type = document.getRootElement().addElement("type");
		type.setText("3");
		Element content = document.getRootElement().addElement("content");
		content.setText(message);
		return document.asXML();
	}

	// 构造用户关闭信息
	public static String constructClientClosingConfirm() {
		Document document = constructDocument();
		Element root = document.getRootElement();
		Element type = root.addElement("type");
		type.setText("7");
		Element content = root.addElement("content");
		content.setText("You can Close");
		return document.asXML();
	}

	// 构造登陆信息 
	public static String constructLoginXML(String userName) {
		Document document = constructDocument();
		Element root = document.getRootElement();
		Element type = root.addElement("type");
		type.setText("1");
		Element user = root.addElement("user");
		user.setText(userName);
		return document.asXML();
	}

	// 构造用户列表
	public static String constructUserList(Set<String> allUsers) {
		Document document = constructDocument();
		Element root = document.getRootElement();
		Element type = root.addElement("type");
		type.setText("4");
		for (String userName : allUsers) {
			Element e = root.addElement("userName");
			e.setText(userName);
		}
		return document.asXML();
	}

	// 构造服务器关闭信息
	public static String constructServerClosingXML() {
		Document document = constructDocument();
		Element root = document.getRootElement();
		Element type = root.addElement("type");
		type.setText("5");
		return document.asXML();
	}

	// 构造客户端关闭信息
	public static String constructClientClosingXML(String userName) {
		Document document = constructDocument();
		Element root = document.getRootElement();
		Element type = root.addElement("type");
		type.setText("6");
		Element user = root.addElement("user");
		user.setText(userName);
		return document.asXML();
	}

	// 从客户端登陆所发送的XML数据中解析出用户名(userName)
	public static String extractUsername(String xml) {
		try {
			SAXReader saxReader = new SAXReader();
			Document document = saxReader.read(new StringReader(xml));
			Element user = document.getRootElement().element("user");
			return user.getText();
		} catch (DocumentException e) {
		}
		return null;
	}

	// 解析登陆结果
	public static String extractResult(String xml) {
		try {
			SAXReader saxReader = new SAXReader();
			Document document = saxReader.read(new StringReader(xml));
			Element result = document.getRootElement().element("result");
			return result.getText();
		} catch (DocumentException e) {
		}
		return null;
	}

	// 解析出用户列表
	public static List<String> extractAllUsers(String xml) {
		List <String> list = new  ArrayList <String>();
		try {
			SAXReader saxReader = new SAXReader();
			Document document = saxReader.read(new StringReader(xml));
			for (Iterator iter = document.getRootElement().elementIterator(
					"userName"); iter.hasNext();) {
				Element e = (Element) iter.next();
				list.add(e.getText());
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return list;
	}

	public static String extractType(String xml) {
	String messageType;
	try { 
		SAXReader saxReader = new SAXReader();
		Document document = saxReader.read(new StringReader(xml));
		Element root = document.getRootElement(); 
		Element type = root.element("type");
		messageType = type.getText(); 
		return messageType; 
		}
	catch (DocumentException e) { 
		// TODO Auto-generated 
		e.printStackTrace();
		} 
	return null;
	}	// 解析消息内容
	public static String extractContent(String message) {
		try {
			SAXReader saxReader = new SAXReader();
			Document document = saxReader.read(new StringReader(message));
			Element root = document.getRootElement();
			Element content = root.element("content");
			return content.getText();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
