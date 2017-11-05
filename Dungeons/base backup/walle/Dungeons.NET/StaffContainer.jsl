//package Catacombs;

/**
 * Summary description for StaffContainer.
 */
public class StaffContainer
{
	private Staff[] fStaffs = new Staff[Player.kMaxNumStaffs];
    private int fNumStaffs = 0;

	public StaffContainer()
	{
        for (int i = 0; i < fStaffs.length ; i++)
        {
            fStaffs[i] = null;
        }
	}

    public Staff getBestStaff()
    {
        return fStaffs[0];
    }
    public Staff getWorstStaff()
    {
        if (fNumStaffs == 0)
            return null;
        return fStaffs[fNumStaffs-1];
    }

    public Staff addStaff(Staff staff)
    {
		Utility.Trace("addStaff - adding " + staff.toString());
        Player owner = staff.getOwner();
//         Utility.Assert(owner != null, "StaffContainer.addStaff - owner != null)");

        Staff drop = null;
        if (owner == null)
        {
            if (fNumStaffs >= fStaffs.length)
                return staff;
        }
        else
        {
            if (fNumStaffs >= owner.getMaxNumStaffs())
            {
                Staff cheapo = this.getWorstStaff();
                if (cheapo == null)
					return staff;
				Utility.Trace("addStaff - cheapo " + cheapo.toString());
				if (cheapo.getValue() >= staff.getValue())
                {
                    return staff;
                }
                drop = fStaffs[fNumStaffs - 1];
                drop.setOwner(null);
				Utility.Trace("addStaff - drop " + drop.toString());
                fNumStaffs--;
            }
        }

        Utility.Assert(fNumStaffs < fStaffs.length, "StaffContainer.addStaff - fNumStaffs < fStaffs.length");
        if (fNumStaffs >= fStaffs.length)
            return drop;

        int ins = fNumStaffs;
        for (int i = 0; i < fNumStaffs; i++)
        {
            if (fStaffs[i].getValue() <= staff.getValue())
            {
                ins = i;
                break;
            }
        }
        Utility.Assert(ins >= 0, "StaffContainer.addStaff - ins >= 0");
        Utility.Assert(ins <= fNumStaffs, "StaffContainer.addStaff - ins <= fNumStaffs");
        for (int i = fNumStaffs; i > ins; i--)
        {
            fStaffs[i] = fStaffs[i - 1];
        }
        fStaffs[ins] = staff;
        fNumStaffs++;

        return drop;
    }

    public void insertStaffs(Player owner, StaffContainer staffs)
    {
        // StaffContainer left = new StaffContainer();
        while (true)
        {
            Staff best = staffs.getBestStaff();
            if (best == null)
                break;
			staffs.removeStaff(best);
            best.setOwner(owner);
            Staff drop = this.addStaff(best);
            if (drop != null)
            {
				staffs.addStaff(drop);
				if (drop == best)
					break;
            }
        }
    }
    public void removeStaff(Staff staff)
    {
        int remove = fNumStaffs-1;
        for (int i = 0; i < fNumStaffs; i++)
        {
            if (fStaffs[i].getValue() == staff.getValue())
            {
                remove = i;
				break;
            }
        }
		fStaffs[remove].setOwner(null);
// 		for (int i = fNumStaffs - 1; i > remove; i--)
//         {
//             fStaffs[i - 1] = fStaffs[i];
//         }
		for (int i = remove; i < fNumStaffs - 1; i++)
		{
			fStaffs[i] = fStaffs[i + 1];
		}
		fNumStaffs--;
        fStaffs[fNumStaffs] = null;
    }

    public int getNumStaffs()
    {
        return fNumStaffs;
    }

    public int getNumChargedStaffs()
    {
        int charged = 0;
        for (int i = 0; i < this.getNumStaffs(); i++)
        {
            if (fStaffs[i].isCharged())
                charged++;
        }
        return charged;
    }

    public Staff getChargedStaff()
    {
        for (int i = 0; i < fNumStaffs; i++)
        {
            if (fStaffs[i].isCharged())
            {
                return fStaffs[i];
            }
        }
        return null;
    }
    void chargeStaffs()
    {
        for (int i = 0; i < this.getNumStaffs(); i++)
        {
            fStaffs[i].charge();
        }
    }

    public int[] getStaffArray()
    {
        int[] staffs = new int[fNumStaffs];
        for (int i = 0; i < this.getNumStaffs(); i++)
        {
            staffs[i] = fStaffs[i].getType();
        }
        return staffs;
    }

    public int getCostForImprovedStaff()
    {
        if (fNumStaffs <= 0)
            return Staff.MinimumCost();

        Staff staff = fStaffs[fNumStaffs - 1];
        return staff.getValueOfImprovedType();
    }

    public String getDescription()
    {
//         String str = "";
//         for (int i = 0; i < this.getNumStaffs(); i++)
//         {
//             Staff staff = fStaffs[i];
//             str += Integer.toString(staff.getType()) + " ";
//         }
//         return str;

        int[] types = new int[Staff.PLATINUM+1];
        for (int i = 0 ; i < Staff.PLATINUM ; i++)
        {
            types[i] = 0;
        }
        for (int i = 0; i < this.getNumStaffs(); i++)
        {
            Staff staff = fStaffs[i];
            types[staff.getType()]++;
        }

        String str = "";
        if (types[Staff.PLATINUM] > 0)
        {
            str += Integer.toString(types[Staff.PLATINUM]) + " platinum ";
        }
        if (types[Staff.GOLD] > 0)
        {
            str += Integer.toString(types[Staff.GOLD]) + " gold ";
        }
        if (types[Staff.SILVER] > 0)
        {
            str += Integer.toString(types[Staff.SILVER]) + " silver ";
        }
        if (types[Staff.BRONZE] > 0)
        {
            str += Integer.toString(types[Staff.BRONZE]) + " bronze ";
        }
        if (types[Staff.WOOD] > 0)
        {
            str += Integer.toString(types[Staff.WOOD]) + " wood ";
        }
        return str;
    }

    public String getVerboseDescription()
    {
        int[] types = new int[Staff.PLATINUM+1];
        for (int i = 0 ; i < Staff.PLATINUM ; i++)
        {
            types[i] = 0;
        }
        for (int i = 0; i < this.getNumStaffs(); i++)
        {
            Staff staff = fStaffs[i];
            types[staff.getType()]++;
        }

        String str = "";
        if (types[Staff.PLATINUM] > 0)
        {
            str += Utility.numToStr(types[Staff.PLATINUM], "A Platinum Staff", "Platinum Staffs") + "\n";
        }
        if (types[Staff.GOLD] > 0)
        {
            str += Utility.numToStr(types[Staff.GOLD], "A Golden Staff", "Golden Staffs") + "\n";
        }
        if (types[Staff.SILVER] > 0)
        {
            str += Utility.numToStr(types[Staff.SILVER], "A Silver Staff", "Silver Staffs") + "\n";
        }
        if (types[Staff.BRONZE] > 0)
        {
            str += Utility.numToStr(types[Staff.BRONZE], "A Bronze Staff", "Bronze Staffs") + "\n";
        }
        if (types[Staff.WOOD] > 0)
        {
            str += Utility.numToStr(types[Staff.WOOD], "A Wooden Staff", "Wooden Staffs") + "\n";
        }
        return str;
    }

	public String toString()
	{
		String str = Utility.numToStr(fNumStaffs, "One Staff", "Staffs") + "\n";
		for (int i = 0 ; i < fNumStaffs ; i++)
		{
			str += fStaffs[i].toString() + "\n";
		}
		for (int i = fNumStaffs ; i < Player.kMaxNumStaffs ; i++)
		{
			Utility.Assert(fStaffs[i] == null, "StaffContainer.toString - fStaffs[i] == null");
		}
		return str;
	}
}
