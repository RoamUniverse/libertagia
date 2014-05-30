package com.carl.thread;

import java.io.IOException;

import com.carl.controller.MainController;
import com.carl.http.Request;
import com.carl.pojo.UserInfo;

public class InitThread extends RequestThread {

	public InitThread(MainController controller, UserInfo userInfo) {
		super(controller, userInfo);
	}

	@Override
	public void threadTask() {
		// TODO Auto-generated method stub
		try {
			Request.getInitCookiesAndCaptcha(userInfo);
			userInfo.setStatus("初始化完成");
			controller.updateTable(userInfo);
			showLogs(false,"用户名:" + userInfo.getUsername() + "\t初始化完成.");
		} catch (IOException e) {
			showLogs(true,"用户名:" + userInfo.getUsername() + "\t初始化状态错误,请检查网络.");
		}
	}

}
