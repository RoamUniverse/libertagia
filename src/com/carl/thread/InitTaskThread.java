package com.carl.thread;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.carl.controller.MainController;
import com.carl.http.ParseHtml;
import com.carl.http.Request;
import com.carl.message.UserMessage;
import com.carl.pojo.UserInfo;

/*********数据准备***********
1.GET	http://libertagia.com/office/tasks/	结果:取得secret参数值
2.POST	http://tasks.libertagia.com/		参数:secret	结果:更新cookie
3.GET	http://tasks.libertagia.com/		结果:更新cookie
***************************/

/**
 * 
 * 功能:		初始化任务数据,启动自动任务线程
 * 筛选状态:	
 * @author Carl.Huang
 *
 */
public class InitTaskThread extends RequestThread {

	public InitTaskThread(MainController controller, UserInfo userInfo) {
		super(controller, userInfo);
	}

	public InitTaskThread(MainController controller, UserInfo userInfo,
			int delay, int tryTimes) {
		super(controller, userInfo, delay, tryTimes);
	}

	@Override
	public void threadTask() {
		try {
			// 1.GET http://libertagia.com/office/tasks/ 结果:取得secret参数值
			//启动任务,置状态任务中.
			showInfo("开始执行任务初始化....");
			updateUserInfo(UserMessage.UserStatus.IN_INIT_TASK,
					UserMessage.UserProgress.IN_TASK);
			String result = Request.getURLResult(userInfo, Request.task);
			if (ParseHtml.isCompletedTask(result)) {
				updateUserInfo(UserMessage.UserStatus.DONE_TASK,
						UserMessage.UserProgress.IS_LOGIN);
				showInfo("任务已完成...");
				return;
			}
			String token = ParseHtml.getSecretParam(result);
			if (token == null) {
				//获取任务参数失败,置状态未登录.
				updateUserInfo(UserMessage.UserStatus.FAIL_INIT_TASK,
						UserMessage.UserProgress.NO_LOGIN);
				showError("初始化任务失败,请检查账户信息.");
				return;
			}
			
			// 2.POST http://tasks.libertagia.com/ 参数:secret 结果:更新cookie
			Map<String, String> data = new HashMap<String, String>();
			data.put("secret", token);
			result = Request.postURLResult(userInfo, Request.task_run, data,
					null, true);
			if (!ParseHtml.verifyLoginStatus(result)) {
				showError("任务失败...丢失登录状态...");
				//任务失败,置状态未登录
				updateUserInfo(UserMessage.UserStatus.FAIL_INIT_TASK,
						UserMessage.UserProgress.NO_LOGIN);
				return;
			}
			// 3.GET http://tasks.libertagia.com/ 结果:更新cookie
			result = Request.getURLResult(userInfo, Request.task_run);
			if (!ParseHtml.verifyLoginStatus(result)) {
				showError("任务失败...丢失登录状态...");
				//任务失败,置状态未登录
				updateUserInfo(UserMessage.UserStatus.FAIL_INIT_TASK,
						UserMessage.UserProgress.NO_LOGIN);
				return;
			}
			showInfo("初始化任务成功...开始进行任务....");
			updateUserInfo(UserMessage.UserStatus.IN_TASK,
					UserMessage.UserProgress.IN_TASK);
			new RunTaskThread(controller, userInfo).start();
			
		} catch (IOException e) {
			e.printStackTrace();
			updateUserInfo(UserMessage.UserStatus.FAIL_TASK,
					UserMessage.UserProgress.IS_LOGIN);
			showError("初始化任务发生错误,请检查网络....");
//			if (tryTimes <= ThreadMessage.MAX_TRY_TIME) {
//				showError("初始化任务发生错误,请检查网络..程序将在2秒后重试...");
//				new InitTaskThread(controller, userInfo, 2000, tryTimes + 1).start();
//			} else {
//				showError("初始化任务发生错误,请检查网络..程序已重试超过"
//						+ ThreadMessage.MAX_TRY_TIME + "次,将停止本次操作....");
//			}
		}
	}
}
