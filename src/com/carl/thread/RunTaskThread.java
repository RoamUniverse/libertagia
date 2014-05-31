package com.carl.thread;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.carl.controller.MainController;
import com.carl.http.ParseHtml;
import com.carl.http.Request;
import com.carl.pojo.UserInfo;


/*********任务开始***********
1.POST	http://tasks.libertagia.com/index.php	参数:action=verifyStatus		结果:得到JOSN结果,其中有下一个任务token,已完成的任务token,完成任务标示
2.POST	http://tasks.libertagia.com/			参数:action=view	code=下一个任务token	结果:取得任务进行页面 captcha
3.POST	http://tasks.libertagia.com/index.php	参数:result=计算结果&action=finishTask	结果:true or false
*********任务结束(进入递归)***********/
public class RunTaskThread extends RequestThread {
	private int taskNO ;
	public RunTaskThread(MainController controller, UserInfo userInfo) {
		super(controller, userInfo);
	}
	
	public RunTaskThread(MainController controller, UserInfo userInfo,
			int delay, int tryTimes) {
		super(controller, userInfo, delay, tryTimes);
	}

	public RunTaskThread(MainController controller, UserInfo userInfo,
			int delay, int tryTimes, int taskNO) {
		super(controller, userInfo, delay, tryTimes);
		this.taskNO = taskNO;
	}

	@Override
	public void threadTask() {
		try {
			// 1.POST http://tasks.libertagia.com/index.php 参数:action=verifyStatus
			// 结果:得到JOSN结果,其中有下一个任务token,已完成的任务token,完成任务标示
			Map<String, String> data = new HashMap<String, String>();
			data.put("action", "verifyStatus");
			String result = Request.postURLResult(userInfo, Request.task_run_index, data, null, false);
			boolean done = ParseHtml.isDoneTask(result);
			if(done){
				//TODO任务已完成
				System.out.println("完成!!!!!!!!!!!!!!!!!!");
				System.out.println("完成!!!!!!!!!!!!!!!!!!");
				System.out.println("完成!!!!!!!!!!!!!!!!!!");
				System.out.println("完成!!!!!!!!!!!!!!!!!!");
				System.out.println("完成!!!!!!!!!!!!!!!!!!");
				return;
			}
			taskNO = ParseHtml.getDoneTaskCount(result);
			String token = ParseHtml.getNextTask(result);
			
			// 2.POST http://tasks.libertagia.com/ 参数:action=view code=下一个任务token
			// 结果:取得任务进行页面 captcha
			data = new HashMap<String, String>();
			data.put("action", "view");
			data.put("code", token);
			result = Request.postURLResult(userInfo, Request.task_run, data, null, false);
			//System.out.println(result);
			int r = ParseHtml.getTaskCaptcha(result);
			// 3.POST http://tasks.libertagia.com/index.php
			// 参数:result=计算结果&action=finishTask 结果:true or false
			data = new HashMap<String, String>();
			data.put("result", r+"");
			data.put("action", "finishTask");
			result = Request.postURLResult(userInfo, Request.task_run_index, data, null, false);
			new RunTaskThread(controller, userInfo).start();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
