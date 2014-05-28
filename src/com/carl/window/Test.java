package com.carl.window;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class Test extends JFrame {

	private JPanel contentPane;
	private JTable table;
	private JTextField textField;

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
					Test frame = new Test();
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
	public Test() throws Exception {
		setBackground(Color.WHITE);
		UIManager
				.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 668, 573);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JButton nextBtn = new JButton("Next");
		nextBtn.setBackground(Color.WHITE);
		nextBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Next", "title",
						JOptionPane.PLAIN_MESSAGE);
			}
		});
		nextBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
		nextBtn.setBounds(137, 410, 117, 36);
		contentPane.add(nextBtn);

		JButton refreshBtn = new JButton("Refresh");
		refreshBtn.setBackground(Color.WHITE);
		refreshBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Refresh", "title",
						JOptionPane.PLAIN_MESSAGE);
			}
		});
		refreshBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
		refreshBtn.setBounds(10, 410, 117, 36);
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
		loadBtn.setBounds(264, 410, 117, 36);
		contentPane.add(loadBtn);

		String[] name = { "username", "password","status" };
		final DefaultTableModel model = new DefaultTableModel(null, name);
		JTable table = new JTable(model);
		table.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(new JPasswordField()));
		table.setFont(new Font("Segoe UI", Font.BOLD, 13));
		table.setEnabled(false);
		table.setRowHeight(25);
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(10, 11, 642, 381);
		contentPane.add(scrollPane);

		JButton test = new JButton("test");
		test.setBackground(Color.WHITE);
		test.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.addRow(new String[]{ "小芳", "142321" });
			}
		});
		test.setFont(new Font("Segoe UI", Font.BOLD, 13));
		test.setBounds(391, 410, 117, 36);
		contentPane.add(test);
		
		textField = new JTextField();
		textField.setBounds(10, 470, 117, 36);
		contentPane.add(textField);
		textField.setColumns(10);
	}
}
