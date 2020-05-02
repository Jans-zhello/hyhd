package com.zzz.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

public class HuifuDB {
	private Connection conn = null;

	public HuifuDB(Connection conn) {
		this.conn = conn;
	}

	public void add(Huifu obj) throws Exception {
		PreparedStatement st = conn
				.prepareStatement("insert into huifu(txt,type,userid,titleid) values(?,?,?,?)");

		st.setString(1, obj.getTxt());
		st.setString(2, obj.getType());
		st.setInt(3, obj.getUserid());
		st.setInt(4, obj.getTitleid());
		if (st.executeUpdate() <= 0) {
			throw new Exception();
		}
	}

	public void set(Huifu obj) throws Exception {
		PreparedStatement st = conn
				.prepareStatement("update huifu set  txt =?,type =?,sendtime =?,userid =?,titleid =? where hid=?");
		st.setString(1, obj.getTxt());
		st.setString(2, obj.getType());
		st.setString(3, obj.getSendtime());
		st.setInt(4, obj.getUserid());
		st.setInt(5, obj.getTitleid());
		st.setInt(6, obj.getHid());
		if (st.executeUpdate() <= 0) {
			throw new Exception();
		}
	}

	public void delete(int obj) throws Exception {
		PreparedStatement st = conn
				.prepareStatement("delete from huifu where hid=?");
		st.setObject(1, obj);

		if (st.executeUpdate() <= 0) {
			throw new Exception();
		}
	}

	public Vector<Huifu> findKey(int key) throws Exception {
		return findColumnName("hid", key);
	}

	public static final String TXT = "txt";
	public static final String TYPE = "type";
	public static final String SENDTIME = "sendtime";
	public static final String USERID = "userid";
	public static final String TITLEID = "titleid";

	public Vector<Huifu> findColumnName(String cname, Object value)
			throws Exception {
		PreparedStatement pst = conn
				.prepareStatement("SELECT * FROM huifu WHERE " + cname + "=?");
		pst.setObject(1, value);
		ResultSet re = pst.executeQuery();
		Vector<Huifu> list = new Vector<Huifu>();
		while (re.next()) {
			Huifu obj1 = new Huifu();
			obj1.setHid(re.getInt("hid"));
			obj1.setTxt(re.getString("txt"));
			obj1.setType(re.getString("type"));
			obj1.setSendtime(re.getString("sendtime"));
			obj1.setUserid(re.getInt("userid"));
			obj1.setTitleid(re.getInt("titleid"));
			list.add(obj1);
		}
		return list;
	}

	public Vector<Huifu> findAll() throws Exception {
		PreparedStatement pst = conn.prepareStatement("SELECT * FROM huifu");
		ResultSet re = pst.executeQuery();
		Vector<Huifu> list = new Vector<Huifu>();
		while (re.next()) {
			Huifu obj1 = new Huifu();
			obj1.setHid(re.getInt("hid"));
			obj1.setTxt(re.getString("txt"));
			obj1.setType(re.getString("type"));
			obj1.setSendtime(re.getString("sendtime"));
			obj1.setUserid(re.getInt("userid"));
			obj1.setTitleid(re.getInt("titleid"));
			list.add(obj1);
		}
		return list;
	}

}