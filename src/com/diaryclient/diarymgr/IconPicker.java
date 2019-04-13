package com.diaryclient.diarymgr;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.PopupFactory;
import javax.swing.SwingUtilities;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import com.diaryclient.comm.ICallback;



public class IconPicker extends JPanel {
	
private static final long serialVersionUID = -5384012731547358721L;
    
	private String selectedfilename = "";
	private String filepath = ".\\resource\\icons\\";
    private JPanel calendarPanel;
    private java.awt.Font font = new java.awt.Font("Times",java.awt.Font.PLAIN,12);
    private final LabelManager lm = new LabelManager();
    private javax.swing.Popup pop;
    private BodyPanel bodyPanel;
    
    private String iconnames[] = {
    		"closed-umbrella.png",
    		"expressionless-face.png",
    		"face-throwing-a-kiss.png",
    		"face-with-stuck-out-tongue-and-winking-eye.png",
    		"face-with-tears-of-joy.png",
    		"face-with-tears.png",
    		"face-without-mouth.png",
    		"grinning-face-with-smiling-eyes.png",
    		"grinning-face.png",
    		"hugging-face.png",
    		"kissing-face-with-smiling-eyes.png",
    		"man-and-woman-holding-hands.png",
    		"neutral-face.png",
    		"pensive-face.png",
    		"rolling-on-the-floor-laughing.png",
    		"smiling-face-with-heart-shaped-eyes.png",
    		"smiling-face-with-open-mouth-and-cold-sweat.png",
    		"smiling-face-with-open-mouth.png",
    		"smiling-face-with-smiling-eyes.png",
    		"smiling-face-with-sunglasses.png",
    		"thinking-face.png",
    		"white-frowning-face.png",
    		"white-smiling-face.png",
    		"winking-face.png"};
    
    private JComponent showDate;
    private boolean isShow = false;
    
    private static Color hoverColor = Color.BLUE; // hover color
    

    private IconPicker(){
    	
        initCalendarPanel();
    }
    

    private ICallback callback;

    public void setCallback(ICallback callback)
    {
       this.callback= callback;
    }
   
    public static IconPicker getInstance(){
        return new IconPicker();
    }
    
    private void initCalendarPanel(){
        calendarPanel = new JPanel(new java.awt.BorderLayout());
        calendarPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0xAA, 0xAA, 0xAA)));
        
        calendarPanel.add(bodyPanel = new BodyPanel(), java.awt.BorderLayout.CENTER);
        
        this.addAncestorListener(new AncestorListener() {
            public void ancestorAdded(AncestorEvent event) { }
            public void ancestorRemoved(AncestorEvent event) {hidePanel();}
            //hide pop when move component.  
            public void ancestorMoved(AncestorEvent event) {
                hidePanel();
            }
        });
    }
    public void register(final JComponent showComponent) {
        this.showDate = showComponent;
        showComponent.setRequestFocusEnabled(true);
        showComponent.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent me) {
                showComponent.requestFocusInWindow();
            }
        });
        this.add(showComponent, BorderLayout.CENTER);
        this.setPreferredSize(new Dimension(90, 25));
        this.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        showComponent.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent me) {
                if (showComponent.isEnabled()) {
                    showComponent.setCursor(new Cursor(Cursor.HAND_CURSOR));
                }
            }
            public void mouseExited(MouseEvent me) {
                if (showComponent.isEnabled()) {
                    showComponent.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    showComponent.setForeground(Color.BLACK);
                }
            }
            public void mousePressed(MouseEvent me) {
                if (showComponent.isEnabled()) {
                    showComponent.setForeground(hoverColor);
                    if (isShow) {
                        hidePanel();
                    } else {
                        showPanel(showComponent);
                    }
                }
            }
            public void mouseReleased(MouseEvent me) {
                if (showComponent.isEnabled()) {
                    showComponent.setForeground(Color.BLACK);
                }
            }
        });
        showComponent.addFocusListener(new FocusListener() {
            public void focusLost(FocusEvent e) {
                hidePanel();
            }
            public void focusGained(FocusEvent e) { }
        });
    }
    //hide the main panel.
    private void hidePanel() {
        if (pop != null) {
            isShow = false;
            pop.hide();
            pop = null;
        }
    }
 
    //show the main panel.
    private void showPanel(Component owner) {
        if (pop != null) pop.hide();
        Point show = new Point(0, showDate.getHeight());
        SwingUtilities.convertPointToScreen(show, showDate);
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        int x = show.x;
        int y = show.y;
        if (x < 0) x = 0;
        if (x > size.width - 212) x = size.width - 212;
        if (y > size.height - 167) y -= 165;
        pop = PopupFactory.getSharedInstance().getPopup(owner, calendarPanel, x, y);
        pop.show();
        isShow = true;
    }
    // change text or label's content.
    private void commit() {
    	
    	callback.callback(selectedfilename);
    	
        hidePanel();
    }
    
    // body panel.
    private class BodyPanel extends JPanel {
    	
        
        private static final long serialVersionUID = 5677718768457235447L;
        
        public BodyPanel(){
            super(new GridLayout(7, 7));
            this.setPreferredSize(new java.awt.Dimension(210, 140));
            initMonthPanel();
        }
        private void initMonthPanel(){
            updateDate();
        }
        public void updateDate() {
            this.removeAll();
            lm.clear();
           
            for (int i = 0; i < iconnames.length; i++) {
               
                IconLabel label = new IconLabel(iconnames[i]);
                lm.addLabel(label);
                this.add(label);
            }
            
        }
    }
    
    private class IconLabel extends JLabel implements java.awt.event.MouseListener, java.awt.event.MouseMotionListener {

        private static final long serialVersionUID = -6002103678554799020L;
        private boolean isSelected;
        private String filename;
        
        public IconLabel(String filename){
        	
        	super(new ImageIcon(new ImageIcon(filepath + filename).getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
        	
        	
        	File file = new File(filepath + filename);
        	System.out.println(filepath + filename + " exists :" + file.exists());
        	
            this.filename = filename;
                        
            this.setFont(font);
            this.addMouseListener(this);
            this.addMouseMotionListener(this);  
        }
        public boolean getIsSelected() {
            return isSelected;
        }
        public void setSelected(boolean b, boolean isDrag) {
            isSelected = b;
            if (b && !isDrag) {
            	selectedfilename=this.filename;
            }
        }
        
        @Override
        protected void paintComponent(Graphics g) {
        	
            if (isSelected){
                Stroke s = new BasicStroke(1.0f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_BEVEL, 1.0f, new float[] { 2.0f, 2.0f }, 1.0f);
                Graphics2D gd = (Graphics2D) g;
                gd.setStroke(s);
                gd.setColor(Color.BLACK);
                Polygon p = new Polygon();
                p.addPoint(0, 0);
                p.addPoint(getWidth() - 1, 0);
                p.addPoint(getWidth() - 1, getHeight() - 1);
                p.addPoint(0, getHeight() - 1);
                gd.drawPolygon(p);
            }
            super.paintComponent(g);
        }
        public boolean contains(Point p) {
            return this.getBounds().contains(p);
        }
        private void update() {
            repaint();
        }
        @Override
        public void mouseDragged(MouseEvent e) { }
        @Override
        public void mouseMoved(MouseEvent e) { }
        @Override
        public void mouseClicked(MouseEvent e) { }

        @Override
        public void mousePressed(MouseEvent e) {
            isSelected = true;
            selectedfilename = filename;
            update();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            Point p = SwingUtilities.convertPoint(this, e.getPoint(), bodyPanel);
            this.setForeground(Color.BLACK);
            lm.setSelect(p, false);
            commit();
        }
        @Override // change color when mouse over.
        public void mouseEntered(MouseEvent e) {
            this.setForeground(hoverColor);
            this.repaint();
        }
        @Override // change color when mouse exit.
        public void mouseExited(MouseEvent e) {

            this.setForeground(java.awt.Color.BLACK);
            this.repaint();
        }
	
    }
    
    private class LabelManager {
        private List<IconLabel> list;
        
        public LabelManager(){
            list = new ArrayList<IconLabel>();
        }
        
        public void addLabel(IconLabel label){
            list.add(label);
        }
        public void clear() {
            list.clear();
        }
        public void setSelect(Point p, boolean b) {
            //如果是拖动,则要优化一下,以提高效率  
            if (b) {
                //表示是否能返回,不用比较完所有的标签,能返回的标志就是把上一个标签和  
                //将要显示的标签找到了就可以了  
                boolean findPrevious = false, findNext = false;
                for (IconLabel lab : list) {
                    if (lab.contains(p)) {
                        findNext = true;
                        if (lab.getIsSelected()) findPrevious = true;
                        else lab.setSelected(true, b);
                    } else if (lab.getIsSelected()) {
                        findPrevious = true;
                        lab.setSelected(false, b);
                    }
                    if (findPrevious && findNext) return;
                }
            }else {
                IconLabel temp = null;
                for (IconLabel m : list) {
                    if (m.contains(p)) {
                        temp = m;
                    } else if (m.getIsSelected()) {
                        m.setSelected(false, b);
                    }
                }
                if (temp != null) temp.setSelected(true, b);
            }
        }
    }

}
