package com.diaryclient.datamgr;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 
 * DB Management class, current just supply return connection
 * in fact, you can modify it for more functions
 *
 */
public class DBManager {

	private static boolean _initialized=false;
	
	private static String _url=null;

	public static boolean initialize() {
		if (_initialized)
			return true;

		// 驱动程序名
		String driver = "com.mysql.cj.jdbc.Driver";
		try {
			// 加载驱动程序
			Class.forName(driver);
			_initialized = true;
			return true;
		} catch (ClassNotFoundException e) {
			System.out.println("Sorry,can`t find the Driver!"); 
			e.printStackTrace();
			_initialized = false;
			return false;
		}
	}

	public static Connection getconn() {
		
		if (!initialize())
			return null;
		
		//声明Connection对象
        Connection con;
        
        //遍历查询结果集
        try {
        	// if not set timezone, when save date, it will -9 hours
        	//_url = "jdbc:mysql://localhost:3306/diary?user=root&password=123456&useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC&useSSL=false";
        	_url = "jdbc:mysql://localhost:3306/diary?user=root&password=123456&useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai&useSSL=false";
			con = DriverManager.getConnection(_url);
			
			if(!con.isClosed()) {
			    return con;
			} else {
				return null;
			}
			
		} catch (SQLException e) {

			e.printStackTrace();
			return null;
		}
	}

}
