package com.carl.action;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.URL;

import org.apache.commons.io.IOUtils;

public class Request {
	
	public static void main(String[] args) throws Exception {
		URL url = new URL("http://libertagia.com/office/members/login");
		InetSocketAddress socketAddress = new InetSocketAddress(
				InetAddress.getByName("127.0.0.1"), 8087);
		HttpURLConnection conn = (HttpURLConnection) url
				.openConnection(new Proxy(Type.HTTP, socketAddress));
		conn.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:30.0) Gecko/20100101 Firefox/30.0");
		InputStream in = conn.getInputStream();
		String s = IOUtils.toString(in);
		System.out.println(s);
		conn.disconnect();
	}
}
