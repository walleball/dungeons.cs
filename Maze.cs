using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Dungeons
{
	class Maze
	{
// 	public bool fExplored = false;
	private Room[]	fRooms;
	private int		fNumRooms;
//	private Room	fHermit;
	private Room	fWizard;
	private Room	fJanitor;
	private Room	fJanitorUnlockedDoor;
// 	private Room	fJanitorRaisedPortcullis;
	private Room	fJanitorTarget;
	private Room	fExit;
    public  String  fContent = "";

	public bool	fPlayerInWeb;
	static private bool debug = false; // debug

	private static bool[] sExploredRooms = null; 

	public Maze()
	{
		fRooms = null;
//		fHermit = null;
		fWizard = null;
		fJanitor = null;
		fJanitorUnlockedDoor = null;
// 		fJanitorRaisedPortcullis = null;
		fJanitorTarget = null;
		fExit = null;
		fPlayerInWeb = false;
	}
	public void create(int numRooms)
	{
		fNumRooms = numRooms;
		fRooms = new Room[fNumRooms];
		for (int iRoom = 0 ; iRoom < fNumRooms ; iRoom++)
		{
			fRooms[iRoom] = new Room(iRoom + 1);
		}
		fExit = new Room(Room.kExitNumber);
	}
	public void dispose()
	{
		Utility.Trace("Maze.dispose");
		for (int iRoom = 0 ; iRoom < fNumRooms ; iRoom++)
		{
			fRooms[iRoom].dispose();
			fRooms[iRoom] = null;
		}
		fExit.dispose();
		fExit = null;
	}
	public void setExplored(Room room)
	{
		if (!room.isExit())
		{
// 			bool wasExplored = sExploredRooms[room.getRoomNumber() - 1];
			sExploredRooms[room.getRoomNumber() - 1] = true;
// 			if (!wasExplored)
// 			{
// 				bool done = true;
// 				for (int i = 0; i < this.getNumRooms(); i++)
// 				{
// 					if (!sExploredRooms[i])
// 					{
// 						done = false;
// 						break;
// 					}
// 				}
// 				if (done)
// 				{
// 					fExplored = true;
// 				}
// 			}
		}
	}
	public bool isExplored(Room room)
	{
		Utility.Assert(room.getRoomNumber() <= sExploredRooms.Length, "Maze.isExplored - room.getRoomNumber() < sExploredRooms.length");
		if (room.getRoomNumber() > sExploredRooms.Length)
			return false;
		if (room.isExit())
			return false;
		return (sExploredRooms[room.getRoomNumber() - 1]);
	}
	public void initialize()
	{
		Utility.Trace("Maze - initialize");

        if (GameEngine.instance.fDungeon.fCompleted)
		{
			sExploredRooms = new bool[GameEngine.kMaxNumRooms];
			for (int i = 0; i < GameEngine.kMaxNumRooms; i++)
				sExploredRooms[i] = true;
		}
		else
		{
			sExploredRooms = new bool[GameEngine.kMaxNumRooms];

			for (int i = 0; i < GameEngine.kMaxNumRooms; i++)
				sExploredRooms[i] = false;
		}

		int no; // = GameEngine.rand.range(2, fNumRooms);

		// each room should have at least two exits
		for (int iRoom = 1 ; iRoom <= fNumRooms ; iRoom++)
		{
			Room room = this.getRoom(iRoom);

			// each room shall have two exits
			for (int iPassage = 1 ; iPassage <= 2 ; iPassage++)
			{
				Sequence seq = new Sequence(2, fNumRooms);
				seq.remove(iRoom);
				bool hasPassage = false;
				do
				{
					no = seq.remove();
					hasPassage = false;
					if (room.hasPassage(this.getRoom(no)))
						hasPassage = true;
					Room tmpRoom = this.getRoom(no);
					if (tmpRoom.hasPassage(this.getRoom(iRoom)))
						hasPassage = true;
				} while (seq.count() > 0 && hasPassage);
				if (!hasPassage)
					room.addPassage(this.getRoom(no));
			}
		}

		// normalize passages, i.e. make sure they "double-back"
		for (int iRoom = 1 ; iRoom <= fNumRooms ; iRoom++)
		{
			Room room = this.getRoom(iRoom);

			// each room shall have two exits
			for (int iPassage = 1 ; iPassage <= room.getNumPassages() ; iPassage++)
			{
				Room otherRoom = room.getPassage(iPassage);
				if (!otherRoom.hasPassage(room))
				{
					otherRoom.addPassage(room);
				}
			}
		}

		if (debug)
		{
			Room room = this.getRoom(1);
            room.addStaff(new Staff(null, Staff.BRONZE));
			room.fDragonSwords = room.fDragonSwords + 1;
			room.fSerpentShields = room.fSerpentShields + 1;
			room.fPit = true;
			room.addPassage(fExit);
			room.createDoor(fExit);
			fExit.addPassage(room);
		}

		// Create normal doors
		Room fromRoom = null;
		Room toRoom = null;
		int num_doors = this.getNumRooms() / 10;
		for (int iDoor = 1 ; iDoor <= num_doors + 1 ; iDoor++)
		{
			bool proceed = false;
			bool done = false;
			
			do
			{
				proceed = false;

				// Find a room with no door
				do
				{
					done = true;
					no = GameEngine.rand.range(2, fNumRooms);
					fromRoom = this.getRoom(no);
					if (fromRoom.hasDoor())
						done = false;
				} while (!done);

				// Find a passage to a room without a door
				for (int i = 0 ; i < 10 ; i++)
				{
					int index = GameEngine.rand.range(1, fromRoom.getNumPassages());
					toRoom = fromRoom.getPassage(index);
					if (!toRoom.hasDoor() && toRoom.getRoomNumber() != 1)
					{
						proceed = true;
						break;
					}
				}
			} while (!proceed);

			// Convert the passage to a doorway
			if (iDoor <= num_doors)
			{
				Utility.Trace("Create door from " + fromRoom.getRoomNumber() + " to " + toRoom.getRoomNumber());
				fromRoom.createDoor(toRoom);
				toRoom.createDoor(fromRoom);
				if (GameEngine.rand.range(1, 2) == 1)
				{
					fromRoom.fPoisoned = true;
					toRoom.fPoisoned = true;
				}
			}
			else
			{
				// Convert the last door to two exit-doors
				Utility.Trace("Create exit doors in rooms " + fromRoom.getRoomNumber() + " and " + toRoom.getRoomNumber());
				fromRoom.removePassage(toRoom);
				fromRoom.addPassage(fExit);
				fromRoom.createDoor(fExit);
				fExit.addPassage(fromRoom);
				toRoom.removePassage(fromRoom);
				toRoom.addPassage(fExit);
				toRoom.createDoor(fExit);
				fExit.addPassage(toRoom);
				if (GameEngine.rand.range(1, 2) == 1)
				{
					fromRoom.fPoisoned = true;
					toRoom.fPoisoned = true;
				}
			}
		}

		Utility.Trace("Removing passages...");

		// Go through the maze and remove one passage from each room
		for (int iRoom = 2 ; iRoom <= this.getNumRooms() ; iRoom++)
		{
			int rnd = GameEngine.rand.range(0, 100);
			if (rnd >= GameEngine.instance.fSparseFactor)
				continue;
			Room room = this.getRoom(iRoom);
			int passages = room.getNumPassages();
			if (room.hasDoor() && !room.isExitDoor())
				passages++;
			if (passages > 1)
			{
				Room other = room.getRandomPassage();
				if (other != null && other.getRoomNumber() != 1)
				{
					Utility.Trace("Removing passage between room " + room.getRoomNumber() + " and " + other.getRoomNumber());
					room.removePassage(other);
					other.removePassage(room);
					// Since the janitor can pass through doors,
					// it should be allowed to have rooms with only
					// a single door and no passages!
					// USE_LOCKED_DOORS
					// However, make sure there are at least one or two keys within reach
					// from the initial room (1)
					Room[] path = this.findRoute(room, other, null, USE_LOCKED_DOORS);
					if (path.Length == 0)
					{
						Utility.Trace("Reinserting passage between room " + room.getRoomNumber() + " and " + other.getRoomNumber());
						room.addPassage(other);
						other.addPassage(room);
					}
				}
			}
		}

		Utility.Trace("Creating portcullises...");

		int num_portcullis = this.getNumRooms() / 5;
		num_portcullis = GameEngine.rand.range(0, num_portcullis);
		int iPortcullis = 0;
		Sequence sRooms = new Sequence(1, this.getNumRooms());
		while (sRooms.count() > 0)
		{
			no = sRooms.remove();
			Room from = this.getRoom(no);
			if (from.getNumPassages() > 0)
			{
				no = GameEngine.rand.range(1, from.getNumPassages());
				if (from.fPortcullis[no-1] == Room.kPortcullisNone)
				{
					Room to = from.getPassage(no);
					Utility.Trace("Creating portcullis in passage between room " + from.getRoomNumber() + " and " + to.getRoomNumber());
					from.fPortcullis[no-1] = Room.kPortcullisOpen;
					to.createPortcullis(from);
					++iPortcullis;
				}
			}			
			if (iPortcullis >= num_portcullis)
				break;
		}

		Utility.Trace("Janitor...");
// 		int numPassages = 3;
//         if (fNumRooms < 10)
//             numPassages = 0;
		fJanitor = this.getRoom(GameEngine.rand.range(2, fNumRooms));

		fJanitorTarget = fJanitor;
		fJanitorUnlockedDoor = null;
// 		fJanitorRaisedPortcullis = null;

	}
	public void distributeStuff(int numPlayers)
	{
		Utility.Trace("Maze - distributeStuff");

		// distribute keys (one for each door) 
		Sequence seq = new Sequence(2, this.getNumRooms());
		//		seq.shuffle();
		int num_doors = this.getNumRooms() / 10;
		int num_chests = this.getNumRooms() / 20;
		int num_keys = num_doors /*+ num_chests*/ + 2;
        if (GameEngine.instance.fDungeon.fCompleted)
			num_keys = 0;

		int no;
		for (int i = 0; i < num_keys; i++)
		{
			no = seq.remove();
			Room room = this.getRoom(no);
            Utility.Assert(room.fKeys == 0, "Maze.initialize - room.getKeys() == 0");
			room.fKeys = 1;
		}

		// distribute coins
		int num_coins = this.getNumRooms() / 7 + 1 /*numPlayers*/;
		for (int i = 0; i < num_coins /*8 * numPlayers*/ ; i++)
		{
			//			no = fHermit.getRoomNumber();
			// while (no == fHermit.getRoomNumber())
				no = GameEngine.rand.range(2, this.getNumRooms());
			Room room = this.getRoom(no);
			// pile coins as high as possible
            if (room.fCoins == 0)
			{
				//				no = fHermit.getRoomNumber();
				// while (no == fHermit.getRoomNumber())
					no = GameEngine.rand.range(2, this.getNumRooms());
				room = this.getRoom(no);
			}
            room.fCoins = room.fCoins + GameEngine.rand.range(0, GameEngine.instance.fDungeon.fNumber / 2);
		}
		// distribute carpets
		//		int num_carpets = this.getNumRooms() / 15 + 1;
        int num_carpets = GameEngine.instance.fNumPlayers + this.getNumRooms() / 40;
		for (int i = 0; i < num_carpets; i++)
		{
			Room room = null;
			no = GameEngine.rand.range(2, this.getNumRooms());
			room = this.getRoom(no);

			room.fCarpets = room.fCarpets + 1;
		}

		// distribute arrows
		int num_arrows = this.getNumRooms() / 10 + 1/*numPlayers*/ + 1;
		for (int i = 0; i < num_arrows /* 2 * numPlayers + 2 */ ; i++)
		{
			no = GameEngine.rand.range(2, this.getNumRooms());
			Room room = this.getRoom(no);
            room.fArrows = room.fArrows + GameEngine.rand.range(1, (GameEngine.instance.fDungeon.fNumber / 2) + 1);
		}
		// distribute axes
		{
			no = GameEngine.rand.range(2, this.getNumRooms());
			Room room = this.getRoom(no);
			room.fAxes = room.fAxes + 1;
		}
// 		// distribute staffs
//         for (int i = 0; i < GameEngine.instance.fDungeon.fNumber; i++)
// 		{
// 			if (GameEngine.rand.range(1, 10) == 1)
// 			{
// 				no = GameEngine.rand.range(2, this.getNumRooms());
// 				Room room = this.getRoom(no);
// 				room.fAxes = room.fAxes + 1;
// 			}
// 		}

	    // distribute springs of water
		int num_springs = this.getNumRooms() / 10;
		for (int i = 0; i < num_springs /* 5 */ ; i++)
		{
			Room room = null;
			do
			{
				no = GameEngine.rand.range(2, this.getNumRooms());
				room = this.getRoom(no);
            } while (room.fWater > 0);

            Utility.Assert(room.fWater == -1, "Maze.initialize - !room.hasWater()");
            room.fWater = numPlayers * GameEngine.rand.range(10, 50);
		}

		// create chests
		for (int iChest = 0; iChest < num_chests; iChest++)
		{
			Room target = null;
			int maxCount = 0;
			for (int i = 0; i < 100; ++i)
			{
				Room room = null;
				while (room == null || room.hasChest() || room.hasDoor())
				{
					no = GameEngine.rand.range(1, fNumRooms);
					room = this.getRoom(no);
				}
				int count = room.countItems() + room.fCoins;
				if (count > maxCount)
				{
					target = room;
					maxCount = count;
				}
			}
			Utility.Assert(target != null, "Maze.initialize - target != null");
			target.createChest();
			if (GameEngine.rand.range(1, 2) == 1)
                target.fChest.fPoisoned = true;
		}

		if (debug)
		{
			Room entry = this.getRoom(1);
			entry.fCoins = 5;
			entry.fKeys = 1;
			entry.fCarpets = 1;
			entry.fAxes = 1;
            entry.fWater = 100;
			//			entry.createChest();
		}
	}
	public void distributeSwords(int count)
	{
        if (count > 0)
        {
            if (fContent != "")
                fContent += " ";
            fContent += "Sword";
            if (count > 1)
                fContent += "*" + Convert.ToString(count);
        }

		for (int i = 0 ; i < count ; ++i)
		{
			int no = GameEngine.rand.range(2, this.getNumRooms());
			Room room = this.getRoom(no);
			room.fDragonSwords = room.fDragonSwords + 1;
		}
	}
	public void distributeShields(int count)
	{
        if (count > 0)
        {
            if (fContent != "")
                fContent += " ";
            fContent += "Shield";
            if (count > 1)
                fContent += "*" + Convert.ToString(count);
        }

        for (int i = 0; i < count; ++i)
		{
			int no = GameEngine.rand.range(2, this.getNumRooms());
			Room room = this.getRoom(no);
			room.fSerpentShields = room.fSerpentShields + 1;
		}
	}
	public void distributeSandals(int count)
	{
        if (count > 0)
        {
            if (fContent != "")
                fContent += " ";
            fContent += "Sandals";
            if (count > 1)
                fContent += "*" + Convert.ToString(count);
        }
        for (int i = 0; i < count; ++i)
		{
			int no = GameEngine.rand.range(2, this.getNumRooms());
			Room room = this.getRoom(no);
			room.fSandals = room.fSandals + 1;
		}
	}
	public void distributeArmor(int armor)
	{
        if (armor > 0)
        {
            if (fContent != "")
                fContent += " ";
            fContent += Player.ARMOR_STRINGS[armor];
        }

		int no = GameEngine.rand.range(2, this.getNumRooms());
		Room room = this.getRoom(no);
		room.fArmor = armor;
	}
    public void distributeStaff(int type)
    {
        if (type > 0)
        {
            if (fContent != "")
                fContent += " ";
            fContent += Staff.TYPE_STRING[type];
        }

        int no = GameEngine.rand.range(2, this.getNumRooms());
        Room room = this.getRoom(no);
        room.addStaff(new Staff(null, type));
    }
    public void distributeRing(int type)
    {
        if (type != 0)
        {
            if (fContent != "")
                fContent += " ";
            fContent += Player.GetRingDescription(type);
        }

        int no = GameEngine.rand.range(2, this.getNumRooms());
        Room room = this.getRoom(no);
        room.fRing = type;
    }
    public void postInitialize()
	{
	}
	public int getNumRooms()
	{
		return fNumRooms;
	}
	public Room getRoom(int no)
	{
		Utility.Assert(no >= 0, "Maze.getRoom - no >= 0");
		Utility.Assert(no <= fNumRooms, "Maze.getRoom - no <= fNumRooms");
		if (no == 0)
			return fExit;
		return fRooms[no - 1];
	}
	public Room getExit()
	{
		return fExit;
	}
//	public Room getLair()
//	{
//		return fLair;
//	}
	public Room getRandomLair(bool force_create)
	{
		// Find room without door and only one entrance
		int passages = 1;
		bool allow_door = false;
		while (true)
		{
			Sequence seq = new Sequence(2, this.getNumRooms());
			//seq.remove(fHermit.getRoomNumber());
			seq.remove(this.getRoom(1).getPassage(1).getRoomNumber());
			seq.remove(this.getRoom(1).getPassage(2).getRoomNumber());
			while (seq.count() > 0)
			{
				int no = seq.remove();
				Room room = this.getRoom(no);
				if (room.isLair())
					continue;
				if ((allow_door || !room.hasDoor()) && room.getNumPassages() <= passages)
				{
					while (room.getNumPassages() > 1)
					{
						Room other = room.getRandomPassage();
						if (other != null && other.getRoomNumber() != 1)
						{
							room.removePassage(other);
							other.removePassage(room);
							Room[] path = this.findRoute(room, other, null, USE_LOCKED_DOORS);
							if (path.Length == 0)
							{
								room.addPassage(other);
								other.addPassage(room);
								// try another room
								break;
							}
						}
					}
					if (room.getNumPassages() <= 1)
						return room;
				}
			}
			if (allow_door)
			{
				if (force_create)
				{
					passages++;
					if (passages > 3)
						return null;
					allow_door = false;
				}
				else
					break;
			}
			else
			{
				allow_door = true;
			}
		}
		return null; // uncomment
//		return this.getRoom(1);
	}
	public Room getLair(int lair_no)
	{
		int no = lair_no;
		for (int i = 1 ; i <= this.getNumRooms(); i++)
		{
			Room room = this.getRoom(i);
			if (room.isLair())
			{
				no--;
				if (no == 0)
				{
					return room;
				}
			}
		}
		Utility.Trace("Maze.getLair called with invalid lair # (" + lair_no + ")");
		return null;
	}
	public bool isWizard(Room room)
	{
		if (room == fWizard)
			return true;
		return false;
	}
	public bool isJanitor(Room room)
	{
		if (room == fJanitor)
			return true;
		return false;
	}
    public void setWizard(Room room)
    {
        fWizard = room;
    }
    public void reset()
    {
        Utility.Trace("Maze.reset");
        for (int iRoom = 1; iRoom <= fNumRooms; iRoom++)
        {
            Room room = this.getRoom(iRoom);
            room.reset();
        }
    }
	public void process()
	{
		Utility.Trace("Maze.process");
		for (int iRoom = 1 ; iRoom <= fNumRooms ; iRoom++)
		{
			Room room = this.getRoom(iRoom);
			room.process();
		}

		// Wizard
		//
		Room aRoom = fWizard;
		while (aRoom == fWizard && fWizard != null)
		{
			int no = GameEngine.rand.range(1, this.getNumRooms());
			aRoom = this.getRoom(no);
			if (aRoom.hasMuck())
			{
				Utility.Trace("The wizard disposed of muck in cavern " + aRoom.getRoomNumber());
					aRoom.removeMuck();
			}
 			break;
		}
		fWizard = aRoom;

		Utility.Trace("Maze.process - janitor");
		// Janitor
		//
		if (fJanitor != null)
		{
			Utility.Trace("Janitor path: " + this.getJanitorPath());
            if (fJanitor.fWeb)
			{
				Utility.Trace("The janitor is removing a web in cavern " + fJanitor.getRoomNumber());
				if (GameEngine.rand.range(1, 2) == 1)
				{
					Utility.Trace("The janitor removed a web in cavern " + fJanitor.getRoomNumber());
					fJanitor.fWeb = false;
				}
			}
            else if (fJanitor.fPit)
			{
				Utility.Trace("The janitor is filling in pit in cavern " + fJanitor.getRoomNumber());
				if (GameEngine.rand.range(1, 2) == 1)
				{
					Utility.Trace("The janitor filled in pit in cavern " + fJanitor.getRoomNumber());
					fJanitor.fPit = false;
				}
			}
			else if (fJanitor.hasMuck())
			{
				Utility.Trace("The janitor is cleaning up muck in cavern " + fJanitor.getRoomNumber());
				if (GameEngine.rand.range(1, 3) == 1)
				{
					Utility.Trace("The janitor cleaned up muck in cavern " + fJanitor.getRoomNumber());
					fJanitor.removeMuck();
				}
			}
			else
			{
				if (fJanitor.getRoomNumber() == fJanitorTarget.getRoomNumber())
				{
					while (fJanitor.getRoomNumber() == fJanitorTarget.getRoomNumber())
					{
						int no = GameEngine.rand.range(1, this.getNumRooms());
						if (!this.getRoom(no).isLair())
							no = GameEngine.rand.range(1, this.getNumRooms());
						fJanitorTarget = this.getRoom(no);
					}

					// Set new target! Help adventurer if any.
					Room target = null;
					int thirst = GameEngine.rand.range(0, 20);
                    for (int iPlayer = 1; iPlayer <= GameEngine.instance.fNumPlayers; iPlayer++)
					{
						Player player = GameEngine.instance.getPlayer(iPlayer);
                        if (player.isAlive() && !player.hasEscaped() && player.fWater <= thirst)
						{
                            target = player.fRoom;
                            thirst = player.fWater;
                            Utility.Trace("The janitor detects a thirsty player in cavern " + player.fRoom.getRoomNumber());
						}
					}

					if (target != null && target != fJanitor)
					{
						fJanitorTarget = target;
						Utility.Trace("The janitor is rescuing a thirsty player in cavern " + target.getRoomNumber());
					}
				}

				// take one step towards the target
                Room[] path = GameEngine.instance.fMaze.findRoute(fJanitor, fJanitorTarget, null, Maze.USE_EVERYTHING);
				Utility.Assert(path.Length > 0, "Maze.process - path.length > 0");
				Utility.Trace("The janitor is moving towards " + fJanitorTarget.getRoomNumber() + " via " + path[0].getRoomNumber());
				aRoom = path[0];

				int num = fJanitor.getNumPassages();
				if (fJanitor.hasDoor() && !fJanitor.isExitDoor() /* && !fJanitor.isDoorLocked() */)
					num++;
				Room tmp = aRoom; 
				for (int i = 0 ; i < num ; i++)
				{
					if (tmp.hasMuck())
					{
						aRoom = tmp;
						Utility.Trace("The janitor smells muck in cavern " + aRoom.getRoomNumber());
						break;
					}
                    if (tmp.fPit)
					{
						aRoom = tmp;
						Utility.Trace("The janitor feels a draft from cavern " + aRoom.getRoomNumber());
						break;
					}
                    if (tmp.fWeb)
					{
						aRoom = tmp;
						Utility.Trace("The janitor sees a web in cavern " + aRoom.getRoomNumber());
						break;
					}
					if (fJanitor.isBlockedByPortcullis(tmp))
					{
						Utility.Trace("The janitor raises the portcullis between cavern " + fJanitor.getRoomNumber() + " and " + tmp.getRoomNumber());
						fJanitor.openPortcullis(tmp);
						tmp.openPortcullis(fJanitor);
					}
					int no = GameEngine.rand.range(1, num);
					if (no > fJanitor.getNumPassages())
                        tmp = fJanitor.fDoor;
					else
						tmp = fJanitor.getPassage(no);
				}

				if (fJanitorUnlockedDoor != null)
				{
					if (!fJanitorUnlockedDoor.isDoorJammed())
					{
						if (fJanitorUnlockedDoor.isDoorOpen())
						{
							Utility.Trace("The janitor closes the door between cavern " + fJanitor.getRoomNumber() + " and " + fJanitorUnlockedDoor.getRoomNumber());
							fJanitorUnlockedDoor.closeDoor();
							fJanitor.closeDoor();
						}
						if (!fJanitorUnlockedDoor.isDoorLocked())
						{
							Utility.Trace("The janitor locks the door between cavern " + fJanitor.getRoomNumber() + " and " + fJanitorUnlockedDoor.getRoomNumber());
							fJanitorUnlockedDoor.lockDoor();
							fJanitor.lockDoor();
						}
					}
					fJanitorUnlockedDoor = null;
				}
//				if (fJanitorRaisedPortcullis != null)
//				{
//					Utility.Trace("The janitor lowers the portcullis between cavern " + fJanitor.getRoomNumber() + " and " + fJanitorRaisedPortcullis.getRoomNumber());
//					fJanitor.closePortcullis();
//					fJanitorRaisedPortcullis.closePortcullis();
//				}
				if (fJanitor.hasDoor() && fJanitor.fDoor == aRoom)
				{
					if (fJanitor.isDoorLocked())
					{
						Utility.Trace("The janitor unlocks the door between cavern " + fJanitor.getRoomNumber() + " and " + aRoom.getRoomNumber());
						fJanitor.unlockDoor();
						aRoom.unlockDoor();
						fJanitorUnlockedDoor = fJanitor;
					}
					if (!fJanitor.isDoorOpen())
					{
						Utility.Trace("The janitor opens the door between cavern " + fJanitor.getRoomNumber() + " and " + aRoom.getRoomNumber());
						fJanitor.openDoor();
						aRoom.openDoor();
					}
				}
				if (fJanitor.isBlockedByPortcullis(aRoom))
				{
					Utility.Trace("The janitor raises the portcullis between cavern " + fJanitor.getRoomNumber() + " and " + aRoom.getRoomNumber());
					fJanitor.openPortcullis(aRoom);
					aRoom.openPortcullis(fJanitor);
				}
				Utility.Trace("The janitor moves to cavern " + aRoom.getRoomNumber());
				fJanitor = aRoom;
			}
		}
		fPlayerInWeb = false;
		Utility.Trace("Maze.process - done");
	}
	public static int USE_NO_DOORS = 0;
	public static int USE_OPEN_DOORS = 1;
	public static int USE_CLOSED_DOORS = 2;
	public static int USE_LOCKED_DOORS = 3;
	public static int USE_PORTCULLIS = 4;
	public static int USE_EVERYTHING = 5;
	public Room[] findRoute(Room fromRoom, Room toRoom, Player player, int useDoors)
	{
// 		Utility.Assert(player == null || useDoors != USE_LOCKED_DOORS, "player == null || useDoors != USE_LOCKED_DOORS");
		Utility.Assert(fromRoom != null, "Maze.findRoute - fromRoom != null");
		Utility.Assert(toRoom != null, "Maze.findRoute - toRoom != null");
		String str = "null";
		if (player != null)
            str = player.fName;
		Utility.Trace("Maze.findRoute: " + fromRoom.toString() + ", " + toRoom.toString() + ", " + str + ", " + useDoors);

		if (fromRoom == toRoom)
		{
			Utility.Trace("from equals to, return empty path");
			Room[] path = new Room[0];
			return path;
		}

		int[] room_path = new int[fNumRooms + 1];
		for (int i = 0 ; i <= fNumRooms ; i++)
			room_path[i] = 999;
		room_path[fromRoom.getRoomNumber()] = 1000 + fromRoom.getRoomNumber();

		Utility.Trace("Maze.findRoute: searching maze...");

		bool done = false;
		while (!done)
		{
			bool exhausted = true;
			for (int iRoom = 0 ; iRoom <= fNumRooms ; iRoom++)
			{
				if (room_path[iRoom] >= 1000)
				{
					room_path[iRoom] = room_path[iRoom] - 1000;
					if (iRoom == toRoom.getRoomNumber())
					{
						done = true;
						exhausted = false;
						break;
					}
					Room room = this.getRoom(iRoom);
//					if (player == null && room.hasMuck())
//						continue;
					for (int iPassage = 1 ; iPassage <= room.getNumPassages() ; iPassage++)
					{
						if (room.isPassable(iPassage) || useDoors >= USE_PORTCULLIS)
						{
							Room tmpRoom = room.getPassage(iPassage);
							if (player == null || Player.hasExplored(room) || Player.hasExplored(tmpRoom))
								if (room_path[tmpRoom.getRoomNumber()] == 999)
									room_path[tmpRoom.getRoomNumber()] = -iRoom;
						}
					}
					if (room.hasDoor())
					{
						bool allowDoor = true;
						if (player != null)
						{
							if (room.isDoorLocked())
								if (useDoors < Maze.USE_LOCKED_DOORS)
									allowDoor = false;
						}
						else
						{
							if (room.isDoorLocked())
							{
								if (useDoors < Maze.USE_LOCKED_DOORS)
									allowDoor = false;
							}
							else if (!room.isDoorOpen())
							{
								if (useDoors < Maze.USE_CLOSED_DOORS)
									allowDoor = false;
							}
							else
							{
								if (useDoors < Maze.USE_OPEN_DOORS)
									allowDoor = false;
							}
						}
//						if ((player == null && useLockedDoors) || !room.isDoorLocked())
						if (allowDoor)
						{
                            Room door = room.fDoor;
//							Utility.Assert(player == null || player.hasVisited(room) || player.hasVisited(door), "Maze.findRoute - player == null || player.hasVisited(room) || player.hasVisited(door)");
							if (player == null || Player.hasExplored(room) || Player.hasExplored(door))
								if (room_path[door.getRoomNumber()] == 999 && !door.isExit())
									room_path[door.getRoomNumber()] = -iRoom;
						}
					}
					exhausted = false;

				}
			}
			for (int iRoom = 0 ; iRoom <= fNumRooms ; iRoom++)
			{
				if (room_path[iRoom] <= 0)
					room_path[iRoom] = 1000 - room_path[iRoom];
			}

			if (exhausted)
				done = true;
		}

		if (room_path[toRoom.getRoomNumber()] == 999)
		{
			Utility.Trace("Maze.findRoute: found no path, return empty path");
			Room[] aRoom = new Room[0];
			return aRoom;
		}

		Utility.Trace("Maze.findRoute: found path, backtracking...");

		int count = 0;
		int current = toRoom.getRoomNumber();
		while (current != fromRoom.getRoomNumber())
		{
			count++;
			current = room_path[current];
			Utility.Assert(current > 0, "Maze.findRoute - current > 0");
			Utility.Assert(current < 999, "Maze.findRoute - current < 999");
		}

		Utility.Trace("Maze.findRoute: path is " + count + " rooms");

		Room[] rooms = new Room[count];
		current = toRoom.getRoomNumber();
		for (int i = count ; i > 0 ; i--)
		{
			rooms[i-1] = this.getRoom(current);
			current = room_path[current];
		}
		Utility.Assert(current == fromRoom.getRoomNumber(), "Maze.findRoute - current == fromRoom.getRoomNumber()");
		// Remove this code! Causes a crash...
//		if (GameEngine.rand.range(1, 5) == 1)
//		{
//			rooms[0] = null;
//			rooms[1] = null;
//		}

		str = "" + rooms[0].getRoomNumber();
		for (int j = 1 ; j < rooms.Length ; j++)
			str += "-" + rooms[j].getRoomNumber();
		Utility.Trace("Maze.findRoute: returning path: " + str);

		return rooms;
	}
	public String toString()
	{
		String str = "Maze " + fNumRooms + " rooms\n";
		for (int iRoom = 1 ; iRoom <= fNumRooms ; iRoom++)
		{
			Room room = this.getRoom(iRoom);
			str = str + room.toString();
		}
		return str;
	}
	public String getJanitorPath()
	{
		Room[] path = this.findRoute(fJanitor, fJanitorTarget, null, Maze.USE_LOCKED_DOORS);
		String str = "" + fJanitor.getRoomNumber();
		if (path.Length > 0)
		{
			for (int i = 0 ; i < path.Length ; i++)
				str += "-" + path[i].getRoomNumber();
		}
		return str;
	}
	public String getDungeonName()
	{
		int lairs = 0;
		MonsterCounter mc = new MonsterCounter();
		for (int i = 1 ; i <= this.getNumRooms() ; ++i)
		{
			Room room = this.getRoom(i);
			if (room.isLair())
			{
				lairs++;
				Monster lair = room.createMonster();
				mc.countMonster(lair);
			}
		}
		return mc.toString();
//		return lairs + " lairs";
	}
	}
}
