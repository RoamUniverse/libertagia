package com.carl.http;

import org.apache.regexp.RE;
import org.apache.regexp.RECompiler;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

public class ParseHtml {
	/*
	 * 取得进入http://tasks.libertagia.com的POST参数
	 * 参数名:secret
	 * 解析方式:HtmlCleaner
	 * 目标:<input type="hidden" value="5386c7b1aa2a948b1492c545" name="secret">
	 */
	public static String getSecretParam(String result) {
		HtmlCleaner c = new HtmlCleaner();
		TagNode root =c.clean(result);
		TagNode[] t = root.getElementsByAttValue("name", "secret", true, false);
		for (TagNode tagNode : t) {
			return tagNode.getAttributeByName("value");
		}
		return null;
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
				return 0;
			}
			String[] a = tmp.split(",");
			System.out.println(a.length);
			return a.length;
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
		return result.contains("Your daily tasks have been completed successfully");
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
	public static void main(String[] args) {
		String s = "[\"ZWFmZDJkYmM5YTY3ZDQ0OGM1MWNhZWNkOGZmYTNiM2Z8ZDNkeEdldEI4RG1ZMWQ5Qw==\",\"ZWFmZDJkYmM5YTY3ZDQ0OGM1MWNhZWNkOGZmYTNiM2Z8V09Pc3JnTnNUTEdVZlQ3VA==\",\"ZWFmZDJkYmM5YTY3ZDQ0OGM1MWNhZWNkOGZmYTNiM2Z8ZmE1YjA1ZWJhMjRlOTk0Mg==\"]";
		RE re = new RE(); // 新建正则表达式对象;
		RECompiler compiler = new RECompiler(); // 新建编译对象;
		re.setProgram(compiler.compile("\\[.+,*\\]")); // 编译
		boolean bool = re.match(s); // 测试是否匹配;
		System.out.println(bool);
		if (bool) {
			String tmp = re.getParen(0);
			System.out.println(tmp);
			tmp = tmp.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\"", "");
			String[] a = tmp.split(",");
			System.out.println(a.length);
			for (String string : a) {
				System.out.println(string);
			}
		}
	}
}
