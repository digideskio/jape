/*
  Written 1999 by Douglas Greiman.
 
  This software may be used and distributed according to the terms
  of the GNU Public License, incorporated herein by reference.
*/

package duggelz.jape;

import java.util.Hashtable;

public class Mercenary extends BasicStructure
{
    // Static data
    public static final int CIPHERTEXT_OFFSET   = 1;
    public static final int CIPHERTEXT_LENGTH   = 0x918;

    public static final int FIRST_ITEM_OFFSET   = 0x0c;
    public static final int ITEM_LENGTH         = 0x24;
    public static final int ITEM_COUNT          = 19;

    public static final int HELMET_INDEX        = 0;
    public static final int BODY_ARMOR_INDEX    = 1;
    public static final int LEG_ARMOR_INDEX     = 2;
    public static final int HEADGEAR_1_INDEX    = 3;
    public static final int HEADGEAR_2_INDEX    = 4;
    public static final int RIGHT_HAND_INDEX    = 5;
    public static final int LEFT_HAND_INDEX     = 6;

    public static final int BACKPACK_1_3_INDEX  = 7;
    public static final int BACKPACK_2_3_INDEX  = 8;
    public static final int BACKPACK_3_3_INDEX  = 9;
    public static final int BACKPACK_4_3_INDEX  = 10;

    public static final int BACKPACK_1_1_INDEX  = 11;
    public static final int BACKPACK_2_1_INDEX  = 12;
    public static final int BACKPACK_3_1_INDEX  = 13;
    public static final int BACKPACK_4_1_INDEX  = 14;

    public static final int BACKPACK_1_2_INDEX  = 15;
    public static final int BACKPACK_2_2_INDEX  = 16;
    public static final int BACKPACK_3_2_INDEX  = 17;
    public static final int BACKPACK_4_2_INDEX  = 18;

    public static final int ENERGY_OFFSET       = 0x2c7;
    public static final int MAX_ENERGY_OFFSET   = 0x2c8;
    public static final int NICK_OFFSET         = 0x2da;
    public static final int NICK_LENGTH         = 20;
    public static final int SKILL1_OFFSET       = 0x340;
    public static final int SKILL2_OFFSET       = 0x341;
    public static final int DEX_OFFSET          = 0x348;
    public static final int WIS_OFFSET          = 0x349;
    public static final int LVL_OFFSET          = 0x351;
    public static final int HEALTH_OFFSET       = 0x364;
    public static final int AGI_OFFSET          = 0x370;
    public static final int STR_OFFSET          = 0x376;
    public static final int LDR_OFFSET          = 0x37f;
    public static final int MEC_OFFSET          = 0x394;
    public static final int MAX_HEALTH_OFFSET   = 0x395;
    public static final int HEAD_OFFSET         = 0x39c;
    public static final int PANTS_OFFSET        = 0x3ba;
    public static final int VEST_OFFSET         = 0x3d8;
    public static final int SKIN_OFFSET         = 0x3f6;
    public static final int MED_OFFSET          = 0x55c;
    public static final int MRK_OFFSET          = 0x561;
    public static final int EXP_OFFSET          = 0x562;
    public static final int MORALE_OFFSET       = 0x6c8;
    //    public static final int #???_OFFSET         = 0x721;
    public static final int CHECKSUM_OFFSET     = 0x8a0;
    public static final int CHECKSUM_LENGTH     = 4;

    public static Hashtable classFields = new Hashtable();
    static {
	classFields.put("Energy", new ByteField(ENERGY_OFFSET));       
	classFields.put("Max Energy", new ByteField(MAX_ENERGY_OFFSET));   
	classFields.put("Nickname", new StringField(NICK_OFFSET, NICK_LENGTH));         
	classFields.put("Skill1", new ChoiceField(new ByteField(SKILL1_OFFSET), Skill.table));
	classFields.put("Skill2", new ChoiceField(new ByteField(SKILL2_OFFSET), Skill.table));
	classFields.put("Dexterity", new ByteField(DEX_OFFSET));          
	classFields.put("Wisdom", new ByteField(WIS_OFFSET));          
	classFields.put("Level", new ByteField(LVL_OFFSET));          
	classFields.put("Health", new ByteField(HEALTH_OFFSET));       
	classFields.put("Agility", new ByteField(AGI_OFFSET));          
	classFields.put("Strength", new ByteField(STR_OFFSET));          
	classFields.put("Leadership", new ByteField(LDR_OFFSET));          
	classFields.put("Mechanical", new ByteField(MEC_OFFSET));          
	classFields.put("Max Health", new ByteField(MAX_HEALTH_OFFSET));   
	classFields.put("Head", new ByteField(HEAD_OFFSET));         
	classFields.put("Pants", new ByteField(PANTS_OFFSET));        
	classFields.put("Vest", new ByteField(VEST_OFFSET));         
	classFields.put("Skin", new ByteField(SKIN_OFFSET));         
	classFields.put("Medical", new ByteField(MED_OFFSET));          
	classFields.put("Marksmanship", new ByteField(MRK_OFFSET));          
	classFields.put("Explosives", new ByteField(EXP_OFFSET));          
	classFields.put("Morale", new ByteField(MORALE_OFFSET));       
	classFields.put("Checksum", new IntField(CHECKSUM_OFFSET));     
    }

    // Instance data
    public byte[] allData;
    public Item[] items = new Item[ITEM_COUNT];
    public int adjustCount;

    // Instance methods
    public Mercenary(byte[] rawData, int[] table) {
	super(classFields);
	this.decode(rawData, table);
    }

    public void decode(
	byte[] rawData, 
	int[] table)
    {
	// Save all data
	this.allData = new byte[rawData.length];
	System.arraycopy(rawData, 0, this.allData, 0, this.allData.length);

	// Decode the encoded portion
        byte[] ciphertext = new byte[CIPHERTEXT_LENGTH];
	System.arraycopy(rawData, CIPHERTEXT_OFFSET, ciphertext, 0, CIPHERTEXT_LENGTH);
        byte[] plaintext = JapeAlg.Decode(ciphertext, ciphertext.length, table);
	this.data = plaintext;

	// Instantiate the merc's items
	for( int idx = 0; idx < this.ITEM_COUNT; ++idx ) 
	{
	    byte[] itemData = new byte[ITEM_LENGTH];
	    int itemOffset = FIRST_ITEM_OFFSET + idx * ITEM_LENGTH;
	    System.arraycopy(this.data, itemOffset, itemData, 0, ITEM_LENGTH);
	    Item item = new Item(itemData);
	    this.items[idx] = item;
	}
    }
    
    public byte[] encode(
	int[] table)
    {
	// Copy in the merc's items
	for( int idx = 0; idx < this.ITEM_COUNT; ++idx ) 
	{
	    Item item = this.items[idx];
	    byte[] itemData = item.encode();
	    int itemOffset = FIRST_ITEM_OFFSET + idx * ITEM_LENGTH;
	    System.arraycopy(itemData, 0, this.data, itemOffset, ITEM_LENGTH);
	}

	// Make sure checksum is valid
	this.recomputeChecksum();

	// Copy over non-encrypted data
        byte[] rawData = new byte[this.allData.length];
	System.arraycopy(this.allData, 0, rawData, 0, this.allData.length);

	// Start copying in decrypted data
        byte[] plaintext = new byte[CIPHERTEXT_LENGTH];
	System.arraycopy(this.data, 0, plaintext, 0, CIPHERTEXT_LENGTH);

	// Encode the decoded portion
        byte[] ciphertext = JapeAlg.Encode(plaintext, plaintext.length, table);
	System.arraycopy(ciphertext, 0, rawData, CIPHERTEXT_OFFSET, CIPHERTEXT_LENGTH);

        return rawData;
    }

    public void recomputeChecksum()
    {
        int checksum = JapeAlg.MercChecksum(this.data);
	this.data[this.CHECKSUM_OFFSET] = (byte) (checksum & 0xFF);
        this.data[this.CHECKSUM_OFFSET+1] = (byte) ((checksum >> 8) & 0xFF);
        this.data[this.CHECKSUM_OFFSET+2] = (byte) ((checksum >> 16) & 0xFF);
        this.data[this.CHECKSUM_OFFSET+3] = (byte) ((checksum >> 24) & 0xFF);
    }
}
