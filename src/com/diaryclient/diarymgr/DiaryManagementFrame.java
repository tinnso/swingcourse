package com.diaryclient.diarymgr;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.EventQueue;
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
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.diary.picturemagr.PictureManager;
import com.diaryclient.datamgr.DBManager;
import com.diaryclient.datamgr.StaticDataManager;

public class DiaryManagementFrame extends JFrame {
	
	private JTable _datatable;
	private DiaryTableModel _datamodel;
	private JButton btnallselect;
	private JButton btnmodify;
	private JButton btndelete;
	
	

	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DiaryManagementFrame frame = new DiaryManagementFrame();

					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public void initilization(int uid) {
		String sql = "";
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		List<Boolean>checkeds = new ArrayList<Boolean>();
		List<String> dates = new ArrayList<String>();
		List<Date> updatedates= new ArrayList<Date>();
		List<Date> insertdates = new ArrayList<Date>();
		
		try {
			conn = DBManager.getconn();

			if (uid == -1) {
				sql = "select date, updatedate, insertdate from diary";
				ps = conn.prepareStatement(sql);
			} else {
				sql = "select date, updatedate, insertdate from diary where userid=?";
				ps = conn.prepareStatement(sql);
				ps.setInt(1, uid);
			}
			
			rs = ps.executeQuery();
			
			while (rs.next()) {
				checkeds.add(false);
				dates.add(rs.getString("date"));
				updatedates.add(rs.getDate("updatedate"));
				insertdates.add(rs.getDate("insertdate"));
			}
			
			
			// TODO
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
		
		if (dates.size() > 0) {
			
			_datamodel = new DiaryTableModel();
			_datamodel.checks = checkeds;
			_datamodel.dates = dates;
			_datamodel.updatedates = updatedates;
			_datamodel.insertdates = insertdates;
			
			_datatable.setModel(_datamodel);
			
		}

	}
	
	public DiaryManagementFrame() {
		this.setSize(600, 400);// 设窗体的大小 宽和高
		this.setLayout(null);

		// 窗体大小不能改变
		this.setResizable(false);

		// 居中显示
		this.setLocationRelativeTo(null);
		
		
		JScrollPane scrollpane = new JScrollPane();
		
		_datatable = new JTable();
		scrollpane.setViewportView(_datatable);
		
		scrollpane.setBounds(5, 5, 585, 300);
		this.add(scrollpane);
		
		int id = StaticDataManager.getUID();
		initilization(-1);// TODO
		
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
		
		btnmodify = new JButton("修改");
		btnmodify.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				
			}
			
		});
		
		btndelete = new JButton("删除");
		btndelete.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				
			}
			
		});
		
		this.addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
			
				StaticDataManager.pop();
			}
		});

	}

	
}
