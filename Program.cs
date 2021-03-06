﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Dungeons
{
	class Program
	{
		static void Main(string[] args)
		{
		PlayerRecord[] records = new PlayerRecord[2];
//		records[0] = new PlayerRecord("Walle", Player.HOBBIT, 0);
		records[0] = new PlayerRecord("Gandalf", Player.MAGICIAN, 10000);
//		players[0] = Player.HUMAN;
// 		records[1] = new PlayerRecord("Legolas", Player.ELF, 0);
		records[1] = new PlayerRecord("Bilbo", Player.HOBBIT, 0);
// 		records[3] = new PlayerRecord("Aragorn", Player.HUMAN, 0);
// 		records[4] = new PlayerRecord("Gimli", Player.DWARF, 0);
// 		records[2] = new PlayerRecord("Walle", Player.ELF, 500000);
//		players[0] = Player.MAGICIAN;
		//		names[0] = "Bilbo";
		records[0].fCoins = 2;
		records[0].fArrows = 0;
//		records[4].fCoins = 0;
//		records[4].fArrows = 3;
//		records[0].fSandals = 1;
//		records[1].fSandals = 1;
		records[0].fStaffs[0] = Staff.NONE;
//        records[0].fStaffs[1] = Staff.GOLD;
//         records[0].fStaffs[2] = Staff.SILVER;
//         records[0].fStaffs[3] = Staff.PLATINUM;
//         records[0].fStaffs[4] = Staff.BRONZE;
//         records[1].fStaffs[0] = Staff.WOOD;
//         records[1].fStaffs[1] = Staff.BRONZE;
//         records[1].fStaffs[2] = Staff.SILVER;
//         records[1].fStaffs[3] = Staff.WOOD;
        records[0].fSwords = 3;
		records[1].fSwords = 3;
		records[0].fShields = 3;
		records[1].fShields = 3;
		records[0].fArmor = 5;
		records[1].fArmor = 5;

				//		DungeonRecord dungeon = new DungeonRecord(1);
        DungeonRecord dungeon = new DungeonRecord(3);
//         DungeonRecord dungeon = new DungeonRecord(30);

//		DungeonRecord dungeon = new DungeonRecord(5);
//		dungeon.setName("crash on map-misc");

// 		// wound testing 
// 		int hp = 200;
// 		PlayerRecord rec = records[0];
// 		RandomNumber rand = new RandomNumber();
// 		for (int i = 0; i < Player.kMaxWounds; i++)
// 		{
// 			rec.fWounds[i] = i + 1;
// 		}
// 		int heal = rand.range(1, 20);
// 		for (int w = 0; w < Player.kMaxWounds; w++)
// 		{
// 			int wound = w + 1;
// 			if (rec.fWounds[w] > 0 && heal >= wound)
// 			{
// 				int healtime = heal;
// 				while (healtime >= wound && wound > 0)
// 				{
// 					healtime -= wound;
// 					wound--;
// 				}
// 				if (wound > 0)
// 				{
// 					rec.fWounds[wound - 1] += rec.fWounds[w];
// 					hp -= wound * rec.fWounds[w];
// 				}
// 				rec.fWounds[w] = 0;
// 			}
// 		}


/*
		Timespan t = new Timespan(4000);
		Utility.Trace(t.toDisplayString());
		t = new Timespan(14000);
		Utility.Trace(t.toDisplayString());
		t = new Timespan(814000);
		Utility.Trace(t.toDisplayString());
		t = new Timespan(9814000);
		Utility.Trace(t.toDisplayString());
		t = new Timespan(49814000);
		Utility.Trace(t.toDisplayString());
*/
		Console console = new Console();
		GameEngine c = new GameEngine(console, dungeon, records/*, names, coins, scores*/);
		Utility.Trace(c.toString());
		console.setEngine(c);
		c.start();
		}
	}
}
