package net.reidemeister.hexmap;

import net.reidemeister.util.*;

import javax.swing.*;
import java.awt.*;


/**
 *  A startup-screen.
 *
 *  @version $Id: StartUp.java,v 1.1 2000/01/22 09:05:13 reidemei Exp $
 *  @author Jan Reidemeister
 */ 
public class StartUp
		  extends JWindow {

		public final static String version = "$Id: StartUp.java,v 1.1 2000/01/22 09:05:13 reidemei Exp $";

		public StartUp () {
				super ();		
				Debug.print ("StartUp - constructor");
				Dimension x = getToolkit ().getScreenSize ();
				MediaTracker mt = new MediaTracker(this);
				try {
						img = getToolkit ().getImage (this.getClass ().getClassLoader ().getSystemResource("net/reidemeister/hexmap/startup.gif"));
						mt.addImage (img, 0);
				} catch (Exception e) {}
				try {
						mt.waitForAll ();
				} catch (/*Interrupted*/Exception e) {}
				this.setSize (300,200);
				this.setLocation (((int) (x.getSize ().width / 2)) - 150, ((int) (x.getSize ().height / 2)) - 100);
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
				Debug.print ("StartUp - paint - text: " + text + ", err: " + err);
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
