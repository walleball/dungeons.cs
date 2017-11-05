//package Dungeons;

import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;

import java.io.*;
import javax.microedition.io.*;

/**
 * Summary description for Phone.
 */
public class Phone
	implements Device, CommandListener
{
	private SuperStore fSuperStore;
	private GameEngine fEngine;
	private Display fDisplay;
	private DungeonsMIDlet  fMIDlet;
	private Maze			 fMaze;

	private Form			fRoomForm;
	private Image fImgUnvisited;
	private Image fImgVisited;
	private Image fImgLast;
	private Image fImgUnvisitedMuck;
	private Image fImgVisitedMuck;
	private Image fImgRecent;
	private Image fImgRecentMuck;
	private Image fImgExit;
    private Image fImgLair;
	private Image fImgExitRoom;
	private TextField fNameField;
	private TextField fDescriptionField;
	private TextField fCoinField;
	private TextField fSearchEdit;
	private ChoiceGroup fChoices;
	private boolean fMoving;
	private boolean fShooting;
	private int		fShotsFired;
	private boolean fHasTalkedToWizard;
	private boolean fHasTalkedToJanitor;
	private boolean fUsingMagic;
//	private String[] fCommands = new String[3];
	private boolean fDisplayDebugInfo = false;
	//	private boolean fDisplayBriefHermit = false;

	public static final int IMAGE_LAST = 4;
	public static final int IMAGE_RECENT = 3;
	public static final int IMAGE_VISITED = 2;
	public static final int IMAGE_UNVISITED = 1;

//		String[] races = { "Hobbit", "Human", "Elf", "Dwarf", "Magician" };
	static int[] POOL_BONUS_GOLD = { 1, 0, 1, 0, 2 };
	static int[] POOL_BONUS_ARROWS = { 0, 1, 0, 2, 0 };

	//	private static String kHighscoreServer = "olofw2";
	private static String kHighscoreServer = "217.215.101.161";

	public Phone(SuperStore store)
	{
		fSuperStore = store;
		fMIDlet = null;
		fDisplay = null;
		fEngine = null;
		fMaze = null;
		fRoomForm = null;
		fImgUnvisited = null;
		fImgVisited = null;
		fImgLast = null;
		fImgRecent = null;
		fImgRecentMuck = null;
		fImgExit = null;
        fImgLair = null;
		fImgExitRoom = null;
		fNameField = null;
		fDescriptionField = null;
		fSearchEdit = null;

		fMoving = false;
		fShooting = false;
		fShotsFired = 0;
		fHasTalkedToWizard = false;
		fHasTalkedToJanitor = false;
	}
	public void init(DungeonsMIDlet midlet)
	{
		fMIDlet = midlet;
		fDisplay = Display.getDisplay(midlet);
		fEngine = fMIDlet.getEngine();
        fMaze = fEngine.fMaze;

		try 
		{
			Utility.Trace("loading colors");
			fImgUnvisited = Image.createImage("/unvisited.png");
			fImgVisited = Image.createImage("/visited.png");
			fImgLast = Image.createImage("/last.png");
			fImgUnvisitedMuck = Image.createImage("/unvisited_muck.png");
			fImgVisitedMuck = Image.createImage("/visited_muck.png");
			fImgRecent = Image.createImage("/recent.png");
			fImgRecentMuck = Image.createImage("/recent_muck.png");
			fImgExit = Image.createImage("/exit.png");
            fImgLair = Image.createImage("/lair.png");
			fImgExitRoom = Image.createImage("/exit_room.png");
		}
		catch (java.io.IOException ex) 
		{
			// do nothing
			Utility.Trace("loading colors failed");
		}
	}
	protected void addCommand(String cmdText, int priority)
	{
		Utility.Trace("addCommand(" + cmdText + ")");
		Player player = fEngine.getCurrentPlayer();
/*
//		if (cmdText == fCommands[0] && cmdText == fCommands[1] && cmdText == fCommands[2])
//			priority = 0;
		boolean moved = false;
//		int waits = 0;
//		int needed = 2;
		for (int i = 0 ; i < Player.kNumCommands ; i++)
		{
//			if (player.fCommands[i] == "Wait" || player.fCommands[i] == "Drink")
//				waits++;
//			if (player.fCommands[i] == "Continue" && needed > 1)
//				needed--;
			if (player.fCommands[i] == "Move")
				moved = true;
		}
//		if (waits >= needed)
		if (!moved)
*/
		if (player != null && player.isWaiting())
		{
			if (cmdText == "Move")
				priority = 5;
			else if (cmdText == "Wait")
				priority = 4;
		}
		fRoomForm.addCommand(new Command(cmdText, Command.SCREEN, priority));
	}
	public void displayEncounter(Monster monster, String message)
	{
		Player player = fEngine.getCurrentPlayer();
		String str = "";
		if (monster.isAlive())
			str = monster.getEncounterDescription(player.isAlive());
		else
		{
			Utility.Assert(false, "Phone.displayEncounter - false"); // can't happen
			str = "You are now a DRAGONSLAYER.";
			str += "But this is not supposed to happen..";
//			Utility.Assert(monster instanceof Dragon);
//			str = "The Dragon rushes at you...\n";
//			str += "But before it had time to roast you, you slew it with a fierceful swing with the mighty Sword\n";
//			str += "You are now a DRAGONSLAYER.";
		}
		if (message.length() > 0)
			str += "\r\n" + message;
		Utility.Trace(str);

		//		Form form = new Form(monster.getMonsterDescription());
        String caption = player.fName; // player.fNumber + ". " + Player.raceToString(player.fRace);
        caption += " (hp: " + player.fHitPoints + ")";
		Form form = new Form(caption);

		form.append(str);

		form.addCommand(new Command("Continue", Command.SCREEN, 0));
		form.setCommandListener(this);
		fDisplay.setCurrent(form);
	}
	public void displayEncounter(String title, String str)
	{
		Utility.Trace("displayEncounter:" + str);

		//		Form form = new Form("Dungeons");
		Form form = new Form(title);
		form.append(str);

		form.addCommand(new Command("Continue", Command.SCREEN, 0));
		form.setCommandListener(this);
		fDisplay.setCurrent(form);
	}
	public void displayEncounter(String str)
	{
		Player player = fEngine.getCurrentPlayer();
        this.displayEncounter(player.fName, str);
	}
	public void displayRoom(Room room)
	{
// 		System.gc();

		fMoving = false;
		fShooting = false;
		fShotsFired = 0;
		fHasTalkedToWizard = false;
		fHasTalkedToJanitor = false;

		Player player = fEngine.getCurrentPlayer();

		String str = "Cavern " + room.getRoomNumber();
        str = player.fName; // player.fNumber + ". " + Player.raceToString(player.fRace);
		Utility.Trace(str);
		fRoomForm = null;
		fRoomForm = new Form(str);

		boolean must_escape = false;
//		if (room.hasDoor() && room.isDoorOpen() && room.isExitDoor())
//			must_escape = true;

		str = fEngine.getAdjacentMonsterDescription(room);
		str += room.getAdjacentMonsterDescription();
		str += room.getDescription();
		str += room.getAdjacentPlayerDescription();
		String content = room.getContentDescription();
		content += fEngine.getMonsterContentDescription(room);
		Utility.Trace("content: '" + content + "'");
		if (content.length() == 0)
			content = "Nothing";
		if (content.length() > 0)
		{
			str += "The room contains:\n";
			str += content;
		}
		Utility.Trace(str);
		fRoomForm.append(str);

		if (player.canMove())
		{
			this.addCommand("Move", 2);
		}
		else /*if (player.getWater() == 0)*/
		{
			this.addCommand("Wait", 5);
		}
		if (!player.isParalyzed())
		{
			if (fEngine.hasAdjacentMonster(room))
			{
				str = "Lightning Bolt";
				if (player.getNumberOfBolts() > 1)
					str += " *" + player.getNumberOfBolts();
				if (player.canConjureLightningBolt())
					this.addCommand(str, 3);
				str = "Shoot arrow";
				if (player.getNumberOfShots() > 1)
					str += " *" + player.getNumberOfShots();
				if (player.canShoot())
					this.addCommand(str, 3);
			}
			if (player.needsContent(room))
			{
				this.addCommand("Take", 4);
			}
			if (!must_escape && room.hasDoor() && room.isDoorLocked())
			{
				if (player.getLockPick() > 0)
					this.addCommand("Pick Lock", 5);
			}
            if (room.hasChest() && room.fChest.fLocked)
			{
				if (player.getLockPick() > 0)
					this.addCommand("Pick Lock", 5);
			}
			if (!must_escape)
			{
				if (room.hasDoor())
				{
					if (room.isDoorLocked())
					{
						if (player.hasKey())
							this.addCommand("Unlock", 6);
					}
					else if (room.isDoorOpen())
					{
						if (!room.isExitDoor())
							if (!room.isDoorJammed())
								this.addCommand("Close", 9);
					}
					else
					{
						this.addCommand("Open", 6);
					}
				}
                if (room.hasChest() && room.fChest.fLocked)
				{
					if (player.hasKey())
						this.addCommand("Unlock", 6);
				}
			}
			str = "Fight";
			if (player.getNumberOfAttacks() > 1)
				str += " *" + player.getNumberOfAttacks();
			if (fEngine.hasMonster(room) && player.canAttack())
				this.addCommand(str, 2); // 3
			str = "Fireball";
			if (player.getNumberOfBalls() > 1)
				str += " *" + player.getNumberOfBalls();
			if (fEngine.hasMonster(room) && player.canConjureFireball())
				this.addCommand(str, 2); // 3
		}
//		if (fMaze.isHall(room))
//			this.addCommand("Read slab", 7);
		this.addCommand("Map", 8);
		this.addCommand("Inventory", 8);
		if (!player.isParalyzed())
		{
            if (GameEngine.instance.fMaze.isWizard(room) && (player.getNumBalls() > 0 || player.getNumBolts() > 0))
			{
                int neededCoins = player.getMinimumCoinsForNewStaff();
                if (player.getBorrowableCoins() >= neededCoins)
                    this.addCommand("Talk to Wizard", 1);
			}
            if (GameEngine.instance.fMaze.isJanitor(room))
			{
                if (player.fKeys < player.getMaxNumKeys())
					this.addCommand("Talk to Janitor", 11);
			}
			if (fEngine.hasAdjacentMonster(room) && player.getNoiseLevel() <= 1)
				this.addCommand("Make noise", 12);
		}
		if (fDisplayDebugInfo)
			this.addCommand("Trace", 12);
		this.addCommand("Wait", 13);
		boolean moved = false;
		for (int i = 0 ; i < Player.kNumCommands ; i++)
		{
			if (player.fCommands[i] == "Move")
				moved = true;
		}
        if (!moved)
        {
            if (player.fWater > 8)
            {
                if (player.fRoom.hasUnblockedPassage())
                    this.addCommand("Drink", 13);
                else
                    this.addCommand("Drink", 4);
            }
            else if (player.fWater > 4)
                this.addCommand("Drink more", 13);
            else if (player.fWater > 0)
                this.addCommand("Drink all", 13);
        }

		fRoomForm.setCommandListener(this);
		fDisplay.setCurrent(fRoomForm);
	}
	public void displayMove(Room room)
	{
		String str = "Cavern " + room.getRoomNumber();
		Utility.Trace(str);
		List list = new List(str, List.IMPLICIT);
		Image img = fImgExit;

		boolean must_escape = false;
//		if (room.hasDoor() && room.isDoorOpen() && room.isExitDoor())
//			must_escape = true;

		Player player = fEngine.getCurrentPlayer();

		if (!must_escape)
		{
			for (int i = 1 ; i <= room.getNumPassages() ; i++)
			{
				if (!room.isPassable(i))
					continue;
				Room otherRoom = room.getPassage(i);
				str = String.valueOf(otherRoom.getRoomNumber());

				int visited = IMAGE_UNVISITED;
				boolean muck = false;
                if (player.fSerpentShields > 0 && otherRoom.hasMuck())
					muck = true;

				if (player.hasVisited(otherRoom))
				{
					visited = IMAGE_VISITED;
					if (otherRoom.hasScent())
						visited = IMAGE_RECENT;
                    if (player.fLastRoom == otherRoom)
						visited = IMAGE_LAST;
				}
				if (player.hasExplored(otherRoom))
				{
					int numPassages = otherRoom.getNumPassages();
					if (otherRoom.hasDoor() && (otherRoom.isDoorLocked() || !otherRoom.isExitDoor()))
						numPassages++;
					str += "   (" + player.getNumUnvisited(otherRoom) + "/" + numPassages + ")";
				}
                boolean lair = player.fKnowsLair && otherRoom.isLair();
				boolean exit = false;
				if (otherRoom.hasDoor() && otherRoom.isExitDoor())
				{
					if (player.fKnowsExits || (player.hasExplored(otherRoom) && !otherRoom.isDoorLocked()))
						exit = true;
				}
				img = getRoomImage(visited, muck, lair, exit);
				list.append(str, img);
			}
		}
		if (room.hasDoor() && room.isDoorOpen())
		{
			img = fImgExit;
            Room door = room.fDoor;
			Utility.Assert(door != null, "Phone.displayMove - door != null");
			if (door.isExit())
			{
				list.append("Exit", img);
			}
			else
			{
				str = "Door: " + String.valueOf(door.getRoomNumber());
				int visited = IMAGE_UNVISITED;
				boolean muck = false;
                if (player.fSerpentShields > 0 && door.hasMuck())
					muck = true;

				if (player.hasVisited(door))
				{
					visited = IMAGE_VISITED;
					if (door.hasScent())
						visited = IMAGE_RECENT;
                    if (player.fLastRoom == door)
						visited = IMAGE_LAST;
				}

				if (player.hasExplored(door))
				{
//					str += "   (" + player.getNumUnvisited(door) + "/" + door.getNumPassages() + ")";
					int numPassages = door.getNumPassages() + 1;
					str += "   (" + player.getNumUnvisited(door) + "/" + numPassages + ")";
				}
                boolean lair = player.fKnowsLair && door.isLair();
				img = getRoomImage(visited, muck, lair, false);
				list.append(str, img);
			}
		}
//		list.append(" ...", null);

		//		list.addCommand(new Command("Select", Command.SCREEN, 0));
		list.addCommand(new Command("Back", Command.BACK, 0));
		list.setCommandListener(this);
		fDisplay.setCurrent(list);
	}
	public void displayShoot(Room room, Staff staff)
	{
		Player player = fEngine.getCurrentPlayer();

		fUsingMagic = false;
		if (staff != null)
			fUsingMagic = true;
		String str; // = "Cavern " + room.getRoomNumber();
		if (staff != null)
		{
			str = "Cast " + staff.TYPE_STRING[staff.getType()]; // + " bolt";
			if (player.getNumberOfBolts() > 1)
			{
				int numBolts = player.getNumBolts();
				if (numBolts > fShotsFired + player.getChargedStaffs())
					numBolts = fShotsFired + player.getChargedStaffs();
				str += " " + (fShotsFired+1) + "/" + numBolts /*player.getNumberOfBolts()*/;
			}
		}
		else
		{
			str = "Shoot arrow";
			if (player.getNumberOfShots() > 1)
			{
				str += " " + (fShotsFired + 1) + "/" + player.getNumberOfShots();
			}
		}
		Utility.Trace(str);
		int select = -1;
		int truaim = -1;
		if (Player.RingHasCapability(player.fRing, Player.RING_TRUEAIM))
			truaim = -2;
		List list = new List(str, List.IMPLICIT);
		Image img = fImgExit;

        int last_shot = player.fLastTarget;
		int numPortcullis = 0;
		for (int i = 1 ; i <= room.getNumPassages() ; i++)
		{
			if (fUsingMagic && room.isBlockedByPortcullis(i))
			{
				numPortcullis++;
				continue;
			}
			Room otherRoom = room.getPassage(i);
			if (truaim == -2 && fEngine.hasMonster(otherRoom))
			{
				truaim = i - 1 - numPortcullis;
			}
			if (otherRoom.getRoomNumber() == last_shot)
			{
				Utility.Trace("Last shot match: " + last_shot);
				select = i - 1 - numPortcullis;
				if (truaim != -1 && !fEngine.hasMonster(otherRoom))
					select = -1;
			}
			str = String.valueOf(otherRoom.getRoomNumber());
			int visited = IMAGE_UNVISITED;
			boolean muck = false;
            if (player.fSerpentShields > 0 && otherRoom.hasMuck())
				muck = true;

			if (player.hasVisited(otherRoom))
			{
				visited = IMAGE_VISITED;
				if (otherRoom.hasScent())
					visited = IMAGE_RECENT;
                if (player.fLastRoom == otherRoom)
					visited = IMAGE_LAST;
			}
			if (player.hasExplored(otherRoom))
			{
				int numPassages = otherRoom.getNumPassages();
				if (otherRoom.hasDoor() && (otherRoom.isDoorLocked() || !otherRoom.isExitDoor()))
					numPassages++;
				str += "   (" + player.getNumUnvisited(otherRoom) + "/" + numPassages + ")";
			}
            boolean lair = player.fKnowsLair && otherRoom.isLair();
			boolean exit = false;
			if (otherRoom.hasDoor() && otherRoom.isExitDoor())
			{
				if (player.fKnowsExits || (player.hasExplored(otherRoom) && !otherRoom.isDoorLocked()))
					exit = true;
			}
			img = getRoomImage(visited, muck, lair, exit);
			list.append(str, img);
		}

		if (room.hasDoor() && room.isDoorOpen())
		{
            Room door = room.fDoor;
			Utility.Assert(door != null, "Phone.displayShoot - door != null");
			if (door.isExit())
			{
//				list.append("Exit", fImgExit);
			}
			else
			{
				if (door.getRoomNumber() == last_shot)
				{
					Utility.Trace("Last shot match: " + last_shot);
					select = room.getNumPassages() - numPortcullis;
				}
				if (truaim == -2 && fEngine.hasMonster(door))
				{
					truaim = room.getNumPassages() - numPortcullis;
				}
				str = "Door: " + String.valueOf(door.getRoomNumber());
				int visited = IMAGE_UNVISITED;
				boolean muck = false;
                if (player.fSerpentShields > 0 && door.hasMuck())
					muck = true;

				if (player.hasVisited(door))
				{
					visited = IMAGE_VISITED;
					if (door.hasScent())
						visited = IMAGE_RECENT;
                    if (player.fLastRoom == door)
						visited = IMAGE_LAST;
				}
				if (player.hasExplored(door))
				{
//					str += "   (" + player.getNumUnvisited(door) + "/" + door.getNumPassages() + ")";
					int numPassages = door.getNumPassages() + 1;
					str += "   (" + player.getNumUnvisited(door) + "/" + numPassages + ")";

				}
                boolean lair = player.fKnowsLair && door.isLair();
				img = getRoomImage(visited, muck, lair, false);
				list.append(str, img);
			}
		}

		if (truaim != -1 && select == -1)
			select = truaim;
		Utility.Trace("setSelectedIndex: " + select);
		if (select >= 0)
			list.setSelectedIndex(select, true);

		if (fShotsFired == 0)
			list.addCommand(new Command("Back", Command.BACK, 0));
		else
			list.addCommand(new Command("Continue ", Command.SCREEN, 0));

		list.setCommandListener(this);
		fDisplay.setCurrent(list);
	}
	public void displayInventory()
	{
		Player player = fEngine.getCurrentPlayer();
        String title = player.fName; // player.fNumber + ". " + Player.raceToString(player.fRace);
        title += " (HP:" + player.fHitPoints + ")";
		String inventory = ""; // "You are carrying:\n";

		String content = "";
		// coins
        if (player.fCoins > 0)
		{
            content += Utility.numToStr(player.fCoins, "A Gold Coin", "Gold Coins") + "\n";
		}
		// keys
        if (player.fKeys > 0)
		{
            content += Utility.numToStr(player.fKeys, "A Key", "Keys") + "\n";
		}
		// carpets
        if (player.fCarpets > 0)
		{
            content += Utility.numToStr(player.fCarpets, "A Flying Carpet", "Flying Carpets") + "\n";
		}
		// arrows
        if (player.fArrows > 0)
		{
            content += Utility.numToStr(player.fArrows, "An Arrow", "Arrows") + "\n";
		}
        // axes
        if (player.fAxes > 0)
        {
            content += Utility.numToStr(player.fAxes, "An Axe", "Axes");
            content += " (" + player.getMaxNumAxes() + ")" + "\n";
        }
        // swords
		{
            content += Utility.numToStr(player.fDragonSwords, "A Dragon Sword", "Dragon Swords");
			content += " (" + player.getMaxNumDragonSwords() + ")" + "\n";
		}
		// shields
		{
            content += Utility.numToStr(player.fSerpentShields, "A Serpent Shield", "Serpent Shields"); // + "(" + player.fShieldCount + ")\n";
			content += " (" + player.getMaxNumSerpentShields() + ")" + "\n";
		}
        content += player.ARMOR_STRINGS[player.fArmor] + "\n";

		// sandals
        if (player.fSandals > 0)
		{
            content += Utility.numToStr(player.fSandals, "The Sandals of Silent Treading", "Sandals of Silent Treading") + "\n";
		}
		// ring
		if (player.fRing != 0)
		{
			content += player.GetRingDescription(player.fRing);
			if (fDisplayDebugInfo) 
				content += "(" + player.fRing + ")";
			content += "\n";
		}
		// staffs
		int charged = player.getChargedStaffs();
		int charging = player.getStaffs() - charged;
		if (charged > 0)
		{
			content += Utility.numToStr(charged, "A Magic Staff", "Magic Staffs");
			content += " (" + player.getMaxNumStaffs() + ")" + "\n";
            if (charging == 0)
            {
                content += player.getStaffDescriptions() + "\n";
            }
        }
		if (charging > 0)
		{
			content += Utility.numToStr(charging, "A Used Staff", "Used Staffs") + "\n";
		}
		//water
        content += String.valueOf(player.fWater) + " unit";
        if (player.fWater > 1) content += "s";
		content += " of Water\n";

		// wounds
		content += player.getWoundDescription() + "\n";

		if (fDisplayDebugInfo)
		{
			content += GameEngine.instance.toString();
		}
		else
		{
			content += player.getLevelDescription() + "\n";
		}

		if (content == "")
			content = "Nothing";
		inventory += content;

		//		Alert alert = new Alert(title, inventory, null/*image*/, AlertType.INFO);
		Alert alert = new Alert(title);

		alert.setString(inventory);
		alert.setTimeout(Alert.FOREVER);
		fDisplay.setCurrent(alert, fRoomForm);
	}
	/*
	public void displayHighScores()
	{
		Form form = new Form("Survivors");

		String str = "not implemented yet";
		form.append(str);
		form.addCommand(new Command("Back", Command.BACK, 0));
/*
  		if (!uploaded)
		{
			form.addCommand(new Command("Upload", Command.SCREEN, 0));
		}
		else
		{
			form.addCommand(new Command("Download", Command.SCREEN, 0));
		}
*		
		form.setCommandListener(this);
		fDisplay.setCurrent(form);
	}
*/
	public void displayMap()
	{
		Player player = fEngine.getCurrentPlayer();
		Form form = new Form("Maps:");
//		form.append(text);
		int count = 6;
        if (fEngine.fNumPlayers > 1)
			count = 7;

		String[] strings = new String[count];
		strings[0] = "Search Room"; 
		strings[1] = "Visited Rooms"; 
		strings[2] = "Unvisited Rooms"; 
		strings[3] = "Fountains";
// 		strings[4] = "Doors";
		strings[4] = "Misc";
		strings[5] = "Notes";
        if (fEngine.fNumPlayers > 1)
			strings[6] = "Adventurers";

		fChoices = null;
		fChoices = new ChoiceGroup("", Choice.EXCLUSIVE, strings, null);
		fChoices.setSelectedIndex(2, true);
        if (player.fWater <= 4)
			fChoices.setSelectedIndex(3, true);
		form.append(fChoices);
		form.addCommand(new Command("Display ", Command.SCREEN, 0));

		form.setCommandListener(this);
		fDisplay.setCurrent(form);
	}
	public void displayMapInformation(int info)
	{
		if (info == 0) // search room
		{
			this.displaySearch();
			return;
		}
		else if (info == 1) // visited rooms
			this.displayMapVisitedRooms();
		else if (info == 2)
			this.displayMapUnvisitedRooms();
		else if (info == 3)
			this.displayMapFountains();
		else if (info == 4)
// 			this.displayMapDoors();
			this.displayMapMisc();
		else if (info == 5)
			this.displayMapNotes();
		else if (info == 6)
			this.displayMapAdventurers();
// 		else if (info == 7)
	}
	public void displayMapVisitedRooms()
	{
		Player player = fEngine.getCurrentPlayer();
		String title = "Visited";
		String map = "";

		for (int i = 1 ; i <= fMaze.getNumRooms() ; i++)
		{
			Room room = fMaze.getRoom(i);
			if (player.hasVisited(room))
				map += room.getShortDescription() + "\n";
		}

		Alert alert = new Alert(title);
		alert.setString(map);
		alert.setTimeout(Alert.FOREVER);
		fDisplay.setCurrent(alert, fRoomForm);
	}
	public void displayMapUnvisitedRooms()
	{
		Player player = fEngine.getCurrentPlayer();
		String title = "Unvisited";
		String map = "";
		int unvisited = 0;

		for (int i = 1 ; i <= fMaze.getNumRooms() ; i++)
		{
			Room room = fMaze.getRoom(i);
			if (!player.hasVisited(room))
			{
				unvisited++;
				String str = room.getRoomNumber() + ": ";
                Room[] path = fMaze.findRoute(player.fRoom, room, player, Maze.USE_CLOSED_DOORS);
				if (path.length > 0)
				{
					str += path[0].getRoomNumber();
					for (int j = 1 ; j < path.length ; j++)
						str += "-" + path[j].getRoomNumber();
					map += str + "\n";
				}
				else if (fDisplayDebugInfo)
					map += str + "?\n";
			}
		}
		for (int i = 1 ; i <= fMaze.getNumRooms() ; i++)
		{
			Room room = fMaze.getRoom(i);
			if (player.hasVisited(room) && room.hasDoor() && room.isDoorLocked())
			{
				unvisited++;
				if (room.isExitDoor() && Player.fKnowsExits)
				{
                    if (player.fKeys > 0)
						map += "{?}: ";
					else
						map += "{ }: ";
				}
				else
				{
                    if (player.fKeys > 0)
						map += "[?]: ";
					else
						map += "[ ]: ";
				}
                Room[] path = fMaze.findRoute(player.fRoom, room, player, Maze.USE_CLOSED_DOORS);
				if (path.length > 0)
				{
					String str = "" + path[0].getRoomNumber();
					for (int j = 1 ; j < path.length ; j++)
						str += "-" + path[j].getRoomNumber();
					map += str;
				}
				else
				{
					Utility.Trace("Cant happen");
				}
				map += "\n";
			}
		}

//		if (map == "")
		if (unvisited == 0)
			map = "All rooms have been visited!\n";
		else if (map == "")
			map = "???";

		if (unvisited < 5)
		{
			Utility.Trace("displayMapUnvisited - exits");
			for (int i = 1 ; i <= fMaze.getNumRooms() ; i++)
			{
				Room room = fMaze.getRoom(i);
				if (room.hasDoor() && room.isExitDoor() && !room.isDoorLocked())
				{
					map += "exit: ";
                    Room[] path = fMaze.findRoute(player.fRoom, room, player, Maze.USE_CLOSED_DOORS); // fMaze.findHermitRoute(player, room);
					if (path.length > 0)
					{
						String str = "" + path[0].getRoomNumber();
						for (int j = 1 ; j < path.length ; j++)
							str += "-" + path[j].getRoomNumber();
						map += str;
					}
                    else if (player.fRoom != room)
						map += "?-" + room.getRoomNumber();
					map += "\n";
				}
			}
		}

		Alert alert = new Alert(title);
		alert.setString(map);
		alert.setTimeout(Alert.FOREVER);
		fDisplay.setCurrent(alert, fRoomForm);
	}
	public void displayMapFountains()
	{
		Player player = fEngine.getCurrentPlayer();
		String title = "Fountains";
		String map = "";

		for (int i = 1 ; i <= fMaze.getNumRooms() ; i++)
		{
			Room room = fMaze.getRoom(i);
			boolean display = player.fKnowsFountains || player.hasExplored(room);
            if (display && room.fWater > 0)
			{
				map += room.getRoomNumber() + ": ";
                Room[] path = fMaze.findRoute(player.fRoom, room, player, Maze.USE_CLOSED_DOORS);
				//				Room[] path = fMaze.findHermitRoute(player, room);
				if (path.length > 0)
				{
					String str = "" + path[0].getRoomNumber();
					for (int j = 1 ; j < path.length ; j++)
						str += "-" + path[j].getRoomNumber();
					map += str;
				}
                else if (player.fRoom != room)
					map += "?";
				map += "\n";
			}
		}

		if (map == "")
			map = "You do not know where the fountains are!";

		Alert alert = new Alert(title);
		alert.setString(map);
		alert.setTimeout(Alert.FOREVER);
		fDisplay.setCurrent(alert, fRoomForm);
	}

	public void displayMapMisc()
	{
		Utility.Trace("displayMapMisc");
		Player player = fEngine.getCurrentPlayer();
        String title = "Misc (Room " + player.fRoom.getRoomNumber() + ")";
		String map = "";
        boolean show_all = GameEngine.instance.fDungeon.fCompleted;

		Utility.Trace("displayMapMisc - lairs");
		int lairs_left = 0;
		Room room = null;
		for (int i = 1 ; i <= fMaze.getNumRooms() ; i++)
		{
			room = fMaze.getRoom(i);
			if (room.isLair() && player.fKnowsLair && player.hasExplored(room) && !player.hasVisited(room))
            {
				lairs_left++;
				String lair = room.fLair.getMonsterDescription();
				Utility.Trace("lair; " + lair + lair.lastIndexOf(' ') + lair.substring(lair.lastIndexOf(' ') + 1));
				lair = lair.substring(lair.lastIndexOf(' ') + 1);
				map += lair + ": ";
                Room[] path = fMaze.findRoute(player.fRoom, room, player, Maze.USE_LOCKED_DOORS);
				if (path.length > 0)
                {
                    String str = "" + path[0].getRoomNumber();
                    for (int j = 1 ; j < path.length ; j++)
                        str += "-" + path[j].getRoomNumber();
                    map += str;
                }
                else if (player.fRoom != room)
					map += "?-" + room.getRoomNumber();
				map += "\n";
            }
		}
		Utility.Trace("displayMapMisc - chests");
		for (int i = 1 ; i <= fMaze.getNumRooms() ; i++)
		{
			room = fMaze.getRoom(i);
			if ((player.hasExplored(room) || show_all) && room.hasChest())
                if (room.fChest.fLocked)
                {
                    map += "[Chest]: ";
                    Room[] path = fMaze.findRoute(player.fRoom, room, player, Maze.USE_LOCKED_DOORS);
					if (path.length > 0)
                    {
                        String str = "" + path[0].getRoomNumber();
                        for (int j = 1 ; j < path.length ; j++)
                            str += "-" + path[j].getRoomNumber();
                        map += str;
                    }
                    else if (player.fRoom != room)
						map += "?-" + room.getRoomNumber();
					map += "\n";
                }
		}

		Utility.Trace("displayMapMisc - items");
		for (int i = 1 ; i <= fMaze.getNumRooms() ; i++)
		{
			room = fMaze.getRoom(i);
			String item = "";
            if (room.fDragonSwords > 0 && player.fDragonSwords < player.getMaxNumDragonSwords())
				item += "sword ";
            if (room.fSerpentShields > 0 && player.fSerpentShields < player.getMaxNumSerpentShields())
				item += "shield ";
            if (room.fSandals > 0 && player.fSandals == 0)
				item += "sandals ";
			if ((room.fRing | player.fRing) != player.fRing)
				item += "ring ";
			if (room.hasStaffs())
			{
				Staff best = room.fStaffs.getBestStaff();
				if (player.wantsStaff(best))
					item += room.fStaffs.getDescription();
			}
            if (room.fArmor > player.fArmor && room.fArmor <= player.getMaxArmor())
                item += Player.ARMOR_STRINGS[room.fArmor] + " ";
			if (room.hasChest())
			{
                Chest chest = room.fChest;
                if (!chest.fLocked || player.fKnowsItems)
				{
                    if (chest.fDragonSwords > 0)
						item += "sword ";
                    if (chest.fSerpentShields > 0)
						item += "shield ";
                    if (chest.fSandals > 0)
						item += "sandals ";
					if (chest.fStaffs != null && chest.fStaffs.getNumStaffs() > 0)
						item += "staff ";
                    if (chest.fArmor > 0)
						item += "armor ";
					if (chest.fRing > 0)
						item += "ring ";
				}
			}
			if (item != "" && (player.hasExplored(room) || player.fKnowsItems))
            {
                map += item;
                Room[] path = fMaze.findRoute(player.fRoom, room, player, Maze.USE_LOCKED_DOORS);
				if (path.length > 0)
                {
                    String str = "" + path[0].getRoomNumber();
                    for (int j = 1 ; j < path.length ; j++)
                        str += "-" + path[j].getRoomNumber();
                    map += str;
                }
                else if (player.fRoom != room)
					map += "?-" + room.getRoomNumber();
				map += "\n";
            }
		}

		Utility.Trace("displayMapMisc - coins");
		if (!show_all || lairs_left == 0)
		{
			for (int i = 1 ; i <= fMaze.getNumRooms() ; i++)
			{
				room = fMaze.getRoom(i);
				boolean show = false;
                if (room.fCoins > 0)
				{
					if (room.isLair())
					{
						if (player.hasExplored(room))
							show = true;
					}
					else
					{
						if (player.hasExplored(room) || show_all)
							show = true;
					}
				}
				if (show)
				{
                    if (room.fCoins == 1)
						map += "Coin: ";
					else
                        map += room.fCoins + " coins: ";
                    Room[] path = fMaze.findRoute(player.fRoom, room, player, Maze.USE_LOCKED_DOORS);
					if (path.length > 0)
					{
						String str = "" + path[0].getRoomNumber();
						for (int j = 1 ; j < path.length ; j++)
							str += "-" + path[j].getRoomNumber();
						map += str;
					}
                    else if (player.fRoom != room)
						map += "?-" + room.getRoomNumber();
					map += "\n";
				}
			}
		}

		Utility.Trace("displayMapMisc - keys");
        if (player.fKeys < player.getMaxNumKeys())
		{
			for (int i = 1; i <= fMaze.getNumRooms(); i++)
			{
				room = fMaze.getRoom(i);
                if ((player.hasExplored(room) || show_all) && room.fKeys > 0)
				{
					map += "Key: ";
                    Room[] path = fMaze.findRoute(player.fRoom, room, player, Maze.USE_LOCKED_DOORS);
					if (path.length > 0)
					{
						String str = "" + path[0].getRoomNumber();
						for (int j = 1; j < path.length; j++)
							str += "-" + path[j].getRoomNumber();
						map += str;
					}
                    else if (player.fRoom != room)
						map += "?-" + room.getRoomNumber();
					map += "\n";
				}
			}
		}
		Utility.Trace("displayMapMisc - carpets");
		for (int i = 1 ; i <= fMaze.getNumRooms() ; i++)
		{
			room = fMaze.getRoom(i);
			boolean hasCarpet = false;
            if (room.fCarpets > 0)
				hasCarpet = true;
			if (room.hasChest() && player.fKnowsCarpets)
			{
                if (room.fChest.fCarpets > 0)
					hasCarpet = true;
			}
			if (hasCarpet && (player.hasExplored(room) || player.fKnowsCarpets))
            {
                if (player.fCarpets == 0)
                {
                    map += "Carpet: ";
                    Room[] path = fMaze.findRoute(player.fRoom, room, player, Maze.USE_LOCKED_DOORS);
					if (path.length > 0)
                    {
                        String str = "" + path[0].getRoomNumber();
                        for (int j = 1 ; j < path.length ; j++)
                            str += "-" + path[j].getRoomNumber();
                        map += str;
                    }
                    else if (player.fRoom != room)
						map += "?-" + room.getRoomNumber();
					map += "\n";
                }
            }
		}

		// arrows
		Utility.Trace("displayMapMisc - arrows");
		if (player.getMaxNumShots() != 0 /*&& (!show_all || lairs_left == 0)*/)
		{
			for (int i = 1 ; i <= fMaze.getNumRooms() ; i++)
			{
				room = fMaze.getRoom(i);
                if (room.fArrows > 0 && (player.hasExplored(room) || show_all))
				{
                    map += room.fArrows + " arrows: ";
                    Room[] path = fMaze.findRoute(player.fRoom, room, player, Maze.USE_LOCKED_DOORS);
					if (path.length > 0)
					{
						String str = "" + path[0].getRoomNumber();
						for (int j = 1 ; j < path.length ; j++)
							str += "-" + path[j].getRoomNumber();
						map += str;
					}
                    else if (player.fRoom != room)
						map += "?-" + room.getRoomNumber();
					map += "\n";
				}
			}
		}

		// axes
		Utility.Trace("displayMapMisc - axes");
		int attacks = player.getMaxNumAttacks();
		if (attacks < 0)
			attacks = 1;
		if (attacks > player.getNumWeapons())
		{
			for (int i = 1 ; i <= fMaze.getNumRooms() ; i++)
			{
				room = fMaze.getRoom(i);
                if (room.fAxes > 0 && (player.hasExplored(room) || show_all))
				{
					map += "Axe: ";
                    Room[] path = fMaze.findRoute(player.fRoom, room, player, Maze.USE_LOCKED_DOORS);
					if (path.length > 0)
					{
						String str = "" + path[0].getRoomNumber();
						for (int j = 1 ; j < path.length ; j++)
							str += "-" + path[j].getRoomNumber();
						map += str;
					}
                    else if (player.fRoom != room)
						map += "?-" + room.getRoomNumber();
					map += "\n";
				}
			}
		}

		Utility.Trace("displayMapMisc - exits");
		for (int i = 1 ; i <= fMaze.getNumRooms() ; i++)
		{
			room = fMaze.getRoom(i);
			boolean display = false;
			if (room.hasDoor() && room.isExitDoor())
			{
				if (player.hasExplored(room))
					display = !room.isDoorLocked();
				else 
					display = player.fKnowsExits;
				if (show_all)
					display = true;
			}
			if (display)
			{
				map += "exit: ";
                Room[] path = fMaze.findRoute(player.fRoom, room, player, Maze.USE_LOCKED_DOORS);
				//				Room[] path = fMaze.findHermitRoute(player, room);
				if (path.length > 0)
				{
					String str = "" + path[0].getRoomNumber();
					for (int j = 1 ; j < path.length ; j++)
						str += "-" + path[j].getRoomNumber();
					map += str;
				}
                else if (player.fRoom != room)
					map += "?-" + room.getRoomNumber();
				map += "\n";
			}
		}

		Alert alert = new Alert(title);
		alert.setString(map);
		alert.setTimeout(Alert.FOREVER);
		fDisplay.setCurrent(alert, fRoomForm);
	}
	public void displayMapNotes()
	{
        DungeonRecord dungeon = GameEngine.instance.fDungeon;
		Player player = fEngine.getCurrentPlayer();
        Form form = new Form("Room " + player.fRoom.getRoomNumber());
		fNameField = null;
        fNameField = new TextField("Name ", dungeon.fName, 256, TextField.ANY);
		fDescriptionField = null;
        fDescriptionField = new TextField("\n", dungeon.fDescription, 256, TextField.ANY);
		if (true)
		{
			form.append(fNameField);
			form.append(fDescriptionField);
		}
		else
		{
            form.append(dungeon.fName);
		}

		form.addCommand(new Command("Note", Command.SCREEN, 0));			
		form.setCommandListener(this);
		fDisplay.setCurrent(form);
	}
	public void displayMapAdventurers()
	{
		Player player = fEngine.getCurrentPlayer();
		String title = "Adventurers";
		String map = "";

        for (int iPlayer = 1; iPlayer <= fEngine.fNumPlayers; iPlayer++)
		{
			Player pl = fEngine.getPlayer(iPlayer);
            //			String str  = pl.fExperienceLevel + ". " + Player.raceToString(pl.fRace) + " (" + pl.fHitPoints + ")";
            String str = pl.fExperienceLevel + ". " + pl.fName + " (" + pl.fHitPoints + ")";
			if (pl == player)
				str = str.toUpperCase();		
			map += str;
			if (pl.hasEscaped())
			{
				map += " escaped.\n";
			}
			else
			{
                Room room = pl.fRoom;
                Room[] path = fMaze.findRoute(player.fRoom, pl.fRoom, player, Maze.USE_CLOSED_DOORS);
				if (path.length > 0)
				{
					str = "" + path[0].getRoomNumber();
					for (int i = 1 ; i < path.length ; i++)
						str += "-" + path[i].getRoomNumber();
					map += ": " + str;
				}
                else if (player.fRoom != room)
					map += "?-" + room.getRoomNumber();
				if (!pl.isAlive())
					map += " dead";
				map += "\n";
			}
			str = "";
            if (pl.fCarpets == 0)
				str += "No Carpet! ";
			if (pl.getNumWeapons() == 0)
				str += "No weapons! ";
            if (pl.fArrows > 0)
                if (pl.fArrows == 1)
					str += "arrow ";
				else
                    str += pl.fArrows + " arrows ";
            if (pl.fKeys > 0)
                if (pl.fKeys == 1)
					str += "key ";
				else
                    str += pl.fKeys + " keys ";

            str += pl.fDragonSwords + "/" + pl.getMaxNumDragonSwords() + " swords ";
            str += pl.fSerpentShields + "/" + pl.getMaxNumSerpentShields() + " shields ";
		    str += pl.getStaffs() + "/" + pl.getMaxNumStaffs() + " staffs ";

            if (pl.fArmor > 0)
                str += Player.ARMOR_STRINGS[pl.fArmor] + " ";
			if (pl.fSandals > 0)
				str += "sandals ";
			if (pl.fRing != 0)
				str += "ring ";
			if (pl.fCoins > 0)
                if (pl.fCoins == 1)
					str += "coin ";
				else
                    str += pl.fCoins + " coins ";
			if (str != "")
				str += "\n";
			map += str;
		}

//		if (map == "")
//			map = "You are the only one left...";

		Alert alert = new Alert(title);
		alert.setString(map);
		alert.setTimeout(Alert.FOREVER);
		fDisplay.setCurrent(alert, fRoomForm);
	}
	public void displayKill(Monster[] monsters, Room room, Staff staff, boolean splinter)
	{
		fUsingMagic = false;
		if (staff != null)
			fUsingMagic = true;
		fShotsFired++;

		Utility.Trace("displayKill");
		Player player = fEngine.getCurrentPlayer();

		String str = "";
		boolean arrow = false;
        if (player.fRoom == room)
		{
			if (fUsingMagic)
				str = "You conjure a fireball with your " + staff.TYPE_STRING[staff.getType()] + " staff!\n";
		}
		else
		{
			if (fUsingMagic)
				str = "You fire a lightning bolt to cavern " + room.getRoomNumber() + ".\n";
			else
			{
				str = "You shoot an arrow to cavern " + room.getRoomNumber() + ".\n";
				arrow = true;
			}	
		}

		if (fUsingMagic)
		{
			if (splinter)
			{
				str += "Your " + staff.TYPE_STRING[staff.getType()] + " staff splintered!\n";
				if (fShooting)
					fShotsFired = player.getNumBolts();
				else
					fShotsFired = player.getNumBalls();
				int damage = GameEngine.rand.range(0, 2);
				player.applyDamage(damage);
				if (!player.isAlive())
					str += "\nYou're dead.\n";
				player.makeNoise(player.kFightNoise);
			}
		}

		if (!splinter && monsters.length == 0)
			str += "That wasn't very clever, was it?\n";

		if (monsters.length <= 5)
		{
			for (int i = 0 ; i < monsters.length ; i++)
			{
				if (monsters[i].isAlive())
					str += "You wounded ";
				else
				{
					str += "You killed ";
				}
				str += monsters[i].getMonsterDescription() + "\n";
			}
		}
		else
		{
			MonsterCounter mcKilled = new MonsterCounter();
			MonsterCounter mcWounded = new MonsterCounter();
			for (int i = 0 ; i < monsters.length ; i++)
			{
				Monster monster = monsters[i];
				if (monster.isAlive())
					mcWounded.countMonster(monster);
				else
					mcKilled.countMonster(monster);
			}
			if (mcKilled.getCount() > 0)
			{
				str += "You killed:\n";
				str += mcKilled.getContentString();
			}
			if (mcWounded.getCount() > 0)
			{
				str += "You wounded:\n";
				str += mcWounded.getContentString();
			}
		}

		if (arrow)
		{
            str += "You have " + Utility.numToStrLower(player.fArrows, "one arrow", "arrows") + " left.\n";
		}

		String caption = player.fName; // player.getNumber() + ". " + Player.raceToString(player.getRace());
		caption += " (HP:" + player.fHitPoints + ")";
//		if (fUsingMagic)
//			caption += " " + Staff.TYPE_STRING[staff.getType()];
		Form form = new Form(caption);
		form.append(str);
		boolean again = false;
		boolean fireball = false;
//		boolean lightning = false;
		if (fShooting)
		{
            if (fEngine.hasAdjacentMonster(player.fRoom))
			{
                if (!fUsingMagic && fShotsFired < player.getMaxNumShots() && player.fArrows > 0)
					again = true;
				if (fUsingMagic && fShotsFired < player.getNumBolts() && player.hasChargedStaff())
					again = true;
			}
		}
		else
		{
            if (fEngine.hasMonster(player.fRoom))
			{
				if (fUsingMagic && fShotsFired < player.getNumBalls() && player.hasChargedStaff())
					fireball = true;
			}
            else if (fEngine.hasAdjacentMonster(player.fRoom))
			{
				if (fUsingMagic && fShotsFired < player.getNumBolts() && player.hasChargedStaff())
				{
//					lightning = true;
					fShooting = true;
					again = true;
				}
				if (!fUsingMagic && player.getNumberOfAttacks() >= 2 && player.getNumberOfShots() >= 3 && player.canShoot())
				{
					fShooting = true;
					again = true;
				}
			}
		}
		
		if (fireball)
			form.addCommand(new Command("Fireball", Command.SCREEN, 0));
//		if (lightning)
//			form.addCommand(new Command("Lightning", Command.SCREEN, 0));			


		if (again)
		{
			form.addCommand(new Command("Continue  ", Command.SCREEN, 2));
		}
		else
			form.addCommand(new Command("Continue ", Command.SCREEN, 2));

		form.setCommandListener(this);
		fDisplay.setCurrent(form);
	}
	public void displaySearch()
	{
		Player player = fEngine.getCurrentPlayer();
        Room room = player.fRoom;
		String title = "Cavern " + room.getRoomNumber();
		String str = "Where do you wish to go?";

		Form form = new Form(title);
		//		form.append(str);
		fSearchEdit = null;
		fSearchEdit = new TextField(str, "", 10, TextField.NUMERIC);
		form.append(fSearchEdit);
		form.addCommand(new Command("Search", Command.SCREEN, 0));
		form.addCommand(new Command("Continue", Command.SCREEN, 1));
		form.setCommandListener(this);
		fDisplay.setCurrent(form);
	}
	public void displayPath(Room[] path)
	{
		Utility.Trace("displayPath(Room[] path)");
		//		if (path == null)
		//			Utility.Trace("path is null");
		//		Utility.Trace("path.length: " + path.length);
		Player player = fEngine.getCurrentPlayer();
        Room room = player.fRoom;
		String title = "Cavern " + room.getRoomNumber();
		String str = "Try:\n";
		if (path == null || path.length == 0)
			str = "You don't know how to get there.";
		else
		{
			str += path[0].getRoomNumber();
			//			Utility.Trace(str);
			for (int i = 1 ; i < path.length ; i++)
			{
				str += " - " + path[i].getRoomNumber();
				//				Utility.Trace(str);
			}
		}
		Utility.Trace(str);

		Alert alert = new Alert(title);
		alert.setString(str);
		alert.setTimeout(Alert.FOREVER);
		fDisplay.setCurrent(alert, fRoomForm);
	}
	public void displayWarning(String str)
	{
		Utility.Trace("displayWarning - " + str);
		String title = "Warning";
		Player player = fEngine.getCurrentPlayer();
		if (player != null)
            title = player.fName;
        Alert alert = new Alert(title /*player.fNumber + ". " + Player.raceToString(player.fRace)*/);
		alert.setString(str);
		alert.setTimeout(Alert.FOREVER);
		fDisplay.setCurrent(alert, fRoomForm);
	}
	public void displayWizard()
	{
		Utility.Trace("displayWizard");
		Player player = fEngine.getCurrentPlayer();

		if (fHasTalkedToWizard)
		{
			Alert alert = new Alert("Dungeons");
			alert.setString("The Wizard ignores you.");
			alert.setTimeout(Alert.FOREVER);
			fDisplay.setCurrent(alert, fRoomForm);
			return;
		}
		fHasTalkedToWizard = true;

		Form form = new Form("The Wizard says:");
        String str = "Greetings, " + Player.raceToString(player.fRace) + ".\n";
		str += "Would you like to purchase a Magic Staff?\n";
        str += "(You have " + player.fCoins + " coins)";
		form.append(str);

        int coins = player.fCoins;
        int count = Staff.TypeForMoney(coins);

        String[] strings = new String[count];
        for (int i = 1; i <= count; i++)
            strings[i-1] = Staff.TYPE_STRING[i];

        fChoices = null;
        fChoices = new ChoiceGroup("", Choice.EXCLUSIVE, strings, null);
        fChoices.setSelectedIndex(count-1, true);
        form.append(fChoices);
		form.addCommand(new Command("Buy Staff", Command.SCREEN, 0));
		form.addCommand(new Command("Continue", Command.SCREEN, 1));

        form.setCommandListener(this);
        fDisplay.setCurrent(form);

// 		String strCoins = "" + coins;
// 		fCoinField = null;
// 		fCoinField = new TextField("Coins:", strCoins, 10, TextField.NUMERIC);
// 		form.append(fCoinField);
// 		form.addCommand(new Command("Buy Staff", Command.SCREEN, 0));
// 		form.addCommand(new Command("Continue", Command.SCREEN, 1));
// 		form.setCommandListener(this);
// 		fDisplay.setCurrent(form);
	}
	public void buyStaff(int type)
	{
		Player player = fEngine.getCurrentPlayer();
        int available = player.fCoins;
//		int type = Staff.TypeForMoney(coins);
		int coins = Staff.VALUE[type - 1];

        int required = 0;
		Staff staff = null;
		if (type == Staff.NONE)
		{
			coins = 0;
		}
		else
		{
			Utility.Trace("Phone.buyStaff - type: " + type);
			staff = new Staff(player, type);
			// 		int required = GameEngine.kStaffCost; // GameEngine.rand.range(30, 50);
			required = staff.getValue();
		}
		String text = "";
		if (coins == 0)
			text = "Very well, it was up to you.";
		else if (coins > available)
		{
			text = "Very well, it was up to you.";
		}
		else if (coins < required)
		{
			text = "What!!!   Do you think that a Magic Staff is nothing but a piece of wood?\n";
			text += "I will tell you. It is very rare, very useful and VERY valuable.\n";
			text += "Be ready to offer more Gold Coins next time.";
		}
		else
		{
            if (fEngine.hasMonster(player.fRoom) && !player.hasChargedStaff() && player.getNumBalls() > 0)
				this.addCommand("Fireball", 7);
            if (fEngine.hasAdjacentMonster(player.fRoom) && !player.hasChargedStaff() && player.getNumBolts() > 0)
				this.addCommand("Lightning Bolt", 2);

			Utility.Assert(coins >= required, "Phone.buyStaff - coins >= required");
			text = "Thank you very much.\nUse your brand new Magic Staff wisely.";
			player.fCoins = player.fCoins - required;
            staff = player.addStaff(staff);
            if (staff != null)
            {
                player.fRoom.addStaff(staff);
            }
		}
		staff = null;

        Form form = new Form("The Wizard says:");
		form.append(text);
		form.addCommand(new Command("Continue ", Command.SCREEN, 0));
		form.setCommandListener(this);
		fDisplay.setCurrent(form);
	}
	public void displayJanitor()
	{
		Utility.Trace("displayJanitor");
		Player player = fEngine.getCurrentPlayer();

        if (fHasTalkedToJanitor || player.fCoins < player.fExperienceLevel || player.fCoins == 0)
		{
			Alert alert = new Alert("The Janitor says:");
			alert.setString("Quit bugging me!\nI have work to do.");
			alert.setTimeout(Alert.FOREVER);
			fDisplay.setCurrent(alert, fRoomForm);
			return;
		}
		fHasTalkedToJanitor = true;

		Form form = new Form("The Janitor says:");
		String str = "";//"\n";
        str += "Greetings, " + Player.raceToString(player.fRace) + ".\n";
		str += "Would you like to buy a key?\n";
        str += "(You have " + Utility.numToStrLower(player.fKeys, "one key", "keys") + ")\n";
		form.append(str);

		form.addCommand(new Command("Buy Key", Command.SCREEN, 0));
		form.addCommand(new Command("Continue", Command.SCREEN, 1));
		form.setCommandListener(this);
		fDisplay.setCurrent(form);
	}
	public void buyKeys(int keys)
	{
		Player player = fEngine.getCurrentPlayer();

		String text = "";
		if (keys == 0)
			text = "Very well, it was up to you.";
		else
		{
            Utility.Assert(player.fCoins >= keys * player.fExperienceLevel, "Phone.buyKeys - player.getCoins() >= keys * player.getExperienceLevel()");
			text = "Thank you.\nDon't go around opening every locked chest and door you find now.";
			if (((player.fRoom.hasDoor() && player.fRoom.isDoorLocked()) || (player.fRoom.hasChest() && player.fRoom.fChest.fLocked)) && player.fKeys == 0)
				this.addCommand("Unlock", 6);
            int cost = /*10 * */player.fExperienceLevel;
			if (cost == 0)
				cost = 1;
			player.fCoins = player.fCoins - keys * cost;
			player.fKeys = player.fKeys + keys;
		}

/*
		Alert alert = new Alert("The Janitor says:");
		alert.setString(text);
		alert.setTimeout(Alert.FOREVER);
		fDisplay.setCurrent(alert, fRoomForm);
*/
		Form form = new Form("The Janitor says:");
		form.append(text);
		form.addCommand(new Command("Continue ", Command.SCREEN, 0));
		form.setCommandListener(this);
		fDisplay.setCurrent(form);
	}
	public void displayPickLock()
	{
		Player player = fEngine.getCurrentPlayer();
        Room currentRoom = player.fRoom;
		String str;
		int pick = GameEngine.rand.range(1, 6);
        if (GameEngine.instance.fMaze.isJanitor(currentRoom))
            pick--;
		if (pick <= player.getLockPick())
		{
			str = "You successfully picked the lock!";

            if (currentRoom.hasChest() && currentRoom.fChest.fLocked)
			{
				currentRoom.unlockChest();
			}
			else
			{
				Utility.Assert(currentRoom.hasDoor(), "Phone.displayPickLock - currentRoom.hasDoor()");
				// unlock/open/close door
                Room door = currentRoom.fDoor;
				Utility.Assert(currentRoom.isDoorLocked(), "Phone.displayPickLock - currentRoom.isDoorLocked()");
				currentRoom.unlockDoor();
				currentRoom.openDoor();
				if (!door.isExit())
				{
					door.unlockDoor();
					door.openDoor();
				}
			}
		}
		else
		{
			str = "You failed to pick the lock.";
		}

		int damage = 0;
		// Spring trap on a 6, unless the player is a true master lock pick
		if (pick == 6 && player.getLockPick() <= 6)
		{
			boolean poison = false;
			if (currentRoom.hasChest())
			{
                poison = currentRoom.fChest.fPoisoned;
                currentRoom.fChest.fPoisoned = false;
			}
			else
			{
                poison = currentRoom.fPoisoned;
				currentRoom.fPoisoned = false;
			}
			if (poison)
			{
				damage = GameEngine.rand.range(1, 2);
				str += "\nYou stuck yourself on a poisoned needle!";
				player.applyDamage(damage);
				if (!player.isAlive())
					str += "\nYou're dead.";
			}
		}
        String caption = player.fName;
		if (damage > 0)
            caption += " (hp: " + player.fHitPoints + ")";
		Form form = new Form(caption);
		form.append(str);
		form.addCommand(new Command("Continue ", Command.SCREEN, 0));
		form.setCommandListener(this);
		fDisplay.setCurrent(form);
	}
	public void displayExit()
	{
		Utility.Trace("displayExit");
		Player player = fEngine.getCurrentPlayer();
        String str = "You have escaped with " + Utility.numToStrLower(player.fCoins, "one Gold Coin", "Gold Coins") + "!\n";
		str += "Wounds: " + player.getWoundDescription() + "\n";

		boolean completed = false;
        DungeonRecord dungeon = GameEngine.instance.fDungeon;
		{
			completed = true;
			for (int i = 1 ; i <= fMaze.getNumRooms() ; i++)
			{
				Room room = fMaze.getRoom(i);
				if (room.isLair())
				{
					if (!Player.hasVisited(room))
					{
						completed = false;
						break;
					}
				}
			}
		}
		if (completed)
		{
            int bonus = 5 * dungeon.fNumber - 5;
			if (bonus > 0)
			{
				str += "Dungeon bonus: " + bonus + "\n";
				player.updateScore(bonus);
			}

            bonus = dungeon.fNumber * fMaze.getNumRooms() / 4 - GameEngine.instance.fTurnCount;
            if (dungeon.fCompleted)
				bonus = 0;
			if (bonus > 0)
			{
				str += "Speed bonus: " + bonus + "\n";
				player.updateScore(bonus);
			}
		}
		else
		{
//			int bonus = player.fCoins / 5;
//			str += "Coin bonus: " + bonus + "\n";
//			player.updateScore(bonus);
		}

        PlayerRecord rec = player.fPlayerRecord;
        if (Player.GetLevel(player.fRace, player.getScore()) > player.fExperienceLevel)
		{
			// new level heals all wounds!
            int hp = player.fHitPoints;
			str += "You have attained another level!\n";
			int swords = player.getMaxNumDragonSwords();
			int shields = player.getMaxNumSerpentShields();
			int staffs = player.getMaxNumStaffs();

			player.applyExperience();

			if (swords != player.getMaxNumDragonSwords())
			{
				str += "You can wield another sword!\n";
			}
			if (shields != player.getMaxNumSerpentShields())
			{
				str += "You can use another shield!\n";
			}
			if (staffs != player.getMaxNumStaffs())
			{
				str += "You can use " + Utility.numToStr(player.getMaxNumStaffs()-staffs, "one more Magic Staff!\n", "more Magic Staffs!\n");
			}

			str += player.getLevelDescription() + "\n";
            if (hp > player.fHitPoints)
                player.fHitPoints = hp;
		}
		else
		{
            str += (Player.NextLevel(player.fRace, player.getScore()) - player.getScore()) + " xp until next level.\n";
		}

        Form form = new Form(player.fName /*player.fNumber + ". " + Player.raceToString(player.fRace) + "."*/);
		form.append(str);
		form.addCommand(new Command("Continue ", Command.SCREEN, 0));
		form.setCommandListener(this);
		fDisplay.setCurrent(form);
	}
	public void displayDead(String str)
	{
		Utility.Trace("displayDead: " + str);

		Player player = fEngine.getCurrentPlayer();
        Form form = new Form(player.fName /*player.fNumber + ". " + Player.raceToString(player.fRace)*/);
		form.append(str);
		form.addCommand(new Command("Continue ", Command.SCREEN, 0));
		form.setCommandListener(this);
		fDisplay.setCurrent(form);
	}
	public void displayGameOver()
	{
		Utility.Trace("displayGameOver - enter");

        DungeonRecord dungeon = GameEngine.instance.fDungeon;

		Utility.Trace("displayGameOver - updating players and configuration");
//		PlayerStore store = new PlayerStore();
//		Configuration config = new Configuration();
        //		boolean frontier = dungeon.fNumber >= fSuperStore.getDungeonLimit();
        boolean frontier = !dungeon.fCompleted;
		boolean completed = false;
		String str = "";
        for (int i = 1; i <= fEngine.fNumPlayers; i++)
		{
			Player player = fEngine.getPlayer(i);
            str += player.fExperienceLevel + ". ";
            //			str += Player.raceToString(player.fRace) + " ";
            str += player.fName + " ";
            PlayerRecord rec = player.fPlayerRecord;
			// Reset timestamp to indicate that this player participated in the session
			rec.fTimestamp = 0;
			Utility.Assert(rec != null, "Phone.displayGameOver - rec != null");
			if (player.isAlive())
			{
                str += player.fCoins + " coins";
				if (frontier)
				{
					completed = true;
					for (int iRoom = 1 ; iRoom <= fMaze.getNumRooms() ; iRoom++)
					{
						Room room = fMaze.getRoom(iRoom);
						if (room.isLair())
						{
							if (!Player.hasVisited(room))
							{
								completed = false;
								break;
							}
						}
					}
				}
				str += "\n";

//				rec.setName(player.getName());
				rec.fXP = player.getScore();
                rec.fHP = player.fHitPoints;

                rec.fCoins = player.fCoins;
                rec.fSwords = player.fDragonSwords;
                rec.fShields = player.fSerpentShields;
                rec.fArmor = player.fArmor;
				rec.fStaffs = player.getStaffArray();
                rec.fArrows = player.fArrows;

//				rec.fCarpets = player.getCarpets();
                // Get free sandals if they were burnt!
                if (rec.fSandals > 0 && player.fSandals == 0)
                    player.fSandals = 1;
                // only elf and magician and hobbit get to keep the sandals
                if (player.fRace == Player.ELF || player.fRace == Player.MAGICIAN || player.fRace == Player.HOBBIT)
                    rec.fSandals = player.fSandals;
                rec.fKeys = player.fKeys;
                rec.fAxes = player.fAxes;
				rec.fRing = player.fRing;
			}
			else
			{
				// Hmmm. Should this be called now that I want to revive dead players?
// 				fSuperStore.resetParty();
                str += player.fCoins + " coins. Dead!\n";
// 				rec.fXP = 0;
// 				rec.fHP = 0;

				// Lose half experience
// 				rec.fXP = player.getScore() / 2;
				// Lose 20% xp
//				rec.fXP = player.getScore() * 4 / 5;
                // No xp loss for dying
                rec.fXP = player.getScore();
				// Don't really die
				rec.fHP = 1;	
				// And keep your stuff as well
// 				rec.fCoins = 0;
// 				rec.fSwords = 0;
// 				rec.fShields = 0;
// 				rec.fStaffs = 0;
// 				rec.fArrows = 0;
// 				rec.fKeys = 0;
// 				rec.fAxes = 0;
				// but get rid of the ring
				rec.fRing = player.RING_NONE;
			}
//			rec.fDirty = true;
//			store.setPlayerRecord(rec);
		}

		if (frontier && completed)
		{
            Utility.Trace("Increasing dungeon limit to " + (dungeon.fNumber + 1));
            fSuperStore.setDungeonLimit(dungeon.fNumber + 1);
            fSuperStore.setLastDungeonNumber(dungeon.fNumber + 1);
            String name = dungeon.fName;
            if (name != "")
                name += " - ";
			name += fMaze.getDungeonName();
            dungeon.fName = name;
            dungeon.fContent = fMaze.fContent;
			dungeon.fCompleted = true;
		}
		//		config.close();
//		config = null;
//		store.close();
//		store = null;

// 		System.gc();
//		fEngine.dispose();
//		System.gc();

		Utility.Trace("displayGameOver - updating dungeon");
//		DungeonStore dungeons = new DungeonStore();

		int closed = 5 * fSuperStore.getDungeonLimit();
		if (closed == 0)
			closed = 1;
		if (closed > 60)
			closed = 60;
		dungeon.setClosed(closed);


//		dungeons.setDungeonRecord(dungeon);
//		dungeons.close();
//		dungeons = null;

		System.gc();

		Utility.Trace("displayGameOver - creating form");
		if (Utility.ASSERT_LOG != "")
		{
			Utility.Trace("displayGameOver - adding ASSERT log!");
			str += Utility.ASSERT_LOG;
		}
		Form form = new Form("Dungeons");
		form.append(str);
        if (fEngine.fNumPlayers > 1)
			form.addCommand(new Command("Pool", Command.SCREEN, 0));
		form.addCommand(new Command("Exit", Command.EXIT, 2));
		form.setCommandListener(this);
		fDisplay.setCurrent(form);

		Utility.Trace("displayGameOver - exit");
	}
	public void displayProgramError(String msg)
	{
		Utility.Trace("displayProgramError");
		String str = msg;

		if (Utility.ASSERT_LOG.length() > 0)
			str += "\nASSERT:\n" + Utility.ASSERT_LOG;

		str += "\nTRACE:\n" + Utility.getTraceString();

		Form form = new Form("Program Error");
		form.append(str);
		form.addCommand(new Command("Exit", Command.EXIT, 0));
		form.setCommandListener(this);
		fDisplay.setCurrent(form);
	}
	public void flushTrace()
	{
		Utility.TRACE = false;
//		Configuration config = new Configuration();
		fSuperStore.setTrace(Utility.getTraceString());
//		config.close();
//		config = null;
		Utility.TRACE = true;
	}
	public void displayShout()
	{
		Utility.Trace("displayShout");

		Player player = fEngine.getCurrentPlayer();
		player.makeNoise(Player.kFightNoise);

		String str = "You scream at the top of your voice!";

		Alert alert = new Alert("Dungeons");
		alert.setString(str);
		alert.setTimeout(Alert.FOREVER);
		fDisplay.setCurrent(alert, fRoomForm);
	}
	public void displayTrace()
	{
		Utility.Trace("displayTrace");
		String str = "";

		if (Utility.ASSERT_LOG.length() > 0)
			str += "\nASSERT:\n" + Utility.ASSERT_LOG;

		str += "\nTRACE:\n" + Utility.getTraceString();

		Alert alert = new Alert("Trace");
		alert.setString(str);
		alert.setTimeout(Alert.FOREVER);
		fDisplay.setCurrent(alert, fRoomForm);
	}
	public void commandAction(Command c, Displayable s)
	{
		Utility.Trace("commandAction: " + c.getLabel());

		if (c.getCommandType() == Command.EXIT)
		{
			Utility.Trace("fMIDlet.doExit()");
			fMIDlet.doExit();
		}
		else if (c.getCommandType() == Command.BACK)
		{
			Utility.Trace("Command.BACK");
			//			fMIDlet.doPause();
			fDisplay.setCurrent(fRoomForm);
		}
		else if (c.getCommandType() == Command.CANCEL)
		{
			Utility.Trace("Command.CANCEL");
			//			fMIDlet.doPause();
			fDisplay.setCurrent(fRoomForm);
		}
		else if (c.getCommandType() == Command.STOP)
		{
			Utility.Trace("Command.STOP");
			//			fMIDlet.doPause();
			fDisplay.setCurrent(fRoomForm);
		}
		else
		{
			Player player = null;
			if (fEngine != null)
				player = fEngine.getCurrentPlayer();
			if (player != null && c.getLabel() != "" && c.getLabel().indexOf("Continue") == -1)
			{
				for (int i = Player.kNumCommands - 1 ; i > 0 ; --i)
					player.fCommands[i] = player.fCommands[i-1];
					
				//			player.fCommands[2] = player.fCommands[1];
				//			player.fCommands[1] = player.fCommands[0];
				player.fCommands[0] = c.getLabel();
				for (int i = 0 ; i < Player.kNumCommands ; ++i)
					Utility.Trace("Command " + i + ": " + player.fCommands[i]);
			}
			if (c.getLabel() == "Move")
			{
				fMoving = true;
				fShooting = false;
				fShotsFired = 0;
				Utility.Trace("fEngine.doCommandMove()");
				fEngine.doCommandMove();
			}
				//		else if (c.getLabel() == "Shoot arrow")
			else if (c.getLabel().startsWith("Shoot arrow"))
			{
				fShooting = true;
				fMoving = false;
				fShotsFired = 0;
				Utility.Trace("fEngine.doCommandShootArrow()");
				fEngine.doCommandShootArrow();
			}
				//		else if (c.getLabel() == "Lightning Bolt")
			else if (c.getLabel().startsWith("Lightning Bolt"))
			{
				fShooting = true;
				fMoving = false;
				fShotsFired = 0;
				Utility.Trace("fEngine.doCommandCastLightningBolt()");
				fEngine.doCommandCastLightningBolt();
			}
			else if (c.getLabel() == "Take")
			{
				Utility.Trace("fEngine.doCommandTake()");
				fEngine.doCommandTake();
			}
			else if (c.getLabel() == "Open" || c.getLabel() == "Close")
			{
				Utility.Trace("fEngine.doCommandDoor()");
				fEngine.doCommandDoor(false);
			}
			else if (c.getLabel() == "Unlock")
			{
				Utility.Trace("fEngine.doCommandDoor()");
				fEngine.doCommandDoor(true);
			}
			else if (c.getLabel() == "Pick Lock")
			{
				//			Utility.Trace("fEngine.doCommandPickLock()");
				//			fEngine.doCommandPickLock();
				this.displayPickLock();
				//			fEngine.processNextPlayer();
			}
			else if (c.getLabel() == "Inventory")
			{
				Utility.Trace("Phone.displayInventory()");
				if (player.fCommands[0] == player.fCommands[1] && player.fCommands[1] == player.fCommands[2])
				{
					fDisplayDebugInfo = !fDisplayDebugInfo;
					fEngine.fDisplayDebugInfo = !fEngine.fDisplayDebugInfo;
				}
				this.displayInventory();
			}
			//else if (c.getLabel() == "Read slab")
			//{
			//    Utility.Trace("fEngine.doDisplayHighScores()");
			//    this.displayHighScores();
			//}
				//		else if (c.getLabel() == "Fight")
			else if (c.getLabel().startsWith("Fight"))
			{
                fShooting = false;
                Utility.Trace("fEngine.doCommandFight()");
				fEngine.doCommandFight();
			}
				//		else if (c.getLabel() == "Fireball")
			else if (c.getLabel().startsWith("Fireball"))
			{
//                fShooting = false;
                Utility.Trace("fEngine.doCommandCastFireball()");
				fEngine.doCommandCastFireball();
			}
			else if (c.getLabel() == "Map")
			{
				Utility.Trace("Phone.displayMap()");
				this.displayMap();
			}
			else if (c.getLabel() == "Wait")
			{
				Utility.Trace("Wait - fEngine.processNextPlayer()");
				fEngine.doCommandWait();
// 				fEngine.processNextPlayer();
			}
//            else if (c.getLabel() == "Drink")
            else if (c.getLabel().startsWith("Drink"))
            {
				Utility.Trace("Drink - fEngine.processNextPlayer()");
                int water = player.fWater;
				if (water <= 4)
					water = 0;
				else if (water <= 8)
					water = 4;
				else
					water = 8;
				player.fWater = water;
				fEngine.processNextPlayer();
			}
			else if (c.getLabel() == "Make noise")
			{
				Utility.Trace("Phone.displayShout()");
				this.displayShout();
			}
			else if (c.getLabel() == "Trace")
			{
				Utility.Trace("Phone.displayTrace()");
				this.displayTrace();
			}
			else if (c == List.SELECT_COMMAND /*|| c.getLabel() == "Select"*/)
			{
				Utility.Trace("List.SELECT_COMMAND");
				Choice choice = (Choice) s;
				int index = choice.getSelectedIndex();
				String str = choice.getString(index);
				Utility.Trace("str: " + str + " index: " + index);

				if (str == " ...")
				{
					// Display search form
					this.displaySearch();
					return;
				}

                Room currentRoom = player.fRoom;
				Room room = null;
				if (str.endsWith("Exit") || str.startsWith("Door"))
					index = currentRoom.getNumPassages();
				if (index >= currentRoom.getNumPassages())
				{
					Utility.Assert(currentRoom.hasDoor(), "Phone.commandAction(List) - currentRoom.hasDoor()");
					Utility.Assert(currentRoom.isDoorOpen(), "Phone.commandAction(List) - currentRoom.isDoorOpen()");
                    room = currentRoom.fDoor;
				}
				else
				{
					//				room = currentRoom.getPassage(index + 1);
					int no;
					int to = str.indexOf(" ");
					if (to != -1)
						str = str.substring(0, to);					
					Utility.Trace("str: " + str);
					no = Integer.parseInt(str);
					Utility.Trace("no: " + no);
					room = fMaze.getRoom(no);
				}
				if (fMoving)
					fEngine.doMove(room);
				else
				{
					if (player != null)
					{
						Utility.Trace("Last shot: " + room.getRoomNumber());
                        player.fLastTarget = room.getRoomNumber();
					}
					Utility.Assert(fShooting, "Phone.commandAction(List) - fShooting");
					if (fUsingMagic)
						fEngine.doCastLightningBolt(room);
					else
						fEngine.doShootArrow(room);
				}
			}
			else if (c.getLabel() == "Continue")
			{
				// continue - same turn
				Utility.Trace("Continue - fEngine.doEncounters()");
				fEngine.doEncounters();
			}
			else if (c.getLabel() == "Continue ")
			{
				// continue - next player
				Utility.Trace("Continue - fEngine.processNextPlayer()");
				fEngine.processNextPlayer();
			}
			else if (c.getLabel() == "Continue  ")
			{
				// continue - shoot again
				if (fUsingMagic)
					fEngine.doCommandCastLightningBolt();
				else
					fEngine.doCommandShootArrow();
			}
			else if (c.getLabel() == "Search")
			{
				Utility.Trace("Search - this.displayPath(path)");
				String str = fSearchEdit.getString();
				Room[] path = null;
				if (str.length() > 0)
				{
					int no = Integer.parseInt(str);
                    Room from = player.fRoom;
					Room to = fMaze.getRoom(no);
					path = fMaze.findRoute(from, to, player, Maze.USE_CLOSED_DOORS);
					Utility.Trace("Search - this.displayPath(path)");
				}
				this.displayPath(path);
			}
			else if (c.getLabel() == "Talk to Wizard")
			{
				Utility.Trace("Talk to Wizard");
				if (player.fCoins < 250)
					player.borrowCoins();
                this.displayWizard();
			}
			else if (c.getLabel() == "Talk to Janitor")
			{
				Utility.Trace("Talk to Janitor");
				this.displayJanitor();
			}
//			else if (c.getLabel() == "Give Coins")
//			{
//				String str = fCoinField.getString();
//				int coins = 0;
//				if (str.length() > 0)
//					coins = Integer.parseInt(str);
//				this.giveCoins(coins);
//			}
			else if (c.getLabel() == "Buy Staff")
			{
// 				String str = fCoinField.getString();
// 				int coins = 0;
// 				if (str.length() > 0)
// 					coins = Integer.parseInt(str);
                int staff = fChoices.getSelectedIndex();
                this.buyStaff(staff+1);
			}
			else if (c.getLabel() == "Buy Key")
			{
				//			String str = fCoinField.getString();
				//			int keys = 0;
				//			if (str.length() > 0)
				//				keys = Integer.parseInt(str);
				//			this.buyKeys(keys);
				this.buyKeys(1);
			}
//			else if (c.getLabel() == "Display") // hermit
//			{
//				int info = fChoices.getSelectedIndex();
//				this.displayHermitInformation(info);
//			}
			else if (c.getLabel() == "Display ") // maps
			{
				int info = fChoices.getSelectedIndex();
				this.displayMapInformation(info);
			}
			else if (c.getLabel() == "Upload")
			{
			}
			else if (c.getLabel() == "Download")
			{
			}
			else if (c.getLabel() == "Note")
			{
				Utility.Trace("Note");
                DungeonRecord dungeon = GameEngine.instance.fDungeon;
                dungeon.fName = fNameField.getString();
                dungeon.fDescription = fDescriptionField.getString();
				fDisplay.setCurrent(fRoomForm);
			}
			else if (c.getLabel() == "Pool")
			{
				Utility.Trace("Pool");

				int gold = 0;
				int gold_divisor = 0;
				int gold_allotment = 0;
				int arrows = 0;
				int arrow_divisor = 0;
				int arrow_allotment = 0;
                for (int i = 1; i <= fEngine.fNumPlayers; i++)
				{
					Player player2 = fEngine.getPlayer(i);
                    Utility.Trace(player2.fName + " gold: " + player2.fCoins + " arrows: " + player2.fArrows);
                    gold += player2.fCoins;
                    gold_allotment = 2 * player2.getMaxNumStaffs() - player2.getStaffs() + POOL_BONUS_GOLD[player2.fRace - 1];
					gold_divisor += gold_allotment;

                    arrows += player2.fArrows;
					int shots = player2.getMaxNumShots();
					if (shots < 0)
						shots = 1;
                    arrow_allotment = shots + POOL_BONUS_ARROWS[player2.fRace - 1];
					arrow_divisor += arrow_allotment;
                    Utility.Trace(player2.fName + " gold allot: " + gold_allotment + " arrow allot: " + arrow_allotment);
				}
				Utility.Trace(" gold: " + gold + " gold div: " + gold_divisor);
				Utility.Trace(" arrows: " + arrows + " arrow div: " + arrow_divisor);
//				if (gold_divisor == 0)
//					gold_divisor = 1;
//				if (arrow_divisor == 0)
//					arrow_divisor = 1;
                for (int i = fEngine.fNumPlayers; i > 0; i--)
				{
					Player player2 = fEngine.getPlayer(i);
                    PlayerRecord rec = player2.fPlayerRecord;
                    gold_allotment = 2 * player2.getMaxNumStaffs() - player2.getStaffs() + POOL_BONUS_GOLD[player2.fRace - 1];
					if (gold_divisor == 0)
						rec.fCoins = 0;
					else
						rec.fCoins = (gold_allotment * gold) / gold_divisor;
					gold -= rec.fCoins; gold_divisor -= gold_allotment;
//					if (gold_divisor == 0)
//						gold_divisor = 1;

					int shots = player2.getMaxNumShots();
					if (shots < 0)
						shots = 1;
                    arrow_allotment = shots + POOL_BONUS_ARROWS[player2.fRace - 1];
					if (arrow_divisor == 0)
						rec.fArrows = 0;
					else
						rec.fArrows = (arrow_allotment * arrows) / arrow_divisor;
					arrows -= rec.fArrows; arrow_divisor -= arrow_allotment;
//					if (arrow_divisor == 0)
//						arrow_divisor = 1;
                    Utility.Trace(player2.fName + " gold: " + rec.fCoins + " arrows: " + rec.fArrows);
					Utility.Trace(" gold: " + gold + " gold div: " + gold_divisor);
					Utility.Trace(" arrows: " + arrows + " arrow div: " + arrow_divisor);
				}
				Utility.Assert(gold == 0, "gold == 0");
				Utility.Assert(arrows == 0, "arrows == 0");
//				Utility.Assert(gold_divisor == 0, "gold_divisor == 0");
//				Utility.Assert(arrow_divisor == 0, "arrow_divisor == 0");

				fMIDlet.doExit();
			}
// 			else if (c.getLabel() == "Borrow gold")
// 			{
// 				Utility.Trace("Borrow gold");
// 				player.borrowCoins();
// 				this.displayWizard();
// 			}
			else
			{
				Utility.Trace("Unknown command");
				fDisplay.setCurrent(fRoomForm);
			}
		}
	}

	protected Image getRoomImage(int visited, boolean muck, boolean lair, boolean exit)
	{
		if (muck)
		{
			if (visited == IMAGE_LAST)
				return createRoomImage(fImgRecentMuck);
			if (visited == IMAGE_RECENT)
				return createRoomImage(fImgRecentMuck);
			if (visited == IMAGE_VISITED)
				return createRoomImage(fImgVisitedMuck);
			if (visited == IMAGE_UNVISITED)
				return createRoomImage(fImgUnvisitedMuck);
		}

		if (lair && visited == IMAGE_UNVISITED)
        {
            return createRoomImage(fImgLair);
        }

        if (visited == IMAGE_LAST)
			return createRoomImage(fImgLast);
		if (exit)
		{
			if (visited == IMAGE_UNVISITED)
				return createRoomImage(fImgExit);
			return createRoomImage(fImgExitRoom);
		}
		if (visited == IMAGE_RECENT)
			return createRoomImage(fImgRecent);
		if (visited == IMAGE_VISITED)
			return createRoomImage(fImgVisited);
		if (visited == IMAGE_UNVISITED)
			return createRoomImage(fImgUnvisited);
		return createRoomImage(fImgExit);
	}

	protected Image createRoomImage(Image source)
	{
		return source;
//		Image copy = Image.createImage(source.getWidth(), source.getHeight());
//		Graphics g = copy.getGraphics();
//		g.drawImage(source, 0, 0, Graphics.TOP|Graphics.LEFT);
//		return copy;
	}
}
