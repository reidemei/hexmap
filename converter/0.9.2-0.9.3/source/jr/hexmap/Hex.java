package jr.hexmap;

public class Hex
		extends java.lang.Object
		implements java.io.Serializable {
	
	public int level;
	// -3 to 4    
	
	public int mod;
	// 0 - nothing
	// 1 - sparse wood
	// 2 - dense wood
	// 3 - swamp
	// 4 - asphalt
	// 5 - ruins
	// buildings 
	// 6 - light
	// 7 - middle
	// 8 - heavy
	// 9 - very heavy
	
	public int special;
	// 2 - fire
	// 3 - smog

	public boolean rough;
			
	public int flag;
	// -1 - none
	//  0 - white
	//  1 - yellow
	//  2 - Green
	//  3 - Blue
	//  4 - Red
	
	public String unit;
	
	public int direction;
	// 0 to 5
	
	public int color;
	// 0 - white
	// 1 - yellow
	// 2 - Green
	// 3 - Blue
	// 4 - Red
			
	public int street;
			
	public int river;

	public String text;
	
	public Hex () {
		level = 0; mod = 0; special = 0; color = 0; street = 0;
		unit = ""; direction = 0; text = ""; rough = false; river = 0;
	} /* constructor */
} /* class */