/*
  Written 1999 by Douglas Greiman.
 
  This software may be used and distributed according to the terms
  of the GNU Public License, incorporated herein by reference.
*/

package duggelz.jape;

import java.util.Hashtable;

// == struct OBJECTTYPE
public class Item extends BasicStructure
{
    public static final int ITEM_LENGTH                 = 0x24;

    // Static data
    public static final int ID_OFFSET                   = 0x00;
    public static final int QUANTITY_OFFSET             = 0x02;
    public static final int ITEM_1_PERCENT_OFFSET       = 0x04;

    // Non-weapons, non-armor
    public static final int ITEM_2_PERCENT_OFFSET       = 0x05;
    public static final int ITEM_3_PERCENT_OFFSET       = 0x06;
    public static final int ITEM_4_PERCENT_OFFSET       = 0x07;
    public static final int ITEM_5_PERCENT_OFFSET       = 0x08;
    public static final int ITEM_6_PERCENT_OFFSET       = 0x09;

    // Money
    public static final int MONEY_VALUE_OFFSET          = 0x08;

    // weapons, armor
    public static final int AMMO_VARIETY_OFFSET         = 0x05;
    public static final int AMMO_QUANTITY_OFFSET        = 0x06;
    public static final int AMMO_ID_OFFSET              = 0x08;
    public static final int AMMO_PERCENT_OFFSET         = 0x0A;

	// attached objects
    public static final int ATTACHMENT_1_ID_OFFSET      = 0x10;
    public static final int ATTACHMENT_2_ID_OFFSET      = 0x12;
    public static final int ATTACHMENT_3_ID_OFFSET      = 0x14;
    public static final int ATTACHMENT_4_ID_OFFSET      = 0x16;
    public static final int ATTACHMENT_1_PERCENT_OFFSET = 0x18;
    public static final int ATTACHMENT_2_PERCENT_OFFSET = 0x19;
    public static final int ATTACHMENT_3_PERCENT_OFFSET = 0x1A;
    public static final int ATTACHMENT_4_PERCENT_OFFSET = 0x1B;

    public static final int UNKNOWN_1_OFFSET            = 0x1C;
    public static final int UNKNOWN_2_OFFSET            = 0x1D;
    public static final int UNKNOWN_3_OFFSET            = 0x1E;
    public static final int UNKNOWN_4_OFFSET            = 0x1F;
    public static final int WEIGHT_OFFSET               = 0x20;

    public static Hashtable classFields = new Hashtable();
    static {
	classFields.put("Item ID", new ChoiceField(new ShortField(ID_OFFSET), ItemExemplar.nameTable));
	classFields.put("Quantity", new ShortField(QUANTITY_OFFSET));

	classFields.put("Item 1 %", new ByteField(ITEM_1_PERCENT_OFFSET));
	classFields.put("Item 2 %", new ByteField(ITEM_2_PERCENT_OFFSET));
	classFields.put("Item 3 %", new ByteField(ITEM_3_PERCENT_OFFSET));
	classFields.put("Item 4 %", new ByteField(ITEM_4_PERCENT_OFFSET));
	classFields.put("Item 5 %", new ByteField(ITEM_5_PERCENT_OFFSET));
	classFields.put("Item 6 %", new ByteField(ITEM_6_PERCENT_OFFSET));

	classFields.put("Money Value", new ShortField(MONEY_VALUE_OFFSET));

	classFields.put("Ammo Variety", new ByteField(AMMO_VARIETY_OFFSET));
	classFields.put("Ammo Quantity", new ShortField(AMMO_QUANTITY_OFFSET));
	classFields.put("Ammo ID", new ChoiceField(new ShortField(AMMO_ID_OFFSET), ItemExemplar.nameTable));
	classFields.put("Ammo %", new ByteField(AMMO_PERCENT_OFFSET));
	classFields.put("Attachment 1 ID", new ChoiceField(new ShortField(ATTACHMENT_1_ID_OFFSET), ItemExemplar.nameTable));
	classFields.put("Attachment 2 ID", new ChoiceField(new ShortField(ATTACHMENT_2_ID_OFFSET), ItemExemplar.nameTable));
	classFields.put("Attachment 3 ID", new ChoiceField(new ShortField(ATTACHMENT_3_ID_OFFSET), ItemExemplar.nameTable));
	classFields.put("Attachment 4 ID", new ChoiceField(new ShortField(ATTACHMENT_4_ID_OFFSET), ItemExemplar.nameTable));
	classFields.put("Attachment 1 %", new ByteField(ATTACHMENT_1_PERCENT_OFFSET));
	classFields.put("Attachment 2 %", new ByteField(ATTACHMENT_2_PERCENT_OFFSET));
	classFields.put("Attachment 3 %", new ByteField(ATTACHMENT_3_PERCENT_OFFSET));
	classFields.put("Attachment 4 %", new ByteField(ATTACHMENT_4_PERCENT_OFFSET));
	classFields.put("Unknown 1", new ByteField(UNKNOWN_1_OFFSET));
	classFields.put("Unknown 2", new ByteField(UNKNOWN_2_OFFSET));
	classFields.put("Unknown 3", new ByteField(UNKNOWN_3_OFFSET));
	classFields.put("Unknown 4", new ByteField(UNKNOWN_4_OFFSET));
	classFields.put("Weight", new ShortField(WEIGHT_OFFSET));
    }

    // Instance methods
    public Item(byte[] data) {
	super(classFields);
	this.data = data;
    }

    public byte[] encode() {
	return this.data;
    }

    public ItemExemplar getExemplar() {
	int fieldValue = this.getInt("Item ID");
	ItemExemplar exemplar = (ItemExemplar) ItemExemplar.exemplarTable.get(new Integer(fieldValue));
	return exemplar;
    }

    public void set(String name, String value) {
	//System.err.println("Item.set('" + name + "','" + value + "')");
	super.set(name, value);
    }

    public void setInt(String name, int value) {
	//System.err.println("Item.setInt('" + name + "','" + value + "')");
	super.setInt(name, value);
    }
}
