package com.zzz.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

public class UsersDB {
	private Connection conn = null;

	public UsersDB(Connection conn) {
		this.conn = conn;
	}

	public void add(Users obj) throws Exception {
		PreparedStatement st = conn
				.prepareStatement("insert into users(password,state,ukey) values(?,?,?)");

		st.setString(1, obj.getPassword());
		st.setInt(2, obj.getState());
		st.setString(3, obj.getUkey());
		if (st.executeUpdate() <= 0) {
			throw new Exception();
		}
	}

	public void set(Users obj) throws Exception {
		PreparedStatement st = conn
				.prepareStatement("update users set  password =?,state =?,ukey =? where userid=?");
		st.setString(1, obj.getPassword());
		st.setInt(2, obj.getState());
		st.setString(3, obj.getUkey());
		st.setInt(4, obj.getUserid());
		if (st.executeUpdate() <= 0) {
			throw new Exception();
		}
	}

	public void delete(int obj) throws Exception {
		PreparedStatement st = conn
				.prepareStatement("delete from users where userid=?");
		st.setObject(1, obj);

		if (st.executeUpdate() <= 0) {
			throw new Exception();
		}
	}

	public Vector<Users> findKey(int key) throws Exception {
		return findColumnName("userid", key);
	}

	public static final String PASSWORD = "password";
	public static final String STATE = "state";
	public static final String UKEY = "ukey";

	public Vector<Users> findColumnName(String cname, Object value)
			throws Exception {
		PreparedStatement pst = conn
				.prepareStatement("SELECT * FROM users WHERE " + cname + "=?");
		pst.setObject(1, value);
		ResultSet re = pst.executeQuery();
		Vector<Users> list = new Vector<Users>();
		while (re.next()) {
			Users obj1 = new Users();
			obj1.setUserid(re.getInt("userid"));
			obj1.setPassword(re.getString("password"));
			obj1.setState(re.getInt("state"));
			obj1.setUkey(re.getString("ukey"));
			list.add(obj1);
		}
		return list;
	}

	public Vector<Users> findAll() throws Exception {
		PreparedStatement pst = conn.prepareStatement("SELECT * FROM users");
		ResultSet re = pst.executeQuery();
		Vector<Users> list = new Vector<Users>();
		while (re.next()) {
			Users obj1 = new Users();
			obj1.setUserid(re.getInt("userid"));
			obj1.setPassword(re.getString("password"));
			obj1.setState(re.getInt("state"));
			obj1.setUkey(re.getString("ukey"));
			list.add(obj1);
		}
		return list;
	}

}