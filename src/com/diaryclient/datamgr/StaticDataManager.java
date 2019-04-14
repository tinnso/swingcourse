package com.diaryclient.datamgr;

import java.awt.Component;
import java.awt.Window;
import java.util.Stack;

public  class StaticDataManager {

	/**
	 * place for record pages that are be passed or current showed
	 * you can move back to those pages step by step like a stack
	 */
	public static Stack<Window> st = new Stack<Window>();
	
	public static void pop() {
		if (!st.isEmpty()) {
			Window com = st.pop();
			com.dispose();
		}
		
		if (!st.isEmpty()) {
			Window current = st.peek();
			current.setVisible(true);
		} 	
	} 
	
	/**
	 * @param com, current page display window
	 * 
	 */
	public static void push(Window com) {
		
			if (!st.isEmpty()) {
				Window current = st.peek();
				current.setVisible(false);
			}
			com.setVisible(true);
			st.push(com);
			
	} 
	
	/**
	 * 
	 * record current login user information
	 * 
	 */
	public static void setUserInfo(String info, int uid) {
		_user = info;
		_uid = uid;
	}
	
	public static String getUserFolder(){
		return _user;
	}
	
	public static int getUID() {
		return _uid;
	}
	
	public static String _user = null;
	public static int _uid = 0;
}
