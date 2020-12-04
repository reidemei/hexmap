package net.reidemeister.hexmap;

public class Hex extends java.lang.Object implements java.io.Serializable {
	
	public int level;
	
	public int wood;
	public final static int WOOD_NOTHING = 0;
	public final static int WOOD_SPARSE = 1;
	public final static int WOOD_DENSE = 2;

	public int ground;
	public final static int GROUND_NOTHING = 0;
	public final static int GROUND_SWAMP = 1;
	public final static int GROUND_ASPHALT = 2;
	
	public int building;
	public final static int BUILDING_NOTHING = 0;
	public final static int BUILDING_RUINS = 1;
	public final static int BUILDING_LIGHT = 2;
	public final static int BUILDING_MEDIUM = 3;
	public final static int BUILDING_HEAVY = 4;
	public final static int BUILDING_VERYHEAVY = 5;

	public int special;
	public final static int SPECIAL_NOTHING = 0;
	public final static int SPECIAL_FIRE = 2;
	public final static int SPECIAL_SMOG = 3;

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
	
	public Hex() {
		level = 0; 
		wood = WOOD_NOTHING; 
		ground = GROUND_NOTHING; 
		building = BUILDING_NOTHING; 
		special = SPECIAL_NOTHING; 
		color = 0; 
		street = 0;
		unit = ""; 
		direction = 0; 
		text = ""; 
		rough = false; 
		river = 0;
	} /* constructor */
} /* class */