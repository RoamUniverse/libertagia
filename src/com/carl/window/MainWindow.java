package com.carl.window;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

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
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.carl.pojo.UserInfo;

import javax.swing.JSeparator;
import javax.swing.JToolBar;
import javax.swing.JDesktopPane;
import javax.swing.JLayeredPane;
import javax.swing.JEditorPane;
import javax.swing.JTextArea;
import javax.swing.JScrollBar;

public class MainWindow extends JFrame {

	private JPanel contentPane;
	private JTable table;
	private JLabel captcha;
	private JTextField text;
	private Map<String, UserInfo> infos = new HashMap<String, UserInfo>();
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
	public MainWindow(){
		setBackground(Color.WHITE);
//		UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 649, 585);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JButton refreshBtn = new JButton("Refresh");
		refreshBtn.setBackground(Color.WHITE);
		refreshBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Refresh", "title",
						JOptionPane.PLAIN_MESSAGE);
			}
		});
		refreshBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
		refreshBtn.setBounds(484, 79, 117, 36);
		contentPane.add(refreshBtn);

		JButton loadBtn = new JButton("Load");
		loadBtn.setBackground(Color.WHITE);
		loadBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser(); // 对话框
				int i = fileChooser.showOpenDialog(getContentPane()); // opendialog
				if (i == JFileChooser.APPROVE_OPTION) // 判断是否为打开的按钮
				{
					File selectedFile = fileChooser.getSelectedFile(); // 取得选中的文件
					System.out.println(selectedFile.getPath());
				}
			}
		});
		loadBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
		loadBtn.setBounds(484, 126, 117, 36);
		contentPane.add(loadBtn);

		String[] name = { "username","status" };
		final DefaultTableModel model = new DefaultTableModel(null, name);
		table = new JTable(model);
		table.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(new JPasswordField()));
		table.setFont(new Font("Segoe UI", Font.BOLD, 13));
		table.setEnabled(false);
		table.setRowHeight(25);
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(10, 11, 429, 381);
		contentPane.add(scrollPane);

		JButton test = new JButton("test");
		test.setBackground(Color.WHITE);
		test.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println(text.getText());
				infos.get("1114486604@qq.com").login(text.getText());
				infos.get("1114486604@qq.com").getIndex();
			}
		});
		test.setFont(new Font("Segoe UI", Font.BOLD, 13));
		test.setBounds(484, 341, 117, 36);
		contentPane.add(test);
		captcha = new JLabel();
		captcha.setBounds(484, 247, 117, 36);
		captcha.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		contentPane.add(captcha);
		text = new JTextField(20);
		text.setBounds(484,294 , 117, 36);
		contentPane.add(text);
		
		JLabel currentLabel = new JLabel("Current Account :");
		currentLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
		currentLabel.setBounds(484, 173, 117, 26);
		contentPane.add(currentLabel);
		
				JButton nextBtn = new JButton("Next");
				nextBtn.setBackground(Color.WHITE);
				nextBtn.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						JOptionPane.showMessageDialog(null, "Next", "title",
								JOptionPane.PLAIN_MESSAGE);
					}
				});
				nextBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
				nextBtn.setBounds(484, 32, 117, 36);
				contentPane.add(nextBtn);
		
		JLabel accountLabel = new JLabel("");
		accountLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
		accountLabel.setBounds(484, 210, 117, 26);
		accountLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		contentPane.add(accountLabel);
		
		JLabel groupLabel = new JLabel("");
		groupLabel.setEnabled(false);
		groupLabel.setBounds(449, 11, 184, 381);
		groupLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		contentPane.add(groupLabel);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(10, 413, 623, 133);
		contentPane.add(scrollPane_1);
		
		JLabel lblNewLabel = new JLabel("Output:");
		lblNewLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
		lblNewLabel.setBounds(10, 395, 86, 14);
		contentPane.add(lblNewLabel);
	}
	public void setCaptcha(Image icon) {
		captcha.setIcon(new ImageIcon(icon));
		captcha.repaint();
	}

	public Map<String, UserInfo> getInfos() {
		return infos;
	}

	public void setInfos(Map<String, UserInfo> infos) {
		this.infos = infos;
	}
}
