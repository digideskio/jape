/*
  Written 1999 by Douglas Greiman.
 
  This software may be used and distributed according to the terms
  of the GNU Public License, incorporated herein by reference.
*/

package duggelz.jape;

public interface Field
{
    public String get(byte[] data);
    public int getInt(byte[] data);
    public void set(byte[] data, String value);
    public void setInt(byte[] data, int value);
}
