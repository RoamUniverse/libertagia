package com.carl.message;

public class UserMessage {
	/**
	 * 导入账户||恢复状态 -> 未登录 <- initThread线程识别
	 * 用户手动登录后状态 || 任务完成后状态 -> 已登录 <- initTaskThread线程识别
	 * initTaskThread线程执行中状态 -> 任务中
	 * 工作流:	未登录->初始化完成->已登录->任务中->已登录
	 * @author Carl.Huang
	 *
	 */
	public static class UserProgress {
		// 未登录	导入账户文件后状态 || 状态恢复验证后状态
		//			initThread线程任务识别.(若未登录,则初始化数据.若已登录,则修改状态至已登录)
		public static final int NO_LOGIN = 0;
		// 已登录	用户手动登录后状态 || 任务完成后状态
		//			initTaskThread线程识别.(若未完成任务,修改状态至任务中并启动自动任务线程.若已完成,则修改状态至已登录)
		//TODO		添加全局自动任务线程注释.(全局自动任务线程,识别已登录状态账户,自动启动initTaskThread线程)			
		public static final int IS_LOGIN = 1;
		// 任务中	initTaskThread线程执行中状态.
		//			无线程识别
		public static final int IN_TASK = 2;	
		// 初始化完成	仅用户手动点击下一个账户按钮时识别.
		public static final int DONE_INIT = 3;
		public static final int IN_INIT = 4;
		public static final int IN_LOGIN = 5;
		
	}

	public static class UserStatus {
		public static final String NO_LOGIN = "未登录";
		public static final String IN_LOGIN = "登录中";
		public static final String IS_LOGIN = "已登录";
		public static final String FAIL_LOGIN = "登录失败";
		public static final String IN_INIT = "初始化中";
		public static final String DONE_INIT = "初始化完成";
		public static final String FAIL_INIT = "初始化失败";
		public static final String IN_VERIFY = "验证中";
		public static final String FAIL_VARIFY = "验证失败";
		public static final String IN_INIT_TASK = "初始化任务中";
		public static final String FAIL_INIT_TASK = "初始化任务失败";
		public static final String DONE_INIT_TASK = "初始化任务完成";
		public static final String IN_TASK = "任务进行中";
		public static final String DONE_TASK = "任务完成";
		public static final String FAIL_TASK = "任务失败";
	}
}
