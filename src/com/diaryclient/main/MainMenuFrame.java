package com.diaryclient.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import com.diaryclient.datamgr.StaticDataManager;
import com.diaryclient.diarymgr.DiaryEditFrame;
import com.diaryclient.diarymgr.DiaryListFrame;
import com.diaryclient.remindermgr.ReminderListFrame;
import com.diaryclient.usermgr.UserEditFrame;

public class MainMenuFrame extends JFrame {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MainMenuFrame() {
		
		
		
		this.setSize(300, 300);// 设窗体的大小 宽和高
		this.setLayout(null);

		// 窗体大小不能改变
		this.setResizable(false);

		// 居中显示
		this.setLocationRelativeTo(null);
		
		JButton btnuser = new JButton("个人信息修改");
		btnuser.setBounds(40, 20, 200, 30);
		this.add(btnuser);
		
		btnuser.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				UserEditFrame profile = new UserEditFrame(StaticDataManager.getUID());
				StaticDataManager.push(profile);
			}
			
		});
		
		JButton btndiary = new JButton("日记登录");
		btndiary.setBounds(40, 60, 200, 30);
		this.add(btndiary);
		
		btndiary.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				DiaryEditFrame diary = new DiaryEditFrame(null);
				StaticDataManager.push(diary);
			}
			
		});
		
		JButton btndiarymgr = new JButton("日记管理");
		btndiarymgr.setBounds(40, 100, 200, 30);
		this.add(btndiarymgr);
		
		btndiarymgr.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				DiaryListFrame diary = new DiaryListFrame();
				StaticDataManager.push(diary);
			}
			
		});
		
		
		JButton btnremind = new JButton("备忘录");
		btnremind.setBounds(40, 140, 200, 30);
		this.add(btnremind);
		btnremind.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ReminderListFrame frame = new ReminderListFrame();
				
				StaticDataManager.push(frame);
			}
			
		});
		
		
		JButton btnback = new JButton("返回");
		btnback.setBounds(40, 180, 200, 30);
		this.add(btnback);
		
		btnback.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				StaticDataManager.pop();
			}
		});
		
		
		this.addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				StaticDataManager.pop();
			}
		});

	}

}
