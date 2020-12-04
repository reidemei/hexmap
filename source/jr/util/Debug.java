package net.reidemeister.util;

/**
 *  Prints out debug-messages.
 *
 *  @version $Id: Debug.java,v 1.0 1999/08/01 12:13:32 jan Exp $
 *  @author Jan Reidemeister
 */ 
public final class Debug {
	public static boolean debug = false;
	public static final void print (String msg) {
		if (debug)
			System.out.println ("DEBUG: " + msg);
	} /* print */
	public final static String version = "$Id: Debug.java,v 1.0 1999/08/01 12:13:32 jan Exp $";
} /* class */
