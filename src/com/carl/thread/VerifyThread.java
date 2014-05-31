package com.carl.thread;

import java.io.IOException;

import com.carl.controller.MainController;
import com.carl.http.ParseHtml;
import com.carl.http.Request;
import com.carl.message.ThreadMessage;
import com.carl.message.UserMessage;
import com.carl.pojo.UserInfo;

public class VerifyThread extends RequestThread {

	public VerifyThread(MainController controller, UserInfo userInfo) {
		super(controller, userInfo);
	}

	public VerifyThread(MainController controller, UserInfo userInfo,
			int delay, int tryTimes) {
		super(controller, userInfo, delay, tryTimes);
	}

	@Override
	public void threadTask() {
		try {
			showLogs(false, String.format(
					"<Thread-ID:%d> 账户:%s 正在校验状态....当前尝试次数:%d...",
					this.getId(), userInfo.getUsername(), tryTimes));
			String result = Request.getURLResult(userInfo, Request.index);
			if (ParseHtml.verifyLoginStatus(result, "Welcome")) {
				showLogs(
						false,
						String.format("<Thread-ID:%d> 账户:%s\t登录状态有效.",
								this.getId(), userInfo.getUsername()));
				updateUserInfo(UserMessage.UserStatus.IS_LOGIN, UserMessage.UserProgress.IS_LOGIN);
			} else {
				showLogs(true, String.format(
						"<Thread-ID:%d> 账户:%s\t登录状态已失效,需要重新登录.", this.getId(),
						userInfo.getUsername()));
				updateUserInfo(UserMessage.UserStatus.NO_LOGIN, UserMessage.UserProgress.NO_LOGIN);
			}
		} catch (IOException e) {
			if (this.tryTimes <= ThreadMessage.MAX_TRY_TIME) {
				showLogs(true, "用户名:" + userInfo.getUsername()
						+ "\t登录状态校验请求发生错误,请检查网络..程序将在2秒后重试...");
				new VerifyThread(controller, userInfo, 2000, tryTimes + 1).start();
			} else {
				showLogs(true, "用户名:" + userInfo.getUsername()
						+ "\t登录状态校验请求发生错误,请检查网络..程序已重试超过"
						+ ThreadMessage.MAX_TRY_TIME + "次,将停止本次操作....");
			}
			e.printStackTrace();
		}
	}

}
