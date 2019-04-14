package com.diaryclient.remindermgr;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.Timer;

import com.diaryclient.comm.ICallback;
import com.diaryclient.datamgr.DBManager;
import com.diaryclient.datamgr.StaticDataManager;

public class ReminderService {
	
	private int delay=1000;
	private Timer timer;
	
	private List<Reminder> reminders = new ArrayList<Reminder>();
	
	public void start() {
		initilization();
		timer.start();
	}
	 
	
	public void stop() {
		reminders.clear();
		timer.stop();
	}
	
	
	public void initilization() {
		String sql = "";
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		
		try {
			conn = DBManager.getconn();

			sql = "select * from reminder where userid=? and enable=1 order by remindertime ";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, StaticDataManager.getUID());
			rs = ps.executeQuery();
			
			int id = -1;
			Date remindertime = null;
			String remindertext = "";
			
			while (rs.next()) {
				remindertime = rs.getTimestamp("remindertime");
				remindertext = rs.getString("remindertext");
				id = rs.getInt("id");
				
				Reminder reminder = new Reminder(id, remindertime, remindertext);
				reminders.add(reminder);
				
			}
			
			ps.close();
			conn.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
		
		
	}
	
	public void delete(int id) {
		
		Connection conn = null;
		PreparedStatement ps = null;
		
		try {
			conn = DBManager.getconn();
						
			String sql = "delete from  reminder  where id=?";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, id);

			ps.execute();
		
			
			ps.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}	
	}
	
	
	public ReminderService() {
		
		
		
		ActionListener taskPerformer = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				timer.stop();
				
				for(Reminder reminder : reminders) {
					Date now = new Date();
					Date remindertime = reminder.remindertime;
					
					if (now.compareTo(remindertime) >= 0) {
						
						
						JFrame win = (JFrame)StaticDataManager.getcurrentwin();
						
						ReminderDialog dialog = new ReminderDialog(win, reminder);
						
						dialog.setCallback(new ICallback() {

							@Override
							public void callback(String args) {
								int id = Integer.parseInt(args, 10);
								
								int size = reminders.size();
								
								
								for (int i = size - 1; i > 0; i--) {
									if (reminders.get(i).id == id) {
										reminders.remove(i);
									}
									
								}
								delete(id);
							}
							
						});
						
						dialog.setVisible(true);
						
					}
				}
				
				
			}
		  };
		
		timer = new Timer(delay, taskPerformer);
	}
	

}
