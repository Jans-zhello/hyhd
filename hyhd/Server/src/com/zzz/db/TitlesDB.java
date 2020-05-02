package com.zzz.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

public class TitlesDB {
	private Connection conn = null;

	public TitlesDB(Connection conn) {
		this.conn = conn;
	}

	public void add(Titles obj) throws Exception {
		PreparedStatement st = conn
				.prepareStatement("insert into titles(userid,titletxt,type) values(?,?,?)");
		st.setInt(1, obj.getUserid());
		st.setString(2, obj.getTitletxt());
		st.setString(3, obj.getType());
		if (st.executeUpdate() <= 0) {
			throw new Exception();
		}
	}

	public void set(Titles obj) throws Exception {
		PreparedStatement st = conn
				.prepareStatement("update titles set  userid =?,titletxt =?,type =?,sendcreate =? where titleid=?");
		st.setInt(1, obj.getUserid());
		st.setString(2, obj.getTitletxt());
		st.setString(3, obj.getType());
		st.setString(4, obj.getSendcreate());
		st.setInt(5, obj.getTitleid());
		if (st.executeUpdate() <= 0) {
			throw new Exception();
		}
	}

	public void delete(int obj) throws Exception {
		PreparedStatement st = conn
				.prepareStatement("delete from titles where titleid=?");
		st.setObject(1, obj);

		if (st.executeUpdate() <= 0) {
			throw new Exception();
		}
	}

	public Vector<Titles> findKey(int key) throws Exception {
		return findColumnName("titleid", key);
	}

	public static final String USERID = "userid";
	public static final String TITLETXT = "titletxt";
	public static final String TYPE = "type";
	public static final String SENDCREATE = "sendcreate";

	public Vector<Titles> findColumnName(String cname, Object value)
			throws Exception {
		PreparedStatement pst = conn
				.prepareStatement("SELECT * FROM titles WHERE " + cname + "=?");
		pst.setObject(1, value);
		ResultSet re = pst.executeQuery();
		Vector<Titles> list = new Vector<Titles>();
		while (re.next()) {
			Titles obj1 = new Titles();
			obj1.setTitleid(re.getInt("titleid"));
			obj1.setUserid(re.getInt("userid"));
			obj1.setTitletxt(re.getString("titletxt"));
			obj1.setType(re.getString("type"));
			obj1.setSendcreate(re.getString("sendcreate"));
			list.add(obj1);
		}
		return list;
	}

	public Vector<Titles> findAll() throws Exception {
		PreparedStatement pst = conn.prepareStatement("SELECT * FROM titles");
		ResultSet re = pst.executeQuery();
		Vector<Titles> list = new Vector<Titles>();
		while (re.next()) {
			Titles obj1 = new Titles();
			obj1.setTitleid(re.getInt("titleid"));
			obj1.setUserid(re.getInt("userid"));
			obj1.setTitletxt(re.getString("titletxt"));
			obj1.setType(re.getString("type"));
			obj1.setSendcreate(re.getString("sendcreate"));
			list.add(obj1);
		}
		return list;
	}

}