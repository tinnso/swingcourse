package com.diaryclient.main;

import java.awt.BorderLayout;
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
import javax.swing.JTextField;
import javax.swing.JTextPane;
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

		
		JComboBox comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] { "Text Only", "Front", "End" }));
		comboBox.setBounds(10, 0, 150, 20);
		headerPanel.add(comboBox);

		Chooser ser = Chooser.getInstance();
		JTextField txtdate = new JTextField();
		// text.setBounds(10, 10, 400, 20);
		txtdate.setBounds(600-200+50, 0, 100, 20);
		txtdate.setText("2019-04-08");
		ser.register(txtdate);
		headerPanel.add(txtdate);

		container.add(headerPanel);

		/******* header panel part end ******/

		/******* text part begin ******/

		JTextPane mainTextPane = new JTextPane();
		
		HTMLDocument text_html;
		HTMLEditorKit htmledit;
		
		htmledit=new HTMLEditorKit();
		text_html=(HTMLDocument) htmledit.createDefaultDocument();
		mainTextPane.setEditorKit(htmledit);
		mainTextPane.setContentType("text/html");
		mainTextPane.setDocument(text_html);

		mainTextPane.setBounds(0, 50, 600, 600);
		//mainTextPane.setFont(new Font("仿宋", Font.PLAIN, 20));

		// for insert icon
		StyledDocument doc = mainTextPane.getStyledDocument();

		mainTextPane.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {

				char keyChar = e.getKeyChar();

				if (IconHandler.getInstance().checkPattern(keyChar, doc.getLength())) {
					try {
						System.out.println(String.format("%d, %d", IconHandler.getInstance().getStart(),
								IconHandler.getInstance().getOffset() - 1));

						doc.remove(IconHandler.getInstance().getStart(), IconHandler.getInstance().getOffset() - 1);

						String iconname = IconHandler.getInstance().getIconPath();

						if (iconname != null) {
							
							String filepath = new File(this.getClass().getResource("/").getFile(),
									"../resource/icons/" + iconname).getCanonicalPath();

							String imgtag = String.format("<img src='file:///%s' width='20' height='20'>", filepath);
							
							System.out.println(imgtag);
							htmledit.insertHTML(text_html, mainTextPane.getCaretPosition(), 
									imgtag, 0, 0,HTML.Tag.IMG);
							
						}

						// stop other event handling
						e.consume();

					} catch (BadLocationException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}

			}
		});

		container.add(mainTextPane);

		/******* text part end ******/

		/******* foot part begin ******/
		JPanel footer = new JPanel();
		footer.setBounds(0, 660, 600, 30);
		footer.setLayout(null);
		JButton btnimage = new JButton("add picture");
		btnimage.setBounds(350, 0, 100, 20);
		btnimage.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setFileFilter(new FileNameExtensionFilter("image(*.jpg, *.png, *.gif)", "jpg", "png", "gif"));
				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				chooser.setMultiSelectionEnabled(false);
				if (JFileChooser.APPROVE_OPTION == chooser.showOpenDialog(DiaryEditorFrame.this)) {
					// mainTextPane.setText(mainTextPane.getText() + "\n");

					
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
						

						String imgtag = String.format("<DIV><img src='file:///%s' width='%d' height='%d'></DIV>", filepath, width, height);
						
						System.out.println(imgtag);
						try {
							htmledit.insertHTML(text_html, mainTextPane.getCaretPosition(), 
									"<BR>", 0, 0,HTML.Tag.BR);
							
							htmledit.insertHTML(text_html, mainTextPane.getCaretPosition(), 
									imgtag, 0, 0,HTML.Tag.IMG);
							
							htmledit.insertHTML(text_html, mainTextPane.getCaretPosition(), 
									"<BR>", 0, 0,HTML.Tag.BR);
						} catch (BadLocationException | IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
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
				String content = mainTextPane.getText();
				
				System.out.println(content);
			}
		});
		
		footer.add(btnsave);

		container.add(footer);

		/******* foot part end ******/

		
	}
}
