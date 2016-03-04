/*
  Written 1999 by Douglas Greiman.
 
  This software may be used and distributed according to the terms
  of the GNU Public License, incorporated herein by reference.
*/

package duggelz.jape;

public class JapeAlg
{
    public static byte[] Encode(
	byte[] plaintext, 
	int textLength, 
	int[] table )
    {
	byte[] ciphertext = new byte[textLength];
	int i = 0;
	byte lastchar = 0;
	int offset = 0;

	for( i = 0; i < textLength; ++i )
	    {
		byte salt = (byte) table[offset];
		ciphertext[i] = (byte) (plaintext[i] + lastchar + salt);
		offset = (offset + 1) % 49;
		lastchar = ciphertext[i];
	    }
	return ciphertext;
    }

    public static byte[] Decode(
	byte[] ciphertext, 
	int textLength, 
	int[] table)
    {
	byte[] plaintext = new byte[textLength];
	int i = 0;
	byte lastchar = 0;
	int offset = 0;

	for( i = 0; i < textLength; ++i )
	    {
		byte salt = (byte) table[offset];
		plaintext[i] = (byte) (ciphertext[i] - lastchar - salt);
		offset = (offset + 1) % 49;
		lastchar = ciphertext[i];
	    }
	return plaintext;
    }

    private static int sx(byte b)
    {
	return (int) b;
    }

    private static int u(byte b)
    {
	return ((int) b) & 0xFF;
    }

    private static int word_ptr(
	byte[] data,
	int offset )
    {
	byte b0 = data[offset];
	byte b1 = data[offset+1];
	return (int) (((((int) b0) & 0xFF) | ((((int) b1) & 0xFF) << 8)) & 0xFFFF);
    }

//  public static int mul(lhs, rhs):
//      l = (long(lhs) * rhs) & 0x00FFFFFFFFL
//      if l > 0x7FFFFFFF:
//          l = l - 0x100000000L
//      i = int(l)
//      #print "%16x %8x" % (l, i)
//      return i    

    public static int ActorChecksum(
	byte[] data)
    {
	int eax;
	int edx;
	int edi;
	int esi;
	
	eax = sx(data[0x129]);
	edx = sx(data[0x14e]);
	eax = eax + 1;
	edx = edx + 2;
	eax = eax * edx;
	edx = sx(data[0x195]);
	edi = 0x1a0;
	eax = eax + edx + 1;
	edx = sx(data[0x14f]);
	edx = edx + 1;
	eax = eax * edx;
	edx = sx(data[0x128]);
	eax = eax + edx + 1;
	edx = sx(data[0x161]);
	edx = edx + 1;
	eax = eax * edx;
	edx = sx(data[0x105]);
	eax = eax + edx + 1;
	edx = sx(data[0x19b]);
	edx = edx + 1;
	eax = eax * edx;
	edx = sx(data[0x153]);
	esi = eax + edx + 1;
	eax = sx(data[0x160]);
	eax = eax + 1;
	esi = esi * eax;
	edx = 0;
    
	do
	{
	    int ebx = 0;
	    eax = 0;
	    ebx = word_ptr(data,edi);
	    eax = u(data[0x179 + edx]);
	    ebx = ebx + esi;
	    edx = edx + 1;
	    edi = edi + 2;
	    esi = ebx + eax;
	} while( edx < 0x13);

	eax = esi;
	return eax;
    }

    public static int MercChecksum(
	byte[] data)
    {
	int eax;
	int edx;
	int esi;
	int edi;

	esi = 0x13;
	eax = sx(data[0x395]);
	edx = sx(data[0x364]);
	eax = eax + 1;
	edx = edx + 2;
	eax = eax * edx;

	edx = sx(data[0x370]);
	eax = eax + edx + 1;
	edx = sx(data[0x348]);
	edx = edx + 1;
	eax = eax * edx;

	edx = sx(data[0x376]);
	eax = eax + edx + 1;
	edx = sx(data[0x561]);
	edx = edx + 1;
	eax = eax * edx;

	edx = sx(data[0x55c]);
	eax = eax + edx + 1;
	edx = sx(data[0x394]);
	edx = edx + 1;
	eax = eax * edx;

	edx = sx(data[0x562]);
	eax = eax + edx + 1;
	edx = sx(data[0x351]);
	edx = edx + 1;
	eax = eax * edx;

	edx = 0;
	edx = u(data[0x721]);
	int offset = 0xC;
	eax = eax + edx + 1;
    
	do {
	    edi = 0;
	    edx = 0;
	    edi = word_ptr(data,offset);
	    edx = u(data[offset+0x2]);
	    edi = edi + eax;
	    offset = offset + 0x24;
	    esi = esi - 1;
	    eax = edi + edx;
	} while( esi != 0);

	return eax;
    }
}
