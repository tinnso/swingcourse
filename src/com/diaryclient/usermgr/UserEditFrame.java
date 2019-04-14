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

import com.diaryclient.comm.ICallback;
import com.diaryclient.datamgr.DBManager;
import com.diaryclient.datamgr.StaticDataManager;

public class UserEditFrame extends JFrame {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ICallback callback;
	
	private int _userid = -1;


    public void setCallback(ICallback callback)
    {
       this.callback= callback;
    }

	public UserEditFrame(int userid) {
		this.setBounds(0, 0, 350, 400);
		this.setLayout(null);
	
		_userid = userid;

		// �����С���ܸı�
		this.setResizable(false);

		// ������ʾ
		this.setLocationRelativeTo(null);

		this.addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				
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
        
        if(_userid != -1)
        	txtaccount.setEnabled(false);
        
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
        if (_userid != -1) text = "�޸�";
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
						JOptionPane.showMessageDialog(UserEditFrame.this, "You must input the account!");
						return;
					}
					
					if (!pass.equals(pass2)) {
						JOptionPane.showMessageDialog(UserEditFrame.this, "Two time password input are not the same!");
						return;
					}
					
					Connection conn = DBManager.getconn();
					java.sql.Statement statement = conn.createStatement();
					
					if (_userid == -1){ // when for new account
					
						String sql = String.format("select count(*) from duser where account= '%s'",user);
						
						ResultSet rs = statement.executeQuery(sql);
						int count = 0;
						
						while(rs.next()) {
							
							count = rs.getInt(1);
						}
						
						rs.close();
						
						// TODO basically password should be encrypted then save to DB!!!
						
						if (count != 0) {
							JOptionPane.showMessageDialog(UserEditFrame.this, "Account name has been used.");
							return;
						} else {
							
							sql = String.format("insert into duser (account,password,name, type, updatedate, insertdate) values('%s', '%s', '%s', 1,sysdate(),sysdate())", user, pass,name);
							if (statement.execute(sql)) {
								System.out.println("Account Insert Failed");
							}
							
							
							// SELECT LAST_INSERT_ID();
							sql = "SELECT LAST_INSERT_ID() as id";
							rs = statement.executeQuery(sql);

							while (rs.next()) {
								_userid = rs.getInt("id");
							}
							
							
							statement.close();
							bu3.setText("�޸�");
							txtaccount.setEnabled(false);
						}
					} else { // modify account
						String sql = String.format("update duser set name = '%s', password = '%s', updatedate=sysdate(), insertdate=sysdate(), deleted=0 where id = '%d'", 
								name,pass, _userid);
						
						if (statement.execute(sql)) {
							System.out.println("Account Insert Failed");
						}
						statement.close();
						
					}
					conn.close();
					
					JOptionPane.showMessageDialog(UserEditFrame.this, "Account update success.");
					
					if (callback != null) {
						callback.callback(null);
					}
					
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
        
        JButton btnreturn = new JButton("����");
        btnreturn.setBounds(180, 250, 65, 20);
        this.add(btnreturn);
        btnreturn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				StaticDataManager.pop();
			}
		});
        
        
		if (_userid != -1) {
			Connection conn = DBManager.getconn();
			java.sql.Statement statement;
			try {
				statement = conn.createStatement();


				String sql = String.format("select account, password, name from duser where id= '%d'",
						_userid);

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
				e1.printStackTrace();
			}

		}

	}
}
