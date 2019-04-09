package com.diaryclient.usermgr;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import com.diaryclient.datamgr.StaticDataManager;

public class LoginFrame extends JFrame {

	private JTabbedPane tabbedPane;
	private int count = 0;
	
	public LoginFrame() {
		tabbedPane = new JTabbedPane();
		
		JPanel normalpanel = new JPanel();
		normalpanel.setLayout(null);
		normalpanel.setBackground(Color.LIGHT_GRAY);
		
		// �û������¼������Աߵ�����
        JLabel jl1 = new JLabel("�û���");
        jl1.setBounds(70, 70, 50, 20);
        normalpanel.add(jl1);
        
        // �û������¼�����
		JTextField username = new JTextField();
        username.setBounds(120, 70, 150, 20);
        normalpanel.add(username);
        
        // ����������Աߵ�����
        JLabel jl2 = new JLabel("����");
        jl2.setBounds(70, 100, 50, 20);
        normalpanel.add(jl2);
 
        // ���������
        JPasswordField password = new JPasswordField();
        password.setBounds(120, 100, 150, 20);
        normalpanel.add(password);
        
        // ��ť�趨
        JButton bu3 = new JButton("ע��");
        bu3.setBounds(150, 150, 65, 20);
        normalpanel.add(bu3);
        
        bu3.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				StaticDataManager.push(LoginFrame.this);
				UserProfileFrame profile = new UserProfileFrame(false);
			}
        	
        });
        
        // ��ť�趨
        JButton bu1 = new JButton("��¼");
        bu1.setBounds(250, 150, 65, 20);
        normalpanel.add(bu1);
		
		tabbedPane.addTab("Normal Login", normalpanel);
		
		
		JPanel adminpanel = new JPanel();
		adminpanel.setLayout(null);
		adminpanel.setBackground(Color.white);
		
		// �û������¼������Աߵ�����
        JLabel jl3 = new JLabel("����Ա");
        jl3.setBounds(70, 70, 50, 20);
        adminpanel.add(jl3);
        
        // �û������¼�����
		JTextField adminname = new JTextField();
		adminname.setBounds(120, 70, 150, 20);
		adminpanel.add(adminname);
        
        // ����������Աߵ�����
        JLabel jl4 = new JLabel("����");
        jl4.setBounds(70, 100, 50, 20);
        adminpanel.add(jl4);
 
        // ���������
        JPasswordField adminpassword = new JPasswordField();
        adminpassword.setBounds(120, 100, 150, 20);
        adminpanel.add(adminpassword);
        
        // ��ť�趨
        JButton bu2 = new JButton("��¼");
        bu2.setBounds(250, 150, 65, 20);
        adminpanel.add(bu2);
		
		tabbedPane.addTab("Admin Login", adminpanel);
		
		add(tabbedPane);
		
		this.setBounds(0, 0, 355, 265);
		normalpanel.setLayout(null);
		
		// �����С���ܸı�
        this.setResizable(false);
         
        // ������ʾ
        this.setLocationRelativeTo(null);
 
        // ����ɼ�
        //this.setVisible(true);
		
	}
	
	public static void main(String[] args) {
        // ʵ��������
		LoginFrame login = new LoginFrame();
		login.setVisible(true);
    }
}
