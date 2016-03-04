/*
  Written 1999 by Douglas Greiman.
 
  This software may be used and distributed according to the terms
  of the GNU Public License, incorporated herein by reference.
*/

package duggelz.jape;

import java.util.Hashtable;

public abstract class BasicStructure implements Structure
{
    // Instance data
    public Hashtable fields;
    public byte[] data;
    public Structure nextStructure;

    // Instance methods
    public BasicStructure(Hashtable classFields) {
	this.fields = classFields;
    }

    public void chain(Structure nextStructure) {
	this.nextStructure = nextStructure;
    }

    public String get(String name) {
	Field field = (Field) fields.get(name);
	if( field == null ) {
	    if( this.nextStructure != null ) {
		return this.nextStructure.get(name);
	    } else {
		return null;
	    }
	}
	return field.get(this.data);
    }

    public int getInt(String name) {
	Field field = (Field) fields.get(name);
	if( field == null ) {
	    if( this.nextStructure != null ) {
		return this.nextStructure.getInt(name);
	    } else {
		return 0;
	    }
	}
	return field.getInt(this.data);
    }

    public void set(String name, String value) {
	Field field = (Field) fields.get(name);
	if( field != null ) {
	    field.set(this.data, value);
	}
	if( this.nextStructure != null ) {
	    this.nextStructure.set(name, value);
	}
    }

    public void setInt(String name, int value) {
	Field field = (Field) fields.get(name);
	if( field != null ) {
	    field.setInt(this.data, value);
	}
	if( this.nextStructure != null ) {
	    this.nextStructure.setInt(name, value);
	}
    }
}
