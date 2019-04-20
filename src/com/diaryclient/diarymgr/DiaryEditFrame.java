package com.diaryclient.diarymgr;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.diaryclient.comm.DiaryUtil;
import com.diaryclient.comm.ICallback;
import com.diaryclient.datamgr.DBManager;
import com.diaryclient.datamgr.StaticDataManager;
import com.diaryclient.picturemagr.PictureManager;

public class DiaryEditFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final int WINDOW_WIDTH = 700;
	
	private ICallback callback;
	
	public void setCallback(ICallback callback)
    {
       this.callback= callback;
    }

	
	private int _diaryid = -1;
	private String _date = null;
	private int _indexcount = 0;

	private Map<String, String> _links = new HashMap<String, String>();

	private JTextArea mainTextArea = new JTextArea();
	private JTextPane mainTextPane = new JTextPane();
	
	JTextField txtdate = new JTextField(); // date
	JButton btninsert = new JButton("登录");
	JButton btnmodify = new JButton("修改");
	JButton btndelete = new JButton("删除");

	// clear the system folder of current diary, rewrite the URL related to it
	private String rewriteurl(String text, String Date) 
	{
		System.out.println("Html document before rewrite URL:" + text);
		
		String picturepath = getpicturepath(Date);
		File file = new File(picturepath);
		for (File subfile : file.listFiles()) {
			if (subfile.isFile())
				subfile.delete();
			
			// forgive about the folder
		}
		
		String iconpath = "";
		try {
			iconpath = new File(this.getClass().getResource("/").getFile(), "../resource/icons/")
					.getCanonicalPath();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		Document doc = Jsoup.parse(text);
		Elements links = doc.select("img[src]");
	
		for (Element element : links) {
			String imgUrl = element.attr("src");
			String filepath = "";
			
			String filename = imgUrl.substring(imgUrl.lastIndexOf("\\")+1);  
			if (imgUrl.contains("diary")) { // pictures
				filepath = picturepath;
			
			} else if (imgUrl.contains("icons")) { // icons
				filepath = iconpath;
				
				
			}
			String url = String.format("file:///%s\\%s", filepath, filename);
			
			element.attr("src", url);
		}
	
		String result = doc.body().html(); 
		System.out.println("Html document after rewrite URL:" + result);
		
		return result;
		
	}
	
	private String zipurl(String text)
	{
		System.out.println("Html document before zip:" + text);
		
		// convert src of image or icon, save to hashmap
		Document doc = Jsoup.parse(text);
		Elements links = doc.select("img[src]");

		String key = "";
		for (Element element : links) {
			String imgUrl = element.attr("src");
			if (imgUrl.contains("diary")) { // pictures

				key = "p" + _indexcount;
				element.attr("src");
			} else if (imgUrl.contains("icons")) { // icons
				key = "i" + _indexcount;
			}
			element.attr("src", key);
			
			_links.put(key, imgUrl);
			
			_indexcount++;
		}
	
		String result = doc.body().html(); 
		System.out.println("Html document after zip:" + result);
		
		return result;
	}
	
	private String unzipurl(String text)
	{
		System.out.println("Html document before unzip:" + text);
		
		Document doc = Jsoup.parse(text);
		Elements links = doc.select("img[src]");


		for (Element element : links) {
			String key = element.attr("src");
			element.attr("src", _links.get(key));
			
		}
		String result = doc.body().html(); 
		System.out.println("Html document after unzip:" + result);
		
		return result;
	}
	
	private void insert(String strDate) {
		if (strDate == null || strDate.equals(""))
			return;

		Date date = DiaryUtil.strToDate(strDate);

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			String text = mainTextArea.getText();
			
			// same the noraml html to db
			text = unzipurl(text);
			
			conn = DBManager.getconn();

			String sql = "insert into diary (userid, date, text, updatedate, insertdate) values (?,?,?,sysdate(),sysdate())";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, StaticDataManager.getUID());
			ps.setDate(2, new java.sql.Date(date.getTime()));
			ps.setString(3, text);
			ps.execute();

			// SELECT LAST_INSERT_ID();
			sql = "SELECT LAST_INSERT_ID() as id";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();

			int diaryid = -1;


			while (rs.next()) {
				diaryid = rs.getInt("id");
			}

			String checkpath = getpicturepath(strDate);
			
			if (diaryid != -1) {
				_diaryid = diaryid;
				Document doc = Jsoup.parse(text);
				Elements links = doc.select("img[src]");
				for (Element element : links) {
					String imgUrl = element.attr("src");
					System.out.println("imgUrl is : " + imgUrl);
					
					imgUrl = convertname(imgUrl);
					if (imgUrl.startsWith(checkpath))
						PictureManager.readImage2DB(imgUrl, diaryid);
				}
				
				btninsert.setEnabled(false);
				btnmodify.setEnabled(true);
				btndelete.setEnabled(true);

			}

			ps.close();
			conn.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
	}

	private String convertname(String filename) {
		return filename.replace("file:///", "");
	}
	
	private String getpicturepath(String strDate) 
	{
		File file = new File(this.getClass().getResource("/").getFile(),
				"../resource/diary/" + StaticDataManager.getUserFolder()+ "/" +strDate);
		if (file.exists() && file.isDirectory()) {
			// do nothing
		} else {
			file.mkdir();
		}

		String picturepatch = "";
		try {
			picturepatch = file.getCanonicalPath();
		
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("path at getpicturepath : " + picturepatch);
		
		return picturepatch;
	}
	
	private void update(String strDate) {
		if (strDate == null || strDate.equals(""))
			return;

		Date date = DiaryUtil.strToDate(strDate);

		Connection conn = null;
		PreparedStatement ps = null;
		
		String text = mainTextArea.getText();
		
		
		// same the noraml html to db
		text = unzipurl(text);
		
		try {
			conn = DBManager.getconn();

			String sql = "update diary set date=?, text=?, updatedate=sysdate() where id=?";
			ps = conn.prepareStatement(sql);
			ps.setDate(1, new java.sql.Date(date.getTime()));
			ps.setString(2, text);
			ps.setInt(3, _diaryid);
			ps.execute();

			// SELECT LAST_INSERT_ID();
			sql = "delete from photo where diaryid=?";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, _diaryid);

			ps.execute();

			String checkpath = getpicturepath(strDate);
			
			Document doc = Jsoup.parse(text);
			Elements links = doc.select("img[src]");
			for (Element element : links) {
				String imgUrl = element.attr("src");
				imgUrl = convertname(imgUrl);
				if (imgUrl.startsWith(checkpath))
					PictureManager.readImage2DB(imgUrl, _diaryid);
			}

			ps.close();
			conn.close();
			
		}catch (SQLException e) {
				e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
	}
	
	private void delete() {

		Connection conn = null;
		PreparedStatement ps = null;
		
		try {
			conn = DBManager.getconn();
			
			String sql = "delete from photo where diaryid=?";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, _diaryid);

			ps.execute();

			
			sql = "delete from diary where id = ?";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, _diaryid);
			ps.execute();
			
			ps.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
		
		_diaryid = -1;
		mainTextArea.setText("");
		mainTextPane.setText("");
		btninsert.setEnabled(true);
		btnmodify.setEnabled(false);
		btndelete.setEnabled(false);
	}

	private void initilization(String strDate) {
		if (strDate == null || strDate.equals(""))
			return;

		_links.clear();
		_indexcount = 0;
		Date date = DiaryUtil.strToDate(strDate);
		
		_date = strDate;
		
		mainTextArea.setText("");
		mainTextPane.setText("");

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBManager.getconn();
			String sql = "select * from diary where userid =? and date=?";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, StaticDataManager.getUID());
			ps.setDate(2, new java.sql.Date(date.getTime()));
			rs = ps.executeQuery();
			int diaryid = -1;
			String text = "";
			while (rs.next()) {
				diaryid = rs.getInt("id");
				text = rs.getString("text");
				
				// TODO should change text's url to real URL
			}

			if (diaryid != -1) {
				
				text = rewriteurl(text, strDate);
				
				_diaryid = diaryid;
				mainTextArea.setText(zipurl(text));
				mainTextPane.setText(text);
				//mainTextPane.setText(text.replace("\n", "<BR>"));

				File file = new File(this.getClass().getResource("/").getFile(),
						"../resource/diary/" + StaticDataManager.getUserFolder());
				if (file.exists() && file.isDirectory()) {
					// do nothing
				} else {
					file.mkdir();
				}
				
				String datefolder = strDate; // yyyy-MM-dd

				file = new File(file.getPath() + "/" + datefolder);
				if (file.exists() && file.isDirectory()) {
					// TODO should delete the files here.
					
				} else {
					file.mkdir();
				}

				PictureManager.readDB2Image(file.getCanonicalPath() + "/", diaryid);
				
				btninsert.setEnabled(false);
				btnmodify.setEnabled(true);
				btndelete.setEnabled(true);
			} else {
				btninsert.setEnabled(true);
				btnmodify.setEnabled(false);
				btndelete.setEnabled(false);
			}
			conn.close();
			ps.close();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
	}

	public DiaryEditFrame(String strDate) {

		this.setSize(WINDOW_WIDTH + 10, 720);// 设窗体的大小 宽和高
		this.setLayout(null);

		// 窗体大小不能改变
		this.setResizable(false);

		// 居中显示
		this.setLocationRelativeTo(null);

		Container container = getContentPane();// 设置一个容器

		/******* header panel part begin ******/

		// JPanel headerPanel=new JPanel(new GridLayout(1,3,10,10));
		JPanel headerPanel = new JPanel();
		headerPanel.setLayout(null);
		headerPanel.setBounds(0, 10, WINDOW_WIDTH, 30);

		DayPicker ser = DayPicker.getInstance();
		
		// when user change the date, initialize this page again
		ser.setCallback(new ICallback() {

			@Override
			public void callback(String args) {
				if (args == null || args.equals(""))
					return;

				if (!args.equals(_date)) 
				{
					initilization(args);
				}

			}

		});
		
		// text.setBounds(10, 10, 400, 20);
		txtdate.setBounds(WINDOW_WIDTH - 200 + 50, 0, 100, 20);

		if (null == strDate || strDate.contentEquals("")) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
			txtdate.setText(df.format(new Date()));
		} else {
			txtdate.setText(strDate);
		}
		
		ser.register(txtdate);
		headerPanel.add(txtdate);

		container.add(headerPanel);

		/******* header panel part end ******/

		/******* text part begin ******/

		mainTextArea.setBounds(0, 50, WINDOW_WIDTH, 600);
		mainTextArea.setVisible(false);
		container.add(mainTextArea);

		mainTextPane.setEditable(false);
		mainTextPane.setVisible(true);
		mainTextPane.setBackground(Color.LIGHT_GRAY);

		HTMLDocument text_html;
		HTMLEditorKit htmledit;

		htmledit = new HTMLEditorKit();
		text_html = (HTMLDocument) htmledit.createDefaultDocument();
		mainTextPane.setEditorKit(htmledit);
		mainTextPane.setContentType("text/html");
		mainTextPane.setDocument(text_html);

		mainTextPane.setBounds(0, 50, WINDOW_WIDTH, 600);
		// mainTextPane.setFont(new Font("仿宋", Font.PLAIN, 20));

		container.add(mainTextPane);

		/******* text part end ******/

		/******* foot part begin ******/

		JPanel footer = new JPanel();
		footer.setBounds(0, 660, WINDOW_WIDTH, 30);
		footer.setLayout(null);

		JButton btninserticon = new JButton("插入表情");
		btninserticon.setBounds(40, 0, 100, 20);
		btninserticon.setEnabled(false);
		IconPicker picker = IconPicker.getInstance();

		picker.register(btninserticon);

		picker.setCallback(new ICallback() {

			@Override
			public void callback(String args) {
				if (args == null || args.equals(""))
					return;

				String filepath;
				try {
					filepath = new File(this.getClass().getResource("/").getFile(), "../resource/icons/" + args)
							.getCanonicalPath();
					
					String url = String.format("file:///%s", filepath);
					String key = "i"+_indexcount;
					_links.put(key, url);
					
					_indexcount ++;

					String imgtag = String.format("<img src='%s' width='20' height='20'>", key);
					System.out.println(imgtag);
					mainTextArea.append(imgtag);

				} catch (IOException e) {
					e.printStackTrace();
				}

			}

		});

		footer.add(btninserticon);

		JButton btnimage = new JButton("插入图片");
		btnimage.setBounds(150, 0, 100, 20);
		btnimage.setEnabled(false);

		btnimage.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setFileFilter(new FileNameExtensionFilter("image(*.jpg, *.png, *.gif)", "jpg", "png", "gif"));
				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				chooser.setMultiSelectionEnabled(false);
				if (JFileChooser.APPROVE_OPTION == chooser.showOpenDialog(DiaryEditFrame.this)) {

					File file = new File(this.getClass().getResource("/").getFile(),
							"../resource/diary/" + StaticDataManager.getUserFolder());
					if (file.exists() && file.isDirectory()) {
						// do nothing
					} else {
						file.mkdir();
					}

					String datefolder = txtdate.getText();

					file = new File(file.getPath() + "/" + datefolder);
					if (file.exists() && file.isDirectory()) {
						// do nothing
					} else {
						file.mkdir();
					}

					File sourcefile = chooser.getSelectedFile();

					String randomname = UUID.randomUUID().toString() + "."
							+ DiaryUtil.getExtensionName(sourcefile.getName());

					String filepath = null;
					try {
						File destfile = new File(file.getCanonicalPath() + "/" + randomname);
						DiaryUtil.copyFile(sourcefile, destfile);
						filepath = destfile.getCanonicalPath();

					} catch (IOException e1) {
						e1.printStackTrace();
					}

					if (filepath != null) {

						ImageIcon imageIcon = new ImageIcon(sourcefile.toString()); // Icon由图片文件形成
						int width = imageIcon.getIconWidth();
						int height = imageIcon.getIconHeight();
						if (width >= WINDOW_WIDTH) {
							width = WINDOW_WIDTH;
							height = imageIcon.getIconHeight() * WINDOW_WIDTH / imageIcon.getIconWidth();
						}

						if (height >= 600) {
							height = 600;
							width = width * 600 / height;
						}

						String url = String.format("file:///%s", filepath);
						String key = "p"+_indexcount;
						_links.put(key, url);
						
						_indexcount ++;

						String imgtag = String.format("<img src='%s' width='%d' height='%d'>", key, width, height);

						System.out.println(imgtag);
						mainTextArea.append("\n");
						mainTextArea.append(imgtag);
						mainTextArea.append("\n");
					}

				}

			}
		});

		footer.add(btnimage);

		
		btninsert.setBounds(260, 0, 100, 20);
		btninsert.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				String date = txtdate.getText();
				insert(date);

			}
		});

		footer.add(btninsert);

		
		btnmodify.setBounds(370, 0, 100, 20);
		btnmodify.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				String date = txtdate.getText();
				String content = mainTextArea.getText();

				System.out.println(content);
				update(date);
			}
		});

		footer.add(btnmodify);

		
		btndelete.setBounds(480, 0, 100, 20);
		btndelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				delete();
			}
		});

		footer.add(btndelete);
		
		JButton btnreturn = new JButton("返回");
		btnreturn.setBounds(590, 0, 100, 20);
		btnreturn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if ( callback != null)				
					callback.callback(null);
				StaticDataManager.pop();
			}
		});

		footer.add(btnreturn);

		container.add(footer);

		/******* foot part end ******/

		JToggleButton tbEdit = new JToggleButton("预览模式", false);
		tbEdit.setBounds(10, 0, 100, 20);
		tbEdit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				boolean selected = tbEdit.isSelected();
				mainTextArea.setVisible(selected);
				mainTextPane.setVisible(!selected);
				btnimage.setEnabled(selected);
				btninserticon.setEnabled(selected);

				if (!selected) {
					tbEdit.setText("观看模式");
					String text = mainTextArea.getText();
					System.out.println("text from text area: " + text);
					text = unzipurl(text);
					//text =text.replace("\n", "<BR>");
					System.out.println("text after unzip: " + text);
					
					mainTextPane.setText(text);
				} else {
					tbEdit.setText("编辑模式");
				}
			}

		});

		headerPanel.add(tbEdit);

		initilization(txtdate.getText());

		
		this.addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				StaticDataManager.pop();
			}
		});
	}
}
