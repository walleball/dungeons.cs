//package Catacombs;

/**
 * Summary description for Balrog.
 */
public class Balrog extends Monster
{
//	private int hunt_player = 1;
	private Room fTarget = null;

	public Balrog()
	{
		super();
		fHitPoints = 15;
	}
	public void setRoom(Room room)
	{
		super.setRoom(room);
		fTarget = room;
	}
	public int getKillScore()
	{
		return 100;
	}
	public int getDamage()
	{
//		return GameEngine.rand.range(1, 5);
		int points = fHitPoints;
		if (points > 10)
			points = 10;
		return GameEngine.rand.range(1, points);
	}
	public String getEncounterDescription(boolean alive)
	{
		String str = "Shit pommes frites!\nThe Balrog lashes its fiery whip at you!\n";
		if (alive)
			str += "But you miraculously survived.";
		else
			str += "You were reduced to smoking ash.";
		return str;
	}
	public String getAdjacentDescription()
	{
		return "It's getting really hot!\n";
	}
	public String getMonsterDescription()
	{
		return "a balrog";
	}
	public String getLairDescription()
	{
		return "The Lair of the Balrogs";
	}
//	public int getHearing()
//	{
//		return 1;
//	}
	public void process()
	{
		super.process();

		Room room = this.huntPrey();
		if (room != null)
		{
			// move three times in five
			if (GameEngine.rand.range(1, 5) <= 3)
			{
				this.move(room);
			}
			else
			{
				this.rest();
			}
			return;
		}

		// No one to hunt, rest three times in five
        if (GameEngine.rand.range(1, 5) <= 3)
        {
            this.rest();
            return;
        }

		if (this.fRoom == fTarget)
		{
			// find another target
			do 
            {
                room = GameEngine.instance.fMaze.getRoom(GameEngine.rand.range(1, GameEngine.instance.fMaze.getNumRooms()));
			} while(room == fTarget);
            fTarget = room;
		}

        Room[] path = GameEngine.instance.fMaze.findRoute(fRoom, fTarget, null, Maze.USE_OPEN_DOORS);
//		Utility.Assert(path.length > 0, "Balrog.process - path.length > 0");
		if (path.length > 0)
		{
			Utility.Trace("The Balrog is going to room " + fTarget.getRoomNumber() + " via " + path[0].getRoomNumber());
			this.move(path[0]);
		}
		else
		{
            room = this.fRoom.getRandomPassage();
			if (room != null)
			{
				Utility.Trace("The Balrog is moving randomly to cavern " + room.getRoomNumber());
				this.move(room);
			}
		}
	}
}
