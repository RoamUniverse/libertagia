package com.carl.message;

import com.carl.thread.GlobalAutoStayThread;

public class LogLevel {
	public final static  String LOG_INFO = "–≈œ¢:\t";
	public final static  String LOG_ERROR = "¥ÌŒÛ:\t";
	public static void main(String[] args) throws InterruptedException {
		GlobalAutoStayThread g = new GlobalAutoStayThread(null, null,true);
		g.start();
		Thread.sleep(8000);
		g.setStopRequest(false);
		Thread.sleep(20000);
	}
}