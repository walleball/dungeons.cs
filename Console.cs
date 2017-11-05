using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Dungeons
{
	class Console : Device
	{
		private GameEngine fEngine;
		private Maze fMaze;
		private int fShotsFired;
		private bool fShooting;

		public Console()
		{
			fEngine = null;
			fMaze = null;
		}
		public void setEngine(GameEngine engine)
		{
			fEngine = engine;
			fMaze = fEngine.fMaze;
		}
		override public void displayRoom(Room room)
	{
        fShotsFired = 0;

		Player player = fEngine.getCurrentPlayer();

		String str = fEngine.getAdjacentMonsterDescription(room);
		str += room.getAdjacentMonsterDescription();
		str += room.getAdjacentPlayerDescription();
		str = str + room.getDescription();
		str += "The room contains:\n";
		String content = room.getContentDescription();
		content += fEngine.getMonsterContentDescription(room);
		if (content.Length == 0)
			content = "Nothing";
		str += content;

		//		str = str + "\n" + "You are in cavern " + room.getRoomNumber();
		System.Console.WriteLine(str);
		System.Console.WriteLine("What do you wish to do?");
		if (player.canMove())
			System.Console.WriteLine("1   Move");
		if (fEngine.hasAdjacentMonster(room) && player.canShoot())
			System.Console.WriteLine("2   Shoot");
		if (room.hasTakeableContent() && player.needsContent(room))
			System.Console.WriteLine("3   Take");
		if (room.hasDoor())
		{
			if (!room.isDoorLocked())
				System.Console.WriteLine("4   Open/close door");
			else if (player.hasKey())
				System.Console.WriteLine("4   Unlock/lock door");
		}
		System.Console.WriteLine("5   Inventory");
		if (fEngine.hasAdjacentMonster(room) && player.hasChargedStaff())
			System.Console.WriteLine("6   Cast Bolt");
		if (fEngine.hasMonster(room) && player.canAttack())
			System.Console.WriteLine("7   Fight");
		if (fEngine.hasMonster(room) && player.hasChargedStaff())
			System.Console.WriteLine("8   Cast Fireball");
		System.Console.WriteLine("9   Wait");
		System.Console.WriteLine("10   Map");

		while (true)
		{
			for (int i = Player.kNumCommands - 1; i > 0; --i)
				player.fCommands[i] = player.fCommands[i - 1];
			player.fCommands[0] = "";

			int no = this.readNumber();
			if (no == 1)
			{
				player.fCommands[0] = "Move";
				fEngine.doCommandMove();
				break;
			}
			if (no == 2)
			{
				fEngine.doCommandShootArrow();
				break;
			}
			if (no == 3)
			{
				fEngine.doCommandTake();
				break;
			}
			if (no == 4)
			{
				fEngine.doCommandDoor(true);
				break;
			}
			if (no == 5)
			{
				this.displayInventory();
				break;
			}
			if (no == 6)
			{
				fEngine.doCommandCastLightningBolt();
				break;
			}
			if (no == 7)
			{
				fEngine.doCommandFight();
				break;
			}
			if (no == 8)
			{
				fEngine.doCommandCastFireball();
				break;
			}
			if (no == 9)
			{
				fEngine.processNextPlayer();
				break;
			}
			if (no == 10)
			{
				this.displayMap();
				break;
			}
		}
	}
		override public void displayMove(Room room)
	{
        fShooting = false;
		String str = "There are passages leading to:";
		System.Console.WriteLine(str);
		for (int i = 1 ; i <= room.getNumPassages() ; i++)
		{
			if (room.isPassable(i))
			{
				Room otherRoom = room.getPassage(i);
				str = "(" + otherRoom.getRoomNumber() + ")";
				Player player = fEngine.getCurrentPlayer();
				if (Player.hasVisited(otherRoom))
				{
					if (player.fLastRoom == otherRoom)
						str = "[" + Convert.ToString(otherRoom.getRoomNumber()) + "]";
					else
						str = Convert.ToString(otherRoom.getRoomNumber());
				}
				str = str + " ";
				System.Console.Write(str);
			}
		}
		if (room.hasDoor() && room.isDoorOpen())
		{
			str = "Door: ";
			System.Console.Write(str);
            Room otherRoom = room.fDoor;
			str = "(" + otherRoom.getRoomNumber() + ")";
			Player player = fEngine.getCurrentPlayer();
			if (otherRoom.isExit())
				str = "exit";
			else if (Player.hasVisited(otherRoom))
			{
                if (player.fLastRoom == otherRoom)
					str = "<" + Convert.ToString(otherRoom.getRoomNumber()) + ">";
				else
					str = Convert.ToString(otherRoom.getRoomNumber());
			}
			str = str + " ";
			System.Console.Write(str);
		}

		System.Console.WriteLine();
		int no = 0;
		Room toRoom = null;
		while (true)
		{
			no = this.readNumber();
			toRoom = fMaze.getRoom(no);
            if (room.hasPassage(toRoom) || room.fDoor == toRoom)
			{
				break;
			}
			else
				System.Console.WriteLine("No passage leads to " + no);
		}
		fEngine.doMove(toRoom);
	}
		override public void displayShoot(Room room, Staff staff)
	{
        fShooting = true;
		String str = "There are passages leading to:";
		System.Console.WriteLine(str);
		for (int i = 1 ; i <= room.getNumPassages() ; i++)
		{
			Room otherRoom = room.getPassage(i);
			str = "(" + otherRoom.getRoomNumber() + ")";
			Player player = fEngine.getCurrentPlayer();
			if (Player.hasVisited(otherRoom))
			{
                if (player.fLastRoom == otherRoom)
					str = "<" + Convert.ToString(otherRoom.getRoomNumber()) + ">";
				else
					str = Convert.ToString(otherRoom.getRoomNumber());
			}
			str = str + " ";
			System.Console.Write(str);
		}
		if (room.hasDoor() && room.isDoorOpen())
		{
			str = "Door: ";
			System.Console.Write(str);
            Room otherRoom = room.fDoor;
			str = "(" + otherRoom.getRoomNumber() + ")";
			Player player = fEngine.getCurrentPlayer();
			if (Player.hasVisited(otherRoom))
			{
                if (player.fLastRoom == otherRoom)
					str = "<" + Convert.ToString(otherRoom.getRoomNumber()) + ">";
				else
					str = Convert.ToString(otherRoom.getRoomNumber());
			}
			str = str + " ";
			System.Console.Write(str);
		}

		System.Console.WriteLine();
		int no = 0;
		Room toRoom = null;
		while (true)
		{
			no = this.readNumber();
            if (no == 0)
                return;
			toRoom = fMaze.getRoom(no);
            if (room.hasPassage(toRoom) || room.fDoor == toRoom)
			{
				break;
			}
			else 
				System.Console.WriteLine("No passage leads to " + no);
		}
		if (staff != null)
			fEngine.doCastLightningBolt(toRoom);
		else
			fEngine.doShootArrow(toRoom);
	}
		override public void displayEncounter(Monster monster, String message)
	{
		Player player = fEngine.getCurrentPlayer();
		String str = "";
		if (monster.isAlive())
			str = monster.getEncounterDescription(player.isAlive());
		else
		{
			Utility.Assert(monster is Dragon, "Console.displayEncounter - monster is Dragon");
			str = "The Dragon rushes at you...\n";
			str += "But before it had time to roast you, you slew it with a fierceful swing with the mighty Sword\n";
			str += "You are now a DRAGON-SLAYER.";
		}
		if (message.Length > 0)
			str += "\r\n" + message;
		System.Console.WriteLine(str);
		fEngine.doEncounters();
	}
		override public void displayEncounter(String title, String str)
		{
			this.displayEncounter(str);
		}
		override public void displayEncounter(String str)
	{
		System.Console.WriteLine(str);
		fEngine.doEncounters();
	}
		public void displayInventory()
	{
		Player player = fEngine.getCurrentPlayer();
        System.Console.WriteLine("This is You, " + Player.raceToString(player.fRace));
        System.Console.WriteLine("STRENGTH: " + player.fHitPoints);
		System.Console.WriteLine("You are carrying:");

		String content = "";
		// arrows
        if (player.fArrows > 0)
		{
            content += Utility.numToStr(player.fArrows, "An Arrow", "Arrows") + "\n";
		}
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
		// axes
        if (player.fAxes > 0)
		{
            content += Utility.numToStr(player.fAxes, "An Axe", "Axes") + "\n";
		}
		// carpets
        if (player.fCarpets > 0)
		{
            content += Utility.numToStr(player.fCarpets, "A Flying Carpet", "Flying Carpets") + "\n";
		}
		// swords
        if (player.fDragonSwords > 0)
		{
            content += Utility.numToStr(player.fDragonSwords, "A Dragon Sword", "Dragon Swords") + "\n";
		}
		// shields
        if (player.fSerpentShields > 0)
		{
            content += Utility.numToStr(player.fSerpentShields, "A Serpent Shield", "Serpent Shields") + "\n";
		}
		// armor
        if (player.fArmor > 0)
		{
            content += Player.ARMOR_STRINGS[player.fArmor] + "\n";
		}
		// staffs
		if (player.getStaffs() > 0)
		{
			content += Utility.numToStr(player.getStaffs(), "A Magic Staff", "Magic Staffs") + "\n";
            content += player.getStaffDescriptions() + "\n";
		}
		// sandals
        if (player.fSandals > 0)
		{
            content += Utility.numToStr(player.fSandals, "The Sandals of Silence", "Sandals of Silence") + "\n";
		}
		// ring
		if (player.fRing != 0)
		{
			content += Player.GetRingDescription(player.fRing) + "\n";
		}
		//water
        content += Utility.numToStr(player.fWater, "One unit of Water", "units of Water") + "\n";

		content += player.getLevelDescription() + "\n";
		if (content == "")
			content = "Nothing";
		System.Console.WriteLine(content);
        this.displayRoom(player.fRoom);
	}
		public void displayMap()
	{
		Player player = fEngine.getCurrentPlayer();

		for (int i = 1 ; i < fMaze.getNumRooms() ; i++)
		{
			Room room = fMaze.getRoom(i);
			if (Player.hasVisited(room))
				System.Console.WriteLine(room.getShortDescription());
		}
        this.displayRoom(player.fRoom);
	}
		override public void displayExit()
	{
		Player player = fEngine.getCurrentPlayer();
        System.Console.WriteLine("You have escaped with " + Utility.numToStr(player.fCoins, "one Gold Coin", "Gold Coins") + "!");
//		System.Console.WriteLine("You have escaped!");
		fEngine.processNextPlayer();
	}
		override public void displayDead(String strr)
	{
		System.Console.WriteLine(strr);
	}
		override public void displayGameOver()
	{
		System.Console.WriteLine("Game Over!");
		String str = "";
        for (int i = 1; i <= fEngine.fNumPlayers; i++)
		{
			Player player = fEngine.getPlayer(i);
            str += player.fName + " the " + Player.raceToString(player.fRace);
			if (player.isAlive())
				str += " escaped";
            str += " " + Utility.numToStr(player.fCoins, "one Gold Coin", "Gold Coins") + "\n";
		}
		System.Console.WriteLine(str);
		System.Console.WriteLine("TRACE:\n======\n" + Utility.getTraceString());
		fEngine.dispose();
		fEngine = null;
		this.readNumber();
	}
		override public void displayProgramError(String msg)
	{
		System.Console.WriteLine("displayProgramError");
		String str = msg;

		if (Utility.ASSERT_LOG.Length > 0)
			str += "\nASSERT:\n" + Utility.ASSERT_LOG;

		str += "\nTRACE:\n" + Utility.getTraceString();

		System.Console.WriteLine(str);
		this.readNumber();
	}
		override public void flushTrace()
	{
		System.Console.WriteLine(Utility.getTraceString());
	}
		override public void displayKill(Monster[] monsters, Room room, Staff staff, bool splinter)
	{
        fShotsFired++;
		bool magic = false;
		if (staff != null)
			magic = true;
		Player player = fEngine.getCurrentPlayer();
		String str = "";
        if (player.fRoom == room)
		{
			if (magic)
				str = "You fire a lightning bolt with your " + Staff.TYPE_STRING[staff.getType()] + " staff!\n";
		}
		else
		{
			if (magic)
				str = "You fire a lightning bolt to cavern " + room.getRoomNumber() + ".\n";
			else
				str = "You fire an arrow to cavern " + room.getRoomNumber() + ".\n";
		}

		if (splinter)
		{
			str += "Your " + Staff.TYPE_STRING[staff.getType()] + " staff splintered!\n";
            if (fShooting)
                fShotsFired = player.getNumBolts();
            else
                fShotsFired = player.getNumBalls();
            int damage = GameEngine.rand.range(0, 2);
			player.applyDamage(damage);
			if (!player.isAlive())
				str += "\nYou're dead.\n";
			player.makeNoise(Player.kFightNoise);
		}

		if (monsters.Length == 0)
			str += "That wasn't very clever, was it?";

		for (int i = 0 ; i < monsters.Length ; i++)
		{
			Monster monster = monsters[i];
			if (monster.isAlive())
				str += "You wounded ";
			else
				str += "You killed ";
			str += monster.getMonsterDescription() + "\n";
		}
		System.Console.WriteLine(str);

        if (fShooting)
        {
            if (fEngine.hasAdjacentMonster(player.fRoom))
            {
                if (!magic && fShotsFired < player.getMaxNumShots() && player.fArrows > 0)
                {
                    fEngine.doCommandShootArrow();
                    return;
                }
                if (magic && fShotsFired < player.getNumBolts() && player.hasChargedStaff())
                {
                    fEngine.doCommandCastLightningBolt();
                    return;
                }
            }
        }
        else
        {
            if (fEngine.hasMonster(player.fRoom))
            {
                if (magic && fShotsFired < player.getNumBalls() && player.hasChargedStaff())
                {
                    fEngine.doCommandCastFireball();
                    return;
                }
            }
        }

		fEngine.processNextPlayer();
	}
		override public void displayWarning(String str)
	{
		System.Console.WriteLine(str);
		Player player = fEngine.getCurrentPlayer();
        this.displayRoom(player.fRoom);
	}
		private int readNumber()
	{
		int total = 0;
		int num = 0;
		try
		{
			// eat initial line feeds
			do
			{
				num = System.Console.Read();
			} while (num == 10 || num == 13);
			// eat numbers until linefeed
			do
			{
				total = 10 * total + (num - 48);
				num = System.Console.Read();
			} while (num != 10 && num != 13);
		}
		catch (Exception e)
		{
			System.Console.WriteLine("exception: " + e.ToString());
		}
		return total;
	}
		private int readFromConsole()
	{
		int num = 0;
		try
		{
			do
			{
				num = System.Console.Read();
			} while (num == 10 || num == 13);
		}
		catch (Exception e)
		{
			System.Console.WriteLine("exception: " + e.ToString());
		}
		return num - 48;
	}
	}
}
