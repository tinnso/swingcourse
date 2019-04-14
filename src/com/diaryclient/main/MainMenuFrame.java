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
		
		
		
		this.setSize(300, 300);// �贰��Ĵ�С ��͸�
		this.setLayout(null);

		// �����С���ܸı�
		this.setResizable(false);

		// ������ʾ
		this.setLocationRelativeTo(null);
		
		JButton btnuser = new JButton("������Ϣ�޸�");
		btnuser.setBounds(40, 20, 200, 30);
		this.add(btnuser);
		
		btnuser.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				UserEditFrame profile = new UserEditFrame(StaticDataManager.getUID());
				StaticDataManager.push(profile);
			}
			
		});
		
		JButton btndiary = new JButton("�ռǵ�¼");
		btndiary.setBounds(40, 60, 200, 30);
		this.add(btndiary);
		
		btndiary.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				DiaryEditFrame diary = new DiaryEditFrame(null);
				StaticDataManager.push(diary);
			}
			
		});
		
		JButton btndiarymgr = new JButton("�ռǹ���");
		btndiarymgr.setBounds(40, 100, 200, 30);
		this.add(btndiarymgr);
		
		btndiarymgr.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				DiaryListFrame diary = new DiaryListFrame();
				StaticDataManager.push(diary);
			}
			
		});
		
		
		JButton btnremind = new JButton("����¼");
		btnremind.setBounds(40, 140, 200, 30);
		this.add(btnremind);
		btnremind.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ReminderListFrame frame = new ReminderListFrame();
				
				StaticDataManager.push(frame);
			}
			
		});
		
		
		JButton btnback = new JButton("����");
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
