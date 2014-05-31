package com.carl.thread;

import com.carl.controller.MainController;
import com.carl.pojo.UserInfo;

public abstract class RequestThread extends Thread {
	protected MainController controller;
	protected UserInfo userInfo;
	// 启动延迟时间
	protected int delay;
	// 重试次数
	protected int tryTimes = 1;

	public RequestThread(MainController controller, UserInfo userInfo,
			int delay, int tryTimes) {
		super();
		this.controller = controller;
		this.userInfo = userInfo;
		this.delay = delay;
		this.tryTimes = tryTimes;
	}

	public RequestThread(MainController controller, UserInfo userInfo) {
		this(controller, userInfo, 0, 0);
	}

	/*
	 * 用户状态更新
	 */
	protected void updateUserInfo(String status,int inProgress) {
		userInfo.setStatus(status);
		userInfo.setInprogress(inProgress);
		controller.updateTable(userInfo);
	}
	/*
	 * 线程任务
	 */
	public abstract void threadTask();

	/*
	 * 输出反馈信息
	 */
	public void showLogs(boolean isError, String msg) {

		if (isError) {
			controller.showErrorLogs(msg);
		} else {
			controller.showInfoLogs(msg);
		}
	}

	@Override
	public synchronized void start() {
		try {
			Thread.sleep(delay);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		super.start();
	}

	public void run() {
		threadTask();
	}

	public void setController(MainController controller) {
		this.controller = controller;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}
}
