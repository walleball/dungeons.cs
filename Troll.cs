using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Dungeons
{
	class Troll : Monster
	{
		public Troll()
		{
			fHitPoints = 4;
		}
		override public int getKillScore()
		{
			return 5;
		}
		override public int getDamage()
		{
			return GameEngine.rand.range(0, fHitPoints);
		}
		override public String getEncounterDescription(bool alive)
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
		override public String getAdjacentDescription()
		{
			return "A Troll is stomping.\n";
		}
		override public String getMonsterDescription()
		{
			return "a troll";
		}
		override public String getLairDescription()
		{
			return "The Troll Cave";
		}
		override public void process()
		{
			base.process();

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
						/*fMoving = */
						this.move(room);
				}
				else
					this.rest();
			}
		}
	}
}
