package com.carl.http;

import java.io.IOException;

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
	public String getSecretParam(String result) {
		HtmlCleaner c = new HtmlCleaner();
		TagNode root =c.clean(result);
		TagNode[] t = root.getElementsByAttValue("name", "secret", true, false);
		for (TagNode tagNode : t) {
			return tagNode.getAttributeByName("value");
		}
		return null;
	}
	public static void main(String[] args) throws IOException, XPatherException {
//		String result = Request.getURLResult("", "http://www.baidu.com");
//		System.out.println(result);
//		HtmlCleaner c = new HtmlCleaner();
//		TagNode node =c.clean(result);
//		TagNode[] t = node.getElementsByAttValue("id", "lk", true, false);
//		for (int i = 0; i < t.length; i++) {
//			System.out.println("i="+i+"\t"+ c.getInnerHtml(t[i]));
//		}
	}
}
