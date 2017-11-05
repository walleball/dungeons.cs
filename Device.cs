using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Dungeons
{
	abstract class Device
	{
		public abstract void displayRoom(Room room);
		public abstract void displayMove(Room room);
		public abstract void displayShoot(Room room, Staff staff);
		public abstract void displayEncounter(Monster monster, String str);
		public abstract void displayEncounter(String title, String str);
		public abstract void displayEncounter(String str);
		//	public abstract void displayInventory(Player player);
		public abstract void displayKill(Monster[] monsters, Room room, Staff staff, bool splinter);
		public abstract void displayWarning(String str);
		public abstract void displayExit();
		public abstract void displayDead(String str);
		public abstract void displayGameOver();
		public abstract void displayProgramError(String msg);
		public abstract void flushTrace();
		//	public abstract void output(String str);
	}
}
