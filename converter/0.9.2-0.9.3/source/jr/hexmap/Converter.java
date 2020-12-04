package jr.hexmap;

import java.io.*;
import java.util.*;

public class Converter  {

	public final static int WOOD_NOTHING = 0;
	public final static int WOOD_SPARSE = 1;
	public final static int WOOD_DENSE = 2;
	public final static int GROUND_NOTHING = 0;
	public final static int GROUND_SWAMP = 1;
	public final static int GROUND_ASPHALT = 2;
	public final static int BUILDING_NOTHING = 0;
	public final static int BUILDING_RUINS = 1;
	public final static int BUILDING_LIGHT = 2;
	public final static int BUILDING_MEDIUM = 3;
	public final static int BUILDING_HEAVY = 4;
	public final static int BUILDING_VERYHEAVY = 5;
	public final static int SPECIAL_NOTHING = 0;
	public final static int SPECIAL_FIRE = 2;
	public final static int SPECIAL_SMOG = 3;

	public static void main (String[] args) {
		String inputFile = args[0];
		String outputFile = args[1];
		try {
			FileInputStream in = new FileInputStream (inputFile);
			ObjectInputStream ois = new ObjectInputStream (in);
			// first load the old map
			System.out.print ("Loading old map ");
			int xsize = ((Integer) ois.readObject ()).intValue ();
			System.out.print (".");
			int ysize = ((Integer) ois.readObject ()).intValue ();
			System.out.print (".");
			Hex[][] oldMap = (Hex[][]) ois.readObject ();
			System.out.print (".");
			in.close ();
			System.out.println (" Done.");
			System.out.print ("Converting ");
			FileWriter output = new FileWriter (outputFile);
			BufferedWriter out = new BufferedWriter (output);
			out.write ("HexMap" + System.getProperty ("line.separator"));
			System.out.print (".");
			out.write ("1.2" + System.getProperty ("line.separator"));
			System.out.print (".");
			out.write (xsize + System.getProperty ("line.separator"));
			System.out.print (".");
			out.write (ysize + System.getProperty ("line.separator"));
			System.out.print (".");
			// now create the new one
			for (int i = 1; i <= xsize; i++) {
				for (int j = 1; j <= ysize; j++) {
					Hex oldHex = oldMap [i][j];
					String s = i + "|" + j + "|";
					if (oldHex.mod ==5)
						s = s + BUILDING_RUINS + "|";
					else
						if (oldHex.mod ==6)
							s = s + BUILDING_LIGHT + "|";
						else
							if (oldHex.mod ==7)
								s = s + BUILDING_MEDIUM + "|";
							else	
								if (oldHex.mod ==8)
									s = s + BUILDING_HEAVY + "|";
								else	
									if (oldHex.mod ==5)
										s = s + BUILDING_VERYHEAVY + "|";
									else	
										s = s + BUILDING_NOTHING + "|";
					s = s + oldHex.color + "|";
					s = s + oldHex.direction + "|";
					s = s + oldHex.flag + "|";
					if (oldHex.mod ==3)
						s = s + GROUND_SWAMP + "|";
					else
						if (oldHex.mod ==4)
							s = s + GROUND_ASPHALT + "|";
						else
							s = s + GROUND_NOTHING + "|";
					s = s + oldHex.level + "|";
					s = s + oldHex.river + "|";
					s = s + ("" + oldHex.rough).toUpperCase () + "|";
					s = s + oldHex.special + "|";
					s = s + oldHex.street + "|";
					if (oldHex.text.length () == 0)
						s = s + " |";
					else	
						s = s + oldHex.text + "|";
					if (oldHex.unit.length () == 0)
						s = s + " |";
					else	
						s = s + oldHex.unit + "|";
					if (oldHex.mod ==1)
						s = s + WOOD_SPARSE + "|";
					else
						if (oldHex.mod ==2)
							s = s + WOOD_DENSE + "|";
						else
							s = s + WOOD_NOTHING + "|";
					out.write (s + System.getProperty ("line.separator"));
				} /* for */
				System.out.print (".");	
			} /* for */				
			out.flush ();
			out.close ();
		} catch (Exception e) {
			System.out.println (e);
			e.printStackTrace ();
		} /* catch */
	} /* main */
} /* class */