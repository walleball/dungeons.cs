//package Dungeons;

/**
 * Summary description for MonsterCounter.
 */
public class MonsterCounter
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
		if (monster instanceof Orc)
			orcs++;
		else if (monster instanceof Goblin)
			goblins++;
		else if (monster instanceof Troll)
			trolls++;
		else if (monster instanceof Serpent)
			serpents++;
		else if (monster instanceof Spider)
			spiders++;
		else if (monster instanceof Dragon)
			dragons++;
		else if (monster instanceof Balrog)
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
			str += Integer.toString(balrogs) + " Balrogs" + " ";
		if (dragons > 0)
			str += Integer.toString(dragons) + " Dragons" + " ";
		if (spiders > 0)
			str += Integer.toString(spiders) + " Spiders" + " ";
		if (trolls > 0)
			str += Integer.toString(trolls) + " Trolls" + " ";
		if (serpents > 0)
			str += Integer.toString(serpents) + " Serpents" + " ";
		if (goblins > 0)
			str += Integer.toString(goblins) + " Goblins" + " ";
		if (orcs > 0)
			str += Integer.toString(orcs) + " Orcs" + " ";
		return str;
	}
}
