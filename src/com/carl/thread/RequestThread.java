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

	// 系统换行符
	private String lineSeparator = System.getProperty("line.separator", "\n");
	
	public RequestThread(MainController controller, UserInfo userInfo,
			int delay, int tryTimes) {
		super();
		this.controller = controller;
		this.userInfo = userInfo;
		this.delay = delay;
		this.tryTimes = tryTimes;
	}

	public RequestThread(MainController controller, UserInfo userInfo) {
		this(controller, userInfo, 0, 1);
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
	 * 输出日志
	 */
	private void showLogs(boolean isError, String msg) {
		if (userInfo != null) {
			// msg = String.format("<Thread-ID:%d>\t账户:%s\t%s", this.getId(),
			// userInfo.getUsername(),msg);
			msg = String.format("账户:%s"+lineSeparator+"\t\t%s", userInfo.getUsername(), msg);
		} else {
			msg = String.format("%s", msg);
		}
		if (isError) {
			controller.showErrorLogs(msg);
		} else {
			controller.showInfoLogs(msg);
		}
	}
	/*
	 * 输出日志
	 */
	public void showError(String msg) {
		showLogs(true, msg);
	}
	/*
	 * 输出日志
	 */
	public void showInfo(String msg) {
		showLogs(false, msg);
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
		if(!this.getClass().equals(GlobalAutoStayThread.class)){
			controller.threadRun();
			threadTask();
			controller.threadDone();
		}else {
			threadTask();
		}
	}

	public void setController(MainController controller) {
		this.controller = controller;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}
}
