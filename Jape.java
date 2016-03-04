/*
  Written 1999 by Douglas Greiman.
 
  This software may be used and distributed according to the terms
  of the GNU Public License, incorporated herein by reference.
*/

package duggelz.jape;

public class Jape 
{
    public static void main(String[] args)
    {
	try 
	{
	    int i = Long.decode(args[0]).intValue();
	    float f = Float.intBitsToFloat(i);
	    System.out.println("f is " + f);
	}
	catch( Exception e )
	{
	    System.err.println("Oops:");
	    e.printStackTrace(System.err);
	}
	
	System.out.println( "Done" );
    }
}

