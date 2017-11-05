using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Dungeons
{
	class Sequence
	{
		protected int[] fNumbers;
		protected int fCount;
		protected int fIndex;
		protected static RandomNumber rand = new RandomNumber();

		public Sequence(int low, int high)
		{
			fIndex = 0;
			fCount = high - low + 1;
			fNumbers = new int[fCount];
			for (int i = 0; i < fCount; i++)
			{
				fNumbers[i] = low + i;
			}
			Utility.Assert(fNumbers[fCount - 1] == high, "fNumbers[fCount-1] == high");
		}
		static public void seed(int seed)
		{
			rand = null;
			rand = new RandomNumber(seed);
		}
		/*	public void shuffle()
			{
				int[] tmp = fNumbers;
				fNumbers = new int[fCount];

			}*/
		// Gets a random number from the sequence without removing it
		public int get()
		{
			Utility.Assert(fCount > 0, "fCount > 0");
			int index = rand.range(0, fCount - 1);
			return fNumbers[index];
		}
		// Removes a random number from the sequence
		public int remove()
		{
			Utility.Assert(fCount > 0, "fCount > 0");
			int index = rand.range(0, fCount - 1);
			int retval = fNumbers[index];
			for (int i = index + 1; i < fCount; i++)
			{
				fNumbers[i - 1] = fNumbers[i];
			}
			fCount--;
			return retval;
		}
		// Removes a certain number from the sequence
		public bool remove(int no)
		{
			Utility.Assert(fCount > 0, "fCount > 0");
			int index;
			for (index = 0; index < fCount; index++)
				if (fNumbers[index] == no)
					break;
			if (index == fCount)
				return false;
			Utility.Assert(fNumbers[index] == no, "fNumbers[index] == no");
			for (int i = index + 1; i < fCount; i++)
			{
				fNumbers[i - 1] = fNumbers[i];
			}
			fCount--;
			return true;
		}
		public int count()
		{
			return fCount;
		}
	}
}
