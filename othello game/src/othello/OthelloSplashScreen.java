package othello;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class OthelloSplashScreen {
	private final int duration;
	public OthelloSplashScreen(int duration) {
	    this.duration = duration;
	}
	  /**
	   * This method will define the splash window properties and implement a progress bar
	   * @param none
	   * @return none
	   */
	public void displaySplashWindow() {
	    JPanel content = new JPanel(new BorderLayout());
	    content.setBackground(Color.orange);
	    int width = 500;
	    int height = 500;
	    Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
	    int x = (screen.width-width)/2;
	    int y = (screen.height-height)/2;
	    JFrame jf = new JFrame();
	    jf.setUndecorated(true);
	    jf.setBounds(x,y,width,height);
	    JLabel label = new JLabel(new ImageIcon("../splash.gif"));
	    JLabel demo = new JLabel("Othello Game:", JLabel.CENTER);
	    demo.setFont(new Font(demo.getText(),demo.getFont().getStyle(),20));
	    demo.setForeground(Color.white);
	    content.add(label, BorderLayout.CENTER);
	    content.add(demo, BorderLayout.NORTH);

	    JProgressBar pgb=new JProgressBar();
	    pgb.setStringPainted(true);

	    content.add(pgb,BorderLayout.SOUTH);
	    jf.setContentPane(content);
	    jf.setVisible(true);

	    try {
	    	 for(int i=0;i<=100;i++) {
	    		 pgb.setValue(i);
	    		 pgb.setString("Loading......"+(i++)+"%");
	    		 Thread.sleep(duration/100);
				 }
	    } catch (InterruptedException e) {
					e.printStackTrace();
			}
	    jf.dispose();
		}
}
