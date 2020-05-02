package com.zzz.net;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Hashtable;
import java.util.Vector;

import com.zzz.db.TitleInfo;
import com.zzz.server.MessageHandle;
import com.zzz.util.Tools;
/**
 * 对接Server端LoginServer服务器
 * @author Tiny
 *
 */
public class LoginClient extends Thread {

	private static ObjectInputStream oin = null;
	private static ObjectOutputStream oout = null;

	private static void conn()  {
		try {
			Socket socket = new Socket(Tools.IP,Tools.PORT_1);
			oout = new ObjectOutputStream(socket.getOutputStream());
			oin = new ObjectInputStream(socket.getInputStream());
		} catch (Exception e) {
		}
	}
	public static Vector<TitleInfo> getData() {

		MessageHandle msg = new MessageHandle();
		msg.setType(MessageHandle.DOWNLOAD);
		synchronized (LoginClient.class) {
			try {
				oout.writeObject(msg);
				oout.flush();
				msg = (MessageHandle) oin.readObject();
				System.out.println("msgGetValue:"+msg.getReturnvalue().get("message").toString());
				if (msg.getReturnvalue().get("message").toString()
						.equalsIgnoreCase("download ok!")) {
					return (Vector<TitleInfo>) msg.getReturnvalue()
							.get("infos");
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return null;

	}
	
	
	
	
	public static String login(String userid, String password) throws Exception {
		conn();
		Tools.userid = userid;
		MessageHandle msg = new MessageHandle();
		Hashtable table = new Hashtable();
		table.put("userid", userid);
		table.put("password", password);
		msg.setType(msg.LOGIN);
		msg.setValue(table);
		oout.writeObject(msg);
		oout.flush();
		MessageHandle m2 = (MessageHandle) oin.readObject();
		String message = m2.getReturnvalue().get("message").toString();
		if (!message.equalsIgnoreCase("ok")) {
			//登录失败关闭
			try {
				oout.close();
				oin.close();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}else {
			//登录成功则开启informClient
           new InformClient().start();
		} 
		return message;
	}
	public static void send(final MessageHandle msg) {
		  new Thread(){
			  public void run() {
				 synchronized (LoginClient.class) {
					try {
						conn();
						oout.writeObject(msg);
						oout.flush();
						oin.readObject();
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				} 
			  };
		  }.start();
	       	
	}
	public static void close() {
		final MessageHandle msg=new MessageHandle();
		msg.setType(msg.LOGOUT);
		new Thread() {
			public void run() {
				synchronized (LoginClient.class) {
					try {
						oout.writeObject(msg);
						oout.flush();
						oin.readObject();
						oout.close();
						oin.close();
					} catch (Exception e) {
					}

				}
			};
		}.start();

	}
}