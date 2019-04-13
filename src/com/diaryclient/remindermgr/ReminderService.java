package com.diaryclient.remindermgr;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

public class ReminderService {
	
	private int delay=1000;
	private Timer timer;
	

	public void start()
	{
		timer.start();
	}
	 
	
	public void stop()
	{
		timer.stop();
		
	}
	
	
	
	public ReminderService() {
		
		ActionListener taskPerformer = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				
			}
		  };
		
		timer = new Timer(delay, taskPerformer);
	}
	

}
