package com.diaryclient.remindermgr;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

public class ReminderListFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JTable _datatable;
	private ReminderTableModel _datamodel;
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
		List<Date> remindertimes = new ArrayList<Date>();
		List<String> remindertexts = new ArrayList<String>();
		List<Integer> enables = new ArrayList<Integer>();
		List<Date> updatedates= new ArrayList<Date>();
		List<Date> insertdates= new ArrayList<Date>();
		List<Integer> reminderids= new ArrayList<Integer>();
		
		
		try {
			conn = DBManager.getconn();

			sql = "select * from reminder where userid=? order by remindertime ";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, StaticDataManager.getUID());
			rs = ps.executeQuery();
			
			while (rs.next()) {
				checkeds.add(false);
				remindertimes.add(rs.getTimestamp("remindertime"));
				remindertexts.add(rs.getString("remindertext"));
				enables.add(rs.getInt("enable"));
				updatedates.add(rs.getTimestamp("updatedate"));
				insertdates.add(rs.getTimestamp("insertdate"));
				reminderids.add(rs.getInt("id"));
				
			}
			
			
			ps.close();
			conn.close();
			

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
		
		if (remindertimes.size() > 0) {
			
			_datamodel = new ReminderTableModel();
			_datamodel.checks = checkeds;
			_datamodel.remindertimes = remindertimes;
			_datamodel.remindertexts = remindertexts;
			_datamodel.enables = enables;
			_datamodel.updatedates = updatedates;
			_datamodel.insertdates = insertdates;
			_datamodel.reminderids = reminderids;
			
			_datatable.setModel(_datamodel);
			
		}

	}
	
	private void delete(List<Integer> reminderids) {
		Connection conn = null;
		PreparedStatement ps = null;
		
		try {
			conn = DBManager.getconn();
			
			for (Integer id :reminderids) {
				String sql = "delete from  reminder  where id=?";
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

	
	public ReminderListFrame() {
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
				
				ReminderEditFrame frame = new ReminderEditFrame(-1);
				StaticDataManager.push(frame);
				
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
				int reminderid = 0;
				
				Boolean b = false;
				for (int i = 0; i < _datamodel.checks.size(); i++) {
					b =_datamodel.checks.get(i);
					if (b) {
						checkedcount ++;
						reminderid = _datamodel.reminderids.get(i);
					}
				}
				
				if (checkedcount > 1) {
					JOptionPane.showMessageDialog(ReminderListFrame.this, "You can only selected one reminder!");
					return;
				} else if (checkedcount == 0) {
					JOptionPane.showMessageDialog(ReminderListFrame.this, "Please select one reminder!");
					return;
				}
				
				ReminderEditFrame frame = new ReminderEditFrame(reminderid);
				StaticDataManager.push(frame);
				

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
				
				List<Integer> reminderids = new ArrayList<Integer>();
				
				
				Boolean b = false;
				for (int i = 0; i < _datamodel.checks.size(); i++) {
					b =_datamodel.checks.get(i);
					if (b) {
						reminderids.add(_datamodel.reminderids.get(i));
					}
				}
				
				if (reminderids.size() == 0) {
					JOptionPane.showMessageDialog(ReminderListFrame.this, "Please select one Diary!");
				} else {
					delete(reminderids);
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
				StaticDataManager.pop();
			}
		});
		this.add(btnreturn);
		
		this.addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
			
				StaticDataManager.pop();
			}
		});
		
		
		initilization();
		
		

	}

}
