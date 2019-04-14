package com.diaryclient.remindermgr;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import com.diaryclient.comm.ICallback;

public class ReminderDialog extends JDialog {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ICallback callback;
	
	public void setCallback(ICallback callback)
    {
       this.callback= callback;
    }

	
	private SimpleDateFormat datetimeformat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");


	ReminderDialog(JFrame f, Reminder reminder ) {
		super(f, true);
		
		this.setBounds(0, 0, 280, 360);
		this.setLayout(null);
		this.setLocationRelativeTo(f);
		this.setResizable(false);
		
		//JLabel lbliseanble = new JLabel("无效");
		//lbliseanble.setBounds(20, 10, 50, 20);
	    //this.add(lbliseanble);
		
		JLabel lbltime = new JLabel("提示时间");
		lbltime.setBounds(20, 40, 50, 20);
	    this.add(lbltime);
		
		JLabel lblremindertime = new JLabel();
		lblremindertime.setBounds(80, 40, 150, 20);
		lblremindertime.setText(datetimeformat.format(reminder.remindertime));
		
		this.add(lblremindertime);
		
		JLabel lblcontent =  new JLabel("提示内容");
		lblcontent.setBounds(20, 70, 50, 20);
	    this.add(lblcontent);
		
	    JTextArea txtcontent = new JTextArea();
	    txtcontent.setBounds(20, 95, 210, 160);
	    txtcontent.setEnabled(false);
	    txtcontent.setText(reminder.remindertext);
	    this.add(txtcontent);
		
	    JButton btnenable = new JButton("删除");
	    btnenable.setBounds(20, 280, 60, 20);
	    btnenable.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				ReminderDialog.this.dispose();
				
				if (callback != null)
					callback.callback("" + reminder.id);
			}
	    });
	    this.add(btnenable);
	    
		
	    JButton btnclose = new JButton("关闭");
		btnclose.setBounds(100, 280, 60, 20);
		
		this.add(btnclose);
		btnclose.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {				
				ReminderDialog.this.dispose();
			}
	    });
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

}
