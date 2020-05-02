package com.zzz.db;

import java.io.Serializable;

public class HuiFuInfo implements Serializable{
  private int hid;
  private int titleid;
  private int userid;
  private String txt;
  private String type;
  private String sendtime;
  public int getHid() {
	return hid;
}
public void setHid(int hid) {
	this.hid = hid;
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
public String getTxt() {
	return txt;
}
public void setTxt(String txt) {
	this.txt = txt;
}
public String getType() {
	return type;
}
public void setType(String type) {
	this.type = type;
}
public String getSendtime() {
	return sendtime;
}
public void setSendtime(String sendtime) {
	this.sendtime = sendtime;
}
public String getUname() {
	return uname;
}
public void setUname(String uname) {
	this.uname = uname;
}
private String uname;
}
