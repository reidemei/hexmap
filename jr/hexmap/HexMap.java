package jr.hexmap;

import jr.util.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import Acme.JPM.Encoders.*;

/**
 *  Hexfield Map Editor
 *
 *  @version 0.9
 *  @author Jan Reidemeister
 */ 
public class HexMap
		extends JFrame 
		implements Debug, WindowListener, ActionListener, MouseListener, MouseMotionListener, AdjustmentListener, TreeSelectionListener {

	/**
	 *  Constructor
	 */
	public HexMap () {
		
		super ("Hexfield Map Editor 0.9");
	
		version = new String ("0.9");
		startup = new StartUp ();
		imageFac = new ImageFactory ();
		
		System.out.println ("\n    Hexfield Map Editor " + version + " build 19990405\n    (c) 1998-1999 Jan Reidemeister (J.R.@gmx.de) \n    http://JanR.home.pages.de\n");
		
		this.addWindowListener (this);
		this.setSize (685,495);
		Dimension x = getToolkit ().getScreenSize ();
		this.setLocation(((int)(x.getWidth () / 2)) - 335, ((int)(x.getHeight () / 2)) - 225);
		this.setResizable (false);
		this.setIconImage (getToolkit ().getImage (this.getClass ().getClassLoader ().getSystemResource("jr/hexmap/icon.gif")));
		
		this.initUI ();
		
		this.setVisible (true);
		this.setDefaultCloseOperation (JFrame.DO_NOTHING_ON_CLOSE);
		startup.toFront ();
		startup = null;
	} /* Constructor */
	
	/**
	 *  Creates the GUI.
	 */
	private void initUI () {
		
		// the hexmap
		map = new Map (startup, imageFac);
		map.addMouseListener (this);
		map.addMouseMotionListener (this);
	
		street = 0;
		street = street ^ (0x000001);
		streetImg = map.source[16];
		river = street;
		riverImg = map.source[22];
			
		startup.setText ("Initialising Layout.");
		
		// menu
		JMenuBar menuBar;
		JMenu menu;
		JMenuItem menuItem;
		JCheckBox checkBox;
		
		menuBar = new JMenuBar();
		setJMenuBar (menuBar);
		
		menu = new JMenu ("File");
		menuBar.add (menu);
		
		menuItem = new JMenuItem ("New");
		menuItem.setActionCommand ("new");
		menuItem.addActionListener (this);
		menu.add (menuItem);
		menu.addSeparator ();
		menuItem = new JMenuItem ("Load");
		menuItem.setActionCommand ("load");
		menuItem.addActionListener (this);
		menu.add (menuItem);
		menuItem = new JMenuItem("Save");
		menuItem.setActionCommand ("save");
		menuItem.addActionListener (this);
		menu.add (menuItem);
		menu.addSeparator ();
		menuItem = new JMenuItem ("Export GIF");
		menuItem.setActionCommand ("export");
		menuItem.addActionListener (this);
		menu.add (menuItem);
		menu.addSeparator ();
		menuItem = new JMenuItem ("Quit");
		menuItem.setActionCommand ("quit");
		menuItem.addActionListener (this);
		menu.add (menuItem);		
		
		menu = new JMenu ("Options");
		menuBar.add (menu);
	
		checkBoxHex = new JCheckBoxMenuItem ("Show HexNumbers");
		checkBoxHex.setSelected (true);
		checkBoxHex.setActionCommand ("hexnumbers");
		checkBoxHex.addActionListener (this);
		menu.add (checkBoxHex);
		checkBoxUnit = new JCheckBoxMenuItem ("Show UnitNames");
		checkBoxUnit.setSelected (false);
		checkBoxUnit.setActionCommand ("unitnames");
		checkBoxUnit.addActionListener (this);
		menu.add (checkBoxUnit);
		
		menuItem = new JMenuItem ("Info");
		menuItem.setActionCommand ("info");
		menuItem.addActionListener (this);
		menuBar.add (menuItem);
		menuBar.add (Box.createHorizontalGlue ());

		// tree
		DefaultMutableTreeNode node = null;
		DefaultMutableTreeNode top = new DefaultMutableTreeNode("Tools");
		TreePath path = new TreePath (top);
		tree = new JTree(top);    
		tree.getSelectionModel().setSelectionMode (TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.addTreeSelectionListener (this);
		DefaultMutableTreeNode level = new DefaultMutableTreeNode("Level");
		top.add(level);
		path = path.pathByAddingChild (level);
		node = new DefaultMutableTreeNode("Level -3");
		level.add(node);
		node = new DefaultMutableTreeNode("Level -2");
		level.add(node);
		node = new DefaultMutableTreeNode("Level -1");
		level.add(node);
		node = new DefaultMutableTreeNode("Level 0");
		path = path.pathByAddingChild (node);
		level.add(node);
		node = new DefaultMutableTreeNode("Level 1");
		level.add(node);
		node = new DefaultMutableTreeNode("Level 2");
		level.add(node);
		node = new DefaultMutableTreeNode("Level 3");
		level.add(node);
		node = new DefaultMutableTreeNode("Level 4");
		level.add(node);
		node = new DefaultMutableTreeNode("Delete");
		top.add(node);
		
		DefaultMutableTreeNode wood = new DefaultMutableTreeNode("Wood");
		top.add(wood);
		node = new DefaultMutableTreeNode("Sparse");
		wood.add(node);
		node = new DefaultMutableTreeNode("Dense");
		wood.add(node);
//		node = new DefaultMutableTreeNode("Swamp");
//		wood.add(node);
		
		DefaultMutableTreeNode street = new DefaultMutableTreeNode("Street");
		top.add(street);
		
		DefaultMutableTreeNode river = new DefaultMutableTreeNode("River");
		top.add(river);
		
		DefaultMutableTreeNode buildings = new DefaultMutableTreeNode("Buildings");
		top.add(buildings);
		node = new DefaultMutableTreeNode("Asphalt");
		buildings.add(node);
		node = new DefaultMutableTreeNode("Light");
		buildings.add(node);
		node = new DefaultMutableTreeNode("Medium");
		buildings.add(node);
		node = new DefaultMutableTreeNode("Heavy");
		buildings.add(node);
		node = new DefaultMutableTreeNode("Very Heavy");
		buildings.add(node);
		//node = new DefaultMutableTreeNode("Ruins");
		//buildings.add(node);
		
		DefaultMutableTreeNode special = new DefaultMutableTreeNode("Special");
		top.add(special);
		node = new DefaultMutableTreeNode("Rough");
		special.add(node);
		node = new DefaultMutableTreeNode("Fire");
		special.add(node);
		node = new DefaultMutableTreeNode("Smoke");
		special.add(node);
		node = new DefaultMutableTreeNode("Flag");
		special.add(node);
		node = new DefaultMutableTreeNode("Text");
		special.add(node);
		
		// add all units
		if (map.units.size () > 0) {
			DefaultMutableTreeNode units = new DefaultMutableTreeNode("Units");
			top.add(units);
			for (int i = 0; i < map.units.size (); i++) {
				node = new DefaultMutableTreeNode(((Unit) map.units.elementAt (i)).abk);
				units.add (node);
			} /* for */
		} /* if */
			
		// Center - map and scrollbars
		JPanel pCenter = new JPanel ();
		pCenter.setBorder (new EtchedBorder ());
		pCenter.setLayout (new BorderLayout ());
		map.setBorder (new EtchedBorder ());
		pCenter.add (map, BorderLayout.CENTER);
		sbarMapH = new JScrollBar (JScrollBar.HORIZONTAL,1,10,1,17);
		sbarMapH.addAdjustmentListener (this);
		pCenter.add (sbarMapH, BorderLayout.SOUTH);
		sbarMapV = new JScrollBar (JScrollBar.VERTICAL,1,6,1,17);
		sbarMapV.addAdjustmentListener (this);
		pCenter.add (sbarMapV, BorderLayout.EAST);
		
		// Icon - icon and textfield
		JPanel pImg = new JPanel ();
		JPanel pHelp = null;
		pImg.setBorder (new EtchedBorder ());
		img = new JLabel("");
		img.addMouseListener (this);
		label = new JLabel("");
		label.setFont (new Font ("SansSerif", Font.BOLD, 12));
		text  = new JTextField ("",3);
		text.setVisible (false);
		JPanel pText = new JPanel ();
		pText.setLayout (new BorderLayout ());
		pHelp = new JPanel ();
		pHelp.setLayout (new FlowLayout ());
		pHelp.add (label);
		pText.add (pHelp, BorderLayout.CENTER);
		pHelp = new JPanel ();
		pHelp.setLayout (new FlowLayout ());
		pHelp.add (text);
		pText.add (pHelp, BorderLayout.SOUTH);
		pImg.setLayout (new BorderLayout ());
		pImg.add (img, BorderLayout.WEST);
		pImg.add (pText, BorderLayout.CENTER);
		pImg.setMinimumSize (new Dimension (142, 70));
		pImg.setPreferredSize (new Dimension (142, 70));

		// Tree - tree and icon
		JPanel pTree = new JPanel ();
		pTree.setLayout (new BorderLayout ());
		pTree.add (new JScrollPane (tree), BorderLayout.CENTER);
		pTree.add (pImg, BorderLayout.SOUTH);
		
		// Status 
		status = new JLabel ();
		JPanel pStatus = new JPanel ();
		pStatus.setLayout (new FlowLayout ());
		pStatus.add (status);
		pStatus.setBorder (new EtchedBorder ());
		
		// All - pWest and pCenter
		this.getContentPane ().setLayout (new BorderLayout ());
		this.getContentPane ().add (pCenter, BorderLayout.CENTER);
		this.getContentPane ().add (pTree, BorderLayout.WEST);
		this.getContentPane ().add (pStatus, BorderLayout.SOUTH);
		
		// Open the tree
		tree.setSelectionPath (path);
		label.setText ("Level 0");
		img.setIcon (new ImageIcon (map.source[3].getScaledInstance (48, 41, Image.SCALE_SMOOTH)));
			status.setText ("Level 0: (click on map to set the level for a hexfield)");
		if (startup != null)
			startup.setText ("Drawing map.");
	} /* initUI */
	
	/**
	 *  
	 */
	public static void main (String args[]) {
		String laf = "";
		if (args.length > 0) {
			if (args[0].startsWith ("-")) {
				if (args[0].equals ("-debug")) {
				  //DEBUG = true;
				} /* if */
				if (args.length > 1)
					laf = args[1];
			} else 
				laf = args[0];
			if (laf.equals ("windows")) {
   			try {
   				UIManager.setLookAndFeel ("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
   			} catch (Exception e) {System.out.println ("ERROR: Look and Feel not found.");}
			} /* if */
			if (laf.equals ("metal")) {
   			try {
   				UIManager.setLookAndFeel ("javax.swing.plaf.metal.MetalLookAndFeel");
   			} catch (Exception e) {System.out.println ("ERROR: Look and Feel not found.");}
			} /* if */
			if (laf.equals ("motif")) {
   			try {
   				UIManager.setLookAndFeel ("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
   			} catch (Exception e) {System.out.println ("ERROR: Look and Feel not found.");}
			} /* if */
			if (laf.equals ("mac")) {
   			try {
   				UIManager.setLookAndFeel ("javax.swing.plaf.mac.MacLookAndFeel");
   			} catch (Exception e) {System.out.println ("ERROR: Look and Feel not found.");}
			} /* if */
		} /* if */		
		HexMap me = new HexMap ();
	} /* main */
	
	public void windowOpened (WindowEvent e) {}
	
	public void windowClosing (WindowEvent e) {
		int n = JOptionPane.showConfirmDialog(
						new JFrame(), "Really quit?",
						"Hexfield Map Editor 1.0",
						JOptionPane.YES_NO_OPTION);
		if (n == JOptionPane.YES_OPTION) {
			System.exit (0);
		} /* if */ 
		return;
	} /* windowClosing */
	
	public void windowClosed (WindowEvent e) {}
	
	public void windowIconified (WindowEvent e) {}
			 	
	public void windowDeiconified (WindowEvent e) {}
			 	
	public void windowActivated (WindowEvent e) {}
			 	
	public void windowDeactivated (WindowEvent e) {}
			 	
	public void valueChanged (TreeSelectionEvent e) {
		Image helpImg = createImage (68, 59);
		if (helpImg == null)
			return;
		Graphics helpGraphics = helpImg.getGraphics ();
		String who = e.getNewLeadSelectionPath ().getLastPathComponent ().toString ();
		if (who.equals ("Tools") || who.equals ("Level") || who.equals ("Wood") || who.equals ("Buildings") || who.equals ("Special") || who.equals ("Units")) {
			return;
		} /* if */
		text.setVisible (false);
		if (e.getNewLeadSelectionPath ().getParentPath ().getLastPathComponent ().toString ().equals ("Units")) {
			for (unit = 0; unit < map.units.size(); unit++) {
				if (((Unit)map.units.elementAt(unit)).abk.equals (who))
					break;	    
			} /* for */
			typ = 5;
			label.setText (((Unit) map.units.elementAt (unit)).abk);
			label.setToolTipText (((Unit) map.units.elementAt (unit)).name);
			img.setToolTipText (((Unit) map.units.elementAt (unit)).name);
			img.setIcon (new ImageIcon (imageFac.cropImage (imageFac.rotateAndColorImage (((Unit) map.units.elementAt (unit)).icon, rotate * 60, color),10, 9, 48, 41)));
			status.setText ("Unit: " + ((Unit) map.units.elementAt (unit)).name + " (leftclick on uniticon for rotate, rightclick for colorchange; click on map to place)");
			return;
		} /* if */
		label.setText (who);
		if (e.getNewLeadSelectionPath ().getParentPath ().getLastPathComponent ().toString ().equals ("Level")) {
			status.setText (who + ": (click on map to set the level for a hexfield)");
			typ = 1;
			if (who.equals ("Level -3")) {
				mod = -3;
				helpGraphics.drawImage (map.source [0], 0, 0, this);				
			} /* if */
			if (who.equals ("Level -2")) {
				mod = -2;
				helpGraphics.drawImage (map.source [1], 0, 0, this);				
			} /* if */
			if (who.equals ("Level -1")) {
				mod = -1;
				helpGraphics.drawImage (map.source [2], 0, 0, this);				
			} /* if */
			if (who.equals ("Level 0")) {
				mod = 0;
				helpGraphics.drawImage (map.source [3], 0, 0, this);				
			} /* if */
			if (who.equals ("Level 1")) {
				mod = 1;
				helpGraphics.drawImage (map.source [4], 0, 0, this);				
			} /* if */
			if (who.equals ("Level 2")) {
				mod = 2;
				helpGraphics.drawImage (map.source [5], 0, 0, this);				
			} /* if */
			if (who.equals ("Level 3")) {
				mod = 3;
				helpGraphics.drawImage (map.source [6], 0, 0, this);				
			} /* if */
			if (who.equals ("Level 4")) {
				mod = 4;
				helpGraphics.drawImage (map.source [7], 0, 0, this);				
			} /* if */
		} /* if */
		if (e.getNewLeadSelectionPath ().getParentPath ().getLastPathComponent ().toString ().equals ("Tools")) {
			typ = 2;
			if (who.equals ("Delete")) {
				mod = 0;
				helpGraphics.drawImage (map.source [13], 0, 0, this);				
				status.setText ("Delete: (click on map to erase hexfield)");
			} /* if */
			if (who.equals ("Street")) {
				typ = 3;
				helpGraphics.drawImage (streetImg, 0, 0, this);				
				status.setText ("Street: (click on the 6 lines of the icon to add/remove streets; click on map to place)");
			} /* if */
			if (who.equals ("River")) {
				typ = 4;
				helpGraphics.drawImage (riverImg, 0, 0, this);				
				status.setText ("River: (click on the 6 lines of the icon to add/remove river; click on map to place)");
			} /* if */
		} /* if */
		if (e.getNewLeadSelectionPath ().getParentPath ().getLastPathComponent ().toString ().equals ("Wood")) {
			typ = 2;
			if (who.equals ("Sparse")) {
				mod = 1;
				helpGraphics.drawImage (map.source [9], 0, 0, this);
				status.setText ("Sparse Wood: (click on map to place sparse wood on a hexfield)");
			} /* if */
			if (who.equals ("Dense")) {
				mod = 2;
				helpGraphics.drawImage (map.source [10], 0, 0, this);
				status.setText ("Dense Wood: (click on map to place dense wood on a hexfield)");
			} /* if */
			if (who.equals ("Swamp")) {
				mod = 2;
				status.setText ("Swamp: (click on map to place swamp on a hexfield)");
			} /* if */
		} /* if */
		if (e.getNewLeadSelectionPath ().getParentPath ().getLastPathComponent ().toString ().equals ("Buildings")) {
			typ = 2;
			if (who.equals ("Asphalt")) {
				mod = 4;
				helpGraphics.drawImage (map.source [12], 0, 0, this);
				status.setText ("Asphalt: (click on map to place asphalt on a hexfield)");
			} /* if */
			if (who.equals ("Light")) {
				mod = 6;
				helpGraphics.drawImage (map.source [29], 0, 0, this);
				status.setText ("Light building: (click on map to place asphalt on a hexfield)");
			} /* if */
			if (who.equals ("Medium")) {
				mod = 7;
				helpGraphics.drawImage (map.source [30], 0, 0, this);
				status.setText ("Medium building: (click on map to place asphalt on a hexfield)");
			} /* if */
			if (who.equals ("Heavy")) {
				mod = 8;
				helpGraphics.drawImage (map.source [31], 0, 0, this);
				status.setText ("Heavy building: (click on map to place asphalt on a hexfield)");
			} /* if */
			if (who.equals ("Very Heavy")) {
				mod = 9;
				helpGraphics.drawImage (map.source [32], 0, 0, this);
				status.setText ("Very heavy building: (click on map to place on a hexfield)");
			} /* if */
		} /* if */
		if (e.getNewLeadSelectionPath ().getParentPath ().getLastPathComponent ().toString ().equals ("Special")) {
			if (who.equals ("Rough")) {
				typ = 7;
				helpGraphics.drawImage (map.source [11], 0, 0, this);
				status.setText ("Rough: (click on map to mark a hexfield rough)");
			} /* if */
			if (who.equals ("Fire")) {
				typ = 6;
				mod = 2;
				helpGraphics.drawImage (map.source [14], 0, 0, this);
				status.setText ("Fire: (click on map to place fire on a hexfield)");
			} /* if */
			if (who.equals ("Smoke")) {
				typ = 6;
				mod = 3;
				helpGraphics.drawImage (map.source [15], 0, 0, this);
				status.setText ("Smoke: (click on map to place smoke on a hexfield)");
			} /* if */
			if (who.equals ("Flag")) {
				typ = 8;
				helpGraphics.drawImage (imageFac.colorImage (map.source [28], color), 0, 0, this);
				status.setText ("Flag: (rightclick on the icon to change the color; click on map to place the flag on a hexfield)");
			} /* if */
			if (who.equals ("Text")) {
				typ = 9;
				helpGraphics.drawImage (map.source [13], 0, 0, this);
				text.setVisible (true);
				status.setText ("Text: (enter some text and click on the map to place on a hexfield)");
			} /* if */
		} /* if */
		img.setIcon (new ImageIcon (helpImg.getScaledInstance (48, 41, Image.SCALE_SMOOTH)));
	} /* valueChanged */
	
	public void actionPerformed (ActionEvent e) {
		if (e.getActionCommand ().equals ("hexnumbers")) {
			map.setShowHexNumbers (checkBoxHex.isSelected ());
			return;
		} /* if */
		if (e.getActionCommand ().equals ("unitnames")) {
			map.setShowUnitNames (checkBoxUnit.isSelected ());
			return;
		} /* if */
		if (e.getActionCommand ().equals ("save")) {
			this.setTitle ("Hexfield Map Editor " + version + " - Saving Map ...");
			JFileChooser chooser = new JFileChooser(); 
			
			jr.util.ExampleFileFilter filter = new jr.util.ExampleFileFilter ("map","Hexfield map");
			chooser.addChoosableFileFilter(filter);
			chooser.setFileFilter(filter);
			chooser.removeChoosableFileFilter(chooser.getAcceptAllFileFilter());
				
			int returnVal = chooser.showSaveDialog(this.getContentPane ());
			if(returnVal == JFileChooser.APPROVE_OPTION) {
				try {
					String file = chooser.getSelectedFile ().getName ();
					java.io.FileOutputStream out;
					if (file.endsWith(".map"))
						out = new java.io.FileOutputStream (chooser.getSelectedFile ());
					else
						out = new java.io.FileOutputStream (new java.io.File (chooser.getSelectedFile ().getParent (), file + ".map"));
					map.saveMap (out);
				} catch (Exception err) {}
				System.out.println("Done. Map " + chooser.getSelectedFile().getName() + " saved."); 
			} /* if */
			this.setTitle ("Hexfield Map Editor " + version);
			return;
		} /* if */
		if (e.getActionCommand ().equals ("load")) {
			this.setTitle ("Hexfield Map Editor " + version + " - Loading Map ...");
			JFileChooser chooser = new JFileChooser(); 
			
			jr.util.ExampleFileFilter filter;
			filter = new jr.util.ExampleFileFilter ("map","Hexfield map");
			chooser.addChoosableFileFilter(filter);
			chooser.setFileFilter(filter);
			chooser.removeChoosableFileFilter(chooser.getAcceptAllFileFilter());
				
			int returnVal = chooser.showOpenDialog(this.getContentPane ());
			if(returnVal == JFileChooser.APPROVE_OPTION) {
				try {
					java.io.FileInputStream out = new java.io.FileInputStream (chooser.getSelectedFile ());
					if (map.loadMap (out)) {
						map.setXView (1);	map.setYView (1);
						sbarMapH.setMaximum (map.X_Size+1);	sbarMapV.setMaximum (map.Y_Size+1);
						sbarMapH.setValue (1);	sbarMapV.setValue (1);
					} /* if */
				} catch (Exception err) {}
				System.out.println("Done. Map " + chooser.getSelectedFile().getName() + " loaded."); 
			} /* if */
			this.setTitle ("Hexfield Map Editor " + version);
			return;
		} /* if */
		if (e.getActionCommand ().equals ("new")) {
			// open an optiondialog to choose the new mapsize
			Object[] possibilities = {"10x10", "10x16", "10x32", "16x10", "16x16", "16x32", "32x10", "32x16", "32x32"};
			String s = (String)JOptionPane.showInputDialog (	new JFrame(), "Select the size of the new map!", "New map", JOptionPane.PLAIN_MESSAGE, null, possibilities, "16x16");
			if (s != null) {
				s = s.trim();
				if (s.equals ("10x10"))		map.makeMap (10, 10);
				if (s.equals ("10x16"))		map.makeMap (10, 16);
				if (s.equals ("10x32"))		map.makeMap (10, 32);
				if (s.equals ("16x10"))		map.makeMap (16, 10);
				if (s.equals ("16x16"))		map.makeMap (16, 16);
				if (s.equals ("16x32"))		map.makeMap (16, 32);
				if (s.equals ("32x10"))		map.makeMap (32, 10);
				if (s.equals ("32x16"))		map.makeMap (32, 16);
				if (s.equals ("32x32"))		map.makeMap (32, 32);
				map.repaint ();
				map.setXView (1);	map.setYView (1);
				sbarMapH.setValue (1);	sbarMapV.setValue (1);
				sbarMapH.setMaximum (map.X_Size+1);	sbarMapV.setMaximum (map.Y_Size+1);
			} /* if */
			return;
		} /* if */
		if (e.getActionCommand ().equals ("info")) {
			Object[] options = {"Dismiss"};
			JOptionPane.showOptionDialog (new JFrame(),
								"A tool to create hexfield maps. Supports userdefined graphics and units.\n\n" + 
								"(c) 1998-1999 by Jan Reidemeister <J.R.@gmx.de>\n" +
								"http://JanR.home.pages.de\n\n" +
								"THIS SOFTWARE IS PROVIDED ON AN \"AS IS\"\nBASIS WITHOUT WARRANTY OF ANY KIND." ,
								"Hexfield Map Editor " + version, JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, "Dismiss");
			return;
		} /* if */
		if (e.getActionCommand ().equals ("export")) {
			this.setTitle ("Hexfield Map Editor " + version + " - Saving Image ...");
			JFileChooser chooser = new JFileChooser(); 
			
			jr.util.ExampleFileFilter filtergif = new jr.util.ExampleFileFilter ("gif","Gif-Images");
			chooser.addChoosableFileFilter(filtergif);
			chooser.setFileFilter(filtergif);
			chooser.removeChoosableFileFilter(chooser.getAcceptAllFileFilter());
				
			int returnVal = chooser.showSaveDialog(this.getContentPane ());
			if(returnVal == JFileChooser.APPROVE_OPTION) {
				try {
					java.io.FileOutputStream out;
					Acme.JPM.Encoders.ImageEncoder coder = null;
					String file = chooser.getSelectedFile ().getName ();
					if (chooser.getFileFilter() == filtergif) {
						if (file.endsWith(".gif"))
							out = new java.io.FileOutputStream (chooser.getSelectedFile ());
						else
							out = new java.io.FileOutputStream (new java.io.File (chooser.getSelectedFile ().getParent (), file + ".gif"));
						coder = new Acme.JPM.Encoders.GifEncoder (map.getImage (), out);
					} /* if */
					coder.encode ();
				} catch (Exception err) {}
				System.out.println("Done. File " + chooser.getSelectedFile().getName() + " saved."); 
			} /* if */
			this.setTitle ("Hexfield Map Editor " + version);
			return;
		} /* if */
		if (e.getActionCommand ().equals ("quit")) {
			int n = JOptionPane.showConfirmDialog (new JFrame(), "Really quit?", "Hexfield Map Editor", JOptionPane.YES_NO_OPTION);
			if (n == JOptionPane.YES_OPTION) 	System.exit (0);
			return;
		} /* if */
	} /* actionPerformed */
	
	public void mouseClicked (MouseEvent e) {
		if (e.getSource () == img) {
			if (typ == 3) {
				int x = e.getX ();
				int y = e.getY ();
				if (y < 35){
					if (x < 16 )  // 6
						street = street ^ (0x100000);
					else {
						if (x < 31) // 1
							street = street ^ (0x000001);
						else // 2
							street = street ^ (0x000010);
					} /* else */
				} else {
					if (x < 16) // 5
						street = street ^ (0x010000);
					else { 
						if (x < 31) // 4
							street = street ^ (0x001000);
						else // 3
							street = street ^ (0x000100);
					} /* else */
				} /* else */
				streetImg = createImage (68, 59);
				Graphics dbGraphics = streetImg.getGraphics ();
				dbGraphics.drawImage (map.source[13], 0, 0, this);				
				if ((street & 0x000001) == 1)
					dbGraphics.drawImage (map.source[16], 0, 0, this);
				if ((street & 0x000010) == 0x10)
					dbGraphics.drawImage (map.source[17], 0, 0, this);
				if ((street & 0x000100) == 0x100)
					dbGraphics.drawImage (map.source[18], 0, 0, this);
				if ((street & 0x001000) == 0x1000)
					dbGraphics.drawImage (map.source[19], 0, 0, this);
				if ((street & 0x010000) == 0x10000)
					dbGraphics.drawImage (map.source[20], 0, 0, this);
				if ((street & 0x100000) == 0x100000)
					dbGraphics.drawImage (map.source[21], 0, 0, this);
				img.setIcon (new ImageIcon (streetImg.getScaledInstance (48, 41, Image.SCALE_SMOOTH)));	
				return;
			} /* if */
			if (typ == 4) {
				int x = e.getX ();
				int y = e.getY ();
				if (y < 35){
					if (x < 16 )  // 6
						river = river ^ (0x100000);
					else {
						if (x < 31) // 1
							river = river ^ (0x000001);
						else // 2
							river = river ^ (0x000010);
					} /* else */
				} else {
					if (x < 16) // 5
						river = river ^ (0x010000);
					else { 
						if (x < 31) // 4
							river = river ^ (0x001000);
						else // 3
							river = river ^ (0x000100);
					} /* else */
				} /* else */
				riverImg = createImage (68, 59);
				Graphics dbGraphics = riverImg.getGraphics ();
				dbGraphics.drawImage (map.source[13], 0, 0, this);				
				if ((river & 0x000001) == 1)
					dbGraphics.drawImage (map.source[22], 0, 0, this);
				if ((river & 0x000010) == 0x10)
					dbGraphics.drawImage (map.source[23], 0, 0, this);
				if ((river & 0x000100) == 0x100)
					dbGraphics.drawImage (map.source[24], 0, 0, this);
				if ((river & 0x001000) == 0x1000)
					dbGraphics.drawImage (map.source[25], 0, 0, this);
				if ((river & 0x010000) == 0x10000)
					dbGraphics.drawImage (map.source[26], 0, 0, this);
				if ((river & 0x100000) == 0x100000)
					dbGraphics.drawImage (map.source[27], 0, 0, this);
				img.setIcon (new ImageIcon (riverImg.getScaledInstance (48, 41, Image.SCALE_SMOOTH)));	
				return;
			} /* if */
			if (e.getModifiers() == e.BUTTON3_MASK) {
				color = color + 1;
				if (color == 5)
					color = 0;
			} else {
				rotate = rotate + 1;
				if (rotate == 6)
					rotate = 0;
			} /* else */
			if (typ == 8)
				img.setIcon (new ImageIcon (imageFac.colorImage (map.source[28], color).getScaledInstance (48, 41, Image.SCALE_SMOOTH)));
			else 
				img.setIcon (new ImageIcon (imageFac.cropImage (imageFac.rotateAndColorImage (((Unit) map.units.elementAt (unit)).icon, rotate * 60, color),10, 9, 48, 41)));
			return;
		} /* if */
		switch (typ) {
			case 1: map.setLevel (mod);
							break;
			case 2: map.setMod (mod);
							break;
			case 3: map.setStreet (street);
							break;
			case 4: map.setRiver (river);
							break;
			case 5: map.setUnit (((Unit)map.units.elementAt(unit)).abk, rotate, color);
							break;
			case 6: map.setSpecial (mod);
							break;
			case 7: map.setRough (true);
							break;
			case 8: map.setFlag (color);
							break;
			case 9: map.setText (text.getText ());
							break;
		} /* switch */
	} /* mouseClicked */
	
	public void mouseMoved (MouseEvent e) {}
	
	public void mouseDragged (MouseEvent e) {
		mouseClicked (e);
	} /* mouseDragged */
	
	public void mousePressed (MouseEvent e) {}
			 	
	public void mouseEntered (MouseEvent e) {}
			 
	public void mouseExited (MouseEvent e) {}
			 	
	public void mouseReleased (MouseEvent e) {}
			 	
	public void adjustmentValueChanged (AdjustmentEvent e) {
		if (e.getSource() == sbarMapH) {
			map.setXView (sbarMapH.getValue ());
		} /* if */
		if (e.getSource() == sbarMapV) {
			map.setYView (sbarMapV.getValue ());
		} /* if */
	} /* adjustmentValueChanged */
	
	private JLabel img;
	private JLabel label;
	private JLabel status;
	private JTree tree;
	private JScrollBar sbarMapH;
	private JScrollBar sbarMapV;
	private JTextField text;
	private JCheckBoxMenuItem checkBoxHex, checkBoxUnit;
	public StartUp startup;
	/** for any imagemanipulation */
	private ImageFactory imageFac;
	private Map map;
	private int mod;
	private int street;
	private int river;
	private int typ;
	private int rotate;
	private int color;
	private int unit;
	private Image streetImg;
	private Image riverImg;
	private String version;
} /* class */