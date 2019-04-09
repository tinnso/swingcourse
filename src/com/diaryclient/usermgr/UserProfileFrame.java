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
import com.mysql.cj.xdevapi.Statement;

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
		JTextField txtaccount = new JTextField();
		txtaccount.setBounds(70, 70, 150, 20);
        this.add(txtaccount);
        
        // ����������Աߵ�����
        JLabel jl2 = new JLabel("����");
        jl2.setBounds(20, 150, 50, 20);
        this.add(jl2);
 
        // ���������
        JPasswordField txtpass = new JPasswordField();
        txtpass.setBounds(70, 150, 150, 20);
        this.add(txtpass);
        
        // ����������Աߵ�����
        JLabel jl3 = new JLabel("ȷ��");
        jl3.setBounds(20, 200, 50, 20);
        this.add(jl3);
 
        // ���������
        JPasswordField txtpass2 = new JPasswordField();
        txtpass2.setBounds(70, 200, 150, 20);
        this.add(txtpass2);
        
        // ��ť�趨
        
        String text = "��½";
        if (ismodify) text = "�޸�";
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
					
					conn.close();
					
					JOptionPane.showMessageDialog(UserProfileFrame.this, "Account update success.");
					
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
	}
}
