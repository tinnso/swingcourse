package com.diaryclient.usermgr;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.diaryclient.datamgr.StaticDataManager;

public class UserProfileFrame extends JFrame {

	public UserProfileFrame(boolean ismodify) {
		this.setBounds(0, 0, 400, 600);
		this.setLayout(null);

		// �����С���ܸı�
		this.setResizable(false);

		// ������ʾ
		this.setLocationRelativeTo(null);

		// ����ɼ�
		this.setVisible(true);

		this.addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
			
				StaticDataManager.pop();
			}
		});

		
		// �û������¼������Աߵ�����
        JLabel lblname = new JLabel("����");
        lblname.setBounds(20, 20, 50, 20);
        this.add(lblname);
        
        // �û������¼�����
		JTextField txtname = new JTextField();
		txtname.setBounds(70, 20, 150, 20);
        this.add(txtname);
		
		
		// �û������¼������Աߵ�����
        JLabel jl1 = new JLabel("�û���");
        jl1.setBounds(20, 70, 50, 20);
        this.add(jl1);
        
        // �û������¼�����
		JTextField username = new JTextField();
        username.setBounds(70, 70, 150, 20);
        this.add(username);
        
        // ����������Աߵ�����
        JLabel jl2 = new JLabel("����");
        jl2.setBounds(20, 150, 50, 20);
        this.add(jl2);
 
        // ���������
        JPasswordField password = new JPasswordField();
        password.setBounds(70, 150, 150, 20);
        this.add(password);
        
        // ����������Աߵ�����
        JLabel jl3 = new JLabel("ȷ��");
        jl3.setBounds(20, 200, 50, 20);
        this.add(jl3);
 
        // ���������
        JPasswordField password2 = new JPasswordField();
        password2.setBounds(70, 200, 150, 20);
        this.add(password2);
        
        // ��ť�趨
        
        String text = "��½";
        if (ismodify) text = "�޸�";
        JButton bu3 = new JButton(text);
        bu3.setBounds(100, 250, 65, 20);
        this.add(bu3);
	}
}
