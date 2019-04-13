package com.diaryclient.usermgr;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;


import com.diaryclient.comm.ICallback;
import com.diaryclient.datamgr.DBManager;
import com.diaryclient.datamgr.StaticDataManager;



public class UserListFrame extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6562685126900165943L;
	
	private JTable _datatable;
	private UserTableModel _datamodel;
	private JButton btnallselect;
	private JButton btnmodify;
	private JButton btndelete;
	private JButton btnadd;
	
	public void initilization() {
		String sql = "";
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		List<Boolean>checkeds = new ArrayList<Boolean>();
		List<String> accounts = new ArrayList<String>();
		List<String> names = new ArrayList<String>();
		List<Integer> types = new ArrayList<Integer>();
		List<Date> updatedates= new ArrayList<Date>();
		List<Date> insertdates= new ArrayList<Date>();
		List<Integer> deleteds = new ArrayList<Integer>();
		List<Integer> userids =  new ArrayList<Integer>();
		
		try {
			conn = DBManager.getconn();

			sql = "select * from duser order by account";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			
			while (rs.next()) {
				checkeds.add(false);
				accounts.add(rs.getString("account"));
				names.add(rs.getString("name"));
				types.add(rs.getInt("type"));
				updatedates.add(rs.getTimestamp("updatedate"));
				insertdates.add(rs.getTimestamp("insertdate"));
				deleteds.add(rs.getInt("deleted"));
				userids.add(rs.getInt("id"));
				
			}
			
			
			conn.close();
			if (null != ps) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
		
		if (accounts.size() > 0) {
			
			_datamodel = new UserTableModel();
			_datamodel.checks = checkeds;
			_datamodel.accounts = accounts;
			_datamodel.names = names;
			_datamodel.types = types;
			_datamodel.updatedates = updatedates;
			_datamodel.insertdates = insertdates;
			_datamodel.userids = userids;
			_datamodel.deleteds = deleteds;
			
			_datatable.setModel(_datamodel);
			
		}

	}
	
	private void delete(List<Integer> userids) {
		Connection conn = null;
		PreparedStatement ps = null;
		
		try {
			conn = DBManager.getconn();
			
			for (Integer id :userids) {
				String sql = "update duser set deleted = 1, updatedate=sysdate() where id=?";
				ps = conn.prepareStatement(sql);
				ps.setInt(1, id);
	
				ps.execute();
			}
			
			ps.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
		
	}

	
	public UserListFrame() {
		this.setSize(600, 400);// 设窗体的大小 宽和高
		this.setLayout(null);

		this.setResizable(false); // 窗体大小不能改变
		this.setLocationRelativeTo(null); // 居中显示
		
		
		JScrollPane scrollpane = new JScrollPane();
		
		_datatable = new JTable();
		scrollpane.setViewportView(_datatable);
		
		scrollpane.setBounds(5, 5, 585, 300);
		this.add(scrollpane);
		
		
		btnallselect = new JButton("全选"); 
		btnallselect.setBounds(10, 310, 100, 20);
		btnallselect.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int checkedcount = 0;
				for(Boolean b : _datamodel.checks) {
					if (b) {
						checkedcount ++;
					}
				}
				if (checkedcount == _datamodel.checks.size() )
				{
					for (int i = 0; i < _datamodel.checks.size(); i ++)
						_datamodel.checks.set(i, false);
				} else {
					for (int i = 0; i < _datamodel.checks.size(); i ++)
						_datamodel.checks.set(i, true);
					
				}
				_datatable.updateUI();
			}
			
		});
		this.add(btnallselect);
		
		btnadd = new JButton("追加");
		btnadd.setBounds(150, 310, 100, 20);
		btnadd.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				StaticDataManager.push(UserListFrame.this);
				UserEditFrame frame = new UserEditFrame(false, -1);
				frame.setVisible(true);
				frame.setCallback(new ICallback() {

					@Override
					public void callback(String args) {
						initilization();
						_datatable.updateUI();
					}
					
				});
			}
			
		});
		this.add(btnadd);
		
		
		btnmodify = new JButton("修改");
		btnmodify.setBounds(260, 310, 100, 20);
		btnmodify.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				int checkedcount = 0;
				int userid = 0;
				
				Boolean b = false;
				for (int i = 0; i < _datamodel.checks.size(); i++) {
					b =_datamodel.checks.get(i);
					if (b) {
						checkedcount ++;
						userid = _datamodel.userids.get(i);
					}
				}
				
				if (checkedcount > 1) {
					JOptionPane.showMessageDialog(UserListFrame.this, "You can only selected one user!");
					return;
				} else if (checkedcount == 0) {
					JOptionPane.showMessageDialog(UserListFrame.this, "Please select one user!");
					return;
				}
				
				StaticDataManager.push(UserListFrame.this);
				UserEditFrame frame = new UserEditFrame(true, userid);
				frame.setVisible(true);
				
				frame.setCallback(new ICallback() {
					@Override
					public void callback(String args) {
						initilization();
						_datatable.updateUI();
					}
					
				});
			}
			
		});
		this.add(btnmodify);

		btndelete = new JButton("删除");
		btndelete.setBounds(370, 310, 100, 20);
		btndelete.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				List<Integer> userids = new ArrayList<Integer>();
				
				
				Boolean b = false;
				for (int i = 0; i < _datamodel.checks.size(); i++) {
					b =_datamodel.checks.get(i);
					if (b) {
						userids.add(_datamodel.userids.get(i));
					}
				}
				
				if (userids.size() == 0) {
					JOptionPane.showMessageDialog(UserListFrame.this, "Please select one Diary!");
				} else {
					delete(userids);
					initilization();
					_datatable.updateUI();
				};
			}
			
		});
		this.add(btndelete);
	
		
		
		JButton btnreturn = new JButton("返回");
		btnreturn.setBounds(480, 310, 100, 20);
		btnreturn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				UserListFrame.this.dispose();
				StaticDataManager.pop();
			}
		});
		this.add(btnreturn);
		
		this.addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
			
				StaticDataManager.pop();
			}
		});
		
		
		initilization();
		
		

	}

}
