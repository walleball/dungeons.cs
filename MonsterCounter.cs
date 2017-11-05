using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Dungeons
{
	class MonsterCounter
	{
		int orcs = 0;
		int goblins = 0;
		int trolls = 0;
		int serpents = 0;
		int spiders = 0;
		int dragons = 0;
		int balrogs = 0;
		int count = 0;

		public MonsterCounter()
		{
		}

		public void countMonster(Monster monster)
	{
		count++;
		if (monster is Orc)
			orcs++;
		else if (monster is Goblin)
			goblins++;
		else if (monster is Troll)
			trolls++;
		else if (monster is Serpent)
			serpents++;
		else if (monster is Spider)
			spiders++;
		else if (monster is Dragon)
			dragons++;
		else if (monster is Balrog)
			balrogs++;
		else
		{
			Utility.Assert(false, "MonsterCounter.countMonster - Unknown monster");
		}
	}
		public int getCount()
		{
			return count;
		}
		public String getContentString()
		{
			String str = "";
			if (balrogs > 0)
				str += Utility.numToStr(balrogs, "A Balrog", "Balrogs") + "\n";
			if (dragons > 0)
				str += Utility.numToStr(dragons, "A Dragon", "Dragons") + "\n";
			if (trolls > 0)
				str += Utility.numToStr(trolls, "A Troll", "Trolls") + "\n";
			if (spiders > 0)
				str += Utility.numToStr(spiders, "A Giant Spider", "Giant Spiders") + "\n";
			if (serpents > 0)
				str += Utility.numToStr(serpents, "A Giant Serpent", "Giant Serpents") + "\n";
			if (goblins > 0)
				str += Utility.numToStr(goblins, "A Goblin", "Goblins") + "\n";
			if (orcs > 0)
				str += Utility.numToStr(orcs, "An Orc", "Orcs") + "\n";
			return str;
		}
		public String toString()
		{
			String str = "";
			if (balrogs > 0)
				str += Convert.ToString(balrogs) + " Balrogs" + " ";
			if (dragons > 0)
				str += Convert.ToString(dragons) + " Dragons" + " ";
			if (spiders > 0)
				str += Convert.ToString(spiders) + " Spiders" + " ";
			if (trolls > 0)
				str += Convert.ToString(trolls) + " Trolls" + " ";
			if (serpents > 0)
				str += Convert.ToString(serpents) + " Serpents" + " ";
			if (goblins > 0)
				str += Convert.ToString(goblins) + " Goblins" + " ";
			if (orcs > 0)
				str += Convert.ToString(orcs) + " Orcs" + " ";
			return str;
		}
	}
}
