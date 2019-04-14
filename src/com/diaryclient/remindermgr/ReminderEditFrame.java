package com.diaryclient.remindermgr;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import com.diaryclient.comm.ICallback;
import com.diaryclient.datamgr.DBManager;
import com.diaryclient.datamgr.StaticDataManager;

public class ReminderEditFrame extends JFrame {
	private JLabel lbliseanble;
	private JTextArea txtcontent;
	private int _reminderid = -1;
	private boolean _isenable = false;
	DateChooserButton datechooser;
	
	private ICallback callback;
	
	public void setCallback(ICallback callback)
    {
       this.callback= callback;
    }

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int update(int reminderid, Date remindertime,String remindertext, int enable) {
		
		int result = reminderid;

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			
			conn = DBManager.getconn();
			
			String sql = "";
			if (reminderid == -1) {
				sql = "insert into reminder (userid, remindertime, remindertext, enable, updatedate, insertdate) values (?,?,?,?,sysdate(),sysdate())";
				ps = conn.prepareStatement(sql);
				ps.setInt(1, StaticDataManager.getUID());
				ps.setTimestamp(2, new java.sql.Timestamp(remindertime.getTime()));
				ps.setString(3, remindertext);
				ps.setInt(4, enable);
				ps.execute();
				
				// SELECT LAST_INSERT_ID();
				sql = "SELECT LAST_INSERT_ID() as id";
				ps = conn.prepareStatement(sql);
				rs = ps.executeQuery();

				while (rs.next()) {
					result = rs.getInt("id");
				}
				
			} else {
				sql = "update reminder set remindertime=?, remindertext=?, enable=?, updatedate=sysdate() where id=?";
				ps = conn.prepareStatement(sql);
				ps.setTimestamp(1, new java.sql.Timestamp(remindertime.getTime()));
				ps.setString(2, remindertext);
				ps.setInt(3, enable);
				ps.setInt(4, reminderid);
				ps.execute();
				
			}

			ps.close();
			conn.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
		
		return result;
	}
	
	private void initilization(int reminderid) {
		_reminderid = reminderid;
		
		if (reminderid == -1) return;
		
		Connection conn = DBManager.getconn();
		java.sql.Statement statement;
		try {
			statement = conn.createStatement();


			String sql = String.format("select * from reminder where id= '%d'",
					reminderid);

			ResultSet rs = statement.executeQuery(sql);

			Date remindertime = null;
			String remindertext = "";
			int enable = 0;
			
			while (rs.next()) {

				remindertime = rs.getTimestamp("remindertime");
				remindertext = rs.getString("remindertext");
				enable = rs.getInt("enable");
			}

			rs.close();
			statement.close();
			conn.close();
			
			_isenable = enable == 1 ? true : false;
			
			datechooser.setDate(remindertime);
			txtcontent.setText(remindertext);
			String enabletext = enable == 1? "有效" : "无效";
			lbliseanble.setText(enabletext);
			
			
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		
	}
	
	public ReminderEditFrame(int reminderid) {
		
		this.setBounds(0, 0, 280, 360);
		this.setLayout(null);
		
	
		// 窗体大小不能改变
		this.setResizable(false);

		// 居中显示
		this.setLocationRelativeTo(null);
		
		lbliseanble = new JLabel("无效");
		lbliseanble.setBounds(20, 10, 50, 20);
	    this.add(lbliseanble);
		
		JLabel lbltime = new JLabel("提示时间");
		lbltime.setBounds(20, 40, 50, 20);
	    this.add(lbltime);
		
		datechooser = new DateChooserButton();
		datechooser.setBounds(80, 40, 150, 20);
		this.add(datechooser);
		
		JLabel lblcontent =  new JLabel("提示内容");
		lblcontent.setBounds(20, 70, 50, 20);
	    this.add(lblcontent);
		
	    txtcontent = new JTextArea();
	    txtcontent.setBounds(20, 95, 210, 160);
	    this.add(txtcontent);
	    
	    JButton btnenable = new JButton("有效");
	    btnenable.setBounds(20, 280, 60, 20);
	    btnenable.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				_isenable = true;
				lbliseanble.setText("有效");
				
			}
	    });
	    this.add(btnenable);
	    
	    
	    
	    JButton btndisable = new JButton("无效");
	    btndisable.setBounds(90, 280, 60, 20);
	    btnenable.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				_isenable = false;
				lbliseanble.setText("无效");
			}
	    });
	    this.add(btndisable);
	    
	    JButton btnsave = new JButton("保存");
	    btnsave.setBounds(160, 280, 60, 20);
	    
	    btnsave.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				btnsave.setEnabled(false);
				
				String text = txtcontent.getText();
				Date time = datechooser.getDate();
				int id = _reminderid;
				int enble = _isenable ? 1:0;
				
				if("".equals(text)) {
					JOptionPane.showMessageDialog(ReminderEditFrame.this, "You must input the content!");
					btnsave.setEnabled(true);
					return;
				}
				
				_reminderid = update(id, time,text, enble);
				
				btnsave.setEnabled(true);
				
				if (callback != null) {
					callback.callback(null);
				}
				
				StaticDataManager.pop();
			}
	    });
	    this.add(btnsave);
	    
	    initilization(reminderid);
	    
	    /*
	     * when user press close button, can return back to previous page
	     */
	    this.addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {

				StaticDataManager.pop();
			}
		});
	    
	}

	
	
}
