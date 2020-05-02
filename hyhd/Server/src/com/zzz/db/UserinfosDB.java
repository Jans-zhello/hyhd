package com.zzz.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

public class UserinfosDB {
	private Connection conn = null;

	public UserinfosDB(Connection conn) {
		this.conn = conn;
	}

	public void add(Userinfos obj) throws Exception {
		PreparedStatement st = conn
				.prepareStatement("insert into userinfos(userid,uname,sex,address,phone) values(?,?,?,?,?)");
		st.setInt(1, obj.getUserid());
		st.setString(2, obj.getUname());
		st.setString(3, obj.getSex());
		st.setString(4, obj.getAddress());
		st.setString(5, obj.getPhone());
		if (st.executeUpdate() <= 0) {
			throw new Exception();
		}
	}

	public void set(Userinfos obj) throws Exception {
		PreparedStatement st = conn
				.prepareStatement("update userinfos set  uname =?,sex =?,address =?,phone =? where userid=?");
		st.setString(1, obj.getUname());
		st.setString(2, obj.getSex());
		st.setString(3, obj.getAddress());
		st.setString(4, obj.getPhone());
		st.setInt(5, obj.getUserid());
		if (st.executeUpdate() <= 0) {
			throw new Exception();
		}
	}

	public void delete(int obj) throws Exception {
		PreparedStatement st = conn
				.prepareStatement("delete from userinfos where userid=?");
		st.setObject(1, obj);

		if (st.executeUpdate() <= 0) {
			throw new Exception();
		}
	}

	public Vector<Userinfos> findKey(int key) throws Exception {
		return findColumnName("userid", key);
	}

	public static final String UNAME = "uname";
	public static final String SEX = "sex";
	public static final String ADDRESS = "address";
	public static final String PHONE = "phone";

	public Vector<Userinfos> findColumnName(String cname, Object value)
			throws Exception {
		PreparedStatement pst = conn
				.prepareStatement("SELECT * FROM userinfos WHERE " + cname
						+ "=?");
		pst.setObject(1, value);
		ResultSet re = pst.executeQuery();
		Vector<Userinfos> list = new Vector<Userinfos>();
		while (re.next()) {
			Userinfos obj1 = new Userinfos();
			obj1.setUserid(re.getInt("userid"));
			obj1.setUname(re.getString("uname"));
			obj1.setSex(re.getString("sex"));
			obj1.setAddress(re.getString("address"));
			obj1.setPhone(re.getString("phone"));
			list.add(obj1);
		}
		return list;
	}

	public Vector<Userinfos> findAll() throws Exception {
		PreparedStatement pst = conn
				.prepareStatement("SELECT * FROM userinfos");
		ResultSet re = pst.executeQuery();
		Vector<Userinfos> list = new Vector<Userinfos>();
		while (re.next()) {
			Userinfos obj1 = new Userinfos();
			obj1.setUserid(re.getInt("userid"));
			obj1.setUname(re.getString("uname"));
			obj1.setSex(re.getString("sex"));
			obj1.setAddress(re.getString("address"));
			obj1.setPhone(re.getString("phone"));
			list.add(obj1);
		}
		return list;
	}

}