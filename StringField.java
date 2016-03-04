/*
  Written 1999 by Douglas Greiman.
 
  This software may be used and distributed according to the terms
  of the GNU Public License, incorporated herein by reference.
*/

package duggelz.jape;

class StringField implements Field
{
    private int offset;
    private int length;

    public StringField(int offset, int length) 
    {
	this.offset = offset;
	this.length = length;
    }

    public String get(byte[] data)
    {
        StringBuffer sb = new StringBuffer();
	for( int i = 0; i < this.length/2; ++i )
	{
            byte b0 = data[this.offset + i*2];
	    byte b1 = data[this.offset + i*2 + 1];
	    char c = (char) ( (((int) b0) & 0xFF) |
			      ((((int) b1) & 0xFF) << 8) );
	    if ( c == 0 ) {
		break;
	    }
	    sb.append(c);
	}
        return sb.toString();
    }

    public int getInt(byte[] data) throws NumberFormatException {
	return Integer.parseInt(this.get(data));
    }

    public void set(byte[] data, String value)
    {
	for( int i = 0; i < this.length/2; ++i )
	{
	    char c = 0;
	    if ( i < value.length() ) {
		c = value.charAt(i);
	    }

	    data[this.offset + i*2] = (byte) (((int) c) & 0xFF);
	    data[this.offset + i*2 + 1] = (byte) ((((int) c) & 0xFF) >>> 8);
	}
    }

    public void setInt(byte[] data, int value)
    {
	this.set(data, Integer.toString(value));
    }
}
    
