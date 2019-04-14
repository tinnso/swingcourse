package com.diaryclient.remindermgr;

import java.util.Date;

public class Reminder {
	public int id;
	public Date remindertime;
	public String remindertext;
	
	public Reminder(int id, Date remindertime,  String remindertext) {
		this.id = id;
		this.remindertime = remindertime;
		this.remindertext= remindertext;
	}
}
