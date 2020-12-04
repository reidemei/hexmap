package net.reidemeister.util;

import Acme.JPM.Filters.*;
import java.awt.image.*;

/**
 *  A filter to replace white (255, 255, 255) with blue.
 *
 *  @version 1.0
 *  @author Jan Reidemeister
 */ 
public class WhiteToBlueFilter extends RGBBlockFilter {

	/**
	 *  Constructor
	 */
	public WhiteToBlueFilter (ImageProducer producer)	{
		super( producer );
	} /* constructor */

	public int[][] filterRGBBlock (int x, int y, int width, int height, int[][] rgbPixels) {
		for (int row = 0; row < height; ++row)
			for ( int col = 0; col < width; ++col ) {
				if (rgbPixels[row][col] == 0xffffffff) {
					rgbPixels[row][col] = rgbPixels[row][col] & 0xff0000ff;
				} /* if */
			} /* for */
		return rgbPixels;
	} /* filterRGBBlock */
		
} /* class */