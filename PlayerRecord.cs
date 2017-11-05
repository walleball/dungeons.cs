using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Dungeons
{
	class PlayerRecord
	{
		public String fName;
		public int fRace;
		public int fXP;
		public int fHP;
		public int[] fWounds = new int[Player.kMaxWounds];
		//     public int[] fStaffQuality = new int[Player.kMaxNumStaffs];


		public int fCoins;
		public int fSwords;
		public int fShields;
		public int fArmor;
		public int[] fStaffs = new int[Player.kMaxNumStaffs];
		public int fArrows;

		public int fCarpets;
		public int fSandals;
		public int fKeys;
		public int fAxes;
		public int fRing;

		public long fTimestamp;

		public int fMaxHP;

		//	public boolean fIncluded = false;

		public PlayerRecord(String name, int race, int xp/*, int hp*/)
		{
			fName = name;
			fRace = race;
			fXP = xp;

			int lvl = Player.GetLevel(race, xp);
			if (lvl > Player.kWizardLevel)
				lvl = Player.kWizardLevel;
			fMaxHP = Player.HITPOINTS[race - 1][lvl]/* + level-lvl */;

			int level = Player.GetLevel(race, xp);
			if (level > Player.kWizardLevel)
				fMaxHP += (level - Player.kWizardLevel) * Player.HITPOINTS[race - 1][Player.kHitPointBonusLevel];

			// 		if (fHP > fMaxHP)
			// 			fHP = fMaxHP;
			fHP = fMaxHP;

			for (int i = 0; i < Player.kMaxWounds; i++)
				fWounds[i] = 0;

			for (int i = 0; i < Player.kMaxNumStaffs; i++)
				fStaffs[i] = 0;

			fCoins = 0;
			fSwords = 0;
			fShields = 0;
			fArmor = 0;

			//int axes = Player.AXES[race-1][lvl];
			//fArrows = Player.ARROWS[race-1][lvl];
			fArrows = 0;
			// 		fStaffs = 0;
			fAxes = 0;
			//if (fRace == Player.MAGICIAN)
			//{
			//    fStaffs = axes;
			//    axes = 0;
			//}

			fCarpets = 0;
			fSandals = 0;
			fKeys = 0;
			fRing = Player.RING_NONE;

			//fAxes = axes;
			if (fRace == Player.HUMAN)
			{
				fAxes = 1;
			}
			if (fRace == Player.ELF)
			{
				fArrows = 5;
			}
			if (fRace == Player.HOBBIT)
			{
				fStaffs[0] = Staff.WOOD;
			}
			if (fRace == Player.MAGICIAN)
			{
				fStaffs[0] = Staff.GOLD;
			}
		}
		public void initialize()
		{
			fHP = fMaxHP;
			long span = DateTime.Now.Ticks - fTimestamp;
			int heal = (int)(span / (1 * 60 * 60 * 1000));
			Utility.Trace("initialize: " + heal);
			int[] wounds = new int[Player.kMaxWounds];
			for (int w = 0; w < Player.kMaxWounds; w++)
				wounds[w] = fWounds[w];

			for (int w = 0; w < Player.kMaxWounds; w++)
			{
				int wound = w + 1;
				if (wounds[w] > 0 && heal >= wound)
				{
					int healtime = heal;
					while (healtime >= wound && wound > 0)
					{
						healtime -= wound;
						wound--;
					}
					if (wound > 0)
					{
						wounds[wound - 1] += wounds[w];
					}
					wounds[w] = 0;
				}
			}
			for (int w = 0; w < Player.kMaxWounds; w++)
				fHP -= (w + 1) * wounds[w];
			Utility.Trace("rec.fHP: " + fHP);
		}
		public void heal()
		{
			fHP = fMaxHP;
			long span = DateTime.Now.Ticks - fTimestamp;
			long heal = (int)(span / (1 * 60 * 60 * 1000));
			Utility.Trace("heal: " + heal);
			for (int w = 0; w < Player.kMaxWounds; w++)
			{
				int wound = w + 1;
				if (fWounds[w] > 0 && heal >= wound)
				{
					int healtime = (int)heal;
					while (healtime >= wound && wound > 0)
					{
						healtime -= wound;
						wound--;
					}
					if (wound > 0)
					{
						fWounds[wound - 1] += fWounds[w];
					}
					fWounds[w] = 0;
				}
			}
			for (int w = 0; w < Player.kMaxWounds; w++)
				fHP -= (w + 1) * fWounds[w];
			Utility.Trace("rec.fHP: " + fHP);

			fTimestamp += heal * (1 * 60 * 60 * 1000);
		}
		public String toDisplayString()
		{
			int level = Player.GetLevel(this.fRace, this.fXP);
			String str = level + ". " + this.fName;
			if (this.fHP == this.fMaxHP)
				str += " (" + this.fHP + ")";
			else if (this.fHP < 0)
				str += " (-/" + this.fMaxHP + ")";
			else
				str += " (" + this.fHP + "/" + this.fMaxHP + ")";
			//		str += " " + Player.raceToString(this.fRace);
			return str;
		}
		public String toHealTimeString()
		{
			int level = Player.GetLevel(this.fRace, this.fXP);
			String str = level + ". " + this.fName;
			str = this.toDisplayString();
			if (this.fHP != this.fMaxHP)
			{
				long wound = 0;
				for (int w = Player.kMaxWounds; w > 0; w--)
				{
					if (fWounds[w - 1] != 0)
					{
						wound = w;
						break;
					}
				}
				long healtime = (fTimestamp + wound * 60 * 60 * 1000) - DateTime.Now.Ticks;
				if (healtime > 0)
				{
					Timespan t = new Timespan(healtime);
					str += " " + t.toString();
				}
			}
			return str;
		}
	}
}
