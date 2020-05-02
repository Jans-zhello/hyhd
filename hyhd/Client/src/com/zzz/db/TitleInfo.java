package com.zzz.db;

import java.io.Serializable;
import java.util.Vector;
//实现序列化接口,才可从网上传输
public class TitleInfo implements Serializable {
	//建立一对多关系(属于面向对象中的一对多关系)
	private Vector<HuiFuInfo> hf = new  Vector<HuiFuInfo>();
     private String uname;
     public Vector<HuiFuInfo> getHf() {
		return hf;
	}
	public void setHf(Vector<HuiFuInfo> hf) {
		this.hf = hf;
	}
	private String titletxt;
     private String type;
     private String sendcreate;
     private int titleid;
     private int userid;
	public String getUname() {
		return uname;
	}
	public void setUname(String uname) {
		this.uname = uname;
	}
	public String getTitletxt() {
		return titletxt;
	}
	public void setTitletxt(String titletxt) {
		this.titletxt = titletxt;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSendcreate() {
		return sendcreate;
	}
	public void setSendcreate(String sendcreate) {
		this.sendcreate = sendcreate;
	}
	public int getTitleid() {
		return titleid;
	}
	public void setTitleid(int titleid) {
		this.titleid = titleid;
	}
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
}
