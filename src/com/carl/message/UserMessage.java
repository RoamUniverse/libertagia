package com.carl.message;

public class UserMessage {
	public static class UserProgress {
		public static final int NO_LOGIN = 0; // 未登录
		public static final int IS_LOGIN = 1;// 已登录
		public static final int IN_TASK = 2;// 任务中
		public static final int DONE_TASK = 3;// 任务完成
	}

	public static class UserStatus {
		public static final String NO_LOGIN = "未登录";
		public static final String IS_LOGIN = "已登录";
		public static final String IN_INIT = "初始化中";
		public static final String DONE_INIT = "初始化完成";
		public static final String IN_VERIFY = "验证中";
		public static final String FAIL_VARIFY = "验证失败";
		public static final String IN_INIT_TASK = "初始化任务中";
		public static final String FAIL_INIT_TASK = "初始化任务失败";
		public static final String DONE_INIT_TASK = "初始化任务完成";
		public static final String IN_TASK = "任务进行中";
		public static final String DENO_TASK = "任务完成";
		public static final String FAIL_TASK = "任务失败";
	}
}
