package com.carl.thread;

import java.io.IOException;

import com.carl.controller.MainController;
import com.carl.http.Request;
import com.carl.pojo.UserInfo;

public class LoginThread extends RequestThread {
	private String code;

	public LoginThread(MainController controller, UserInfo userInfo) {
		super(controller, userInfo);
	}

	public LoginThread(MainController controller, UserInfo userInfo, String code) {
		super(controller, userInfo);
		this.code = code;
	}

	@Override
	public void threadTask() {
		try {
			Request.getLoginCookies(userInfo, code);
			new VerifyThread(controller, userInfo).start();
		} catch (IOException e) {
			showLogs(true, "程序发生网络错误,请重试");
			e.printStackTrace();
		}
	}

}
