using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Dungeons
{
	class Goblin : Monster
	{
		public Goblin()
		{
			fHitPoints = 1;
		}
		override public void setRoom(Room room)
		{
			base.setRoom(room);
		}
		override public int getKillScore()
		{
			return 2;
		}
		override public int getDamage()
		{
			return GameEngine.rand.range(1, 2);
		}
		override public String getEncounterDescription(bool alive)
		{
			String str = "Help!  A Goblin attacks you with his javelin.\n";
			if (alive)
				str += "But his throw did only minor damages.";
			else
			{
				int alternative = GameEngine.rand.range(1, 10);
				if (alternative == 1)
					str += "He gave you a really cool chest-piercing!";
				else
					str += "What a pity! He pierced your chest.";
			}
			return str;
		}
		override public String getAdjacentDescription()
		{
			//		return "You can hear a Goblin grunting.\n";
			return "A goblin is grunting.\n";
		}
		override public String getMonsterDescription()
		{
			return "a goblin";
		}
		override public String getLairDescription()
		{
			return "The Goblin Den";
		}
		override public void process()
		{
			base.process();

			Room room = this.huntPrey();
			if (room != null)
			{
				// move two times in five
				if (GameEngine.rand.range(1, 5) <= 2)
				{
					this.move(room);
				}
				// Shoot arrow every second
				else if (GameEngine.rand.range(1, 2) <= 1)
				{
					this.shootArrow(room);
				}
			}
			else
			{
				// move two times in five
				if (GameEngine.rand.range(1, 5) <= 2)
				{
					this.move();
				}
				else if (GameEngine.rand.range(1, 4) == 1)
				{
					this.dig();
				}
				else
					this.rest();
			}
		}
		private void dig()
		{
			Utility.Trace("Goblin digs pit in room: " + fRoom.getRoomNumber());
			this.fRoom.fPit = true;
		}
	}
}
