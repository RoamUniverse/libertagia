package com.carl.thread;

import java.io.IOException;

import com.carl.controller.MainController;
import com.carl.http.ParseHtml;
import com.carl.http.Request;
import com.carl.message.UserMessage;
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
			//置正在登录状态
			updateUserInfo(UserMessage.UserStatus.IN_LOGIN, UserMessage.UserProgress.IN_LOGIN);
			showInfo("正在登录.....");
			Request.getLoginCookies(userInfo, code);
			String result = Request.getURLResult(userInfo, Request.index);
			if (ParseHtml.verifyLoginStatus(result)) {
				//登录成功,置状态已登录
				updateUserInfo(UserMessage.UserStatus.IS_LOGIN,
						UserMessage.UserProgress.IS_LOGIN);
				showInfo("登录成功........");
				//new InitTaskThread(controller, userInfo).start();
			} else {
				showError("登录失败...请检查账户信息...");
				//登录失败,置状态未登录
				updateUserInfo(UserMessage.UserStatus.FAIL_LOGIN,
						UserMessage.UserProgress.NO_LOGIN);
				return;
			}
		} catch (IOException e) {
			//登录异常,置状态未登录
			updateUserInfo(UserMessage.UserStatus.FAIL_LOGIN,
					UserMessage.UserProgress.NO_LOGIN);
			showError("登录请求发生错误,请检查网络....");
//			if (tryTimes <= ThreadMessage.MAX_TRY_TIME) {
//				showError("登录请求发生错误,请检查网络..程序将在2秒后重试...");
//				new LoginThread(controller, userInfo, 2000, tryTimes + 1, code)
//						.start();
//			} else {
//				showError("登录请求发生错误,请检查网络..程序已重试超过"
//						+ ThreadMessage.MAX_TRY_TIME + "次,将停止本次操作....");
//			}

			e.printStackTrace();
		}
	}

}
