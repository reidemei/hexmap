package jr.hexmap;

import jr.util.*;

import java.awt.*;
import java.awt.event.*; 
import java.awt.image.*;
import java.io.*;
import java.util.Vector;

/**
 *  A JComponent, capable to display a hexfieldmap.
 *
 *  @version 1.0
 *  @author Jan Reidemeister
 */ 
public class Map
		extends javax.swing.JComponent
		implements Debug {
			
	/**
	 *  Constructor
	 *
	 *  @param s the startup-splash-screen
	 *  @param X the size of the map
	 *  @param Y the size of the map
	 *  @param fac an ImageFactory for imagehandling
	 */
	public Map (StartUp s, int X, int Y, ImageFactory fac) {
		
		if (DEBUG) System.err.println ("DEBUG: Map: Constuctor");
		hexnumbers = true;
		
		setCursor (new Cursor (Cursor.HAND_CURSOR));
		
		startup = s;
		if (fac == null)
			imageFac = new ImageFactory ();
		else
			imageFac = fac;
		
		enableEvents(AWTEvent.MOUSE_MOTION_EVENT_MASK);
		
		if (startup != null)
			startup.setText ("Generating map.");
		makeMap (X, Y);
		if (startup != null)
			startup.setText ("Loading units.");
		loadUnits ();
		if (startup != null)
			startup.setText ("Loading images.");
		
		loadImages ();
		
		XView = YView = oldX = oldY = 1;
		
	} /* constructor */
	
	/**
	 *  Constructor
	 */
	public Map () {
		
		this (null, 16, 16, null);
		
	} /* constructor */
	
	/**
	 *  Constructor
	 *
	 *  @param s the startup-splash-screen
	 */
	public Map (StartUp s) {
		
		this (s, 16, 16, null);
		
	} /* constructor */
	
	/**
	 *  Constructor
	 *
	 *  @param s the startup-splash-screen
	 *  @param fac an ImageFactory for imagehandling
	 */
	public Map (StartUp s, ImageFactory fac) {
		
		this (s, 16, 16, fac);
		
	} /* constructor */
	
	/**
	 *  Indicates the optimal size.
	 *
	 *  @return the optimal size
	 */
	public Dimension getPreferredSize () {
		
		return getMinimumSize ();
		
	} /* getPreferredSize */
	
	/**
	 *  Indicates the minimal size.
	 *
	 *  @return the minimal size
	 */
	public Dimension getMinimumSize () {
		
		return new Dimension (530,310);
		
	} /* getMinimumSize */
	
	/**
	 *  Generates a new empty map.
	 *
	 *  @param x X-size
	 *  @param y Y-size
	 */
	public void makeMap (int x, int y) {
		
		System.out.print ("Initialising map ");	
		X_Size = x; Y_Size = y;
		
		map = new Hex [x+1][y+1];
		for (int i = 0; i < x+1; i++) {
			for (int j = 0; j < y+1; j++) {
				map [i][j] = new Hex();
				map [i][j].flag = -1;
			} /* for */
			System.out.print (".");	
		} /* for */
		System.out.println (" Done.");	
		
		// clear the bufferimage
		dbImage = null;
		
	} /* makeMap */
	
	/**
	 *  Saves the complet map.
	 *
	 *  @param out FileOutputStream, to write on
	 */
	public boolean saveMap (java.io.FileOutputStream out){
		
		try {
			ObjectOutputStream s = new ObjectOutputStream(out);
			s.writeObject (new Integer (X_Size));
			s.writeObject (new Integer (Y_Size));
			s.writeObject (map);
			s.flush();
		} catch (java.io.IOException e) {
			return (false);
		} /* catch */
		return (true);
		
	} /* saveMap */
	
	/**
	 *  Loads the complete map.
	 *
	 *  @param in FileInputStream, to read from
	 */
	public boolean loadMap (java.io.FileInputStream in){
		
		try {
			ObjectInputStream s = new ObjectInputStream(in);
			X_Size = ((Integer)s.readObject ()).intValue ();
			Y_Size = ((Integer)s.readObject ()).intValue ();
			map = null;
			map = (Hex[][])s.readObject ();
			dbImage = null;
		} catch (Exception e) {
			return (false);
		} /* catch */
		return (true);
		
	} /* saveMap */
	
	/**
	 *  Loads all units.
	 */
	private void loadUnits () {
		
		System.out.print ("Loading units ");
		units = new Vector();
		BufferedReader in = null;
		String inputLine;
		
		try {
			in = new BufferedReader (new FileReader ("units/units.ini"));
		} catch (FileNotFoundException e) { 
			if (startup != null)
				startup.setError ("No units found.");
			return;
		} /* catch */
			
		// parse the text
		try {
			Unit unit = null;
			while ((inputLine = in.readLine()) != null) {
				if (!inputLine.startsWith("//")) {
					unit = new Unit ();
					inputLine.trim();
					unit.abk = inputLine.substring (0, inputLine.indexOf (';'));
					inputLine = inputLine.substring (inputLine.indexOf (';') + 1);
					unit.iconName = inputLine.substring (0, inputLine.indexOf (';'));
					unit.name = inputLine.substring (inputLine.indexOf (';') + 1);
					units.addElement (unit);
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
		
		source = new Image [33];
	  
		System.out.print ("Loading unit-images ");	
		
		for (int i = 0; i < units.size (); i++) {
			((Unit) units.elementAt (i)).icon  = imageFac.loadImage ("units/" + ((Unit) units.elementAt (i)).iconName);
			System.out.print (".");	
			if	(((Unit) units.elementAt (i)).icon == null) {
				System.err.println ("\n\nERROR: Can't load file \"units/" + ((Unit) units.elementAt (i)).iconName + "\"! Removing unit ...");
				units.removeElementAt (i);
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
			
		source[0] = imageFac.cropImage (x, 0, 0, 67, 59);
		System.out.print (".");	 // level -3
		source[16] = imageFac.cropImage (y, 0, 0, 67, 59);
		System.out.print (".");		
		source[1] = imageFac.cropImage (x, 68, 0, 67, 59);
		System.out.print (".");	 // level -2
		source[17] = imageFac.cropImage (y, 68, 0, 67, 59);
		System.out.print (".");	
		source[2] = imageFac.cropImage (x, 136, 0, 67, 59);
		System.out.print (".");	 // level -1
		source[18] = imageFac.cropImage (y, 136, 0, 67, 59);
		System.out.print (".");	
		source[3] = imageFac.cropImage (x, 204, 0, 67, 59);
		System.out.print (".");	 // level 0
		source[19] = imageFac.cropImage (y, 204, 0, 67, 59);
		System.out.print (".");	
		source[4] = imageFac.cropImage (x, 272, 0, 67, 59);
		System.out.print (".");	 // level 1
		source[20] = imageFac.cropImage (y, 272, 0, 67, 59);
		System.out.print (".");	
		source[5] = imageFac.cropImage (x, 340, 0, 67, 59);
		System.out.print (".");	 // level 2
		source[21] = imageFac.cropImage (y, 340, 0, 67, 59);
		System.out.print (".");	
		source[6] = imageFac.cropImage (x, 0, 59, 67, 59);
		System.out.print (".");	 // level 3
		source[22] = imageFac.cropImage (z, 0, 0, 67, 59);
		System.out.print (".");	
		source[7] = imageFac.cropImage (x, 68, 59, 67, 59);
		System.out.print (".");	 // level 4
		source[23] = imageFac.cropImage (z, 68, 0, 67, 59);
		System.out.print (".");	
		source[8] = imageFac.cropImage (x, 136, 59, 67, 59);
		System.out.print (".");	 // highlight
		source[29] = imageFac.cropImage (w, 0, 0, 67, 59);
		System.out.print (".");	
		source[9] = imageFac.cropImage (x, 204, 59, 67, 59);
		System.out.print (".");	 // sparse wood
		source[24] = imageFac.cropImage (z, 136, 0, 67, 59);
		System.out.print (".");	
		source[10] = imageFac.cropImage (x, 272, 59, 67, 59);
		System.out.print (".");	 // dense wood
		source[31] = imageFac.cropImage (w, 136, 0, 67, 59);
		System.out.print (".");	
		source[11] = imageFac.cropImage (x, 340, 59, 67, 59);
		System.out.print (".");	 // rough
		source[25] = imageFac.cropImage (z, 204, 0, 67, 59);
		System.out.print (".");	
		source[12] = imageFac.cropImage (x, 0, 118, 67, 59);
		System.out.print (".");	 // asphalt
		source[32] = imageFac.cropImage (w, 204, 0, 67, 59);
		System.out.print (".");	
		source[13] = imageFac.cropImage (x, 68, 118, 67, 59);
		System.out.print (".");	 // empty
		source[26] = imageFac.cropImage (z, 272, 0, 67, 59);
		System.out.print (".");	
		source[14] = imageFac.cropImage (x, 136, 118, 67, 59);
		System.out.print (".");	 // fire
		source[30] = imageFac.cropImage (w, 68, 0, 67, 59);
		System.out.print (".");	 // 
		source[15] = imageFac.cropImage (x, 204, 118, 67, 59);
		System.out.print (".");	 // smog
		source[27] = imageFac.cropImage (z, 340, 0, 67, 59);
		System.out.print (".");	 
		source[28] = imageFac.cropImage (x, 272, 118, 67, 59);
		System.out.print (".");	 // flag
		
		for (int i = 0; i < 33; i++) {
			if (source [i] == null) {
				System.err.println ("\n\nERROR: Something gone wrong with the images! Aborting ...");
				System.exit (-1);
			} /* if */
		} /* for */
		System.out.println (" Done.");
		
	} /* loadImages */
	
	/**
	 *  Sets, if to show the hexnumbers.
	 *
	 *  @param b if to show the hexnumbers
	 */
	public void setShowHexNumbers (boolean b) {
		
		hexnumbers = b;
		repaintAll ();
		repaint ();
		
	} /* setShowHexNumbers */
	
	/**
	 *  sets, if to show the unitnames.
	 *
	 *  @param b if to show the unitnames
	 */
	public void setShowUnitNames (boolean b) {
		
		unitnames = b;
		repaintAll ();
		repaint ();
		
	} /* setShowUnitNames */
	
	/**
	 *  Sets the level for a specific hex.
	 *  @param x X-count 
	 *  @param y Y-count
	 *  @param l level
	 */
	public void setLevel (int X, int Y, int l) {
		
		map [X][Y].level = l;
		paintImg (X, Y);
		repaint ();
		
	} /* setLevel */
	
	/**
	 *  Sets the level for tha actual hex.
	 *
	 *  @param l level
	 */
	public void setLevel (int l) {
		
		setLevel (oldX, oldY, l);
		
	} /* setLevel */
	
	/**
	 *  Sets the text for a specific hex.
	 *
	 *  @param t text
	 */
	public void setText (int X, int Y, String t) {
		
		map [X][Y].text = t;
		paintImg (X, Y);
		repaint ();
		
	} /* setText */
	
	/**
	 *  Sets the text for the actual hex.
	 *
	 *  @param t text
	 */
	public void setText (String t) {
		
		setText (oldX, oldY, t);
		
	} /* setText */
	
	/**
	 *  Sets the unit for a specific hex.
	 *
	 *  @param x X-count 
	 *  @param y Y-count
	 *  @param u Unit
	 *  @param d direction
	 *  @param c color
	 */
	public void setUnit (int X, int Y, String u, int d, int c) {
		
		map [X][Y].unit = u;
		map [X][Y].direction = d;
		map [X][Y].color = c;
		paintImg (X, Y);
		repaint ();
		
	} /* setUnit */
	
	/**
	 *  Sets the unit for the actual hex.
	 *
	 *  @param u unit
	 *  @param d direction
	 *  @param c color
	 */
	public void setUnit (String u, int d, int c) {
		
		setUnit (oldX, oldY, u, d, c);
		
	} /* setUnit */
	
	/**
	 *  Gets the unit-image for a specific hex.
	 *
	 *  @param x X-count 
	 *  @param y Y-count
	 *  @return unit-image
	 */    
	public Image getUnit (int X, int Y) {
		
		int i;
		for (i = 0; i < units.size(); i++) {
			if (((Unit)units.elementAt(i)).abk.equals (map[X][Y].unit))
				break;	    
		} /* for */
		if (i == units.size())
			return (source[13]);
		return (imageFac.rotateAndColorImage(((Unit)units.elementAt(i)).icon, map[X][Y].direction * 60, map[X][Y].color));
		
	} /* getUnit */
	
	/**
	 *  Sets the modifier for a specific hex.
	 *
	 *  @param x X-count 
	 *  @param y Y-count
	 *  @param m modifier
	 */
	public void setMod (int X, int Y, int m) {
		
		map [X][Y].mod = m;
		
		// delete all
		if (m == 0) {
			setUnit (X, Y, "", 0, 0);
			setSpecial (X, Y, 0);
			setRough (X, Y, false);
			setText (X, Y, "");
			setFlag (X, Y, -1);
		} /* if */
		paintImg (X, Y);
		repaint ();
		
	} /* setMod */
	
	/**
	 *  Sets the modifier for the actual hex.
	 *
	 *  @param m modifier
	 */
	public void setMod (int m) {
		
		setMod (oldX, oldY, m);
		
	} /* setMod */
	
	/**
	 *  Gets the modifier-image for a specific Hex.
	 *
	 *  @param x X-count 
	 *  @param y Y-count
	 *  @param m modifier
	 *  @return modifier-image
	 */
	public Image getMod (int X, int Y) {
		
		switch (map [X][Y].mod) {
			case 1: return (source[9]);	// sparse
			case 2: return (source[10]);	// dense
			case 3: return (source[10]);	// swamp
			case 4: return (source[12]);	// asphalt
			case 5: return (source[10]);	// ruins
			case 6: return (source[29]);	// light
			case 7: return (source[30]);	// medium
			case 8: return (source[31]);	// heavy
			case 9: return (source[32]);	// very heavy
		} /* switch */
		return (source[13]);  // nothing
		
	} /* getMod */
	
	/**
	 *  Sets the special modifier for a specific hex.
	 *
	 *  @param x X-count 
	 *  @param y Y-count
	 *  @param s modifier
	 */
	public void setSpecial (int X, int Y, int s) {
		
		map [X][Y].special = s;
		paintImg (X, Y);
		repaint ();
		
	} /* setSpecial */
	
	/**
	 *  Sets the special modifier for the actual hex.
	 *
	 *  @param s modifier
	 */
	public void setSpecial (int s) {
		
		setSpecial (oldX, oldY, s);
		
	} /* setSpecial */
	
	/**
	 *  Gets the special modifier-image for a specific Hex.
	 *
	 *  @param x X-count 
	 *  @param y Y-count
	 *  @param m modifier
	 *  @return special modifier-image
	 */
	public Image getSpecial (int X, int Y) {
		
		switch (map [X][Y].special) {
			case 2: return (source[14]);	// fire
			case 3: return (source[15]);	// smog
		} /* switch */
		return (source[13]);  // nothing
		
	} /* getSpecial */
	
	/**
	 *  Sets the rough shape for a specific hex
	 *
	 *  @param x X-count 
	 *  @param y Y-count
	 *  @param rough true, when rough
	 */
	public void setRough (int X, int Y, boolean rough) {
		
		map [X][Y].rough = rough;
		paintImg (X, Y);
		repaint ();
		
	} /* setRough */
	
	/**
	 *  Sets the rough shape for the actual hex.
	 *
	 *  @param river Integer-value, indicating all river-fields
	 */
	public void setRough (boolean rough) {
		
		setRough (oldX, oldY, rough);
		
	} /* setRough */
	
	/**
	 *  Sets the river for a specific hex.
	 *
	 *  @param x X-count 
	 *  @param y Y-count
	 *  @param river Integer-value, indicating all river-fields
	 */
	public void setRiver (int X, int Y, int river) {
		
		map [X][Y].river = river;
		paintImg (X, Y);
		repaint ();
		
	} /* setRiver */
	
	/**
	 *  Sets the river for the actual hex.
	 *
	 *  @param street Integer-value, indicating all street-fields
	 */
	public void setRiver (int river) {
		
		setRiver (oldX, oldY, river);
		
	} /* setRiver */
	
	/**
	 *  Sets a flag on a specific hex.
	 *
	 *  @param x X-count 
	 *  @param y Y-count
	 *  @param street Integer-value, indicating the color
	 */
	public void setFlag (int X, int Y, int color) {
		
		map [X][Y].flag = color;
		paintImg (X, Y);
		repaint ();
		
	} /* setFlag */
	
	/**
	 *  Sets a flag on the actual hex.
	 *
	 *  @param street Integer-value, indicating the color
	 */
	public void setFlag (int color) {
		
		setFlag (oldX, oldY, color);
		
	} /* setFlag */

	/**
	 *  Sets the street for a specific hex.
	 *
	 *  @param x X-count 
	 *  @param y Y-count
	 *  @param street Integer-value, indicating all street-fields
	 */
	public void setStreet (int X, int Y, int street) {
		
		map [X][Y].street = street;
		paintImg (X, Y);
		repaint ();
		
	} /* setRiver */
	
	/**
	 *  Sets the street for the actual hex.
	 *
	 *  @param street true, when street
	 */
	public void setStreet (int street) {
		
		setStreet (oldX, oldY, street);
		
	} /* setRiver */
	
	/**
	 *  Sets the new viewpoint.
	 *
	 *  @param value new X-space
	 */
	public void setYView (int value) {
		
		YView = value;
		repaint ();
		
	} /* setYView */
	
	/**
	 *  Sets the new viewpoint.
	 *
	 *  @param value new Y-space
	 */
	public void setXView (int value) {
		
		XView = value;
		repaint ();
		
	} /* setXView */
	
	/**
	 *  Gets the actual map-image.
	 *
	 *  @return map-image
	 */
	public Image getImage () {
		
		return (dbImage);
		
	} /* getImage */
	
	/**
	 *  Start the repainting.
	 *
	 *  @param g graphics-object, to draw on
	 */
	public void update (Graphics g) {
		
		paint (g);
		
	} /* update */
	
	/**
	 *  Paints the complet object.
	 *
	 *  @param g graphics-object, to draw on
	 */
	public void paint (Graphics g) {
		
		// if bufferimage is empty
		if (dbImage == null) {
			System.out.print ("Creating image-object ...");
			dbImage = createImage (50 * X_Size + 12 + 5 ,61 * Y_Size + 12 + 5);
			dbGraphics = dbImage.getGraphics ();
			System.out.println (" Done.");
			repaintAll ();
			if (startup != null) {
				startup.setVisible (false);
				startup = null;
			} /* if */
		} /* if */
			
		// calculate the position
		int X = (oldX-1) * 49, Y;
		if ((oldX % 2) == 1) {
			Y = (oldY-1) * 59; 
		} else {
			Y = (oldY-1) * 59 + 29;
		} /* else */
			
		// draw the map	
		g.drawImage (dbImage, -(XView-1)*49, -(YView-1)*59, this);
		// draw the highligth
		g.drawImage (source[8], X+5-(XView-1)*49, Y+5-(YView-1)*59, this);
		
	} /* paint */
	
	/**
	 *  Paints the entire map.
	 */
	private void repaintAll () {	
		
		for (int x = 1; x <= X_Size; x++)
			for (int y = 1; y <= Y_Size; y++)
				paintImg (x, y);
		
	} /* repaintAll */
	
	/**
	 *  Paints the specific hex.
	 *
	 *  @param X X-value
	 *  @param Y Y-value
	 */
	public void paintImg (int posX, int posY) {
		
		if (dbGraphics == null)
			return;
		String a;
		int x = posX; int y = posY;
		if (posX < 10) a = "0" + posX;
		else a = "" + posX;
		if (posY < 10) a = a + "0" + posY;
		else a = a + posY;	
		posX = (posX-1) * 49;
		if ((posX % 2) == 0) {
			posY = (posY-1) * 59; 
		} else {
			posY = (posY-1) * 59 + 29;
		} /* else */
		posX = posX + 5;
		posY = posY + 5;
		// level
		dbGraphics.drawImage (source[map [x][y].level + 3], posX, posY, this);
		if (map [x][y].level >= 0) {
			// river
			if ((map [x][y].river & 0x000001) == 1)
				dbGraphics.drawImage (source[22], posX, posY, this);
			if ((map [x][y].river & 0x000010) == 0x10)
				dbGraphics.drawImage (source[23], posX, posY, this);
			if ((map [x][y].river & 0x000100) == 0x100)
				dbGraphics.drawImage (source[24], posX, posY, this);
			if ((map [x][y].river & 0x001000) == 0x1000)
				dbGraphics.drawImage (source[25], posX, posY, this);
			if ((map [x][y].river & 0x010000) == 0x10000)
				dbGraphics.drawImage (source[26], posX, posY, this);
			if ((map [x][y].river & 0x100000) == 0x100000)
				dbGraphics.drawImage (source[27], posX, posY, this);
		} /* if */
		// street
		if ((map [x][y].street & 0x000001) == 1)
			dbGraphics.drawImage (source[16], posX, posY, this);
		if ((map [x][y].street & 0x000010) == 0x10)
			dbGraphics.drawImage (source[17], posX, posY, this);
		if ((map [x][y].street & 0x000100) == 0x100)
			dbGraphics.drawImage (source[18], posX, posY, this);
		if ((map [x][y].street & 0x001000) == 0x1000)
			dbGraphics.drawImage (source[19], posX, posY, this);
		if ((map [x][y].street & 0x010000) == 0x10000)
			dbGraphics.drawImage (source[20], posX, posY, this);
		if ((map [x][y].street & 0x100000) == 0x100000)
			dbGraphics.drawImage (source[21], posX, posY, this);
		// rough
		if (map [x][y].rough) 
			dbGraphics.drawImage (source[11], posX, posY, this);
		// specials, modifiers
		if (map [x][y].level >= 0) {
			dbGraphics.drawImage (getMod (x, y), posX, posY, this);
			dbGraphics.drawImage (getSpecial (x, y), posX, posY, this);
		} else {
			if (map [x][y].special == 3) 
				dbGraphics.drawImage (source[15], posX, posY, this);
		} /* else */
		// flag
		if (map[x][y].flag > -1)
			dbGraphics.drawImage (imageFac.colorImage (source[28], map[x][y].flag), posX, posY, this);
		// Hex-count
		dbGraphics.setFont (new Font ("Monospaced", Font.PLAIN, 12));
		if (hexnumbers) {
			dbGraphics.setColor (Color.white);
			dbGraphics.fillRect (posX + 20, posY + 46, 29, 10);
			dbGraphics.setColor (Color.black);
			dbGraphics.drawString (a, posX + 20, posY + 55);
		} /* if */
		// unit
		if (!map[x][y].unit.equals ("")) {
			if (unitnames) {
				dbGraphics.setColor (Color.white);
				dbGraphics.setFont (new Font ("Monospaced", Font.PLAIN, 12));
				dbGraphics.drawString (map[x][y].unit, posX + 34 - dbGraphics.getFontMetrics ().stringWidth (map[x][y].unit) / 2, posY + 10);	
			} /* if */
			dbGraphics.drawImage (getUnit (x, y), posX, posY-2, this);
		} /* if */
		// text
		dbGraphics.setColor (Color.yellow);
		dbGraphics.setFont (new Font ("Monospaced", Font.BOLD, 13));
		dbGraphics.drawString (map[x][y].text, posX + 62 - dbGraphics.getFontMetrics ().stringWidth (map[x][y].text), posY + 33);	
		
	} /* paintImg */
	
	/**
	 *  handle the mousemotion.
	 *
	 *  @param e MouseEvent
	 */
	protected void processMouseMotionEvent (MouseEvent e) {
		
		if (e.getID() == MouseEvent.MOUSE_MOVED || e.getID() == MouseEvent.MOUSE_DRAGGED) {
			int X = e.getX ();
			int Y = e.getY ();
			int x = (X - 10) / 49 + XView;
			if (x % 2 == 0) 
				Y = Y - 29;
			int y = Y / 58 + YView;
			if (!(x > X_Size) && (x > 0) && !(y > Y_Size) && (y > 0)) {
				if ((oldX != x) || (oldY != y)) {
					oldX = x; oldY = y;
					repaint();
				} /* if */
			} /* if */
		} /* if */
		super.processMouseMotionEvent (e);
		
	} /* processMouseMotionEvent */
	
	/** indicates, if to show the hexnumbers */
	private boolean hexnumbers;
	/** indicates, if to show the unitnames */
	private boolean unitnames;
	/** all the needed images */
	public Image [] source;
	/** size of the map */
	public int X_Size;
	/** size of the map */
	public int Y_Size;
	/** all mech-images */
	public Vector units;
	/** the map-data */
	private Hex [][] map;
	/** offscreen-image */
	private Image dbImage;
	/** graphic-context of th offscreen-image */
	private Graphics dbGraphics;
	/** actual mouseposition */
	private int oldX;
	/** actual mouseposition */
	private int oldY;
	/** actual viewpoint */
	private int XView;
	/** actual viewpoint */
	private int YView;
	/** only for the output on startup */
	private StartUp startup;
	/** for any imagemanipulation */
	private ImageFactory imageFac;
} /* class */