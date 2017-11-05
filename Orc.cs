using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Dungeons
{
	class Orc : Monster
	{
		public Orc() : base()
		{
			fHitPoints = 1;
		}
		override public void setRoom(Room room)
		{
			base.setRoom(room);
		}
		override public int getKillScore()
		{
			return 1;
		}
		override public int getDamage()
		{
			return GameEngine.rand.range(0, 1);
		}
		override public int applyDamage(int damage, int count)
		{
			return base.applyDamage(damage + GameEngine.rand.range(0, 1), count);
		}
		override public String getEncounterDescription(bool alive)
		{
			String str = "Ooouuch!  An Orc is running at you with his axe above his head.\n";
			if (alive)
				str += "But you avoided his blow.";
			else
				str += "Bad luck! It cleaved your skull.";
			return str;
		}
		override public String getAdjacentDescription()
		{
			//		return "You can hear an Orc cursing.\n";
			return "An orc is cursing.\n";
		}
		override public String getMonsterDescription()
		{
			return "an orc";
		}
		override public String getLairDescription()
		{
			return "The Orc Stronghold";
		}
		override public void process()
	{
		base.process();

		Room room = this.huntPrey();
		if (room != null)
		{
			// hunt three times in five
            if (GameEngine.rand.range(1, 5) <= 3)
            {
                this.move(room);
            }
            // Shoot arrow three in four
            else if (GameEngine.rand.range(1, 4) <= 3)
            {
                this.shootArrow(room);
            }
		}
		else
		{
			// move once in three
			if (GameEngine.rand.range(1, 3) <= 1)
			{
				// search for other orcs
                for (int i = 0; i < this.fRoom.getNumPassages(); i++)
				{
                    room = this.fRoom.getRandomPassage();
					if (room != null)
					{
						for (int iMonster = 1 ; iMonster <= GameEngine.instance.fNumMonsters ; iMonster++)
						{
							Monster monster = GameEngine.instance.getMonster(iMonster);
                            if (room == monster.fRoom)
							{
								if (monster is Orc)
									break;
							}
						}
					}
				}
				if (room != null)
					this.move(room);
			}
            else
				this.rest();
		}

	}
	}
}
