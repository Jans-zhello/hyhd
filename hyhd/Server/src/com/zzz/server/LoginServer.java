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
 * ��¼������(�ճ���Ϣ���������)
 * 
 * @author Tiny
 * 
 */
public class LoginServer extends Thread {
	private Socket socket = null;

	public LoginServer(Socket socket) {
		this.socket = socket;
	}

	// ����������
	private static ServerSocket server = null;
	private static boolean flag = true;

	public static void OpenServer() throws Exception {
		try {
			server = new ServerSocket(Integer.parseInt(ServerConfig
					.getValue("server_port")));
			while (flag) {
				new LoginServer(server.accept()).start();
			}

		} catch (Exception e) {
			e.printStackTrace();
			try {
				closeServer();
			} catch (Exception e1) {
			}
			throw e;
		}
	}

	// �رշ�����
	public static void closeServer() throws Exception {
		try {
			flag = false;
			server.close();
		} catch (Exception e) {
			throw e;
		}
	}

	// ����ͻ�������
	// ����:
	// һ̨���������ͬʱ�ṩ�������
	// ��Щ��ͬ�ķ���֮��ͨ���˿ں�������
	// ��ͬ�Ķ˿ں����ṩ��ͬ�ķ���:
	// ����ʹ��ͬһ��������,��ͬһwifi��,���˿ںŲ�һ��һ��
	private static HashMap<Integer, ObjectOutputStream> userpool = new HashMap<Integer, ObjectOutputStream>();
	int userid = 0;

	public void run() {
		try {
			ObjectInputStream oin = new ObjectInputStream(
					socket.getInputStream());
			ObjectOutputStream out = new ObjectOutputStream(
					socket.getOutputStream());
			while (true) {
				MessageHandle msHandle = (MessageHandle) oin.readObject();
				System.out.println("�������õ���Ϣ��:"+msHandle.getType());
				/**
				 * ��¼��̨���
				 */
				if (msHandle.getType().equalsIgnoreCase(MessageHandle.LOGIN)) {
					int userid = Integer.parseInt(msHandle.getValue()
							.get("userid").toString());
					String passsword = msHandle.getValue().get("password")
							.toString();
					this.userid = userid;
					Connection conn = DBManager.getDBManager().getConnection();
					UsersDB db = new UsersDB(conn);
					Vector<Users> v = db.findKey(userid);
					MessageHandle m1 = new MessageHandle();// ���ͻ��˻�����Ϣ
					if (v.size() >= 1) {
						if (v.get(0).getPassword().equalsIgnoreCase(passsword)) {
							if (v.get(0).getState() == 1) {
								Hashtable table = new Hashtable();
								table.put("message", "ok");
								m1.setReturnvalue(table);
								// ����������⣬��֮ǰ����,�Ƴ���ȥ
								ObjectOutputStream out2 = userpool.get(userid);
								if (out2 != null) {
									try {
										out2.close();
										userpool.remove(userid);
									} catch (Exception e) {
									}
								}
								// ����¼�ɹ����û���ӵ�HashMap����ȥ
								userpool.put(userid, out);
							} else {
								Hashtable table = new Hashtable();
								table.put("message", "not activated");
								m1.setReturnvalue(table);
							}
						} else {
							Hashtable table = new Hashtable();
							table.put("message", "password error");
							m1.setReturnvalue(table);
						}
					} else {
						Hashtable table = new Hashtable();
						table.put("message", "notUser");
						m1.setReturnvalue(table);
					}
					out.writeObject(m1);
					out.flush();
					conn.close();
					/**
					 * ��¼ʧ�ܴ��������׳��쳣�ر�socket
					 */
					if (!m1.getReturnvalue().get("message").equals("ok")) {
						throw new Exception();
					}
				}
				/**
				 * �˳���̨���
				 */

				else if (msHandle.getType().equalsIgnoreCase(
						MessageHandle.LOGOUT)) {
					userpool.remove(userid);
					throw new Exception();
				}
				/**
				 * ע���̨���(������ע�����д����ͬ������������,ѧϰ)
				 * 
				 */
				else if (msHandle.getType().equalsIgnoreCase(
						MessageHandle.REGISTER)) {
					Connection conn = DBManager.getDBManager().getConnection();
					MessageHandle m2 = new MessageHandle();// ���ͻ��˻�����Ϣ
					try {
						conn.setAutoCommit(false);// �ֶ��ύ����
						UsersDB db = new UsersDB(conn);
						//����Ƿ�����
						UserinfosDB db2 = new UserinfosDB(conn);
						Vector<Userinfos> v0 = db2.findAll();
					    for (Userinfos userinfos : v0) {
						  if (msHandle.getValue().get("uname")
								.toString().equals(userinfos.getUname())) {
							  Hashtable table = new Hashtable();
							  table.put("message", "is registered");
							  m2.setReturnvalue(table);
							  out.writeObject(m2);
							  out.flush();
							  return;
							}
						}
						//����users��
						Users u1 = new Users();
						u1.setPassword(msHandle.getValue().get("password")
								.toString());
						u1.setState(0);
						/**
						 * ͨ�������key�ֶε�Ψһ�Ի�ȡ�ող����userid(��˼·)
						 */
						String key = new Date().getTime() + "R"
								+ (Math.random() * 100) + "R"
								+ (Math.random() * 1000);

						u1.setUkey(key);
						db.add(u1);
						/**
						 * ��ȡ�ող����userid
						 */
						Vector<Users> v2 = db.findColumnName(db.UKEY, key);
						u1 = v2.get(0);
                        //����userinfo��
						Userinfos info = new Userinfos();
						info.setUserid(u1.getUserid());
						info.setAddress(msHandle.getValue().get("address")
								.toString());
						info.setPhone(msHandle.getValue().get("phone")
								.toString());
						info.setSex(msHandle.getValue().get("sex").toString());
						info.setUname(msHandle.getValue().get("uname")
								.toString());
						db2.add(info);
						conn.commit();

						Hashtable table = new Hashtable();
						table.put("message", "register ok!");
						table.put("userid", u1.getUserid() + "");
						table.put("uname", info.getUname());
						m2.setReturnvalue(table);
					} catch (Exception e) {
						conn.rollback();// ����������ع�������֮ǰ�Ĳ���
						Hashtable table = new Hashtable();
						table.put("message", "register error!");
						m2.setReturnvalue(table);
					} finally {
						conn.close();
					}
					out.writeObject(m2);
					out.flush();
				} else if (msHandle.getType().equalsIgnoreCase(
						MessageHandle.DOWNLOAD)) {
					Connection conn = DBManager.getDBManager().getConnection();
					MessageHandle m5 = new MessageHandle();// ���ͻ��˻�����Ϣ
					try {
						ResultSet rs = conn.createStatement().executeQuery("select  t.titleid,t.userid,u.uname,t.titletxt,t.type,t.sendcreate from titles t left join userinfos u on t.userid=u.userid order by t.sendcreate desc limit 10");
						Vector<TitleInfo> v = new Vector<TitleInfo>();
						while (rs.next()) {
							TitleInfo ti = new TitleInfo();
							ti.setTitleid(rs.getInt("titleid"));
							ti.setUserid(rs.getInt("userid"));
							ti.setUname(rs.getString("uname"));
							ti.setSendcreate(rs.getString("sendcreate"));
							ti.setTitletxt(rs.getString("titletxt"));
							ti.setType(rs.getString("type"));
							v.add(ti);
							/**
							 * ÿһ��title�ֶ�Ӧ����ظ�����
							 */
							ResultSet rs1 = conn
									.createStatement()
									.executeQuery(
											"select h.hid"
													+ ",h.txt,h.type,h.sendtime,h.userid,u.uname,h.titleid from huifu h inner join userinfos u "
													+ "on h.userid=u.userid and"
													+ " h.titleid="
													+ ti.getTitleid()
													+ " order by h.sendtime");
							while (rs1.next()) {
								HuiFuInfo h = new HuiFuInfo();
								h.setUserid(rs1.getInt("userid"));
								h.setSendtime(rs1.getString("sendtime"));
								h.setTitleid(rs1.getInt("titleid"));
								h.setTxt(rs1.getString("txt"));
								h.setType(rs1.getString("type"));
								h.setUname(rs1.getString("uname"));
								h.setHid(rs1.getInt("hid"));
								ti.getHf().add(h);
							}
						}
						Hashtable table = new Hashtable();
						table.put("message", "download ok!");
						table.put("infos", v);
						m5.setReturnvalue(table);
					} catch (Exception e) {
						e.printStackTrace();
						Hashtable table = new Hashtable();
						table.put("message", "download error!");
						m5.setReturnvalue(table);
					} finally {
						conn.close();
					}
					out.writeObject(m5);
					out.flush();

				} else if (msHandle.getType().equalsIgnoreCase(
						MessageHandle.SEND_TEXT)
						|| msHandle.getType().equalsIgnoreCase(
								MessageHandle.SEND_IMAGE)
						|| msHandle.getType().equalsIgnoreCase(
								MessageHandle.SEND_AMR)) {
					Connection conn = DBManager.getDBManager().getConnection();
					MessageHandle m3 = new MessageHandle();// ���ͻ��˻�����Ϣ
					try {
						Titles t = new Titles();
						t.setUserid(Integer.parseInt(msHandle.getValue()
								.get("userid").toString()));
						t.setTitletxt(msHandle.getValue().get("titletxt")
								.toString());
						t.setType(msHandle.getType());
						new TitlesDB(conn).add(t);

						Hashtable table = new Hashtable();
						table.put("message", "send ok!");
						m3.setReturnvalue(table);
						/**
						 * ֪ͨ�����ͻ��˸���(��һ�¼���)
						 */
						MessageHandle m = new MessageHandle();
						Hashtable table1 = new Hashtable();
						table1.put("message", "action");
						m.setReturnvalue(table1);
						new Inform(m).start();
					} catch (Exception e) {
						Hashtable table = new Hashtable();
						table.put("message", "send error!");
						m3.setReturnvalue(table);

					} finally {
						conn.close();
					}
					out.writeObject(m3);
					out.flush();
				} else if (msHandle.getType().equalsIgnoreCase(
						MessageHandle.HUIFU_TEXT)
						|| msHandle.getType().equalsIgnoreCase(
								MessageHandle.HUIFU_IMAGE)
						|| msHandle.getType().equalsIgnoreCase(
								MessageHandle.HUIFU_AMR)) {
					Connection conn = DBManager.getDBManager().getConnection();
					MessageHandle m4 = new MessageHandle();// ���ͻ��˻�����Ϣ
					try {
						Huifu hf = new Huifu();
						hf.setTitleid(Integer.parseInt(msHandle.getValue()
								.get("titleid").toString()));
						hf.setUserid(Integer.parseInt(msHandle.getValue()
								.get("userid").toString()));
						hf.setTxt(msHandle.getValue().get("txt").toString());
						hf.setType(msHandle.getType());
						new HuifuDB(conn).add(hf);

						Hashtable table = new Hashtable();
						table.put("message", "huifu ok!");
						m4.setReturnvalue(table);
						/**
						 * ֪ͨ�����ͻ��˸���(��һ�¼���)
						 */
						MessageHandle m = new MessageHandle();
						Hashtable table1 = new Hashtable();
						table1.put("message", "action");
						m.setReturnvalue(table1);
						new Inform(m).start();

					} catch (Exception e) {
						Hashtable table = new Hashtable();
						table.put("message", "huifu error!");
						m4.setReturnvalue(table);

					} finally {
						conn.close();
					}
					out.writeObject(m4);
					out.flush();

				} else {
					System.out.println("�鿴������Ϣ");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				socket.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
}
