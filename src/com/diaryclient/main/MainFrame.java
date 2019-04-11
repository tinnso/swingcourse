package com.diaryclient.main;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyledDocument;

import diaryclient.main.test.JTextPane2;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;

public class MainFrame extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	/*
	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();

					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	*/

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		Container container = getContentPane();// 设置一个容器

		// set layout of the main frame
		BoxLayout mainlayout = new BoxLayout(container, BoxLayout.Y_AXIS);
		container.setLayout(mainlayout);

		/******* header panel part begin ******/

		// JPanel headerPanel=new JPanel(new GridLayout(1,3,10,10));
		JPanel headerPanel = new JPanel();
		headerPanel.setMaximumSize(new Dimension(600, 30));

		// BoxLayout headerlayout=new BoxLayout(headerPanel, BoxLayout.X_AXIS);
		headerPanel.setLayout(new BorderLayout());

		JComboBox comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] { "Text Only", "Front", "End" }));
		comboBox.setPreferredSize(new Dimension(150, 20));
		headerPanel.add(comboBox, BorderLayout.WEST);

		JLabel lable = new JLabel("");
		headerPanel.add(lable, BorderLayout.CENTER);

		JPanel datePanel = new JPanel(new GridLayout(1, 1, 10, 10));
		// JLabel datelable = new JLabel("show today");
		// datePanel.add(datelable);
		headerPanel.add(datePanel, BorderLayout.EAST);

		DayChooser ser = DayChooser.getInstance();
		JTextField text = new JTextField();
		// text.setBounds(10, 10, 400, 20);
		text.setPreferredSize(new Dimension(100, 20));
		text.setText("2019-04-08");
		ser.register(text);
		datePanel.add(text);

		// second method
		/*
		 * Chooser ser2 = Chooser.getInstance("yyyy年MM月dd日"); JLabel label = new
		 * JLabel(""); label.setBounds(10, 50, 200, 30); ser2.register(label);
		 * datePanel.add(label);
		 */
		// second method

		container.add(headerPanel);

		/******* header panel part end ******/

		/******* text part begin ******/

		JTextPane mainTextPane = new JTextPane();
		// mainTextPane.setPreferredSize(new Dimension(600, 680));

		mainTextPane.setFont(new Font("仿宋", Font.PLAIN, 20));

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
							// mainTextPane.insertIcon(new ImageIcon("./resource/icons/" + iconname));

							ImageIcon imageIcon = new ImageIcon("./resource/icons/" + iconname); // Icon由图片文件形成
							Image image = imageIcon.getImage(); // 但这个图片太大不适合做Icon
							// 为把它缩小点，先要取出这个Icon的image ,然后缩放到合适的大小
							Image smallImage = image.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
							// 再由修改后的Image来生成合适的Icon
							ImageIcon smallIcon = new ImageIcon(smallImage);

							mainTextPane.insertIcon(smallIcon);
						}

						// stop other event handling
						e.consume();

					} catch (BadLocationException e1) {
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
		JButton btnimage = new JButton("add picture");

		btnimage.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				if (JFileChooser.APPROVE_OPTION == chooser.showOpenDialog(MainFrame.this)) {
					// mainTextPane.setText(mainTextPane.getText() + "\n");

					// insert a new line
					try { // 插入文本
						doc.insertString(doc.getLength(), "\n", null);
					} catch (BadLocationException e2) {
						e2.printStackTrace();
					}

					ImageIcon imageIcon = new ImageIcon(chooser.getSelectedFile().toString()); // Icon由图片文件形成
					Image image = imageIcon.getImage(); // 但这个图片太大不适合做Icon
					// 为把它缩小点，先要取出这个Icon的image ,然后缩放到合适的大小
					Image smallImage = image.getScaledInstance(600,
							imageIcon.getIconHeight() * 600 / imageIcon.getIconWidth(), Image.SCALE_SMOOTH);
					// 再由修改后的Image来生成合适的Icon
					ImageIcon smallIcon = new ImageIcon(smallImage);

					mainTextPane.insertIcon(smallIcon);

					// insert a new line
					try { // 插入文本
						doc.insertString(doc.getLength(), "\n", null);
					} catch (BadLocationException e2) {
						e2.printStackTrace();
					}
				}

			}
		});

		footer.add(btnimage);

		container.add(footer);

		/******* foot part end ******/

		setTitle("JPanel面板的案例");
		setSize(600, 300);// 设窗体的大小 宽和高
		setVisible(true);// 设定窗体的可视化
		// 设置窗体的关闭方式
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

	}

}
