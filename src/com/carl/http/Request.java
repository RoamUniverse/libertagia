package com.carl.http;

import java.awt.Image;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Scanner;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.carl.pojo.UserInfo;

public class Request {
	private static final String login = "http://libertagia.com/office/members/login";
	private static final String captcha = "http://libertagia.com/img/captcha.jpg";

	
	public static void main(String[] args) throws IOException {
		UserInfo userInfo = new UserInfo("1114486604@qq.com", "567890");
		Request.getInitCookiesAndCaptcha(userInfo);
		
		System.out.println(userInfo.getCookies());
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream(); 
		ImageIO.write((RenderedImage) userInfo.getCaptcha(), "jpg", bos);
		File file = new File("/Users/hysm/Desktop/cataaa.jpg");
		FileUtils.writeByteArrayToFile(file, bos.toByteArray());
		
		Scanner sc = new Scanner(System.in);
		System.out.println("input code");
		String code = sc.nextLine();
		
		Request.getLoginCookies(userInfo, code);
		
		System.out.println(userInfo.getCookies());
		
		String link = "http://libertagia.com/office/dashboard/index";
		String r = Request.getURLResult(userInfo, link);
		System.out.println(r);
	}
	
	private Request() {

	}

	/*
	 * 初始化连接
	 */
	@SuppressWarnings("unused")
	private static HttpURLConnection initConnection(String urlString,
			String method) throws IOException {
		return initConnection(urlString, method, "");
	}

	/*
	 * 初始化连接
	 */
	private static HttpURLConnection initConnection(String urlString,
			String method, String cookies) throws IOException {
		HttpURLConnection conn = (HttpURLConnection) (new URL(urlString)
				.openConnection());
		conn.setRequestProperty("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		conn.setRequestProperty(
				"User-Agent",
				"Mozilla/5.0 (Macintosh; Intel Mac OS X 10.7; rv:29.0) Gecko/20100101 Firefox/29.0");
		if (!"".equals(cookies)) {
			conn.setRequestProperty("Cookie", cookies);
		}
		conn.setReadTimeout(15 * 1000);
		conn.setRequestMethod(method);
		return conn;
	}

	/*
	 * 取得初始化cookies 取得验证码图片
	 */
	public static void getInitCookiesAndCaptcha(UserInfo userInfo)
			throws IOException {
		HttpURLConnection conn = initConnection(login, "GET",
				userInfo.getCookies());
		conn.getInputStream();
		updateCookies(userInfo, conn);
		conn.disconnect();

		conn = initConnection(captcha, "GET",userInfo.getCookies());
		InputStream in = conn.getInputStream();
		Image captchaImage = ImageIO.read(in);
		updateCookies(userInfo, conn);
		userInfo.setCaptcha(captchaImage);
		conn.disconnect();
	}

	/*
	 * 更新cookies
	 */
	private static void updateCookies(UserInfo userInfo, HttpURLConnection conn) {
		String key = null;
		for (int i = 1; (key = conn.getHeaderFieldKey(i)) != null; i++) {
			if (key.equalsIgnoreCase("set-cookie")) {
				String val = conn.getHeaderField(i);
				val = val.substring(0, val.indexOf(";"));
				String cKey = val.substring(0, val.indexOf("="));
				String cVal = val.substring(val.indexOf("=") + 1, val.length());
//				System.err.println(String.format("add cookie %s = %s", cKey,cVal));
				userInfo.addCookie(cKey, cVal);
			}
		}
	}

	/*
	 * POST登录取得登录状态cookies
	 */
	public static void getLoginCookies(UserInfo userInfo, String code)
			throws IOException {
		HttpURLConnection conn = initConnection(login, "POST",
				userInfo.getCookies());
		conn.setInstanceFollowRedirects(false);
		conn.setRequestProperty("Referer",login);
		conn.setRequestProperty("Host", "libertagia.com");
		conn.setDoOutput(true);		
		String s = String
				.format("_method=POST&data[Member][email]=%s&data[Member][password]=%s&data[Member][captcha]=%s&remember=1",
						userInfo.getUsername(), userInfo.getPassword(), code);
		String e = URLEncoder.encode(s, "utf-8").replaceAll("%26", "&")
				.replaceAll("%3D", "=");
		conn.getOutputStream().write(e.getBytes());
		conn.getInputStream();
		updateCookies(userInfo, conn);
		conn.disconnect();
	}

	/*
	 * 发送GET请求 附带登录状态cookies
	 */
	public static String getURLResult(UserInfo userInfo,String url) throws IOException {
		HttpURLConnection conn = initConnection(url, "GET", userInfo.getCookies());
		InputStream in = conn.getInputStream();
		updateCookies(userInfo, conn);
		return IOUtils.toString(in,"utf-8");
		
	}
	
	/*
	 * 发送POST请求 附带登录状态cookies 附带参数
	 */
	public static String postURLResult(UserInfo userInfo,String url, Map<String, String> data,
			Map<String, String> header) throws IOException {
		HttpURLConnection conn = initConnection(url, "POST", userInfo.getCookies());
		InputStream in = conn.getInputStream();
		updateCookies(userInfo, conn);
		return IOUtils.toString(in,"utf-8");
	}
}
