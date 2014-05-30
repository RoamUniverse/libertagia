package com.carl.thread;

import java.io.IOException;

import com.carl.controller.MainController;
import com.carl.http.Request;
import com.carl.message.ThreadMessage;
import com.carl.pojo.UserInfo;

public class InitThread extends RequestThread {

	public InitThread(MainController controller, UserInfo userInfo) {
		super(controller, userInfo);
	}

	public InitThread(MainController controller, UserInfo userInfo, int delay,
			int tryTimes) {
		super(controller, userInfo, delay, tryTimes);
	}

	@Override
	public void threadTask() {
		try {
			showLogs(false, String.format(
					"<Thread-ID:%d> 账户:%s 正在初始化....当前尝试次数:%d....", this.getId(),
					userInfo.getUsername(), tryTimes));
			Request.getInitCookiesAndCaptcha(userInfo);
			userInfo.setStatus("初始化完成");
			controller.updateTable(userInfo);
			showLogs(false, String.format("<Thread-ID:%d> 账户:%s\t初始化完成.....",
					this.getId(), userInfo.getUsername()));
		} catch (IOException e) {
			if (this.tryTimes <= ThreadMessage.MAX_TRY_TIME) {
				showLogs(true, "用户名:" + userInfo.getUsername()
						+ "\t初始化状态发生错误,请检查网络..程序将在2秒后重试...");
				new InitThread(controller, userInfo, 2000, tryTimes + 1).start();
			} else {
				showLogs(true, "用户名:" + userInfo.getUsername()
						+ "\t初始化状态发生错误,请检查网络..程序已重试超过"+ThreadMessage.MAX_TRY_TIME+"次,将停止本次操作....");
			}
		}
	}

}
