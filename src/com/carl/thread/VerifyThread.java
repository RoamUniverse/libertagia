package com.carl.thread;

import java.io.IOException;

import com.carl.controller.MainController;
import com.carl.http.ParseHtml;
import com.carl.http.Request;
import com.carl.pojo.UserInfo;

public class VerifyThread extends RequestThread {

	public VerifyThread(MainController controller, UserInfo userInfo) {
		super(controller, userInfo);
	}

	@Override
	public void threadTask() {
		try {
			String result = Request.getURLResult(userInfo, Request.index);
//			System.out.println(result);
			if(ParseHtml.verifyLoginStatus(result, "Welcome")){
				showLogs(false, String.format("<Thread-ID:%d> 账户:%s\t登录状态有效.",this.getId(), userInfo.getUsername()));
				updateSatus("已登录.");
			}else {
				showLogs(true, String.format("<Thread-ID:%d> 账户:%s\t登录状态已失效,需要重新登录.", this.getId() ,userInfo.getUsername()));
				updateSatus("未登录.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}
