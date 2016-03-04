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

    public static final int ENERGY_OFFSET_99      = 0x2c7;
    public static final int ENERGY_OFFSET_103     = 45;
    public static final int MAX_ENERGY_OFFSET_99  = 0x2c8;
    public static final int MAX_ENERGY_OFFSET_103 = 46;
    public static final int NICK_OFFSET_99        = 0x2da;
    public static final int NICK_OFFSET_103       = 2;
    public static final int NICK_LENGTH           = 20;
    public static final int ubProfile_Offset_103  = 717;
    public static final int CHECKSUM_OFFSET_99    = 0x8a0;
    public static final int CHECKSUM_OFFSET_103   = 972;
    public static final int CHECKSUM_LENGTH     = 4;

    // These stats are in STRUCT_AIData in 103
    public static final int MORALE_OFFSET_99      = 0x6c8;
    public static final int MORALE_OFFSET_103     = 204;

    // These stats are in STRUCT_Statistics in 103
    public static final int statsOffset_103       = 428;
    public static final int SKILL1_OFFSET_99      = 0x340;
    public static final int SKILL1_OFFSET_103     = 8 + statsOffset_103;
    public static final int SKILL2_OFFSET_99      = 0x341;
    public static final int SKILL2_OFFSET_103     = 9 + statsOffset_103;
    public static final int DEX_OFFSET_99         = 0x348;
    public static final int DEX_OFFSET_103        = 10 + statsOffset_103;
    public static final int WIS_OFFSET_99         = 0x349;
    public static final int WIS_OFFSET_103        = 11 + statsOffset_103;
    public static final int LVL_OFFSET_99         = 0x351;
    public static final int LVL_OFFSET_103        = 2 + statsOffset_103;
    public static final int HEALTH_OFFSET_99      = 0x364;
    public static final int HEALTH_OFFSET_103     = 0 + statsOffset_103;
    public static final int AGI_OFFSET_99         = 0x370;
    public static final int AGI_OFFSET_103        = 3 + statsOffset_103;
    public static final int STR_OFFSET_99         = 0x376;
    public static final int STR_OFFSET_103        = 4 + statsOffset_103;
    public static final int LDR_OFFSET_99         = 0x37f;
    public static final int LDR_OFFSET_103        = 14 + statsOffset_103;
    public static final int MEC_OFFSET_99         = 0x394;
    public static final int MEC_OFFSET_103        = 5 + statsOffset_103;
    public static final int MAX_HEALTH_OFFSET_99  = 0x395;
    public static final int MAX_HEALTH_OFFSET_103 = 1 + statsOffset_103;
    public static final int MED_OFFSET_99         = 0x55c;
    public static final int MED_OFFSET_103        = 12 + statsOffset_103;
    public static final int MRK_OFFSET_99         = 0x561;
    public static final int MRK_OFFSET_103        = 6 + statsOffset_103;
    public static final int EXP_OFFSET_99         = 0x562;
    public static final int EXP_OFFSET_103        = 7 + statsOffset_103;

    // Instance data
    public byte[] allData;
    public Item[] items = new Item[ITEM_COUNT];
    public int savedGameVersion;
    public int trailingOffset;
    public int inventorySum;

    public int ENERGY_OFFSET;
    public int MAX_ENERGY_OFFSET;
    public int NICK_OFFSET;
    public int CHECKSUM_OFFSET;
    public int MORALE_OFFSET;
    public int SKILL1_OFFSET;
    public int SKILL2_OFFSET;
    public int DEX_OFFSET;
    public int WIS_OFFSET;
    public int LVL_OFFSET;
    public int HEALTH_OFFSET;
    public int AGI_OFFSET;
    public int STR_OFFSET;
    public int LDR_OFFSET;
    public int MEC_OFFSET;
    public int MAX_HEALTH_OFFSET;
    public int MED_OFFSET;
    public int MRK_OFFSET;
    public int EXP_OFFSET;

    // Instance methods
    public Mercenary(byte[] rawData, int savedGameVersion,
		     int trailingOffset,  int inventorySum,
		     int[] table) {
	super(new Hashtable());
	this.savedGameVersion = savedGameVersion;
	this.trailingOffset = trailingOffset;
	this.inventorySum = inventorySum;
	this.decode(rawData, table);

	// Compute field offsets
	if (savedGameVersion <= 99) {
	    ENERGY_OFFSET = ENERGY_OFFSET_99;
	    MAX_ENERGY_OFFSET = MAX_ENERGY_OFFSET_99;
	    NICK_OFFSET = NICK_OFFSET_99;
	    CHECKSUM_OFFSET = CHECKSUM_OFFSET_99;
	    MORALE_OFFSET = MORALE_OFFSET_99;
	    SKILL1_OFFSET = SKILL1_OFFSET_99;
	    SKILL2_OFFSET = SKILL2_OFFSET_99;
	    DEX_OFFSET = DEX_OFFSET_99;
	    WIS_OFFSET = WIS_OFFSET_99;
	    LVL_OFFSET = LVL_OFFSET_99;
	    HEALTH_OFFSET = HEALTH_OFFSET_99;
	    AGI_OFFSET = AGI_OFFSET_99;
	    STR_OFFSET = STR_OFFSET_99;
	    LDR_OFFSET = LDR_OFFSET_99;
	    MEC_OFFSET = MEC_OFFSET_99;
	    MAX_HEALTH_OFFSET = MAX_HEALTH_OFFSET_99;
	    MED_OFFSET = MED_OFFSET_99;
	    MRK_OFFSET = MRK_OFFSET_99;
	    EXP_OFFSET = EXP_OFFSET_99;
	} else {
	    ENERGY_OFFSET = ENERGY_OFFSET_103;
	    MAX_ENERGY_OFFSET = MAX_ENERGY_OFFSET_103;
	    NICK_OFFSET = NICK_OFFSET_103;
	    CHECKSUM_OFFSET = CHECKSUM_OFFSET_103;
	    MORALE_OFFSET = trailingOffset + MORALE_OFFSET_103;
	    SKILL1_OFFSET = trailingOffset + SKILL1_OFFSET_103;
	    SKILL2_OFFSET = trailingOffset + SKILL2_OFFSET_103;
	    DEX_OFFSET = trailingOffset + DEX_OFFSET_103;
	    WIS_OFFSET = trailingOffset + WIS_OFFSET_103;
	    LVL_OFFSET = trailingOffset + LVL_OFFSET_103;
	    HEALTH_OFFSET = trailingOffset + HEALTH_OFFSET_103;
	    AGI_OFFSET = trailingOffset + AGI_OFFSET_103;
	    STR_OFFSET = trailingOffset + STR_OFFSET_103;
	    LDR_OFFSET = trailingOffset + LDR_OFFSET_103;
	    MEC_OFFSET = trailingOffset + MEC_OFFSET_103;
	    MAX_HEALTH_OFFSET = trailingOffset + MAX_HEALTH_OFFSET_103;
	    MED_OFFSET = trailingOffset + MED_OFFSET_103;
	    MRK_OFFSET = trailingOffset + MRK_OFFSET_103;
	    EXP_OFFSET = trailingOffset + EXP_OFFSET_103;
	}

	this.fields.put("Energy", new ByteField(ENERGY_OFFSET));
	this.fields.put("Max Energy",
			new ByteField(MAX_ENERGY_OFFSET));
	this.fields.put("Nickname", 
			new StringField(NICK_OFFSET, NICK_LENGTH));
	this.fields.put("Skill1", 
			new ChoiceField(new ByteField(SKILL1_OFFSET),
					Skill.table));
	this.fields.put("Skill2", 
			new ChoiceField(new ByteField(SKILL2_OFFSET),
					Skill.table));
	this.fields.put("Dexterity", new ByteField(DEX_OFFSET));
	this.fields.put("Wisdom", new ByteField(WIS_OFFSET));
	this.fields.put("Level", new ByteField(LVL_OFFSET));
	this.fields.put("Health", new ByteField(HEALTH_OFFSET));
	this.fields.put("Agility", new ByteField(AGI_OFFSET));
	this.fields.put("Strength", new ByteField(STR_OFFSET));
	this.fields.put("Leadership", new ByteField(LDR_OFFSET));
	this.fields.put("Mechanical", new ByteField(MEC_OFFSET));
	this.fields.put("Max Health", new ByteField(MAX_HEALTH_OFFSET));
	this.fields.put("Medical", new ByteField(MED_OFFSET));
	this.fields.put("Marksmanship", new ByteField(MRK_OFFSET));
	this.fields.put("Explosives", new ByteField(EXP_OFFSET));
	this.fields.put("Morale", new ByteField(MORALE_OFFSET));
	this.fields.put("ubProfile", new ByteField(ubProfile_Offset_103));
	this.fields.put("Checksum", new IntField(CHECKSUM_OFFSET));
    }

    public void decode(
	byte[] rawData, 
	int[] table)
    {
	// Save all data
	this.allData = new byte[rawData.length];
	System.arraycopy(rawData, 0, this.allData, 0, this.allData.length);

	// Decode the encoded portion
	if (this.savedGameVersion <= 99) {
	    byte[] ciphertext = new byte[CIPHERTEXT_LENGTH];
	    System.arraycopy(rawData, CIPHERTEXT_OFFSET, ciphertext, 0, 
			     CIPHERTEXT_LENGTH);
	    byte[] plaintext = JapeAlg.Decode(ciphertext, ciphertext.length,
					      table);
	    this.data = plaintext;

	    // Instantiate the merc's items
	    for( int idx = 0; idx < this.ITEM_COUNT; ++idx ) {
		byte[] itemData = new byte[ITEM_LENGTH];
		int itemOffset = FIRST_ITEM_OFFSET + idx * ITEM_LENGTH;
		System.arraycopy(this.data, itemOffset, itemData, 0,
				 ITEM_LENGTH);
		Item item = new Item(itemData);
		this.items[idx] = item;
	    }
	} else {
	    // No encryption used
	    this.data = new byte[rawData.length-1];
	    System.arraycopy(rawData, 1, this.data, 0, this.data.length);
	}
    }
    
    public byte[] encode(
	int[] table)
    {
	// Copy in the merc's items
	if (this.savedGameVersion <= 99) {
	    for( int idx = 0; idx < this.ITEM_COUNT; ++idx ) {
		Item item = this.items[idx];
		byte[] itemData = item.encode();
		int itemOffset = FIRST_ITEM_OFFSET + idx * ITEM_LENGTH;
		System.arraycopy(itemData, 0, this.data, itemOffset, 
				 ITEM_LENGTH);
	    }
	}

	// Make sure checksum is valid
	this.recomputeChecksum();

	// Copy over non-encrypted data
        byte[] rawData = new byte[this.allData.length];
	System.arraycopy(this.allData, 0, rawData, 0, this.allData.length);

	// Start copying in decrypted data
	if (this.savedGameVersion <= 99) {
	    byte[] plaintext = new byte[CIPHERTEXT_LENGTH];
	    System.arraycopy(this.data, 0, plaintext, 0, CIPHERTEXT_LENGTH);
	    
	    // Encode the decoded portion
	    byte[] ciphertext = JapeAlg.Encode(plaintext, plaintext.length,
					       table);
	    System.arraycopy(ciphertext, 0, rawData, CIPHERTEXT_OFFSET,
			     CIPHERTEXT_LENGTH);
	} else {
	    System.arraycopy(this.data, 0, rawData, 1,
			     this.data.length);
	}

        return rawData;
    }

    public int computeChecksum() {
	if (this.savedGameVersion <= 99) {
	    return JapeAlg.MercChecksum(this.data);
	} else {
	    int checksum = 1;
	    checksum += (this.getInt("Health") + 1);
	    checksum *= (this.getInt("Max Health") + 1);
	    checksum += (this.getInt("Agility") + 1);
	    checksum *= (this.getInt("Dexterity") + 1);
	    checksum += (this.getInt("Strength") + 1);
	    checksum *= (this.getInt("Marksmanship") + 1);
	    checksum += (this.getInt("Medical") + 1);
	    checksum *= (this.getInt("Mechanical") + 1);
	    checksum += (this.getInt("Explosives") + 1);

	    checksum *= (this.getInt("Level") + 1);
	    checksum += (this.getInt("ubProfile") + 1);

	    // Iterate through inventory
	    checksum += this.inventorySum;
	    
	    return checksum;
	}
    }

    public void validateChecksum() throws SaveGame.FormatException
    {
        int checksum = this.computeChecksum();
	int savedChecksum = this.getInt("Checksum");
	if (checksum != savedChecksum) {
	    throw new SaveGame.FormatException(
		"Invalid checksum (0x" + Integer.toHexString(checksum) +
		" vs 0x" + Integer.toHexString(savedChecksum) +
		") in SOLDIERTYPE for '" +
		this.get("Nickname") + "'");
	}
    }

    public void recomputeChecksum()
    {
        int checksum = this.computeChecksum();
	this.setInt("Checksum", checksum);
    }
}

// Local Variables:
// tab-width: 8
// End:
