package com.diaryclient.usermgr;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.diaryclient.datamgr.DBManager;
import com.diaryclient.datamgr.StaticDataManager;
import com.diaryclient.main.MainMenuFrame;
import com.mysql.cj.xdevapi.Statement;

public class UserProfileFrame extends JFrame {

	public UserProfileFrame(boolean ismodify) {
		this.setBounds(0, 0, 350, 400);
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
		JTextField txtaccount = new JTextField();
		txtaccount.setBounds(70, 70, 150, 20);
        this.add(txtaccount);
        
        if(ismodify)
        	txtaccount.setEnabled(false);
        
        // 密码输入框旁边的文字
        JLabel jl2 = new JLabel("密码");
        jl2.setBounds(20, 150, 50, 20);
        this.add(jl2);
 
        // 密码输入框
        JPasswordField txtpass = new JPasswordField();
        txtpass.setBounds(70, 150, 150, 20);
        this.add(txtpass);
        
        // 密码输入框旁边的文字
        JLabel jl3 = new JLabel("确认");
        jl3.setBounds(20, 200, 50, 20);
        this.add(jl3);
 
        // 密码输入框
        JPasswordField txtpass2 = new JPasswordField();
        txtpass2.setBounds(70, 200, 150, 20);
        this.add(txtpass2);
        
        // 按钮设定
        
        String text = "登陆";
        if (ismodify) text = "修改";
        JButton bu3 = new JButton(text);
        bu3.setBounds(100, 250, 65, 20);
        
        bu3.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				try {
					
					bu3.setEnabled(false);
					
					String user = txtaccount.getText();
					String pass = String.valueOf(txtpass.getPassword());
					String pass2 = String.valueOf(txtpass2.getPassword());
					
					String name = txtname.getText();
					
					if("".equals(user)) {
						JOptionPane.showMessageDialog(UserProfileFrame.this, "You must input the account!");
						return;
					}
					
					if (!pass.equals(pass2)) {
						JOptionPane.showMessageDialog(UserProfileFrame.this, "Two time password input are not the same!");
						return;
					}
					
					Connection conn = DBManager.getconn();
					java.sql.Statement statement = conn.createStatement();
					
					if (!ismodify){ // when for new account
					
						String sql = String.format("select count(*) from duser where account= '%s'",user);
						
						ResultSet rs = statement.executeQuery(sql);
						int count = 0;
						
						while(rs.next()) {
							
							count = rs.getInt(1);
						}
						
						rs.close();
						
						if (count != 0) {
							JOptionPane.showMessageDialog(UserProfileFrame.this, "Account name has been used.");
							return;
						} else {
							
							sql = String.format("insert into duser (account,password,name, type) values('%s', '%s', '%s', 1)", user, pass,name);
							if (statement.execute(sql)) {
								System.out.println("Account Insert Failed");
							}
							statement.close();
						}
					} else { // modify account
						String sql = String.format("update duser set name = '%s', password = '%s' where id = '%d'", 
								name,pass, StaticDataManager.getUID());
						
						if (statement.execute(sql)) {
							System.out.println("Account Insert Failed");
						}
						statement.close();
						
					}
					conn.close();
					
					JOptionPane.showMessageDialog(UserProfileFrame.this, "Account update success.");
					
					UserProfileFrame.this.dispose();
					StaticDataManager.pop();
					
				} catch (SQLException e1) {
					System.out.println("sql error happend!!!");
					e1.printStackTrace();
				} catch (Exception e2) {
					System.out.println("some other error happend!!!");
					e2.printStackTrace();
				} finally {
					bu3.setEnabled(true);
				}
			}

		});
        
        this.add(bu3);
        
        JButton btnreturn = new JButton("返回");
        btnreturn.setBounds(180, 250, 65, 20);
        this.add(btnreturn);
        btnreturn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				UserProfileFrame.this.dispose();
				StaticDataManager.pop();
			}
		});
        
        
		if (ismodify) {
			Connection conn = DBManager.getconn();
			java.sql.Statement statement;
			try {
				statement = conn.createStatement();


				String sql = String.format("select account, password, name from duser where id= '%d'",
						StaticDataManager.getUID());

				ResultSet rs = statement.executeQuery(sql);

				String account = null, password = null, name = null;
				while (rs.next()) {

					account = rs.getString(1);
					password = rs.getString(2);
					name = rs.getString(3);
				}

				rs.close();
				statement.close();
				conn.close();
				
				txtaccount.setText(account);
				txtpass.setText(password);
				txtpass2.setText(password);
				txtname.setText(name);
				
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}

	}
}
