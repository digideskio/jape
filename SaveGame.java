/*
  Written 1999 by Douglas Greiman.
 
  This software may be used and distributed according to the terms
  of the GNU Public License, incorporated herein by reference.
*/

package duggelz.jape;

import java.io.File;
import java.io.RandomAccessFile;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.OutputStream;

public class SaveGame
{
    public static class FormatException extends Exception
    {
	public FormatException(String message) {
	    super(message);
	}
    }
    
    // == SAVED_GAME_HEADER
    public static final int SAVED_GAME_HEADER_Offset = 0;
    public static final int SAVED_GAME_HEADER_Length_99 = 0x1B0;
    public static final int SAVED_GAME_HEADER_Length_103 = 0x1B4;
    
    public static final int uiSavedGameVersion_Offset = 0;
    public static final int uiSavedGameVersion_Length = 4;

    public static final int uiDay_Offset = 0x118;
    public static final int ubHour_Offset = 0x11C;
    public static final int ubMin_Offset = 0x11D;
    public static final int iCurrentBalance_Offset = 0x124;
    public static final int sInitialGameOptions_Offset = 0x12F;
    // The GAME_OPTIONS struct (accidentally?) changes size in 1.13,
    // but due to padding, the next field is at the same offset as
    // 1.12 and earlier.
    public static final int sInitialGameOptions_Length_99 = 12;
    public static final int sInitialGameOptions_Length_103 = 14;
    public static final int uiRandom_Offset = 0x140;
    public static final int ubInventorySystem_Offset_103 = 0x144;

    // == TacticalStatusType
    public static final int TacticalStatusType_Offset_99 = 0x1B0;
    public static final int TacticalStatusType_Offset_103 = 0x1B4;
    public static final int TacticalStatusType_Length = 316;

    // == gWorldSectorX (INT16), gWorldSectorY (INT16), gWorldSectorZ (INT8)
    public static final int gWorldSectorXYZ_Offset_99 = 0x2EC;
    public static final int gWorldSectorXYZ_Offset_103 = 0x2F0;
    public static final int gWorldSectorXYZ_Length = 5;

    // No actual type here, just programmatic reads
    public static final int GameClock_Offset_99 = 0x2F1;
    public static final int GameClock_Offset_103 = 0x2F5;
    // = 4+1+1+1+1+4+4+1+4+4+1+4+1+1+1+1+4+4+20;
    public static final int GameClock_Length = 62; // == 0x3e

    // == STRATEGICEVENT
    public static final int uiNumGameEvents_Offset_99 = 0x32f;
    public static final int uiNumGameEvents_Offset_103 = 0x333;
    // Read uiNumGameEvents number of STRATEGICEVENT structs, starting
    // from this offset:
    public static final int STRATEGICEVENT_Offset_99 = 0x333;
    public static final int STRATEGICEVENT_Offset_103 = 0x337;
    public static final int STRATEGICEVENT_Length = 0x1C;

    // == LaptopSaveInfoStruct
    public static final int MAXITEMS_99 = 351;
    public static final int MAXITEMS_103 = 5001;
    public static final int LaptopSaveInfoStruct_Length_99 = 7440;
    public static final int LaptopSaveInfoStruct_Length_103 = 81840;
    // this is an array inside the LaptopSaveInfo
    public static final int BobbyRayInventory_RelOffset = 0x676; // 1654
    public static final int usNumberOfBobbyRayOrderItems_RelOffset_99 = 0x1c6c;
    public static final int usNumberOfBobbyRayOrderItems_RelOffset_103 = 0x13f0c;
    public static final int BobbyRayOrderStruct_Length = 84;
    public static final int ubNumberLifeInsurancePayouts_RelOffset_99 = 0x1c74;
    public static final int ubNumberLifeInsurancePayouts_RelOffset_103 = 0x13f14;

    // == MERCPROFILESTRUCT
    public static final int MERCPROFILESTRUCT_Count = 0xAA;
    public static final int MERCPROFILESTRUCT_Length_99 = 0x2CC;
    public static final int MERCPROFILESTRUCT_Length_103 = 0x278;
    public static final int MERCPROFILESTRUCT_invsize_RelOffset_103 = 
	MERCPROFILESTRUCT_Length_103;

    // == SOLDIERTYPE
    public static final int TOTAL_SOLDIERS = 156;
    public static final int SOLDIERTYPE_Length_99 = 0x918;
    public static final int SOLDIERTYPE_Length_103 = 0x430;
    public static final int SOLDIERTYPE_TrailingLength_103 = 525;

    // == struct path, PathSt
    public static final int PathSt_Length = 20;

    // == KEYS
    public static final int NUM_KEYS = 64;
    public static final int KEY_ON_RING_Length = 2;

    // == OBJECTTYPE (103)
    public static final int OBJECTTYPE_Length_103 = 5;
    public static final int STACKEDOBJECTDATA_Length_103 = 16;
    public static final int LBENODE_Length_103 = 16;

    // Basic data
    public String filename;
    public RandomAccessFile file;

    public int codeTableIdx;
    public int codeTableSubIdx;
    public int[] codeTable;

    // Read from save game
    public int uiSavedGameVersion = 0;
    public int STRATEGICEVENT_Count = 0; 
    public int LaptopSaveInfoStruct_Offset = 0;
    public int usNumberOfBobbyRayOrderItems = 0;
    public int ubNumberLifeInsurancePayouts = 0;

    public int actorCount = MERCPROFILESTRUCT_Count;
    public int actorOffset = 0;
    public Actor[] actors = new Actor[MERCPROFILESTRUCT_Count];

    public int mercCount = 0;
    public int mercOffset = 0;
    public Mercenary[] mercs = new Mercenary[this.TOTAL_SOLDIERS];

    // Computed offsets
    public int SAVED_GAME_HEADER_Length;
    public int sInitialGameOptionsLength;
    public int TacticalStatusType_Offset;
    public int gWorldSectorXYZ_Offset;
    public int GameClock_Offset;
    public int uiNumGameEvents_Offset;
    public int STRATEGICEVENT_Offset;
    public int MAXITEMS;
    public int LaptopSaveInfoStruct_Length;
    public int usNumberOfBobbyRayOrderItems_RelOffset;
    public int ubNumberLifeInsurancePayouts_RelOffset;
    public int MERCPROFILESTRUCT_Length;
    public int SOLDIERTYPE_Length;

    // Instance methods
    public void load(String filename) throws IOException, FormatException
    {
        // Open game game
        this.filename = filename;
        this.file = new RandomAccessFile(this.filename,"r");

	try {
	    // Read SAVE_GAME_HEADER
	    this.file.seek(this.uiSavedGameVersion_Offset);
	    this.uiSavedGameVersion = this.readIntLE(file);
	    System.out.println("Read saved game " + this.filename + 
			       ", version=" + this.uiSavedGameVersion);

	    // Compute offsets from save game version
	    if ((this.uiSavedGameVersion >= 95) && 
		(this.uiSavedGameVersion <= 99)) {
		SAVED_GAME_HEADER_Length = 
		    SAVED_GAME_HEADER_Length_99;
		sInitialGameOptionsLength = 
		    sInitialGameOptions_Length_99;
		TacticalStatusType_Offset = 
		    TacticalStatusType_Offset_99;
		gWorldSectorXYZ_Offset = 
		    gWorldSectorXYZ_Offset_99;
		GameClock_Offset = 
		    GameClock_Offset_99;
		uiNumGameEvents_Offset = 
		    uiNumGameEvents_Offset_99;
		STRATEGICEVENT_Offset = 
		    STRATEGICEVENT_Offset_99;
		MAXITEMS = 
		    MAXITEMS_99;
		LaptopSaveInfoStruct_Length = 
		    LaptopSaveInfoStruct_Length_99;
		usNumberOfBobbyRayOrderItems_RelOffset =
		    usNumberOfBobbyRayOrderItems_RelOffset_99;
		ubNumberLifeInsurancePayouts_RelOffset =
		    ubNumberLifeInsurancePayouts_RelOffset_99;
		MERCPROFILESTRUCT_Length = 
		    MERCPROFILESTRUCT_Length_99;
		SOLDIERTYPE_Length = 
		    SOLDIERTYPE_Length_99;
	    } else if ((this.uiSavedGameVersion >= 103) && 
		       (this.uiSavedGameVersion <= 103)) {
		SAVED_GAME_HEADER_Length = 
		    SAVED_GAME_HEADER_Length_103;
		sInitialGameOptionsLength = 
		    sInitialGameOptions_Length_103;
		TacticalStatusType_Offset = 
		    TacticalStatusType_Offset_103;
		gWorldSectorXYZ_Offset = 
		    gWorldSectorXYZ_Offset_103;
		GameClock_Offset = 
		    GameClock_Offset_103;
		uiNumGameEvents_Offset = 
		    uiNumGameEvents_Offset_103;
		STRATEGICEVENT_Offset = 
		    STRATEGICEVENT_Offset_103;
		MAXITEMS = 
		    MAXITEMS_103;
		LaptopSaveInfoStruct_Length = 
		    LaptopSaveInfoStruct_Length_103;
		usNumberOfBobbyRayOrderItems_RelOffset =
		    usNumberOfBobbyRayOrderItems_RelOffset_103;
		ubNumberLifeInsurancePayouts_RelOffset =
		    ubNumberLifeInsurancePayouts_RelOffset_103;
		MERCPROFILESTRUCT_Length = 
		    MERCPROFILESTRUCT_Length_103;
		SOLDIERTYPE_Length = 
		    SOLDIERTYPE_Length_103;
	    } else {
		throw new FormatException(
		    "Save game version " + this.uiSavedGameVersion + 
		    " not supported.  " +
		    "Supported values are 95, 96, 97, 97, 99 and 103");
	    }

	    this.file.seek(this.uiNumGameEvents_Offset);
	    this.STRATEGICEVENT_Count = this.readIntLE(this.file);
	    if ((this.STRATEGICEVENT_Count < 0) ||
		(this.STRATEGICEVENT_Count > 1000)) {
		throw new FormatException(
		    "Internal Error: STRATEGICEVENT_Count = " + 
		    this.STRATEGICEVENT_Count);
	    }

	    // Read MERCPROFILESTRUCTs
	    this.actorOffset = 
		( this.STRATEGICEVENT_Offset +
		  this.STRATEGICEVENT_Count * this.STRATEGICEVENT_Length +
		  this.LaptopSaveInfoStruct_Length);
	    
	    // Decide which coding table was used for MERCPROFILESTRUCTs
	    this.file.seek(this.actorOffset);
	    byte[] rawData = new byte[16];
	    this.file.readFully(rawData);
	    int[] indexes = this.findActorCodeTable(rawData);
	    if ( indexes == null) {
		throw new FormatException("Unable to determine code table");
	    }
	    this.codeTableIdx = indexes[0];
	    this.codeTableSubIdx = indexes[1];
	    if (indexes[0] == -1) {
		    this.codeTable = null;
	    } else {
		    this.codeTable = JapeConst.CodeTables
			[this.codeTableIdx][this.codeTableSubIdx];
	    }

	    // Finally read MERCPROFILESTRUCTs data
	    this.loadActors();

	    // Read SOLDIERCREATE_STRUCTs
	    this.mercOffset = (int) file.getFilePointer();
	    this.loadMercs();
	}
	finally
	{
	    // Close game file
	    this.file.close();
	}
    }
    
    // Read all Actor data
    public void loadActors() throws IOException, FormatException
    {
	//System.out.println("actorOffset = " + this.actorOffset);
        this.file.seek(this.actorOffset);
	for( int idx = 0; idx < this.MERCPROFILESTRUCT_Count; ++idx )
	{
	    ByteArrayOutputStream actorData = new ByteArrayOutputStream();

	    // Read actor data
            byte[] mercProfileStructData = 
		new byte[this.MERCPROFILESTRUCT_Length];
	    this.file.readFully(mercProfileStructData);
	    actorData.write(mercProfileStructData, 0,
			    mercProfileStructData.length);
	    
	    if (this.uiSavedGameVersion == 103) {
		int inventorySize = this.readIntLE(this.file);
		this.writeIntLE(actorData, inventorySize);
		//System.out.println("Read " + inventorySize + " items");
		if ((inventorySize < 0) || (inventorySize > 1000)) {
		    throw new FormatException
			("Invalid inventory size " + inventorySize + 
			 " for MERCPROFILESTRUCT " + idx);
		}
		for(int itemIdx = 0; itemIdx < inventorySize; ++itemIdx) {
		    byte[] inventoryItem = new byte[12];
		    this.file.readFully(inventoryItem);
		    actorData.write(inventoryItem, 0, inventoryItem.length);
		}
	    }

	    // Create new actor
            Actor actor = new Actor(actorData.toByteArray(),
				    uiSavedGameVersion, 
				    this.codeTable);
	    actor.validateChecksum();
	    System.out.println("Read MERCPROFILE '" +
			       actor.get("Nickname") + "'");
            this.actors[idx] = actor;
        }
    }

    // Read all Mercenary data
    public void loadMercs() throws IOException, FormatException
    {
	//System.out.println("mercOffset = 0x" +
	//Integer.toHexString(this.mercOffset));
        this.file.seek(this.mercOffset);

	// Read the initial run of inactive soldiers (may be none)
	int mercIdx = 0;
	while (mercIdx < this.TOTAL_SOLDIERS) {
	    byte initialActive = file.readByte();
	    // This entry is active?
	    if( initialActive != 0 ) {
		// Ok, back up one byte
		file.seek(file.getFilePointer()-1);
		break;
	    }
	    mercIdx++;
	    this.mercOffset++;
	    //System.out.println("Read 1 inactive entry");
	}
	    
	// Read active soldiers.  Note that each mercData starts with
	// 1 byte for ubActive (its own), and ends with 0 to many
	// bytes for ubActives for inactive mercs.  This is a hack to
	// avoid having to create and deal with empty Merc objects for
	// inactive soldiers.
	for( ; mercIdx < this.TOTAL_SOLDIERS; ++mercIdx )
	{
	    int trailingOffset = 0;
	    ByteArrayOutputStream mercData = new ByteArrayOutputStream();

	    // Read boolean "is soldier active?" byte.
	    byte ubActive = file.readByte();
	    mercData.write(ubActive);
	    //System.out.println("ubActive = " + ubActive);
	    if (ubActive != 1) {  
		throw new FormatException("ubActive = " + ubActive);
	    }

	    // Read soldier data
	    byte[] soldierData = new byte[this.SOLDIERTYPE_Length];
	    this.file.readFully(soldierData);
	    mercData.write(soldierData, 0, soldierData.length);

	    int inventorySum = 0;
	    if (this.uiSavedGameVersion == 103) {
		// Read new inventory
		inventorySum = inventoryLoad_103(this.file, mercData);

		// Read rest of soldier data
		trailingOffset = mercData.size() - 1; // -1 for ubActive
		byte[] trailingData = 
		    new byte[this.SOLDIERTYPE_TrailingLength_103];
		this.file.readFully(trailingData);
		mercData.write(trailingData, 0, trailingData.length);
	    }

	    // Read path node data
	    int uiNumOfNodes = this.readIntLE(file);
	    this.writeIntLE(mercData, uiNumOfNodes);
	    if ((uiNumOfNodes < 0) || (uiNumOfNodes > 1000)) {
		throw new FormatException(
                    "Internal error: uiNumOfNodes=" + uiNumOfNodes);
	    } else {
		byte[] pathStData = new byte[uiNumOfNodes * 
					     this.PathSt_Length];
		file.readFully(pathStData);
		mercData.write(pathStData, 0, pathStData.length);
	    }
	    
	    // Read key ring
	    byte ubOne = file.readByte();
	    mercData.write(ubOne);
	    //System.out.println("ubOne = " + ubOne);
	    if (ubOne != 1 && ubOne != 0) {
		throw new FormatException("ubOne = " + ubOne);
	    }
	    if (ubOne == 1) {
		byte[] keyRingData = new byte[this.NUM_KEYS * 
					      this.KEY_ON_RING_Length];
		file.readFully(keyRingData);
		mercData.write(keyRingData, 0, keyRingData.length);
	    }
	    
	    // Read subsequent inactive soldier entries and append them to
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
	    Mercenary merc = new Mercenary(mercData.toByteArray(),
					   this.uiSavedGameVersion,
					   trailingOffset,
					   inventorySum,
					   this.codeTable);
	    System.out.println("Read SOLDIERTYPE '" +
			       merc.get("Nickname") + "'");
	    merc.validateChecksum();
	    this.mercs[mercCount] = merc;
	    this.mercCount = this.mercCount + 1;
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
	for( int idx = 0; idx < this.MERCPROFILESTRUCT_Count; ++idx )
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
                byte[] plaintext = 
		    JapeAlg.Decode(
			ciphertext, 8, 
			JapeConst.CodeTables[mainTable][subTable]);

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

	// Not encrypted?
        if ( (ciphertext[1] == 0) &&
	     (ciphertext[3] == 0) &&
	     (ciphertext[5] == 0) &&
	     (ciphertext[7] == 0) ) 
	{
		int[] results = { -1, -1 };
		return results;
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
	for( int idx = 0; idx < this.MERCPROFILESTRUCT_Count; ++idx )
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

    /** Little-endian 4-byte read */
    public int readIntLE(DataInput d) throws IOException {
	int i = d.readInt();
	int j = (((i & 0x000000FF) << 24) |
		 ((i & 0x0000FF00) << 8) |
		 ((i & 0x00FF0000) >>> 8) |
		 ((i & 0xFF000000) >>> 24));
	return j;
    }

    /** Little-endian 4-byte write */
    public void writeIntLE(OutputStream os, int i) throws IOException {
	os.write((i & 0x000000FF));
	os.write((i & 0x0000FF00) >>> 8);
	os.write((i & 0x00FF0000) >>> 16);
	os.write((i & 0xFF000000) >>> 24);
    }

    // For save game version 103
    public int inventoryLoad_103(DataInput file, 
				 OutputStream mercData) 
	throws IOException, FormatException 
    {
	int inventorySize = this.readIntLE(file);
	this.writeIntLE(mercData, inventorySize);
	//System.out.println("Read " + inventorySize + " items");
	if ((inventorySize < 0) || (inventorySize > 55)) {
	    throw new FormatException
		("Invalid inventory size " + inventorySize);
	}
	int sum = 0;
	for(int itemIdx = 0; itemIdx < inventorySize; ++itemIdx) {
	    sum += this.objecttypeLoad_103(file, mercData);

	    int bNewItemCount;
	    bNewItemCount = this.readIntLE(file);
	    this.writeIntLE(mercData, bNewItemCount);

	    int bNewItemCycleCount;
	    bNewItemCycleCount = this.readIntLE(file);
	    this.writeIntLE(mercData, bNewItemCycleCount);
	}
	return sum;
    }

    // For save game version 103
    public int objecttypeLoad_103(DataInput file,
				  OutputStream mercData)
	throws IOException, FormatException 
    {
	byte obj[] = new byte[OBJECTTYPE_Length_103];
	file.readFully(obj);
	mercData.write(obj);

	// For computing checksum
	ShortField usItem = new ShortField(0);
	ByteField ubNumberOfObjects = new ByteField(2);
	int sum = usItem.getInt(obj) + ubNumberOfObjects.getInt(obj);

	// Read stacked objects, whatever that means
	int objStackSize = this.readIntLE(file);
	this.writeIntLE(mercData, objStackSize);
	if ((objStackSize < 0) || (objStackSize > 55)) {
	    throw new FormatException
		("Invalid object stack size " + objStackSize);
	}
	for(int idx = 0; idx<objStackSize; ++idx) {
	    stackedObjectDataLoad_103(file, mercData);
	}

	return sum;
    }

    // For save game version 103
    public void stackedObjectDataLoad_103(DataInput file,
					  OutputStream mercData)
	throws IOException, FormatException 
    {
	byte sob[] = new byte[STACKEDOBJECTDATA_Length_103];
	file.readFully(sob);
	mercData.write(sob);

	int attachmentsSize = this.readIntLE(file);
	this.writeIntLE(mercData, attachmentsSize);
	if ((attachmentsSize < 0) || (attachmentsSize > 55)) {
	    throw new FormatException
		("Invalid attachment size " + attachmentsSize);
	}
	for(int idx = 0; idx<attachmentsSize; ++idx) {
	    objecttypeLoad_103(file, mercData);
	}
    }
	
    /** main() for testing */
    public static void main(String[] args) {
	File saveDir = new File("Saves");
	File[] filenames = saveDir.listFiles();
	for( int idx = 0; idx < filenames.length; ++idx) {
	    File filename = filenames[idx];
	    if (filename.getName().endsWith(".sav")) {
		SaveGame saveGame = new SaveGame();
		try {
		    saveGame.load(filename.getPath());
		} catch( IOException e ) {
		    e.printStackTrace(System.err);
		    //System.exit(1);
		} catch( SaveGame.FormatException e ) {
		    e.printStackTrace(System.err);
		    //System.exit(1);
		}
	    }
	}
    }
}

// Local Variables:
// tab-width: 8
// End:
