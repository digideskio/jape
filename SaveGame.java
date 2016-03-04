/*
  Written 1999 by Douglas Greiman.
 
  This software may be used and distributed according to the terms
  of the GNU Public License, incorporated herein by reference.
*/

package duggelz.jape;

import java.io.RandomAccessFile;
import java.io.IOException;

public class SaveGame
{
    public class FormatException extends Exception
    {
    }

    // Empirical constants
    public int thingCountOffset = 0x32f;
    public int thingCount = 0;
    public int thingOffset = 0x333;
    public int thingDataLength = 0x1C;
    
    public int otherThingDataLength = 0x1d10;
    
    public int actorCount = 0xAA;
    public int actorDataLength = 0x2CC;
    public int actorOffset = 0;
    public Actor[] actors = new Actor[actorCount];
    
    public int mercCount = 0;
    public int mercMaxCount = 18;
    public int mercDataLength = 1 + 0x918 + 4 + 1 + 0x80;
    public int mercOffset = 0;
    public Mercenary[] mercs = new Mercenary[mercMaxCount];

    public String filename;
    public RandomAccessFile file;

    public int codeTableIdx;
    public int codeTableSubIdx;
    public int[] codeTable;

    public void load(String filename) throws IOException, FormatException
    {
        // Open game game
        this.filename = filename;
        this.file = new RandomAccessFile(this.filename,"r");

	try {
	    // Determine file offsets
	    this.file.seek(this.thingCountOffset);
	    this.thingCount = (int) (((int) this.file.readByte()) & 0xFF);
	    this.actorOffset = ( this.thingOffset +
				 this.thingCount * this.thingDataLength +
				 this.otherThingDataLength );
	    this.mercOffset = this.actorOffset + this.actorDataLength * this.actorCount;
	    //print hex(this.actorOffset)
	    //print hex(this.mercOffset)
	    
	    // Decide which coding table was used
	    this.file.seek(this.actorOffset);
	    byte[] rawData = new byte[16];
	    this.file.readFully(rawData);
	    int[] indexes = this.findActorCodeTable(rawData);
	    if ( indexes == null) {
		//System.err.println("ERROR: Unable to determine code table!");
		throw new FormatException();
	    }
	    this.codeTableIdx = indexes[0];
	    this.codeTableSubIdx = indexes[1];
	    this.codeTable = JapeConst.CodeTables[this.codeTableIdx][this.codeTableSubIdx];
	    
	    // Load data
	    this.loadActors();
	    this.loadMercs();
	}
	finally
	{
	    // Close game file
	    this.file.close();
	}
    }
    
    // Read all Actor data
    public void loadActors() throws IOException
    {
        this.file.seek(this.actorOffset);
	for( int idx = 0; idx < this.actorCount; ++idx )
	{
	    // Read actor data
            byte[] actorData = new byte[this.actorDataLength];
	    this.file.readFully(actorData);

	    // Create new actor
            Actor actor = new Actor(actorData, this.codeTable);
            this.actors[idx] = actor;
        }
    }

    // Read all Mercenary data
    public void loadMercs() throws IOException
    {
        this.file.seek(this.mercOffset);
	for( int mercIdx = 0; mercIdx < 18; ++mercIdx )
	{
	    // Do some kind of adjustment discovered by trial and error
	    int adjustIdx;
	    for( adjustIdx = 0; adjustIdx <16; ++adjustIdx )
	    {
		// Did we find the "header"
		if( file.readByte() == 1 )
		{
		    // Ok, back up one byte
		    file.seek(file.getFilePointer()-1);
		    break;
		}

		//System.err.println("Adjusting...");
	    }

	    // Did we find the "header"
	    if( adjustIdx == 16 ) 
	    {
		// Assume we've read all the mercs
		//System.err.println("Confused by save game format, giving up.");
		break;
	    }

            // Read possible mercenary data
	    byte[] mercData = new byte[this.mercDataLength];
	    this.file.readFully(mercData);

            // Create merc
            Mercenary merc = new Mercenary(mercData, this.codeTable);
            this.mercs[mercCount] = merc;
            this.mercCount = this.mercCount + 1;
	    merc.adjustCount = adjustIdx;

	    // Print debugging info
//  	    System.err.println();
//  	    System.err.print("Nick =");
//  	    for( int i=0; i<16; i+=2 ) {
//  		System.err.print(" " + (char) merc.data[i+0x2da] );
//  	    }
//  	    System.err.println();

//  	    System.err.print("Data =");
//  	    for( int i=0; i<16; ++i ) {
//  		System.err.print(" " + Integer.toHexString((merc.data[i]+256)%256) );
//  	    }
//  	    System.err.println();

//  	    System.err.print("Merc =");
//  	    for( int i=0; i<16; ++i ) {
//  		System.err.print(" " + Integer.toHexString((mercData[i]+256)%256) );
//  	    }
//  	    System.err.println();
//  	    System.err.print("Last =");
//  	    for( int offset = 0x919; offset < 0x919 + 4 + 1 + 0x80; ++offset)
//  	    {
//  		if ( (offset - 0x919) % 16 == 0 )
//  		{
//  		    System.err.println();
//  		    System.err.print("      ");
//  		}
//  		byte datum = mercData[offset];
//  		if( datum < 16 && datum >= 0 ) {
//  		    System.err.print(" 0" + Integer.toHexString((datum+256)%256) );
//  		} else {
//  		    System.err.print(" " + Integer.toHexString((datum+256)%256) );
//  		}
//  	    }
//  	    System.err.println();

        }
    }

    public void save() throws IOException 
    {
        // Open game game
        this.file = new RandomAccessFile(this.filename,"rw");

	try {
	    // Save data
	    this.saveActors();
	    this.saveMercs();
	}
	finally
	{
	    // Close game file
	    this.file.close();
	}
    }

    // Write all Actor data
    public void saveActors() throws IOException
    {
	// Iterate over actors
        this.file.seek(this.actorOffset);
	for( int idx = 0; idx < this.actorCount; ++idx )
	{
            Actor actor = this.actors[idx];
            byte[] actorData = actor.encode(this.codeTable);
            this.file.write(actorData);
        }
    }

    // Write Merc data
    public void saveMercs() throws IOException
    {
	// Iterate over mercenaries
        this.file.seek(this.mercOffset);
	for( int idx = 0; idx < this.mercCount; ++idx )
	{
            Mercenary merc = this.mercs[idx];
            byte[] mercData = merc.encode(this.codeTable);

	    // Do the mysterious "adjustment" 
	    for( int i = 0; i < merc.adjustCount; ++i )
	    {
		this.file.writeByte(0);
	    }

	    // Now write the encoded merc data
            this.file.write(mercData);
        }
    }

    public int[] findActorCodeTable(byte[] ciphertext) 
    {
        // Brute force search through every coding table
  	for( int mainTable = 0; mainTable < 4; ++mainTable )
  	{
  	    for( int subTable = 0; subTable < 57; ++subTable )
  	    {
                // Try this decoding
//  		for( int i = 0; i < ciphertext.length; ++i )
//  		{
//  		    System.out.print("" + (int) ciphertext[i] + " ");
//  		}
//  		System.out.println();
                byte[] plaintext = JapeAlg.Decode(ciphertext, 8, JapeConst.CodeTables[mainTable][subTable]);
//  		for( int i = 0; i < plaintext.length; ++i )
//  		{
//  		    System.out.print("" + (int) plaintext[i] + " ");
//  		}
//  		System.out.println();

                // Now use our knowledge of the data structure to guess
                // whether the plaintext is correct
                if ( (plaintext[1] == 0) &&
		     (plaintext[3] == 0) &&
                     (plaintext[5] == 0) &&
		     (plaintext[7] == 0) ) 
		{
                    // We have a winner
		    int[] results = { mainTable, subTable };
                    return results;
		}

	    }
	}
	return null;
    }

    public Actor getActor(int idx) {
        return this.actors[idx];
    }

    public Mercenary getMerc(int idx) {
        return this.mercs[idx];
    }

    public Actor getActorByNick(String nick) 
    {
	for( int idx = 0; idx < this.actorCount; ++idx )
	{
	    Actor actor = this.actors[idx];
	    if ( (actor != null) && (actor.get("Nickname").equals(nick)) )
	    {
                return actor;
	    }
	}
        return null;
    }

    public Mercenary getMercByNick(String nick) 
    {
	for( int idx = 0; idx < this.mercCount; ++idx )
	{
	    Mercenary merc = this.mercs[idx];
	    if ( (merc != null) && (merc.get("Nickname").equals(nick)) )
	    {
                return merc;
	    }
	}
        return null;
    }
}
