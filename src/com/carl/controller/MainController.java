package com.carl.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.carl.message.LogLevel;
import com.carl.message.UserMessage;
import com.carl.pojo.UserInfo;
import com.carl.thread.GlobalAutoStayThread;
import com.carl.thread.InitTaskThread;
import com.carl.thread.InitThread;
import com.carl.thread.LoginThread;
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
	//当前业务线程数
	private int threadCount = 0;
	//全局任务线程
	private GlobalAutoStayThread thread;
	
	/*
	 * 从文件读入账户信息 用于初始化账户信息
	 */
	public void loadAccountFile(String path) {
		this.showInfoLogs("收到载入账户文件消息.正在确认文件....");
		File accountFile = new File(path);
		if (!accountFile.exists()) {
			this.showMessage("文件不存在,请重新选择.");
			return;
		}
		try {
			this.showInfoLogs("文件已确认,正在读取数据....");
			List<String> context = FileUtils.readLines(accountFile);
			this.parseAccountFile(context);
			window.setBtnLoadStatus(false);
			window.setBtnVerifyStatus(false);
			window.setBtnImportAccountAble(false);
		} catch (IOException e) {
			this.showMessage("程序发生内部错误,请联系作者!!!");
			e.printStackTrace();
		}
	}

	/*
	 * 解析账户文件
	 */
	private void parseAccountFile(List<String> context) {
		this.showInfoLogs("文件已读取完毕,正在解析数据....");
		infos.clear();
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
			this.updateTable();
			this.showInfoLogs(String.format("解析结果: 总数 %d , 成功 %d , 失败 %d",
					count, success, fail));
		}
		//initAllAccount();
	}

	/*
	 * 从硬盘反序列化得到UserInfo对象
	 */
	@SuppressWarnings("unchecked")
	public void loadSerializableFile(String path) {
		showInfoLogs("开始读取状态文件....<PATH>:" + path);
		File load = new File(path);
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(new FileInputStream(load));
			infos = (List<UserInfo>) ois.readObject();
			ois.close();
			if(infos == null){
				infos = new ArrayList<UserInfo>();
			}
			this.updateTable();
			window.setBtnImportAccountAble(false);
			window.setBtnInitLogin(false);
			window.setBtnLoadStatus(false);
			//window.setBtnVerifyStatus(false);
			showInfoLogs("状态文件读取完毕....");
		} catch (FileNotFoundException e) {
			showMessage("文件不存在或不可读,请重试.");
			e.printStackTrace();
		} catch (IOException e) {
			showMessage("程序发生内部错误,请联系作者!!!");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			showMessage("程序发生内部错误,请联系作者!!!");
			e.printStackTrace();
		} finally {
			if (ois != null) {
				try {
					ois.close();
				} catch (IOException e) {
					showMessage("程序发生内部错误,请联系作者!!!");
					e.printStackTrace();
				}
			}
		}
	}

	/*
	 * 向硬盘写入UserInfo对象序列化文件
	 */
	public void saveSerializableFile(String path) {
		File save = new File(path);
		ObjectOutputStream oos = null;
		showInfoLogs("写入状态文件.... <PATH>:" + path);
		try {
			oos = new ObjectOutputStream(new FileOutputStream(save));
			oos.writeObject(infos);
			oos.flush();
			showInfoLogs("状态文件已保存...");
		} catch (FileNotFoundException e) {
			showMessage("文件不存在或不可读,请重试.");
			e.printStackTrace();
		} catch (IOException e) {
			showMessage("程序发生内部错误,请联系作者!!!");
			e.printStackTrace();
		} finally {
			if (oos != null) {
				try {
					oos.close();
				} catch (IOException e) {
					showMessage("程序发生内部错误,请联系作者!!!");
					e.printStackTrace();
				}
			}
		}
		infos = null;
	}

	/*
	 * 收到用户输入的验证码
	 */
	public void Login(UserInfo userInfo, String captcha) {
		RequestThread thread = new LoginThread(this, userInfo, captcha);
		thread.start();
	}

	/*
	 * 设置下个账户
	 */
	public void nextAccount() {
		for (UserInfo u : infos) {
			//筛选已完成初始化的账户
			if (u.getInprogress() == UserMessage.UserProgress.DONE_INIT) {
				window.setCaptchaAndAccount(u);
				return;
			}
		}
		//无账户则禁用
		window.setLoginBtnAble(false);
		this.showMessage("无账户需要登录.......");
		//initAllAccount();
	}
	
	/*
	 * 开启新线程
	 */
	public synchronized void threadRun(){
		threadCount += 1;
		window.updateThread(threadCount, (thread != null) ? thread.isAlive()
				: false);
	}
	/*
	 * 开启新线程
	 */
	public synchronized void threadDone(){
		threadCount -= 1;
		window.updateThread(threadCount, (thread != null) ? thread.isAlive()
				: false);
	}
	
	public void runGlobalThread() {
		thread = new GlobalAutoStayThread(this, null, true);
		thread.start();
	}
	public void shutdownGlobalThread() {
//		while(thread.isAlive()){
			thread.setStopRequest(false);
			window.lblNextCheck.setText("未启动");
//		}
	}
	
	public void updateNextCheck(int i) {
		window.lblNextCheck.setText(i+"秒");
	}
	/*
	 * 所有账户开始任务
	 */
	public void startTask() {
		for (UserInfo userInfo : infos) {
			if (userInfo.getInprogress()==UserMessage.UserProgress.IS_LOGIN) {
				new InitTaskThread(this, userInfo).start();
			}
		}
	}
	/*
	 * 初始化所有账户数据
	 */
	public void initAllAccount() {
		for (UserInfo userInfo : infos) {
			//筛选状态未未登录的账户
			if(userInfo.getInprogress()==UserMessage.UserProgress.NO_LOGIN){
				new InitThread(this, userInfo).start();
			}
		}
	}

	/*
	 * 回调页面,显示弹出框
	 */
	public void showMessage(String msg) {
		this.showErrorLogs(msg);
		this.window.showMessage(msg);
	}

	/*
	 * 回调页面,显示Info
	 */
	public void showInfoLogs(String log) {
		this.window.showLogs(LogLevel.LOG_INFO + log + lineSeparator);
	}

	/*
	 * 回调页面,显示Error
	 */
	public void showErrorLogs(String log) {
		this.window.showLogs(LogLevel.LOG_ERROR + log + lineSeparator);
	}

	/*
	 * 更新表格中单个用户数据
	 */
	public void updateTable(UserInfo userInfo) {
		window.showAccountInTable(userInfo);
	}

	/*
	 * 更新表格中所有用户数据
	 */
	public void updateTable() {
		window.showAllAccountInTable(infos);
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
