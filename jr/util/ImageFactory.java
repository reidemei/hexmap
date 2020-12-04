package jr.util;
 
import java.awt.*;
import java.awt.image.*;

/**
 *  A utilityclass for image manipulation.
 *
 *  @version 1.0
 *  @author Jan Reidemeister
 */ 
public class ImageFactory
		extends Component 
		implements Debug {
	
	/**
	 *  Constructor. Simply creates the MediaTracker.
	 */
	public ImageFactory () {
		
		if (DEBUG) System.err.println ("DEBUG: ImageFactory: Constuctor");
		mt = new MediaTracker(this);
		
	} /* constructor */
		
	/**
	 *  Loads the image with the specified name.
	 *
	 *  @param file the name of the image
	 *  @return the image or null if not found or any other error
	 */
	public Image loadImage (String file) {
		
		// if the file don't exist, retun null
		java.io.File file1 = new java.io.File (file);
		if (!file1.exists ())
			return (null);
		
		Image a;
		
		try {
			// load the image
			if (DEBUG) System.err.println ("DEBUG: ImageFactory: loading image \"" + file + "\"");
			a = getToolkit ().getImage (ClassLoader.getSystemResource (file));
			mt.addImage (a, 0);
		} catch (Exception e) { 
			return (null);
		} /* catch */
			
		// wait for the image
		try {
			mt.waitForAll ();		
		} catch (InterruptedException e) {
			return (null);
		} /* catch */
		
		// look for errors
		if (mt.isErrorAny ()) {
			if (DEBUG) System.err.println ("DEBUG: ImageFactory: can't load image!" + (mt.getErrorsID (0))[0].toString ());
			mt.removeImage (a, 0);
			return (null);
		} else {
			mt.removeImage (a, 0);
			return (a);
		}/* else */
			
	} /* loadImage */
	
	/**
	 *  Rotates an image.
	 *
	 *  @param i the sourceimage
	 *  @param angle angle to rotate in degrees
	 *  @return the rotated image or null if there is any error
	 */
	public Image rotateImage (Image i, double angle) {
		
		ImageProducer producer = new FilteredImageSource (i.getSource (), new RotateFilter (-angle * (Math.PI / 180)));
		Image a = createImage (producer);
		mt.addImage (a, 0);
		
		// wait for the image
		try {
			mt.waitForAll ();		
		} catch (InterruptedException e) {
			return (null);
		} /* catch */
			
		// look for errors	
		if (mt.isErrorAny ()) {
			if (DEBUG) System.err.println ("DEBUG: ImageFactory: error on rotating image!" + (mt.getErrorsID (0))[0].toString ());
			mt.removeImage (a, 0);
			return (null);
		} else {
			mt.removeImage (a, 0);
			return (a);
		}/* else */
			
	} /* rotateImage */   

	/**
	 *  Colors an image. It only replaces the color white (255, 255, 255) with a specific color.
	 *
	 *  @param i sourceimage
	 *  @param color the wished color (0 - white, 1 - yellow, 2 - Green, 3 - Blue, 4 - Red)
	 *  @return the colored image or null if there is any error
	 */
	public Image colorImage (Image i, int color) {
		
		// do nothing, if white is wished
		if (color == 0) 
			return (i);
		
		ImageFilter filter = null;
		switch (color) {
			case 1: filter = new WhiteToYellowFilter (i.getSource ());
					break;
			case 2: filter = new WhiteToGreenFilter (i.getSource ());
					break;
			case 3: filter = new WhiteToBlueFilter (i.getSource ());
					break;
			case 4: filter = new WhiteToRedFilter (i.getSource ());
					break;
		} /* switch */
		ImageProducer producer = new FilteredImageSource (i.getSource (), filter);
		Image a = createImage (producer);
		mt.addImage (a, 0);
		
		// wait for the image
		try {
			mt.waitForAll ();		
		} catch (InterruptedException e) {
			return (null);
		} /* catch */
			
		// look for errors
		if (mt.isErrorAny ()) {
			if (DEBUG) System.err.println ("DEBUG: ImageFactory: error on coloring image!" + (mt.getErrorsAny ())[0].toString ());
			mt.removeImage (a, 0);
			return (null);
		} else {
			mt.removeImage (a, 0);
			return (a);
		}/* else */
			
	} /* colorImage */
	
	/**
	 *  Crops a region from an image.
	 *
	 *  @param i the sourceimage
	 *  @param x the x-statingpoint
	 *  @param y the y-startingpoint
	 *  @param h the height
	 *  @param w the width
	 *  @return the cropped region or null if there is any error
	 */
	public Image cropImage (Image i, int x, int y, int h, int w) {
		
		ImageProducer producer = new FilteredImageSource (i.getSource (), new java.awt.image.CropImageFilter (x, y, h, w));
		Image a = createImage (producer);
		mt.addImage (a, 0);
		
		// wait for the image
		try {
			mt.waitForAll ();		
		} catch (InterruptedException e) {
			return (null);
		} /* catch */
			
		// lok for errors
		if (mt.isErrorAny ()) {
			if (DEBUG) System.err.println ("DEBUG: ImageFactory: error on cropping image!" + (mt.getErrorsID (0))[0].toString ());
			mt.removeImage (a, 0);
			return (null);
		} else {
			mt.removeImage (a, 0);
			return (a);
		}/* else */
			
	} /* cropImage */   

	/**
	 *  Rotates and colors an image. If the image is rotated, it is cropped back to ist sourcedimension.
	 *
	 *  @param i the sourceimage
	 *  @param angle angle to rotate in degrees
	 *  @param color the wished color (0 - white, 1 - yellow, 2 - Green, 3 - Blue, 4 - Red)
	 *  @return the manipulated image or null if there is any error
	 */
	public Image rotateAndColorImage (Image i, double angle, int color) {
		
		Image a = rotateImage (i, angle);
		if (a != null) {
			a = colorImage (a, color);
			if (a != null) {
				if ((angle == 0) || (angle == (3 * 60)))
					return (a);		
				else
					return (cropImage (a, 9, 13, 69, 60));
			} /* if */
		} /* if */
		
		return (null);
		
	} /* rotateAndColorImage */

	/**
	 *  Scales an image.
	 *
	 *  @param i the sourceimage
	 *  @param h the wished height
	 *  @param w the wished width
	 *  @return the scaled image or null if there is any error
	 */
	public Image scaleImage (Image i, int h, int w) {
		
		return (i.getScaledInstance (h, w, Image.SCALE_SMOOTH));
		
	} /* scaleImage */
	
	MediaTracker mt;

} /* class */