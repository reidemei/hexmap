package jr.util;

import Acme.JPM.Filters.*;
import java.awt.image.*;

/**
 *  A filter to replace whit (255, 255, 255) with red.
 *
 *  @version 1.0
 *  @author Jan Reidemeister
 */ 
public class WhiteToRedFilter extends RGBBlockFilter {

	/**
	 *  Constructor
	 */
	public WhiteToRedFilter (ImageProducer producer)	{
		super( producer );
	} /* constructor */

	public int[][] filterRGBBlock (int x, int y, int width, int height, int[][] rgbPixels) {
		for (int row = 0; row < height; ++row)
			for ( int col = 0; col < width; ++col ) {
				if (rgbPixels[row][col] == 0xffffffff) {
					rgbPixels[row][col] = rgbPixels[row][col] & 0xffff0000;
				} /* if */
			} /* for */
		return rgbPixels;
	} /* filterRGBBlock */
} /* class */