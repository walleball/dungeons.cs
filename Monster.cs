using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Dungeons
{
	class Monster : ICloneable
	{
		protected int fHitPoints;
		public Room fRoom = null;
		protected Room fLastRoom = null;
		//	protected Room  fLastHunt = null;
		protected Room fTargetRoom = null;
		protected int fEndurance;
		protected bool fStationary = false;

		protected bool fHunting = false;
		protected bool fMoving = true;

		static protected int NO_HEARING = Player.kFightNoise;

		public Monster(/*Room room*/)
		{
			//		fRoom = room;
			//		fLastRoom = room;
			//		fLastHunt = null;
			fHitPoints = 1;
			fEndurance = 3;
			fMoving = true;
		}
		public object Clone()
		{
 			return this.MemberwiseClone();
//			return this;
		}
		public virtual void setRoom(Room room)
		{
			Utility.Assert(room != null, "Monster.setRoom - room != null");
			fRoom = room;
			fLastRoom = room;
		}
		public void setStationary(bool stay)
		{
			fStationary = stay;
		}
		public virtual int getKillScore()
		{
			return 0;
		}
		public virtual int getDamage()
		{
			return 0;
		}
		public virtual String getEncounterDescription(bool alive)
		{
			return "";
		}
		public virtual String getAdjacentDescription()
		{
			return "";
		}
		public virtual String getMonsterDescription()
		{
			return "a monster";
		}
		public virtual String getLairDescription()
		{
			return "the monster lair";
		}
		virtual public int getHearing()
		{
			return 2;
		}
		virtual public int applyDamage(int damage, int count)
		{
			Utility.Assert(count > 0, "Monster.applyDamage count > 0");
			fStationary = false;
			int remaining = 0;
			if (damage > 0)
			{
				remaining = damage - fHitPoints;
				fHitPoints -= damage;
			}
			if (this.isAlive())
			{
				if (fTargetRoom == null)
				{
					bool wasHunting = fHunting;
					Utility.Trace(this.getMonsterDescription() + " is wounded and follows hearing");
					Room room = this.followHearing();

					if (fTargetRoom == null && room != null)
						fTargetRoom = room;

					// still act surprised
					fHunting = wasHunting;

					if (fTargetRoom == null)
					{
						bool open_door = false;
						if (fRoom.hasDoor() && fRoom.isDoorOpen() && !fRoom.isExitDoor())
							open_door = true;
						int num = fRoom.getNumPassages();
						if (open_door)
							num++;
						if (num > 0)
						{
							int passage = ((count - 1) % num) + 1;
							if (open_door && passage == num)
								fTargetRoom = fRoom.fDoor;
							else if (fRoom.isPassable(passage))
								fTargetRoom = fRoom.getPassage(passage);
							else
							{
								if (fTargetRoom == null)
									fTargetRoom = fRoom.getRandomPassage(fLastRoom);
								if (fTargetRoom != null && GameEngine.instance.hasMonster(fTargetRoom))
									fTargetRoom = fRoom.getRandomPassage();
							}

							if (fTargetRoom != null)
							{
								room = fTargetRoom.getRandomPassage(fRoom);
								if (room != fRoom)
									fTargetRoom = room;
							}
						}
					}
					// 				if (fTargetRoom == null)
					// 					fTargetRoom = fRoom.getRandomPassage(fLastRoom);
					// 				if (fTargetRoom != null && GameEngine.instance.hasMonster(fTargetRoom))
					// 					fTargetRoom = fRoom.getRandomPassage();
				}
				Utility.Trace(this.getMonsterDescription() + " is wounded and makes noise");
				this.makeNoise(Player.kFightNoise);
			}
			else
			{
				Utility.Trace(this.getMonsterDescription() + " is killed and makes noise");
				this.makeNoise(Player.kFightNoise);
			}

			return remaining;
		}
		public bool isAlive()
		{
			if (fHitPoints > 0)
				return true;
			return false;
		}
		virtual public bool isSurprised()
		{
			if (fHunting)
				return false;
			if (fMoving)
				return true;
			return false;
		}
		public void makeNoise(int noise, Room source)
		{
			fRoom.makeNoise(noise, source);
		}
		public void makeNoise(int noise)
		{
			fRoom.makeNoise(noise);
		}
		public virtual void process()
		{
			Utility.Trace("Process - " + this.getMonsterDescription());
			fHunting = false;
			fMoving = false;
			// implement in subclass
		}
		public void fight()
		{
			fEndurance = fEndurance - 1;
			this.makeNoise(Player.kFightNoise);
		}
		public void shootArrow(Room targetRoom)
		{
			targetRoom.receiveArrow();
		}
		protected Room followHearing()
		{
			// if alive player in room, stay and fight
			//		if (GameEngine.instance.hasPlayer(fRoom))
			//			return fRoom;

			Room room = fRoom.getHearCandidate(this.getHearing());
			if (room != null)
			{
				if (fTargetRoom == null)
				{
					fTargetRoom = room.getRandomPassage(fRoom);
					if (fTargetRoom == fRoom)
						fTargetRoom = room;
				}
				Utility.Trace(this.getMonsterDescription() + " is following hearing from room " + fRoom.getRoomNumber() + " to " + room.getRoomNumber());
				// if alive player in room, stay and fight, unless there is fighting to be had in another room, in which
				// case the monster has a hard time deciding...
				if (GameEngine.instance.hasPlayer(fRoom))
					if (GameEngine.rand.range(0, 1) == 0)
						return fRoom;
			}

			// unless there is another player in the next room!
			// 		if (GameEngine.instance.hasPlayer(fRoom))
			// 		{
			// 			if (room != null && !GameEngine.instance.hasPlayer(room))
			// 			{
			// 				return fRoom;
			// 			}
			// 		}

			fHunting = true;

			if (room == null && fTargetRoom != null)
			{
				Room[] path = GameEngine.instance.fMaze.findRoute(fRoom, fTargetRoom, null, Maze.USE_OPEN_DOORS);
				if (path.Length > 0)
					room = path[0];
			}
			else if (room != null)
			{
				if (!fRoom.isPassable(room))
					room = fRoom.getRandomPassage(fLastRoom);
			}

			if (room == null)
			{
				fHunting = false;
			}
			else
			{
				fStationary = false;
			}

			if (fHunting)
			{
				//			Utility.Trace(this.getMonsterDescription() + " follows hearing and makes noise");
				//			this.makeNoise(Player.kFightNoise);
			}

			return room;
		}
		protected Room huntPrey()
		{
			//		if (fLastHunt != null)
			//			fHunting = true;

			// if alive player in room, stay and fight
			//		if (GameEngine.instance.hasPlayer(fRoom))
			//			return fRoom;

			// else follow trail (if present)
			Room room = fRoom.getHearCandidate(this.getHearing());
			if (room != null)
			{
				Utility.Trace(this.getMonsterDescription() + " is following hearing from room " + fRoom.getRoomNumber() + " to " + room.getRoomNumber());
				//  				+ " and makes noise");
				//             this.makeNoise(Player.kFightNoise, fRoom);
			}
			else
			{
				room = fRoom.getScentCandidate();
				if (room != null)
					Utility.Trace(this.getMonsterDescription() + " is following scent from room " + fRoom.getRoomNumber() + " to " + room.getRoomNumber());
			}

			// if alive player in room, stay and fight, 
			// unless there is another player in the next room!
			// if alive player in room, stay and fight, unless there is fighting to be had in another room, in which
			// case the monster has a hard time deciding...
			if (room != null)
			{
				if (fTargetRoom == null)
				{
					fTargetRoom = room.getRandomPassage(fRoom);
					if (fTargetRoom == fRoom)
						fTargetRoom = room;
				}
				if (GameEngine.instance.hasPlayer(fRoom))
					if (GameEngine.rand.range(0, 1) == 0)
						return fRoom;
			}
			// 		if (GameEngine.instance.hasPlayer(fRoom))
			// 		{
			// 			if (room != null && !GameEngine.instance.hasPlayer(room))
			// 			{
			// 				return fRoom;
			// 			}
			// 		}

			fHunting = true;

			if (room == null && fTargetRoom != null)
			{
				Room[] path = GameEngine.instance.fMaze.findRoute(fRoom, fTargetRoom, null, Maze.USE_OPEN_DOORS);
				if (path.Length > 0)
					room = path[0];
			}
			else if (room != null)
				if (!fRoom.isPassable(room))
					room = fRoom.getRandomPassage(fLastRoom);

			if (room == null)
			{
				fHunting = false;
			}
			else
			{
				fStationary = false;
			}

			if (fHunting)
			{
				// 			Utility.Trace(this.getMonsterDescription() + " hunts prey and makes noise");
				// 			this.makeNoise(Player.kFightNoise);
			}
			return room;
		}
		protected bool move(Room room)
		{
			Utility.Assert(room != null, "Monster.move - room != null");
			fMoving = false;
			if (fStationary)
			{
				Utility.Assert(!fHunting, "Monster.move - !fHunting");
				//			if (GameEngine.rand.range(1, 25) == 1)
				int interval = Room.kLairProduction * this.getKillScore();
				if (GameEngine.rand.range(1, interval) == 1)
				{
					if (GameEngine.fDisplayDebugInfo)
						Utility.Assert(false, this.getMonsterDescription() + " is no longer stationary.");
					Utility.Trace(this.getMonsterDescription() + " is no longer stationary.");
					fStationary = false;
				}
				else
				{
					return false;
				}
			}

			if (fEndurance <= 0)
			{
				Utility.Trace(this.getMonsterDescription() + " is too tired to move.");
				fEndurance = 3;
				return false;
			}

			if (!fHunting && room == fRoom)
			{
				//			fEndurance = fEndurance - 1;
				return false;
			}
			if (fRoom.hasPassage(room))
			{
				if (!fRoom.isPassable(room))
					room = null;
			}
			else
			{
				if (!fRoom.hasDoor() || !fRoom.isDoorOpen() || fRoom.fDoor != room)
				{
					// Can happen if the player closes a door...
					//					Utility.Assert(false);
					room = null;
				}
			}

			if (room != null)
			{
				// get tired...
				fEndurance = fEndurance - 1;
				fLastRoom = fRoom;
				fRoom = room;
				if (fTargetRoom == room)
					fTargetRoom = null;
				fMoving = true;
				return true;
			}
			return false;
		}
		protected void move()
		{
			// follow trail (if present)
			Room room = this.huntPrey();

			if (room != null)
			{
				if (!fRoom.hasPassage(room))
				{
					if (!fRoom.hasDoor() || !fRoom.isDoorOpen() || fRoom.fDoor != room)
					{
						// Can happen if the player closes a door...
						//					Utility.Assert(false);
						room = null;
					}
				}
			}
			if (room == null)
			{
				if (GameEngine.instance.hasPlayer(fRoom))
					if (GameEngine.rand.range(0, 1) == 0)
						return;
				room = fRoom.getRandomPassage(fLastRoom);
			}

			// Move away from a fight only if there are other players to attack!
			if (room != null)
			{
				this.move(room);
			}
		}
		protected void rest()
		{
			fEndurance = 3;
		}
	}
}
