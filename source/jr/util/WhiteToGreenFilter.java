package jr.util;

import Acme.JPM.Filters.*;
import java.awt.image.*;

/**
 *  A filter to replace white (255, 255, 255) with green.
 *
 *  @version 1.0
 *  @author Jan Reidemeister
 */ 
public class WhiteToGreenFilter extends RGBBlockFilter {

	/**
	 *  Constructor
	 */
	public WhiteToGreenFilter (ImageProducer producer)	{
		super( producer );
	} /* constructor */

	public int[][] filterRGBBlock (int x, int y, int width, int height, int[][] rgbPixels) {
		for (int row = 0; row < height; ++row)
			for ( int col = 0; col < width; ++col ) {
				if (rgbPixels[row][col] == 0xffffffff) {
					rgbPixels[row][col] = rgbPixels[row][col] & 0xff00ff00;
				} /* if */
			} /* for */
		return rgbPixels;
	} /* filterRGBBlock */
} /* class */