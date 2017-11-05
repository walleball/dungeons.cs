//package Catacombs;

/**
 * Summary description for Serpent.
 */
public class Serpent extends Monster
{
	public Serpent()
	{
		super();
		fHitPoints = 2;
	}
	public void setRoom(Room room)
	{
		super.setRoom(room);
	}
	public int getKillScore()
	{
		return 4;
	}
    public int getHearing()
    {
        return Monster.NO_HEARING;
    } 
    public int getDamage()
	{
//		return GameEngine.rand.range(1, 3);
		return GameEngine.rand.range(1, fHitPoints + 1);
	}
	public String getEncounterDescription(boolean alive)
	{
		String str = "Cripes!  A Giant Serpent just entered your cavern.\n";
		if (alive)
			str += "But you managed to avoid its violent attack.";
		else
			str += "And it swallowed you easily.";
		return str;
	}
	public String getAdjacentDescription()
	{
//		return "You can hear something hissing.\n";
		return "Something is hissing.\n";
	}
	public String getMonsterDescription()
	{
		return "a giant serpent";
	}
	public String getLairDescription()
	{
		return "The Serpent Pit";
	}
	public void process()
	{
		super.process();

		Room room = this.huntPrey();
		if (room != null)
		{
			// hunt once in two
			if (GameEngine.rand.range(1, 2) <= 1)
			{
				this.move(room);
			}
		}
		else
		{
			// move once in three
			if (GameEngine.rand.range(1, 3) <= 1)
			{
				this.move();
			}
			else if (GameEngine.rand.range(1, 5) == 1)
			{
				this.crap();
			}
			else
				this.rest();
		}
	}
	private void crap()
	{
		Utility.Trace("Serpent craps in room: " + fRoom.getRoomNumber());
        this.fRoom.setMuck();
	}
}
