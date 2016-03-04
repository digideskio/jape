/*
  Written 1999 by Douglas Greiman.
 
  This software may be used and distributed according to the terms
  of the GNU Public License, incorporated herein by reference.
*/

package duggelz.jape;

class ByteField implements Field
{
    private int offset;

    public ByteField(int offset) 
    {
	this.offset = offset;
    }

    public String get(byte[] data)
    {
	return Integer.toString(this.getInt(data));
    }

    public int getInt(byte[] data)
    {
	int value = ((int) data[this.offset]) & 0xFF;
	return value;
    }

    public void set(byte[] data, String str) throws NumberFormatException
    {
	int value = Integer.parseInt(str);
	this.setInt(data, value);
    }

    public void setInt(byte[] data, int value)
    {
	data[this.offset] = (byte) value;
    }
}
    
