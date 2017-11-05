using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Dungeons
{
	class Chest
	{
		public bool fLocked;
		public bool fPoisoned;

		public int fCoins;
		public int fKeys;
		public int fDragonSwords;
		public int fSerpentShields;
		public int fArmor;
		public int fCarpets;
		public StaffContainer fStaffs = null;
		public int fArrows;
		public int fAxes;
		public int fSandals;
		public int fRing;


		public Chest()
		{
			fLocked = true;
			fPoisoned = false;

			fCoins = 0;
			fKeys = 0;
			fDragonSwords = 0;
			fSerpentShields = 0;
			fArmor = 0;
			fCarpets = 0;
			fArrows = 0;
			fAxes = 0;
			fSandals = 0;
			fRing = Player.RING_NONE;
		}
	}
}
