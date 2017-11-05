using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Dungeons
{
	class Spider : Monster
	{
		public Spider()
		{
			fHitPoints = 1;
		}
		override public void setRoom(Room room)
		{
			base.setRoom(room);
		}
		override public int getKillScore()
		{
			return 3;
		}
		override public int getDamage()
		{
			return GameEngine.rand.range(0, 3);
		}
		override public bool isSurprised()
		{
			return false;
		}
		override public String getEncounterDescription(bool alive)
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
		override public String getAdjacentDescription()
		{
			return "You hear a scraping sound.\n";
		}
		override public String getMonsterDescription()
		{
			return "a giant spider";
		}
		override public String getLairDescription()
		{
			return "The Spider Hole";
		}
		override public void process()
		{
			base.process();

			if (GameEngine.instance.fMaze.fPlayerInWeb)
			{
				fStationary = false;
				Room aRoom = this.huntWebbedPlayer();
				if (aRoom != null)
				{
					// move three times in five
					if (GameEngine.rand.range(1, 5) <= 3)
					{
						this.move(aRoom);
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
						Room aRoom = this.fRoom.getRandomPassage();
						if (aRoom != null)
						{
							this.move(aRoom);
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
					if (path.Length > 0)
					{
						if (path.Length < length)
						{
							length = path.Length;
							room = path[0];
						}
					}
				}
			}
			return room;
		}
		private bool allPlayersAreParalyzed()
		{
			bool all_paralyzed = false;
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
}
