/*
  Written 1999 by Douglas Greiman.
 
  This software may be used and distributed according to the terms
  of the GNU Public License, incorporated herein by reference.
*/

package duggelz.jape;

import java.io.RandomAccessFile;
import java.io.IOException;
import java.io.ByteArrayOutputStream;

public class SaveGame
{
    public class FormatException extends Exception
    {
	public FormatException(String message) {
	    super(message);
	}
    }

    public int uiSavedGameVersionOffset = 0;
    public int uiSavedGameVersionLength = 4;

    public int uiDayOffset = 280;
    public int uiDayLength = 4;
    public int ubHourOffset = 284;
    public int ubHourLength = 1;
    public int ubMinOffset = 285;
    public int ubMinLength = 1;
    public int iCurrentBalanceOffset = 292;
    public int iCurrentBalanceLength = 4;

    // Empirical constants
    public int thingCountOffset = 0x32f;  // "thing" == STRATEGICEVENT
    public int thingCount = 0;
    public int thingOffset = 0x333;
    public int thingDataLength = 0x1C;
    
    public int otherThingDataLength = 0x1d10;
    
    // == MERCPROFILESTRUCT
    public int actorCount = 0xAA;
    public int actorDataLength = 0x2CC;
    public int actorOffset = 0;
    public Actor[] actors = new Actor[actorCount];
    
    // == SOLDIERTYPE
    public int mercCount = 0;
    public int TOTAL_SOLDIERS = 156;
    public int SOLDIERCREATE_STRUCT__length = 0x918;
    public int mercOffset = 0;
    public Mercenary[] mercs = new Mercenary[this.TOTAL_SOLDIERS];

    // == struct path, PathSt
    public int PathSt__length = 20;

    // == KEYS
    public int NUM_KEYS = 64;
    public int KEY_ON_RING__length = 2;

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
	    //System.err.println(this.actorOffset);
	    //System.err.println(this.mercOffset);
	    
	    // Decide which coding table was used
	    this.file.seek(this.actorOffset);
	    byte[] rawData = new byte[16];
	    this.file.readFully(rawData);
	    int[] indexes = this.findActorCodeTable(rawData);
	    if ( indexes == null) {
		//System.err.println("ERROR: Unable to determine code table!");
		throw new FormatException("Unable to determine code table");
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
    public void loadMercs() throws IOException, FormatException
    {
        this.file.seek(this.mercOffset);

	// Read the initial run of inactive soldiers (may be none)
	int mercIdx = 0;
	while (true) {
	    byte initialActive = file.readByte();
	    // This entry is active?
	    if( initialActive != 0 ) {
		// Ok, back up one byte
		file.seek(file.getFilePointer()-1);
		break;
	    }
	    mercIdx++;
	    this.mercOffset++;
	    //System.out.println("Read inactive entry");
	}
	    
	// Read active soldiers
	for( ; mercIdx < this.TOTAL_SOLDIERS; ++mercIdx )
	{
	    ByteArrayOutputStream mercData = new ByteArrayOutputStream();

	    // Read boolean "is soldier present" byte.  We assume that
	    // the very first ubActive in the file will be 1,
	    // otherwise this needs to be cleaned up.
	    byte ubActive = file.readByte();
	    mercData.write(ubActive);
	    //System.out.println("ubActive = " + ubActive);
	    if (ubActive != 1) {  
		//System.err.println("Confused by save game format, giving up.");
		throw new FormatException("ubActive = " + ubActive);
	    }

	    // Read soldier data
	    byte[] soldierCreateData = new byte[this.SOLDIERCREATE_STRUCT__length];
	    this.file.readFully(soldierCreateData);
	    mercData.write(soldierCreateData, 0, soldierCreateData.length);
	    
	    // Read path node data
	    int uiNumOfNodes = file.readInt();
	    mercData.write((uiNumOfNodes & 0x000000FF));
	    mercData.write((uiNumOfNodes & 0x0000FF00) >> 8);
	    mercData.write((uiNumOfNodes & 0x00FF0000) >> 16);
	    mercData.write((uiNumOfNodes & 0xFF000000) >> 24);
	    //System.out.println("uiNumOfNodes = " + uiNumOfNodes);
	    if (uiNumOfNodes < 0) {
		//System.err.println("Confused by save game format, giving up.");
		throw new FormatException("uiNumOfNodes=" + uiNumOfNodes);
	    }
	    if (uiNumOfNodes > 0) {
		byte[] pathStData = new byte[uiNumOfNodes * this.PathSt__length];
		file.readFully(pathStData);
		mercData.write(pathStData, 0, pathStData.length);
	    }
	    
	    // Read key ring
	    byte ubOne = file.readByte();
	    mercData.write(ubOne);
	    //System.out.println("ubOne = " + ubOne);
	    if (ubOne != 1 && ubOne != 0) {
		//System.err.println("Confused by save game format, giving up.");
		throw new FormatException("ubOne = " + ubOne);
	    }
	    if (ubOne == 1) {
		byte[] keyRingData = new byte[this.NUM_KEYS * this.KEY_ON_RING__length];
		file.readFully(keyRingData);
		mercData.write(keyRingData, 0, keyRingData.length);
	    }
	    
	    // Read trailing inactive soldier entries and append them to
	    // this entry as opaque data, so that we don't have to
	    // deal with creating Mercenary objects for inactive
	    // profiles.
	    while (mercIdx < this.TOTAL_SOLDIERS) {
		byte nextActive = file.readByte();
		// Next entry is active?
		if( nextActive != 0 ) {
		    // Ok, back up one byte
		    file.seek(file.getFilePointer()-1);
		    break;
		}
		mercIdx++;
		mercData.write(nextActive);
		//System.out.println("Read inactive entry");
	    }
	    
	    // Create merc
	    Mercenary merc = new Mercenary(mercData.toByteArray(), this.codeTable);
	    this.mercs[mercCount] = merc;
	    this.mercCount = this.mercCount + 1;
	    
	    // Print debugging info
	    //  	    System.err.println();
//  	    System.out.println("Nick = \"" + merc.get("Nickname") + "\"");
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
	
	//	System.out.println("Number of SOLDIERCREATE_STRUCTs read = " + this.mercCount);
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

// Local Variables:
// tab-width: 8
// End:
