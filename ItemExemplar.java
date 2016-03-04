/*
  Written 1999 by Douglas Greiman.
 
  This software may be used and distributed according to the terms
  of the GNU Public License, incorporated herein by reference.
*/

package duggelz.jape;

import java.util.*;

class ItemExemplar {
    // Static data
    public static final int NONE_CATEGORY = 0;
    public static final int WEAPON_CATEGORY = 1;
    public static final int MELEE_WEAPON_CATEGORY = 2;
    public static final int WEAPON_ATTACHMENT_CATEGORY = 3;
    public static final int AMMO_CATEGORY = 4;
    public static final int GRENADE_CATEGORY = 5;
    public static final int EXPLOSIVE_CATEGORY = 6;
    public static final int DETONATOR_CATEGORY = 7;
    public static final int HELMET_CATEGORY = 8;
    public static final int BODY_ARMOR_CATEGORY = 9;
    public static final int ARMOR_ATTACHMENT_CATEGORY = 10;
    public static final int LEG_ARMOR_CATEGORY = 11;
    public static final int HEAD_GEAR_CATEGORY = 12;
    public static final int MEDICAL_CATEGORY = 13;
    public static final int TOOL_CATEGORY = 14;
    public static final int USABLE_ITEM_CATEGORY = 15;
    public static final int MISC_CATEGORY = 16;
    public static final int KEY_CATEGORY = 17;
    public static final int MONEY_CATEGORY = 18;

    public static Hashtable nameTable = new Hashtable();
    public static Hashtable exemplarTable = new Hashtable();
    public static Vector nameList = new Vector();
    public static Vector ammoNameList = new Vector();
    public static Vector attachmentNameList = new Vector();

    static {
	ammoNameList.addElement("None");
	attachmentNameList.addElement("None");
    }

    static {
	new ItemExemplar(0x0000, "None", NONE_CATEGORY);
	new ItemExemplar(0x0001, "Glock 17", WEAPON_CATEGORY, 0x47, 0x49, 0x4B);
	new ItemExemplar(0x0002, "Glock 18", WEAPON_CATEGORY, 0x47, 0x49, 0x4B);
	new ItemExemplar(0x0003, "Beretta 92F", WEAPON_CATEGORY, 0x47, 0x49, 0x4B);
	new ItemExemplar(0x0004, "Beretta 92R", WEAPON_CATEGORY, 0x47, 0x49, 0x4B);
	new ItemExemplar(0x0005, ".38 Special", WEAPON_CATEGORY, 0x4D, 0x4E, 0x4F);
	new ItemExemplar(0x0006, "Barracuda", WEAPON_CATEGORY, 0x56, 0x58, 0x5A);
	new ItemExemplar(0x0007, "Desert Eagle", WEAPON_CATEGORY, 0x57, 0x59, 0x5B);
	new ItemExemplar(0x0008, "Colt .45", WEAPON_CATEGORY, 0x50, 0x52, 0x54);
	new ItemExemplar(0x0009, "H&K MP5K", WEAPON_CATEGORY, 0x48, 0x4A, 0x4C);
	new ItemExemplar(0x000A, "MAC-10", WEAPON_CATEGORY, 0x51, 0x53, 0x55);
	new ItemExemplar(0x000B, "Thompson M1A1", WEAPON_CATEGORY, 0x51, 0x53, 0x55);
	new ItemExemplar(0x000C, "Colt Commando", WEAPON_CATEGORY, 0x5E, 0x5F);
	new ItemExemplar(0x000D, "H&K MP53", WEAPON_CATEGORY, 0x5E, 0x5F);
	new ItemExemplar(0x000E, "AKSU-74", WEAPON_CATEGORY, 0x5C, 0x5D);
	new ItemExemplar(0x000F, "FN-P90", WEAPON_CATEGORY, 0x69, 0x6A);
	new ItemExemplar(0x0010, "Type-85", WEAPON_CATEGORY, 0x60, 0x62);
	new ItemExemplar(0x0011, "SKS", WEAPON_CATEGORY, 0x60, 0x62);
	new ItemExemplar(0x0012, "Dragunov", WEAPON_CATEGORY, 0x60, 0x62);
	new ItemExemplar(0x0013, "M24", WEAPON_CATEGORY, 0x64, 0x66);
	new ItemExemplar(0x0014, "Steyr AUG", WEAPON_CATEGORY, 0x5E, 0x5F);
	new ItemExemplar(0x0015, "H&K G41", WEAPON_CATEGORY, 0x5E, 0x5F);
	new ItemExemplar(0x0016, "Ruger Mini-14", WEAPON_CATEGORY, 0x5E, 0x5F);
	new ItemExemplar(0x0017, "C-7", WEAPON_CATEGORY, 0x5E, 0x5F);
	new ItemExemplar(0x0018, "FA-MAS", WEAPON_CATEGORY, 0x5E, 0x5F);
	new ItemExemplar(0x0019, "AK-74", WEAPON_CATEGORY, 0x5C, 0x5D);
	new ItemExemplar(0x001A, "AKM", WEAPON_CATEGORY, 0x61, 0x63);
	new ItemExemplar(0x001B, "M14", WEAPON_CATEGORY, 0x65, 0x67);
	new ItemExemplar(0x001C, "FN-FAL", WEAPON_CATEGORY, 0x65, 0x67);
	new ItemExemplar(0x001D, "H&K G3A3", WEAPON_CATEGORY, 0x65, 0x67);
	new ItemExemplar(0x001E, "H&K G11", WEAPON_CATEGORY, 0x68);
	new ItemExemplar(0x001F, "Remington M870", WEAPON_CATEGORY, 0x6B, 0x6C);
	new ItemExemplar(0x0020, "SPAS-15", WEAPON_CATEGORY, 0x6B, 0x6C);
	new ItemExemplar(0x0021, "CAWS", WEAPON_CATEGORY, 0x6D, 0x6E);
	new ItemExemplar(0x0022, "FN Minimi", WEAPON_CATEGORY, 0x5E, 0x5F);
	new ItemExemplar(0x0023, "RPK-74", WEAPON_CATEGORY, 0x5C, 0x5D);
	new ItemExemplar(0x0024, "H&K 21", WEAPON_CATEGORY, 0x65, 0x67);
	new ItemExemplar(0x0025, "Combat Knife", MELEE_WEAPON_CATEGORY);
	new ItemExemplar(0x0026, "Throwing Knife", MELEE_WEAPON_CATEGORY);
	new ItemExemplar(0x0027, "A Rock", MELEE_WEAPON_CATEGORY);
	new ItemExemplar(0x0028, "Grenade Launcher", WEAPON_CATEGORY, 0x93, 0x94, 0x95, 0x96);
	new ItemExemplar(0x0029, "Mortar", WEAPON_CATEGORY, 0x8C);
	new ItemExemplar(0x002A, "Another Rock", MELEE_WEAPON_CATEGORY);
	new ItemExemplar(0x002B, "Young Male Creature Claws", MELEE_WEAPON_CATEGORY);
	new ItemExemplar(0x002C, "Young Female Creature Claws", MELEE_WEAPON_CATEGORY);
	new ItemExemplar(0x002D, "Adult Male Creature Claws", MELEE_WEAPON_CATEGORY);
	new ItemExemplar(0x002E, "Adult Female Creature Claws", MELEE_WEAPON_CATEGORY);
	new ItemExemplar(0x002F, "Queen Tentacle", MELEE_WEAPON_CATEGORY);
	new ItemExemplar(0x0030, "Queen Creature Spit", MELEE_WEAPON_CATEGORY);
	new ItemExemplar(0x0031, "Knuckle Dusters", MELEE_WEAPON_CATEGORY);
	new ItemExemplar(0x0032, "Underslung Grenade Launcher", WEAPON_ATTACHMENT_CATEGORY);
	new ItemExemplar(0x0033, "LAW", WEAPON_CATEGORY);
	new ItemExemplar(0x0034, "Bloodcat Claws", MELEE_WEAPON_CATEGORY);
	new ItemExemplar(0x0035, "Bloodcat Bite", MELEE_WEAPON_CATEGORY);
	new ItemExemplar(0x0036, "Machete", MELEE_WEAPON_CATEGORY);
	new ItemExemplar(0x0037, "Rocket Rifle", WEAPON_CATEGORY, 0x6F, 0x70, 0x71);
	new ItemExemplar(0x0038, "Automag III", WEAPON_CATEGORY, 0x64, 0x66);
	new ItemExemplar(0x0039, "Infant Creature Spit", MELEE_WEAPON_CATEGORY);
	new ItemExemplar(0x003A, "Young Male Spit", MELEE_WEAPON_CATEGORY);
	new ItemExemplar(0x003B, "Old Male Spit", MELEE_WEAPON_CATEGORY);
	new ItemExemplar(0x003C, "Tank Cannon", WEAPON_CATEGORY);
	new ItemExemplar(0x003D, "Dart Gun", WEAPON_CATEGORY, 0x72);
	new ItemExemplar(0x003E, "Bloody Throwing Knife", MELEE_WEAPON_CATEGORY);
	new ItemExemplar(0x003F, "Flamethrower", WEAPON_CATEGORY);
	new ItemExemplar(0x0040, "Crowbar", USABLE_ITEM_CATEGORY);
	new ItemExemplar(0x0041, "Auto Rocket Rifle", WEAPON_CATEGORY, 0x6F, 0x70, 0x71);
	new ItemExemplar(0x0047, "9mm Pistol Magazine", AMMO_CATEGORY, 0x00, 15);
	new ItemExemplar(0x0048, "9mm SMG Magazine", AMMO_CATEGORY, 0x00, 30);
	new ItemExemplar(0x0049, "9mm Pistol Magazine, AP", AMMO_CATEGORY, 0x02, 15);
	new ItemExemplar(0x004A, "9mm SMG Magazine, AP", AMMO_CATEGORY, 0x02, 30);
	new ItemExemplar(0x004B, "9mm Pistol Magazine, HP", AMMO_CATEGORY, 0x01, 15);
	new ItemExemplar(0x004C, "9mm SMG Magazine, HP", AMMO_CATEGORY, 0x01, 30);
	new ItemExemplar(0x004D, ".38 Speed Loader", AMMO_CATEGORY, 0x00, 6);
	new ItemExemplar(0x004E, ".38 Speed Loader, AP", AMMO_CATEGORY, 0x02, 6);
	new ItemExemplar(0x004F, ".38 Speed Loader, HP", AMMO_CATEGORY, 0x01, 6);
	new ItemExemplar(0x0050, ".45 Pistol Magazine", AMMO_CATEGORY, 0x00, 7);
	new ItemExemplar(0x0051, ".45 SMG Magazine", AMMO_CATEGORY, 0x00, 30);
	new ItemExemplar(0x0052, ".45 Pistol Magazine, AP", AMMO_CATEGORY, 0x02, 7);
	new ItemExemplar(0x0053, ".45 SMG Magazine, AP", AMMO_CATEGORY, 0x02, 30);
	new ItemExemplar(0x0054, ".45 Pistol Magazine, HP", AMMO_CATEGORY, 0x01, 7);
	new ItemExemplar(0x0055, ".45 SMG Magazine, HP", AMMO_CATEGORY, 0x01, 30);
	new ItemExemplar(0x0056, ".357 Speed Loader", AMMO_CATEGORY, 0x00, 6);
	new ItemExemplar(0x0057, ".357 Magazine", AMMO_CATEGORY, 0x00, 9);
	new ItemExemplar(0x0058, ".357 Speed Loader, AP", AMMO_CATEGORY, 0x02, 6);
	new ItemExemplar(0x0059, ".357 Magazine, AP", AMMO_CATEGORY, 0x02, 9);
	new ItemExemplar(0x005A, ".357 Speed Loader, HP", AMMO_CATEGORY, 0x01, 6);
	new ItemExemplar(0x005B, ".357 Magazine, HP", AMMO_CATEGORY, 0x01, 9);
	new ItemExemplar(0x005C, "5.45mm Magazine", AMMO_CATEGORY, 0x02, 30);
	new ItemExemplar(0x005D, "5.45mm Magazine, HP", AMMO_CATEGORY, 0x01, 30);
	new ItemExemplar(0x005E, "5.56mm Magazine", AMMO_CATEGORY, 0x02, 30);
	new ItemExemplar(0x005F, "5.56mm Magazine, HP", AMMO_CATEGORY, 0x01, 30);
	new ItemExemplar(0x0060, "7.62mm WP Magazine, 10", AMMO_CATEGORY, 0x02, 10);
	new ItemExemplar(0x0061, "7.62mm WP Magazine, 30", AMMO_CATEGORY, 0x02, 30);
	new ItemExemplar(0x0062, "7.62mm WP Magazine, 10 HP", AMMO_CATEGORY, 0x01, 10);
	new ItemExemplar(0x0063, "7.62mm WP Magazine, 30 HP", AMMO_CATEGORY, 0x01, 30);
	new ItemExemplar(0x0064, "7.62mm NATO Magazine, 5", AMMO_CATEGORY, 0x02, 5);
	new ItemExemplar(0x0065, "7.62mm NATO Magazine, 20", AMMO_CATEGORY, 0x02, 20);
	new ItemExemplar(0x0066, "7.62mm NATO Ammunition, 5 HP", AMMO_CATEGORY, 0x01, 5);
	new ItemExemplar(0x0067, "7.62mm NATO Magazine, 20 HP", AMMO_CATEGORY, 0x01, 20);
	new ItemExemplar(0x0068, "4.7mm Magazine", AMMO_CATEGORY, 0x02, 50);
	new ItemExemplar(0x0069, "5.7mm Magazine", AMMO_CATEGORY, 0x02, 50);
	new ItemExemplar(0x006A, "5.7mm Magazine, HP", AMMO_CATEGORY, 0x01, 50);
	new ItemExemplar(0x006B, "12 Gauge Magazine", AMMO_CATEGORY, 0x03, 7);
	new ItemExemplar(0x006C, "12 Gauge Magazine, Buckshot", AMMO_CATEGORY, 0x04, 7);
	new ItemExemplar(0x006D, "CAWS Magazine", AMMO_CATEGORY, 0x03, 10);
	new ItemExemplar(0x006E, "CAWS Magazine, Buckshot", AMMO_CATEGORY, 0x04, 10);
	new ItemExemplar(0x006F, "Minirockets, AP", AMMO_CATEGORY, 0x09, 5);
	new ItemExemplar(0x0070, "Minirockets, HP", AMMO_CATEGORY, 0x08, 5);
	new ItemExemplar(0x0071, "Minirockets, HEAP", AMMO_CATEGORY, 0x0A, 5);
	new ItemExemplar(0x0072, "Tranquilizer Dart", AMMO_CATEGORY, 0x0B, 1);
	// ? 0x73
	new ItemExemplar(0x0083, "Stun Grenade", GRENADE_CATEGORY);
	new ItemExemplar(0x0084, "Tear Gas Grenade", GRENADE_CATEGORY);
	new ItemExemplar(0x0085, "Mustard Gas Grenade", GRENADE_CATEGORY);
	new ItemExemplar(0x0086, "Mini Hand Grenade", GRENADE_CATEGORY);
	new ItemExemplar(0x0087, "Reg Hand Grenade", GRENADE_CATEGORY);
	new ItemExemplar(0x0088, "A Jar of RDX Crystals", EXPLOSIVE_CATEGORY);
	new ItemExemplar(0x0089, "TNT", EXPLOSIVE_CATEGORY);
	new ItemExemplar(0x008A, "HMX", EXPLOSIVE_CATEGORY);
	new ItemExemplar(0x008B, "C1", EXPLOSIVE_CATEGORY);
	new ItemExemplar(0x008C, "Mortar Shell", GRENADE_CATEGORY);
	new ItemExemplar(0x008D, "Land Mine", EXPLOSIVE_CATEGORY);
	new ItemExemplar(0x008E, "C4", EXPLOSIVE_CATEGORY);
	new ItemExemplar(0x008F, "Trip Flare", EXPLOSIVE_CATEGORY);
	new ItemExemplar(0x0090, "Trip Klaxon", EXPLOSIVE_CATEGORY);
	new ItemExemplar(0x0091, "Shaped Charge", EXPLOSIVE_CATEGORY);
	new ItemExemplar(0x0092, "Chemical Break Light", GRENADE_CATEGORY);
	new ItemExemplar(0x0093, "40mm Grenade", GRENADE_CATEGORY);
	new ItemExemplar(0x0094, "40mm Tear Gas Grenade", GRENADE_CATEGORY);
	new ItemExemplar(0x0095, "40mm Stun Grenade", GRENADE_CATEGORY);
	new ItemExemplar(0x0096, "40mm Smoke Grenade", GRENADE_CATEGORY);
	new ItemExemplar(0x0097, "Smoke Grenade", GRENADE_CATEGORY);
	new ItemExemplar(0x0098, "Tank Shell", GRENADE_CATEGORY);
	new ItemExemplar(0x0099, "Struct Ignite", EXPLOSIVE_CATEGORY);
	new ItemExemplar(0x009A, "Creature Cocktail", GRENADE_CATEGORY);
	new ItemExemplar(0x009B, "Structure Explosion", EXPLOSIVE_CATEGORY);
	new ItemExemplar(0x009C, "Great Big Explosion", EXPLOSIVE_CATEGORY);
	new ItemExemplar(0x009D, "Big Tear Gas", GRENADE_CATEGORY);
	new ItemExemplar(0x009E, "Small Creature Gas", EXPLOSIVE_CATEGORY);
	new ItemExemplar(0x009F, "Large Creature Gas", EXPLOSIVE_CATEGORY);
	new ItemExemplar(0x00A1, "Flak Jacket", BODY_ARMOR_CATEGORY);
	new ItemExemplar(0x00A2, "Compound 18 Flak Jacket", BODY_ARMOR_CATEGORY);
	new ItemExemplar(0x00A3, "Coated Flak Jacket", BODY_ARMOR_CATEGORY);
	new ItemExemplar(0x00A4, "Kevlar Vest", BODY_ARMOR_CATEGORY);
	new ItemExemplar(0x00A5, "Compound 18 Kevlar Vest", BODY_ARMOR_CATEGORY);
	new ItemExemplar(0x00A6, "Coated Kevlar Vest", BODY_ARMOR_CATEGORY);
	new ItemExemplar(0x00A7, "Spectra Vest", BODY_ARMOR_CATEGORY);
	new ItemExemplar(0x00A8, "Compound 18 Spectra Vest", BODY_ARMOR_CATEGORY);
	new ItemExemplar(0x00A9, "Coated Spectra Vest", BODY_ARMOR_CATEGORY);
	new ItemExemplar(0x00AA, "Kevlar Leggings", LEG_ARMOR_CATEGORY);
	new ItemExemplar(0x00AB, "Compound 18 Kevlar Leggings", LEG_ARMOR_CATEGORY);
	new ItemExemplar(0x00AC, "Coated Kevlar Leggings", LEG_ARMOR_CATEGORY);
	new ItemExemplar(0x00AD, "Spectra Leggings", LEG_ARMOR_CATEGORY);
	new ItemExemplar(0x00AE, "Compound 18 Spectra Leggings", LEG_ARMOR_CATEGORY);
	new ItemExemplar(0x00AF, "Coated Spectra Leggings", LEG_ARMOR_CATEGORY);
	new ItemExemplar(0x00B0, "Steel Helmet", HELMET_CATEGORY);
	new ItemExemplar(0x00B1, "Kevlar Helmet", HELMET_CATEGORY);
	new ItemExemplar(0x00B2, "Compound 18 Kevlar Helmet", HELMET_CATEGORY);
	new ItemExemplar(0x00B3, "Coated Kevlar Helmet", HELMET_CATEGORY);
	new ItemExemplar(0x00B4, "Spectra Helmet", HELMET_CATEGORY);
	new ItemExemplar(0x00B5, "Compound 18 Spectra Helmet", HELMET_CATEGORY);
	new ItemExemplar(0x00B6, "Coated Spectra Helmet", HELMET_CATEGORY);
	new ItemExemplar(0x00B7, "Ceramic Plates", ARMOR_ATTACHMENT_CATEGORY);
	new ItemExemplar(0x00B8, "Infant Creature Hide", BODY_ARMOR_CATEGORY);
	new ItemExemplar(0x00B9, "Young Adult Creature Hide", BODY_ARMOR_CATEGORY);
	new ItemExemplar(0x00BA, "Adult Creature Hide", BODY_ARMOR_CATEGORY);
	new ItemExemplar(0x00BB, "Queen Creature Hide", BODY_ARMOR_CATEGORY);
	new ItemExemplar(0x00BC, "Leather Jacket", BODY_ARMOR_CATEGORY);
	new ItemExemplar(0x00BD, "Leather & Kevlar Jacket", BODY_ARMOR_CATEGORY);
	new ItemExemplar(0x00BE, "Compound 18 Leather & Kevlar Jacket", BODY_ARMOR_CATEGORY);
	new ItemExemplar(0x00BF, "Coated Leather & Kevlar Jacket", BODY_ARMOR_CATEGORY);
	new ItemExemplar(0x00C0, "Young Female Hide", BODY_ARMOR_CATEGORY);
	new ItemExemplar(0x00C1, "Old Female Hide", BODY_ARMOR_CATEGORY);
	new ItemExemplar(0x00C2, "Arulco T-Shirt", BODY_ARMOR_CATEGORY);
	new ItemExemplar(0x00C3, "Deidranna T-Shirt", BODY_ARMOR_CATEGORY);
	new ItemExemplar(0x00C4, "Guardian Vest", BODY_ARMOR_CATEGORY);
	new ItemExemplar(0x00C5, "Treated Guardian Vest", BODY_ARMOR_CATEGORY);
	new ItemExemplar(0x00C6, "Coated Guardian Vest", BODY_ARMOR_CATEGORY);
	new ItemExemplar(0x00C9, "First Aid Kit", MEDICAL_CATEGORY);
	new ItemExemplar(0x00CA, "Medical Kit", MEDICAL_CATEGORY);
	new ItemExemplar(0x00CB, "Tool Kit", TOOL_CATEGORY);
	new ItemExemplar(0x00CC, "Locksmith Kit", TOOL_CATEGORY);
	new ItemExemplar(0x00CD, "Camouflage Kit", USABLE_ITEM_CATEGORY);
	new ItemExemplar(0x00CE, "Boobytrap Kit", USABLE_ITEM_CATEGORY);
	new ItemExemplar(0x00CF, "Silencer", WEAPON_ATTACHMENT_CATEGORY);
	new ItemExemplar(0x00D0, "Sniper Scope", WEAPON_ATTACHMENT_CATEGORY);
	new ItemExemplar(0x00D1, "Bipod", WEAPON_ATTACHMENT_CATEGORY);
	new ItemExemplar(0x00D2, "Extended Ear", HEAD_GEAR_CATEGORY);
	new ItemExemplar(0x00D3, "Night Vision Goggles", HEAD_GEAR_CATEGORY);
	new ItemExemplar(0x00D4, "Sun Goggles", HEAD_GEAR_CATEGORY);
	new ItemExemplar(0x00D5, "Gas Mask", HEAD_GEAR_CATEGORY);
	new ItemExemplar(0x00D6, "Canteen", USABLE_ITEM_CATEGORY);
	new ItemExemplar(0x00D7, "Metal Detector", USABLE_ITEM_CATEGORY);
	new ItemExemplar(0x00D8, "Compound 18", USABLE_ITEM_CATEGORY);
	new ItemExemplar(0x00D9, "Royal Jelly", USABLE_ITEM_CATEGORY);
	new ItemExemplar(0x00DA, "Jar of Elixir", TOOL_CATEGORY);
	new ItemExemplar(0x00DB, "Money", MONEY_CATEGORY);
	new ItemExemplar(0x00DC, "Glass Jar", MISC_CATEGORY);
	new ItemExemplar(0x00DD, "Jar of Blood", MISC_CATEGORY);
	new ItemExemplar(0x00DE, "Energy Booster", MEDICAL_CATEGORY);
	new ItemExemplar(0x00DF, "Detonator", DETONATOR_CATEGORY);
	new ItemExemplar(0x00E0, "Remote Detonator", DETONATOR_CATEGORY);
	new ItemExemplar(0x00E1, "Videotape", MISC_CATEGORY);
	new ItemExemplar(0x00E2, "Deed", MISC_CATEGORY);
	new ItemExemplar(0x00E3, "Letter from Enrico", MISC_CATEGORY);
	new ItemExemplar(0x00E4, "Computer Diskette", MISC_CATEGORY);
	new ItemExemplar(0x00E5, "Chalice of Chance", MISC_CATEGORY);
	new ItemExemplar(0x00E6, "Bloodcat Claws", MISC_CATEGORY);
	new ItemExemplar(0x00E7, "Bloodcat Teeth", MISC_CATEGORY);
	new ItemExemplar(0x00E8, "Bloodcat Pelt", MISC_CATEGORY);
	new ItemExemplar(0x00E9, "Switch", MISC_CATEGORY);
	new ItemExemplar(0x00EA, "Action Item", MISC_CATEGORY);
	new ItemExemplar(0x00EB, "Regeneration Booster", MEDICAL_CATEGORY);
	new ItemExemplar(0x00EC, "Syringe", MEDICAL_CATEGORY);
	new ItemExemplar(0x00ED, "Syringe", MEDICAL_CATEGORY);
	new ItemExemplar(0x00EE, "Syringe", MEDICAL_CATEGORY);
	new ItemExemplar(0x00EF, "Jar of Human Blood", MISC_CATEGORY);
	new ItemExemplar(0x00F0, "Ownership", MISC_CATEGORY);
	new ItemExemplar(0x00F1, "Laser Scope", WEAPON_ATTACHMENT_CATEGORY);
	new ItemExemplar(0x00F2, "Remote Bomb Trigger", DETONATOR_CATEGORY);
	new ItemExemplar(0x00F3, "Wire Cutter", TOOL_CATEGORY);
	new ItemExemplar(0x00F4, "Duckbill", WEAPON_ATTACHMENT_CATEGORY);
	new ItemExemplar(0x00F5, "Bottle of Alcohol", USABLE_ITEM_CATEGORY);
	new ItemExemplar(0x00F6, "UV Goggles", HEAD_GEAR_CATEGORY);
	new ItemExemplar(0x00F8, "Head", MISC_CATEGORY);
	new ItemExemplar(0x00F9, "Imposter's Head", MISC_CATEGORY);
	new ItemExemplar(0x00FA, "T-Rex's Head", MISC_CATEGORY);
	new ItemExemplar(0x00FB, "Slay's Head", MISC_CATEGORY);
	new ItemExemplar(0x00FC, "Druggist's Head", MISC_CATEGORY);
	new ItemExemplar(0x00FD, "Matron's Head", MISC_CATEGORY);
	new ItemExemplar(0x00FE, "Tiffany's Head", MISC_CATEGORY);
	new ItemExemplar(0x00FF, "Wine", USABLE_ITEM_CATEGORY);
	new ItemExemplar(0x0100, "Beer", USABLE_ITEM_CATEGORY);
	new ItemExemplar(0x0101, "Porno Magazine", MISC_CATEGORY);
	new ItemExemplar(0x0102, "Video Camera", MISC_CATEGORY);
	new ItemExemplar(0x0103, "Robot Remote Control", HEAD_GEAR_CATEGORY);
	new ItemExemplar(0x0104, "Creature Claw", MISC_CATEGORY);
	new ItemExemplar(0x0105, "Creature Flesh", MISC_CATEGORY);
	new ItemExemplar(0x0106, "Unidentifiable Creature Organ", MISC_CATEGORY);
	new ItemExemplar(0x0107, "Remote Control", MISC_CATEGORY);
	new ItemExemplar(0x0108, "Platinum Watch", MISC_CATEGORY);
	new ItemExemplar(0x0109, "Golf Clubs", MISC_CATEGORY);
	new ItemExemplar(0x010A, "Walkman", MISC_CATEGORY);
	new ItemExemplar(0x010B, "Portable TV", MISC_CATEGORY);
	new ItemExemplar(0x010C, "Current Balance:", MISC_CATEGORY);
	new ItemExemplar(0x010D, "Cigars", MISC_CATEGORY);
	new ItemExemplar(0x010F, "Small key", KEY_CATEGORY);
	new ItemExemplar(0x0110, "Prison key", KEY_CATEGORY);
	new ItemExemplar(0x0111, "Cheap key", KEY_CATEGORY);
	new ItemExemplar(0x0112, "Dull key", KEY_CATEGORY);
	new ItemExemplar(0x0113, "Shiny key", KEY_CATEGORY);
	new ItemExemplar(0x0114, "Padlock key", KEY_CATEGORY);
	new ItemExemplar(0x0115, "Electronic Key", KEY_CATEGORY);
	new ItemExemplar(0x0116, "Keycard", KEY_CATEGORY);
	new ItemExemplar(0x0117, "Key", KEY_CATEGORY);
	new ItemExemplar(0x0118, "Key", KEY_CATEGORY);
	new ItemExemplar(0x0119, "Key", KEY_CATEGORY);
	new ItemExemplar(0x011A, "Key", KEY_CATEGORY);
	new ItemExemplar(0x011B, "Key", KEY_CATEGORY);
	new ItemExemplar(0x011C, "Key", KEY_CATEGORY);
	new ItemExemplar(0x011D, "Key", KEY_CATEGORY);
	new ItemExemplar(0x011E, "Key", KEY_CATEGORY);
	new ItemExemplar(0x011F, "Key", KEY_CATEGORY);
	new ItemExemplar(0x0120, "Key", KEY_CATEGORY);
	new ItemExemplar(0x0121, "Key", KEY_CATEGORY);
	new ItemExemplar(0x0122, "Key", KEY_CATEGORY);
	new ItemExemplar(0x0123, "Key", KEY_CATEGORY);
	new ItemExemplar(0x0124, "Key", KEY_CATEGORY);
	new ItemExemplar(0x0125, "Key", KEY_CATEGORY);
	new ItemExemplar(0x0126, "Key", KEY_CATEGORY);
	new ItemExemplar(0x0127, "Key", KEY_CATEGORY);
	new ItemExemplar(0x0128, "Key", KEY_CATEGORY);
	new ItemExemplar(0x0129, "Key", KEY_CATEGORY);
	new ItemExemplar(0x012A, "Key", KEY_CATEGORY);
	new ItemExemplar(0x012B, "Key", KEY_CATEGORY);
	new ItemExemplar(0x012C, "Key", KEY_CATEGORY);
	new ItemExemplar(0x012D, "Key", KEY_CATEGORY);
	new ItemExemplar(0x012E, "Key", KEY_CATEGORY);
	new ItemExemplar(0x012F, "Silver Platter", MISC_CATEGORY);
	new ItemExemplar(0x0130, "Duct Tape", MISC_CATEGORY);
	new ItemExemplar(0x0131, "Aluminium Rod", MISC_CATEGORY);
	new ItemExemplar(0x0132, "Spring", MISC_CATEGORY);
	new ItemExemplar(0x0133, "Rod & Spring", WEAPON_ATTACHMENT_CATEGORY);
	new ItemExemplar(0x0134, "Steel Tube", MISC_CATEGORY);
	new ItemExemplar(0x0135, "Quick Glue", MISC_CATEGORY);
	new ItemExemplar(0x0136, "Gun Barrel Extender", WEAPON_ATTACHMENT_CATEGORY);
	new ItemExemplar(0x0137, "String", MISC_CATEGORY);
	new ItemExemplar(0x0138, "Soda Can", MISC_CATEGORY);
	new ItemExemplar(0x0139, "Soda Can with String", USABLE_ITEM_CATEGORY);
	new ItemExemplar(0x013A, "Marbles", USABLE_ITEM_CATEGORY);
	new ItemExemplar(0x013B, "LameBoy Display", MISC_CATEGORY);
	new ItemExemplar(0x013C, "Copper Wire", MISC_CATEGORY);
	new ItemExemplar(0x013D, "Display Unit", MISC_CATEGORY);
	new ItemExemplar(0x013E, "FumblePak", MISC_CATEGORY);
	new ItemExemplar(0x013F, "X-ray Tube", MISC_CATEGORY);
	new ItemExemplar(0x0140, "Pack of Gum", MISC_CATEGORY);
	new ItemExemplar(0x0141, "X-ray Device", MISC_CATEGORY);
	new ItemExemplar(0x0142, "Batteries", USABLE_ITEM_CATEGORY);
	new ItemExemplar(0x0143, "Rubber Band", MISC_CATEGORY);
	new ItemExemplar(0x0144, "X-ray Detector", USABLE_ITEM_CATEGORY);
	new ItemExemplar(0x0145, "Silver Nugget", MONEY_CATEGORY);
	new ItemExemplar(0x0146, "Gold Nugget", MONEY_CATEGORY);
	new ItemExemplar(0x0147, "Tank of Gas", USABLE_ITEM_CATEGORY);
    }

    // Instance data
    public int id;
    public String name;
    public int category;
    public int ammo1;
    public int ammo2;
    public int ammo3;
    public int ammo4;
    public int ammoVariety;
    public int ammoCapacity;

    // Instance methods
    public ItemExemplar(int id, String name, int category) 
    { this(id, name, category, 0, 0, 0, 0); }
    public ItemExemplar(int id, String name, int category, int arg1) 
    { this(id, name, category, arg1, 0, 0, 0); }
    public ItemExemplar(int id, String name, int category, int arg1, int arg2) 
    { this(id, name, category, arg1, arg2, 0, 0); }
    public ItemExemplar(int id, String name, int category, int arg1, int arg2, int arg3) 
    { this(id, name, category, arg1, arg2, arg3, 0); }

    public ItemExemplar(int id, String name, int category, int arg1, int arg2, int arg3, int arg4)
    {
	this.id = id;
	this.name = name;
	this.category = category;
	switch( this.category ) {
	case WEAPON_CATEGORY:
	    this.ammo1 = arg1;
	    this.ammo2 = arg2;
	    this.ammo3 = arg3;
	    this.ammo4 = arg4;
	    break;

	case AMMO_CATEGORY:
	    this.ammoVariety = arg1;
	    this.ammoCapacity = arg2;
	    ammoNameList.addElement(this.name);
	    break;

	case WEAPON_ATTACHMENT_CATEGORY:
	    this.attachmentNameList.addElement(this.name);
	    break;

	case ARMOR_ATTACHMENT_CATEGORY:
	    this.attachmentNameList.addElement(this.name);
	    break;

	case GRENADE_CATEGORY:
	    this.attachmentNameList.addElement(this.name);
	    break;

	case DETONATOR_CATEGORY:
	    this.attachmentNameList.addElement(this.name);
	    break;

	}

	// Register item type
	this.nameTable.put(name, new Integer(id));
	this.nameTable.put(new Integer(id), name);

	this.exemplarTable.put(name, this);
	this.exemplarTable.put(new Integer(id), this);

	this.nameList.addElement(this.name);
    }
}
