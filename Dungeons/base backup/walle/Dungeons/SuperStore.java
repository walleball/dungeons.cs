//package Catacombs;

//import javax.microedition.lcdui.*;
//import javax.microedition.midlet.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import javax.microedition.rms.RecordComparator;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordFilter;
import javax.microedition.rms.RecordListener;
import javax.microedition.rms.RecordStore;

import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreNotOpenException;
import javax.microedition.rms.InvalidRecordIDException;

/**
 * Summary description for SuperStore.
 */
public class SuperStore
{
	// The name of the record store used to hold SuperStore
	private static final String STORE_NAME = "SuperStore";
	private static final String kMagicIdentifier = "Dungeons";
	private static final String kVersion = "10";

	// configuration
	private int			fLastDungeon;
	private int[]		fIncluded;
	private int[]		fExcluded;
	private int			fDungeonLimit;
	private String		fTrace = "";
	// players
	private PlayerRecord[] fPlayerRecords = null;
	private int fNumPlayers = 0;
	// dungeons
	private DungeonRecord[] fDungeonRecords = null;
	private int fNumDungeons = 0;

	// The record store itself
	private RecordStore fStore;
	private int			fId;

	// constants
	private static final int kMaxNumPlayers = 20;
	private static final int kMaxNumDungeons = 50;

	// Gain an hour heal time if not participating in a session
//	private static final long kRestBonus = 1 * 60 * 60 * 1000; 

	public SuperStore()
	{
		fLastDungeon = 0;
		fExcluded = new int[DungeonsMIDlet.kMaxNumPlayers+1];
		fIncluded = new int[DungeonsMIDlet.kMaxNumPlayers+1];
		this.resetParty();
		fId = -1;
		fDungeonLimit = 0;

		fPlayerRecords = new PlayerRecord[kMaxNumPlayers];
		fDungeonRecords = new DungeonRecord[kMaxNumDungeons];

		try 
		{
			fStore = RecordStore.openRecordStore(STORE_NAME, true);
			this.readSuperStore();
		} 
		catch (RecordStoreException ex) 
		{
			System.err.println(ex);
		}
		Utility.Trace("recordstore opened");
	}
	public int getLastDungeonNumber()
	{
		return fLastDungeon;
	}
	public void setLastDungeonNumber(int dungeon)
	{
		fLastDungeon = dungeon;
	}
	public int[] getIncludedPlayers()
	{
		return fIncluded;
	}
	public int[] getExcludedPlayers()
	{
		return fExcluded;
	}
	public int getDungeonLimit()
	{
		return fDungeonLimit;
	}
	public void setDungeonLimit(int limit)
	{
		fDungeonLimit = limit;
//		fDungeonLimit = 20;
	}
	public String getTrace()
	{
		return fTrace;
	}
	public void setTrace(String trace)
	{
		fTrace = trace;
	}
	public PlayerRecord[] getPlayerRecords()
	{
		Utility.Trace("getPlayerRecords()");
		Utility.Assert(fPlayerRecords != null, "fPlayerRecords != null");
		if (fPlayerRecords.length != fNumPlayers)
		{
			PlayerRecord[] records = new PlayerRecord[fNumPlayers];
			for (int i = 0 ; i < fNumPlayers ; i++)
			{
				records[i] = fPlayerRecords[i];
			}
			return records;
		}
		return fPlayerRecords;
	}
/*
	public DungeonRecord[] getDungeonRecords()
	{
		Utility.Trace("getDungeonRecords()");
		Utility.Assert(fDungeonRecords != null, "fDungeonRecords != null");
		if (fDungeonRecords.length != fNumDungeons)
		{
			DungeonRecord[] records = new DungeonRecord[fNumDungeons];
			for (int i = 0 ; i < fNumDungeons ; i++)
			{
				records[i] = fDungeonRecords[i];
			}
			return records;
		}
		return fDungeonRecords;
	}
*/	
	public DungeonRecord getDungeonRecord(int number)
	{
		Utility.Trace("getDungeonRecord(" + number + ") entered. fNumDungeons: " + fNumDungeons);
		DungeonRecord rec = null;
		for (int i = 0 ; i < fNumDungeons ; i++)
		{
			if (fDungeonRecords[i].fNumber == number)
			{
				rec = fDungeonRecords[i];
				break;
			}
		}
		if (rec == null)
		{
			Utility.Trace("Dungeon not found. Creating new");
			rec = new DungeonRecord(number);
			Utility.Trace("Record not found. Creating new: " + rec.toString());
			Utility.Assert(fNumDungeons < kMaxNumDungeons, "fNumDungeons < kMaxNumDungeons");
			fDungeonRecords[fNumDungeons++] = rec;
		}
		Utility.Trace("getDungeonRecord(" + number + ") returned : " + rec.toString());
		return rec;
	}
	public void addPlayerRecord(PlayerRecord rec)
	{
		Utility.Assert(fPlayerRecords != null, "fPlayerRecords != null");
		Utility.Assert(fNumPlayers < kMaxNumPlayers, "fNumPlayers < kMaxNumPlayers");
		if (fNumPlayers >= kMaxNumPlayers)
			return;
		fPlayerRecords[fNumPlayers++] = rec;
	}
/*
	public void addDungeonRecord(DungeonRecord rec)
	{
		Utility.Assert(fDungeonRecords != null, "fDungeonRecords != null");
		Utility.Assert(fNumDungeons < kMaxNumDungeons, "fNumDungeons < kMaxNumDungeons");
		if (fNumDungeons >= kMaxNumDungeons)
			return;
		fDungeonRecords[fNumDungeons++] = rec;
	}
*/	
	public void resetParty()
	{
		Utility.Trace("resetParty - enter: " + this.toString());
		for (int i = 0 ; i <= DungeonsMIDlet.kMaxNumPlayers ; i++)
		{
			fExcluded[i] = i;
			fIncluded[i] = -1;
		}
		fExcluded[DungeonsMIDlet.kMaxNumPlayers] = -1;
		fIncluded[0] = -1;
		Utility.Trace("resetParty - exit: " + this.toString());
	}
	public String toString()
	{
		String str = "last: " + fLastDungeon + 
					" limit: " + fDungeonLimit + 
					" id: " + fId;
		str += " included:";
		for (int i = 0 ; i <= DungeonsMIDlet.kMaxNumPlayers ; i++)
			str += " " + fIncluded[i];
		str += " excluded:";
		for (int i = 0 ; i <= DungeonsMIDlet.kMaxNumPlayers ; i++)
			str += " " + fExcluded[i];

		str += "#players: " + fNumPlayers + " #dungeons: " + fNumDungeons;
		return str;
	}
	public void close() 
	{
		if (fStore == null) 
			return;

		try
		{
			this.writeSuperStore();
			fStore.closeRecordStore();
		}
		catch (RecordStoreException ex)
		{
		}
		fStore = null;
		fIncluded = null;
		fExcluded = null;
		Utility.Trace("recordstore closed");
	}
	public void readSuperStore()
	{
		Utility.Trace("readSuperStore()");
		Utility.Assert(fStore != null, "SuperStore.readSuperStore - fStore != null");
		if (fStore == null) 
			return;

		try
		{
			int count = fStore.getNumRecords();
			RecordEnumeration enum = fStore.enumerateRecords(null, null, false);
			Utility.Trace("fStore.enumerateRecords() returned " + enum.numRecords());
			Utility.Assert(enum.numRecords() == count, "SuperStore.readSuperStore - enum.numRecords() == count");
			for (int i = 0 ; i < count ; i++)
			{
				int id = -1;
				try
				{
					id = enum.nextRecordId();
					this.readRecord(id);
					Utility.Trace("Found id: " + id);
					fId = id;
					for (int j = i + 1 ; j < count ; j++)
					{
						id = enum.nextRecordId();
						Utility.Trace("Deleting id: " + id);
						fStore.deleteRecord(id);
					}
					break;
				}
				catch (InvalidRecordIDException ex)
				{
					this.resetParty();
				}
				catch(IOException ex)
				{
					if (id != -1)
						fStore.deleteRecord(id);
					this.resetParty();
				}
			}
			Utility.Trace("readSuperStore() used record " + fId);
		}
		catch (RecordStoreNotOpenException ex)
		{
		}
		catch (RecordStoreException ex)
		{
		}

		// Remove this CHEAT code
//  		if (fNumPlayers == 0)
//  		{
//  			Utility.Trace("No player records. Adding default players");
//  			int cheat_multiplier = 0;
//  			PlayerRecord legolas = new PlayerRecord("Legolas", Player.ELF, cheat_multiplier * 90, 100);
//  			PlayerRecord gandalf = new PlayerRecord("Gandalf", Player.MAGICIAN, cheat_multiplier * 100, 100);
//  			PlayerRecord aragorn = new PlayerRecord("Aragorn", Player.HUMAN, cheat_multiplier * 80, 100);
//  			PlayerRecord gimli = new PlayerRecord("Gimli", Player.DWARF, cheat_multiplier * 50, 100);
//  			PlayerRecord bilbo = new PlayerRecord("Bilbo", Player.HOBBIT, cheat_multiplier * 40, 100);
//  
//  			legolas.fCoins = cheat_multiplier * 100;
//  			gandalf.fCoins = cheat_multiplier * 100;
//  			aragorn.fCoins = cheat_multiplier * 75;
//  			gimli.fCoins = cheat_multiplier * 50;
//  			bilbo.fCoins = cheat_multiplier * 50;
//  
//  			legolas.fStaffs = cheat_multiplier;
//  			gandalf.fStaffs = cheat_multiplier;
//  			aragorn.fStaffs = cheat_multiplier;
//  			gimli.fStaffs = cheat_multiplier;
//  			bilbo.fStaffs = cheat_multiplier;
//  
//  			legolas.fSwords = cheat_multiplier;
//  			gandalf.fSwords = cheat_multiplier;
//  			aragorn.fSwords = cheat_multiplier;
//  			gimli.fSwords = cheat_multiplier;
//  			bilbo.fSwords = cheat_multiplier;
//  
//  			legolas.fShields = cheat_multiplier;
//  			gandalf.fShields = cheat_multiplier;
//  			aragorn.fShields = cheat_multiplier;
//  			gimli.fShields = cheat_multiplier;
//  			bilbo.fShields = cheat_multiplier;
//  
//  			addPlayerRecord(bilbo);
//  			addPlayerRecord(gimli);
//  			addPlayerRecord(aragorn);
//  			addPlayerRecord(gandalf);
//  			addPlayerRecord(legolas); 
//  		}

		Utility.Trace("readSuperStore done: " + this.toString());
	}
	protected void readRecord(int id) throws RecordStoreException, RecordStoreNotOpenException, InvalidRecordIDException, IOException 
	{
		Utility.Trace("readRecord(" + id +")");

		byte[] bytes = fStore.getRecord(id);
		DataInputStream is = new DataInputStream(new ByteArrayInputStream(bytes));

		// identifier
		String tmp = is.readUTF();
		Utility.Trace("tmp: " + tmp);
		Utility.Trace("kMagicIdentifier: " + kMagicIdentifier);
		if (!tmp.equals(kMagicIdentifier))
		{
			Utility.Trace("Wrong identifier: " + tmp);
			throw new IOException();
		}
		// version
		tmp = is.readUTF();
		Utility.Trace("tmp: " + tmp);
		Utility.Trace("kVersion: " + kVersion);
		if (!tmp.equals(kVersion))
		{
			Utility.Trace("Wrong version: " + tmp);
			throw new IOException();
		}
		// configuration
		int last = is.readInt();
		Utility.Trace("readRecord - last = " + last);

		for (int i = 0 ; i < DungeonsMIDlet.kMaxNumPlayers + 1 ; i++)
		{
			fIncluded[i] = is.readInt();
			Utility.Trace("readRecord - fIncluded[" + i + "] = " + fIncluded[i]);
			fExcluded[i] = is.readInt();
			Utility.Trace("readRecord - fExcluded[" + i + "] = " + fExcluded[i]);
		}

		int limit = is.readInt();
		Utility.Trace("readRecord - limit = " + limit);
		if (last > limit)
			last = limit;
		if (fDungeonLimit < 0 || fDungeonLimit > 1000 /*|| last > limit*/)
			throw new IOException();
		for (int i = 0 ; i < DungeonsMIDlet.kMaxNumPlayers ; ++i)
		{
			if (fIncluded[i] < -1 || fIncluded[i] > DungeonsMIDlet.kMaxNumPlayers || 
				fExcluded[i] < -1 || fExcluded[i] > DungeonsMIDlet.kMaxNumPlayers)
				throw new IOException();
		}
		fLastDungeon = last;
		fDungeonLimit = limit;
		fTrace = is.readUTF();

		Utility.Trace("readRecord - fTrace = " + fTrace);

		// players
		fNumPlayers = is.readInt();
		Utility.Trace("readRecord - fNumPlayers = " + fNumPlayers);
		for (int i = 0 ; i < fNumPlayers ; ++i)
		{
			String name = is.readUTF();
			int race = is.readInt();
			int xp = is.readInt();
// 			int hp = is.readInt();
			if (name == "" || race <= 0 || race > 5 || /*hp < 0 || */xp < 0)
			{
				Utility.Trace("readPlayerRecord - read illegal record. name: " + name + " race: " + race + " xp: " + xp);
				throw new IOException();
			}

			PlayerRecord rec = new PlayerRecord(name, race, xp);
			for (int w = 0; w < Player.kMaxWounds; w++)
			{
				rec.fWounds[w] = is.readInt();
				Utility.Trace("rec.fWounds[" + w + "] = " + rec.fWounds[w]);
			}
			rec.fCoins = is.readInt();
			rec.fSwords = is.readInt();
			rec.fShields = is.readInt();
			rec.fArmor = is.readInt();
	            int numStaffs = is.readInt();
			Utility.Trace("readRecord - num staffs: " + numStaffs);
      	      rec.fStaffs = new int[numStaffs];
            	for (int s = 0; s < numStaffs; s++ )
			{
	                	rec.fStaffs[s] = is.readInt();
				Utility.Trace("readRecord - staff: " + rec.fStaffs[s]);
			}

			rec.fArrows = is.readInt();
			rec.fCarpets = is.readInt();
			rec.fSandals = is.readInt();
			rec.fKeys = is.readInt();
			rec.fAxes = is.readInt();
			rec.fRing = is.readInt();

			rec.fTimestamp = is.readLong();

			rec.initialize();

/*
			// heal one point per hour
			rec.fHP = rec.fMaxHP;
			long span = System.currentTimeMillis() - millis;
			int heal = (int)(span / (1 * 60 * 60 * 1000));
			Utility.Trace("heal: " + heal);
			for (int w = 0; w < Player.kMaxWounds; w++)
			{
				int wound = w + 1;
				if (rec.fWounds[w] > 0 && heal >= wound)
				{
					int healtime = heal;
					while (healtime >= wound && wound > 0)
					{
						healtime -= wound;
						wound--;
					}
					if (wound > 0)
					{
						rec.fWounds[wound - 1] += rec.fWounds[w];
					}
					rec.fWounds[w] = 0;
				}
			}
			for (int w = 0; w < Player.kMaxWounds; w++)
				rec.fHP -= (w+1) * rec.fWounds[w];
			Utility.Trace("rec.fHP: " + rec.fHP);

			rec.fTimestamp = millis + heal * (1 * 60 * 60 * 1000);
*/
			Utility.Trace("readRecord() read player " + rec.toString());
			fPlayerRecords[i] = rec;
		}

		// dungeons
		fNumDungeons = is.readInt();
		Utility.Trace("readRecord - fNumDungeons = " + fNumDungeons);
		for (int i = 0 ; i < fNumDungeons ; ++i)
		{
			int number = is.readInt();
			boolean completed = is.readBoolean();
			long opens = is.readLong();
			long treasure = is.readLong();
			String name = is.readUTF();
            String content = is.readUTF();
			String desc = is.readUTF();

			if (number < 0 || number > 100)
			{
				Utility.Trace("readDungeonRecord - read illegal record. number: " + number);
				throw new IOException();
			}

			DungeonRecord rec = new DungeonRecord(number);
			rec.fOpenTime = opens;
			rec.fTreasureTime = treasure;
			rec.fCompleted = completed;
            rec.fName = name;
            rec.fContent = content;
			rec.fDescription = desc;

// 			if (!completed)
// 			{
// 				// Read explored rooms
// 				rec.explored_rooms = new boolean[GameEngine.kMaxNumRooms];
// 				for (int j = 0 ; j < GameEngine.kMaxNumRooms ; j++)
// 				{
// 					rec.explored_rooms[j] = is.readBoolean();
// 				}
// 			}

			Utility.Trace("readRecord() read dungeon " + rec.toString());
			fDungeonRecords[i] = rec;
		}
	}
	public void writeSuperStore() 
	{
		Utility.Assert(fStore != null, "SuperStore.writeSuperStore - fStore != null");
		if (fStore == null)
			return;

		try
		{
			byte[] bytes = toByteArray();
			if (fId != -1)
				fStore.setRecord(fId, bytes, 0, bytes.length);
			else
				fId = fStore.addRecord(bytes, 0, bytes.length);
		}
		catch (IOException ex)
		{
		}
		catch (RecordStoreException ex)
		{
		}
	}
	private byte[] toByteArray() throws IOException 
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream os = new DataOutputStream(baos);

		// identifier
		os.writeUTF(kMagicIdentifier);
		// version
		os.writeUTF(kVersion);
		// configuration
		os.writeInt(fLastDungeon);
		for (int i = 0 ; i < DungeonsMIDlet.kMaxNumPlayers + 1 ; i++)
		{
			os.writeInt(fIncluded[i]);
			os.writeInt(fExcluded[i]);
		}
		os.writeInt(fDungeonLimit);
		os.writeUTF(fTrace);

		// players
		// count number players alive
		int alive = fNumPlayers;
// 		int alive = 0;
// 		for (int i = 0; i < fNumPlayers; i++)
// 		{
// 			PlayerRecord rec = fPlayerRecords[i];
// 			if (rec.fHP > 0)
// 				alive++;
// 		}
// 		Utility.Trace("toByteArray - alive: " + alive);
		Utility.Trace("toByteArray - fNumPlayers: " + fNumPlayers);
		os.writeInt(alive);
		for (int i = 0 ; i < fNumPlayers ; i++)
		{
			PlayerRecord rec = fPlayerRecords[i];
			// skip dead players
// 			if (rec.fHP <= 0)
// 				continue;
			os.writeUTF(rec.fName);
			os.writeInt(rec.fRace);
			os.writeInt(rec.fXP);
			for (int w = 0; w < Player.kMaxWounds; w++)
			{
				os.writeInt(rec.fWounds[w]);
				Utility.Trace("rec.fWounds[" + w + "] = " + rec.fWounds[w]);
			}
// 			os.writeInt(rec.fHP);

			os.writeInt(rec.fCoins);
			os.writeInt(rec.fSwords);
			os.writeInt(rec.fShields);
			os.writeInt(rec.fArmor);
            	os.writeInt(rec.fStaffs.length);
			Utility.Trace("toByteArray - num staffs: " + rec.fStaffs.length);
            	for (int s = 0; s < rec.fStaffs.length; s++ )
			{
                		os.writeInt(rec.fStaffs[s]);
				Utility.Trace("toByteArray - staff: " + rec.fStaffs[s]);
			}

            	os.writeInt(rec.fArrows);
			os.writeInt(rec.fCarpets);
			os.writeInt(rec.fSandals);
			os.writeInt(rec.fKeys);
			os.writeInt(rec.fAxes);
			os.writeInt(rec.fRing);
			long millis = System.currentTimeMillis();
			if (rec.fTimestamp != 0)
			{
				int restBonus = 5 * fDungeonLimit;
				if (restBonus == 0)
					restBonus = 1;
				if (restBonus > 60)
					restBonus = 60;
				millis = rec.fTimestamp - restBonus * 60 * 1000; // kRestBonus;
			}
			os.writeLong(millis);

			Utility.Trace("toByteArray() wrote player " + rec.toString());
		}

		// dungeons
		Utility.Trace("toByteArray - fNumDungeons: " + fNumDungeons);

		int low = fDungeonLimit - DungeonsMIDlet.kMaxNumAvailableDungeons;
		if (low < 0)
			low = 0;
		int numDungeons = fDungeonLimit - low;
		Utility.Trace("toByteArray - lopw: " + low);
		Utility.Trace("toByteArray - fNumDungeons: " + fNumDungeons);

		os.writeInt(numDungeons);
// 		for (int i = 0 ; i < fNumDungeons ; i++)
		for (int i = low; i < fDungeonLimit; ++i)
		{
// 			DungeonRecord rec = fDungeonRecords[i];
			DungeonRecord rec = this.getDungeonRecord(i);
			os.writeInt(rec.fNumber);
			os.writeBoolean(rec.fCompleted);
			os.writeLong(rec.fOpenTime);
			os.writeLong(rec.fTreasureTime);
            os.writeUTF(rec.fName);
            os.writeUTF(rec.fContent);
            os.writeUTF(rec.fDescription);

// 			if (!rec.fCompleted)
// 			{
// 				// Write explored rooms
// 				for (int j = 0; j < GameEngine.kMaxNumRooms; j++)
// 				{
// 					if (rec.explored_rooms == null)
// 						os.writeBoolean(false);
// 					else
// 						os.writeBoolean(rec.explored_rooms[j]);
// 				}
// 				rec.explored_rooms = null;
// 			}
			Utility.Trace("toByteArray() wrote dungeon " + rec.toString());
		}

		return baos.toByteArray();
	}
}