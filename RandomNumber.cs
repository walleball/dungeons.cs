﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Dungeons
{
	class RandomNumber : Random
	{
		public RandomNumber() : base((int) DateTime.Now.Ticks)
		{
		}
		public RandomNumber(int seed) : base(seed)
		{
		}

		// returns a number in the range 0 to top-1
		public int rnd(int top)
		{
			int num = -1;
			while (num < 0)
			{
				num = this.Next();
			}
			num = num % (10 * top);	// was 2
			num = num / 10;
			Utility.Assert(num >= 0, "RandomNumber.rnd - num >= 0");
			Utility.Assert(num < top, "RandomNumber.rnd - num < top");
			return num;
		}
		// returns a number in the range low to high
		public int range(int low, int high)
		{
			Utility.Assert(high >= low, "Utility.range - high(" + high + ") >= low(" + low + ")");
			int range = high - low + 1;
			if (range <= 0)
				return low;
			int num = this.rnd(range);
			num = num + low;
			Utility.Assert(num >= low, "RandomNumber.range - num >= low");
			Utility.Assert(num <= high, "RandomNumber.range - num <= high");
			return num;
		}
	}
}
