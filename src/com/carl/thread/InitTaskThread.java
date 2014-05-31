package com.carl.thread;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.carl.controller.MainController;
import com.carl.http.ParseHtml;
import com.carl.http.Request;
import com.carl.message.ThreadMessage;
import com.carl.message.UserMessage;
import com.carl.pojo.UserInfo;

/*********����׼��***********
1.GET	http://libertagia.com/office/tasks/	���:ȡ��secret����ֵ
2.POST	http://tasks.libertagia.com/		����:secret	���:����cookie
3.GET	http://tasks.libertagia.com/		���:����cookie
***************************/

/**
 * 
 * ����:		��ʼ����������,�����Զ������߳�
 * ɸѡ״̬:	
 * @author Carl.Huang
 *
 */
public class InitTaskThread extends RequestThread {

	public InitTaskThread(MainController controller, UserInfo userInfo) {
		super(controller, userInfo);
	}

	public InitTaskThread(MainController controller, UserInfo userInfo,
			int delay, int tryTimes) {
		super(controller, userInfo, delay, tryTimes);
	}

	@Override
	public void threadTask() {
		try {
			// 1.GET http://libertagia.com/office/tasks/ ���:ȡ��secret����ֵ
			//��������,��״̬������.
			showInfo("��ʼִ�������ʼ��....");
			updateUserInfo(UserMessage.UserStatus.IN_INIT_TASK,
					UserMessage.UserProgress.IN_TASK);
			String result = Request.getURLResult(userInfo, Request.task);
			if (ParseHtml.isCompletedTask(result)) {
				updateUserInfo(UserMessage.UserStatus.DONE_TASK,
						UserMessage.UserProgress.IS_LOGIN);
				showInfo("���������...");
				return;
			}
			String token = ParseHtml.getSecretParam(result);
			if (token == null) {
				//��ȡ�������ʧ��,��״̬δ��¼.
				updateUserInfo(UserMessage.UserStatus.FAIL_INIT_TASK,
						UserMessage.UserProgress.NO_LOGIN);
				showError("��ʼ������ʧ��,�����˻���Ϣ.");
				return;
			}
			
			// 2.POST http://tasks.libertagia.com/ ����:secret ���:����cookie
			Map<String, String> data = new HashMap<String, String>();
			data.put("secret", token);
			result = Request.postURLResult(userInfo, Request.task_run, data,
					null, true);
			if (!ParseHtml.verifyLoginStatus(result)) {
				showError("����ʧ��...��ʧ��¼״̬...");
				//����ʧ��,��״̬δ��¼
				updateUserInfo(UserMessage.UserStatus.FAIL_INIT_TASK,
						UserMessage.UserProgress.NO_LOGIN);
				return;
			}
			// 3.GET http://tasks.libertagia.com/ ���:����cookie
			result = Request.getURLResult(userInfo, Request.task_run);
			if (!ParseHtml.verifyLoginStatus(result)) {
				showError("����ʧ��...��ʧ��¼״̬...");
				//����ʧ��,��״̬δ��¼
				updateUserInfo(UserMessage.UserStatus.FAIL_INIT_TASK,
						UserMessage.UserProgress.NO_LOGIN);
				return;
			}
			showInfo("��ʼ������ɹ�...��ʼ��������....");
			updateUserInfo(UserMessage.UserStatus.IN_TASK,
					UserMessage.UserProgress.IN_TASK);
			new RunTaskThread(controller, userInfo).start();
			
		} catch (IOException e) {
			e.printStackTrace();
			updateUserInfo(UserMessage.UserStatus.FAIL_TASK,
					UserMessage.UserProgress.IS_LOGIN);
			if (tryTimes <= ThreadMessage.MAX_TRY_TIME) {
				showError("��ʼ������������,��������..������2�������...");
				new InitTaskThread(controller, userInfo, 2000, tryTimes + 1).start();
			} else {
				showError("��ʼ������������,��������..���������Գ���"
						+ ThreadMessage.MAX_TRY_TIME + "��,��ֹͣ���β���....");
			}
		}
	}
}