/*
  Written 1999 by Douglas Greiman.
 
  This software may be used and distributed according to the terms
  of the GNU Public License, incorporated herein by reference.
*/

package duggelz.jape;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

public class DataChangeEvent extends AWTEvent
{
    // Instance data
    private Object oldValue;
    private Object newValue;

    // Instance methods
    public DataChangeEvent(Object source, Object oldValue, Object newValue)
    {
	super(source, RESERVED_ID_MAX+1);
	this.oldValue = oldValue;
	this.newValue = newValue;
    }

    public String getOldValue() { return (String) this.oldValue; }
    public String getNewValue() { return (String) this.newValue; }
}				     
