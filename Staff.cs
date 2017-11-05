using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Dungeons
{
	class Staff
	{
    private static int kDefaultCharge = 6;
    public static int kMagicCharge = 6;
//     public static int kStaffCost = 25;

	private static int kLightningDamageLow = 0;
	private static int kLightningDamageHigh = 5;
	private static int kFireballDamageLow = 2;
	private static int kFireballDamageHigh = 10;

    public static int CAST_SUCCESS = 0;
    public static int CAST_FAIL = 1;
    public static int CAST_SPLINTER = 2;

    public static int NONE = 0;
    public static int WOOD = 1;
    public static int BRONZE = 2;
	public static int SILVER = 3;
	public static int GOLD = 4;
    public static int PLATINUM = 5;
    public static int PALLADIUM = 6;
    public static int MAX_STAFF = 6;

    static int[] VALUE = { 10, 25, 50, 100, 250, 500 };
    static int[] SPLINTER = { 20, 50, 100, 500, 1000, 10000 };
	public static String[] TYPE_STRING = { "none", "wood", "bronze", "silver", "gold", "platinum", "palladium" };

    private Player fOwner;
    private int fType;
    private int fCharge = kDefaultCharge;

    public Staff(Player owner, int type)
	{
        Utility.Assert(type >= WOOD && type <= MAX_STAFF, "Staff - type >= WOOD && type <= MAX_STAFF");
        fOwner = owner;
        fType = type;
	}

    public int getType()
    {
        return fType;
    }

    public void setOwner(Player owner)
    {
        fOwner = owner;
    }
    public Player getOwner()
    {
        return fOwner;
    }

    public static int TypeForMoney(int coins)
    {
		Utility.Trace("TypeForMoney - coins: " + coins);
        for (int i = MAX_STAFF ; i >= WOOD ; i--)
        {
            if (coins >= VALUE[i-1])
                return i;
        }
        return NONE;
    }
    public int getValue()
    {
        return VALUE[fType - 1];
    }
    public int getValueOfImprovedType()
    {
        if (fType == MAX_STAFF)
            return 1000000;
        return VALUE[fType];
    }
    public static int MinimumCost()
    {
        return VALUE[0];
    }
    public bool isCharged()
    {
        // TODO: Use different charge level on different types?
        if (fCharge >= Staff.kMagicCharge)
        {
            return true;
        }
        return false;
    }
	public int getLightningDamageLow()
	{
		return kLightningDamageLow; // + fType - SILVER;
	}
	public int getLightningDamageHigh()
	{
		return kLightningDamageHigh + fType - SILVER;
	}
	public int getFireballDamageLow()
	{
		return kFireballDamageLow; // + fType - SILVER;
	}
	public int getFireballDamageHigh()
	{
		return kFireballDamageHigh + fType - SILVER;
	}
    public int castSpell()
    {
		Utility.Assert(fOwner != null, "Staff.castSpell - fOwner != null");
        fCharge = fOwner.getMagic() - 1;
        int splinter = SPLINTER[fType - 1];
        if (GameEngine.rand.range(1, splinter) == splinter)
            return CAST_SPLINTER;
        return CAST_SUCCESS;
    }
    public void charge()
    {
        fCharge++;
    }
	public String toString()
	{
		String str = Staff.TYPE_STRING[this.getType()];
		if (this.isCharged())
			str += " charged";

		if (fOwner != null)
            str += " " + fOwner.fName;

		return str;
	}
	}
}
