package com.carl.thread;

import java.io.IOException;

import com.carl.controller.MainController;
import com.carl.http.ParseHtml;
import com.carl.http.Request;
import com.carl.message.ThreadMessage;
import com.carl.pojo.UserInfo;

public class LoginThread extends RequestThread {
	private String code;

	public LoginThread(MainController controller, UserInfo userInfo, int delay,
			int tryTimes, String code) {
		super(controller, userInfo, delay, tryTimes);
		this.code = code;
	}

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
			showLogs(false, String.format(
					"<Thread-ID:%d> 账户:%s 正在登录...当前尝试次数:%d....", this.getId(),
					userInfo.getUsername(), tryTimes));
			Request.getLoginCookies(userInfo, code);
			// TODO 这里可能会发生死锁,或者其他问题.
			// new VerifyThread(controller, userInfo).start();
			String result = Request.getURLResult(userInfo, Request.index);
			if (ParseHtml.verifyLoginStatus(result, "Welcome")) {
				showLogs(
						false,
						String.format("<Thread-ID:%d> 账户:%s\t登录成功.",
								this.getId(), userInfo.getUsername()));
				updateSatus("已登录.");
			} else {
				showLogs(true, String.format(
						"<Thread-ID:%d> 账户:%s\t登录失败,请检查账户信息.", this.getId(),
						userInfo.getUsername()));
				updateSatus("未登录.");
			}
		} catch (IOException e) {
			if (tryTimes <= ThreadMessage.MAX_TRY_TIME) {
				showLogs(true, "用户名:" + userInfo.getUsername()
						+ "\t登录请求发生错误,请检查网络..程序将在2秒后重试...");
				new LoginThread(controller, userInfo, 2000, tryTimes + 1, code).start();
			} else {
				showLogs(true, "用户名:" + userInfo.getUsername()
						+ "\t登录请求发生错误,请检查网络..程序已重试超过"
						+ ThreadMessage.MAX_TRY_TIME + "次,将停止本次操作....");
			}

			e.printStackTrace();
		}
	}

}
