package com.diaryclient.datamgr;

import java.awt.Component;
import java.util.Stack;

import javax.swing.JFrame;

public  class StaticDataManager {

	public static JFrame LoginFrame = null;
	public static String user = null;
	
	public static Stack<Component> st = new Stack<Component>();
	
	public static void pop() {
		if (!st.isEmpty()) {
			Component com = st.pop();
			com.setVisible(true);
		} 	
	} 
	
	public static void push(Component com) {
			st.push(com);
			com.setVisible(false);
	} 
	
	public static void setUserInfo(String info) {
		user = info;
	}
	
	public static String getUserFolder(){
		
		return "cs";
		//return user;
	}
}
