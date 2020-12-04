
                  ******************************************
                  ****      Hexfield Map Editor 0.9     ****
                  ****  (c) 1998-1999 Jan Reidemeister  ****
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

    * hexfield maps from 10x10 up to 32x32 fields
    * ground levels from -3 to 4 (can be more in a future release if you wish)
    * light and heavy woods
    * roads and rivers
    * light, medium, heavy and very heavy buildings
    * fire and smog
    * asphalt and rough terrain
    * flags and custom text for every hexfield
    * save maps and export to GIF
    * custom units, that can be colored and rotated

    All the graphics can be changed, so that you can customize the maps to
    your needs.

**********************                                             ************
**** REQUIREMENTS ***************************************************** 03 ****
**********************                                             ************

    All you need is a JDK 1.2 runtime environment. You can get it from
    http://java.sun.com
    But don't ask me about installing a JDK.

***************                                                    ************
**** USAGE ************************************************************ 04 ****
***************                                                    ************

      Installation:
    ++++++++++++++++++++++++++
    Simply unzip the hexmap.zip to a directory of your wish.

      Uninstall:
    ++++++++++++++++++++++++++
    Delete the directory, in which you unzipped the files. No other 
    files are created or modified.
    
      Using:
    ++++++++++++++++++++++++++
    On a WindowsPC start the HexMap.bat. If the JDK is not in your PATH, 
    you have to edit the batchfile and insert the path to the java.exe.
    On any other system execute the java-interpreter in the directory you 
    unziped the files as follows:
    
    java jr.hexmap.HexMap
    
    Possible options are the look and feel for the swing-GUI.
    That works only if the nessessary classes are on your system.
    
    java.exe jr.hexmap.HexMap {windows|metal|motif|mac}
    
      Changing the graphics:
    ++++++++++++++++++++++++++
    If you want to change the graphicset, then edit the pictures in the
    images-directory.
    img.gif    - all levelgraphics, specials, wood (the image between 
                 asphalt and fire must left free; the last free is for
                 swamp in a future release) 
    street.gif - the streets (remember, that they must fit together)
    river.gif  - the rivers (same as streets)
    build.gif  - the buildings (the last free is for ruins in a future 
                 release) 
    !! IMPORTANT !!    
    Remember on the 256-color limit of GIF. That means, that the colors in 
    ALL images together must be less then 256. Otherfalls the GIF-export will
    crash without any errormessage (Not my fault, it's the export-filter).
    Dont change the size of the pictures, that won't work.
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

    The entire contents of this software package, excluding the Acme
    (http://www.acme.com/java/) files, two utility-classes (FileFilter
    and RotateFilter) from the Java-Tutorial 
    (http://java.sun.com/docs/books/tutorial/) and the graphics is
    copyrighted by Jan Reidemeister.

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

    You can download the programm at:
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
    http://wwwiti.cs.uni-magdeburg.de/~reidemei

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
          1997 -        started working on a editor in VisualBasic with
                        some stops and retries but never ended
       11/1998 -        started with a java-hexeditor
       12/1998 -        a working version, but less features
       03/1999 -        adding many features
    06/04/1999 - 0.9  - first public release

      What's new:
    +++++++++++++++++++
    everything ;-)
 
      To do:
    +++++++++++++++++++
    Create some nice graphicsets. If somebody will do that, you are willcome.
    Create more unit-pictures.

      Known problems:
    +++++++++++++++++++
    * If you dont have enough memory free, than it can be, that the 
      programmm crashes with larger maps.
      Workaround: Create smaller maps and put them together.
    * Sometimes some grapics are only blank and yellow. There must be an
      error when the icons are cropped from the big images, but I don't
      know why and how to fix it. 
      Workaround: Simply restart the application, it's only a graphic-problem,
      you can save your work.
    * Sometimes when you select something in the tree, not icon is displayed. 
      That can also happen, when you change the color or rotate a unit.
      Again I don't know how to fix it.
      Workaround: Select something else and then go back.
      

05.04.1999 Jan Reidemeister