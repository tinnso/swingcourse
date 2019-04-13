package com.diaryclient.usermgr;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import com.diaryclient.datamgr.DBManager;
import com.diaryclient.datamgr.StaticDataManager;
import com.diaryclient.main.MainMenuFrame;

import java.sql.*;

public class LoginFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTabbedPane tabbedPane;

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

		bu3.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				StaticDataManager.push(LoginFrame.this);
				UserEditFrame profile = new UserEditFrame(-1);
				profile.setVisible(true);
			}

		});

		// ��ť�趨
		JButton bu1 = new JButton("��¼");
		bu1.setBounds(250, 150, 65, 20);
		normalpanel.add(bu1);
		
		bu1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					
					bu1.setEnabled(false);
					
					String user = username.getText();
					String pass = String.valueOf(password.getPassword());
		
					
					if( "".equals(user)) {
						JOptionPane.showMessageDialog(LoginFrame.this, "You must input the account!");
						return;
					}
					
					
					Connection conn = DBManager.getconn();
					java.sql.Statement statement = conn.createStatement();
					
					String sql = String.format("select id, account from duser where account='%s' and password='%s' and type=1 and deleted=0",user, pass);
					
					ResultSet rs = statement.executeQuery(sql);
					int count = 0;
					
					int userid = 0;
					while(rs.next()) {
						
						userid = rs.getInt(1);
						count ++;
					}
					
					rs.close();
					
					if (count == 0) {
						JOptionPane.showMessageDialog(LoginFrame.this, "Account or Password not correct");
						return;
					} else {
	
						StaticDataManager.setUserInfo(user, userid);
						MainMenuFrame menu = new MainMenuFrame();
						StaticDataManager.push(LoginFrame.this);
						menu.setVisible(true);
					
					}
					
					conn.close();
									
					
				} catch (SQLException e1) {
					System.out.println("sql error happend!!!");
					e1.printStackTrace();
				} catch (Exception e2) {
					System.out.println("some other error happend!!!");
					e2.printStackTrace();
				} finally {
					bu1.setEnabled(true);
				}
				
			}
				


		});
		

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
		
		bu2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					
					bu2.setEnabled(false);
					
					String user = adminname.getText();
					String pass = String.valueOf(adminpassword.getPassword());
		
					
					if( "".equals(user)) {
						JOptionPane.showMessageDialog(LoginFrame.this, "You must input the account!");
						return;
					}
					
					
					Connection conn = DBManager.getconn();
					java.sql.Statement statement = conn.createStatement();
					
					String sql = String.format("select id, account from duser where account='%s' and password='%s' and type=0 and deleted=0",user, pass);
					
					ResultSet rs = statement.executeQuery(sql);
					int count = 0;
					
					int userid = 0;
					while(rs.next()) {
						count ++;
						userid = rs.getInt(1);
					}
					
					rs.close();
					
					if (count == 0) {
						JOptionPane.showMessageDialog(LoginFrame.this, "Account or Password not correct");
						return;
					} else {
						StaticDataManager.setUserInfo(user, userid);
						
						UserListFrame frame = new  UserListFrame();
						StaticDataManager.push(LoginFrame.this);
						frame.setVisible(true);
					}
					
					conn.close();
					
				} catch (SQLException e1) {
					System.out.println("sql error happend!!!");
					e1.printStackTrace();
				} catch (Exception e2) {
					System.out.println("some other error happend!!!");
					e2.printStackTrace();
				} finally {
					bu2.setEnabled(true);
				}
				
			}
				


		});

		tabbedPane.addTab("Admin Login", adminpanel);

		add(tabbedPane);

		this.setBounds(0, 0, 355, 265);
		normalpanel.setLayout(null);

		// �����С���ܸı�
		this.setResizable(false);

		// ������ʾ
		this.setLocationRelativeTo(null);

		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public static void main(String[] args) {
		// ʵ��������
		LoginFrame login = new LoginFrame();
		login.setVisible(true);

	}
}
