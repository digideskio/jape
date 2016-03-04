/*
  Written 1999 by Douglas Greiman.
 
  This software may be used and distributed according to the terms
  of the GNU Public License, incorporated herein by reference.
*/

package duggelz.jape;

public interface Structure 
{
    public void chain(Structure nextStructure);

    public String get(String name);

    public int getInt(String name);

    public void set(String name, String value);

    public void setInt(String name, int value);
}

