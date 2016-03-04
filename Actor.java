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
    //public static final #???_INC_OFFSET    = 0x135;
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
    public static final int MAX_HEALTH_OFFSET  = 0x14e;
    public static final int DEX_OFFSET         = 0x14f;
    public static final int PERSONALITY_OFFSET = 0x150;
    public static final int SKILL1_OFFSET      = 0x151;
    public static final int EXP_OFFSET         = 0x153;
    public static final int SKILL2_OFFSET      = 0x154;
    public static final int LDR_OFFSET         = 0x155;
    public static final int LVL_OFFSET         = 0x160;
    public static final int MRK_OFFSET         = 0x161;
    public static final int WIS_OFFSET         = 0x163;
    public static final int AGI_OFFSET         = 0x195;
    public static final int MEC_OFFSET         = 0x19b;
    public static final int CHECKSUM_OFFSET    = 0x2b8;
    public static final int CHECKSUM_LENGTH    = 0x4;

    public static Hashtable classFields = new Hashtable();
    static {
	classFields.put("Name", new StringField(NAME_OFFSET, NAME_LENGTH));
	classFields.put("Nickname", new StringField(NICK_OFFSET, NICK_LENGTH));
	classFields.put("Medical", new ByteField(MED_OFFSET));
	classFields.put("Strength", new ByteField(STR_OFFSET));
	classFields.put("Level Inc", new ByteField(LVL_INC_OFFSET));
	classFields.put("Health Inc", new ByteField(HEALTH_INC_OFFSET));
	classFields.put("Agility Inc", new ByteField(AGI_INC_OFFSET));
	classFields.put("Dexterity Inc", new ByteField(DEX_INC_OFFSET));
	classFields.put("Wisdom Inc", new ByteField(WIS_INC_OFFSET));
	classFields.put("Marksmanship Inc", new ByteField(MRK_INC_OFFSET));
	classFields.put("Medical Inc", new ByteField(MED_INC_OFFSET));
	classFields.put("Mechanical Inc", new ByteField(MEC_INC_OFFSET));
	classFields.put("Explosives Inc", new ByteField(EXP_INC_OFFSET));
	classFields.put("Strength Inc", new ByteField(STR_INC_OFFSET));
	classFields.put("Leadership Inc", new ByteField(LDR_INC_OFFSET));
	classFields.put("Kills", new ShortField(KILLS_OFFSET));
	classFields.put("Assists", new ShortField(ASSISTS_OFFSET));
	classFields.put("Shots Fired", new ShortField(SHOTS_FIRED_OFFSET));
	classFields.put("Shots Hit", new ShortField(SHOTS_HIT_OFFSET));
	classFields.put("Battles", new ShortField(BATTLES_OFFSET));
	classFields.put("Wounds", new ShortField(WOUNDS_OFFSET));

	classFields.put("Max Health", new ByteField(MAX_HEALTH_OFFSET));
	classFields.put("Dexterity", new ByteField(DEX_OFFSET));         
	classFields.put("Personality", new ByteField(PERSONALITY_OFFSET)); 
	classFields.put("Skill1", new ChoiceField(new ByteField(SKILL1_OFFSET), Skill.table));
	classFields.put("Explosives", new ByteField(EXP_OFFSET));         
	classFields.put("Skill2", new ChoiceField(new ByteField(SKILL2_OFFSET), Skill.table));
	classFields.put("Leadership", new ByteField(LDR_OFFSET));         
	classFields.put("Level", new ByteField(LVL_OFFSET));         
	classFields.put("Marksmanship", new ByteField(MRK_OFFSET));         
	classFields.put("Wisdom", new ByteField(WIS_OFFSET));         
	classFields.put("Agility", new ByteField(AGI_OFFSET));         
	classFields.put("Mechanical", new ByteField(MEC_OFFSET));         
	classFields.put("Checksum", new IntField(CHECKSUM_OFFSET));    
    }

    // Instance methods
    public Actor(byte[] rawData, int[] table) {
	super(classFields);
	this.decode(rawData, table);
    }

    public void decode(
	byte[] ciphertext, 
	int[] table)
    {
        // decode all Actor data
        this.data = JapeAlg.Decode(ciphertext, ciphertext.length, table);
    }
    
    public byte[] encode(
	int[] table)
    {
	// make sure checksum is valid
	this.recomputeChecksum();

        // encode all Actor data
        byte[] ciphertext = JapeAlg.Encode(this.data, this.data.length, table);
        return ciphertext;
    }

    public void recomputeChecksum()
    {
        int checksum = JapeAlg.ActorChecksum(this.data);
	this.setInt("Checksum", checksum);
    }
}
