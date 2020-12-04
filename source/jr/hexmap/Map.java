package net.reidemeister.hexmap;

import net.reidemeister.util.*;

import java.awt.*;
import java.awt.event.*; 
import java.awt.image.*;
import java.io.*;
import java.util.*;

/**
 *  A JComponent, capable to display a hexfieldmap.
 *
 *  @version 1.2 (2001-01-21)
 *  @author Jan Reidemeister
 */ 
public class Map extends javax.swing.JComponent {

	/** the version */
	public final static String[] LOAD_VERSIONS = {"1.2", "1.3"};
	/** the version */
	public final static String VERSION = "1.3";
	/** int of Color white */
	public static final int RGB_WHITE = Color.white.getRGB();
	/** int of Color black */
	public static final int RGB_BLACK = Color.black.getRGB();
	/** int of Color red */
	public static final int RGB_RED = Color.red.getRGB();
	/** int of Color green */
	public static final int RGB_GREEN = Color.green.getRGB();
	/** int of Color blue */
	public static final int RGB_BLUE = Color.blue.getRGB();
	/** int of Color yellow */
	public static final int RGB_YELLOW = Color.yellow.getRGB();
	/** the color for the hexnumbers */
	private Color hexnumberColor;
	/** the image for the mousemap */
	private BufferedImage mousemap;	
	/** the size of the map - points */
	private int picSizeX;
	/** the size of the map - points */
	private int picSizeY;
	/** indicates, if to show the hexnumbers */
	private boolean hexnumbers;
	/** indicates, if to show the unitnames */
	private boolean unitnames;
	/** size of the map - hexes */
	public int X_Size;
	/** size of the map - hexes */
	public int Y_Size;
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
	/** for any imagemanipulation */
	private ImageFactory imageFac;

	/**
	 *  Creates a new Map.
	 *
	 *  @param X 	the size of the map
	 *  @param Y 	the size of the map
	 *  @param fac 	an ImageFactory for imagehandling
	 *
	 */
	public Map (int X, int Y, ImageFactory fac) {
		Debug.print ("Map - constuctor - X: " + X + ", Y: " + Y +", ImageFactory: " + fac);
		hexnumbers = true;
		setCursor (new Cursor (Cursor.HAND_CURSOR));
		if (fac == null)
			imageFac = new ImageFactory ();
		else
			imageFac = fac;
		enableEvents(AWTEvent.MOUSE_MOTION_EVENT_MASK);
		makeMap (X, Y);
		oldX = oldY = 1;
		hexnumberColor = Color.white;
		Image img = imageFac.loadImage ("net/reidemeister/hexmap/mousemap.gif");
		if (img == null)
			;
		mousemap = new BufferedImage (68, 75, BufferedImage.TYPE_INT_RGB);
		mousemap.getGraphics ().drawImage (img, 0, 0, this);
	} /* constructor */

	/**
	 *  Creates a new map with the size of 16x16.
	 *
	 */
	public Map () {
		this (16, 16, null);
	} /* constructor */

	/**
	 *  Creates a new map with the size of 16x16.
	 *
	 *  @param fac 	an ImageFactory for imagehandling
	 *
	 */
	public Map (ImageFactory fac) {
		this (16, 16, fac);
	} /* constructor */

	/**
	 *  Generates a new empty map with the given size.
	 *
	 *  @param x 	X-size
	 *  @param y 	Y-size
	 *
	 */
	public void makeMap (int x, int y) {
		System.out.print ("Initialising map (" + x + "," + y + ") ");	
		Dimension d = getPreferredSize ();
		X_Size = x; Y_Size = y;
		map = new Hex [x+1][y+1];
		for (int i = 0; i <= x; i++) {
			for (int j = 0; j <= y; j++) {
				map [i][j] = new Hex();
				map [i][j].flag = -1;
			} /* for */
			System.out.print (".");	
		} /* for */
		System.out.println (" Done.");	
		picSizeX = 67 + (49 * (X_Size-1));
		picSizeY = 29 + (59 * Y_Size);
		initMap ();
		firePropertyChange("preferredSize", d, getPreferredSize ());
		firePropertyChange("maximumSize", d, getPreferredSize ());
		firePropertyChange("minimumSize", d, getPreferredSize ());
	} /* makeMap */

	/**
	 *  Creates the image for the map and draws all hexes.
	 *
	 */
	private void initMap () { 
		dbImage = null;
		dbGraphics = null;
		System.out.print ("Creating image-object ...");
		dbImage = new BufferedImage (
						(int) getPreferredSize ().getWidth (), 
						(int) getPreferredSize ().getHeight (), 
						BufferedImage.TYPE_INT_RGB);
		//createImage (50 * X_Size + 10, 61 * Y_Size + 10);
		dbGraphics = dbImage.getGraphics ();
		dbGraphics.setColor (Color.white);
		dbGraphics.fillRect (0, 0, (int) getPreferredSize ().getWidth (), (int) getPreferredSize ().getHeight ());
		System.out.println (" Done.");
		repaintAll ();
		invalidate ();
	} /* initMap */

	/**
	 *  Saves the complete map.
	 *
	 *  @param out 	FileOutputStream, to write on
	 *
	 */
	public boolean saveMap (FileWriter out){
		Debug.print ("Map - saveMap - out: " + out);				
		try {
			System.out.print ("Saving map ");
			BufferedWriter w = new BufferedWriter (out);
			w.write ("HexMap" + System.getProperty ("line.separator"));
			w.write (VERSION + System.getProperty ("line.separator"));
			w.write (X_Size + System.getProperty ("line.separator"));
			w.write (Y_Size + System.getProperty ("line.separator"));
			for (int i = 1; i <= X_Size; i++) {
				for (int j = 1; j <= Y_Size; j++) {
					String s = i + "|" + j + "|";
					s = s + map [i][j].building + "|";
					s = s + map [i][j].color + "|";
					s = s + map [i][j].direction + "|";
					s = s + map [i][j].flag + "|";
					s = s + map [i][j].ground + "|";
					s = s + map [i][j].level + "|";
					s = s + map [i][j].river + "|";
					s = s + ("" + map [i][j].rough).toUpperCase () + "|";
					s = s + map [i][j].special + "|";
					s = s + map [i][j].street + "|";
					if (map [i][j].text.length () == 0)
						s = s + " |";
					else	
						s = s + map [i][j].text + "|";
					if (map [i][j].unit.length () == 0)
						s = s + " |";
					else	
						s = s + map [i][j].unit + "|";
					s = s + map [i][j].wood;
					w.write (s + System.getProperty ("line.separator"));
				} /* for */
				System.out.print (".");	
			} /* for */
			w.write (hexnumberColor.getRGB () + System.getProperty ("line.separator"));
			w.write (("" + hexnumbers).toUpperCase () + System.getProperty ("line.separator"));
			w.write (("" + unitnames).toUpperCase () + System.getProperty ("line.separator"));
			w.flush ();
			w.close ();
			System.out.println (" Done.");
		} catch (IOException e) {
			return (false);
		} /* catch */
		return (true);
	} /* saveMap */

	/**
	 *  Loads the complete map.
	 *
	 *  @param in 	FileInputStream, to read from
	 *
	 */
	public boolean loadMap (java.io.FileReader in){
		Debug.print ("Map - loadMap - in: " + in);
		Dimension d = getPreferredSize ();
		try {
			System.out.print ("Loading map ");
			BufferedReader inp = new BufferedReader (in);
			String s = inp.readLine ();
			System.out.print (".");	
			// is it for us
			if (!(s.trim ().equals ("HexMap")))
				return (false);
			String version = inp.readLine ().trim ();
			System.out.print (".");	
			// check the version
			int count = 0;
			for (int i = 0; i < LOAD_VERSIONS.length; i++)
				if (!(version.equals (LOAD_VERSIONS[i])))
					count++;
			if (count == LOAD_VERSIONS.length)
				return (false);
			// now get the size	
			s = inp.readLine ();
			System.out.print (".");	
			int xsize = Integer.parseInt (s);
			s = inp.readLine ();
			System.out.print (".");	
			int ysize = Integer.parseInt (s);
			// get every hex
			Hex[][] newMap = new Hex[xsize+1][ysize+1];
			for (int i = 1; i <= xsize; i++) {
				System.out.print (".");	
				for (int j = 1; j <= ysize; j++) {
					s = inp.readLine ();
					StringTokenizer st = new StringTokenizer (s, "|");
					// Throw the first 2 away
					st.nextToken ();
					st.nextToken ();
					Hex hex = new Hex ();
					hex.building = Integer.parseInt (st.nextToken ());
					hex.color = Integer.parseInt (st.nextToken ());
					hex.direction = Integer.parseInt (st.nextToken ());
					hex.flag = Integer.parseInt (st.nextToken ());
					hex.ground = Integer.parseInt (st.nextToken ());
					hex.level = Integer.parseInt (st.nextToken ());
					hex.river = Integer.parseInt (st.nextToken ());
					hex.rough = (st.nextToken ().equals ("TRUE"));
					hex.special = Integer.parseInt (st.nextToken ());
					hex.street = Integer.parseInt (st.nextToken ());
					hex.text = st.nextToken ().trim ();
					hex.unit = st.nextToken ().trim ();
					hex.wood = Integer.parseInt (st.nextToken ());
					newMap[i][j] = hex;
				} /* for */
			} /* for */
			if (version.equals ("1.2")) {
				// dont load any more
			} else {
				hexnumberColor = Color.decode (inp.readLine ());
				hexnumbers = (inp.readLine ().equals ("TRUE"));
				unitnames = (inp.readLine ().equals ("TRUE"));
			} /* else */
			inp.close ();
			// all is fine, set the real values
			X_Size = xsize;
			Y_Size = ysize;
			map = newMap;
			System.out.println (" Done.");				
			initMap ();
		} catch (Exception e) {
			return (false);
		} /* catch */
		firePropertyChange("preferredSize", d, getPreferredSize ());
		firePropertyChange("maximumSize", d, getPreferredSize ());
		firePropertyChange("minimumSize", d, getPreferredSize ());
		return (true);
	} /* saveMap */

	/**
	 *  Sets the color for the hexnumbers.
	 *
	 *  @param c 	the color
	 *
	 */
	public void setHexNumberColor (Color c) {
		Debug.print ("Map - setHexNumberColor - c: " + c);
		hexnumberColor = c;
		repaintAll ();
		repaint ();
	} /* setHexNumberColor */

	/**
	 *  Gets the color for the hexnumbers.
	 *
	 *  @return 	the color
	 *
	 */
	public Color getHexNumberColor () {
		return (hexnumberColor);
	} /* getHexNumberColor */

	/**
	 *  Sets, if to show the hexnumbers.
	 *
	 *  @param b 	if to show the hexnumbers
	 *
	 */
	public void setShowHexNumbers (boolean b) {
		Debug.print ("Map - setHexNumbers - b: " + b);
		hexnumbers = b;
		repaintAll ();
		repaint ();
	} /* setShowHexNumbers */
	
	/**
	 *  Gets, if to show the hexnumbers.
	 *
	 *  @return 	if to show the hexnumbers
	 *
	 */
	public boolean getShowHexNumbers () {
		return (hexnumbers);
	} /* getShowHexNumbers */
	
	/**
	 *  Sets, if to show the unitnames.
	 *
	 *  @param b 	if to show the unitnames
	 *
	 */
	public void setShowUnitNames (boolean b) {
		Debug.print ("Map - setShowUnitNames - b: " + b);
		unitnames = b;
		repaintAll ();
		repaint ();
	} /* setShowUnitNames */

	/**
	 *  Gets, if to show the unitnames.
	 *
	 *  @return 	if to show the unitnames
	 *
	 */
	public boolean getShowUnitNames () {
		return (unitnames);
	} /* getShowUnitNames */

	/**
	 *  Sets the level for a specific hex.
	 *
	 *  @param x 	X-pos 
	 *  @param y 	Y-pos
	 *  @param l 	the level
	 *
	 */
	public void setLevel (int X, int Y, int l) {
		Debug.print ("Map - setLevel - X: " + X + ", Y: " + Y +", l: " + l);
		map [X][Y].level = l;
		paintImg (X, Y);
		repaint ();
	} /* setLevel */

	/**
	 *  Sets the level for tha actual hex.
	 *
	 *  @param l 	the level
	 *
	 */
	public void setLevel (int l) {
		setLevel (oldX, oldY, l);
	} /* setLevel */

	/**
	 *  Sets the text for a specific hex.
	 *
	 *  @param x 	X-pos 
	 *  @param y 	Y-pos
	 *  @param t 	the text
	 *
	 */
	public void setText (int X, int Y, String t) {
		Debug.print ("Map - setText - X: " + X + ", Y: " + Y +", t: " + t);
		map [X][Y].text = t;
		paintImg (X, Y);
		repaint ();
	} /* setText */

	/**
	 *  Sets the text for the actual hex.
	 *
	 *  @param t the text
	 *
	 */
	public void setText (String t) {
		setText (oldX, oldY, t);
	} /* setText */

	/**
	 *  Sets the unit for a specific hex.
	 *
	 *  @param x 	X-pos 
	 *  @param y 	Y-pos
	 *  @param u 	Unit
	 *  @param d 	direction
	 *  @param c 	color
	 *
	 */
	public void setUnit (int X, int Y, String u, int d, int c) {
		Debug.print ("Map - setUnit - X: " + X + ", Y: " + Y +", u: " + u + ", d: " + d + ", c: " + c);
		map [X][Y].unit = u;
		map [X][Y].direction = d;
		map [X][Y].color = c;
		paintImg (X, Y);
		repaint ();
	} /* setUnit */

	/**
	 *  Sets the unit for the actual hex.
	 *
	 *  @param u 	unit
	 *  @param d 	direction
	 *  @param c 	color
	 *
	 */
	public void setUnit (String u, int d, int c) {
		setUnit (oldX, oldY, u, d, c);
	} /* setUnit */

	/**
	 *  Gets the unit-image for a specific hex.
	 *
	 *  @param x 	X-pos 
	 *  @param y 	Y-pos
	 *
	 *  @return 	unit-image
	 *
	 */    
	public Image getUnit (int X, int Y) {
		Debug.print ("Map - getUnit - X: " + X + ", Y: " + Y);
		int i;
		for (i = 0; i < Images.units.size(); i++) {
			if (((Unit) Images.units.elementAt(i)).abk.equals (map[X][Y].unit))
				break;	    
		} /* for */
		if (i == Images.units.size())
			return (Images.empty);
		return (imageFac.rotateAndColorImage(((Unit) Images.units.elementAt(i)).icon, map[X][Y].direction * 60, map[X][Y].color));
	} /* getUnit */

	/**
	 *  All elements in that hex will be deleted.
	 *
	 *  @param x 	X-pos 
	 *  @param y 	Y-pos
	 *
	 */
	public void emptyHex (int X, int Y) {
		setUnit (X, Y, "", 0, 0);
		setWood (X, Y, Hex.WOOD_NOTHING);
		setGround (X, Y, Hex.GROUND_NOTHING);
		setSpecial (X, Y, Hex.SPECIAL_NOTHING);
		setBuilding (X, Y, Hex.BUILDING_NOTHING);
		setRough (X, Y, false);
		setStreet(X, Y, 0);
		setRiver(X, Y, 0);
		setText (X, Y, "");
		setFlag (X, Y, -1);
		paintImg (X, Y);
		repaint ();		
	} /* emptyHex */

	/**
	 *  All elements in the actual hex will be deleted.
	 *
	 */
	public void emptyHex () {
		emptyHex (oldX, oldY);
	} /* emptyHex */

	/**
	 *  Sets the ground for a specific hex.
	 *
	 *  @param x 	X-pos 
	 *  @param y 	Y-pos
	 *  @param g 	the ground
	 *
	 */
	public void setGround (int X, int Y, int g) {
		Debug.print ("Map - setGround - X: " + X + ", Y: " + Y +", g: " + g);
		map [X][Y].ground = g;
		// delete all
		paintImg (X, Y);
		repaint ();
	} /* setGround */

	/**
	 *  Sets the ground for the actual hex.
	 *
	 *  @param g 	the ground
	 *
	 */
	public void setGround (int g) {
		setGround (oldX, oldY, g);
	} /* setGround */

	/**
	 *  Gets the modifier-image for a specific Hex.
	 *
	 *  @param x 	X-pos 
	 *  @param y 	Y-pos
	 *
	 *  @return 	ground-image
	 *
	 */
	public Image getGround (int X, int Y) {
		Debug.print ("Map - getGround - X: " + X + ", Y: " + Y);
		switch (map [X][Y].ground) {
			case Hex.GROUND_SWAMP: return (Images.swamp);
			case Hex.GROUND_ASPHALT: return (Images.asphalt);
		} /* switch */
		return (Images.empty);  // nothing
	} /* getGround */

	/**
	 *  Sets the building for a specific hex.
	 *
	 *  @param x 	X-pos 
	 *  @param y 	Y-pos
	 *  @param b 	the building
	 *
	 */
	public void setBuilding (int X, int Y, int b) {
		Debug.print ("Map - setBuilding - X: " + X + ", Y: " + Y +", b: " + b);
		map [X][Y].building = b;
		paintImg (X, Y);
		repaint ();
	} /* setBuilding */

	/**
	 *  Sets the building for the actual hex.
	 *
	 *  @param b 	the buiding
	 *
	 */
	public void setBuilding (int b) {
		setBuilding (oldX, oldY, b);
	} /* setBuilding */

	/**
	 *  Gets the building-image for a specific Hex.
	 *
	 *  @param x 	X-pos 
	 *  @param y 	Y-pos
	 *
	 *  @return 	building-image
	 *
	 */
	public Image getBuilding (int X, int Y) {
		Debug.print ("Map - getBuilding - X: " + X + ", Y: " + Y);
		switch (map [X][Y].building) {
			case Hex.BUILDING_RUINS: return (Images.ruins);
			case Hex.BUILDING_LIGHT: return (Images.lightBuilding);
			case Hex.BUILDING_MEDIUM: return (Images.mediumBuilding);
			case Hex.BUILDING_HEAVY: return (Images.heavyBuilding);
			case Hex.BUILDING_VERYHEAVY: return (Images.veryHeavyBuilding);
		} /* switch */
		return (Images.empty);  // nothing
	} /* getBuilding */

	/**
	 *  Sets the wood for a specific hex.
	 *
	 *  @param x 	X-pos 
	 *  @param y 	Y-pos
	 *  @param w 	the wood
	 *
	 */
	public void setWood (int X, int Y, int w) {
		Debug.print ("Map - setWood - X: " + X + ", Y: " + Y +", w: " + w);
		map [X][Y].wood = w;
		paintImg (X, Y);
		repaint ();
	} /* setWood */

	/**
	 *  Sets the wood for the actual hex.
	 *
	 *  @param b 	the wood
	 *
	 */
	public void setWood (int b) {
		setWood (oldX, oldY, b);
	} /* setWood */

	/**
	 *  Gets the wood-image for a specific Hex.
	 *
	 *  @param x 	X-pos 
	 *  @param y 	Y-pos
	 *
	 *  @return 	wood-image
	 *
	 */
	public Image getWood (int X, int Y) {
		Debug.print ("Map - getWood - X: " + X + ", Y: " + Y);
		switch (map [X][Y].wood) {
			case Hex.WOOD_SPARSE: return (Images.sparseWood);
			case Hex.WOOD_DENSE: return (Images.denseWood);
		} /* switch */
		return (Images.empty);  // nothing
	} /* getWood */

	/**
	 *  Sets the special modifier for a specific hex.
	 *
	 *  @param x 	X-pos 
	 *  @param y 	Y-pos
	 *  @param s 	the special
	 *
	 */
	public void setSpecial (int X, int Y, int s) {
		Debug.print ("Map - setSpecial - X: " + X + ", Y: " + Y +", s: " + s);
		map [X][Y].special = s;
		paintImg (X, Y);
		repaint ();
	} /* setSpecial */

	/**
	 *  Sets the special modifier for the actual hex.
	 *
	 *  @param s 	the special
	 */
	public void setSpecial (int s) {
		setSpecial (oldX, oldY, s);
	} /* setSpecial */

	/**
	 *  Gets the special modifier-image for a specific Hex.
	 *
	 *  @param x 	X-pos 
	 *  @param y 	Y-pos
	 *  @param m 	modifier
	 *
	 *  @return 	special modifier-image
	 *
	 */
	public Image getSpecial (int X, int Y) {
		Debug.print ("Map - getSpecial - X: " + X + ", Y: " + Y);
		switch (map [X][Y].special) {
			case Hex.SPECIAL_FIRE: return (Images.fire);
			case Hex.SPECIAL_SMOG: return (Images.smog);
		} /* switch */
		return (Images.empty);  // nothing
	} /* getSpecial */

	/**
	 *  Sets the rough shape for a specific hex
	 *
	 *  @param x 	X-pos 
	 *  @param y 	Y-pos
	 *  @param r 	true, when rough
	 *
	 */
	public void setRough (int X, int Y, boolean r) {
		Debug.print ("Map - setRough - X: " + X + ", Y: " + Y +", r: " + r);
		map [X][Y].rough = r;
		paintImg (X, Y);
		repaint ();
	} /* setRough */

	/**
	 *  Sets the rough shape for the actual hex.
	 *
	 *  @param r 	true, when rough
	 */
	public void setRough (boolean r) {
		setRough (oldX, oldY, r);
	} /* setRough */
	
	/**
	 *  Sets the river for a specific hex.
	 *
	 *  @param x 		X-pos 
	 *  @param y 		Y-pos
	 *  @param river 	Integer-value, indicating all river-fields
	 */
	public void setRiver (int X, int Y, int river) {
		Debug.print ("Map - setRiver - X: " + X + ", Y: " + Y +", river: " + river);
		map [X][Y].river = river;
		paintImg (X, Y);
		repaint ();
	} /* setRiver */

	/**
	 *  Sets the river for the actual hex.
	 *
	 *  @param street 	Integer-value, indicating all street-fields
	 */
	public void setRiver (int river) {
		setRiver (oldX, oldY, river);
	} /* setRiver */

	/**
	 *  Sets a flag on a specific hex.
	 *
	 *  @param x 		X-pos 
	 *  @param y 		Y-pos
	 *  @param color 	Integer-value, indicating the color
	 */
	public void setFlag (int X, int Y, int color) {
		Debug.print ("Map - setFlag - X: " + X + ", Y: " + Y +", color: " + color);
		map [X][Y].flag = color;
		paintImg (X, Y);
		repaint ();
	} /* setFlag */

	/**
	 *  Sets a flag on the actual hex.
	 *
	 *  @param color 	Integer-value, indicating the color
	 */
	public void setFlag (int color) {
		setFlag (oldX, oldY, color);
	} /* setFlag */
	
	/**
	 *  Sets the street for a specific hex.
	 *
	 *  @param x 		X-pos 
	 *  @param y 		Y-pos
	 *  @param street 	Integer-value, indicating all street-fields
	 */
	public void setStreet (int X, int Y, int street) {
		Debug.print ("Map - setStreet - X: " + X + ", Y: " + Y +", street: " + street);
		map [X][Y].street = street;
		paintImg (X, Y);
		repaint ();
	} /* setRiver */
	
	/**
	 *  Sets the street for the actual hex.
	 *
	 *  @param street 	true, when street
	 */
	public void setStreet (int street) {
		setStreet (oldX, oldY, street);
	} /* setRiver */

	/**
	 *  Gets the actual map-image.
	 *
	 *  @return 	map-image
	 *
	 */
	public Image getImage () {
		return (dbImage);
	} /* getImage */
	
	/**
	 *  Start the repainting.
	 *
	 *  @param g 	graphics-object, to draw on
	 *
	 */
	public void update (Graphics g) {
		paint (g);
	} /* update */
	
	/**
	 *  Paints the complet object.
	 *
	 *  @param g 	graphics-object, to draw on
	 *
	 */
	public void paint (Graphics g) {
		Debug.print ("Map - paint");
		// calculate the position
		int X = (oldX-1) * 49, Y;
		if ((oldX % 2) == 1) {
			Y = (oldY-1) * 59; 
		} else {
			Y = (oldY-1) * 59 + 29;
		} /* else */
		// draw the map	
		g.drawImage (dbImage, 0, 0, this);
		// draw the highligth
		g.drawImage (Images.highlight, X, Y, this);
	} /* paint */
	
	/**
	 *  Paints the entire map.
	 *
	 */
	private void repaintAll () {	
		Debug.print ("Map - repaintAll");		
		for (int x = 1; x <= X_Size; x++)
			for (int y = 1; y <= Y_Size; y++)
				paintImg (x, y);
	} /* repaintAll */
	
	/**
	 *  Paints the specific hex.
	 *
	 *  @param x 	X-pos 
	 *  @param y 	Y-pos
	 *
	 */
	public void paintImg (int posX, int posY) {
		Debug.print ("Map - paintImg - posX: " + posX + ", posY: " + posY );
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
		posX = posX;
		posY = posY;
		// level
		switch (map [x][y].level) {
			case (-3): dbGraphics.drawImage (Images.levelm3, posX, posY, this); break;
			case (-2): dbGraphics.drawImage (Images.levelm2, posX, posY, this); break;
			case (-1): dbGraphics.drawImage (Images.levelm1, posX, posY, this); break;
			case (0): dbGraphics.drawImage (Images.level0, posX, posY, this); break;
			case (1): dbGraphics.drawImage (Images.level1, posX, posY, this); break;
			case (2): dbGraphics.drawImage (Images.level2, posX, posY, this); break;
			case (3): dbGraphics.drawImage (Images.level3, posX, posY, this); break;
			case (4): dbGraphics.drawImage (Images.level4, posX, posY, this); break;
			default: 
					dbGraphics.setColor (Color.yellow);
					dbGraphics.setFont (new Font ("Monospaced", Font.BOLD, 13));
					if (map [x][y].level < -3) {
						dbGraphics.drawImage (Images.levelm3, posX, posY, this);
						dbGraphics.drawString (Integer.toString (map[x][y].level), posX + 5, posY + 33);	
					} else {
						dbGraphics.drawImage (Images.level4, posX, posY, this);
						dbGraphics.drawString (Integer.toString (map[x][y].level), posX + 5 + dbGraphics.getFontMetrics ().stringWidth ("-"), posY + 33);	
					} /* else */
		} /* switch */
		if (map [x][y].level >= 0) {
			// river
			if ((map [x][y].river & 0x000001) == 1)
				dbGraphics.drawImage (Images.river1, posX, posY, this);
			if ((map [x][y].river & 0x000010) == 0x10)
				dbGraphics.drawImage (Images.river2, posX, posY, this);
			if ((map [x][y].river & 0x000100) == 0x100)
				dbGraphics.drawImage (Images.river3, posX, posY, this);
			if ((map [x][y].river & 0x001000) == 0x1000)
				dbGraphics.drawImage (Images.river4, posX, posY, this);
			if ((map [x][y].river & 0x010000) == 0x10000)
				dbGraphics.drawImage (Images.river5, posX, posY, this);
			if ((map [x][y].river & 0x100000) == 0x100000)
				dbGraphics.drawImage (Images.river6, posX, posY, this);
		} /* if */
		// street
		if ((map [x][y].street & 0x000001) == 1)
			dbGraphics.drawImage (Images.street1, posX, posY, this);
		if ((map [x][y].street & 0x000010) == 0x10)
			dbGraphics.drawImage (Images.street2, posX, posY, this);
		if ((map [x][y].street & 0x000100) == 0x100)
			dbGraphics.drawImage (Images.street3, posX, posY, this);
		if ((map [x][y].street & 0x001000) == 0x1000)
			dbGraphics.drawImage (Images.street4, posX, posY, this);
		if ((map [x][y].street & 0x010000) == 0x10000)
			dbGraphics.drawImage (Images.street5, posX, posY, this);
		if ((map [x][y].street & 0x100000) == 0x100000)
			dbGraphics.drawImage (Images.street6, posX, posY, this);
		// rough
		if (map [x][y].rough)
			dbGraphics.drawImage (Images.rough, posX, posY, this);
		// specials, modifiers
		if (map [x][y].level >= 0) {
			dbGraphics.drawImage (getGround (x, y), posX, posY, this);
			dbGraphics.drawImage (getBuilding (x, y), posX, posY, this);
			dbGraphics.drawImage (getWood (x, y), posX, posY, this);
			dbGraphics.drawImage (getSpecial (x, y), posX, posY, this);
		} else {
			dbGraphics.drawImage (getBuilding (x, y), posX, posY, this);
			if (map [x][y].special == Hex.SPECIAL_SMOG) 
				dbGraphics.drawImage (Images.smog, posX, posY, this);
		} /* else */
		// flag
		if (map[x][y].flag > -1)
			dbGraphics.drawImage (imageFac.colorImage (Images.flag, map[x][y].flag), posX, posY, this);
		// Hex-count
		dbGraphics.setFont (new Font ("Monospaced", Font.PLAIN, 12));
		dbGraphics.setColor (Color.white);
		if (hexnumbers) {
			dbGraphics.setColor (hexnumberColor);
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
	 *  Handle the mousemotion.
	 *
	 *  @param e MouseEvent
	 *
	 */
	protected void processMouseMotionEvent (MouseEvent e) {
		if (e.getID() == MouseEvent.MOUSE_MOVED || e.getID() == MouseEvent.MOUSE_DRAGGED) {
			int X = e.getX ();
			int Y = e.getY ();
			int x = (X - 10) / 49 + 1;
			if (x % 2 == 0) 
				Y = Y - 29;
			int y = Y / 59 + 1;			
			int mouseMapX = X - (x-1)*49;
			int mouseMapY = Y - (y-1)*59;
			if ((mouseMapX >= 0) && (mouseMapY >= 0)) {
				int rgbColor = mousemap.getRGB (mouseMapX, mouseMapY);
				Point pt = null;
				if (rgbColor == RGB_WHITE) {
					pt = new Point(x, y);
					if (x % 2 == 0)
						pt.y = pt.y - 1;
				} else if (rgbColor == RGB_RED) {
					pt = new Point(x - 1, y - 1);
				} else if (rgbColor == RGB_GREEN) {
					pt = new Point(x - 1, y);
				} else if (rgbColor == RGB_BLUE) {
					pt = new Point(x + 1, y);
				} else if (rgbColor == RGB_YELLOW) {
					pt = new Point(x + 1, y - 1);
				} else if (rgbColor == RGB_BLACK) {
				} else {
					System.err.println("Error in method getHex()");
				} /* else */
				if (pt == null || pt.x <= 0 || pt.y < 0 || pt.x > X_Size || pt.y > Y_Size) {
					// invalid hex
				} else {
					// OK
					if (x % 2 == 0)
						pt.y = pt.y + 1;
					if (pt.y > 0) {		
						Debug.print ("Map - processMouseMotionEvent - x: " + pt.x + ", y: " + pt.y);
						if ((oldX != pt.x) || (oldY != pt.y)) {
							oldX = pt.x; oldY = pt.y;
							repaint();
						} /* if */
					} /* if */
				} /* else */			
			} /* if */
			//
		} /* if */
		super.processMouseMotionEvent (e);
	} /* processMouseMotionEvent */

	/**
	 *  Returns the minimum size for the map.
	 *
	 *  @return 	the size     
	 */
	public Dimension getMinimumSize () {
		return (getPreferredSize ());
	} /* getMinimumSize */

	/**
	 *  Returns the preferred size for the map.
	 *
	 *  @return 	the size     
	 */
	public Dimension getPreferredSize () {
		return (new Dimension (picSizeX,  picSizeY));
	} /* getPreferredSize */

	/**
	 *  Returns the maximum size for the map.
	 *
	 *  @return 	the size     
	 */
	public Dimension getMaximumSize () {
		return (getPreferredSize ());
	} /* getMaximumSize */
} /* class */
