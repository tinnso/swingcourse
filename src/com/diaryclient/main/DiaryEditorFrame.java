package com.diaryclient.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import com.diary.comm.DiaryUtil;
import com.diaryclient.datamgr.StaticDataManager;

public class DiaryEditorFrame extends JFrame {

	/**
	 * Launch the application.
	 */
	
	public static void main(String[] args) {

		DiaryEditorFrame frame = new DiaryEditorFrame();

		frame.setVisible(true);

		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
				
	}
	
	public DiaryEditorFrame() {
		JTextArea mainTextArea = new JTextArea();
		JTextPane mainTextPane = new JTextPane();
		
		this.setSize(610, 720);// 设窗体的大小 宽和高
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
		headerPanel.setBounds(0, 10, 600, 30);

		

		DayChooser ser = DayChooser.getInstance();
		JTextField txtdate = new JTextField();
		// text.setBounds(10, 10, 400, 20);
		txtdate.setBounds(600-200+50, 0, 100, 20);
		txtdate.setText("2019-04-08");
		ser.register(txtdate);
		headerPanel.add(txtdate);

		container.add(headerPanel);

		/******* header panel part end ******/

		/******* text part begin ******/
		
		mainTextArea.setBounds(0, 50, 600, 600);
		mainTextArea.setVisible(false);
		container.add(mainTextArea);
		
		mainTextPane.setEditable(false);
		mainTextPane.setVisible(true);
		mainTextPane.setBackground(Color.LIGHT_GRAY);

	
		HTMLDocument text_html;
		HTMLEditorKit htmledit;
		
		htmledit=new HTMLEditorKit();
		text_html=(HTMLDocument) htmledit.createDefaultDocument();
		mainTextPane.setEditorKit(htmledit);
		mainTextPane.setContentType("text/html");
		mainTextPane.setDocument(text_html);

		mainTextPane.setBounds(0, 50, 600, 600);
		//mainTextPane.setFont(new Font("仿宋", Font.PLAIN, 20));

		container.add(mainTextPane);
	

		/******* text part end ******/

		/******* foot part begin ******/
		
		
		JPanel footer = new JPanel();
		footer.setBounds(0, 660, 600, 30);
		footer.setLayout(null);
		
		JButton btninserticon = new JButton("insert icon");
		btninserticon.setBounds(250, 0, 100, 20);
		btninserticon.setEnabled(false);
		IconPicker  picker= IconPicker.getInstance();
		
		picker.register(btninserticon);
		
		picker.setCallback(new ICallback(){

			@Override
			public void callback(String args) {
				if (args == null || args.equals("")) return;
				
				String filepath;
				try {
					filepath = new File(this.getClass().getResource("/").getFile(),
							"../resource/icons/" + args).getCanonicalPath();
					
					String imgtag = String.format("<img src='file:///%s' width='20' height='20'>", filepath);
					System.out.println(imgtag);
					mainTextArea.append(imgtag);

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				
			}
			
			
		});
		
		footer.add(btninserticon);
		
		JButton btnimage = new JButton("insert picture");
		btnimage.setBounds(370, 0, 100, 20);
		btnimage.setEnabled(false);
		
		btnimage.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setFileFilter(new FileNameExtensionFilter("image(*.jpg, *.png, *.gif)", "jpg", "png", "gif"));
				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				chooser.setMultiSelectionEnabled(false);
				if (JFileChooser.APPROVE_OPTION == chooser.showOpenDialog(DiaryEditorFrame.this)) {
					
					File file =  new File(this.getClass().getResource("/").getFile(),
						"../resource/diary/" + StaticDataManager.getUserFolder());
					if (file.exists() && file.isDirectory()) {
						// do nothing
					} else {
						file.mkdir();
					}
				
					String datefolder = txtdate.getText();
					
					file = new File(file.getPath() +"/" + datefolder);
					if (file.exists() && file.isDirectory()) {
						// do nothing
					} else {
						file.mkdir();
					}
					
					File sourcefile = chooser.getSelectedFile();
					
					String randomname = UUID.randomUUID().toString() +"."+ DiaryUtil.getExtensionName(sourcefile.getName());
					
					String filepath = null;
					try {
						File destfile = new File(file.getCanonicalPath() + "/"+ randomname);
						DiaryUtil.copyFile(sourcefile, destfile);
						filepath = destfile.getCanonicalPath();
						
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					if (filepath != null) {
						
						ImageIcon imageIcon = new ImageIcon(sourcefile.toString()); // Icon由图片文件形成
						int width = imageIcon.getIconWidth();
						int height = imageIcon.getIconHeight();
						if (width >= 600) {
							width = 600;
							height = imageIcon.getIconHeight() * 600 / imageIcon.getIconWidth();
						}
						
						if (height >= 600) {
							height = 600;
							width = width * 600 / height;
						}
					
						String imgtag = String.format("<img src='file:///%s' width='%d' height='%d'>", filepath, width, height);
						
						System.out.println(imgtag);
						mainTextArea.append("\n");
						mainTextArea.append(imgtag);
						mainTextArea.append("\n");
					}
					
				}

			}
		});
		
		
		footer.add(btnimage);
		
		JButton btnsave = new JButton("save");
		btnsave.setBounds(480, 0, 100, 20);
		btnsave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				String date = txtdate.getText();
				String content = mainTextArea.getText();
				
				System.out.println(content);
			}
		});
		
		footer.add(btnsave);

		container.add(footer);

		/******* foot part end ******/

		
		JToggleButton tbEdit  = new JToggleButton("Edit", false);
		tbEdit.setBounds(10, 0, 100, 20);
		tbEdit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				boolean selected = tbEdit.isSelected();
				mainTextArea.setVisible(selected);
				mainTextPane.setVisible(!selected);
				btnimage.setEnabled(selected);
			
				if (!selected) {
					mainTextPane.setText(mainTextArea.getText().replace("\n", "<BR>"));
				}
			}
			
			
		});

		headerPanel.add(tbEdit);
		
	}
}
