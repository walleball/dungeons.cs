// package Catacombs;

/**
 * Summary description for Controller.
 */
public class GameEngine
{
	public Maze	fMaze;
	private Player[] fPlayers;
	private int		fCurrentPlayer;
	public int		fNumPlayers;
	private Device	fDevice;
	private Monster[] fMonsters;
	public int		fNumMonsters;
	private int		fEncounter;
	private int		fSwordAttacks;
	private int		fMonsterAttacks;
	private static int kMaxNumMonsterAttacks = 6;
	public static RandomNumber rand = null; // new RandomNumber();
	protected static int kBalrogTurn = 300;
	public int fTurnCount = 0;
	public int fSparseFactor = 0;
	protected PlayerRecord[] fPlayerRecords;
    protected static int kNumMonsterTypes = 7;
	public DungeonRecord fDungeon = null;
    public static boolean fDisplayDebugInfo = false;

    static int[] POOL_BONUS_GOLD = { 1, 0, 1, 0, 2 };
    static int[] POOL_BONUS_ARROWS = { 0, 1, 0, 2, 0 };

	private static final int kWoundBonus = 1;
//	private static final int kMagicSeed = 19;	// updated 20070506
//	private static final int kMagicSeed = 20;	// updated 20070508
//	private static final int kMagicSeed = 21;	// updated 20070605
//	private static final int kMagicSeed = 22;	// updated 20070629
//	private static final int kMagicSeed = 23;	// updated 20070726
//	private static final int kMagicSeed = 24;	// updated 20070831
//	private static final int kMagicSeed = 25;	// updated 20070920
//	private static final int kMagicSeed = 26;	// updated 20071005
//	private static final int kMagicSeed = 27;	// updated 20071027
// 	private static final int kMagicSeed = 28;	// updated 20071122
//	public static final int kMagicSeed = 29;	// updated 20071214
//	public static final int kMagicSeed = 30;	// updated 20080115
//	public static final int kMagicSeed = 31;	// updated 20080311
//	public static final int kMagicSeed = 32;	// updated 20080313
//	public static final int kMagicSeed = 33;	// updated 20080318
//  public static final int kMagicSeed = 34;	// updated 20080409
// 	public static final int kMagicSeed = 35;	// updated 20080529
//	public static final int kMagicSeed = 36;	// updated 20080617
//	public static final int kMagicSeed = 37;	// updated 20080712
//	public static final int kMagicSeed = 38;	// updated 20080728
//	public static final int kMagicSeed = 39;	// updated 20080801
//	public static final int kMagicSeed = 40;	// updated 20080805
//	public static final int kMagicSeed = 41;	// updated 20080810
//	public static final int kMagicSeed = 42;	// updated 20080816
//	public static final int kMagicSeed = 43;	// updated 20080909
//	public static final int kMagicSeed = 44;	// updated 20080930
//  public static final int kMagicSeed = 45;	// updated 20081019
//  public static final int kMagicSeed = 46;	// updated 20081026
//  public static final int kMagicSeed = 47;	// updated 20081126
//	public static final int kMagicSeed = 48;	// updated 20081215
// 	public static final int kMagicSeed = 49;	// updated 20090210
//	public static final int kMagicSeed = 50;	// updated 20090304
// 	public static final int kMagicSeed = 51;	// updated 20090323
//  public static final int kMagicSeed = 52;	// updated 20090421
//	public static final int kMagicSeed = 53;	// updated 20090511
//	public static final int kMagicSeed = 54;	// updated 20090526
//	public static final int kMagicSeed = 55;	// updated 20090603
// 	public static final int kMagicSeed = 56;	// updated 20090607
	public static final int kMagicSeed = 57;	// updated 20090609

	private static final int kLightningMultiplier = 1;
	private static final int kArrowMultiplier = 2;
	private static final int kFireballMultiplier = 2;
	private static final int kFightMultiplier = 3;

	public static final int kMaxNumRooms = 40;
	private static final int kMaxNumMonsters = 60;		// 50

	private static final int kNumSwordDungeons = 4;
	private static final int kNumShieldDungeons = 3;
	private static final int kNumSandalDungeons = 2;



	public static GameEngine	instance = null;

	public GameEngine(Device device, DungeonRecord dungeon, PlayerRecord[] records)
	{
		Utility.DEVICE = device;
		instance = this;
		fPlayerRecords = records;		
		fNumPlayers = records.length;
		Utility.Trace(fNumPlayers + " adventurers");

		fCurrentPlayer = 0;
		fDevice = device;

		fPlayers = new Player[fNumPlayers];
		fDungeon = dungeon;

		// Distribute swords, shields and sandals
		this.rand = new RandomNumber(kMagicSeed);

		int swords = 0;
		int shields = 0;
		int sandals = 0;
		int top = 20;
		for (int i = 0 ; i < kNumSwordDungeons ; ++i)
		{
			int no = rand.range(2, 18);
			if (no > 10)
			{
				if (no >= top)
					no = rand.range(2,18);
				else
					top = no;
			}
            if (no == fDungeon.fNumber)
				swords++;
		}
		top = 20;
		for (int i = 0 ; i < kNumShieldDungeons ; ++i)
		{
			int no = rand.range(2, 18);
			if (no > 10)
			{
				if (no >= top)
					no = rand.range(2,18);
				else
					top = no;
			}
            if (no == fDungeon.fNumber)
				shields++;
		}
		top = 20;
		for (int i = 0 ; i < kNumSandalDungeons ; ++i)
		{
			int no = rand.range(2, 18);
			if (no > 10)
			{
				if (no >= top)
					no = rand.range(2,18);
				else
					top = no;
			}
            if (no == fDungeon.fNumber)
				sandals++;
		}

        int seed = fDungeon.fNumber + kMagicSeed;
		this.rand = new RandomNumber(seed);
		Sequence.seed(seed);

		int min_rooms = fDungeon.getMinRooms();
		int max_rooms = fDungeon.getMaxRooms();
		int num_rooms = rand.range(min_rooms, max_rooms);
		if (num_rooms > kMaxNumRooms)
			num_rooms = kMaxNumRooms;
		fSparseFactor = rand.range(1, 100);
		fMaze = new Maze();
		fMaze.create(num_rooms);
		fMaze.initialize();	// one player

		for (int i = 0; i < fPlayerRecords.length; i++)
		{
			PlayerRecord rec = fPlayerRecords[i];
			fPlayers[i] = new Player(rec, fMaze.getRoom(1));
		}
		for (int i = 0; i < fPlayerRecords.length; i++)
		{
			if (fPlayers[i].getBorrowableCoins() >= fPlayers[i].getMinimumCoinsForNewStaff())
			{
				fMaze.setWizard(fMaze.getRoom(1));
			}
		}

		// TODO: Remove this CHEAT code!
//		Player.fKnowsExits = true;

        if (this.fDungeon.fCompleted)
		{
			Player.fKnowsExits = true;
			Player.fKnowsFountains = true;
			Player.fKnowsItems = true;
			Player.fKnowsCarpets = true;
			Player.fKnowsLair = true;
		}

		fMonsters = new Monster[kMaxNumMonsters];
		fNumMonsters = 0;

		// 1, 3, 7, 11, 21
		int lair_score = 1;
        if (fDungeon.fNumber > 0)
			lair_score += 2;
		if (fDungeon.fNumber > 1)
			lair_score += 4;
        if (fDungeon.fNumber > 2)
			lair_score += 4;
        if (fDungeon.fNumber > 3)
			lair_score += 10;

//        int monster_score = /* 4 + */ 2 * fNumPlayers + 4 * fDungeon + num_rooms / 10;
		// 0 0 1 3 6 10 15 21 28 36 45
		//-1 0 1 2 3 4 5 6  7  8  9 10 11  12  13  14  15   16  17  18  19  20
		// 0 1 1 2 3 5 8 13 21 35 56 91 147 238 385 623 1008  xx  xx  xx  xx

		int inc = 0;
		int monster_score = 0;
        for (int iLevel = 0; iLevel < fDungeon.fNumber; iLevel++)
		{
			if (inc >= 2)
			{
				lair_score++;
				monster_score--;
			}
			monster_score += inc;
			inc++;
		}

		lair_score *= 2;

//		lair_score += monster_score;
//		monster_score = monster_score * fNumPlayers + fNumPlayers - 1;

        int random_modifier = fDungeon.fNumber / 10;

		lair_score = fDungeon.getLairPoints();
		monster_score = fDungeon.getMonsterPoints();

		int num_lairs = 0;
		while (lair_score > 1)
		{
			Monster monster = this.getRandomMonster(random_modifier);
            if (monster.getKillScore() < (fDungeon.fNumber / 2))
				monster = this.getRandomMonster(random_modifier);

//			String str = monster.getMonsterDescription();
//			str = str.substring(str.lastIndexOf(' ')+1);
			int cost = 2 * monster.getKillScore();
			if (cost <= lair_score)
			{
				Room lair = fMaze.getRandomLair(num_lairs == 0);
				if (lair == null)
					break;
				lair.setLair(monster);
				int coins = monster.getKillScore();
 				if (coins > 50)
 					coins = 50;
                lair.fCoins = coins;
				monster.setStationary(true);
				monster.setRoom(lair);
				this.addMonster(monster);
				lair_score -= cost;
				num_lairs++;
				Utility.Trace("Creating " + monster.getMonsterDescription() + " lair in room " + lair.getRoomNumber());
			}
		}

		int no;
		// increase count of existing lairs
		while (lair_score > 1 && num_lairs > 0)
		{
			no = rand.range(1, num_lairs);
			Room lair = fMaze.getLair(no);
			Monster monster = lair.createMonster();
			lair.setLair(monster);
//			lair.fCoins++;
			monster.setStationary(false);
			monster.setRoom(lair);
			int cost = 2 * monster.getKillScore();
			lair_score -= cost;
			Utility.Trace("Creating " + monster.getMonsterDescription() + " in room " + lair.getRoomNumber());
			continue;
		}

		boolean hasRing = false;

		if (fDungeon.hasTreasure())
        {
            if (fDungeon.fNumber >= 20)
            {
                int bonus = fDungeon.fNumber / 10;
                swords += rand.range(0, bonus);
                shields += rand.range(0, bonus - 1);
                sandals += rand.range(0, bonus - 2);
            }
            fMaze.distributeSwords(swords);
            fMaze.distributeShields(shields);
            fMaze.distributeSandals(sandals);

            if (rand.range(1, 5) <= 2)
            {
                int bonus = fDungeon.fNumber / 5 + 1;
                do
                {
                    int armor = rand.range(1, bonus);
                    if (armor > Player.ARMOR_MAX)
                        armor = Player.ARMOR_MAX;
                    fMaze.distributeArmor(armor);
                    bonus = armor;
                } while (bonus > 1);
            }
            if (/*fDungeon.fNumber > 5 && */rand.range(1, 5) <= 2)
            {
                int bonus = fDungeon.fNumber / 5 + 1 + 1;
                do
                {
                    int staff = rand.range(1, bonus);
                    if (staff > Staff.MAX_STAFF)
                        staff = Staff.MAX_STAFF;
                    fMaze.distributeStaff(staff);
                    bonus = staff;
                } while (bonus > 1);
            }
			if (rand.range(1, 4) <= 1)
			{
				int ring = 0;
				int bonus = fDungeon.fNumber / 5;
				if (bonus < 1)
					bonus = 1;
				int caps = rand.range(1, bonus);
				for (int i = 0; i < caps; ++i)
				{
					int cap = rand.range(1, Player.RING_MAX);
					ring |= 1 << (cap - 1);
				}
				Utility.Assert(ring < (1 << Player.RING_MAX), "ring < (1 << Player.RING_MAX) ring:" + ring);
				Utility.Trace("Distributing " + Player.GetRingDescription(ring));
				fMaze.distributeRing(ring);
			}
		}

		fMaze.distributeStuff(fNumPlayers);
		fMaze.postInitialize();

		// true randomness from here on...
		this.rand = null;
		this.rand = new RandomNumber();

		monster_score += lair_score;
		while (monster_score > 0 && num_lairs > 0)
		{
			no = rand.range(1, num_lairs);
			Utility.Trace("Spawning from lair #" + no);

			Room room = fMaze.getLair(no);
			Monster monster = room.createMonster();

			Utility.Assert(monster != null, "GameEngine.ctor monster creation - monster != null");
			monster.setRoom(room);
//			if (rand.range(0, 1) == 1)  // half are stationary
//				monster.setStationary(true);
			if (!this.addMonster(monster))
				room.increaseMonsterBuffer();
			else
				Utility.Trace(monster.getMonsterDescription() + " started in room " + room.getRoomNumber());
			monster_score -= monster.getKillScore();
		}

		// force crash
//		Room fromRoom = fMaze.getRoom(1);
//		Room toRoom = fMaze.getRoom(8);
//		Room[] path = fMaze.findRoute(fromRoom, toRoom, null, Maze.USE_LOCKED_DOORS);
	}
	public void dispose()
	{
		Utility.Trace("GameEngine.dispose");
		fMaze.dispose();
		fMaze = null;
		int i;
		for (i = 0 ; i < fNumPlayers ; i++)
		{
			fPlayers[i].dispose();
			fPlayers[i] = null;
		}
		fPlayers = null;
		fDevice = null;
		for (i = 0 ; i < fNumMonsters ; ++i)
		{
			fMonsters[i] = null;
		}
		fMonsters = null;
	}
	protected Monster getRandomMonster(int modifier)
	{
		int rnd = rand.range(1, kNumMonsterTypes + modifier);
		if (rnd > kNumMonsterTypes)
			rnd = kNumMonsterTypes;

		if (rnd == 1)
			return new Orc();
		if (rnd == 2)
			return new Goblin();
		if (rnd == 3)
			return new Spider();
		if (rnd == 4)
			return new Troll();
		if (rnd == 5)
			return new Serpent();
		if (rnd == 6)
			return new Dragon();
		if (rnd == 7)
			return new Balrog();
		Utility.Assert(false, "GameEngine.getRandomMonster - missing monster type: " + rnd);
		return null;
	}
	public String toString()
	{
		String str = "";
		str += "Turn: " + fTurnCount + "\n";

		MonsterCounter mc = new MonsterCounter();
		for (int iMonster = 1 ; iMonster <= this.fNumMonsters ; iMonster++)
		{
			Monster monster = this.getMonster(iMonster);
			mc.countMonster(monster);
		}
		str += mc.toString() + "\n";

		str += "Sparse: " + fSparseFactor + "\n";
		str += "Dungeon: " + fDungeon.toDisplayString() + "\n";
		str += "Janitor: " + fMaze.getJanitorPath() + "\n";
        str += "Content: " + fMaze.fContent + "\n";

		return str;
	}
	public Player getPlayer(int no)
	{
		Utility.Assert(no > 0, "GameEngine.getPlayer - no > 0");
		Utility.Assert(no <= fNumPlayers, "GameEngine.getPlayer - no <= fNumPlayers");
		return fPlayers[no-1];
	}
	protected Monster getMonster(int no)
	{
		Utility.Assert(no > 0, "GameEngine.getMonster - no > 0");
		Utility.Assert(no <= fNumMonsters, "GameEngine.getMonster - no <= fNumMonsters");
		return fMonsters[no-1];
	}
	public boolean canAddMonsters()
	{
		if (fNumMonsters >= fMonsters.length)
			return false;
		return true;
	}
	protected boolean addMonster(Monster monster)
	{
		if (!this.canAddMonsters())
			return false;
		int ins = rand.range(0, fNumMonsters);
		Utility.Assert(ins >= 0, "GameEngine.addMonster - ins >= 0");
		Utility.Assert(ins <= fNumMonsters, "GameEngine.addMonster - ins <= fNumMonsters");
		for (int i = fNumMonsters ; i > ins ; i--)
		{
			fMonsters[i] = fMonsters[i - 1];
		}
		fMonsters[ins] = monster;
		fNumMonsters++;
		return true;
	}
	protected void removeMonster(Monster monster)
	{
		// find removal point
		int ins;
		for (ins = 0 ; ins < fNumMonsters ; ins++)
		{
			if (fMonsters[ins] == monster)
				break;
		}
		Utility.Assert(ins >= 0, "GameEngine.removeMonster - ins >= 0");
		Utility.Assert(ins <= fNumMonsters, "GameEngine.removeMonster - ins <= fNumMonsters");
		for (int i = ins  + 1; i < fNumMonsters ; i++)
		{
			fMonsters[i - 1] = fMonsters[i];
		}
		fNumMonsters--;
		fMonsters[fNumMonsters] = null;
	}
	Player getCurrentPlayer()
	{
		Utility.Assert(fCurrentPlayer >= 0, "GameEngine.getCurrentPlayer - fCurrentPlayer >= 0");
		Utility.Assert(fCurrentPlayer < fNumPlayers, "GameEngine.getCurrentPlayer - fCurrentPlayer(" + fCurrentPlayer + ") < fNumPlayers(" + fNumPlayers + ")");
		if (fCurrentPlayer < 0 || fCurrentPlayer >= fNumPlayers)
			return null;
		return fPlayers[fCurrentPlayer];
	}
	public void start()
	{
		// ugly trick!
		fCurrentPlayer = -1;
		this.processNextPlayer();
	}
	protected void processNextPlayer()
	{
		Utility.Trace("processNextPlayer - enter");

		try
		{
			fCurrentPlayer++;

			int alive = 0;
			for (int iPlayer = 1 ; iPlayer <= this.fNumPlayers ; iPlayer++)
			{
				Player player = this.getPlayer(iPlayer);
				if (player.isAlive() && !player.hasEscaped())
					alive++;
			}

			if (alive == 0)
			{
				// game over
				fDevice.displayGameOver();
				return;
			}

			if (fCurrentPlayer < this.fNumPlayers)
			{
				// check that all players are not dead
				Player currentPlayer = this.getCurrentPlayer();
				currentPlayer.process();
				if (currentPlayer.hasEscaped() || !currentPlayer.isAlive())
				{
					//				fDevice.displayGameOver();
					this.processNextPlayer();
					return;
				}

				System.out.println("-------------------------------------------------");

				// encounters
				fEncounter = 1;
				fSwordAttacks = 0;
				fMonsterAttacks = 0;
				this.doEncounters();
				// adjacent description
				// room description
				//		fDevice.displayRoom(fCurrentRoom);
			}
			else
			{
				System.out.println("-------------------------------------------------");

				this.process();
				fCurrentPlayer = -1;
				this.processNextPlayer();
			}
		}
		catch (java.lang.Exception e)
		{
			Utility.Trace("Exception caught: " + e.toString());
			fDevice.flushTrace();
			fDevice.displayProgramError(e.toString());
			e.printStackTrace();
		}

		Utility.Trace("processNextPlayer - exit");
	}
	public void process()
	{
		fTurnCount++;

        fMaze.reset();
		Utility.Trace("Turn " + fTurnCount + ": " + fNumMonsters + " monsters are moving...");
		// monster move
		for (int i = 0 ; i < fNumMonsters ; i++)
		{
			Monster monster = fMonsters[i];
			monster.process();
		}
		fMaze.process();

        for (int iPlayer = this.fNumPlayers; iPlayer >= 1; iPlayer--)
        {
            Player player = this.getPlayer(iPlayer);
            player.fArrowHits = 0;
            for (int shots = player.fRoom.fShots; shots > 0; shots--)
            {
                if (GameEngine.rand.range(1, 100) <= 30)
                {
                    player.fRoom.fShots--;
                    player.fArrowHits++;
                }
            }
        }

		System.gc();
    }
	public void doCommandMove()
	{
        fDevice.displayMove(this.getCurrentPlayer().fRoom);
	}
	public void doCommandShootArrow()
	{
        fDevice.displayShoot(this.getCurrentPlayer().fRoom, null);
	}
	public void doCommandCastLightningBolt()
	{
		Staff staff = this.getCurrentPlayer().useStaff();
        fDevice.displayShoot(this.getCurrentPlayer().fRoom, staff);
		staff = null;
	}
	public void doCommandTake()
	{
		Player currentPlayer = this.getCurrentPlayer();
        Room currentRoom = currentPlayer.fRoom;

		// Coins
        currentPlayer.fCoins = currentPlayer.fCoins + currentRoom.fCoins;
		currentRoom.fCoins = 0;

		// Keys
		int wants, got;
        wants = currentPlayer.getMaxNumKeys() - currentPlayer.fKeys;
        got = Utility.min(wants, currentRoom.fKeys);
        currentPlayer.fKeys = currentPlayer.fKeys + got;
        currentRoom.fKeys = currentRoom.fKeys - got;

		// Swords
        wants = currentPlayer.getMaxNumDragonSwords() - currentPlayer.fDragonSwords;
        got = Utility.min(wants, currentRoom.fDragonSwords);
		currentPlayer.fDragonSwords = currentPlayer.fDragonSwords + got;
        currentRoom.fDragonSwords = currentRoom.fDragonSwords - got;

		// Shields
		wants = currentPlayer.getMaxNumSerpentShields();
        wants -= currentPlayer.fSerpentShields;
        got = Utility.min(wants, currentRoom.fSerpentShields);
		currentPlayer.fSerpentShields = currentPlayer.fSerpentShields + got;
        currentRoom.fSerpentShields = currentRoom.fSerpentShields - got;

		// Armor
        if (currentRoom.fArmor > currentPlayer.fArmor && currentRoom.fArmor <= currentPlayer.getMaxArmor())
		{
            int armor = currentPlayer.fArmor;
            currentPlayer.fArmor = currentRoom.fArmor;
            currentRoom.fArmor = armor;
		}

		// Carpets
        if (currentPlayer.fCarpets == 0 && currentRoom.fCarpets > 0)
		{
			currentPlayer.fCarpets = currentPlayer.fCarpets + 1;
            currentRoom.fCarpets = currentRoom.fCarpets - 1;
		}
		// Staffs
        if (currentRoom.hasStaffs())
        {
            currentPlayer.pickUpStaffs(currentRoom.fStaffs);
            if (currentRoom.fStaffs.getNumStaffs() == 0)
				currentRoom.fStaffs = null;
        }
		// Arrows
//		if (currentPlayer.getMaxNumShots() != 0)
		{
            currentPlayer.fArrows = currentPlayer.fArrows + currentRoom.fArrows;
			currentRoom.fArrows = 0;
		}
		// Axes
		int axes = currentPlayer.getMaxNumAxes();
        if (currentPlayer.fAxes < axes && currentRoom.fAxes > 0)
		{
            if (currentRoom.fAxes < axes)
                axes = currentRoom.fAxes;
			currentPlayer.fAxes = currentPlayer.fAxes + axes;
            currentRoom.fAxes = currentRoom.fAxes - axes;
		}
		// Water
        if (currentRoom.fWater > 0)
		{
            wants = currentPlayer.getMaxWater() - currentPlayer.fWater;
			got = currentRoom.drink(wants);
			currentPlayer.fWater = currentPlayer.fWater + got;
		}
		// Sandals
        if (currentPlayer.fSandals == 0 && currentRoom.fSandals > 0)
		{
			currentPlayer.fSandals = currentPlayer.fSandals + 1;
			currentRoom.fSandals = currentRoom.fSandals - 1;
		}
		// Ring
// 		if (currentPlayer.fRing == 0 && currentRoom.fRing != 0)
		if ((currentRoom.fRing | currentPlayer.fRing) != currentPlayer.fRing)
		{
			currentPlayer.fRing |= currentRoom.fRing;
			currentRoom.fRing = Player.RING_NONE;
		}

		this.processNextPlayer();
	}
	public void doCommandDoor(boolean key)
	{
		Player currentPlayer = this.getCurrentPlayer();
        Room currentRoom = currentPlayer.fRoom;

        if (currentRoom.hasChest() && currentRoom.fChest.fLocked)
		{
            Utility.Assert(currentPlayer.fKeys > 0, "GameEngine.doCommandDoor - currentPlayer.getKeys() > 0");
            currentPlayer.fKeys = currentPlayer.fKeys - 1;
			currentRoom.unlockChest();
			fDevice.displayRoom(currentRoom);
			return;
		}

		Utility.Assert(currentRoom.hasDoor(), "GameEngine.doCommandDoor - currentRoom.hasDoor()");
		// unlock/open/close door
        Room door = currentRoom.fDoor;
		if (currentRoom.isDoorLocked())
		{
            Utility.Assert(currentPlayer.fKeys > 0, "GameEngine.doCommandDoor - currentPlayer.getKeys() > 0");
            currentPlayer.fKeys = currentPlayer.fKeys - 1;
			currentRoom.unlockDoor();
			if (!door.isExit())
				door.unlockDoor();
		}

		if (currentRoom.isDoorOpen())
		{
			currentRoom.closeDoor();
			if (!door.isExit())
				door.closeDoor();
		}
		else
		{
			currentRoom.openDoor(key);
			if (!door.isExit())
				door.openDoor(key);
		}

		fDevice.displayRoom(currentRoom);
	}
	public void doCommandFight()
	{
		Player currentPlayer = this.getCurrentPlayer();
		currentPlayer.makeNoise(Player.kFightNoise);
        Room currentRoom = currentPlayer.fRoom;

		Utility.Assert(currentPlayer.canAttack(), "GameEngine.doCommandFight - currentPlayer.canAttack()");

		int useSword = 0;

        int attacks = currentPlayer.fAxes;
        if (currentPlayer.fDragonSwords > 0)
		{
            useSword = currentPlayer.fDragonSwords;
			attacks += useSword;
		}
		if (attacks > currentPlayer.getNumAttacks())
			attacks = currentPlayer.getNumAttacks();

		int damage = 0;
		int strength = currentPlayer.getStrength() - (attacks - 1);
		if (strength < 0)
			strength = 0;
		for (int i = 0 ; i < attacks ; i++)
		{
			int low = 0;
			int high = 2;
			if (useSword > 0)
			{
				low = 1;
				high = 3;
				useSword--;
			}
			damage += rand.range(low, high);
			damage += strength;
		}
		currentPlayer.attack();

		Monster[] tmpKills = new Monster[kMaxNumMonsters];
		int count = 0;
		// no more than one/two kills per attack
//		attacks *= 2;
		for (int i = 1 ; i <= this.fNumMonsters ; i++)
		{
			Monster monster = this.getMonster(i);
            if (monster.fRoom == currentRoom)
			{
				Utility.Assert(monster.isAlive(), "GameEngine.doCommandFight - monster.isAlive()");
				tmpKills[count++] = monster;
				damage = monster.applyDamage(damage, count);
				if (damage <= 0)
					break;
				if (--attacks == 0)
					break;
			}
 		}

		Monster[] kills = new Monster[count];
		for (int i = 0 ; i < count ; i++)
		{
			Monster monster = tmpKills[i];
			if (monster.isAlive())
			{
				currentPlayer.updateScore(kWoundBonus);
			}
			else
			{
				this.removeMonster(monster);
				currentPlayer.updateScore(kFightMultiplier * monster.getKillScore());
			}
			kills[i] = monster;
		}

		fDevice.displayKill(kills, currentRoom, null, false);
	}
	public void doCommandCastFireball()
	{
		Player currentPlayer = this.getCurrentPlayer();
		currentPlayer.makeNoise(Player.kFightNoise);
        Room currentRoom = currentPlayer.fRoom;

		Utility.Assert(currentPlayer.hasChargedStaff(), "GameEngine.doCommandCastFireball - currentPlayer.hasChargedStaff()");

		int count = 0;
		Staff staff = currentPlayer.useStaff();
		int cast = staff.castSpell();

		Monster[] tmpKills = null;
		if (cast == Staff.CAST_SUCCESS)
		{
			tmpKills = new Monster[kMaxNumMonsters];
			int low = staff.getFireballDamageLow();
			int high = staff.getFireballDamageHigh();
			for (int i = 1; i <= this.fNumMonsters; i++)
			{
				Monster monster = this.getMonster(i);
                if (monster.fRoom == currentRoom && monster.isAlive())
				{
					int damage = rand.range(low, high);
					if (damage < 0)
						damage = 0;
					//				if (damage > 0)
					{
						tmpKills[count++] = monster;
						monster.applyDamage(damage, count);
						if (count >= kMaxNumMonsters)
							break;
					}
					low--; high--;
					if (low < -3) low = -3;
					if (high < 1) high = 1;
				}
			}
		}

		Monster[] kills = new Monster[count];
		for (int i = 0 ; i < count ; i++)
		{
			Monster monster = tmpKills[i];
			if (monster.isAlive())
			{
//				currentPlayer.updateScore(kWoundBonus);
			}
			else
			{
				this.removeMonster(monster);
				currentPlayer.updateScore(kFireballMultiplier * monster.getKillScore());
			}
			kills[i] = monster;
		}

        if (cast == Staff.CAST_SPLINTER)
        {
            currentPlayer.splinterStaff(staff);
        }
		fDevice.displayKill(kills, currentRoom, staff, cast == Staff.CAST_SPLINTER);
		staff = null;
	}

	public void doCommandWait()
	{
		Player currentPlayer = this.getCurrentPlayer();
		if (Player.RingHasCapability(currentPlayer.fRing, Player.RING_HEALING))
//			if ((fTurnCount % 5) == 0)
				currentPlayer.fHitPoints++;
		this.processNextPlayer();
	}
	public void doMove(Room newRoom)
	{
		Player currentPlayer = this.getCurrentPlayer();
        Room currentRoom = currentPlayer.fRoom;

		if (newRoom.isExit())
		{
			currentPlayer.setRoom(newRoom);
			fDevice.displayExit();
			return;
		}
		else
		{
			if (currentPlayer.canSmellMuck())
			{
                if (newRoom.hasMuck() && currentPlayer.fSandals == 0)
				{
					fDevice.displayWarning("No! Stop!  This way STINKS.");
					return;
				}
			}
			else
			{
				// warn for possible muck without shield
                if (newRoom != currentPlayer.fLastRoom && currentPlayer.fSerpentShields == 0 && !currentPlayer.fMuckWarned)
				{
                    if (currentPlayer.fRoom.hasStink())
					{
						currentPlayer.fMuckWarned = true;
						fDevice.displayWarning("Beware! You have no Serpent Shield!");
						return;
					}
				}
			}
			// warn for possible pit without carpet
            if (newRoom != currentPlayer.fLastRoom && currentPlayer.fCarpets == 0 && !currentPlayer.fPitWarned)
			{
                if (currentPlayer.fRoom.hasDraft())
				{
					currentPlayer.fPitWarned = true;
					fDevice.displayWarning("Beware! You have no Flying Carpet!");
					return;
				}
			}
		}

        Utility.Assert(currentRoom.hasPassage(newRoom) || currentRoom.fDoor == newRoom, "GameEngine.doMove - currentRoom.hasPassage(newRoom) || currentRoom.getDoor() == newRoom");
		currentPlayer.setRoom(newRoom);
		// process room for portcullis etc when someone passes through
		currentRoom.notifyPassage(newRoom);

        // Kill lair
		if (newRoom.isLair())
		{
			currentPlayer.updateScore(newRoom.stopSpawn());
		}
		this.processNextPlayer();
	}
	public void doShootArrow(Room targetRoom)
	{
		Player currentPlayer = this.getCurrentPlayer();
        Room currentRoom = currentPlayer.fRoom;

        Utility.Assert(currentPlayer.fArrows > 0, "GameEngine.doShootArrow - currentPlayer.getArrows() > 0");
        Utility.Assert(currentRoom.hasPassage(targetRoom) || currentRoom.fDoor == targetRoom, "GameEngine.doShootArrow - currentRoom.hasPassage(targetRoom) || currentRoom.getDoor() == targetRoom");

		currentPlayer.shootArrow();

		Monster[] tmpKills = new Monster[kMaxNumMonsters];
		int count = 0;
		int damage = rand.range(1, 3) + rand.range(0, currentPlayer.getStrength());
		for (int i = 1 ; i <= this.fNumMonsters ; i++)
		{
			Monster monster = this.getMonster(i);
            if (monster.fRoom == targetRoom)
			{
				monster.applyDamage(damage, i);
				if (damage > 0)
				{
					tmpKills[count++] = monster;
					damage = -1;
				}
//				break;
			}
		}
		if (count == 0)
			targetRoom.fArrows = targetRoom.fArrows + 1;

		Monster[] kills = new Monster[count];
		for (int i = 0 ; i < count ; i++)
		{
			Monster monster = tmpKills[i];
			if (monster.isAlive())
			{
				currentPlayer.updateScore(kWoundBonus);
			}
			else
			{
				this.removeMonster(monster);
				currentPlayer.updateScore(kArrowMultiplier * monster.getKillScore());
			}
			kills[i] = monster;
		}

		fDevice.displayKill(kills, targetRoom, null, false);

		for (int i = 0; i < count; i++)
		{
			kills[i] = null;
		}
		kills = null;
	}
	public void doCastLightningBolt(Room targetRoom)
	{
		Player currentPlayer = this.getCurrentPlayer();
        Room currentRoom = currentPlayer.fRoom;

		Utility.Assert(currentPlayer.hasChargedStaff(), "GameEngine.doCastLightningBolt - currentPlayer.hasChargedStaff()");
        Utility.Assert(currentRoom.hasPassage(targetRoom) || currentRoom.fDoor == targetRoom, "GameEngine.doCastLightningBolt - currentRoom.hasPassage(targetRoom) || currentRoom.getDoor() == targetRoom");

		int count = 0;
		Monster[] tmpKills = null;
        Staff staff = currentPlayer.useStaff();
        int cast = staff.castSpell();

// 		if (cast == Player.CAST_SUCCESS)
		if (cast != Staff.CAST_FAIL)
		{
			tmpKills = new Monster[kMaxNumMonsters];
			int low = staff.getLightningDamageLow();
			int high = staff.getLightningDamageHigh();
			for (int i = 1; i <= this.fNumMonsters; i++)
			{
				Monster monster = this.getMonster(i);
                if (monster.fRoom == targetRoom)
				{
					int damage = rand.range(low, high);
					if (cast != Staff.CAST_SUCCESS)
					{
						damage = 0;
					}

					//				if (damage < 0)
					//					damage = 0;
					//				if (damage > 0)
					{
						tmpKills[count++] = monster;
						monster.applyDamage(damage, count);
						if (count >= kMaxNumMonsters)
							break;
					}
					low--; high--;
					if (low < -3) low = -3;
					if (high < 1) high = 1;
				}
			}
		}

		Monster[] kills = new Monster[count];
		for (int i = 0 ; i < count ; i++)
		{
			Monster monster = tmpKills[i];
			if (monster.isAlive())
			{
//				currentPlayer.updateScore(kWoundBonus);
			}
			else
			{
				this.removeMonster(monster);
				currentPlayer.updateScore(kLightningMultiplier * monster.getKillScore());
			}
			kills[i] = monster;
		}

        if (cast == Staff.CAST_SPLINTER)
        {
            currentPlayer.splinterStaff(staff);
        }
        fDevice.displayKill(kills, targetRoom, staff, cast == Staff.CAST_SPLINTER);
		staff = null;

		for (int i = 0; i < count; i++)
		{
			kills[i] = null;
		}
		kills = null;
	}
	public void doEncounters()
	{
//		Utility.Trace("doEncounters - enter (count: " + fEncounter + ")");

		Player currentPlayer = this.getCurrentPlayer();
        Room currentRoom = currentPlayer.fRoom;

		if (!currentPlayer.isAlive())
		{
			this.processNextPlayer();
			return;
		}

		int count = 1;

		if (Utility.ASSERT_LOG != "")
		{
			if (count >= fEncounter)
			{
				fEncounter++;
				fDevice.displayEncounter("Assertion failed!");
				return;
			}
			count++;

			if (count >= fEncounter)
			{
				fEncounter++;
				fDevice.displayEncounter(Utility.ASSERT_LOG);
				return;
			}
			count++;
		}

		// check pit
        if (currentRoom.fPit && currentPlayer.fCarpets == 0 && currentPlayer.fCommands[0] == "Move")
		{
			// fall in pit
// 			currentPlayer.die();
			// Aargh! You've fallen into a pit!
// 			fDevice.displayDead("Aargh! You've fallen into a pit!");

			if (count >= fEncounter)
			{	
				fEncounter++;
				int damage = rand.range(1, Player.kMaxWounds);
				currentPlayer.applyDamage(damage);
				if (currentPlayer.isAlive())
				{
					fDevice.displayEncounter("Aargh! You've fallen into a pit!\n\nYou survived the fall...");
				}
				else
				{
					currentPlayer.die();
					fDevice.displayDead("Aargh! You've fallen into a pit!");
				}
				return;
			}
			count++;
		}

		// check muck
		if (currentRoom.hasMuck())
		{
			if (count >= fEncounter)
			{
				fEncounter++;
                if (currentPlayer.fSandals > 0)
				{
					String str = "Ooops!  You have run into some poisonous serpent-muck.\n";
					str += "But your sandals protected you! Get out of there, fast..."; 
					currentPlayer.fSandals = currentPlayer.fSandals - 1;
					fDevice.displayEncounter(str);
				}
				else
				{
					// suffocate
// 					currentPlayer.die();
// 					fDevice.displayDead("Ooops!  You have run into some poisonous serpent-muck.");
					int damage = rand.range(2, Player.kMaxWounds);
					currentPlayer.applyDamage(damage);
					String str = "Ooops!  You have run into some poisonous serpent-muck.";
					if (currentPlayer.isAlive())
					{
						str += "\nGet out of there, fast!";
						fDevice.displayEncounter(str);
					}
					else
					{
						currentPlayer.die();
						fDevice.displayDead(str);
					}
				}
				return;
			}
			count++;
		}

		if (currentPlayer.isParalyzed())
		{
			if (count >= fEncounter)
			{
				fEncounter++;
				String str = "You are paralyzed by spider poison.";
				fDevice.displayEncounter(str);
				return;
			}
			count++;
		}
		else
		{
			// check web
            if (currentRoom.fWeb)
			{
				if (count >= fEncounter)
				{
					fEncounter++;
					fMaze.fPlayerInWeb = true;
					String str = "You are stuck in a spider web";
					if (this.rand.range(0, 5) <= currentPlayer.getStrength())
					{
						str += ", but managed to break free.\n";
						currentRoom.fWeb = false;
						currentPlayer.fCommands[0] = "Move";
						// Don't miss out on an encounter!
						count--;
					}
					else
					{
						str += " and cannot move!\n";
					}
					fDevice.displayEncounter(str);
					return;
				}
				count++;
			}
		}

        // check water
        if (currentPlayer.fWater < 0)
		{
			// You have thirst to death.
			currentPlayer.die();
			fDevice.displayDead("You have thirst to death.");
			return;
		}

        // check arrow hits
        if (currentPlayer.fArrowHits > 0)
        {
            // You have been hit
            if (count >= fEncounter)
            {
                fEncounter++;

                int damage = 0;
                for (int hits = 0 ; hits < currentPlayer.fArrowHits ; hits++)
                    damage += rand.range(0, 2);
                if (currentPlayer.fArmor > 0)
                {
                    int absorb = rand.range(0, currentPlayer.fArmor);
                    if (absorb > damage)
                        absorb = damage;
                    currentPlayer.fArmorCount -= absorb;
                    damage -= absorb;
                }
                String str = "You have been hit by " + Utility.numToStr(currentPlayer.fArrowHits, "an arrow", "arrows");
				if (damage > 0)
					str += " (-" + damage + " hp)!";
				else
					str += "!\nYour armor saved you.";
                currentPlayer.applyDamage(damage);
                if (currentPlayer.isAlive())
                {
					String title = currentPlayer.fName; // player.fNumber + ". " + Player.raceToString(player.fRace);
					title += " (HP:" + currentPlayer.fHitPoints + ")";
                    fDevice.displayEncounter(title, str);
                }
                else
                {
                    currentPlayer.die();
                    str += "\nYou died!";
                    fDevice.displayDead(str);
                }
                return;
            }
            count++;
        }

		// check wizard
        if (this.fMaze.isWizard(currentRoom))
		{
			if (count >= fEncounter)
			{
				fEncounter++;
				String str = "Suddenly a cloud of smoke appears before you and a Wizard materializes.";
				if (this.hasMonster(currentRoom))
				{
					str += "\nHe waves his staff and casts a freeze spell!";
				}
				// Wizard charges staffs
				currentPlayer.chargeStaffs(); currentPlayer.chargeStaffs(); currentPlayer.chargeStaffs();
                fDevice.displayEncounter(currentPlayer.fName + " (gc: " + currentPlayer.fCoins + ")", str);
				return;
			}
			count++;
		}

		// check janitor
        if (this.fMaze.isJanitor(currentRoom))
		{
            if (currentPlayer.fWater < 9)
			{
                String title = currentPlayer.fName + " (keys: " + currentPlayer.fKeys + ")";
				String str = "The Janitor gives you some water.";
				currentPlayer.fWater = 10;
				fDevice.displayEncounter(title, str);
				return;
			}
		}

		// check water
        if ((currentPlayer.fWater == 8 || currentPlayer.fWater == 4) && !currentPlayer.isWaiting())
		{
			if (count >= fEncounter)
			{
				fEncounter++;
				String str = "You are thirsty.";
                if (currentPlayer.fWater <= 5)
					str = "You are very thirsty!";
				fDevice.displayEncounter(str);
				return;
			}
			count++;
		}

        if (!this.fMaze.isWizard(currentRoom))
		{
			for (int i = 1 ; i <= this.fNumMonsters ; i++)
			{
				Monster monster = this.getMonster(i);
                if (monster.fRoom == currentRoom)
				{
					if (count >= fEncounter && fMonsterAttacks < kMaxNumMonsterAttacks)
					{
						String str = "";
						fEncounter++;

                        fMonsterAttacks++;

                        if (monster.isSurprised())
						{
							monster.makeNoise(Player.kFightNoise);
							str = "You surprised " + monster.getMonsterDescription();
							fDevice.displayEncounter(str);
							return;
						}

// 						fMonsterAttacks++;

						// kill dragon with sword
                        if ((monster instanceof Dragon || monster instanceof Balrog) && currentPlayer.fDragonSwords > fSwordAttacks && !currentPlayer.isParalyzed())
						{
							// whack the monster!
							int low = 1;
							int high = 5;
							high += currentPlayer.getStrength();
							int damage = rand.range(low, high);
							monster.applyDamage(damage, 1);
							if (monster instanceof Dragon)
								str = "You whack at the dragon with the mighty Dragon Sword!\n";
							else // Balrog
								str = "You whack at the balrog with the mighty Dragon Sword!\n";
							if (monster.isAlive())
							{
								currentPlayer.updateScore(kWoundBonus);
								// dragon is stunned by the forceful blow!
								str += "But it survived the onslaught...";
							}
							else
							{
								this.removeMonster(monster);
								// make sure we do not miss the next encounter
								fEncounter--;
								currentPlayer.updateScore(monster.getKillScore());
								if (monster instanceof Dragon)
									str += "You killed the dragon.";
								else // Balrog
									str += "You killed the balrog.";
							}

							currentPlayer.fSwordCount -= rand.range(0, 2);
							// sword breaks every third time it is used
							if (currentPlayer.fSwordCount <= 0)
							{
								currentPlayer.fSwordCount = Player.kSwordCount;
                                currentPlayer.fDragonSwords = currentPlayer.fDragonSwords - 1;
								str += "\nYour Dragon Sword shattered!\n";
                                str += "You have " + Utility.numToStrLower(currentPlayer.fDragonSwords, "one sword", "swords") + " left.";
							}
							else
								fSwordAttacks++;

							fDevice.displayEncounter(str);
							currentPlayer.makeNoise(Player.kFightNoise);
							return;
						}
						else
						{
							boolean parried = !currentPlayer.isParalyzed();
							int parry = rand.range(0, currentPlayer.getNumAttacks());
							if (parry < fMonsterAttacks)
								parried = false;

							monster.fight();

							if (parried)
							{
								currentPlayer.fSwordCount -= rand.range(0, 1);
								str += "You parried the attack of " + monster.getMonsterDescription() + "!\n";
								fDevice.displayEncounter(str);
								return;
							}
							else
							{
								int damage = monster.getDamage();
                                if (!currentPlayer.isParalyzed() && currentPlayer.fSerpentShields > 0)
								{
									int absorb = rand.range(0, 4);
									if (absorb > damage)
										absorb = damage;
									currentPlayer.fShieldCount -= absorb;
									damage -= absorb;
									Utility.Assert(damage >= 0, "damage >= 0");
									if (damage < 0)
										damage = 0;
									if (currentPlayer.fShieldCount <= 0)
									{
										currentPlayer.fSerpentShields = currentPlayer.fSerpentShields - 1;
                                        if (currentPlayer.fHitPoints > damage)
											str += "The Serpent Shield shattered!\n";
										currentPlayer.fShieldCount = Player.kShieldCount;
									}
								}
                                int armor = currentPlayer.fArmor;
								if (armor > 0)
								{
									int absorb = rand.range(0, armor);
									if (absorb > damage)
										absorb = damage;
									currentPlayer.fArmorCount -= absorb;
									damage -= absorb;
									Utility.Assert(damage >= 0, "damage >= 0");
									if (damage < 0)
										damage = 0;
									if (currentPlayer.fArmorCount <= 0)
									{
										str += "Your armor is breaking!\n";
                                        currentPlayer.fArmor = armor - 1;
                                        currentPlayer.fArmorCount = Player.kArmorCountMultiplier * currentPlayer.fArmor;
									}
								}
								// Balrogs and dragons burns carpets
								if (monster instanceof Dragon || monster instanceof Balrog)
								{
									if (damage > 0)
									{
                                        if (currentPlayer.fHitPoints > damage)
										{
											String msg = "";
                                            if (currentPlayer.fCarpets > 0)
												msg += "The Flying Carpet";
                                            if (currentPlayer.fSandals > 0)
											{
												if (msg != "")
													msg += " and the ";
												else
													msg += "The ";
												msg += "Sandals of Silence";
											}
											if (msg != "")
												msg += " went up in flames!\n";
											str += msg;
										}
										currentPlayer.fCarpets = 0;
										currentPlayer.fSandals = 0;
									}
								}
								// Spiders paralyzes its victims
								if (monster instanceof Spider)
								{
									if (currentPlayer.isParalyzed())
									{
										this.doEncounters();
										return;
									}
									if (damage > 0)
									{
                                        if (currentPlayer.fHitPoints > damage)
										{
											str += "You were paralyzed by its poison!";
											int turns = 5 * damage - currentPlayer.getStrength();
											currentPlayer.setParalyzed(turns);
											// make sure we dont repeat the same encounter
											fEncounter++;
										}
									}
								}
								currentPlayer.applyDamage(damage);
								if (!currentPlayer.isAlive())
								{
									currentPlayer.die();
									fEncounter = 1000; // do no more encounters...
								}
							}
							fDevice.displayEncounter(monster, str);
							return;
						}
					}
					count++;
				}
			}
		}

		Utility.ASSERT_LOG = "";

		Utility.Trace("doEncounters - done");

		if (currentPlayer.isAlive())
			fDevice.displayRoom(currentRoom);
		else
			this.processNextPlayer();

		Utility.Trace("doEncounters - exit");
	}
	public int countPlayers(Room room)
	{
		int count = 0;
		for (int iPlayer = 1 ; iPlayer <= this.fNumPlayers ; iPlayer++)
		{
			Player player = this.getPlayer(iPlayer);
            if (room == player.fRoom && player.isAlive())
				count++;
		}
		return count;
	}
	public boolean hasPlayer(Room room)
	{
		if (this.countPlayers(room) == 0)
			return false;
		return true;
	}
	public boolean hasAdjacentMonster(Room room)
	{
		for (int iMonster = 1 ; iMonster <= this.fNumMonsters ; iMonster++)
		{
			Monster monster = this.getMonster(iMonster);
            if (room.hasPassage(monster.fRoom))
				return true;
            if (room.hasDoor() && room.isDoorOpen() && !room.isExitDoor() && room.fDoor == monster.fRoom)
				return true;
		}
		return false;
	}
	public String getAdjacentMonsterDescription(Room room)
	{
		String str = "";

		MonsterCounter mc = new MonsterCounter();
		for (int iMonster = 1 ; iMonster <= this.fNumMonsters ; iMonster++)
		{
			Monster monster = this.getMonster(iMonster);
            if (room.hasPassage(monster.fRoom))
			{
				mc.countMonster(monster);
			}
            if (room.hasDoor() && room.isDoorOpen() && !room.isExitDoor() && room.fDoor == monster.fRoom)
			{
				mc.countMonster(monster);
			}
		}
		
		if (mc.getCount() <= 6)
		{
			for (int iMonster = 1 ; iMonster <= this.fNumMonsters ; iMonster++)
			{
				Monster monster = this.getMonster(iMonster);
                if (room.hasPassage(monster.fRoom))
				{
					str += monster.getAdjacentDescription();
				}
                if (room.hasDoor() && room.isDoorOpen() && !room.isExitDoor() && room.fDoor == monster.fRoom)
				{
					str += monster.getAdjacentDescription();
				}
			}
		}
		else
		{
			str += "You can hear:\n";
			str += mc.getContentString();
		}
		return str;
	}
	public boolean hasMonster(Room room)
	{
		for (int iMonster = 1 ; iMonster <= this.fNumMonsters ; iMonster++)
		{
			Monster monster = this.getMonster(iMonster);
            if (room == monster.fRoom)
				return true;
		}
		return false;
	}
	public String getMonsterContentDescription(Room room)
	{
		String str = "";

        if (this.fMaze.isWizard(room))
		{
			str += "The Wizard!\n";
		}
        if (this.fMaze.isJanitor(room))
		{
			str += "The Janitor\n";
		}

		String strPlayers = "";
		String strCorpses = "";
		for (int iPlayer = 1 ; iPlayer <= this.fNumPlayers ; iPlayer++)
		{
			Player player = this.getPlayer(iPlayer);
            if (player != this.getCurrentPlayer() && player.fRoom == room)
			{
				if (player.isAlive())
				{
					if (strPlayers != "")
						strPlayers += " ";
                    strPlayers += player.fName;
				}
				else
                    strCorpses += "The body of " + player.fName + ".\n";
			}
		}
		if (strPlayers != "")
			str += strPlayers + "\n";
		str += strCorpses;

        MonsterCounter mc = new MonsterCounter();
		for (int iMonster = 1 ; iMonster <= this.fNumMonsters ; iMonster++)
		{
			Monster monster = this.getMonster(iMonster);
            if (monster.fRoom == room)
				mc.countMonster(monster);
		}
		str += mc.getContentString();
		return str;
	}
}
