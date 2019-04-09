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
		
		// 用户号码登录输入框旁边的文字
        JLabel jl1 = new JLabel("用户名");
        jl1.setBounds(70, 70, 50, 20);
        normalpanel.add(jl1);
        
        // 用户号码登录输入框
		JTextField username = new JTextField();
        username.setBounds(120, 70, 150, 20);
        normalpanel.add(username);
        
        // 密码输入框旁边的文字
        JLabel jl2 = new JLabel("密码");
        jl2.setBounds(70, 100, 50, 20);
        normalpanel.add(jl2);
 
        // 密码输入框
        JPasswordField password = new JPasswordField();
        password.setBounds(120, 100, 150, 20);
        normalpanel.add(password);
        
        // 按钮设定
        JButton bu3 = new JButton("注册");
        bu3.setBounds(150, 150, 65, 20);
        normalpanel.add(bu3);
        
        bu3.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				StaticDataManager.push(LoginFrame.this);
				UserProfileFrame profile = new UserProfileFrame(false);
			}
        	
        });
        
        // 按钮设定
        JButton bu1 = new JButton("登录");
        bu1.setBounds(250, 150, 65, 20);
        normalpanel.add(bu1);
		
		tabbedPane.addTab("Normal Login", normalpanel);
		
		
		JPanel adminpanel = new JPanel();
		adminpanel.setLayout(null);
		adminpanel.setBackground(Color.white);
		
		// 用户号码登录输入框旁边的文字
        JLabel jl3 = new JLabel("管理员");
        jl3.setBounds(70, 70, 50, 20);
        adminpanel.add(jl3);
        
        // 用户号码登录输入框
		JTextField adminname = new JTextField();
		adminname.setBounds(120, 70, 150, 20);
		adminpanel.add(adminname);
        
        // 密码输入框旁边的文字
        JLabel jl4 = new JLabel("密码");
        jl4.setBounds(70, 100, 50, 20);
        adminpanel.add(jl4);
 
        // 密码输入框
        JPasswordField adminpassword = new JPasswordField();
        adminpassword.setBounds(120, 100, 150, 20);
        adminpanel.add(adminpassword);
        
        // 按钮设定
        JButton bu2 = new JButton("登录");
        bu2.setBounds(250, 150, 65, 20);
        adminpanel.add(bu2);
		
		tabbedPane.addTab("Admin Login", adminpanel);
		
		add(tabbedPane);
		
		this.setBounds(0, 0, 355, 265);
		normalpanel.setLayout(null);
		
		// 窗体大小不能改变
        this.setResizable(false);
         
        // 居中显示
        this.setLocationRelativeTo(null);
 
        // 窗体可见
        //this.setVisible(true);
		
	}
	
	public static void main(String[] args) {
        // 实例化对象
		LoginFrame login = new LoginFrame();
		login.setVisible(true);
    }
}
