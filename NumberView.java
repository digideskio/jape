/*
  Written 1999 by Douglas Greiman.
 
  This software may be used and distributed according to the terms
  of the GNU Public License, incorporated herein by reference.
*/

package duggelz.jape;

import java.awt.*;
import java.awt.event.*;

public class NumberView extends TextField implements FieldView, TextListener
{
    private boolean suppressEvents = false;
    private String fieldName;
    private boolean modified = false;
    private String oldValue;
    private Structure struct; 
    
    public NumberView(String fieldName, int length)
    {
	super(length);
	this.fieldName = fieldName;
	this.addTextListener(this);
    }

    public String getFieldName() { return this.fieldName; }
    public String getValue() { return this.getText(); }
    public boolean isModified() { return this.modified; }
    public void setStruct(Structure struct) { this.struct = struct; }

    public void textValueChanged(TextEvent event)
    {
	// Ignore programmatic changes
	if( this.suppressEvents == true ) {
	    return;
	}

	// Get new value
	String newValue = getText();

	// Validate new value
	try {
	    if( newValue.equals("") ) {
		newValue = "0";
	    }
	    
	    // Attempt to save value
	    this.commit(newValue);

	    // Success. Save new value and notify listeners
	    this.oldValue = newValue;
	} catch( NumberFormatException xcptn) {
	    // Failure
	    this.setText(this.oldValue);
	}
    }	

    public void commit(String newValue) 
    {
	// Look for the all structs with named field
	if( this.struct != null )
	{
	    String oldValue = struct.get(this.fieldName);
	    if( (oldValue == null && (newValue != null)) || 
		(oldValue != null && (! oldValue.equals(newValue))) ) 
	    {
		this.modified = true;
		struct.set(this.fieldName, newValue);
		this.fireDataChangeEvent(new DataChangeEvent(this, this.oldValue, newValue));
	    }
	}
    }

    public synchronized void refresh() 
    {
	try {
	    this.suppressEvents = true;

	    // Look for the first struct with named field
	    String value = null;
	    if( this.struct != null ) 
	    {
		value = struct.get(this.fieldName);
	    }
	    
	    // Update gui
	    if( value != null ) {
		this.setText(value);
	    } else {
		this.setText("");
	    }
	    this.oldValue = value;
	    
	    // Update state machine
	    this.modified = false;
	} finally {
	    this.suppressEvents = false;
	}
    }

    public String toString() {
	String s = "NumberView[suppressEvents = " + suppressEvents + "," +
	    "fieldName = " + fieldName + "," + 
	    "modified = " + modified + "," + 
	    "oldValue = " + oldValue + "," + 
	    "struct = " + struct + "]";
	return s;
    }

    // Data Change event processing
    private DataChangeMixin mixin = new DataChangeMixin();

    public void addDataChangeListener(DataChangeListener l) {
	this.mixin.addDataChangeListener(l);
    }

    public void removeDataChangeListener(DataChangeListener l) {
	this.mixin.removeDataChangeListener(l);
    }

    public void fireDataChangeEvent(DataChangeEvent e) {
	this.mixin.fireDataChangeEvent(e);
    }
}
