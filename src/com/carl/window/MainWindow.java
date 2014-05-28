package com.carl.window;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.carl.controller.MainController;
import com.carl.pojo.UserInfo;

public class MainWindow extends JFrame {

	// 内容面板
	private JPanel contentPane;
	// 账户表格
	private JTable table;
	// 验证码图片
	private JLabel captcha;
	// 验证码输入框
	private JTextField captchaText;
	// 刷新按钮
	private JButton initBtn;
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

	/**
	 * Launch the application.
	 * 
	 * @throws Exception
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws ClassNotFoundException
	 */
	public static void main(String[] args) throws Exception {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow frame = new MainWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * 
	 * @throws Exception
	 */
	public MainWindow() {
		setBackground(Color.WHITE);
		// UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 649, 585);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		initBtn = new JButton("\u521D\u59CB\u5316");
		initBtn.setBackground(Color.WHITE);
		initBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
		initBtn.setBounds(484, 25, 117, 36);
		contentPane.add(initBtn);

		String[] name = { "username", "status" };
		model = new DefaultTableModel(null, name);
		table = new JTable(model);
		table.setFont(new Font("Segoe UI", Font.BOLD, 13));
		table.setEnabled(false);
		table.setRowHeight(25);
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(10, 11, 429, 381);
		contentPane.add(scrollPane);
		captcha = new JLabel();
		captcha.setBounds(484, 147, 120, 43);
		captcha.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		contentPane.add(captcha);
		captchaText = new JTextField(20);
		captchaText.setBounds(484, 194, 117, 36);
		contentPane.add(captchaText);

		JLabel currentLabel = new JLabel("Account :");
		currentLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
		currentLabel.setBounds(484, 73, 117, 26);
		contentPane.add(currentLabel);

		JButton nextBtn = new JButton("Next");
		nextBtn.setBackground(Color.WHITE);

		nextBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
		nextBtn.setBounds(484, 242, 117, 36);
		contentPane.add(nextBtn);

		accountLabel = new JLabel("");
		accountLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
		accountLabel.setBounds(484, 110, 117, 26);
		accountLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		contentPane.add(accountLabel);

		btnLoadAccount = new JButton("LoadAccount");
		btnLoadAccount.setFont(new Font("Dialog", Font.BOLD, 13));
		btnLoadAccount.setBounds(484, 290, 117, 36);
		contentPane.add(btnLoadAccount);

		JLabel groupLabel = new JLabel("");
		groupLabel.setEnabled(false);
		groupLabel.setBounds(449, 11, 184, 381);
		groupLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		contentPane.add(groupLabel);

		logTextArea = new JTextArea();
		logTextArea.setLineWrap(true);
		logTextArea.setBounds(10, 413, 623, 133);

		JScrollPane scrollPane_1 = new JScrollPane(logTextArea);
		scrollPane_1.setBounds(10, 413, 623, 133);
		contentPane.add(scrollPane_1);

		JLabel lblNewLabel = new JLabel("Output:");
		lblNewLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
		lblNewLabel.setBounds(10, 395, 86, 14);

		/*************** 控件事件代码 *****************/

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

		// next按钮事件
		nextBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				JOptionPane.PLAIN_MESSAGE);
//				JOptionPane.showMessageDialog(null, "Next", "title",
				controller.nextAccount();
			}
		});

		// 初始化按钮事件
		initBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// model.addRow(new String[]{"test","????"});
				controller.initAllAccount();
			}
		});
		/******************************************/

		contentPane.add(lblNewLabel);
		// contentPane.setFocusTraversalPolicy(new FocusTraversalOnArray(new
		// Component[]{refreshBtn, table, captcha, captchaText, currentLabel,
		// nextBtn, accountLabel, scrollPane_1, lblNewLabel, groupLabel,
		// scrollPane}));
	}

	/*
	 * 设置账户信息取验证码
	 */
	public void setCaptchaAndAccount(UserInfo userInfo) {
		accountLabel.setText(userInfo.getUsername());
		captcha.setIcon(new ImageIcon(userInfo.getCaptcha()));
		captcha.repaint();
	}

	/*
	 * 取得所有账户信息,并显示到表格中 根据表格行数置账户编号属性
	 */
	public void showAllAccountInTable(List<UserInfo> infos) {
		// TODO 置账户编号
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

	public void clearTable() {
		model.setRowCount(0);
	}

	/*
	 * 向控制器发送导入账户文件请求
	 */
	private void loadAccountFile(String path) {
		controller.loadAccountFile(path);
	}

	/*
	 * 输出日志
	 */
	public void showLogs(String log) {
		logTextArea.append(log);
	}

	/*
	 * 弹出警告框
	 */
	public void showMessage(String msg) {
		JOptionPane.showMessageDialog(null, msg, "警告",
				JOptionPane.PLAIN_MESSAGE);
	}

	public void setController(MainController controller) {
		this.controller = controller;
	}
}
