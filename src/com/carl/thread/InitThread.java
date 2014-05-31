package com.carl.thread;

import java.io.IOException;

import com.carl.controller.MainController;
import com.carl.http.ParseHtml;
import com.carl.http.Request;
import com.carl.message.ThreadMessage;
import com.carl.message.UserMessage;
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
			//置进入初始化状态
			updateUserInfo(UserMessage.UserStatus.IN_INIT, UserMessage.UserProgress.IN_INIT);
			showInfo("正在校验是否已登录...");
			String result = Request.getURLResult(userInfo, Request.index);
			if (ParseHtml.verifyLoginStatus(result, "Welcome")) {
				// 判断已登录,置状态已登录.
				updateUserInfo(UserMessage.UserStatus.IS_LOGIN, UserMessage.UserProgress.IS_LOGIN);
				showInfo("已登录.....开始自动任务....");
				new InitTaskThread(controller, userInfo).start();
				return;
			}
			showInfo("未登录....");
			showInfo("正在初始化....当前尝试次数:" + tryTimes + "....");
			Request.getInitCookiesAndCaptcha(userInfo);
			if (userInfo.getCaptcha()!=null) {
				//初始化完成,置状态初始化完成.
				updateUserInfo(UserMessage.UserStatus.DONE_INIT, UserMessage.UserProgress.DONE_INIT);
				showInfo("初始化完成.....");
				return;
			}
			//初始化失败,置状态未登录
			updateUserInfo(UserMessage.UserStatus.FAIL_INIT, UserMessage.UserProgress.NO_LOGIN);				
			showInfo("初始化失败.....");
		} catch (IOException e) {
			//初始化异常,置状态未登录.
			updateUserInfo(UserMessage.UserStatus.FAIL_INIT, UserMessage.UserProgress.NO_LOGIN);
			if (this.tryTimes <= ThreadMessage.MAX_TRY_TIME) {
				showError("初始化状态发生错误,请检查网络..程序将在2秒后重试...");
				new InitThread(controller, userInfo, 2000, tryTimes + 1).start();
			} else {
				showError("初始化状态发生错误,请检查网络..程序已重试超过"
						+ ThreadMessage.MAX_TRY_TIME + "次,将停止本次操作....");
			}
		}
	}

}
