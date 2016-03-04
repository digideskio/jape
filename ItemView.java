/*
  Written 1999 by Douglas Greiman.
 
  This software may be used and distributed according to the terms
  of the GNU Public License, incorporated herein by reference.
*/

package duggelz.jape;

import java.awt.*;
import java.awt.event.*;

public class ItemView extends Button
{
    // Static data
    public static final int MAX_LENGTH = 16;
    public static final String none    = "           None             ";
    public static final String unknown = "          Unknown           ";

    // Instance data
    public int index;
    public Item item;
    
    // Instance methods
    public ItemView(int index) 
    {
	super(none);
	this.index = index;
    }

    public int getIndex() { return this.index; }

    public void setActor(Mercenary merc) 
    {
	// Get the item from the merc
	if( merc != null ) 
	{
	    this.item = merc.items[this.index];
	} else {
	    this.item = null;
	}

	// Redisplay button
	this.refresh();
    }

    public void refresh()
    {
	// Get name of item exemplar
	String name;
	if( this.item != null )
	{
	    ItemExemplar exemplar = item.getExemplar();
	    if( exemplar != null ) 
	    {
		name = item.getExemplar().name;
		if( name.length() > MAX_LENGTH ) {
		    name = name.substring(0, MAX_LENGTH);
		}
	    } else {
		name = this.unknown;
	    }
	} else {
	    name = this.none;
	}

	// Set button label
	this.setLabel(name);
    }
}
