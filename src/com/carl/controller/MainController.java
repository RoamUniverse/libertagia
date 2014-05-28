package com.carl.controller;

import java.util.List;

import com.carl.pojo.UserInfo;
import com.carl.window.MainWindow;

/**
 * 主控制器
 * 
 * @author carl.huang
 * 
 */
public class MainController {
	// 持有账户状态信息
	private List<UserInfo> infos;
	// 持有主窗口
	private MainWindow window;

	/*
	 * 根据UserInfo容器中的编号取得账户
	 */
	public void getAccountByIndex(int index) {

	}

	/*
	 * 从文件读入账户信息 用于初始化账户信息
	 */
	public void loadAccounts(String path) {

	}

	/*
	 * 从硬盘反序列化得到UserInfo对象
	 */
	public void loadFileForCookies(String path) {

	}

	/*
	 * 收到用户输入的验证码
	 */
	public void pushCaotcha(int index, String captcha) {
		// TODO 启动线程 进行登陆cookie获取
	}

	/*
	 * 将UserInfo对象序列化至硬盘
	 */
	public void saveFileForCookies(String path) {

	}
	
	/*
	 * 回调页面,显示信息
	 */
	private void showMessage(String msg) {

	}
	
	/*
	 * 回调页面,显示日志
	 */
	private void showLogs(String log) {
		
	}
	public List<UserInfo> getInfos() {
		return infos;
	}

	public MainWindow getWindow() {
		return window;
	}
}
