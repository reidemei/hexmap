
                  ******************************************
                  ****     Hexfield Map Editor 0.9.5    ****
                  ****  (c) 1998-2001 Jan Reidemeister  ****
                  ****        E-mail: J.R.@gmx.de       ****
                  ****  WWW: http://JanR.home.pages.de  ****
                  ******************************************


******************                                                 ************
**** CONTENTS ********************************************************* 00 ****
******************                                                 ************

    Introduction ___________________________________________________ 01
    Features _______________________________________________________ 02
    Requirements ___________________________________________________ 03
    Usage __________________________________________________________ 04
    Copyright ______________________________________________________ 05
    Disclaimer _____________________________________________________ 06
    Getting The Program ____________________________________________ 07
    Contact ________________________________________________________ 08
    About The Author _______________________________________________ 09
    History, What's New, To Do, Known Problems _____________________ 10

**********************                                             ************
**** INTRODUCTION ***************************************************** 01 ****
**********************                                             ************

    HexMap is a editor for creating hexfield maps. It can be used to
    design maps for BattleTech [(c) FASA] or other tabletops with
    similar maps. Custom units can be used. Initially designed for
    BattleTech-PBEMs.

******************                                                 ************
**** FEATURES ********************************************************* 02 ****
******************                                                 ************

    * custom sized hexfield maps up to 50x50 fields
    * special images for ground levels from -3 to 4
    * other ground level with numbers
    * light and heavy woods, swamp
    * roads and rivers
    * light, medium, heavy and very heavy buildings
    * fire and smog
    * asphalt and rough terrain
    * flags and custom text for every hexfield
    * save/load maps 
    * export to GIF or PNG
    * custom units with colors and direction

    All the graphics can be changed, so that you can customize the maps to
    your needs.

**********************                                             ************
**** REQUIREMENTS ***************************************************** 03 ****
**********************                                             ************

    All you need is a Java 2 (aka 1.2/1.3) runtime environment. You 
    can get it free from 'http://java.sun.com'.
    But don't ask me about installing a JRE.

***************                                                    ************
**** USAGE ************************************************************ 04 ****
***************                                                    ************

      Installation:
    ++++++++++++++++++++++++++
    Simply unzip the hexmap-???.zip to a directory of your wish.

      Uninstall:
    ++++++++++++++++++++++++++
    Delete the directory, in which you unzipped the files. No other 
    files are created or modified.
    
      Using:
    ++++++++++++++++++++++++++
    On a Windows PC start the hexmap.bat. Notice, that depending on your 
    java version it can happen, that you have to change the CLASSPATH to
    start HexMap. If the JRE is not in your PATH, you have to edit the 
    batchfile and insert the path to the java.exe. On any other system 
    execute the java-interpreter in the directory you unziped the files 
    as follows (maybe you have to set the CLASSPATH):
    
    java jr.hexmap.HexMap
    
    Possible options are the look and feel for the swing-GUI.
    That works only if the nessessary classes are on your system.
    
    java jr.hexmap.HexMap {windows|metal|motif|mac}
    
    Please note, that some operations on a complete map (hex-numbers, 
    unit-names, map-loading) may take some time, depending on your 
    system it can be, the HexMap freezes for some seconds. Also the 
    export to GIF or PNG may take some time.

      Changing the graphics:
    ++++++++++++++++++++++++++
    If you want to change the graphicset, then edit the pictures in the
    images-directory. It would be nice, if you send me your pictures.

    img.gif    - all levelgraphics, specials, wood (the image between 
                 asphalt and fire must left free) 
    street.gif - the streets (remember, that they must fit together)
    river.gif  - the rivers (same as streets)
    build.gif  - the buildings (the last free is for ruins, I am 
                 looking for an image) 

    !! IMPORTANT !!    
    Remember on the 256-color limit of GIF. That means, that the colors in 
    !!ALL!! images together must be less then 256. Otherfalls the GIF-export will
    crash without any errormessage (Not my fault, it's the export-filter).
    Don't change the size of the pictures, that won't work.
    Also the background must be marked transparent.

      Adding/Changing units:
    ++++++++++++++++++++++++++
    The units can be configured through the "units.ini" found in the 
    units-directory. Look in there for the filestructure. The 
    unit-images must have the same size as the other pictures, with the 
    unit placed in the middle. If you want colorchange for your units,
    then the color to be changed must be white (RGB - 255,255,255).

*******************                                                ************
**** COPYRIGHT ******************************************************** 05 ****
*******************                                                ************

    The entire contents of this software package, excluding the Export
    Encoders, two utility-classes (FileFilter and RotateFilter) from 
    the Java-Tutorial (http://java.sun.com/docs/books/tutorial/) and 
    the graphic set is copyrighted by Jan Reidemeister.

    Most of the graphics supplied with HexMap are taken from Nico's 
    BattleTech PBEM. Have a look at:
        http://members.tripod.de/BT_PBEMnicoh/index.htm

    The GIF export encoder is from Acme (http://www.acme.com/java/).

    The PNG export encoder is from J. David Eisenberg (david@catcode.com, 
    http://www.keypoint.com/) under GNU Lesser General Public License
    (http://www.gnu.org/copyleft/lesser.html).

    You may not modify, disassemble or reverse engineer the program in
    any way. You are free to distribute the software (software distributors 
    are bound to send the notification) by electronic means and make as 
    many copies as you want on electronic or magnetic media, as long as 
    the files of this package remain unmodified, with copyright notices
    intact and no fee is charged beyond a reasonable amount for the media
    and handling.
    Selling for money is PROHIBITED without the author's prior permission.

    This software package is FREEWARE. You can use it as long and how 
    often you wish.

********************                                               ************
**** DISCLAIMER ******************************************************* 06 ****
********************                                               ************

    THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS WITHOUT WARRANTY OF ANY
    KIND. IN NO EVENT WILL JAN REIDEMEISTER ACCEPT RESPONSIBILITY FOR ANY
    DAMAGES, INCLUDING ANY LOST PROFITS, LOST SAVINGS OR OTHER INCIDENTAL,
    CONSEQUENTIAL, INDIRECT, SPECIAL OR SIMILAR DAMAGES ARISING OUT OF USE
    OR INABILITY TO USE THE SOFTWARE EVEN IF THE USER HAS BEEN ADVISED
    AND AUTHOR INFORMED OF THE POSSIBILITY OF SUCH DAMAGES.
    THE AUTHOR DOES NOT WARRANT THAT THE OPERATION OF THE SOFTWARE WILL BE
    UNINTERRUPTED. THE AUTHOR DOES NOT ASSUME ANY RESPONSIBILITY FOR ANY
    ERRORS WHICH MAY APPEAR IN THIS CODE NOR ANY RESPONSIBILITY TO UPDATE IT.

*****************************                                      ************
**** GETTING THE PROGRAM ********************************************** 07 ****
*****************************                                      ************

    You can download the program at:
    http://wwwiti.cs.uni-magdeburg.de/~reidemei/hexmap
    
*****************                                                  ************
**** CONTACT ********************************************************** 08 ****
*****************                                                  ************

    If you want to write the author, don't hestitate. You may send any
    questions, comments, improvements, suggestions, ideas, bug reports,
    complaints, problems, etc., etc. using these addresses.

      E-mail address:
    +++++++++++++++++++
    J.R.@gmx.de (will always forward to the right address)
    reidemei@iti.cs.uni-magdeburg.de

      WWW homepage:
    +++++++++++++++++++
    http://JanR.home.page.de (will always direct you to the right page)
    http://www-wi.cs.uni-magdeburg.de/~reidemei

      ICQ:
    ++++++++++++++++++
    12424536 (but at the moment I'm not so often online)

**************************                                         ************
**** ABOUT THE AUTHOR ************************************************* 10 ****
**************************                                         ************

    Born in 1976 and lives in Magdeburg (Germany).
    At the moment Student of Magdeburg University 
    (http://www.uni-magdeburg.de).

****************************************************               ************
**** HISTORY, WHAT'S NEW, TO DO, KNOWN PROBLEMS *********************** 11 ****
****************************************************               ************

      History:
    +++++++++++++++++++
          1997 -           started working on a editor in VisualBasic with
                           some stops and retries but never ended
       11/1998 -           started with a java-hexeditor
       12/1998 -           a working version, but less features
       03/1999 -           adding many features
    06/04/1999 - 0.9     - first public release
    05/10/1999 - 0.9.1   - some minor internal changes, now working with 1.1 JDK 
                           (1.1.7 tested)
    22/01/2000 - 0.9.2   - added swamp image, more levels and some units
    08/04/2000 - 0.9.2.1 - some more units
    21/01/2001 - 0.9.3   - some code rewriting, resizeable Window, 
                           more combinations of ground/wood/buildings/special items
                           custom sized maps, changed fileformat
                           I think, it wont work with JRE 1.1 any longer, but not tested.
    22/01/2001 - 0.9.4   - added an PNG-export
    27/01/2001 - 0.9.5   - different color for hexnumbers, improved calculation of the mouse position

      What's new:
    +++++++++++++++++++
    * You can now resize the window to see more of the map.
    * I changed the storage of the hexproperties. So now you can combine 
      any ground with wood, buildings and special items.
      Unfortunately this required a change in the savefile format. The new format is 
      textbased (you can have a look at it with an editor, but don't change anything) 
      and more flexible about future enhancements. A converter for your old maps 
      is included. In the converter-subdir execute:
        java jr.hexmap.Converter oldfile newfile
      where 'oldfile' is the old map file and 'newfile' the new one. If you have 
      backslashs ('\') in your file path, you have to escape them with a second 
      one ('\\').
      If you have problems converting your maps, you can email them to me and I 
      will try to convert them for you.
    * Create maps of any size up to 50x50. (Actually there is no limit in the 
      maximum size of the map, but a 50x50 map takes about 47 MB RAM on my computer.
      The big part is the uncompressed image in memory. And because 64 MB is the 
      normal limit for an java-application you have to increase the memorylimit of 
      the java interpreter to create bigger maps. Execute java.exe without any param 
      at a prompt and see if you can find the right switch for your version
      (1.2/1.3: '-Xmx??m' with ?? for the max size in MB).
    * PNG-Export
    * set the color for the hexnumbers
    * improved calculation of the mouse position
 
      To do:
    +++++++++++++++++++
    Create some nice graphicsets. If somebody will do that, you are welcome.
    Create more unit-pictures. 
    Custom fonts.

      Known problems:
    +++++++++++++++++++
    * Large maps may be to big for your computer. I think I catched that case 
      but if the programm crashes with large maps (up to 50x50 should work) 
      avoid them and create 2 or more smaller ones and put them together.
    * Sometimes some grapics are only blank and yellow. There must be an
      error when the icons are cropped from the big images, but I don't
      know why and how to fix it. 
      Workaround: Simply restart the application, it's only a graphic-problem,
      you can save your work.
    * Sometimes when you select something in the tree, not icon is displayed. 
      (I only had this under WindowsNT, on Linux that did not happend.)
      That can also happen, when you change the color or rotate a unit.
      Again I don't know how to fix it.
      Workaround: Select something else and then go back.
    * After loading a map or creating a new one the scrollbars don't reflect the 
      new size. You have to resize the app to force them to do so.

2001-01-27 Jan Reidemeister