package com.zzz.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import com.zzz.db.DBManager;
import com.zzz.db.HuiFuInfo;
import com.zzz.db.Huifu;
import com.zzz.db.HuifuDB;
import com.zzz.db.TitleInfo;
import com.zzz.db.Titles;
import com.zzz.db.TitlesDB;
import com.zzz.db.Userinfos;
import com.zzz.db.UserinfosDB;
import com.zzz.db.Users;
import com.zzz.db.UsersDB;
import com.zzz.tool.ServerConfig;

/**
 * 通知服务器(再开一个服务器重新将users添加到userpool中)
 * 
 * @author Tiny
 * 
 */
public class InformServer extends Thread {
	private Socket socket = null;

	public InformServer(Socket socket) {
		this.socket = socket;
	}

	// 开启服务器
	private static ServerSocket server = null;
	private static boolean flag = true;

	public static void OpenServer() throws Exception {
		try {
			server = new ServerSocket(Integer.parseInt(ServerConfig
					.getValue("inform_server_port")));
			while (flag) {
				new InformServer(server.accept()).start();
			}

		} catch (Exception e) {
			e.printStackTrace();
			try {
				server.close();
			} catch (Exception e1) {
			}
			throw e;
		}
	}

	// 关闭服务器
	public static void closeServer() throws Exception {
		try {
			flag = false;
			server.close();
		} catch (Exception e) {
			throw e;
		}
	}
	// 处理客户端请求
	public static HashMap<String, ObjectOutputStream> userpool = new HashMap<String, ObjectOutputStream>();
	public void run() {
		try {
			ObjectInputStream oin = new ObjectInputStream(
					socket.getInputStream());
			ObjectOutputStream out = new ObjectOutputStream(
					socket.getOutputStream());
			while (true) {
				MessageHandle m = (MessageHandle) oin.readObject();
				userpool.put(m.getValue().get("userid").toString(),out);
			}

		} catch (Exception e) {
			try {
				socket.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
}
