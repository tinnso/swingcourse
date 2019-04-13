package com.diaryclient.datamgr;

import java.awt.Component;
import java.util.Stack;

public  class StaticDataManager {

	/**
	 * place for record pages that are be passed,
	 * you can move back to those pages step by step like a queue
	 */
	public static Stack<Component> st = new Stack<Component>();
	
	public static void pop() {
		if (!st.isEmpty()) {
			Component com = st.pop();
			com.setVisible(true);
		} 	
	} 
	
	/**
	 * @param com, current page you want to save back
	 * 
	 */
	public static void push(Component com) {
			st.push(com);
			com.setVisible(false);
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
