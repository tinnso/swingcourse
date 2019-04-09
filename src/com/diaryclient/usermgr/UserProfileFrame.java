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

		// 窗体大小不能改变
		this.setResizable(false);

		// 居中显示
		this.setLocationRelativeTo(null);

		// 窗体可见
		this.setVisible(true);

		this.addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
			
				StaticDataManager.pop();
			}
		});

		
		// 用户号码登录输入框旁边的文字
        JLabel lblname = new JLabel("姓名");
        lblname.setBounds(20, 20, 50, 20);
        this.add(lblname);
        
        // 用户号码登录输入框
		JTextField txtname = new JTextField();
		txtname.setBounds(70, 20, 150, 20);
        this.add(txtname);
		
		
		// 用户号码登录输入框旁边的文字
        JLabel jl1 = new JLabel("用户名");
        jl1.setBounds(20, 70, 50, 20);
        this.add(jl1);
        
        // 用户号码登录输入框
		JTextField username = new JTextField();
        username.setBounds(70, 70, 150, 20);
        this.add(username);
        
        // 密码输入框旁边的文字
        JLabel jl2 = new JLabel("密码");
        jl2.setBounds(20, 150, 50, 20);
        this.add(jl2);
 
        // 密码输入框
        JPasswordField password = new JPasswordField();
        password.setBounds(70, 150, 150, 20);
        this.add(password);
        
        // 密码输入框旁边的文字
        JLabel jl3 = new JLabel("确认");
        jl3.setBounds(20, 200, 50, 20);
        this.add(jl3);
 
        // 密码输入框
        JPasswordField password2 = new JPasswordField();
        password2.setBounds(70, 200, 150, 20);
        this.add(password2);
        
        // 按钮设定
        
        String text = "登陆";
        if (ismodify) text = "修改";
        JButton bu3 = new JButton(text);
        bu3.setBounds(100, 250, 65, 20);
        this.add(bu3);
	}
}
