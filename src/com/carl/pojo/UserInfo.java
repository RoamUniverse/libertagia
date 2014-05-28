package com.carl.pojo;

import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.imageio.ImageIO;


public class UserInfo implements Serializable {
	private String username;
	private String password;
	private transient Image captcha;
	private transient HttpURLConnection conn = null;
	private Map<String, String> cookies = new HashMap<String, String>();
	private transient String status = "初始化";
	private int rowIndex;
	/**
	 * 构建请求头
	 * 
	 * */
	private void buildHeader() {
		conn.setRequestProperty("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		conn.setRequestProperty(
				"User-Agent",
				"Mozilla/5.0 (Macintosh; Intel Mac OS X 10.7; rv:29.0) Gecko/20100101 Firefox/29.0");

	}

	/**
	 * 格式化cookie为字符串
	 * */
	private String cookies2String() {
		StringBuffer sb = new StringBuffer();
		Iterator<String> itr = this.cookies.keySet().iterator();
		while (itr.hasNext()) {
			String key = (String) itr.next();
			String val = this.cookies.get(key);
			sb.append(" " + key + "=" + val + ";");
		}
		sb.replace(sb.length() - 1, sb.length(), "");
		return sb.toString();
	}

	public UserInfo(String username, String password) {
		this.username = username;
		this.password = password;
	}

	/**
	 * 取得验证码图片 请求头附带cookie
	 * */
	private void getCaptchaPage() {
		String link = "http://libertagia.com/img/captcha.jpg";
		try {
			URL url = new URL(link);
			conn = (HttpURLConnection) url.openConnection();
			this.buildHeader();
			conn.setReadTimeout(15 * 1000);
			conn.setRequestMethod("GET");
			this.requestWithCookies();
			InputStream in = conn.getInputStream();
			captcha = ImageIO.read(in);
		} catch (Exception e) {

		}
		conn.disconnect();
	}

	/**
	 * 抓取主页源码,验证是否登录
	 * */
	public void getIndex() {
		// String link = "http://libertagia.com/office/dashboard/index";
		String link = "http://libertagia.com/office/tasks";
		try {
			URL url = new URL(link);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Referer",
					"http://libertagia.com/office/members/login");
			conn.setRequestProperty("Host", "libertagia.com");
			this.requestWithCookies();
			this.buildHeader();
			System.out.println(this.getHtml());
			this.buildCookies();
		} catch (Exception e) {
			System.out.println("UserInfo.getIndex()->error");
			e.printStackTrace();
		}
		conn.disconnect();
	}

	/**
	 * POST登录
	 * */
	public void login(String code) {
		String link = "http://libertagia.com/office/members/login";
		try {
			URL url = new URL(link);
			conn = (HttpURLConnection) url.openConnection();
			this.buildHeader();
			conn.setRequestMethod("POST");
			conn.setInstanceFollowRedirects(false);
			conn.setRequestProperty("Referer",
					"http://libertagia.com/office/members/login");
			conn.setRequestProperty("Host", "libertagia.com");
			conn.setDoOutput(true);
			this.requestWithCookies();
			String s = String
					.format("_method=POST&data[Member][email]=%s&data[Member][password]=%s&data[Member][captcha]=%s&remember=1",
							this.username, this.password, code);
			String e = URLEncoder.encode(s, "utf-8").replaceAll("%26", "&").replaceAll("%3D", "=");
			conn.getOutputStream().write(e.getBytes());
			this.buildCookies();
			conn.getInputStream();
		} catch (Exception e) {
			System.out.println("UserInfo.login()->error");
			e.printStackTrace();
		}
		conn.disconnect();
	}

	/**
	 * 抓取登录页面cookie
	 * */
	public void getLogin() {
		String link = "http://libertagia.com/office/members/login";
		try {
			URL url = new URL(link);
			conn = (HttpURLConnection) url.openConnection();
			this.buildHeader();
			conn.setReadTimeout(15 * 1000);
			conn.setRequestMethod("GET");
			conn.getInputStream();
			this.buildCookies();
			conn.disconnect();
			this.getCaptchaPage();
		} catch (Exception e) {
			System.out.println("UserInfo.getLogin()->error");
			e.printStackTrace();
		}
	}

	/**
	 * 取得当前连接响应的页面数据
	 * */
	private String getHtml() throws IOException {
		InputStream is = conn.getInputStream();
		Reader r = new InputStreamReader(is);
		StringBuilder result = new StringBuilder("");
		int i;
		while ((i = r.read()) >= 0) {
			result.append((char) i);
		}
		return result.toString();
	}

	/**
	 * 根据当前连接返回的响应头更新cookie
	 * */
	private void buildCookies() {
		String key = null;
		for (int i = 1; (key = conn.getHeaderFieldKey(i)) != null; i++) {
			if (key.equalsIgnoreCase("set-cookie")) {
				String val = conn.getHeaderField(i);
				val = val.substring(0, val.indexOf(";"));
				String cKey = val.substring(0, val.indexOf("="));
				String cVal = val.substring(val.indexOf("=") + 1, val.length());
				cookies.put(cKey, cVal);
			}
		}
	}

	private void requestWithCookies() {
		conn.setRequestProperty("Cookie", this.cookies2String());
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

	public int getRowIndex() {
		return rowIndex;
	}

	public void setRowIndex(int rowIndex) {
		this.rowIndex = rowIndex;
	}
	

}
