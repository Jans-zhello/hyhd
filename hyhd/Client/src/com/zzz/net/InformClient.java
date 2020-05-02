package com.zzz.net;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Hashtable;
import java.util.Vector;

import com.zzz.app.ConsoleActivity;
import com.zzz.db.TitleInfo;
import com.zzz.server.MessageHandle;
import com.zzz.util.Tools;
/**
 * 对接Server端InformServer服务器
 * @author Administrator
 *
 */
public class InformClient extends Thread {
	static Socket socket = null;
	public static ConsoleActivity action = null;
    @Override
    public void run() {
       try {
    		socket = new Socket(Tools.IP, Tools.PORT_2);
			ObjectOutputStream oout = new ObjectOutputStream(socket
					.getOutputStream());
			ObjectInputStream oin = new ObjectInputStream(socket
					.getInputStream());
			MessageHandle m = new MessageHandle();
			Hashtable table = new Hashtable();
			table.put("userid", Tools.userid);
			m.setValue(table);
			oout.writeObject(m);
			oout.flush();
			while (true) {
				MessageHandle m2 = (MessageHandle) oin.readObject();
				Vector<TitleInfo> vv = LoginClient.getData();
				if (action != null) {
					action.vv1 = vv;
					android.os.Message msg1 = new android.os.Message();
					action.hand.sendMessage(msg1);
				}
			}
	   }
	 catch (Exception e) {
               e.printStackTrace();    
	}  
    }
	// 关闭
	public static void close() throws Exception {
		try {
			socket.close();
		} catch (Exception e) {
			throw e;
		}
	}
}
