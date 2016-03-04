/*
  Written 1999 by Douglas Greiman.
 
  This software may be used and distributed according to the terms
  of the GNU Public License, incorporated herein by reference.
*/

package duggelz.jape;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

public class DataChangeMixin
{
    // Instance data
    private Vector listeners = new Vector(1);

    // Instance methods
    public synchronized void addDataChangeListener(DataChangeListener l) {
	if (l == null) {
	    return;
	}
	this.listeners.addElement(l);
    }

    public synchronized void removeDataChangeListener(DataChangeListener l) {
	if (l == null) {
	    return;
	}
	this.listeners.removeElement(l);
    }

    public void fireDataChangeEvent(DataChangeEvent event) {
	int size = this.listeners.size();
	for( int idx = 0; idx < size; ++idx ) {
	    ((DataChangeListener) this.listeners.elementAt(idx)).dataChanged(event);
	}
    }
}
