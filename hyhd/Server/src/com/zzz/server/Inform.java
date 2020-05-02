package com.zzz.server;

import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Set;
//获取通知服务器的userpool中ObjectOutputStream,做内容更新准备
public class Inform extends Thread {
	MessageHandle m = null;

	public Inform(MessageHandle messageHandle) {
		this.m = messageHandle;
	}

	public void run() {
		HashMap<String, ObjectOutputStream> hm = (HashMap<String, ObjectOutputStream>) InformServer.userpool
				.clone();
		Set<String> keys = hm.keySet();
		for (String string : keys) {
			try {
				ObjectOutputStream out = hm.get(string);
				synchronized (out) {
					out.writeObject(m);
					out.flush();
				}
			} catch (Exception e) {
				
				
				
				
			}
		}
	}
}
