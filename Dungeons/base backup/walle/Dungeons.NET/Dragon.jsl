//package Catacombs;

/**
 * Summary description for Dragon.
 */
public class Dragon extends Monster
{
	public Dragon()
	{
		super();
		fHitPoints = GameEngine.rand.range(5, 6); // 5
	}
	public void setRoom(Room room)
	{
		super.setRoom(room);
	}
	public int getKillScore()
	{
		return 10;
	}
	public int getDamage()
	{
		return GameEngine.rand.range(1, fHitPoints);
	}
	public String getEncounterDescription(boolean alive)
	{
		String str = "Oh! Shit!  The Dragon caught ya!\n";
		if (alive)
			str += "But you survived his deadly fire.";
		else
			str += "He roasted your guts.";
		return str;
	}
	public String getAdjacentDescription()
	{
		return "It's getting hot!\n";
	}
	public String getMonsterDescription()
	{
		return "a dragon";
	}
	public String getLairDescription()
	{
		return "The Dragon Lair";
	}
	public void process()
	{
		super.process();
		Room room = this.huntPrey();
		if (room != null)
		{
			// move four times in five
			if (GameEngine.rand.range(1, 5) <= 4)
			{
				this.move(room);
			}
			else
			{
				this.rest();
			}
			return;
		}
		else
		{
			// move two times in five
			if (GameEngine.rand.range(1, 5) <= 2)
			{
				this.move();
			}
			else
				this.rest();
		}
	}
}
