package diaryclient.main.test;

import java.awt.Container;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyleConstants;



public class JTextPane2 extends JFrame {
	private JTextPane mypane;
	private JTextPane pane;
	private JScrollPane scrollMypane;
	private JScrollPane scrollPane;

	public JTextPane2() {
		super("JTextPane Test");
		Container container = getContentPane();
		container.setLayout(null);
		pane = new JTextPane();
		pane.setEditable(false);
		scrollPane = new JScrollPane(pane);
		scrollPane.setBounds(new Rectangle(0, 10, 400, 200));
		JButton insertButton = new JButton("����DƬ");
		insertButton.setBounds(new Rectangle(0, 220, 100, 40));

		insertButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				chooser.showOpenDialog(JTextPane2.this);
				mypane.setText(mypane.getText() + "\n"); // add by cs
				mypane.insertIcon(new ImageIcon(chooser.getSelectedFile().toString()));
			}
		});

		mypane = new JTextPane();
		scrollMypane = new JScrollPane(mypane);
		scrollMypane.setBounds(new Rectangle(0, 270, 400, 200));

		JButton sendButton = new JButton("�l��");
		sendButton.setBounds(new Rectangle(170, 500, 60, 40));
		sendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Vector picVector = new Vector();
				for (int i = 0; i < mypane.getStyledDocument().getRootElements()[0].getElement(0)
						.getElementCount(); i++) {
					Icon icon = StyleConstants.getIcon(mypane.getStyledDocument().getRootElements()[0].getElement(0)
							.getElement(i).getAttributes());
					if (icon != null) {
						picVector.add(icon.toString());
					}
				}
				System.err.println("�l�� JTextPane �ă����� :\n" + mypane.getText());
				int k = 0;
				for (int i = 0; i < mypane.getText().length(); i++) {
					if (mypane.getStyledDocument().getCharacterElement(i).getName().equals("icon")) {
						System.err.println("���ڵ� " + i + " �в����ˈDƬ���DƬ��·���� :");
						pane.insertIcon(new ImageIcon(picVector.get(k).toString()));
						System.err.println(picVector.get(k++).toString());
					} else {
						try {
							pane.getStyledDocument().insertString(pane.getText().length(),
									mypane.getStyledDocument().getText(i, 1), null);
						} catch (BadLocationException e1) {
							e1.printStackTrace();
						}
					}
				}
				picVector.clear();
			}
		});
		container.add(scrollPane);
		container.add(insertButton);
		container.add(scrollMypane);
		container.add(sendButton);
		setSize(400, 600);
		setVisible(true);

	}

	public static void main(String[] args) {
		new JTextPane2().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
