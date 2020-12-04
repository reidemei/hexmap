package jr.hexmap;

import jr.util.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.io.*;
import Acme.JPM.Encoders.*;

/**
 *  Hexfield Map Editor
 *
 *  @version $Id: HexMap.java,v 0.9.3 2001/01/22 09:02:41 reidemei Exp $
 *  @author Jan Reidemeister
 */ 
public class HexMap extends JFrame implements WindowListener, 
											  ActionListener, 
											  MouseListener, 
											  MouseMotionListener, 
											  TreeSelectionListener {
	/** the Version */
	public final static String VERSION = "0.9.4";
	/** date of building the app */
	public final static String BUILD = "2001-01-22";
	/** die ScrollPane für den Map */
	private JScrollPane scroll;

	/**
	 *  Constructor
	 */
	public HexMap () {
		super ("Hexfield Map Editor " + VERSION);
		Debug.print ("HexMap - constructor");		
		startup = new StartUp ();
		imageFac = new ImageFactory ();
		System.out.println ("");
		System.out.println ("    Hexfield Map Editor " + VERSION + " build " + BUILD);
		System.out.println ("    (c) 1998-2001 Jan Reidemeister (J.R.@gmx.de)");
		System.out.println ("    http://JanR.home.pages.de\n");
		this.addWindowListener (this);
		// set the window-position (works only on Win, as far i hava seen)
		this.setSize (685,495);
		Dimension x = getToolkit ().getScreenSize ();
//		this.setLocation (((int) (x.getSize ().width / 2)) - 335, ((int) (x.getSize ().height / 2)) - 225);
//		this.setResizable (false);
		this.setIconImage (getToolkit ().getImage (this.getClass ().getClassLoader ().getSystemResource("jr/hexmap/icon.gif")));
		startup.setText ("Loading units.");
		loadUnits ();				
		startup.setText ("Loading images.");
		loadImages ();		
		startup.setText ("Generating map.");
		this.initUI ();		
		this.setVisible (true);
		this.setDefaultCloseOperation (JFrame.DO_NOTHING_ON_CLOSE);
		startup.toFront ();
		startup.dispose ();
		startup = null;
		// clean up
		System.gc ();
	} /* Constructor */

	/**
	 *  Loads all units.
	 */
	private void loadUnits () {
		System.out.print ("Loading units ");
		Images.units = new java.util.Vector();
		BufferedReader in = null;
		String inputLine;				
		try {
			in = new BufferedReader (new FileReader ("units/units.ini"));
		} catch (FileNotFoundException e) { 
			Debug.print ("HexMap - loadUnits - ERROR: unit-file not found");
			if (startup != null)
				startup.setError ("No units found.");
			return;
		} /* catch */			
		// parse the text
		try {
			Unit unit = null;
			while ((inputLine = in.readLine ()) != null) {
				if ((!inputLine.startsWith ("//")) && (!(inputLine.trim ()).equals (""))) {
					unit = new Unit ();
					inputLine.trim();
					unit.abk = inputLine.substring (0, inputLine.indexOf (';'));
					inputLine = inputLine.substring (inputLine.indexOf (';') + 1);
					unit.iconName = inputLine.substring (0, inputLine.indexOf (';'));
					unit.name = inputLine.substring (inputLine.indexOf (';') + 1);
					Images.units.addElement (unit);
					System.out.print (".");	
				} /* if */
			} /* while */
			in.close();
		} catch (IOException e) { }	
		System.out.println (" Done.");
	} /* loadUnits */	
	
	/**
	 *  Loads all needed images.
	 */
	private void loadImages () {
		System.out.print ("Loading unit-images ");	
		for (int i = 0; i < Images.units.size (); i++) {
			((Unit) Images.units.elementAt (i)).icon  = imageFac.loadImage ("units/" + ((Unit) Images.units.elementAt (i)).iconName);
			System.out.print (".");	
			if	(((Unit) Images.units.elementAt (i)).icon == null) {
				System.err.println ("\n\nERROR: Can't load file \"units/" + ((Unit) Images.units.elementAt (i)).iconName + "\"! Removing unit ...");
				Images.units.removeElementAt (i--);
			} /* if */
		} /* for */
		System.out.print (" Done.\nLoading images ");	
		Image x = imageFac.loadImage ("images/img.gif");
		if (x == null) {
			System.err.println ("\n\nERROR: Can't load file \"images/img.gif\"! Aborting ...");
			System.exit (-1);
		} /* if */
		System.out.print (".");	
		Image y = imageFac.loadImage ("images/street.gif");
		if (y == null) {
			System.err.println ("\n\nERROR: Can't load file \"images/street.gif\"! Aborting ...");
			System.exit (-1);
		} /* if */
		System.out.print (".");	
		Image z = imageFac.loadImage ("images/river.gif");
		if (z == null) {
			System.err.println ("\n\nERROR: Can't load file \"images/river.gif\"! Aborting ...");
			System.exit (-1);
		} /* if */
		System.out.print (".");	
		Image w = imageFac.loadImage ("images/build.gif");
		if (w == null) {
			System.err.println ("\n\nERROR: Can't load file \"images/build.gif\"! Aborting ...");
			System.exit (-1);
		} /* if */
		System.out.print (".");	
		// crop the icons from the images
		Images.levelm3 = imageFac.cropImage (x, 0, 0, 67, 59);
		System.out.print (".");
		Images.street1 = imageFac.cropImage (y, 0, 0, 67, 59);
		System.out.print (".");		
		Images.levelm2 = imageFac.cropImage (x, 68, 0, 67, 59);
		System.out.print (".");	
		Images.street2 = imageFac.cropImage (y, 68, 0, 67, 59);
		System.out.print (".");	
		Images.levelm1 = imageFac.cropImage (x, 136, 0, 67, 59);
		System.out.print (".");	
		Images.street3 = imageFac.cropImage (y, 136, 0, 67, 59);
		System.out.print (".");	
		Images.level0 = imageFac.cropImage (x, 204, 0, 67, 59);
		System.out.print (".");	
		Images.street4 = imageFac.cropImage (y, 204, 0, 67, 59);
		System.out.print (".");	
		Images.level1 = imageFac.cropImage (x, 272, 0, 67, 59);
		System.out.print (".");	
		Images.street5 = imageFac.cropImage (y, 272, 0, 67, 59);
		System.out.print (".");	
		Images.level2 = imageFac.cropImage (x, 340, 0, 67, 59);
		System.out.print (".");	
		Images.street6 = imageFac.cropImage (y, 340, 0, 67, 59);
		System.out.print (".");	
		Images.level3 = imageFac.cropImage (x, 0, 59, 67, 59);
		System.out.print (".");	
		Images.river1 = imageFac.cropImage (z, 0, 0, 67, 59);
		System.out.print (".");	
		Images.level4 = imageFac.cropImage (x, 68, 59, 67, 59);
		System.out.print (".");	
		Images.river2 = imageFac.cropImage (z, 68, 0, 67, 59);
		System.out.print (".");	
		Images.highlight = imageFac.cropImage (x, 136, 59, 67, 59);
		System.out.print (".");	
		Images.lightBuilding = imageFac.cropImage (w, 0, 0, 67, 59);
		System.out.print (".");	
		Images.sparseWood = imageFac.cropImage (x, 204, 59, 67, 59);
		System.out.print (".");	
		Images.river3 = imageFac.cropImage (z, 136, 0, 67, 59);
		System.out.print (".");	
		Images.denseWood = imageFac.cropImage (x, 272, 59, 67, 59);
		System.out.print (".");
		Images.heavyBuilding = imageFac.cropImage (w, 136, 0, 67, 59);
		System.out.print (".");	
		Images.rough = imageFac.cropImage (x, 340, 59, 67, 59);
		System.out.print (".");	
		Images.river4 = imageFac.cropImage (z, 204, 0, 67, 59);
		System.out.print (".");	
		Images.asphalt = imageFac.cropImage (x, 0, 118, 67, 59);
		System.out.print (".");	
		Images.veryHeavyBuilding = imageFac.cropImage (w, 204, 0, 67, 59);
		System.out.print (".");	
		Images.empty = imageFac.cropImage (x, 68, 118, 67, 59);
		System.out.print (".");	
		Images.river5 = imageFac.cropImage (z, 272, 0, 67, 59);
		System.out.print (".");	
		Images.fire = imageFac.cropImage (x, 136, 118, 67, 59);
		System.out.print (".");	
		Images.mediumBuilding = imageFac.cropImage (w, 68, 0, 67, 59);
		System.out.print (".");	
		Images.smog = imageFac.cropImage (x, 204, 118, 67, 59);
		System.out.print (".");	
		Images.river6 = imageFac.cropImage (z, 340, 0, 67, 59);
		System.out.print (".");	 
		Images.flag = imageFac.cropImage (x, 272, 118, 67, 59);
		System.out.print (".");	
		Images.swamp = imageFac.cropImage (x, 340, 118, 67, 59);
		System.out.print (".");	
		System.out.println (" Done.");
	} /* loadImages */
		
	/**
	 *  Creates the GUI.
	 */
	private void initUI () {
		Debug.print ("HexMap - initUI - creating map");
		// the hexmap
		map = new Map (imageFac);
		map.addMouseListener (this);
		map.addMouseMotionListener (this);
		street = 0;
		street = street ^ (0x000001);
		streetImg = Images.street1;
		river = street;
		riverImg = Images.river1;
		startup.setText ("Initialising Layout.");
		Debug.print ("HexMap - initUI - creating menu");
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
		menuItem.setAccelerator (KeyStroke.getKeyStroke (KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		menuItem.addActionListener (this);
		menu.add (menuItem);
		menu.addSeparator ();
		menuItem = new JMenuItem ("Load");
		menuItem.setActionCommand ("load");
		menuItem.setAccelerator (KeyStroke.getKeyStroke (KeyEvent.VK_L, ActionEvent.CTRL_MASK));
		menuItem.addActionListener (this);
		menu.add (menuItem);
		menuItem = new JMenuItem("Save as");
		menuItem.setActionCommand ("save");
		menuItem.setAccelerator (KeyStroke.getKeyStroke (KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		menuItem.addActionListener (this);
		menu.add (menuItem);
		menu.addSeparator ();
		menuItem = new JMenuItem ("Export GIF");
		menuItem.setActionCommand ("export");
		menuItem.setAccelerator (KeyStroke.getKeyStroke (KeyEvent.VK_E, ActionEvent.CTRL_MASK));
		menuItem.addActionListener (this);
		menu.add (menuItem);
		menu.addSeparator ();
		menuItem = new JMenuItem ("Quit");
		menuItem.setActionCommand ("quit");
		menuItem.setAccelerator (KeyStroke.getKeyStroke (KeyEvent.VK_X, ActionEvent.CTRL_MASK));
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
		// 			checkBoxLevel = new JCheckBoxMenuItem ("Show Levels");
		//				checkBoxLevel.setSelected (false);
		//				checkBoxLevel.setActionCommand ("levels");
		//				checkBoxLevel.addActionListener (this);
		//				menu.add (checkBoxLevel);
		menuItem = new JMenuItem ("Info");
		menuItem.setActionCommand ("info");
		menuItem.addActionListener (this);
		menuBar.add (menuItem);
		menuBar.add (new JLabel ("       "));
		menuBar.add (Box.createHorizontalGlue ());
		menuBar.add (Box.createHorizontalGlue ());
		menuBar.add (Box.createHorizontalGlue ());
		Debug.print ("HexMap - initUI - creating tree");
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
		node = new DefaultMutableTreeNode("Deeper");
		level.add(node);
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
		node = new DefaultMutableTreeNode("Higher");
		level.add(node);
		node = new DefaultMutableTreeNode("Delete");
		top.add(node);
		DefaultMutableTreeNode wood = new DefaultMutableTreeNode("Wood");
		top.add(wood);
		node = new DefaultMutableTreeNode("Sparse");
		wood.add(node);
		node = new DefaultMutableTreeNode("Dense");
		wood.add(node);
		DefaultMutableTreeNode ground = new DefaultMutableTreeNode("Ground");
		top.add (ground);
		node = new DefaultMutableTreeNode("Asphalt");
		ground.add (node);
		node = new DefaultMutableTreeNode("Swamp");
		ground.add (node);
		DefaultMutableTreeNode street = new DefaultMutableTreeNode("Street");
		top.add(street);
		DefaultMutableTreeNode river = new DefaultMutableTreeNode("River");
		top.add(river);
		DefaultMutableTreeNode buildings = new DefaultMutableTreeNode("Buildings");
		top.add(buildings);
		node = new DefaultMutableTreeNode("Light");
		buildings.add(node);
		node = new DefaultMutableTreeNode("Medium");
		buildings.add(node);
		node = new DefaultMutableTreeNode("Heavy");
		buildings.add(node);
		node = new DefaultMutableTreeNode("Very Heavy");
		buildings.add(node);
		node = new DefaultMutableTreeNode("Ruins");
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
		Debug.print ("HexMap - initUI - adding units");		
		// add all units
		if (Images.units.size () > 0) {
			DefaultMutableTreeNode units = new DefaultMutableTreeNode("Units");
			top.add(units);
			for (int i = 0; i < Images.units.size (); i++) {
				node = new DefaultMutableTreeNode(((Unit) Images.units.elementAt (i)).abk);
				units.add (node);
			} /* for */
		} /* if */
		Debug.print ("HexMap - initUI - creating layout");			
		// Center - map and scrollbars
		JPanel pCenter = new JPanel ();
		pCenter.setBorder (new EtchedBorder ());
		pCenter.setLayout (new BorderLayout ());
//		map.setBorder (new EtchedBorder ());
		scroll = new JScrollPane (map, 
						JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
						JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		pCenter.add (scroll, BorderLayout.CENTER);
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
		img.setIcon (new ImageIcon (Images.level0.getScaledInstance (48, 41, Image.SCALE_SMOOTH)));
		status.setText ("Level 0: (click on map to set the level for a hexfield)");
		if (startup != null)
			startup.setText ("Drawing map.");
	} /* initUI */
	
	/**
	 *  creates an HexMap-Editor and starts ist with the right LaF
	 *
	 *  @param args 	the param from the commandline
	 *
	 */
	public static void main (String args[]) {
		String laf = "";
		if (args.length > 0) {
			if (args[0].startsWith ("-")) {
				if (args[0].equals ("-debug")) {
					Debug.debug = true;
				} /* if */
				if (args.length > 1)
					laf = args[1];
			} else 
				laf = args[0];
			Debug.print ("HexMap - main - selected LaF: " + laf);		
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
		try {
			HexMap me = new HexMap ();
		} catch (Exception e) {
			System.out.println ("ERROR: Exception catched in main");
			System.out.println (e);
		} /* catch */
	} /* main */

	/** does nothing */		
	public void windowOpened (WindowEvent e) {}
	/** does nothing */			
	public void windowClosed (WindowEvent e) {}
	/** does nothing */			
	public void windowIconified (WindowEvent e) {}
	/** does nothing */			
	public void windowDeiconified (WindowEvent e) {}
	/** does nothing */			
	public void windowActivated (WindowEvent e) {}
	/** does nothing */			
	public void windowDeactivated (WindowEvent e) {}
		
	public void windowClosing (WindowEvent e) {
		Debug.print ("HexMap - windowClosing");
		int n = JOptionPane.showConfirmDialog (
					this, 
					"Really quit?",
					"Hexfield Map Editor " + VERSION,
					JOptionPane.YES_NO_OPTION);
		if (n == JOptionPane.YES_OPTION) {
			System.exit (0);
		} /* if */ 
		return;
	} /* windowClosing */
		
	public void valueChanged (TreeSelectionEvent e) {
		String who = null;
		try {
			who = e.getNewLeadSelectionPath ().getLastPathComponent ().toString ();
		} catch (Exception except) {
			return;
		} /* catch */
		if (who == null)
			return;
		Debug.print ("HexMap - valueChanged - selected: " + who);
		if (who.equals ("Tools") || who.equals ("Level") || 
			who.equals ("Wood") || who.equals ("Buildings") || who.equals ("Ground") || 
			who.equals ("Special") || who.equals ("Units")) {
			return;
		} /* if */
		Image helpImg = createImage (68, 59);
		if (helpImg == null)
			return;
		Graphics helpGraphics = helpImg.getGraphics ();
		text.setVisible (false);
		if (e.getNewLeadSelectionPath ().getParentPath ().getLastPathComponent ().toString ().equals ("Units")) {
			for (unit = 0; unit < Images.units.size(); unit++) {
				if (((Unit) Images.units.elementAt(unit)).abk.equals (who))
					break;	    
			} /* for */
			type = TYPE_UNIT;
			label.setText (((Unit) Images.units.elementAt (unit)).abk);
			label.setToolTipText (((Unit) Images.units.elementAt (unit)).name);
			img.setToolTipText (((Unit) Images.units.elementAt (unit)).name);
			img.setIcon (new ImageIcon (imageFac.cropImage (imageFac.rotateAndColorImage (((Unit) Images.units.elementAt (unit)).icon, rotate * 60, color),10, 9, 48, 41)));
			status.setText ("Unit: " + ((Unit) Images.units.elementAt (unit)).name + " (leftclick on uniticon for rotate, rightclick for colorchange; click on map to place)");
			return;
		} /* if */
		label.setText (who);
		if (e.getNewLeadSelectionPath ().getParentPath ().getLastPathComponent ().toString ().equals ("Level")) {
			status.setText (who + ": (click on map to set the level for a hexfield)");
			type = TYPE_LEVEL;
			if (who.equals ("Deeper")) {
				mod = -4;
				text.setVisible (true);
				text.setText ("-4");
				helpGraphics.drawImage (Images.levelm3, 0, 0, this);				
				status.setText (who + ": (enter the level and click on map to set the level for a hexfield)");
			} /* if */
			if (who.equals ("Level -3")) {
				mod = -3;
				helpGraphics.drawImage (Images.levelm3, 0, 0, this);				
			} /* if */
			if (who.equals ("Level -2")) {
				mod = -2;
				helpGraphics.drawImage (Images.levelm2, 0, 0, this);				
			} /* if */
			if (who.equals ("Level -1")) {
				mod = -1;
				helpGraphics.drawImage (Images.levelm1, 0, 0, this);				
			} /* if */
			if (who.equals ("Level 0")) {
				mod = 0;
				helpGraphics.drawImage (Images.level0, 0, 0, this);				
			} /* if */
			if (who.equals ("Level 1")) {
				mod = 1;
				helpGraphics.drawImage (Images.level1, 0, 0, this);				
			} /* if */
			if (who.equals ("Level 2")) {
				mod = 2;
				helpGraphics.drawImage (Images.level2, 0, 0, this);				
			} /* if */
			if (who.equals ("Level 3")) {
				mod = 3;
				helpGraphics.drawImage (Images.level3, 0, 0, this);				
			} /* if */
			if (who.equals ("Level 4")) {
				mod = 4;
				helpGraphics.drawImage (Images.level4, 0, 0, this);				
			} /* if */
			if (who.equals ("Higher")) {
				mod = 5;
				text.setVisible (true);
				text.setText ("5");
				helpGraphics.drawImage (Images.level4, 0, 0, this);				
				status.setText (who + ": (enter the level and click on map to set the level for a hexfield)");
			} /* if */
		} /* if */
		if (e.getNewLeadSelectionPath ().getParentPath ().getLastPathComponent ().toString ().equals ("Tools")) {
			if (who.equals ("Delete")) {
				type = TYPE_DELETE;
				helpGraphics.drawImage (Images.empty, 0, 0, this);				
				status.setText ("Delete: (click on map to erase hexfield)");
			} /* if */
			if (who.equals ("Street")) {
				type = TYPE_STREET;
				helpGraphics.drawImage (streetImg, 0, 0, this);				
				status.setText ("Street: (click on the 6 lines of the icon to add/remove streets; click on map to place)");
			} /* if */
			if (who.equals ("River")) {
				type = TYPE_RIVER;
				helpGraphics.drawImage (riverImg, 0, 0, this);				
				status.setText ("River: (click on the 6 lines of the icon to add/remove river; click on map to place)");
			} /* if */
		} /* if */
		if (e.getNewLeadSelectionPath ().getParentPath ().getLastPathComponent ().toString ().equals ("Wood")) {
			type = TYPE_WOOD;
			if (who.equals ("Sparse")) {
				mod = Hex.WOOD_SPARSE;
				helpGraphics.drawImage (Images.sparseWood, 0, 0, this);
				status.setText ("Sparse Wood: (click on map to place sparse wood on a hexfield)");
			} /* if */
			if (who.equals ("Dense")) {
				mod = Hex.WOOD_DENSE;
				helpGraphics.drawImage (Images.denseWood, 0, 0, this);
				status.setText ("Dense Wood: (click on map to place dense wood on a hexfield)");
			} /* if */
		} /* if */
		if (e.getNewLeadSelectionPath ().getParentPath ().getLastPathComponent ().toString ().equals ("Ground")) {
			type = TYPE_GROUND;
			if (who.equals ("Swamp")) {
				mod = Hex.GROUND_SWAMP;
				helpGraphics.drawImage (Images.swamp, 0, 0, this);
				status.setText ("Swamp: (click on map to place swamp on a hexfield)");
			} /* if */
			if (who.equals ("Asphalt")) {
				mod = Hex.GROUND_ASPHALT;
				helpGraphics.drawImage (Images.asphalt, 0, 0, this);
				status.setText ("Asphalt: (click on map to place asphalt on a hexfield)");
			} /* if */
		} /* if */
		if (e.getNewLeadSelectionPath ().getParentPath ().getLastPathComponent ().toString ().equals ("Buildings")) {
			type = TYPE_BUILDING;
			if (who.equals ("Light")) {
				mod = Hex.BUILDING_LIGHT;
				helpGraphics.drawImage (Images.lightBuilding, 0, 0, this);
				status.setText ("Light building: (click on map to place on a hexfield)");
			} /* if */
			if (who.equals ("Medium")) {
				mod = Hex.BUILDING_MEDIUM;
				helpGraphics.drawImage (Images.mediumBuilding, 0, 0, this);
				status.setText ("Medium building: (click on map to place on a hexfield)");
			} /* if */
			if (who.equals ("Heavy")) {
				mod = Hex.BUILDING_HEAVY;
				helpGraphics.drawImage (Images.heavyBuilding, 0, 0, this);
				status.setText ("Heavy building: (click on map to place on a hexfield)");
			} /* if */
			if (who.equals ("Very Heavy")) {
				mod = Hex.BUILDING_VERYHEAVY;
				helpGraphics.drawImage (Images.veryHeavyBuilding, 0, 0, this);
				status.setText ("Very heavy building: (click on map to place on a hexfield)");
			} /* if */
			if (who.equals ("Ruins")) {
				mod = Hex.BUILDING_RUINS;
				helpGraphics.drawImage (Images.ruins, 0, 0, this);
				status.setText ("Ruins: (click on map to place on a hexfield)");
			} /* if */
		} /* if */
		if (e.getNewLeadSelectionPath ().getParentPath ().getLastPathComponent ().toString ().equals ("Special")) {
			type = TYPE_SPECIAL;
			if (who.equals ("Rough")) {
				type = TYPE_ROUGH;
				helpGraphics.drawImage (Images.rough, 0, 0, this);
				status.setText ("Rough: (click on map to mark a hexfield rough)");
			} /* if */
			if (who.equals ("Fire")) {
				mod = Hex.SPECIAL_FIRE;
				helpGraphics.drawImage (Images.fire, 0, 0, this);
				status.setText ("Fire: (click on map to place fire on a hexfield)");
			} /* if */
			if (who.equals ("Smoke")) {
				mod = Hex.SPECIAL_SMOG;
				helpGraphics.drawImage (Images.smog, 0, 0, this);
				status.setText ("Smoke: (click on map to place smoke on a hexfield)");
			} /* if */
			if (who.equals ("Flag")) {
				type = TYPE_FLAG;
				helpGraphics.drawImage (imageFac.colorImage (Images.flag, color), 0, 0, this);
				status.setText ("Flag: (rightclick on the icon to change the color; click on map to place the flag on a hexfield)");
			} /* if */
			if (who.equals ("Text")) {
				type = TYPE_TEXT;
				helpGraphics.drawImage (Images.empty, 0, 0, this);
				text.setVisible (true);
				status.setText ("Text: (enter some text and click on the map to place on a hexfield)");
			} /* if */
		} /* if */
		img.setIcon (new ImageIcon (helpImg.getScaledInstance (48, 41, Image.SCALE_SMOOTH)));
	} /* valueChanged */

	public void actionPerformed (ActionEvent e) {
		Debug.print ("HexMap - actionPerformed - actionCommand: " + e.getActionCommand ());
		if (e.getActionCommand ().equals ("hexnumbers")) {
			map.setShowHexNumbers (checkBoxHex.isSelected ());
			return;
		} /* if */
		if (e.getActionCommand ().equals ("unitnames")) {
			map.setShowUnitNames (checkBoxUnit.isSelected ());
			return;
		} /* if */
		if (e.getActionCommand ().equals ("save")) {
			this.setTitle ("Hexfield Map Editor " + VERSION + " - Saving Map ...");
			JFileChooser chooser = new JFileChooser (); 
			jr.util.ExampleFileFilter filter = new jr.util.ExampleFileFilter ("map","Hexfield map");
			chooser.addChoosableFileFilter (filter);
			chooser.setFileFilter (filter);
			chooser.removeChoosableFileFilter (chooser.getAcceptAllFileFilter ());
			int returnVal = chooser.showSaveDialog (this.getContentPane ());
			if(returnVal == JFileChooser.APPROVE_OPTION) {
				try {
					Debug.print ("HexMap - actionPerformed - saving map: " + chooser.getSelectedFile ());
					String file = chooser.getSelectedFile ().getName ();
					FileWriter out;
					if (file.endsWith(".map"))
						out = new FileWriter (chooser.getSelectedFile ());
					else
						out = new FileWriter (new File (chooser.getSelectedFile ().getParent (), file + ".map"));
					if (map.saveMap (out)) {
						JOptionPane.showMessageDialog (
							this, 
							"The map was saved!",
							"Info", 
							JOptionPane.INFORMATION_MESSAGE);
						System.out.println("Done. Map " + chooser.getSelectedFile().getName() + " saved."); 
					} else  {
						JOptionPane.showMessageDialog (
							this, 
							"There wa a problem saving that map!",
							"Error", 
							JOptionPane.ERROR_MESSAGE);
						System.out.println("Done. Map " + chooser.getSelectedFile().getName() + " not saved."); 					
					} /* else */
				} catch (Exception err) {}
			} /* if */
			this.setTitle ("Hexfield Map Editor " + VERSION);
			return;
		} /* if */
		if (e.getActionCommand ().equals ("load")) {
			this.setTitle ("Hexfield Map Editor " + VERSION + " - Loading Map ...");
			JFileChooser chooser = new JFileChooser(); 
			jr.util.ExampleFileFilter filter;
			filter = new jr.util.ExampleFileFilter ("map","Hexfield map");
			chooser.addChoosableFileFilter(filter);
			chooser.setFileFilter(filter);
			chooser.removeChoosableFileFilter(chooser.getAcceptAllFileFilter());
			int returnVal = chooser.showOpenDialog(this.getContentPane ());
			if(returnVal == JFileChooser.APPROVE_OPTION) {
				try {
					Debug.print ("HexMap - actionPerformed - loading map: " + chooser.getSelectedFile ());
					FileReader in = new FileReader (chooser.getSelectedFile ());
					if (map.loadMap (in)) {
						JOptionPane.showMessageDialog (
							this, 
							"The map was loaded!\n\n" +
							"Sorry, but the ScrollBars don't reflect the new size\n" +
							"(Can anybody tell me why???), so you have to resize \n" +
							"the application to do so :-(.\n",
							"Info", 
							JOptionPane.INFORMATION_MESSAGE);
						System.out.println("Done. Map " + chooser.getSelectedFile().getName() + " loaded."); 
					} else {
						JOptionPane.showMessageDialog (
							this, 
							"The map was not loaded!\n" +
							"If that map was saved with an old version,\n" +
							"you have to convert it first.",
							"Error", 
							JOptionPane.ERROR_MESSAGE);
						System.out.println("Done. Map " + chooser.getSelectedFile().getName() + " not loaded."); 
					} /* else */
				} catch (Exception err) {}
			} /* if */
			this.setTitle ("Hexfield Map Editor " + VERSION);
			return;
		} /* if */
		if (e.getActionCommand ().equals ("new")) {
			// open an optiondialog to choose the new mapsize
			final JDialog d = new JDialog (this, "Create a new Map...", true);
			d.setResizable (false);
			d.getContentPane ().setLayout (new BorderLayout ());
			final JTextField rows = new JTextField ("16", 4);
			final JTextField cols = new JTextField ("16", 4);
			JPanel p = new JPanel (new GridLayout (2, 2));
			p.add (new JLabel ("Rows:", JLabel.CENTER));
			p.add (rows);
			p.add (new JLabel ("Columns:", JLabel.CENTER));
			p.add (cols);
			d.getContentPane ().add (p, BorderLayout.CENTER);
			p = new JPanel (new FlowLayout (FlowLayout.RIGHT));
			final HexMap hm = this;
			JButton b = new JButton ("OK");
			b.addActionListener (new ActionListener () {
				public void actionPerformed (ActionEvent aEvt) {
					try {
						int row = Integer.parseInt (rows.getText ());
						int col = Integer.parseInt (cols.getText ());
						if ((row > 0) && (col > 0)) {
							int r = map.X_Size;
							int c = map.Y_Size;
							try {
								map.makeMap (row, col);
							} catch (OutOfMemoryError oome) {
								System.gc ();
								map.makeMap (r, c);
								System.gc ();
								JOptionPane.showMessageDialog (
									d, 
									"That map is to big for your Computer!\n" +
									"You have to increase the memory-limit of \n" +
									"the JRE to create that sized maps (Have a \n" + 
									"look in the 'readme.txt').", 
									"Error", 
									JOptionPane.ERROR_MESSAGE);
								return;
							} /* catch */
							map.repaint ();
							d.hide ();
							d.dispose ();
							hm.getContentPane ().invalidate ();
							JOptionPane.showMessageDialog (
								d, 
								"A new map was created.\n\n" +
								"Sorry, but the ScrollBars don't reflect the new size\n" +
								"(Can anybody tell me why???), so you have to resize \n" +
								"the application to do so :-(.\n",
								"Info", 
								JOptionPane.INFORMATION_MESSAGE);
						} /* if */
					} catch (Exception exc) {
						JOptionPane.showMessageDialog (
							d, 
							"Please enter valid numbers!", 
							"Error", 
							JOptionPane.ERROR_MESSAGE);
					} /* catch */
				} /* actionPerformed */
			}); /* ActionListener */
			p.add (b);
			b = new JButton ("Cancel");
			b.addActionListener (new ActionListener () {
				public void actionPerformed (ActionEvent aEvt) {
					d.hide ();
					d.dispose ();
				} /* actionPerformed */
			}); /* ActionListener */
			p.add (b);
			d.getContentPane ().add (p, BorderLayout.SOUTH);
			d.pack ();
			d.setLocationRelativeTo (this);
			d.show ();
			return;
		} /* if */
		if (e.getActionCommand ().equals ("info")) {
			Object[] options = {"Dismiss"};
			JOptionPane.showOptionDialog (
					this, 
					"A tool to create hexfield maps. \nSupports userdefined graphics and units.\n\n" + 
					"Version: " + VERSION + " (" + BUILD + ") \n" +
					"(c) 1998-2001 by Jan Reidemeister <J.R.@gmx.de>\n" +
					"http://JanR.home.pages.de\n\n" +
					"Refer to the readme.txt for more details.\n" + 
					"THIS SOFTWARE IS PROVIDED ON AN \"AS IS\"\nBASIS WITHOUT WARRANTY OF ANY KIND." ,
					"Hexfield Map Editor", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, "Dismiss");
			return;
		} /* if */
		if (e.getActionCommand ().equals ("export")) {
			this.setTitle ("Hexfield Map Editor " + VERSION + " - Export Image ...");
			JFileChooser chooser = new JFileChooser (); 
			jr.util.ExampleFileFilter filtergif = new jr.util.ExampleFileFilter ("gif", "GIF Images");
			chooser.addChoosableFileFilter (filtergif);
			jr.util.ExampleFileFilter filterpng = new jr.util.ExampleFileFilter ("png", "PNG Images");
			chooser.addChoosableFileFilter (filterpng);
			chooser.setFileFilter (filtergif);
			chooser.removeChoosableFileFilter (chooser.getAcceptAllFileFilter ());
			int returnVal = chooser.showSaveDialog (this.getContentPane ());
			if(returnVal == JFileChooser.APPROVE_OPTION) {
				Debug.print ("HexMap - actionPerformed - exporting map: " + chooser.getSelectedFile ());
				String file = chooser.getSelectedFile ().getName ();
				try {
					java.io.FileOutputStream out = null;
					if (chooser.getFileFilter() == filtergif) {
						if (file.endsWith(".gif"))
							out = new java.io.FileOutputStream (chooser.getSelectedFile ());
						else
							out = new java.io.FileOutputStream (new java.io.File (chooser.getSelectedFile ().getParent (), file + ".gif"));
						Acme.JPM.Encoders.ImageEncoder coder = new Acme.JPM.Encoders.GifEncoder (map.getImage (), out);
						coder.encode ();
					} /* if */
					if (chooser.getFileFilter() == filterpng) {
						if (file.endsWith(".png"))
							out = new java.io.FileOutputStream (chooser.getSelectedFile ());
						else
							out = new java.io.FileOutputStream (new java.io.File (chooser.getSelectedFile ().getParent (), file + ".png"));
						byte[] pngbytes;
						com.keypoint.PngEncoderB coder = new com.keypoint.PngEncoderB (
								(BufferedImage) map.getImage (),
								com.keypoint.PngEncoder.NO_ALPHA,
								0, 1);
						pngbytes = coder.pngEncode();
						out.write (pngbytes);
					} /* if */
					out.flush ();
					out.close ();
				} catch (Exception err) {}
				System.out.println("Done. File " + chooser.getSelectedFile().getName() + " saved."); 
			} /* if */
			this.setTitle ("Hexfield Map Editor " + VERSION);
			return;
		} /* if */
		if (e.getActionCommand ().equals ("quit")) {
			int n = JOptionPane.showConfirmDialog (new JFrame(), "Really quit?", "Hexfield Map Editor", JOptionPane.YES_NO_OPTION);
			if (n == JOptionPane.YES_OPTION) 	System.exit (0);
			return;
		} /* if */
	} /* actionPerformed */
	
	public void mouseClicked (MouseEvent e) {
		Debug.print ("HexMap - mouseClicked - x: " + e.getX () + ", y: " + e.getY ());
		if (e.getSource () == img) {
			if (type == TYPE_STREET) {
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
				dbGraphics.drawImage (Images.empty, 0, 0, this);				
				if ((street & 0x000001) == 1)
					dbGraphics.drawImage (Images.street1, 0, 0, this);
				if ((street & 0x000010) == 0x10)
					dbGraphics.drawImage (Images.street2, 0, 0, this);
				if ((street & 0x000100) == 0x100)
					dbGraphics.drawImage (Images.street3, 0, 0, this);
				if ((street & 0x001000) == 0x1000)
					dbGraphics.drawImage (Images.street4, 0, 0, this);
				if ((street & 0x010000) == 0x10000)
					dbGraphics.drawImage (Images.street5, 0, 0, this);
				if ((street & 0x100000) == 0x100000)
					dbGraphics.drawImage (Images.street6, 0, 0, this);
				img.setIcon (new ImageIcon (streetImg.getScaledInstance (48, 41, Image.SCALE_SMOOTH)));	
				return;
			} /* if */
			if (type == TYPE_RIVER) {
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
				dbGraphics.drawImage (Images.empty, 0, 0, this);				
				if ((river & 0x000001) == 1)
					dbGraphics.drawImage (Images.river1, 0, 0, this);
				if ((river & 0x000010) == 0x10)
					dbGraphics.drawImage (Images.river2, 0, 0, this);
				if ((river & 0x000100) == 0x100)
					dbGraphics.drawImage (Images.river3, 0, 0, this);
				if ((river & 0x001000) == 0x1000)
					dbGraphics.drawImage (Images.river4, 0, 0, this);
				if ((river & 0x010000) == 0x10000)
					dbGraphics.drawImage (Images.river5, 0, 0, this);
				if ((river & 0x100000) == 0x100000)
					dbGraphics.drawImage (Images.river6, 0, 0, this);
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
			if (type == TYPE_FLAG)
				img.setIcon (new ImageIcon (imageFac.colorImage (Images.flag, color).getScaledInstance (48, 41, Image.SCALE_SMOOTH)));
			if (type == TYPE_UNIT)
				img.setIcon (new ImageIcon (imageFac.cropImage (imageFac.rotateAndColorImage (((Unit) Images.units.elementAt (unit)).icon, rotate * 60, color),10, 9, 48, 41)));
			return;
		} /* if */
		switch (type) {
			case TYPE_LEVEL: if (mod == -4 || mod == 5) {
								try {
									int l = Integer.parseInt (text.getText ());
									map.setLevel (l);
								} catch (NumberFormatException err) {
									System.out.println ("Please enter a number!");
								} /* catch */
							} else 
								map.setLevel (mod);
							break;
			case TYPE_GROUND: map.setGround (mod); break;
			case TYPE_BUILDING: map.setBuilding (mod); break;
			case TYPE_WOOD: map.setWood (mod); break;
			case TYPE_STREET: map.setStreet (street); break;
			case TYPE_RIVER: map.setRiver (river); break;
			case TYPE_UNIT: map.setUnit (((Unit) Images.units.elementAt(unit)).abk, rotate, color); break;
			case TYPE_SPECIAL: map.setSpecial (mod); break;
			case TYPE_ROUGH: map.setRough (true); break;
			case TYPE_FLAG: map.setFlag (color); break;
			case TYPE_TEXT: map.setText (text.getText ()); break;
			case TYPE_DELETE: map.emptyHex (); break;
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
	
	private JLabel img;
	private JLabel label;
	private JLabel status;
	private JTree tree;
	private JTextField text;
	private JCheckBoxMenuItem checkBoxHex, checkBoxUnit, checkBoxLevel;
	public StartUp startup;
	/** for any imagemanipulation */
	private ImageFactory imageFac;
	private Map map;	
	private int mod;
	private int street;
	private int river;
	private int type;
	public final static int TYPE_DELETE = -1;
	public final static int TYPE_LEVEL = 0;
	public final static int TYPE_WOOD = 1;
	public final static int TYPE_GROUND = 2;
	public final static int TYPE_BUILDING = 3;
	public final static int TYPE_SPECIAL = 4;
	public final static int TYPE_STREET = 5;
	public final static int TYPE_RIVER = 6;
	public final static int TYPE_UNIT = 7;
	public final static int TYPE_ROUGH = 8;
	public final static int TYPE_FLAG = 9;
	public final static int TYPE_TEXT = 10;
	private int rotate;
	private int color;
	private int unit;
	private Image streetImg;
	private Image riverImg;
} /* class */
