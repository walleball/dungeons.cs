//package Catacombs;

/**
 * Summary description for Room.
 */
public class Room
{
	private int		fNo;
	private Room[]	fPassages;
	private int		fNumPassages;
	public int[]	fPortcullis;
	public Room	    fDoor;
	private boolean fOpen;
	private boolean fLocked;
	private boolean fJammed;
	public boolean fPoisoned;

	public int		fCoins;
	public int		fKeys;
	public int		fDragonSwords;
	public int		fSerpentShields;
	public int		fArmor;
	public int		fCarpets;
    public StaffContainer fStaffs = null;
	public int		fArrows;
	public int		fAxes;
	public int		fSandals;
	public int		fWater;

	public int		fRing;

	public Chest	fChest;

	public boolean fPit;
	private int		fMuck;
	public boolean fWeb;

    public int      fShots;

	private Room[]	fHearing;
	private int[]  fHearingCount;
	private int[]  fNoiseLevel;
	private Room[]	fScent;
	private int[]  fScentCount;

    public Monster fLair = null;
	private int	fLairCount = 0;
	private int fMonsterBuffer = 0;
    private boolean fSpawn = false;

	public static final int kMaxPassages = 20;
	public static final int kExitNumber = 0;
	public static final int kMuckCount = 30;
	public static final int kScentCount = 10; // 8;	// 6

    public static final int kLairProduction = 10;

	public static final int kPortcullisNone = 0;
	public static final int kPortcullisOpen = 1;
	public static final int kPortcullisClosed = 2;

	private static final int kMaxMonsterBuffer = 200;
    
	public Room(int no)
	{
		fNo = no;
		fDoor = null; // no door
		fOpen = false;
		fLocked = true;
		fJammed = false;
		fPoisoned = false;
		
		fNumPassages = 0;
		fPassages = new Room[kMaxPassages];
		fPortcullis = new int[kMaxPassages];

		fCoins = 0;
		fKeys = 0;
		fDragonSwords = 0;
		fSerpentShields = 0;
		fArmor = 0;
		fCarpets = 0;
		fArrows = 0;
		fAxes = 0;
		fSandals = 0;
		fWater = -1;
		fRing = 0;

		fChest = null;

		fPit = false;
		fMuck = 0;
		fWeb = false;
        fShots = 0;

		fHearing = new Room[2];
		fHearingCount = new int[2];
		fNoiseLevel = new int[2];
		fScent = new Room[3];
		fScentCount = new int[3];
	}
	public void dispose()
	{
		Utility.Trace("Room.dispose");
		int i;
		for (i = 0 ; i < kMaxPassages ; ++i)
			fPassages[i] = null;
		fPassages = null;
		fPortcullis = null;
		for (i = 0 ; i < 2 ; ++i)
			fHearing[i] = null;
		fHearing = null;
		fHearingCount = null;
		fNoiseLevel = null;
		for (i = 0 ; i < 3 ; ++i)
			fScent[i] = null;
		fScent = null;
		fScentCount = null;
	}
	public int getRoomNumber()
	{
		return fNo;
	}
	public int getNumPassages()
	{
		return fNumPassages;
	}
	public Room getPassage(int no)
	{
		Utility.Assert(no > 0, "Room.getPassage - no > 0");
		Utility.Assert(no <= fNumPassages, "Room.getPassage - no <= fNumPassages");
		if (no <= 0 || no > fNumPassages)
			return null;
		return fPassages[no-1];
	}
	public Room getRandomPassage()
	{
		return this.getRandomPassage(null);
	}
	public Room getRandomPassage(Room notThis)
	{
		Room room = null;
		boolean open_door = false;
		if (this.hasDoor() && this.isDoorOpen() && !this.isExitDoor())
			open_door = true;
		int num = this.getNumPassages();
		if (open_door)
			num++;
		if (num == 0)
			return null;
		Sequence seq = new Sequence(1, num);
		while (seq.count() > 0)
// 		for (int i = 0 ; i < 3 ; ++i)
		{
			int no = seq.remove();
			if (no > this.getNumPassages())
                room = this.fDoor;
			else
			{
				if (fPortcullis[no-1] != kPortcullisClosed)
					room = this.getPassage(no);
			}
			if (room == notThis)
				room = null;
			if (room != null)
				return room;
		}
		if (notThis != null)
			return this.getRandomPassage();
		return null;
	}
    public boolean hasUnblockedPassage()
    {
        if (this.hasDoor() && this.isDoorOpen())
            return true;
        for (int i = 1 ; i <= this.getNumPassages() ; ++i)
        {
            if (!this.isBlockedByPortcullis(i))
                return true;
        }
        return false;
    }
    public void createDoor(Room room)
	{
		Utility.Assert(!this.hasDoor(), "Room.createDoor - !this.hasDoor()");
		if (this.hasDoor())
			return;
		fDoor = room;
		Utility.Assert(!this.isDoorOpen(), "Room.createDoor - !this.isDoorOpen()");
		if (this.isDoorOpen())
			this.closeDoor();
		this.removePassage(room);
	}
	public void createPortcullis(Room toRoom)
	{
		for (int i = 0 ; i < fNumPassages ; ++i)
		{
			if (fPassages[i] == toRoom)
			{
				Utility.Assert(fPortcullis[i] == kPortcullisNone, "Room.createPortcullis - creating portcullis that already exists");
				fPortcullis[i] = kPortcullisOpen;
				return;
			}
		}
		Utility.Assert(false, "Room.createPortcullis - creating portcullis in passage that does not exist");
	}
/*
	public void createExitDoor()
	{
		Utility.Assert(!this.hasDoor());
		if (this.haDoor())
			return;
		fExit = true;
		this.closeDoor();
	}
*/	
	public boolean hasDoor()
	{
		if (fDoor != null)
			return true;
//		if (fExit)
//			return true;
		return false;
	}
	public boolean isExitDoor()
	{
		Utility.Assert(this.hasDoor(), "Room.isExitDoor - this.hasDoor()");
		if (this.hasDoor())
            if (this.fDoor.isExit())
				return true;
		return false;
	}
	public boolean isDoorLocked()
	{
		Utility.Assert(this.hasDoor(), "Room.isDoorLocked - this.hasDoor()");
		return fLocked;
	}
	public void lockDoor()
	{
		Utility.Assert(this.hasDoor(), "Room.lockDoor - this.hasDoor()");
		Utility.Assert(!fLocked, "Room.lockDoor - !fLocked");
		fLocked = true;		
	}
	public void unlockDoor()
	{
		Utility.Assert(this.hasDoor(), "Room.unlockDoor - this.hasDoor()");
		Utility.Assert(fLocked, "Room.unlockDoor - fLocked");
		fLocked = false;		
	}
	public boolean isDoorOpen()
	{
		Utility.Assert(this.hasDoor(), "Room.isDoorOpen - this.hasDoor()");
		return fOpen;
	}
	public boolean isDoorJammed()
	{
		Utility.Assert(this.hasDoor(), "Room.isDoorJammed - this.hasDoor()");
		return fJammed;
	}
	public void setJammed(boolean jammed)
	{
		fJammed = jammed;
	}
	public void openDoor()
	{
		this.openDoor(false);
	}
	public void openDoor(boolean key)
	{
		Utility.Assert(this.hasDoor(), "Room.openDoor - this.hasDoor()");
		Utility.Assert(!this.isDoorOpen(), "Room.openDoor - !this.isDoorOpen()");
		Utility.Assert(!this.isDoorLocked(), "Room.openDoor - !this.isDoorLocked()");
//		Utility.Assert(!this.isDoorJammed(), "Room.openDoor - !this.isDoorJammed()");
		if (!this.hasDoor())
			return;
		fOpen = true;

		// A door is jammed open once in 10
		if (!key && GameEngine.rand.range(1, 10) == 1)
		{
			Utility.Trace("The door in room " + this.getRoomNumber() + " is now jammed open!");
			this.setJammed(true);
            this.fDoor.setJammed(true);
		}
	}
	public void closeDoor()
	{
		Utility.Assert(this.hasDoor(), "Room.closeDoor - this.hasDoor()");
		Utility.Assert(this.isDoorOpen(), "Room.closeDoor - this.isDoorOpen()");
		if (!this.hasDoor())
			return;
//		Utility.Assert(!this.isDoorJammed(), "Room.closeDoor - !this.isDoorJammed()");
//		if (this.isDoorJammed())
//			return;
		fOpen = false;
	}
	public void openPortcullis(Room toRoom)
	{
		Utility.Trace("Room " + this.getRoomNumber() + ".openPortcullis(room " + toRoom.getRoomNumber() + ")");
		for (int i = 0 ; i < fNumPassages ; ++i)
		{
			if (fPassages[i] == toRoom)
			{
				Utility.Assert(fPortcullis[i] == kPortcullisClosed, "Room.openPortcullis - opening portcullis that is not closed");
				fPortcullis[i] = kPortcullisOpen;
				return;
			}
		}
		Utility.Assert(false, "Room.openPortcullis - opening portcullis that does not exist");
	}
	public void closePortcullis(Room toRoom)
	{
		Utility.Trace("Room " + this.getRoomNumber() + ".closePortcullis(room " + toRoom.getRoomNumber() + ")");
		for (int i = 0 ; i < fNumPassages ; ++i)
		{
			if (fPassages[i] == toRoom)
			{
				Utility.Assert(fPortcullis[i] == kPortcullisOpen, "Room.closePortcullis - closing portcullis that is not open");
				fPortcullis[i] = kPortcullisClosed;
				return;
			}
		}
		Utility.Assert(false, "Room.closePortcullis - closing portcullis that does not exist");
	}
    public void setLair(Monster monster)
    {
        Utility.Assert(monster != null, "Room.setLair - monster != null");
        fLair = monster;
		if (fLairCount <= monster.getKillScore())
		{
			fLairCount++;
			fCoins++;
		}
        fSpawn = true;
    }
	public void increaseLairCount()
	{
		Utility.Assert(this.isLair(), "Room.increaseLairCount - this.isLair()");
		fLairCount++;
	}
	public void increaseMonsterBuffer()
	{
		Utility.Assert(this.isLair(), "Room.increaseMonsterBuffer - this.isLair()");
		if (fMonsterBuffer < kMaxMonsterBuffer)
			fMonsterBuffer++;
	}
    public int stopSpawn()
    {
        Utility.Assert(fLair != null, "Room.removeLair - fLair != null");
        fSpawn = false;
		return 2 * fLair.getKillScore();
    }
    public boolean isLair()
    {
		if (fLair != null)
		{
			Utility.Assert(fLairCount > 0, "Room.isLair - fLairCount > 0");
			return true;
		}
        return false;
    }
	public Monster createMonster()
	{
		Monster monster = null;
		try
		{
			monster = (Monster) fLair.getClass().newInstance();
		}
		catch (IllegalAccessException e)
		{
		}
		catch (InstantiationException e)
		{
		}
		return monster;
	}
	public boolean isExit()
	{
		if (this.getRoomNumber() == kExitNumber)
			return true;
		return false;
	}
    public void addStaff(Staff staff)
    {
        if (fStaffs == null)
        {
            fStaffs = new StaffContainer();
        }
        fStaffs.addStaff(staff);
    }
    public boolean hasStaffs()
    {
        if (fStaffs == null)
            return false;
        Utility.Assert(fStaffs.getNumStaffs() > 0, "Room.hasStaffs - fStaffs.getNumStaffs() > 0");
        return true;
    }
	public int drink(int wants)
	{
		if (fWater >= wants)
		{
			fWater -= wants;
			return wants;
		}
		else
		{
			int got = fWater;
			fWater = 0;
			return got;
		}
	}
	public boolean hasChest()
	{
		if (fChest == null)
			return false;
		return true;
	}
	public void createChest()
	{
		Utility.Assert(!this.hasChest(), "Room.createChest - !this.hasChest()");
		Utility.Assert(!this.hasDoor(), "Room.createChest - !this.hasDoor()");

		fChest = new Chest();
		// transfer all items to the chest
        fChest.fCoins = this.fCoins + GameEngine.rand.range(0, GameEngine.instance.fDungeon.fNumber); this.fCoins = 0;
        fChest.fKeys = this.fKeys; this.fKeys = 0;
        fChest.fDragonSwords = this.fDragonSwords; this.fDragonSwords = 0;
        fChest.fSerpentShields = this.fSerpentShields; this.fSerpentShields = 0;
        fChest.fArmor = this.fArmor; this.fArmor = 0;
        fChest.fCarpets = this.fCarpets; this.fCarpets = 0;
		fChest.fStaffs = this.fStaffs; this.fStaffs = null;
        fChest.fArrows = this.fArrows; this.fArrows = 0;
        fChest.fAxes = this.fAxes; this.fAxes = 0;
		fChest.fSandals = this.fSandals; this.fSandals = 0;
		fChest.fRing = this.fRing; this.fRing = 0;
	}
	public void unlockChest()
	{
		Utility.Assert(this.hasChest(), "Room.unlockChest - this.hasChest()");
        Utility.Assert(fChest.fLocked, "Room.unlockChest - fChest.isLocked()");
		fChest.fLocked = false;

		// transfer the contents to the room
		this.fCoins = this.fCoins + fChest.fCoins; fChest.fCoins = 0;
        this.fKeys = this.fKeys + fChest.fKeys; fChest.fKeys = 0;
        this.fDragonSwords = this.fDragonSwords + fChest.fDragonSwords; fChest.fDragonSwords = 0;
        this.fSerpentShields = this.fSerpentShields + fChest.fSerpentShields; fChest.fSerpentShields = 0;
        this.fArmor = fChest.fArmor; fChest.fArmor = 0;
        this.fCarpets = this.fCarpets + fChest.fCarpets; fChest.fCarpets = 0;
 		this.fStaffs = fChest.fStaffs; fChest.fStaffs = null;
        this.fArrows = this.fArrows + fChest.fArrows; fChest.fArrows = 0;
        this.fAxes = this.fAxes + fChest.fAxes; fChest.fAxes = 0;
        this.fSandals = this.fSandals + fChest.fSandals; fChest.fSandals = 0;
		this.fRing = this.fRing + fChest.fRing; fChest.fRing = 0;
	}
	public void setMuck()
	{
		fMuck = kMuckCount;
	}
	public void removeMuck()
	{
		Utility.Assert(fMuck > 0, "Room.removeMuck - fMuck > 0");
		fMuck = 0;
	}
	public boolean hasMuck()
	{
		if (fMuck > 0)
			return true;
		return false;
	}
	public int countItems()
	{
		int count = 0;
        if (this.fKeys > 0)
			count++;
        if (this.fCoins > 0)
			count++;
        if (this.fDragonSwords > 0)
			count++;
        if (this.fSerpentShields > 0)
			count++;
        if (this.fArmor > 0)
			count++;
        if (this.fCarpets > 0)
			count++;
		if (this.hasStaffs())
			count++;
        if (this.fArrows > 0)
			count++;
        if (this.fAxes > 0)
			count++;
        if (this.fSandals > 0)
			count++;
		if (this.fRing > 0)
			count++;
		return count;
	}
	public boolean hasTakeableContent()
	{
        if (this.fWater > 0)
			return true;
		if (this.countItems() > 0)
			return true;
		return false;
	}
	public void addPassage(Room room)
	{
		Utility.Assert(room != this, "Room.addPassage - room != this");
		Utility.Assert(!this.hasPassage(room), "Room.addPassage - !this.hasPassage(room)");
		Utility.Assert(fNumPassages < kMaxPassages, "Room.addPassage - fNumPassages < kMaxPassages");
		// find insertion point
		int ins;
		for (ins = 0 ; ins < fNumPassages ; ins++)
		{
			if (fPassages[ins].getRoomNumber() > room.getRoomNumber())
				break;
		}
		Utility.Assert(ins >= 0, "Room.addPassage - ins >= 0");
		Utility.Assert(ins <= fNumPassages, "Room.addPassage - ins <= fNumPassages");
		for (int i = fNumPassages ; i > ins ; i--)
		{
			fPassages[i] = fPassages[i-1];
		}

		fPassages[ins] = room;
		fNumPassages++;
//		System.Array.Sort(fPassages, 0, fNumPassages);
	}
	public void removePassage(Room room)
	{
		Utility.Assert(room != this, "Room.removePassage - room != this");
		Utility.Assert(this.hasPassage(room), "Room.removePassage - this.hasPassage(room)");
		// find removal point
		int ins;
		for (ins = 0 ; ins < fNumPassages ; ins++)
		{
			if (fPassages[ins] == room)
				break;
		}
		Utility.Assert(ins >= 0, "Room.removePassage - ins >= 0");
		Utility.Assert(ins <= fNumPassages, "Room.removePassage - ins <= fNumPassages");
		for (int i = ins  + 1; i < fNumPassages ; i++)
		{
			fPassages[i - 1] = fPassages[i];
		}
		fNumPassages--;
		fPassages[fNumPassages] = null;
		//		System.Array.Sort(fPassages, 0, fNumPassages);
	}
	/*
	public boolean hasPassage(int no)
	{
		Utility.Assert(no > 0);
		for (int iPassage = 0 ; iPassage < fNumPassages ; iPassage++)
		{
			if (fPassages[iPassage].getRoomNumber() == no)
				return true;
		}
		return false;
	}
	*/
	public boolean hasPassage(Room room)
	{
		for (int iPassage = 0 ; iPassage < fNumPassages ; iPassage++)
		{
			if (fPassages[iPassage] == room)
				return true;
		}
		return false;
	}
	public boolean isBlockedByPortcullis(Room room)
	{
		for (int iPassage = 0 ; iPassage < fNumPassages ; iPassage++)
		{
			if (fPassages[iPassage] == room)
				return this.isBlockedByPortcullis(iPassage+1);
		}
		return false;
	}
	public boolean isBlockedByPortcullis(int no)
	{
		if (fPortcullis[no-1] == kPortcullisClosed)
			return true;
		return false;
	}
	public boolean isPassable(int no)
	{
		if (this.isBlockedByPortcullis(no))
			return false;
		return true;
	}
	public boolean isPassable(Room room)
	{
		for (int iPassage = 0 ; iPassage < fNumPassages ; iPassage++)
		{
			if (fPassages[iPassage] == room)
				return this.isPassable(iPassage+1);
		}
		return false;
	}
    public void makeNoise(int noise)
    {
        this.makeNoise(noise, null);
    }
    public void makeNoise(int noise, Room source)
    {
		for (int i = 1 ; i <= this.getNumPassages() ; i++)
		{
			Room room = this.getPassage(i);
            if (room != source)
			    room.setHearing(this, noise);
		}
        if (this.hasDoor() && this.isDoorOpen())
        {
            if (this.fDoor != source)
                this.fDoor.setHearing(this, noise);
        }
	}
	public void setScent(Room room)
	{
		if (!this.hasPassage(room))
		{
			Utility.Assert(this.hasDoor(), "Room.setScent - this.hasDoor()");
			Utility.Assert(this.isDoorOpen(), "Room.setScent - this.isDoorOpen()");
            Utility.Assert(this.fDoor == room, "Room.setScent - this.getDoor() == room");
		}
		int candidate = -1;
		for (int i = 0 ; i < 3 ; i++)
		{
			if (fScentCount[i] == 0 || fScent[i] == room)
			{
				candidate = i;
				break;
			}
		}
		if (candidate == -1)
			candidate = GameEngine.rand.range(0, 2);
		fScent[candidate] = room;
		fScentCount[candidate] = kScentCount;
	}
	public void setHearing(Room room, int noise)
	{
		int candidate = -1;
		for (int i = 0 ; i < 2 ; i++)
		{
			if (fHearingCount[i] == 0 || fHearing[i] == room)
			{
				candidate = i;
				break;
			}
		}
		if (candidate == -1)
			candidate = GameEngine.rand.range(0, 1);
		fHearing[candidate] = room;
		fHearingCount[candidate] = 1;
		if (fNoiseLevel[candidate] < noise)
			fNoiseLevel[candidate] = noise;
	}
	public Room getHearCandidate(int hearing)
	{
		int cand = GameEngine.rand.range(0, 1);
		for (int i = 0; i < 2 ; i++)
		{
			if (fNoiseLevel[cand] >= hearing && fHearingCount[cand] > 0)
				return fHearing[cand];

//			if (fHearingCount[i] > 0)
//				return fHearing[i];
			cand++;
			if (cand > 1)
				cand = 0;
		}
		// Return scent candidate, but only with maximum value
		// This is to catch a silent player that just escaped to another
		// cavern
		for (int i = 0 ; i <= 2 ; i++)
		{
			if (fScentCount[i] >= kScentCount)
			{
				Utility.Assert(fScentCount[i] == kScentCount, "Room.getHearCandidate - fScentCount[i] == kScentCount");
				return fScent[i];
			}
		}

		return null;
	}
	public Room getScentCandidate()
	{
		Room room = null;
//		int count = 0;
		int cand = GameEngine.rand.range(0, 2);
		for (int i = 0; i < 3; i++)
		{
			if (fScentCount[cand] > 0)
				return fScent[cand];
//			if (fScentCount[cand] > count)
//			{
//				count = fScentCount[cand];
//				room = fScent[cand];
//			}
			cand++;
			if (cand > 2)
				cand = 0;
//			if (fScentCount[i] > 0)
//				return fScent[i];
		}
		return room;
	}
/*
	public boolean hasScentTo(Room room)
	{
		int count = 0;
		for (int i = 0 ; i < 3 ; i++)
		{
			if (fScentCount[i] > count && fScent[i] == room)
				return true;
		}
		return false;
	}
*/	
	public boolean hasScent()
	{
		int count = 0;
		for (int i = 0 ; i < 3 ; i++)
		{
			if (fScentCount[i] > count)
				return true;
		}
		return false;
	}
    public void receiveArrow()
    {
        fShots++;
    }
    public void reset()
    {
        fShots = 0;
    }
    public void process()
	{
		for (int i = 0 ; i < 2 ; i++)
		{
			if (fHearingCount[i] > 0)
			{
				fHearingCount[i]--;
				if (fHearingCount[i] == 0)
					fNoiseLevel[i] = 0;
			}
		}
		for (int i = 0 ; i < 3 ; i++)
		{
			if (fScentCount[i] > 0)
				fScentCount[i]--;
		}
		if (fMuck > 0)
			fMuck--;

        if (fSpawn && fLair != null)
        {
            int interval = kLairProduction * fLair.getKillScore();
			// halve the interval (+1) if the lair is empty
			if (!GameEngine.instance.hasMonster(this))
				interval = (interval + 1) / 2;
			if (interval < kLairProduction)
				interval = kLairProduction;
			// double the interval if the lair has but one door
			if (this.getNumPassages() == 0)
				interval *= 2;

			for (int i = 0 ; i < fLairCount ; i++)
			{
				if (GameEngine.rand.range(1, interval) == 1)
				{
					if (GameEngine.instance.canAddMonsters())
					{
						Monster monster = this.createMonster();
						monster.setRoom(this);
						this.fCoins = this.fCoins + 1;
						if (!GameEngine.instance.addMonster(monster))
							fMonsterBuffer++;
						if (GameEngine.rand.range(0, 1) == 1)  // half are stationary
							monster.setStationary(true);
						if (GameEngine.fDisplayDebugInfo)
							Utility.Assert(false, monster.getMonsterDescription() + " entered the maze in room " + this.getRoomNumber());
						Utility.Trace(monster.getMonsterDescription() + " entered the maze in room " + this.getRoomNumber());
					}
					else
						fMonsterBuffer++;
				}
			}
			if (fMonsterBuffer > 0 && (this.getNumPassages() > 0 || (this.hasDoor() && this.isDoorOpen())))
			{
				int count = fMonsterBuffer / 10 + 1;
				for (int i = 0 ; i < count ; ++i)
				{
					if (GameEngine.instance.canAddMonsters())
					{
						Utility.Trace("Entering buffered monsters in room " + this.getRoomNumber());
						Monster monster = this.createMonster();
						monster.setRoom(this);
						if (GameEngine.instance.addMonster(monster))
							fMonsterBuffer--;
						monster.setStationary(false); // none are stationary
						if (GameEngine.fDisplayDebugInfo)
							Utility.Assert(false, monster.getMonsterDescription() + " entered the maze in room " + this.getRoomNumber());
						Utility.Trace(monster.getMonsterDescription() + " entered the maze in room " + this.getRoomNumber());
					}
				}
			}
        }
	}
	public void notifyPassage(Room toRoom)
	{
		for (int i = 0 ; i < fNumPassages ; ++i)
		{
			if (fPassages[i] == toRoom)
			{
				Utility.Assert(fPortcullis[i] != kPortcullisClosed, "Room.notifyPassage - someone passed through closed portcullis");
				if (fPortcullis[i] == kPortcullisOpen)
				{
					if (GameEngine.rand.range(1, 5) == 1)
					{
						fPortcullis[i] = kPortcullisClosed;
						toRoom.closePortcullis(this);
					}
				}
				break;
			}
		}
	}
	public String getAdjacentMonsterDescription()
	{
		String str = "";
		for (int iPassage = 0 ; iPassage <= this.getNumPassages() ; iPassage++)
		{
			Room room = null;
			if (iPassage == 0)
			{
				if (!this.hasDoor() || !this.isDoorOpen())
					continue;
                room = this.fDoor;
			}
			else
				room = this.getPassage(iPassage);
			Utility.Assert(room != null, "Room.getAdjacentDescription - room != null");
            if (room.fPit)
				str += "You can feel a draft.\n";
		}
		for (int iPassage = 0 ; iPassage <= this.getNumPassages() ; iPassage++)
		{
			Room room = null;
			if (iPassage == 0)
			{
				if (!this.hasDoor() || !this.isDoorOpen())
					continue;
                room = this.fDoor;
			}
			else
				room = this.getPassage(iPassage);
			Utility.Assert(room != null, "Room.getAdjacentDescription - room != null (2)");
			if (room.hasMuck())
//				str += "It stinks of serpent-muck.\n";
				str += "It stinks in here.\n";
		}
		for (int iPassage = 0 ; iPassage <= this.getNumPassages() ; iPassage++)
		{
			Room room = null;
			if (iPassage == 0)
			{
				if (!this.hasDoor() || !this.isDoorOpen())
					continue;
                room = this.fDoor;
			}
			else
				room = this.getPassage(iPassage);
			Utility.Assert(room != null, "Room.getAdjacentDescription - room != null (5)");
            if (GameEngine.instance.fMaze.isJanitor(room) && (room.hasMuck() || room.fPit))
				//				str += "Someone is cleaning in cavern " + room.getRoomNumber() + ".\n";
				str += "Someone is cleaning nearby.\n";
		}
		for (int iPassage = 0 ; iPassage <= this.getNumPassages() ; iPassage++)
		{
			Room room = null;
			if (iPassage == 0)
			{
				if (!this.hasDoor() || !this.isDoorOpen())
					continue;
                room = this.fDoor;
			}
			else
				room = this.getPassage(iPassage);
			Utility.Assert(room != null, "Room.getAdjacentDescription - room != null (3)");
            if (room.fDragonSwords > 0 || room.fSerpentShields > 0 || room.fSandals > 0 || room.hasStaffs() || room.fRing != 0)
			{
				if (Player.fKnowsItems || Player.hasExplored(room))
				{
					// Check if the player really wants the item
					//
					boolean wants = false;
					Player player = GameEngine.instance.getCurrentPlayer();
					if (room.fDragonSwords > 0 && player.fDragonSwords < player.getMaxNumDragonSwords())
						wants = true;
					else if (room.fSerpentShields > 0 && player.fSerpentShields < player.getMaxNumSerpentShields())
						wants = true;
					else if (room.fSandals > 0 && (player.fSandals == 0))
						wants = true;
					else if (room.fRing != 0 /*&& (player.fRing == 0)*/)
						wants = true;
					else if (room.hasStaffs() && player.getMaxNumStaffs() > 0)
					{
						Staff best = room.fStaffs.getBestStaff();
						if (player.wantsStaff(best))
							wants = true;
					}

					if (wants)
						str += "You feel a potent power from cavern " + room.getRoomNumber() + ".\n";
				}
				else
					str += "You feel a potent power.\n";
			}
		}
		for (int iPassage = 0 ; iPassage <= this.getNumPassages() ; iPassage++)
		{
			Room room = null;
			if (iPassage == 0)
			{
				if (!this.hasDoor() || !this.isDoorOpen())
					continue;
                room = this.fDoor;
			}
			else
				room = this.getPassage(iPassage);
			Utility.Assert(room != null, "Room.getAdjacentDescription - room != null (4)");
		}
		return str;
	}
	public String getAdjacentPlayerDescription()
	{
		String str = "";
		for (int iPassage = 0 ; iPassage <= this.getNumPassages() ; iPassage++)
		{
			Room room = null;
			if (iPassage == 0)
			{
				if (!this.hasDoor() || !this.isDoorOpen())
					continue;
                room = this.fDoor;
			}
			else
				room = this.getPassage(iPassage);
			if (!room.isExit())
			{
				String strPlayers = "";
				Player player = GameEngine.instance.getCurrentPlayer();
                for (int iPlayer = 1; iPlayer <= GameEngine.instance.fNumPlayers; iPlayer++)
				{
					Player pl = GameEngine.instance.getPlayer(iPlayer);
                    if (pl != player && pl.isAlive() && pl.fRoom == room)
					{
						if (strPlayers != "")
							strPlayers += " ";
                        strPlayers += pl.fName;
					}
				}

				if (strPlayers != "")
					str += room.getRoomNumber() + ": " + strPlayers + "\n";
			}
		}
		
		return str;
	}
	public String getShortDescription()
	{
		String str = String.valueOf(this.getRoomNumber()) + ":";
		for (int iPassage = 1 ; iPassage <= this.getNumPassages() ; iPassage++)
			str = str + " " + this.getPassage(iPassage).getRoomNumber();
		if (this.hasDoor())
		{
			str += " (";
			if (!this.isDoorLocked())
			{
                Room door = this.fDoor;
				if (door.isExit())
					str += "exit";
				else
					str += door.getRoomNumber();
			}
			str += ")";
		}
		return str;
	}
	public String getDescription()
	{
		String str = "You are in cavern " + this.getRoomNumber() + "\n";
        if (fLair != null)
        {
			String lair = fLair.getLairDescription();
			if (fLairCount > 1)
				lair += " *" + fLairCount;
			lair += "\n";
			str += lair;
//            str += lair.toUpperCase();
        }
//		str = str + "There are passages leading to caverns:\n";
		if (this.getNumPassages() > 0)
		{
			str = str + "Passages lead to:\n";
			for (int iPassage = 1 ; iPassage <= this.getNumPassages() ; iPassage++)
			{
				if (iPassage > 1)
				{
					//				if (iPassage == this.getNumPassages())
					//					str = str + " and ";
					//				else
					//					str = str + ", ";
					str = str + " ";
				}
				str = str + this.getPassage(iPassage).getRoomNumber();
			}
			str += "\n";

			for (int iPassage = 1 ; iPassage <= this.getNumPassages() ; iPassage++)
			{
				if (!this.isPassable(iPassage))
					str += "A portcullis blocks the passage to room " + this.getPassage(iPassage).getRoomNumber() + "\n";
			}
		}
		return str;
	}
	public String getContentDescription()
	{
		String content = "";

		// pit
        if (this.fPit)
		{
			content += "A pit!\n";
		}
		// muck
		if (this.hasMuck())
		{
			content += "Serpent Muck!\n";
		}
		// muck
        if (this.fWeb)
		{
			content += "A Spider Web!\n";
		}
		// doors
		if (this.hasDoor())
		{
			if (this.isDoorOpen())
			{
				//				content += "An open door leading to ";
				if (this.isExitDoor())
					content += "An exit!\n";
				else
                    content += "A door leading to " + this.fDoor.getRoomNumber() + "\n";
			}
			else if (this.isDoorLocked())
			{
 				if (this.isExitDoor() && Player.fKnowsExits)
 					content += "A locked exit!\n";
 				else
					content += "A locked door\n";
			}
			else
			{
				content += "A closed door\n";
			}
		}
		// chest
		if (this.hasChest())
		{
            if (this.fChest.fLocked)
				content += "A locked chest\n";
			else
				content += "An open chest\n";
		}
		// water 
        if (this.fWater >= 0)
		{
            if (this.fWater > 0)
				content += "A Fountain of Water\n";
			else
				content += "A dried up Fountain\n";
		}
		// coins
        if (this.fCoins > 0)
		{
            content += Utility.numToStr(this.fCoins, "A Gold Coin", "Gold Coins") + "\n";
		}
		// keys
        if (this.fKeys > 0)
		{
            content += Utility.numToStr(this.fKeys, "A Key", "Keys") + "\n";
		}
		// dragon swords
        if (this.fDragonSwords > 0)
		{
            content += Utility.numToStr(this.fDragonSwords, "A Dragon Sword", "Dragon Swords") + "\n";
		}
		// serpent shields
        if (this.fSerpentShields > 0)
		{
            content += Utility.numToStr(this.fSerpentShields, "A Serpent Shield", "Serpent Shields") + "\n";
		}
		// armor
        if (this.fArmor > 0)
		{
            content += Player.ARMOR_STRINGS[this.fArmor] + "\n";
		}
		// carpets
        if (this.fCarpets > 0)
		{
            content += Utility.numToStr(this.fCarpets, "A Flying Carpet", "Flying Carpets") + "\n";
		}
		// staffs
		if (this.hasStaffs())
		{
            content += this.fStaffs.getVerboseDescription();
		}
		// arrows
        if (this.fArrows > 0)
		{
            content += Utility.numToStr(this.fArrows, "An Arrow", "Arrows") + "\n";
		}
		// axes
        if (this.fAxes > 0)
		{
            content += Utility.numToStr(this.fAxes, "An Axe", "Axes") + "\n";
		}
		// sandals
        if (this.fSandals > 0)
		{
            Utility.Assert(this.fSandals == 1, "Room.getContentDescription - this.getSandals() == 1");
			content += "The Sandals of Silence\n";
		}
		// ring
		if (this.fRing > 0)
		{
// 			if (Player.fKnowsItems)
 				content += Player.GetRingDescription(this.fRing) + "\n";
 //			else
//				content += "A Magical Ring\n";
		}

		return content;
	}
	public boolean hasDraft()
	{
		for (int iPassage = 0 ; iPassage <= this.getNumPassages() ; iPassage++)
		{
			Room room = null;
			if (iPassage == 0)
			{
				if (!this.hasDoor() || !this.isDoorOpen())
					continue;
                room = this.fDoor;
			}
			else
				room = this.getPassage(iPassage);
			Utility.Assert(room != null, "Room.hasDraft - room != null");
            if (room.fPit)
				return true;
		}
		return false;
	}
	public boolean hasStink()
	{
		for (int iPassage = 0 ; iPassage <= this.getNumPassages() ; iPassage++)
		{
			Room room = null;
			if (iPassage == 0)
			{
				if (!this.hasDoor() || !this.isDoorOpen())
					continue;
                room = this.fDoor;
			}
			else
				room = this.getPassage(iPassage);
			Utility.Assert(room != null, "Room.hasStink - room != null (2)");
			if (room.hasMuck())
				//				str += "It stinks of serpent-muck.\n";
				return true;
		}
		return false;
	}
	public String toString()
	{
		String str = "Room " + this.getRoomNumber() + " Exits (";
		for (int iPassage = 1 ; iPassage <= this.getNumPassages() ; iPassage++)
		{
			if (iPassage > 1)
				str = str + ", ";
			str = str + this.getPassage(iPassage).getRoomNumber();
		}
		str = str + ")";
		if (this.hasDoor())
		{
			str += " door: ";
            Room door = this.fDoor;
			if (door == null)
				str += "exit";
			else
				str += door.getRoomNumber();
		}
//		str += "\n";
		return str;
	}
}
