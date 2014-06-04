package com.carl.http;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.regexp.RE;
import org.apache.regexp.RECompiler;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;

public class ParseHtml {
	/*
	 * 取得进入http://tasks.libertagia.com的POST参数
	 * 参数名:secret
	 * 解析方式:HtmlCleaner
	 * 目标:<input type="hidden" value="5386c7b1aa2a948b1492c545" name="secret">
	 */
	public static String getSecretParam(String result) {
		 HtmlCleaner cleaner = new HtmlCleaner();  
		 TagNode node = cleaner.clean(result);
		 String secret = null;
		 try {
			 Object[] ns = node.evaluateXPath("//input[@type='hidden']");
			 if(ns.length > 0){
				 secret = ((TagNode)ns[0]).getAttributeByName("value");
			 }
		} catch (XPatherException e) {
			e.printStackTrace();
		}
		return secret;
	}
	
	/*
	 * 取得json中下个对象的token
	 */
	public static String getNextTask(String result) {
		RE re = new RE(); // 新建正则表达式对象;
		RECompiler compiler = new RECompiler(); // 新建编译对象;
		re.setProgram(compiler.compile("\"next_task\":\"[\\d\\w=]+")); // 编译
		boolean bool = re.match(result); // 测试是否匹配;
		System.out.println(bool);
		if (bool) {
			String tmp = re.getParen(0);
			tmp = tmp.replaceFirst("\"next_task\":\"", "");
			return tmp;
		}
		return null;
	}
	
	public static int getTaskCaptcha(String result) {
		RE re = new RE(); // 新建正则表达式对象;
		RECompiler compiler = new RECompiler(); // 新建编译对象;
		re.setProgram(compiler
				.compile("<span id=\"cap_text\".*>\\d+\\s*.+\\s\\d+")); // 编译
		boolean bool = re.match(result); // 测试是否匹配;
		System.out.println(bool);
		if (bool) {
			String tmp = re.getParen(0).replaceAll(
					"<span id=\"cap_text\" class=\"input-group-addon\">", "");
			String[] a = tmp.split(" ");
			if (a.length != 3) {
				return 0;
			}
			if (a[1].equals("+")) {
				int one = Integer.parseInt(a[0]);
				int two = Integer.parseInt(a[2]);
				return one+two;
			}else if (a[1].equals("-")) {
				int one = Integer.parseInt(a[0]);
				int two = Integer.parseInt(a[2]);
				return one - two;
			}
			
		}
		return -1;
	}
	
	/*
	 * 获取json中已完成任务个数
	 */
	public static int getDoneTaskCount(String result) {
		RE re = new RE(); // 新建正则表达式对象;
		RECompiler compiler = new RECompiler(); // 新建编译对象;
		re.setProgram(compiler.compile("\\[.*,*\\]")); // 编译
		boolean bool = re.match(result); // 测试是否匹配;
		System.out.println(bool);
		if (bool) {
			String tmp = re.getParen(0);
			System.out.println(tmp);
			tmp = tmp.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\"", "");
			if ("".equals(tmp)) {
				return 1;
			}
			String[] a = tmp.split(",");
			System.out.println(a.length);
			return a.length+1;
		}
		return 0;
	}
	/*
	 * 获取json中是否已完成任务
	 */
	public static boolean isDoneTask(String result) {
		return !result.contains("\"finished\":false");
		
	}
	/*
	 * HTML判断任务是否完成
	 */
	public static boolean isCompletedTask(String result) {
		if(result.contains("Your daily tasks have been completed successfully"))
			return true;
		DateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		String today = f.format(new Date());
		HtmlCleaner cleaner = new HtmlCleaner();  
		 TagNode node = cleaner.clean(result);
		 try {
			 Object[] td = node.evaluateXPath("//table//tbody//tr//td[1]");
			 if(td.length == 0)
				 return false;
			 for (Object o : td) {
				String date = ((TagNode)o).getText().toString();
				if(today.equals(date))
					return true;
			}
		} catch (XPatherException e) {
			e.printStackTrace();
		}
		 return false;
	}
	/*
	 * 验证登录状态是否有效
	 */
	public static boolean verifyLoginStatus(String result ,String find) {
		return result.contains(find);
	}
	/*
	 * 验证登录状态是否有效
	 */
	public static boolean verifyLoginStatus(String result) {
		return result.contains("Welcome");
	}
}
