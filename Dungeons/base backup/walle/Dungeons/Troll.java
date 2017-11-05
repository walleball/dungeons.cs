//package Catacombs;

/**
 * Summary description for Troll.
 */
public class Troll extends Monster
{
	public Troll()
	{
		super();
		fHitPoints = 4;
	}
	public int getKillScore()
	{
		return 5;
	}
	public int getDamage()
	{
		return GameEngine.rand.range(0, fHitPoints);
	}
	public String getEncounterDescription(boolean alive)
	{
		String str = "A Troll swings at you with his club.\n";
		if (alive)
			str += "But his blow did only minor damage.";
		else
		{
			str += "He crushed your skull!";
		}
		return str;
	}
	public String getAdjacentDescription()
	{
		return "A Troll is stomping.\n";
	}
	public String getMonsterDescription()
	{
		return "a troll";
	}
	public String getLairDescription()
	{
		return "The Troll Cave";
	}
	public void process()
	{
		super.process();

		Room room = this.followHearing();
		if (room != null)
		{
			// move once in three
			if (GameEngine.rand.range(1, 3) <= 1)
			{
				this.move(room);
			}
		}
		else
		{
			// move once in five
			if (GameEngine.rand.range(1, 5) <= 1)
			{
				room = fRoom.getRandomPassage();
				if (room == fLastRoom)
					room = fRoom.getRandomPassage();
				if (room != null)
					/*fMoving = */ this.move(room);
			}
			else
				this.rest();
		}
	}
}
