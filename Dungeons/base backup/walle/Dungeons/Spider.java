//package Catacombs;

/**
 * Summary description for Spider.
 */
public class Spider extends Monster
{
	public Spider()
	{
		super();
		fHitPoints = 1;
	}
	public void setRoom(Room room)
	{
		super.setRoom(room);
	}
	public int getKillScore()
	{
		return 3;
	}
	public int getDamage()
	{
		return GameEngine.rand.range(0, 3);
	}	
	public boolean isSurprised()
	{
		return false;
	}
	public String getEncounterDescription(boolean alive)
	{
		String str = "A Spider jumps at you!\n";
		if (alive)
			str += "But you narrowly avoided its fangs.";
		else
		{
			str += "It ripped you to pieces!";
		}
		return str;
	}
	public String getAdjacentDescription()
	{
		return "You hear a scraping sound.\n";
	}
	public String getMonsterDescription()
	{
		return "a giant spider";
	}
	public String getLairDescription()
	{
		return "The Spider Hole";
	}
	public void process()
	{
		super.process();

        if (GameEngine.instance.fMaze.fPlayerInWeb)
		{
			fStationary = false;
			Room room = this.huntWebbedPlayer();
			if (room != null)
			{
				// move three times in five
				if (GameEngine.rand.range(1, 5) <= 3)
				{
					this.move(room);
				}
				else
					this.rest();
			}
			return;
		}
		if (this.allPlayersAreParalyzed())
		{
            if (!this.fRoom.fWeb)
			{
				if (GameEngine.rand.range(1, 4) <= 2)
					this.weave();
			}
			else
			{
				// move three times in five
				if (GameEngine.rand.range(1, 5) <= 3)
				{
                    Room room = this.fRoom.getRandomPassage();
					if (room != null)
					{
						this.move(room);
					}
				}
				else
					this.rest();
			}
			return;
		}

		// Spiders avoid players (unless trapped in web or in same room)
		if (GameEngine.instance.hasPlayer(fRoom))
			return;

		// If a spider hears a player it moves in a random direction
		Room room = fRoom.getHearCandidate(this.getHearing());
		if (room != null)
		{
			fStationary = false;
			fHunting = true;
			if (GameEngine.rand.range(1, 5) <= 3)
			{
				// If the room contains an unattacked player, run for it!
				if (GameEngine.instance.hasPlayer(room) && !GameEngine.instance.hasMonster(room))
                    room = this.fRoom.getRandomPassage();
				if (room != null)
				{
					this.move(room);
				}
			}
			return;
		}

		fHunting = false;

		// If a player scents a player, it weaves a web
        room = this.fRoom.getScentCandidate();
		if (room != null)
		{
			if (GameEngine.rand.range(1, 2) == 1)
				this.weave();
			return;
		}

		// else 
		// move two times in five
		if (GameEngine.rand.range(1, 5) <= 2)
		{
			room = fRoom.getRandomPassage();
			if (room == fLastRoom)
				room = fRoom.getRandomPassage();
			if (room != null)
				this.move(room);
		}
		else if (GameEngine.rand.range(1, 4) == 1)
		{
			this.weave();
		}
		else
			this.rest();
	}
	private void weave()
	{
		Utility.Trace("Spider weaves a web in room: " + fRoom.getRoomNumber());
        this.fRoom.fWeb = true;
	}
	private Room huntWebbedPlayer()
	{
		this.fStationary = false;
		this.fHunting = true;
		int length = 100;
		Room room = null;
        for (int iPlayer = 1; iPlayer <= GameEngine.instance.fNumPlayers; iPlayer++)
		{
			Player player = GameEngine.instance.getPlayer(iPlayer);
			// Hunt closest living (unparalized) players stuck in webs
            if (player.isAlive() && player.fRoom.fWeb && !player.isParalyzed())
			{
                Room[] path = GameEngine.instance.fMaze.findRoute(this.fRoom, player.fRoom, null, Maze.USE_OPEN_DOORS);
				if (path.length > 0)
				{
					if (path.length < length)
					{
						length = path.length;
						room = path[0];
					}
				}
			}
		}
		return room;
	}
	private boolean allPlayersAreParalyzed()
	{
		boolean all_paralyzed = false;
        for (int iPlayer = 1; iPlayer <= GameEngine.instance.fNumPlayers; iPlayer++)
		{
			Player player = GameEngine.instance.getPlayer(iPlayer);
			// Hunt closest living (unparalized) players stuck in webs
            if (player.fRoom == this.fRoom && player.isAlive())
			{
				if (!player.isParalyzed())
					return false;
				all_paralyzed = true;
			}
		}
		if (all_paralyzed)
			return true;

		return false;
	}
}
