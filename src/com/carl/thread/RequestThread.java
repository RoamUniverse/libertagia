package com.carl.thread;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.carl.controller.MainController;
import com.carl.http.ParseHtml;
import com.carl.http.Request;
import com.carl.message.LogLevel;
import com.carl.pojo.UserInfo;

public class RequestThread extends Thread {
	private MainController controller;
	private UserInfo userInfo;
	private boolean isInit = false;

	public RequestThread(MainController controller, UserInfo userInfo) {
		super();
		this.controller = controller;
		this.userInfo = userInfo;
	}

	public RequestThread(MainController controller, UserInfo userInfo,
			boolean isInit) {
		super();
		this.controller = controller;
		this.userInfo = userInfo;
		this.isInit = isInit;
	}

	public void run() {
		if (isInit) {
			try {
				Request.getInitCookiesAndCaptcha(userInfo);
				controller.updateTable(userInfo);
				controller.showLogs(LogLevel.LOG_INFO,
						"用户名:" + userInfo.getUsername() + "\t初始化完成.");
			} catch (IOException e) {
				controller.showLogs(LogLevel.LOG_ERROR,
						"用户名:" + userInfo.getUsername() + "\t初始化状态错误,请检查网络.");
			}
		}else {
			
		}
		
	}
	/*********数据准备***********
	1.GET	http://libertagia.com/office/tasks/	结果:取得secret参数值
	2.POST	http://tasks.libertagia.com/		参数:secret	结果:更新cookie
	3.GET	http://tasks.libertagia.com/		结果:更新cookie
	***************&***********/
	/*********任务开始***********
	1.POST	http://tasks.libertagia.com/index.php	参数:action=verifyStatus		结果:得到JOSN结果,其中有下一个任务token,已完成的任务token,完成任务标示
	2.POST	http://tasks.libertagia.com/		参数:action=view	code=下一个任务token	结果:取得任务进行页面 captcha
	3.POST	http://tasks.libertagia.com/index.php	参数:result=计算结果&action=finishTask	结果:true or false
	*********任务结束(进入递归)***********/

	/*
	 * 取得任务POST所需参数
	 */
	private void getTaskToken() throws IOException{
		//	1.GET	http://libertagia.com/office/tasks/	结果:取得secret参数值
		String result = Request.getURLResult(userInfo, Request.task);
		String token = ParseHtml.getSecretParam(result);
		//	2.POST	http://tasks.libertagia.com/		参数:secret	结果:更新cookie
		Map<String, String> data = new HashMap<String, String>();
		data.put("secret", token);
		result = Request.postURLResult(userInfo, Request.task_run, data, null,true);
		//	3.GET	http://tasks.libertagia.com/		结果:更新cookie
		Request.getURLResult(userInfo, Request.task_run);
	}
	
	/*
	 * 下一个任务
	 * 递归进行
	 */
	private void nextWork() {
		//1.POST	http://tasks.libertagia.com/index.php	参数:action=verifyStatus		结果:得到JOSN结果,其中有下一个任务token,已完成的任务token,完成任务标示
		
		//2.POST	http://tasks.libertagia.com/		参数:action=view	code=下一个任务token	结果:取得任务进行页面 captcha
		//3.POST	http://tasks.libertagia.com/index.php	参数:result=计算结果&action=finishTask	结果:true or false
		
	}

	public void setController(MainController controller) {
		this.controller = controller;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}
}
