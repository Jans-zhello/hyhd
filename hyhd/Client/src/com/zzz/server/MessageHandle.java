package com.zzz.server;
/**
 * 信息处理类(连接服务器和客户端的桥梁)
 */
import java.io.Serializable;
import java.util.Hashtable;

public class MessageHandle implements Serializable {

	public static final String LOGIN = "LOGIN";
	public static final String LOGOUT = "LOGOUT";
	public static final String REGISTER = "REGISTER";
	public static final String SEND_TEXT = "SEND_TEXT";
	public static final String SEND_IMAGE = "SEND_IMAGE";
	public static final String SEND_AMR = "SEND_AMR";
	public static final String HUIFU_TEXT = "HUIFU_TEXT";
	public static final String HUIFU_IMAGE = "HUIFU_IAMGE";
	public static final String HUIFU_AMR = "HUIFU_AMR";
	public static final String VIEW_USER = "VIEW_USER";
	public static final String DOWNLOAD = "DOWNLOAD";
	public Hashtable value = null;
	public Hashtable returnvalue = null;
	public String type = "";

	public Hashtable getValue() {
		return value;
	}

	public void setValue(Hashtable value) {
		this.value = value;
	}

	public Hashtable getReturnvalue() {
		return returnvalue;
	}

	public void setReturnvalue(Hashtable returnvalue) {
		this.returnvalue = returnvalue;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
