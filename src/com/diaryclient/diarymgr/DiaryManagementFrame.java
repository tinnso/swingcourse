package com.diaryclient.diarymgr;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.diary.comm.DiaryUtil;
import com.diary.picturemagr.PictureManager;
import com.diaryclient.datamgr.DBManager;
import com.diaryclient.datamgr.StaticDataManager;
import com.diaryclient.main.DiaryEditorFrame;
import com.diaryclient.usermgr.UserProfileFrame;

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
		List<Date> dates = new ArrayList<Date>();
		List<Date> updatedates= new ArrayList<Date>();
		List<Date> insertdates = new ArrayList<Date>();
		List<Integer> diaryids = new ArrayList<Integer>();
		
		try {
			conn = DBManager.getconn();

			if (uid == -1) {
				sql = "select date, updatedate, insertdate, id from diary order by userid, date";
				ps = conn.prepareStatement(sql);
			} else {
				sql = "select date, updatedate, insertdate, id from diary where userid=? order by date";
				ps = conn.prepareStatement(sql);
				ps.setInt(1, uid);
			}
			
			rs = ps.executeQuery();
			
			while (rs.next()) {
				checkeds.add(false);
				dates.add(rs.getDate("date"));
				updatedates.add(rs.getTimestamp("updatedate"));
				insertdates.add(rs.getTimestamp("insertdate"));
				diaryids.add(rs.getInt("id"));
				
			
				
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
			_datamodel.diaryids = diaryids;
			
			_datatable.setModel(_datamodel);
			
		}

	}
	
	private void export(List<Integer>diaryids, String targetpath) {
		
		String sql = "";
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		String text = "";
		String date = "";
		
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
		
		
		try {
			conn = DBManager.getconn();
			
			for (int diaryid : diaryids) {
				
				sql = "select date, text from diary where id=?";
				ps = conn.prepareStatement(sql);
				ps.setInt(1, diaryid);
				
				rs = ps.executeQuery();
				while (rs.next()) {
					text = rs.getString("text"); 
					Date tmpDate = rs.getDate("date");
					if (tmpDate != null) {
						date = dateformat.format(tmpDate);
					} else {
						date = "";
					}
					
					File datefolder = new File(targetpath + date);
					datefolder.mkdir();
					File reourcefolder = new File(datefolder.getCanonicalPath() + "/pictures");
					reourcefolder.mkdir();
					File iconfolder = new File(datefolder.getCanonicalPath() + "/icons");
					iconfolder.mkdir();
					
					PictureManager.readDB2Image(reourcefolder.getCanonicalPath() + "/", diaryid);
					
					try {
						
						String sourceiconfolder = new File(this.getClass().getResource("/").getFile(), "../resource/icons/")
								.getCanonicalPath();
						
						File sourceicons = new File(sourceiconfolder);
						for (File file :sourceicons.listFiles() ) 
						{
							File destfile = new File(iconfolder + "/" + file.getName());
							DiaryUtil.copyFile(file, destfile);
						}
						
						text = modifyurl(text);
						
						File htmlfile = new File(datefolder + "/"+datefolder.getName()+".html");
						htmlfile.createNewFile();
						
						FileWriter fileWritter = new FileWriter(htmlfile, false);
						fileWritter.write(text);
					    fileWritter.close();
						

					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					
				}
			}

			
			
			ps.close();
			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
	}
	
	private String modifyurl(String text)
	{
		System.out.println("Html document before zip:" + text);
		
		// convert src of image or icon, save to hashmap
		Document doc = Jsoup.parse(text);
		Elements links = doc.select("img[src]");

		String key = "";
		for (Element element : links) {
			String imgUrl = element.attr("src");
			String filename = imgUrl.substring(imgUrl.lastIndexOf("\\")+1);  
			
			if (imgUrl.contains("diary")) { // pictures

				imgUrl = ".\\pictures\\" + filename;
			} else if (imgUrl.contains("icons")) { // icons
				imgUrl = ".\\icons\\" + filename;
			}
			element.attr("src", imgUrl);
		}
	
		String result = doc.html(); 
		System.out.println("Html document after zip:" + result);
		
		return result;
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
		btnmodify.setBounds(150, 310, 100, 20);
		btnmodify.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				int checkedcount = 0;
				String date = "";
				
				Boolean b = false;
				for (int i = 0; i < _datamodel.checks.size(); i++) {
					b =_datamodel.checks.get(i);
					if (b) {
						checkedcount ++;
						
						SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
						
						date = dateformat.format(_datamodel.dates.get(i));
					}
				}
				
				if (checkedcount > 1) {
					JOptionPane.showMessageDialog(DiaryManagementFrame.this, "You can only selected one Diary!");
					return;
				} else if (checkedcount == 0) {
					JOptionPane.showMessageDialog(DiaryManagementFrame.this, "Please select one Diary!");
					return;
				}
				
				DiaryEditorFrame frame = new DiaryEditorFrame(date);
				StaticDataManager.push(DiaryManagementFrame.this);
			}
			
		});
		this.add(btnmodify);

		btndelete = new JButton("删除");
		btndelete.setBounds(260, 310, 100, 20);
		btndelete.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				List<Integer> diaryids = new ArrayList<Integer>();
				
				
				Boolean b = false;
				for (int i = 0; i < _datamodel.checks.size(); i++) {
					b =_datamodel.checks.get(i);
					if (b) {
						diaryids.add(_datamodel.diaryids.get(i));
					}
				}
				
				if (diaryids.size() == 0) {
					JOptionPane.showMessageDialog(DiaryManagementFrame.this, "Please select one Diary!");
				} else {
					delete(diaryids);
					
					int id = StaticDataManager.getUID();
					initilization(id);
				};
			}
			
		});
		this.add(btndelete);
	
		JButton btnexport = new JButton("导出");
		btnexport.setBounds(370, 310, 100, 20);
		btnexport.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				List<Integer> diaryids = new ArrayList<Integer>();
				
				
				Boolean b = false;
				for (int i = 0; i < _datamodel.checks.size(); i++) {
					b =_datamodel.checks.get(i);
					if (b) {
						diaryids.add(_datamodel.diaryids.get(i));
					}
				}
				
				if (diaryids.size() == 0) {
					JOptionPane.showMessageDialog(DiaryManagementFrame.this, "Please select one Diary!");
				} else {
					
					JFileChooser chooser = new JFileChooser();
					chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					chooser.setMultiSelectionEnabled(false);
					if (JFileChooser.APPROVE_OPTION == chooser.showOpenDialog(DiaryManagementFrame.this)) {
						
						try {
							String targetfolder = chooser.getSelectedFile().getCanonicalPath() + "/";
							
							File file = new File(targetfolder);
							if (file.listFiles().length >0 ) {
								JOptionPane.showMessageDialog(DiaryManagementFrame.this, "Please chose empty folder!");
								return;
							} else {
								export(diaryids, targetfolder);
							}
							
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					
				};
			}
		});
		this.add(btnexport);
		
		JButton btnreturn = new JButton("返回");
		btnreturn.setBounds(480, 310, 100, 20);
		btnreturn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				DiaryManagementFrame.this.dispose();
				StaticDataManager.pop();
			}
		});
		this.add(btnreturn);
		
		this.addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
			
				StaticDataManager.pop();
			}
		});
		
		
		int id = StaticDataManager.getUID();
		initilization(id);// TODO
		//initilization(-1);// TODO
		
		
		this.setVisible(true);

	}

	private void delete(List<Integer> diaryids) {
		Connection conn = null;
		PreparedStatement ps = null;
		
		try {
			conn = DBManager.getconn();
			
			for (Integer id :diaryids) {
				String sql = "delete from photo where diaryid=?";
				ps = conn.prepareStatement(sql);
				ps.setInt(1, id);
	
				ps.execute();
				
				sql = "delete from diary where id = ?";
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
	
}
