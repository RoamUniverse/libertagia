package com.carl.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.carl.message.LogLevel;
import com.carl.pojo.UserInfo;
import com.carl.thread.RequestThread;
import com.carl.window.MainWindow;

/**
 * 主控制器
 * 
 * @author carl.huang
 * 
 */
public class MainController {
	// 持有账户状态信息
	private List<UserInfo> infos = new ArrayList<UserInfo>();
	// 持有主窗口
	private MainWindow window;
	// 系统换行符
	private String lineSeparator = System.getProperty("line.separator", "\n");

	/*
	 * 根据UserInfo容器中的编号取得账户
	 */
	public void getAccountByIndex(int index) {

	}

	/*
	 * 根据编号取得验证码图片
	 */
	public void getCaptchaByIndex(int index) {
		
	}

	/*
	 * 从文件读入账户信息 用于初始化账户信息
	 */
	public void loadAccountFile(String path) {
		this.showLogs(LogLevel.LOG_INFO, "收到载入账户文件消息.正在确认文件....");
		File accountFile = new File(path);
		if (!accountFile.exists()) {
			this.showMessage("文件不存在,请重新选择.");
			return;
		}
		try {
			this.showLogs(LogLevel.LOG_INFO, "文件已确认,正在读取数据....");
			List<String> context = FileUtils.readLines(accountFile);
			this.parseAccountFile(context);
		} catch (IOException e) {
			this.showMessage("文件读取失败,请重试.");
			e.printStackTrace();
		}
	}

	private void parseAccountFile(List<String> context) {
		this.showLogs(LogLevel.LOG_INFO, "文件已读取完毕,正在解析数据....");
		int success = 0, fail = 0, count = context.size();
		for (String account : context) {
			String[] tmp = account.split(" ");
			if (tmp.length != 2) {
				fail++;
			} else {
				infos.add(new UserInfo(tmp[0], tmp[1]));
				success++;
			}
		}
		if (fail != 0) {
			this.window.clearTable();
			this.showMessage(String.format("解析结果: 总数 %d , 成功 %d , 失败 %d",
					count, success, fail));
		} else {
			this.window.showAllAccountInTable(infos);
			this.showLogs(LogLevel.LOG_INFO, String.format(
					"解析结果: 总数 %d , 成功 %d , 失败 %d", count, success, fail));
		}
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
//		UserInfo u = infos.get(index);
		
	}
	
	public void nextAccount() {
		for (UserInfo u : infos) {
			if("未操作".equals(u.getStatus())){
				window.setCaptchaAndAccount(u);
				return;
			}
		}
		this.showLogs(LogLevel.LOG_INFO, "无账户需要初始化.");
	}
	
	/*
	 * 初始化所有账户数据
	 */
	public void initAllAccount() {
		for (UserInfo u : infos) {
			new RequestThread(this,u).start();
		}
	}

	/*
	 * 将UserInfo对象序列化至硬盘
	 */
	public void saveFileForCookies(String path) {

	}

	/*
	 * 回调页面,显示信息
	 */
	public void showMessage(String msg) {
		this.showLogs(LogLevel.LOG_ERROR, msg);
		this.window.showMessage(msg);
	}

	/*
	 * 回调页面,显示日志
	 */
	public void showLogs(String level, String log) {
		this.window.showLogs(level + log + lineSeparator);
	}

	/*
	 * 更新表格数据
	 */
	public void updateTable(UserInfo userInfo) {
		
	}
	public List<UserInfo> getInfos() {
		return infos;
	}

	public MainWindow getWindow() {
		return window;
	}

	public void setWindow(MainWindow window) {
		this.window = window;
	}
}
