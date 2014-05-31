package com.carl.pojo;

import java.awt.Image;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.carl.message.UserMessage;

public class UserInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	private String username;
	private String password;
	private transient Image captcha;
	private Map<String, String> cookies = new HashMap<String, String>();
	private transient String status = UserMessage.UserStatus.NO_LOGIN; //实时状态信息
	private transient int inprogress = UserMessage.UserProgress.NO_LOGIN; //工作流状态
	private int rowIndex;
	public void addCookie(String key, String value) {
		cookies.put(key, value);
	}
	public void deleteCookie(String key) {
		cookies.remove(key);
	}
	public String getCookie(String key) {
		return cookies.get(key);
	}

	/**
	 * 格式化cookie为字符串
	 * */
	public String getCookies() {
		StringBuffer sb = new StringBuffer();
		Iterator<String> itr = this.cookies.keySet().iterator();
		while (itr.hasNext()) {
			String key = (String) itr.next();
			String val = this.cookies.get(key);
			sb.append(" " + key + "=" + val + ";");
		}
		if (sb.length() != 0)
			sb.replace(sb.length() - 1, sb.length(), "");
		return sb.toString();
	}


	public UserInfo(String username, String password) {
		this.username = username;
		this.password = password;
	}


	public Image getCaptcha() {
		return captcha;
	}

	public String getPassword() {
		return password;
	}

	public String getUsername() {
		return username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getStatus() {
		return status;
	}

	public void setCaptcha(Image captcha) {
		this.captcha = captcha;
	}


	@Override
	public String toString() {
		return "UserInfo [username=" + username + ", password=" + password
				+ ", cookies=" + cookies + ", status=" + status
				+ ", inprogress=" + inprogress + "]";
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getInprogress() {
		return inprogress;
	}
	public void setInprogress(int inprogress) {
		this.inprogress = inprogress;
	}
	public int getRowIndex() {
		return rowIndex;
	}
	public void setRowIndex(int rowIndex) {
		this.rowIndex = rowIndex;
	}

}
