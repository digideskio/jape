/*
  Written 1999 by Douglas Greiman.
 
  This software may be used and distributed according to the terms
  of the GNU Public License, incorporated herein by reference.
*/

package duggelz.jape;

import java.util.Hashtable;

// == struct MERCPROFILESTRUCT
public class Actor extends BasicStructure
{
    // Static data
    public static final int NAME_OFFSET        = 0x0;
    public static final int NAME_LENGTH        = 0x3c;
    public static final int NICK_OFFSET        = 0x3c;
    public static final int NICK_LENGTH        = 0x14;
    public static final int MED_OFFSET         = 0x105;
    public static final int STR_OFFSET         = 0x128;
    public static final int MAX_HEALTH_OFFSET  = 0x129;
    public static final int LVL_INC_OFFSET     = 0x12a;
    public static final int HEALTH_INC_OFFSET  = 0x12b;
    public static final int AGI_INC_OFFSET     = 0x12c;
    public static final int DEX_INC_OFFSET     = 0x12d;
    public static final int WIS_INC_OFFSET     = 0x12e;
    public static final int MRK_INC_OFFSET     = 0x12f;
    public static final int MED_INC_OFFSET     = 0x130;
    public static final int MEC_INC_OFFSET     = 0x131;
    public static final int EXP_INC_OFFSET     = 0x132;
    public static final int STR_INC_OFFSET     = 0x133;
    public static final int LDR_INC_OFFSET     = 0x134;
    public static final int KILLS_OFFSET       = 0x136;
    public static final int KILLS_LENGTH       = 0x2;
    public static final int ASSISTS_OFFSET     = 0x138;
    public static final int ASSISTS_LENGTH     = 0x2;
    public static final int SHOTS_FIRED_OFFSET = 0x13a;
    public static final int SHOTS_FIRED_LENGTH = 0x2;
    public static final int SHOTS_HIT_OFFSET   = 0x13c;
    public static final int SHOTS_HIT_LENGTH   = 0x2;
    public static final int BATTLES_OFFSET     = 0x13e;
    public static final int BATTLES_LENGTH     = 0x2;
    public static final int WOUNDS_OFFSET      = 0x140;
    public static final int WOUNDS_LENGTH      = 0x2;
    public static final int HEALTH_OFFSET      = 0x14e;
    public static final int DEX_OFFSET         = 0x14f;
    public static final int PERSONALITY_OFFSET = 0x150;
    public static final int SKILL1_OFFSET      = 0x151;
    public static final int EXP_OFFSET         = 0x153;
    public static final int SKILL2_OFFSET      = 0x154;
    public static final int LDR_OFFSET         = 0x155;
    public static final int LVL_OFFSET         = 0x160;
    public static final int MRK_OFFSET         = 0x161;
    public static final int WIS_OFFSET         = 0x163;
    public static final int AGI_OFFSET_99      = 0x195;
    public static final int AGI_OFFSET_103     = 0x195 - 19 * 2;
    public static final int MEC_OFFSET_99      = 0x19b;
    public static final int MEC_OFFSET_103     = 0x19b - 19 * 2;
    public static final int CHECKSUM_OFFSET_99 = 0x2b8;
    public static final int CHECKSUM_OFFSET_103= 0x2b8 - 19 * 4 - 4;
    public static final int CHECKSUM_LENGTH    = 0x4;

    // Instance variables
    public int savedGameVersion;
    public int AGI_OFFSET;
    public int MEC_OFFSET;
    public int CHECKSUM_OFFSET;

    // Instance methods
    public Actor(byte[] rawData, int savedGameVersion, int[] table) {
	super(new Hashtable());
	this.savedGameVersion = savedGameVersion;
	this.decode(rawData, table);

	if (savedGameVersion <= 99) {
	    AGI_OFFSET = AGI_OFFSET_99;
	    MEC_OFFSET = MEC_OFFSET_99;
	    CHECKSUM_OFFSET = CHECKSUM_OFFSET_99;
	} else {
	    AGI_OFFSET = AGI_OFFSET_103;
	    MEC_OFFSET = MEC_OFFSET_103;
	    CHECKSUM_OFFSET = CHECKSUM_OFFSET_103;
	}

	this.fields.put("Name", new StringField(NAME_OFFSET, NAME_LENGTH));
	this.fields.put("Nickname", 
			new StringField(NICK_OFFSET, NICK_LENGTH));
	this.fields.put("Medical", new ByteField(MED_OFFSET));
	this.fields.put("Strength", new ByteField(STR_OFFSET));
	this.fields.put("Max Health", new ByteField(MAX_HEALTH_OFFSET));
	this.fields.put("Level Inc", new ByteField(LVL_INC_OFFSET));
	this.fields.put("Health Inc", new ByteField(HEALTH_INC_OFFSET));
	this.fields.put("Agility Inc", new ByteField(AGI_INC_OFFSET));
	this.fields.put("Dexterity Inc", new ByteField(DEX_INC_OFFSET));
	this.fields.put("Wisdom Inc", new ByteField(WIS_INC_OFFSET));
	this.fields.put("Marksmanship Inc", new ByteField(MRK_INC_OFFSET));
	this.fields.put("Medical Inc", new ByteField(MED_INC_OFFSET));
	this.fields.put("Mechanical Inc", new ByteField(MEC_INC_OFFSET));
	this.fields.put("Explosives Inc", new ByteField(EXP_INC_OFFSET));
	this.fields.put("Strength Inc", new ByteField(STR_INC_OFFSET));
	this.fields.put("Leadership Inc", new ByteField(LDR_INC_OFFSET));
	this.fields.put("Kills", new ShortField(KILLS_OFFSET));
	this.fields.put("Assists", new ShortField(ASSISTS_OFFSET));
	this.fields.put("Shots Fired", new ShortField(SHOTS_FIRED_OFFSET));
	this.fields.put("Shots Hit", new ShortField(SHOTS_HIT_OFFSET));
	this.fields.put("Battles", new ShortField(BATTLES_OFFSET));
	this.fields.put("Wounds", new ShortField(WOUNDS_OFFSET));

	this.fields.put("Health", new ByteField(HEALTH_OFFSET));
	this.fields.put("Dexterity", new ByteField(DEX_OFFSET));         
	this.fields.put("Personality", new ByteField(PERSONALITY_OFFSET)); 
	this.fields.put("Skill1", new ChoiceField(
			    new ByteField(SKILL1_OFFSET), Skill.table));
	this.fields.put("Explosives", new ByteField(EXP_OFFSET));         
	this.fields.put("Skill2", new ChoiceField(
			    new ByteField(SKILL2_OFFSET), Skill.table));
	this.fields.put("Leadership", new ByteField(LDR_OFFSET));         
	this.fields.put("Level", new ByteField(LVL_OFFSET));         
	this.fields.put("Marksmanship", new ByteField(MRK_OFFSET));         
	this.fields.put("Wisdom", new ByteField(WIS_OFFSET));         

	this.fields.put("Agility", new ByteField(AGI_OFFSET));
	this.fields.put("Mechanical", new ByteField(MEC_OFFSET));
	this.fields.put("Checksum", new IntField(CHECKSUM_OFFSET));
    }

    public void decode(
	byte[] ciphertext, 
	int[] table)
    {
	// decode all Actor data
	if (this.savedGameVersion <= 99) {
	    this.data = JapeAlg.Decode(ciphertext, ciphertext.length,
				       table);
	} else {
	    this.data = new byte[ciphertext.length];
	    System.arraycopy(ciphertext, 0, this.data, 0,
			     ciphertext.length);
	}
    }
    
    public byte[] encode(
	int[] table)
    {
	// make sure checksum is valid
	this.recomputeChecksum();

	// encode all Actor data
	byte[] ciphertext;
	if (this.savedGameVersion <= 99) {
	    ciphertext = JapeAlg.Encode(this.data, this.data.length, table);
	} else {
	    ciphertext = new byte[this.data.length];
	    System.arraycopy(this.data, 0, ciphertext, 0, this.data.length);
	}
        return ciphertext;
    }

    public int computeChecksum() {
	if (this.savedGameVersion <= 99) {
	    return JapeAlg.ActorChecksum(this.data);
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

	    // Iterate through inventory
	    int invBaseOffset = 
		SaveGame.MERCPROFILESTRUCT_invsize_RelOffset_103;
	    IntField invsize = new IntField(invBaseOffset);
	    int inventorySize = invsize.getInt(this.data);
	    for(int itemIdx = 0; itemIdx < inventorySize; ++itemIdx) {
		int offset = invBaseOffset + 4 + itemIdx * 12;
		IntField inv = new IntField(offset);
		checksum += inv.getInt(this.data);

		IntField bInvNumber = new IntField(offset + 8);
		checksum += bInvNumber.getInt(this.data);
	    }

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
		") in MERCPROFILESTRUCT for '" +
		this.get("Name") + "'");
	}
    }

    public void recomputeChecksum()
    {
        int checksum = this.computeChecksum();
	this.setInt("Checksum", checksum);
    }
}
