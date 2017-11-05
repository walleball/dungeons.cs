//package Catacombs;

/**
 * Summary description for Player.
 */
public class Player
{
	public Room	fRoom;
	public Room	fLastRoom;
//	private boolean[] fVisitedRooms;
	public int		fExperienceLevel;

	public PlayerRecord fPlayerRecord;

	public int		fRace;
	public String	fName;
	//	private boolean	fEscaped;
	public int		fHitPoints;
	private int		fMagic;
	private int		fLockPick;
	private int		fStrength;
	//	private int		fDragonSlayer;
	private int		fNoiseLevel;
	public int[]	fWounds = new int[kMaxWounds];

	public int		fCoins;
	public int		fKeys;
	public int		fDragonSwords;
	public int		fSerpentShields;
	public int		fArmor;
	public int		fCarpets;
// 	private int		fStaffs;
	public int		fArrows;
	public int		fAxes;
	public int		fWater;
	public int		fSandals;
    public int      fRing;

    public int      fArrowHits;

	private int		fAttacks;	// fighting
	private int		fShots;		// arrows
	private int		fBolts;		// staffs
	private int		fBalls;		// staffs
	private int		fMaxNumStaffs;
	private int		fAttackCount;
	private int		fShotCount;

	public int		fLastTarget;

	private int		fParalyzed;

	public int fSwordCount;
	public int fShieldCount;
	public int fArmorCount;

	public int fScore;
	public static boolean	fKnowsExits = false;
	public static boolean	fKnowsFountains = false;
	public static boolean	fKnowsItems = false;
	public static boolean  fKnowsCarpets = false;
	public static boolean  fKnowsLair = false;

	public boolean  fMuckWarned = false;
	public boolean  fPitWarned = false;

//	private int[]	fStaffCharges = new int[kMaxNumStaffs];
    private StaffContainer fStaffs = new StaffContainer(); 

	//	public static final int SLAYER_NOT = 0;
	//	public static final int SLAYER_SWORD = 1;
	//	public static final int SLAYER = 2;

	public static final int HOBBIT = 1;
	public static final int HUMAN = 2;
	public static final int ELF = 3;
	public static final int DWARF = 4;
	public static final int MAGICIAN = 5;

	//	public static int sNumber = 1;
	public static final int kMaxNumStaffs = 16;
	public static final int kFightNoise = 10;

	public static final int kSwordCount = 6;
	public static final int kShieldCount = 5;	// 8 was too high
	public static final int kArmorCountMultiplier = 3;

	public static final int kWizardLevel = 13;
	public static final int kHitPointBonusLevel = 3;
	public static final int kNumCommands = 5;
	public String[] fCommands = new String[kNumCommands];

	public static int kMaxWounds = 10;

	static int[] LEVEL_THRESHOLD = { 20, 40, 45, 25, 50 };
//	static int[] LEVEL_THRESHOLD = { 10, 20, 25, 15, 30 };

	public static final int ARMOR_NONE = 0;
	public static final int ARMOR_CLOTH = 1;
	public static final int ARMOR_LEATHER = 2;
	public static final int ARMOR_CHAIN = 3;
	public static final int ARMOR_PLATE = 4;
	public static final int ARMOR_SCALE = 5;
	public static final int ARMOR_MAX = 5;

	static int[] MAX_ARMOR = { 2, 4, 3, 5, 1 };
	static String[] ARMOR_STRINGS = { "No armor", "Clothing", "Leather Armor", "Chain Mail", "Plate Mail", "Dragon Scale Armor" };

    public static final int RING_NONE = 0;
    public static final int RING_GREED = 1;
    public static final int RING_CURIOSITY = 2;
    public static final int RING_BLOODLUST = 3;
    public static final int RING_HEORISM = 4;
    public static final int RING_HEALING = 5;
    public static final int RING_TRUEAIM = 6;
    public static final int RING_STRENGTH = 7;
    public static final int RING_ARMOR = 8;
    public static final int RING_SKILL = 9;
    public static final int RING_MAGIC = 10;
    public static final int RING_MAX = 10;

    static String[] RING_STRINGS = { "No ring", "Greed", "Curiosity", "Blood Lust", "Heroism", "Healing", "True Aim", "Strength", "Protection", "Skill", "Magic" };

	static int[][] HITPOINTS =
		{
			{2,3,4,4,5,5,6,6,7,7,8,10,12,15},
			{5,6,7,8,9,10,11,12,14,16,18,21,25,30},
			{3,4,5,5,6,7,8,9,10,11,12,15,19,25},
			{6,8,9,10,12,14,16,18,20,22,25,30,40,50},
			{2,3,4,5,6,7,8,8,9,9,10,12,15,20}
// 			{2,3,4,4,5,5,6,6,7,7,8},
// 			{5,6,7,8,9,10,11,12,14,16,18},
// 			{3,4,5,5,6,7,8,9,10,11,12},
// 			{6,8,9,10,12,14,16,18,20,22,25},
// 			{2,3,4,5,6,7,8,8,9,9,10}
		};
	static int[][] ATTACKS =
		{
			{-5,-4,-3,-2,1,1,1,2,2,2,2,2,2,3},
			{1,1,2,2,2,3,3,3,4,4,5,5,6,6},
			{-2,-2,-2,1,1,1,2,2,2,3,3,3,4,4},
			{-2,-2,1,1,1,2,2,2,2,2,3,3,3,4},
			{-5,-4,-3,-2,-2,1,1,1,1,1,2,2,2,2}
// 			{-5,-4,-3,-2,1,1,1,2,2,2,2},
// 			{1,1,2,2,2,3,3,3,4,4,5},
// 			{-2,-2,-2,1,1,1,2,2,2,3,3},
// 			{-2,-2,1,1,1,2,2,2,2,2,3},
// 			{-5,-4,-3,-2,-2,1,1,1,1,1,2}
		};
	static int[][] SHOTS =
		{
			{-3,-2,1,1,1,2,2,2,2,3,3,3,4,4},
			{-4,-3,-2,1,1,2,2,2,2,3,3,3,3,4},
			{1,1,2,2,2,3,3,3,4,4,5,5,6,7},
			{0,-4,-3,-2,1,1,2,2,2,2,2,3,3,3},
			{0,0,-4,-3,-3,-2,-2,1,1,1,2,2,2,3}
// 			{-3,-2,1,1,1,2,2,2,2,3,3},
// 			{-4,-3,-2,1,1,2,2,2,2,3,3},
// 			{1,1,2,2,2,3,3,4,4,4,5},
// 			{0,-4,-3,-2,1,1,2,2,2,2,2},
// 			{0,0,-4,-3,-3,-2,-2,1,1,1,2}
		};
	static int[][] LOCKPICK =
		{
			{1,1,2,2,3,3,4,4,5,5,6,6,6,7},
			{0,0,0,0,0,1,1,1,1,1,2,2,3,3},
			{0,0,0,0,0,0,0,0,0,0,0,0,0,1},
			{0,0,0,1,1,1,2,2,3,3,4,4,5,5},
			{0,0,0,0,0,0,0,0,0,0,0,0,1,1}
// 			{1,1,2,2,3,3,4,4,5,5,6},
// 			{0,0,0,0,0,1,1,1,1,1,2},
// 			{0,0,0,0,0,0,0,0,0,0,0},
// 			{0,0,0,1,1,1,2,2,3,3,4},
// 			{0,0,0,0,0,0,0,0,0,0,0}
		};
	static int[][] STRENGTH =
		{
			{0,0,0,0,0,0,0,0,0,0,1,1,1,2},
			{0,0,0,0,0,1,1,1,1,2,2,2,2,3},
			{0,0,0,0,0,0,0,0,1,1,1,1,2,2},
			{0,0,1,1,1,2,2,2,2,3,3,3,3,4},
			{0,0,0,0,0,0,0,0,0,1,1,1,1,1}
// 			{0,0,0,0,0,0,0,0,0,0,1},
// 			{0,0,0,0,0,1,1,1,1,1,2},
// 			{0,0,0,0,0,0,0,0,1,1,1},
// 			{0,0,1,1,1,2,2,2,2,3,3},
// 			{0,0,0,0,0,0,0,0,0,0,0}
		};
	static int[][] MAGIC =
		{
			{1,1,1,1,2,2,2,2,3,3,3,3,3,4},
			{0,0,0,0,0,0,0,0,1,1,1,1,2,2},
			{1,2,2,2,2,2,3,3,3,4,4,4,4,5},
			{0,0,0,0,0,0,0,0,0,0,1,1,1,2},
			{2,2,3,3,3,4,4,4,5,5,5,5,5,5}
// 			{1,1,1,1,2,2,2,2,3,3,3},
// 			{0,0,0,0,0,0,0,0,1,1,1},
// 			{1,2,2,2,2,2,3,3,3,4,4},
// 			{0,0,0,0,0,0,0,0,0,0,0},
// 			{2,2,3,3,3,4,4,4,5,5,5}
		};
	static int[][] BALLS =
		{
			{1,1,1,1,1,1,1,2,2,2,2,2,2,3},
			{0,0,0,1,1,1,1,1,2,2,2,2,2,3},
			{0,0,0,1,1,1,1,2,2,2,2,2,2,3},
			{0,0,0,0,0,1,1,1,1,1,1,1,2,2},
			{0,0,1,1,2,2,3,3,4,4,5,5,6,7}
// 			{1,1,1,1,1,1,1,2,2,2,2},
// 			{0,0,0,1,1,1,1,1,2,2,2},
// 			{0,0,0,1,1,1,1,2,2,2,2},
// 			{0,0,0,0,0,1,1,1,1,1,1},
// 			{0,0,1,1,2,2,2,3,3,3,4,}
		};
	static int[][] BOLTS =
		{
			{1,1,1,2,2,2,2,2,2,2,3,3,3,4},
			{0,0,1,1,1,1,2,2,2,2,2,3,3,3},
			{0,1,1,1,1,2,2,2,2,3,3,3,3,4},
			{0,0,1,1,1,1,1,1,1,1,2,2,2,2},
			{1,1,1,2,2,3,3,4,4,5,5,6,7,8}
// 			{1,1,1,2,2,2,2,2,2,2,2,},
// 			{0,0,1,1,1,1,2,2,2,2,2,},
// 			{0,1,1,1,1,2,2,2,2,3,3,},
// 			{0,0,1,1,1,1,1,1,1,1,2,},
// 			{1,1,1,2,2,3,3,4,4,5,5,}
		};
	static int[][] MAX_STAFFS =
		{
			{1,1,2,2,3,3,4,4,5,6,7,8,9,9},
			{0,0,1,1,2,3,3,4,5,5,6,7,8,9},
			{0,1,2,2,3,3,4,4,5,5,6,7,8,8},
			{0,0,1,1,2,2,2,3,3,4,4,5,6,7},
			{1,1,2,3,4,5,5,6,7,8,9,10,12,14}
// 			{1,1,2,2,3,3,4,4,5,5,6,},
// 			{0,0,1,1,2,3,3,4,5,5,6,},
// 			{0,1,2,2,3,3,4,4,5,5,6,},
// 			{0,0,1,1,2,2,2,3,3,4,4,},
// 			{1,1,2,3,4,5,5,6,6,7,8}
		};
	//static int[][] AXES =
	//    {
	//        {0,0,0,0,0,0,0,0,1,1,1,},
	//        {1,1,1,1,1,2,2,2,2,2,3,},
	//        {0,0,0,0,0,0,0,0,0,0,1,},
	//        {0,0,0,0,0,1,1,1,1,1,2,},
	//        {0,0,0,1,1,1,1,2,2,2,2,}
	//    };
	//static int[][] ARROWS =
	//    {
	//        {3,4,5,6,7,8,9,10,11,13,15},
	//        {0,1,2,3,4,5,6,7,8,9,10},
	//        {5,6,7,8,9,10,12,14,16,18,20},
	//        {0,1,2,3,4,5,6,7,8,9,10},
	//        {0,0,1,1,2,2,3,3,4,4,5}
	//    };

	private static boolean[] sVisitedRooms = new boolean[GameEngine.kMaxNumRooms];

	public Player(PlayerRecord rec, Room room)
	{
		fCommands[4] = "Move";
		rec.heal();
		fPlayerRecord = rec;

		boolean cheat = false; // cheat!

		fRace = rec.fRace;
		fWounds = rec.fWounds;

		Utility.Assert(fRace >= 1 && fRace <= 5, "Player.Player - fRace >= 1 && fRace <= 5");

		fRoom = room;
		fLastRoom = room;
		this.setVisited(room);
		fExperienceLevel = 1;

		//		fDragonSlayer = SLAYER_NOT;
// 		for (int i = 0 ; i < kMaxNumStaffs ; i++)
// 			fStaffCharges[i] = kDefaultCharge;

		fCoins = 0;
		fKeys = 0;
		fDragonSwords = 0;
		fSerpentShields = 0;
		fArmor = 0;
		fCarpets = 0;
// 		fStaffs = 0;
		fArrows = 5;
		fAxes = 0;
		fWater = 20;
		fSandals = 0;
        fRing = RING_NONE;

        fArrowHits = 0;

		fAttacks = 1;
		fShots = 1;
		fBolts = 1;
		fBalls = 1;
		fMaxNumStaffs = 1;
		fLockPick = 0;
		fStrength = 0;
		fNoiseLevel = 2;

		fLastTarget = 0;

		fParalyzed = 0;

		fSwordCount = kSwordCount;
		fShieldCount = kShieldCount;

		fScore = 0;

		if (fRace == HOBBIT)
		{
			fNoiseLevel = 1;
		}


		// apply stuff from the record
		//				fPlayers[no].setNumber(i + 1);
        this.fName = rec.fName;
        this.fCoins = rec.fCoins;
		this.updateScore(rec.fXP);

        this.fDragonSwords = rec.fSwords;
		this.fSerpentShields = rec.fShields;
		this.fArmor = rec.fArmor;
		this.fArrows = rec.fArrows;

		this.fCarpets = rec.fCarpets;
		this.fSandals = rec.fSandals;
		this.fKeys = rec.fKeys;
		this.fAxes = rec.fAxes;

		this.fRing = rec.fRing;

		this.applyExperience();

		this.setStaffs(rec.fStaffs);

        this.fHitPoints = rec.fHP;

		fAttackCount = 0;
		fShotCount = 0;
		fArmorCount = kArmorCountMultiplier * fArmor;

		if (cheat)
		{
			fPlayerRecord.fXP = 1000;
			fCarpets = 1;
			fHitPoints = 100;
			fWater = 100;
			fCoins = 500;
			fKeys = 10;
			fAxes = 2;
			fRing = 1023;
			Staff bronze = new Staff(this, Staff.BRONZE);
			Staff silver = new Staff(this, Staff.SILVER);
			Staff gold = new Staff(this, Staff.GOLD);
/*			fStaffs.addStaff(silver);
			fStaffs.addStaff(gold);
			fStaffs.addStaff(bronze);
			fStaffs.addStaff(new Staff(this, Staff.SILVER));
			fStaffs.addStaff(new Staff(this, Staff.BRONZE));
			fStaffs.addStaff(new Staff(this, Staff.BRONZE));
			Utility.Trace(fStaffs.toString());
			this.splinterStaff(gold);
			Utility.Trace(fStaffs.toString());
*/
			//             fStaffs = 3;
			fSandals = 1;
		}

//		if (fCoins < 5)
//			fCoins = 5;
	}
	public void dispose()
	{
		Utility.Trace("Player.dispose");
		int i;
		for (i = 0 ; i < kNumCommands ; ++i)
			fCommands[i] = null;
		fCommands = null;
// 		fStaffCharges = null;
	}
	public int getScore()
	{
		return fScore;
	}
	public void updateScore(int score)
	{
		fScore += score;
	}
	public int getStrength()
	{
		int strength = fStrength;
		if (RingHasCapability(fRing, RING_STRENGTH))
			strength++;
		if (RingHasCapability(fRing, RING_BLOODLUST))
			strength++;
		return strength;
	}
	public int getMagic()
	{
		int magic = fMagic;
		if (RingHasCapability(fRing, RING_MAGIC))
			magic++;
		return magic;
	}
	public int getLockPick()
	{
		if (this.isWaiting() && fLockPick > 0)
			return fLockPick + 1;
		return fLockPick;
	}
	public int getNoiseLevel()
	{
		//		return fNoiseLevel;
		if (fSandals > 0)
			return fNoiseLevel - 1;
		return fNoiseLevel;
	}
	public static String raceToString(int race)
	{
		switch (race)
		{
			case MAGICIAN:
				return "Magician";
			case DWARF:
				return "Dwarf";
			case ELF:
				return "Elf";
			case HOBBIT:
				return "Hobbit";
			case HUMAN:
				return "Human";
			default:
				Utility.Assert(false, "Player.raceToString - false");
				return "unknown";
		}
	}
	static boolean RingHasCapability(int type, int cap)
	{
		if (cap <= 0 || type == 0)
			return false;
		int flag = 1 << (cap-1);
		boolean has = ((type & flag) != 0);
		return has;
	}
	static String GetRingDescription(int type)
	{
		String str = "No ring";

		for (int i = 1; i <= RING_MAX; ++i)
		{
			if (RingHasCapability(type, i))
			{
				if (str == "No ring")
					str = "A Ring of ";
				else
					str += " and ";
				str += RING_STRINGS[i];
			}
		}
		return str;
	}
	public void applyDamage(int damage)
	{
		if (damage > kMaxWounds)
			damage = kMaxWounds;
		if (damage <= 0)
			return;
		fWounds[damage-1]++;
		fHitPoints -= damage;
	}
	public void setRoom(Room room)
	{
		Utility.Assert(room != null, "Player.setRoom - room != null");
		fLastRoom = fRoom;
		fRoom = room;
		this.fWater = this.fWater - 1;
		fLastRoom.setScent(fRoom);
		this.setVisited(room);
	}
	public void process()
	{
		if (this.isAlive() && !this.isParalyzed())
		{
			// Hobbits are very silent
			this.makeNoise(this.getNoiseLevel());
		}
		this.chargeStaffs();
		fAttackCount--;
		fShotCount--;
		if (fParalyzed > 0)
		{
			fParalyzed--;
			if (fParalyzed == 0)
				fCommands[0] = "Move";
		}
	}
	public void makeNoise(int noise)
	{
		if (!this.isParalyzed())
			fRoom.makeNoise(noise);
	}
	public static boolean hasExplored(Room room)
	{
        if (GameEngine.instance.fMaze.isExplored(room))
			return true;
		return Player.hasVisited(room);
	}
	public void setVisited(Room room)
	{
		if (!room.isExit())
		{
			boolean wasVisited = sVisitedRooms[fRoom.getRoomNumber() - 1];
			sVisitedRooms[fRoom.getRoomNumber()-1] = true;
			if (!wasVisited)
                GameEngine.instance.fMaze.setExplored(room);
		}
	}
	public static boolean hasVisited(Room room)
	{
		Utility.Assert(room.getRoomNumber() <= sVisitedRooms.length, "Player.hasVisited - room.getRoomNumber() < sVisitedRooms.length");
		if (room.getRoomNumber() > sVisitedRooms.length)
			return false;
		if (room.isExit())
			return false;
		return sVisitedRooms[room.getRoomNumber() - 1];
	}
	public int getNumUnvisited(Room room)
	{
		int unvisited = 0;
		for (int i = 1 ; i <= room.getNumPassages() ; i++)
		{
			Room otherRoom = room.getPassage(i);
			if (!this.hasVisited(otherRoom))
				unvisited++;
		}
		if (room.hasDoor())
		{
            if (room.isDoorLocked() || (!room.isExitDoor() && !this.hasVisited(room.fDoor)))
				unvisited++;
		}
		return unvisited;
	}
	public boolean canMove()
	{
		if (this.isParalyzed())
			return false;
        if (this.fRoom.fWeb)
			return false;
        if (!this.fRoom.hasUnblockedPassage())
            return false;
		if (this.fRing != Player.RING_NONE)
		{
			if (Player.RingHasCapability(this.fRing, RING_GREED) && !GameEngine.instance.hasMonster(this.fRoom))
			{
				if (this.fRoom.fCoins > 0)
					return false;
				if (this.fRoom.hasChest() && this.fRoom.fChest.fLocked)
					return false;
			}
			if (!Player.fKnowsItems && Player.RingHasCapability(this.fRing, RING_CURIOSITY) && !GameEngine.instance.hasMonster(this.fRoom))
			{
				if (this.fRoom.hasDoor() && this.fRoom.isDoorLocked())
					return false;
				if (this.fRoom.hasChest() && this.fRoom.fChest.fLocked)
					return false;
			}
			if (Player.RingHasCapability(this.fRing, RING_BLOODLUST))
			{
				if (GameEngine.instance.hasMonster(this.fRoom))
					return false;
			}
			if (Player.RingHasCapability(this.fRing, RING_HEORISM) && 
					(this.canShoot() || this.canConjureLightningBolt()))
			{
				if (GameEngine.instance.hasAdjacentMonster(this.fRoom))
				{
					if (GameEngine.instance.countPlayers(this.fRoom) > 1)
						return false;
				}
			}
		}
        return (this.fWater > 0);
	}
	public boolean hasKey()
	{
        if (this.fKeys > 0)
			return true;
		return false;
	}
	public boolean canAttack()
	{
		if (this.isParalyzed())
			return false;
		if (this.getNumAttacks() > 0)
			return this.hasWeapon();
		return false;
	}
	public int getNumberOfAttacks()
	{
		int attacks = this.getNumAttacks();
		int weapons = this.getNumWeapons();
		if (attacks > weapons)
			attacks = weapons;
		return attacks;
	}
	public int getNumberOfShots()
	{
		int shots = this.getNumShots();
        int arrows = this.fArrows;
		if (shots > arrows)
			shots = arrows;
		return shots;
	}
	public int getNumberOfBalls()
	{
		int balls = this.getNumBalls();
		int staffs = this.getChargedStaffs();
		if (balls > staffs)
			balls = staffs;
		return balls;
	}
	public int getNumberOfBolts()
	{
		int bolts = this.getNumBolts();
		int staffs = this.getChargedStaffs();
		if (bolts > staffs)
			bolts = staffs;
		return bolts;
	}
	public boolean canShoot()
	{
		if (this.isParalyzed())
			return false;
		if (this.getNumberOfShots() > 0)
			return true;
		return false;
	}
	public boolean canConjureFireball()
	{
		if (this.isParalyzed())
			return false;
		if (this.getNumBalls() > 0)
			return this.hasChargedStaff();
		return false;
	}
	public boolean canConjureLightningBolt()
	{
		if (this.isParalyzed())
			return false;
		if (!this.fRoom.hasUnblockedPassage())
			return false;
		if (this.getNumBolts() > 0)
			return this.hasChargedStaff();
		return false;
	}
	public boolean canSmellMuck()
	{
        if (this.fRace == Player.HOBBIT)
			return true;
        if (this.fExperienceLevel > kWizardLevel)
			return true;
		return false;
	}
	public int getNumWeapons()
	{
        return this.fAxes + this.fDragonSwords;
	}
	private boolean hasWeapon()
	{
		if (this.getNumWeapons() > 0)
			return true;
		return false;
	}
	public int getMaxNumKeys()
	{
		return 5;
	}
	public int getMaxNumDragonSwords()
	{
		return this.getMaxNumAxes();
	}
	public int getMaxNumSerpentShields()
	{
        return this.getStrength() + 1;
	}
	public int getMaxArmor()
	{
		int armor = MAX_ARMOR[fRace-1];
		if (RingHasCapability(fRing, RING_ARMOR))
			armor++;
		return armor;
	}
	public int getStaffs()
	{
		return fStaffs.getNumStaffs();
	}
	public void setStaffs(int[] staffs)
	{
        for (int i = 0; i < staffs.length; i++)
        {
            if (staffs[i] != Staff.NONE)
                fStaffs.addStaff(new Staff(this, staffs[i]));
        }
//             fStaffs = staffs;
	}
    public void pickUpStaffs(StaffContainer staffs)
    {
        fStaffs.insertStaffs(this, staffs);
    }
    public Staff addStaff(Staff staff)
    {
		Utility.Assert(staff != null, "Player.addStaff - staff != null");
        return fStaffs.addStaff(staff);
    }
    public void splinterStaff(Staff staff)
    {
        fStaffs.removeStaff(staff);
    }
	public boolean wantsStaff(Staff staff)
	{
		Utility.Assert(staff != null, "Player.wantsStaff - staff != null");
		if (staff == null)
			return false;
		if (this.getStaffs() < this.getMaxNumStaffs())
			return true;
		Staff worst = fStaffs.getWorstStaff();
		if (worst == null)
			return false;
		if (staff.getValue() <= worst.getValue())
			return false;

		return true;
	}
	public int getMaxNumStaffs()
	{
		int num_staffs = fMaxNumStaffs;
		if (RingHasCapability(fRing, RING_MAGIC))
			num_staffs++;
		return num_staffs;
	}
    public int getMinimumCoinsForNewStaff()
    {
        if (this.getStaffs() < this.getMaxNumStaffs())
            return Staff.MinimumCost();
        return fStaffs.getCostForImprovedStaff();
    }
	public int getMaxNumAxes()
	{
		int axes = this.getMaxNumAttacks();
		if (axes < 0)
			axes = 1;
		return axes;
	}
	public int getMaxWater()
	{
		return 30 + 10 * this.getStrength();
	}
	public boolean needsContent(Room room)
	{
        if (room.fKeys > 0 && this.fKeys < this.getMaxNumKeys())
			return true;
        if (room.fCoins > 0)
			return true;
        if (room.fDragonSwords > 0 && this.fDragonSwords < this.getMaxNumDragonSwords())
			return true;
        if (room.fSerpentShields > 0 && this.fSerpentShields < this.getMaxNumSerpentShields())
			return true;
        if (room.fArmor > this.fArmor && room.fArmor <= this.getMaxArmor())
			return true;
        if (room.fCarpets > 0 && (this.fCarpets == 0))
			return true;
        if (room.hasStaffs() && this.getMaxNumStaffs() > 0)
        {
            Staff best = room.fStaffs.getBestStaff();
			if (this.wantsStaff(best))
				return true;
        }
        if (room.fArrows > 0)
			return true;
        if (room.fAxes > 0 && this.fAxes < this.getMaxNumAxes())
			return true;
        if (room.fSandals > 0 && (this.fSandals == 0))
			return true;
        if (room.fWater > 0 && this.fWater < this.getMaxWater())
			return true;
		if ((room.fRing | this.fRing) != this.fRing)
			return true;
		return false;
	}
	StaffContainer getStaffContainer()
	{
		return fStaffs;
	}
	int getChargedStaffs()
	{
        return fStaffs.getNumChargedStaffs();
	}
	boolean hasChargedStaff()
	{
		if (this.getChargedStaffs() > 0)
			return true;
		return false;
	}
	Staff useStaff()
	{
        Staff staff = fStaffs.getChargedStaff();
		Utility.Assert(staff != null, "Player.useStaff - staff != null");
		return staff;
	}
	public void chargeStaffs()
	{
        fStaffs.chargeStaffs();
	}
    public int[] getStaffArray()
    {
        return fStaffs.getStaffArray();
    }
    public String getStaffDescriptions()
    {
        return fStaffs.getDescription();
    }
	public boolean hasEscaped()
	{
		return fRoom.isExit();
	}
	public boolean isAlive()
	{
		if (fHitPoints > 0)
			return true;
		return false;
	}
	public void die()
	{
		do
		{
			this.applyDamage(GameEngine.rand.range(1, kMaxWounds));
		} while (this.isAlive());
		// transfer all items to the current room
		// nah, keep your stuff. It may come in handy when revived...
        // 		fRoom.fCoins = fRoom.fCoins + fCoins); fCoins = 0;
        // 		fRoom.fKeys = fRoom.fKeys + fKeys); fKeys = 0;
        // 		fRoom.fDragonSwords = fRoom.fDragonSwords + fDragonSwords; fDragonSwords = 0;
        // 		fRoom.fSerpentShields = fRoom.fSerpentShields + fSerpentShields); fSerpentShields = 0;
        // 		fRoom.fCarpets = fRoom.fCarpets + fCarpets; fCarpets = 0;
// 		fRoom.fStaffs = fStaffs; fStaffs = null;
    // 		fRoom.fArrows = fRoom.fArrows + fArrows; fArrows = 0;
        // 		fRoom.fAxes = fRoom.fAxes + fAxes; fAxes = 0;
        // 		fRoom.fSandals = fRoom.fSandals + fSandals; fSandals = 0;
	}
	public int getNumAttacks()
	{
		if (fAttacks >= 0)
		{
			return this.getMaxNumAttacks();
		}
		if (fAttackCount <= 0)
			return 1;
		return 0;
	}
	public int getMaxNumAttacks()
	{
		int attacks = fAttacks;
		if (RingHasCapability(fRing, RING_SKILL))
			attacks++;
		if (RingHasCapability(fRing, RING_BLOODLUST))
			attacks++;
		return attacks;
	}
	public int getNumShots()
	{
		if (fShots >= 0)
		{
			return this.getMaxNumShots();
		}
		if (fShotCount <= 0)
			return 1;
		return 0;
	}
	public int getMaxNumShots()
	{
		if (RingHasCapability(fRing, RING_SKILL))
			return fShots + 1;
		return fShots;
	}
	public int getNumBolts()
	{
		if (RingHasCapability(fRing, RING_MAGIC))
			return fBolts + 1;

		return fBolts;
	}
	public int getNumBalls()
	{
		if (RingHasCapability(fRing, RING_MAGIC))
			return fBalls + 1;
		return fBalls;
	}
	public void attack()
	{
		fAttackCount = -fAttacks;
	}
	public void shootArrow()
	{
		fShotCount = -fShots;
		this.fArrows = this.fArrows - 1;
	}
	public void setParalyzed(int turns)
	{
		fParalyzed = turns;
	}
	public boolean isParalyzed()
	{
		return (fParalyzed > 0);
	}
	public int getBorrowableCoins()
	{
		int coins = 0;
        for (int i = 1; i <= GameEngine.instance.fNumPlayers; i++)
		{
			Player someone = GameEngine.instance.getPlayer(i);
            if (someone.fRoom == this.fRoom)
                coins += someone.fCoins;
		}
		return coins;
	}
	public void borrowCoins()
	{
		int coins = this.getBorrowableCoins();
        for (int i = 1; i <= GameEngine.instance.fNumPlayers; i++)
		{
			Player someone = GameEngine.instance.getPlayer(i);
			if (someone == this)
				this.fCoins = coins;
            else if (someone.fRoom == this.fRoom)
                someone.fCoins = 0;
		}
		Utility.Assert(coins == this.getBorrowableCoins(), "Player.borrowCoins: coins == this.getBorrowableCoins()");
	}
	public boolean isWaiting()
	{
		boolean moved = false;
		for (int i = 0; i < Player.kNumCommands; i++)
		{
			if (fCommands[i] == "Move")
				moved = true;
		}
		return !moved;
	}
	public static int GetThreshold(int race)
	{
		return LEVEL_THRESHOLD[race-1];
	}
    public static int GetLevel(int race, int xp)
    {
        if (xp < 2)
            return 0;
        int threshold = Player.GetThreshold(race);
        int limit = threshold * 1000;
        int level = 1;
        while (xp >= threshold)
        {
            level++;
            xp -= threshold;
            threshold += (3 * threshold / 4); // * 1.75 
            if (threshold > limit)
                threshold = limit;
        }
        return level;
    }
    public static int NextLevel(int race, int xp)
	{
		if (xp < 2)
			return 2;
		int current = GetLevel(race, xp);
		int add = Player.GetThreshold(race) - xp % Player.GetThreshold(race);
		while (Player.GetLevel(race, xp + add) == current)
		{
			add += Player.GetThreshold(race);
		}
		return xp + add;
	}
	public void applyExperience()
	{
        int race = this.fRace;
		int xp = this.getScore();
		this.applyExperience(Player.GetLevel(race, xp));
	}
	public void applyExperience(int level)
	{
		fExperienceLevel = level;
        int race = this.fRace - 1;
		int lvl = level;
		if (lvl > Player.kWizardLevel)
			lvl = kWizardLevel;
		fHitPoints = HITPOINTS[race][lvl]/* + level-lvl */;
		fAttacks = ATTACKS[race][lvl];
		fShots = SHOTS[race][lvl];
		fLockPick = LOCKPICK[race][lvl];
		fStrength = STRENGTH[race][lvl];
		fMagic = MAGIC[race][lvl];
		fBalls = BALLS[race][lvl];
		fBolts = BOLTS[race][lvl];
		fMaxNumStaffs = MAX_STAFFS[race][lvl];
		if (level > Player.kWizardLevel)
			fMaxNumStaffs += level - Player.kWizardLevel;
		if (fMaxNumStaffs > Player.kMaxNumStaffs)
			fMaxNumStaffs = Player.kMaxNumStaffs;
	}
	protected String numberString(int num)
	{
		String str;
		if (num >= 0)
			str = "" + num;
		else
			str = "1/" + -num;
		return str;
	}
	public String getLevelDescription()
	{
		String str = "Level " + fExperienceLevel + " (" + this.getScore() + " xp)\n";
		str += "Strength: " + this.getStrength() + "\n";
		str += "Magic: ";
		str += Utility.numToStrLower(7 - this.getMagic(), "Every turn", "turns") + "\n";
		str += "Pick Lock: " + fLockPick + "\n";
		str += "Shots: " + numberString(this.getMaxNumShots()) + "\n";		
		str += "Attacks: " + numberString(this.getMaxNumAttacks()) + "\n";		
		str += "Lightnings: " + numberString(this.getNumBolts()) + "\n";		
		str += "Fireballs: " + numberString(this.getNumBalls()) + "\n";
        str += "Next level: " + Player.NextLevel(this.fRace, this.getScore()) + " xp";
		return str;
	}
	public String getWoundDescription()
	{
		String str;
		int minor = this.fWounds[0] + this.fWounds[1];
		int wounds = this.fWounds[2] + this.fWounds[3];
		int major = this.fWounds[4];
		for (int w = 5; w < this.kMaxWounds; w++)
			major += this.fWounds[w];
		if (major > 0)
			str = Utility.numToStr(major, "A grievious wound", "grievious wounds");
		else if (wounds > 0)
			str = Utility.numToStr(wounds, "A wound", "wounds");
		else if (minor > 0)
			str = Utility.numToStr(minor, "A scratch", "scratches");
		else
			str = "No wounds";
		return str;
	}
}
