package jr.util;
 
import java.awt.*;
import java.awt.image.*;
import java.io.*;

/**
 *  A utilityclass for image manipulation.
 *
 *  @version 1.0
 *  @author Jan Reidemeister
 */ 
public class ImageFactory
		extends Component {
	
	/**
	 *  Constructor. Simply creates the MediaTracker.
	 */
	public ImageFactory () {
		
		Debug.print ("ImageFactory - constuctor");
		mt = new MediaTracker(this);
		
	} /* constructor */

	/**
	 *  Lädt ein Icon.
	 *
	 *  @param filename 	der Dateiname (Pfad relativ)
	 *
	 *  @return 			das Icon, wenn es geladen werden konnte
	 *          		<br>null, wenn nicht
	 *
	 */
	public Image getImageByFilename (String filename) {
		byte[] imgStream = null;
		try {
			Class c = getClass();
			ClassLoader cl = c.getClassLoader();
			java.net.URL u = cl.getResource (filename);
			if (u != null) {
				InputStream is = u.openStream();
				ByteArrayOutputStream bas = new ByteArrayOutputStream();
				int data;
				while ((data = is.read()) != -1) {
					bas.write (data);
				} /* while */
				imgStream = bas.toByteArray();
			} else {
				java.io.FileInputStream pf = new FileInputStream (filename);
				imgStream = new byte[pf.available()];
				pf.read (imgStream);
			} /* else */
		} catch (Exception e) {
			System.err.println ("error!! " + e);
		} /* catch */
		if (imgStream != null) {
			return (getToolkit ().createImage (imgStream));
		} else
			System.err.println ("Can't retrieve Icon");
		return null;
	} /* getImageByFilename */
		
	/**
	 *  Loads the image with the specified name.
	 *
	 *  @param file the name of the image
	 *  @return the image or null if not found or any other error
	 */
	public Image loadImage (String file) {

		Debug.print ("ImageFactory - loadImage - file: " + file);

		// if the file don't exist, return null
		java.io.File file1 = new java.io.File (file);
		if (!file1.exists ()) {
			Debug.print ("ImageFactory - loadImage - file not found");
			return (null);
		} /* if */
		
		Image a;
		
		try {
			// load the image
			a = this.getImageByFilename (file);
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
			Debug.print ("ImageFactory - loadImage - ERROR: can't load image; " + (mt.getErrorsID (0))[0].toString ());
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

		Debug.print ("ImageFactory - rotateImage - Image: " + i + ", angle: " + angle);
		
		
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
			Debug.print ("ImageFactory - rotateImage - ERROR: can't rotate image; " + (mt.getErrorsID (0))[0].toString ());
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
		
		Debug.print ("ImageFactory - colorImage - Image: " + i + ", color: " + color);
		
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
			Debug.print ("ImageFactory - colorImage - ERROR: can't color image; " + (mt.getErrorsID (0))[0].toString ());
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

		Debug.print ("ImageFactory - cropImage - Image: " + i + ", x: " + x + ", y: " + y + ", h: " + h + ", w: " + w);		
		
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
			Debug.print ("ImageFactory - cropImage - ERROR: can't crop image; " + (mt.getErrorsID (0))[0].toString ());
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