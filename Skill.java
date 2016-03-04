/*
  Written 1999 by Douglas Greiman.
 
  This software may be used and distributed according to the terms
  of the GNU Public License, incorporated herein by reference.
*/

package duggelz.jape;

import java.util.*;

public class Skill {
    public static final int SKILL_NONE         =   0;
    public static final int SKILL_LOCKPICKING  =   1;
    public static final int SKILL_HAND_TO_HAND =   2;
    public static final int SKILL_ELECTRONICS  =   3;
    public static final int SKILL_NIGHT_OPS    =   4;
    public static final int SKILL_THROWING     =   5;
    public static final int SKILL_TEACHING     =   6;
    public static final int SKILL_HEAVY_WEAPONS=   7;
    public static final int SKILL_AUTO_WEAPONS =   8;
    public static final int SKILL_STEALTHY     =   9;
    public static final int SKILL_AMIDEXTROUS  = 0xA;
    public static final int SKILL_THIEF        = 0xB;
    public static final int SKILL_MARTIAL_ARTS = 0xC;
    public static final int SKILL_KNIFING      = 0xD;
    public static final int SKILL_ROOF_BONUS   = 0xE;
    public static final int SKILL_CAMOUFLAGED  = 0xF;

    public static final Vector list = new Vector();
    static {
	String[] temp = {
	    "None",
	    "Lockpicking",
	    "Hand to Hand",
	    "Electronics",
	    "Night Ops",
	    "Throwing",
	    "Teaching",
	    "Heavy Weapons",
	    "Auto Weapons",
	    "Stealthy",
	    "Ambidextrous",
	    "Thief",
	    "Martial Arts",
	    "Knifing",
	    "Roof Bonus",
	    "Camouflaged",
	};
	for( int idx = 0; idx < temp.length; ++idx ) {
	    list.addElement(temp[idx]);
	}
    };

    public static Hashtable table = new Hashtable();
    static {
	for( int idx = 0; idx < list.size(); ++idx )
	{
	    table.put(list.elementAt(idx), new Integer(idx));
	    table.put(new Integer(idx), list.elementAt(idx));
	}
    }
}
