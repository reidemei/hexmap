package jr.hexmap;

import javax.swing.*;
import java.awt.*;

/**
 *  A startup-screen.
 *
 *  @version 1.0
 *  @author Jan Reidemeister
 */ 
public class StartUp
		extends JWindow
		implements jr.util.Debug {

	public StartUp () {
		
		super ();
		Dimension x = getToolkit ().getScreenSize ();
		MediaTracker mt = new MediaTracker(this);
		try {
			img = getToolkit ().getImage (this.getClass ().getClassLoader ().getSystemResource("jr/hexmap/startup.gif"));
			mt.addImage (img, 0);
		} catch (Exception e) {}
		try {
			mt.waitForAll ();
		} catch (/*Interrupted*/Exception e) {}
		this.setSize (300,200);
		this.setLocation(((int)(x.getWidth () / 2)) - 150, ((int)(x.getHeight () / 2)) - 100);
		this.setVisible (true);
		text = "Generating map.";
		err = "";
		
	} /* constructor */
	
	/**
	 *  start the repainting
	 *  @param g graphics-object, to draw on
	 */
	public void update (Graphics g) {
		
		paint (g);
		
	} /* update */
	
	/**
	 *  paints the complet object
	 *  @param g graphics-object
	 */
	public void paint (Graphics g) {
		
		g.drawImage (img, 0, 0, this);
		g.setColor (Color.red);
		g.setFont (new Font ("Serif", Font.PLAIN, 12));
		g.drawString (text, 17, 190);
		g.setColor (Color.yellow);
		g.setFont (new Font ("Serif", Font.BOLD, 12));
		g.drawString (err, 180, 190);
		
	} /* paint */
	
	/**
	 *  set the text displayed in startup-screen
	 *  @param t the text
	 */
	public void setText (String t) {
		
		text = t;
		repaint ();
		
	} /* setText */
	
	/**
	 *  set the error-text displayed in startup-screen
	 *  @param e the error-text
	 */
	public void setError (String e) {
		
		err = e;
		repaint ();
		
	} /* setText */
	
	/** the errormessage */
	private String err;
	/** the message */
	private String text;
	/** the backgroundimage */
	private Image img;
} /* class */