using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Dungeons
{
	class Dragon : Monster
	{
		public Dragon()
		{
			fHitPoints = GameEngine.rand.range(5, 6); // 5
		}
		override public void setRoom(Room room)
		{
			base.setRoom(room);
		}
		override public int getKillScore()
		{
			return 10;
		}
		override public int getDamage()
		{
			return GameEngine.rand.range(1, fHitPoints);
		}
		override public String getEncounterDescription(bool alive)
		{
			String str = "Oh! Shit!  The Dragon caught ya!\n";
			if (alive)
				str += "But you survived his deadly fire.";
			else
				str += "He roasted your guts.";
			return str;
		}
		override public String getAdjacentDescription()
		{
			return "It's getting hot!\n";
		}
		override public String getMonsterDescription()
		{
			return "a dragon";
		}
		override public String getLairDescription()
		{
			return "The Dragon Lair";
		}
		override public void process()
		{
			base.process();
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
}
