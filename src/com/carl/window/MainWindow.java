package com.carl.window;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.carl.controller.MainController;
import com.carl.pojo.UserInfo;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MainWindow extends JFrame {
	private static final long serialVersionUID = 1L;
	// 内容面板
	private JPanel contentPane;
	// 账户表格
	private JTable table;
	// 验证码图片标签
	private JLabel captcha;
	// 验证码输入框
	private JTextField captchaText;
	// 载入按钮
	private JButton btnLoadAccount;
	// 控制器
	private MainController controller;
	// 日志输出框
	private JTextArea logTextArea;
	// 表模型
	private DefaultTableModel model;
	// 用户名标签
	private JLabel accountLabel;
	// 初始化按钮
	private JButton BtnInit;
	// 反序列化按钮
	private JButton btnLoadStatus;
	// 序列化按钮
	private JButton btnSaveStatus;
	// 登录按钮
	private JButton btnLogin;
	// 当前用户
	private UserInfo currentUserInfo;
	// 输出滚动条
	private JScrollPane scrollOutput;
	// 当前账户标签
	private JLabel currentLabel;
	// 下个账户按钮
	private JButton nextBtn;
	private JPanel panelAccountOperation;
	private JLabel lblCaptcha;
	private JPanel panelDataOperation;
	private JButton btnStartTask;
	private JButton btnOpenAutoStay;
	private JLabel lblOperation;
	private JLabel lblThread;
	private JLabel lblThreadCount;

	public MainWindow() {
		init();
	}

	private void init() {
		setTitle("< Auto - Task > By: Carl.Huang QQ:284642743");
		setBackground(Color.WHITE);
		// UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 946, 499);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		initAccountsTable();
		initAccountOperation();
		initDataOperation();
		initOutput();
		initEvents();
	}

	/*
	 * 界面构建,账户信息表格
	 */
	private void initAccountsTable() {
		String[] name = { "username", "status" };
		model = new DefaultTableModel(null, name);
		table = new JTable(model);
		table.setFont(new Font("Segoe UI", Font.BOLD, 13));
		table.setEnabled(false);
		table.setRowHeight(25);
		table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 10));
		JScrollPane scrollPaneTable = new JScrollPane(table);
		scrollPaneTable.setBounds(10, 11, 276, 455);
		contentPane.add(scrollPaneTable);
	}

	/*
	 * 界面构建,信息输出框
	 */
	private void initOutput() {
		// 多行文本框
		logTextArea = new JTextArea();
		logTextArea.setTabSize(2);
		logTextArea.setWrapStyleWord(true);
		logTextArea.setEditable(false);
		logTextArea.setLineWrap(true);
//		logTextArea.setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));
		logTextArea.setBounds(0, 0, 628, 283);
		// 滚动面板
		scrollOutput = new JScrollPane(logTextArea);
		scrollOutput.setBounds(298, 183, 628, 283);
		scrollOutput.setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));
		contentPane.add(scrollOutput);
//		scrollOutput.add(logTextArea);
	}

	/*
	 * 界面构建,数据操作组
	 */
	private void initDataOperation() {
		panelDataOperation = new JPanel();
		panelDataOperation.setBounds(665, 11, 261, 160);
		panelDataOperation.setBorder(BorderFactory.createLineBorder(
				Color.BLACK, 3));
		panelDataOperation.setLayout(null);
		contentPane.add(panelDataOperation);

		BtnInit = new JButton("Init");
		BtnInit.setBackground(Color.WHITE);
		BtnInit.setFont(new Font("Dialog", Font.BOLD, 13));
		BtnInit.setBounds(135, 13, 117, 36);
//		panelDataOperation.add(BtnInit);

		btnLoadAccount = new JButton("LoadAccount");
		btnLoadAccount.setFont(new Font("Dialog", Font.BOLD, 13));
		btnLoadAccount.setBounds(9, 13, 117, 36);
		panelDataOperation.add(btnLoadAccount);

		btnSaveStatus = new JButton("SaveStatus");
		btnSaveStatus.setFont(new Font("Dialog", Font.BOLD, 13));
		btnSaveStatus.setBackground(Color.WHITE);
		btnSaveStatus.setBounds(138, 13, 117, 36);
		panelDataOperation.add(btnSaveStatus);

		btnLoadStatus = new JButton("LoadStatus");
		btnLoadStatus.setFont(new Font("Segoe UI", Font.BOLD, 13));
		btnLoadStatus.setBounds(9, 62, 117, 36);
		panelDataOperation.add(btnLoadStatus);

		btnStartTask = new JButton("StartTask");
		btnStartTask.setFont(new Font("Dialog", Font.BOLD, 13));
		btnStartTask.setBounds(9, 111, 117, 36);
		panelDataOperation.add(btnStartTask);

		btnOpenAutoStay = new JButton("OpenAutoStay");
		btnOpenAutoStay.setFont(new Font("Dialog", Font.BOLD, 13));
		btnOpenAutoStay.setBounds(138, 62, 117, 36);
		panelDataOperation.add(btnOpenAutoStay);
		
		lblThread = new JLabel("Thread:");
		lblThread.setBounds(138, 120, 61, 16);
		lblThread.setFont(new Font("Dialog", Font.BOLD, 13));
		panelDataOperation.add(lblThread);
		
		lblThreadCount = new JLabel("0");
		lblThreadCount.setFont(new Font("Dialog", Font.BOLD, 13));
		lblThreadCount.setBounds(200, 120, 39, 16);
		panelDataOperation.add(lblThreadCount);
	}

	/*
	 * 界面构建,账户操作组
	 */
	private void initAccountOperation() {
		panelAccountOperation = new JPanel();
		panelAccountOperation.setBounds(298, 11, 355, 160);
		panelAccountOperation.setLayout(null);
		panelAccountOperation.setBorder(BorderFactory.createLineBorder(
				Color.BLACK, 3));
		contentPane.add(panelAccountOperation);

		// 当前账户输出
		accountLabel = new JLabel("");
		accountLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
		accountLabel.setBounds(85, 15, 249, 43);
		accountLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		panelAccountOperation.add(accountLabel);

		// 验证码图片
		captcha = new JLabel();
		captcha.setBounds(85, 66, 120, 43);
		captcha.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		panelAccountOperation.add(captcha);

		// 验证码输入框
		captchaText = new JTextField(7);
		captchaText.setEnabled(false);
		captchaText.setBounds(217, 66, 120, 43);
		panelAccountOperation.add(captchaText);

		// 当前账户标签
		currentLabel = new JLabel("Account :");
		currentLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
		currentLabel.setBounds(6, 26, 68, 26);
		panelAccountOperation.add(currentLabel);

		// 下个账户按钮
		nextBtn = new JButton("Next");
		nextBtn.setBackground(Color.WHITE);
		nextBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
		nextBtn.setBounds(85, 118, 117, 36);
		panelAccountOperation.add(nextBtn);

		// 验证码标签
		lblCaptcha = new JLabel("Captcha:");
		lblCaptcha.setFont(new Font("Dialog", Font.BOLD, 13));
		lblCaptcha.setBounds(6, 74, 68, 26);
		panelAccountOperation.add(lblCaptcha);

		// 登录按钮
		btnLogin = new JButton("Login");
		btnLogin.setEnabled(false);
		btnLogin.setFont(new Font("Dialog", Font.BOLD, 13));
		btnLogin.setBackground(Color.WHITE);
		btnLogin.setBounds(217, 118, 117, 36);
		panelAccountOperation.add(btnLogin);

		lblOperation = new JLabel("Operation:");
		lblOperation.setFont(new Font("Dialog", Font.BOLD, 13));
		lblOperation.setBounds(6, 122, 85, 26);
		panelAccountOperation.add(lblOperation);

	}

	/*
	 * 初始化事件
	 */
	private void initEvents() {
		/*************** 控件事件代码 *****************/
		//验证码输入框回车事件
		captchaText.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode()==10){
					btnLogin.doClick();
				}
			}
		});
		
		// loadAccount按钮事件
		btnLoadAccount.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileChooser = new JFileChooser(); // 对话框
				int i = fileChooser.showOpenDialog(getContentPane()); // opendialog
				if (i == JFileChooser.APPROVE_OPTION) // 判断是否为打开的按钮
				{
					File selectedFile = fileChooser.getSelectedFile(); // 取得选中的文件
					String path = selectedFile.getPath();
					loadAccountFile(path);
				}
			}
		});

		// 下个账户按钮事件
		nextBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.nextAccount();
			}
		});

		// 登录按钮事件
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String code = captchaText.getText();
				if (!"".equals(code)) {
					controller.Login(currentUserInfo, code);
					captchaText.setText("");
					captcha.setIcon(null);
					accountLabel.setText("");
					nextBtn.doClick();
					return;
				}
				showMessage("验证码为空,请重试.");
			}
		});
		// 初始化按钮事件
		BtnInit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.initAllAccount();
			}
		});

		// 序列化按钮事件
		btnSaveStatus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser(); // 对话框
				int i = fileChooser.showSaveDialog(getContentPane()); // opendialog
				if (i == JFileChooser.APPROVE_OPTION) // 判断是否为打开的按钮
				{
					File selectedFile = fileChooser.getSelectedFile(); // 取得选中的文件
					String path = selectedFile.getPath();
					saveSerializableFile(path);
				}
			}
		});

		// 反序列化按钮事件
		btnLoadStatus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser(); // 对话框
				int i = fileChooser.showOpenDialog(getContentPane());
				if (i == JFileChooser.APPROVE_OPTION) // 判断是否为打开的按钮
				{
					File selectedFile = fileChooser.getSelectedFile(); // 取得选中的文件
					String path = selectedFile.getPath();
					loadSerializableFile(path);
				}
			}
		});
		
		//开始任务
		btnStartTask.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.startTask();
			}
		});
		
		btnOpenAutoStay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//全局自动任务线程
				String title = btnOpenAutoStay.getText();
				if ("OpenAutoStay".equals(title)) {
					btnOpenAutoStay.setText("StopAutoStay");
					btnOpenAutoStay.setForeground(Color.RED);
					controller.runGlobalThread();
					setAllEnable(false);
				}else {
					btnOpenAutoStay.setText("OpenAutoStay");
					btnOpenAutoStay.setForeground(Color.BLACK);
					controller.shutdownGlobalThread();
					setAllEnable(true);
				}
			}
		});
		/******************************************/
	}

	/*
	 * 设置账户信息取验证码
	 */
	public void setCaptchaAndAccount(UserInfo userInfo) {
		currentUserInfo = userInfo;
		accountLabel.setText(userInfo.getUsername());
		captcha.setIcon(new ImageIcon(userInfo.getCaptcha()));
		captcha.repaint();
		btnLogin.setEnabled(true);
		captchaText.setEnabled(true);
	}

	/*
	 * 取得所有账户信息,并显示到表格中 根据表格行数置账户编号属性
	 */
	public void showAllAccountInTable(List<UserInfo> infos) {
		String[][] data = new String[infos.size()][2];
		for (int j = 0; j < infos.size(); j++) {
			UserInfo u = infos.get(j);
			u.setRowIndex(j);
			data[j] = new String[] { u.getUsername(), u.getStatus() };
		}
		String[] head = { "username", "status" };
		model = new DefaultTableModel(data, head);
		table.setModel(model);
		((DefaultTableModel) table.getModel()).fireTableDataChanged();
		// ((DefaultTableModel)table.getModel()).fireTableStructureChanged();;
	}

	/*
	 * 更新单个账户信息
	 */
	public void showAccountInTable(UserInfo userInfo) {
		model.setValueAt(userInfo.getStatus(), userInfo.getRowIndex(), 1);
		table.invalidate();
	}

	/*
	 * 清除表格数据
	 */
	public void clearTable() {
		model.setRowCount(0);
	}

	/*
	 * 全局监视组件激活禁用
	 */
	public void setAllEnable(boolean b) {
		captchaText.setEnabled(b);
		nextBtn.setEnabled(b);
		btnLoadAccount.setEnabled(b);
		btnLoadStatus.setEnabled(b);
		btnLogin.setEnabled(b);
		btnStartTask.setEnabled(b);
		btnSaveStatus.setEnabled(b);
	}
	/*
	 * 向控制器发送导入账户文件请求
	 */
	private void loadAccountFile(String path) {
		controller.loadAccountFile(path);
	}

	/*
	 * 向控制器发送反序列化请求
	 */
	private void loadSerializableFile(String path) {
		controller.loadSerializableFile(path);
	}

	/*
	 * 向控制器发送序列化请求
	 */
	private void saveSerializableFile(String path) {
		controller.saveSerializableFile(path);
	}

	/*
	 * 输出日志
	 */
	public void showLogs(String log) {
		if (logTextArea.getLineCount()> 100) {
			logTextArea.setText(null);
		}
		logTextArea.append(log);
		JScrollBar bar = scrollOutput.getVerticalScrollBar();
		bar.setValue(bar.getMaximum());
	}

	/*
	 * 弹出警告框
	 */
	public void showMessage(String msg) {
		JOptionPane.showMessageDialog(null, msg, "警告",
				JOptionPane.PLAIN_MESSAGE);
	}
	public synchronized void updateThread(int count,boolean isGlobalRun) {
		if (!isGlobalRun) {
			if (count==0) {
				btnOpenAutoStay.setEnabled(true);
			}else {
				btnOpenAutoStay.setEnabled(false);
			}
		}
		lblThreadCount.setText(""+count);
	}
	/*
	 * 禁用登录按钮
	 */
	public void setLoginBtnAble(boolean b) {
		btnLogin.setEnabled(b);
		captchaText.setEnabled(b);
		
	}
	public void setController(MainController controller) {
		this.controller = controller;
	}

}
