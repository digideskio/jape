/*
  Written 1999 by Douglas Greiman.
 
  This software may be used and distributed according to the terms
  of the GNU Public License, incorporated herein by reference.
*/

package duggelz.jape;

import java.util.*;

public class ChoiceField implements Field
{
    private Field baseField;
    private String[] list;
    private Hashtable table;
    
    public ChoiceField(Field baseField, Hashtable table) 
    {
	this.baseField = baseField;
	this.list = list;
	this.table = table;
    }

    public String get(byte[] data)
    {
	int value = this.baseField.getInt(data);
	String str = (String) this.table.get(new Integer(value));
	return str;
    }

    public int getInt(byte[] data)
    {
	int value = this.baseField.getInt(data);
	return value;
    }

    public void set(byte[] data, String str) throws NumberFormatException
    {
	Integer val = (Integer) this.table.get(str);
	if( val == null ) {
	    return;
	}
	int value = val.intValue();
	this.setInt(data, value);
    }

    public void setInt(byte[] data, int value) throws NumberFormatException
    {
	this.baseField.setInt(data, value);
    }
}
    
