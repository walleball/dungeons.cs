using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Dungeons
{
	class Serpent : Monster
	{
		public Serpent()
		{
			fHitPoints = 2;
		}
		override public void setRoom(Room room)
		{
			base.setRoom(room);
		}
		override public int getKillScore()
		{
			return 4;
		}
		override public int getHearing()
		{
			return Monster.NO_HEARING;
		}
		override public int getDamage()
		{
			//		return GameEngine.rand.range(1, 3);
			return GameEngine.rand.range(1, fHitPoints + 1);
		}
		override public String getEncounterDescription(bool alive)
		{
			String str = "Cripes!  A Giant Serpent just entered your cavern.\n";
			if (alive)
				str += "But you managed to avoid its violent attack.";
			else
				str += "And it swallowed you easily.";
			return str;
		}
		override public String getAdjacentDescription()
		{
			//		return "You can hear something hissing.\n";
			return "Something is hissing.\n";
		}
		override public String getMonsterDescription()
		{
			return "a giant serpent";
		}
		override public String getLairDescription()
		{
			return "The Serpent Pit";
		}
		override public void process()
		{
			base.process();

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
}
