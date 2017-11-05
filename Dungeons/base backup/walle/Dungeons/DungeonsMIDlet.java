import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;

import java.io.*;
import javax.microedition.io.*;

//package Dungeons;

public class DungeonsMIDlet
	extends MIDlet
    implements CommandListener 
{
  	private Form mMainForm;
	private Form mNewPlayerForm;
	private Screen mCurrentForm;
	private List mAddPlayerForm;
	private List mRemovePlayerForm;

	private GameEngine fEngine;
	private Command mBackCommand;
	private SuperStore fSuperStore;
	private PlayerRecord[] fPlayerRecords;
	private PlayerRecord[] fPlayers;
	private int[] fIncluded;
	private int[] fExcluded;

	private StringItem mParty;
	private ChoiceGroup	mDungeons;
	private Command mStartCommand;
	private Command mTraceCommand;
	private Command mNewPlayerCommand;
	private Command mAddPlayerCommand;
	private Command mRemovePlayerCommand;
	private Command mRemoveAllCommand;

	private TextField mNameField;
	private ChoiceGroup mRaces;
	private Command mCreatePlayerCommand;

	private boolean fStarted;
	private String TRACE_LOG = "";

	private static String kHighscoreServer = "217.215.101.161";
	public static int kMaxNumPlayers = 10;
	public static int kMaxNumDungeons = 40;
	public static int kMaxNumAvailableDungeons = 10; // kMaxNumDungeons; // 30; // 20; // 10;

	public DungeonsMIDlet() 
	{
		Utility.Trace("fSuperStore = new SuperStore();");
		fSuperStore = new SuperStore();

		mBackCommand = new Command("Back", Command.BACK, 0);

		fStarted = false;
		fIncluded = fSuperStore.getIncludedPlayers();
		fExcluded = fSuperStore.getExcludedPlayers();
		fPlayerRecords = fSuperStore.getPlayerRecords();
		Utility.Trace("fSuperStore.getPlayerRecords returned " + fPlayerRecords.length + " records.");
		fIncluded[fPlayerRecords.length] = -1;
		fExcluded[fPlayerRecords.length] = -1;
		for (int i = 0 ; i < fPlayerRecords.length ; i++)
		{
			Utility.Trace("fIncluded[" + i + "]: " + fIncluded[i]);
			Utility.Trace("fExcluded[" + i + "]: " + fExcluded[i]);
		}
		
		this.setupMainForm();

		mNewPlayerForm = new Form("New Player");
		mCreatePlayerCommand = new Command("Create", Command.OK, 0);
		mNameField = new TextField("Name:", "", 32, TextField.ANY);
		mNewPlayerForm.append(mNameField);

		String[] races = { "Hobbit", "Human", "Elf", "Dwarf", "Magician" };
		mRaces = new ChoiceGroup("Race:", Choice.EXCLUSIVE, races, null);
		mNewPlayerForm.append(mRaces);

		mNewPlayerForm.addCommand(mCreatePlayerCommand);
		mNewPlayerForm.addCommand(mBackCommand);
		mNewPlayerForm.setCommandListener(this);
	}
	private void setupMainForm()
	{
		mMainForm = new Form("Dungeons");
		mStartCommand = new Command("Enter", Command.OK, 0);
		mTraceCommand = new Command("Trace", Command.SCREEN, 100);

		mNewPlayerCommand = new Command("New Player", Command.SCREEN, 20);
		mAddPlayerCommand = new Command("Add Player", Command.SCREEN, 11);
		mRemovePlayerCommand = new Command("Remove Player", Command.SCREEN, 12);
		mRemoveAllCommand = new Command("Remove All", Command.SCREEN, 13);

		TRACE_LOG = fSuperStore.getTrace();
		fSuperStore.setTrace("");

		fPlayerRecords = fSuperStore.getPlayerRecords();
		Utility.Trace("There are " + fPlayerRecords.length + " player records");
		int included = 0;
		for (int i = 0 ; i < kMaxNumPlayers ; i++)
		{
			if (fIncluded[i] == -1)
				break;
			else
				included++;
		}
		Utility.Trace("included: " + included);
		int excluded = 0;
		for (int i = 0 ; i < kMaxNumPlayers ; i++)
		{
			if (fExcluded[i] == -1)
				break;
			else
				excluded++;
		}
		Utility.Trace("excluded: " + excluded);
		Utility.Assert(excluded == fPlayerRecords.length - included, "excluded == fPlayerRecords.length - included");
		if (excluded != fPlayerRecords.length - included)
		{
			fSuperStore.resetParty();
			included = 0;
			excluded = fPlayerRecords.length;
			fIncluded[0] = -1;
			fExcluded[fPlayerRecords.length] = -1;
		}

		String party = "";
		int included_count = 0;
		for (int i = 0 ; i < kMaxNumPlayers ; i++)
		{
			if (fIncluded[i] == -1)
				break;
			else
			{
				party += fPlayerRecords[fIncluded[i]].toDisplayString() + "\n";
				included_count++;
			}
		}
		int excluded_count = 0;
		for (int i = 0 ; i < kMaxNumPlayers ; i++)
		{
			if (fExcluded[i] == -1)
				break;
			else
			{
				excluded_count++;
			}
		}
		Utility.Assert(included == included_count, "included == included_count");
		Utility.Assert(excluded == excluded_count, "excluded == excluded_count");

		mParty = new StringItem("Party\n", party);
		mMainForm.append(mParty);

		if (fSuperStore.getDungeonLimit() > kMaxNumDungeons)
			fSuperStore.setDungeonLimit(kMaxNumDungeons);
		if (fSuperStore.getLastDungeonNumber() > kMaxNumDungeons)
			fSuperStore.setLastDungeonNumber(kMaxNumDungeons);

		int limit = fSuperStore.getDungeonLimit();
		Utility.Assert(limit <= kMaxNumDungeons, "DungeonsMIDlet.setupMainForm - limit <= kMaxNumDungeons");
		int low = limit - kMaxNumAvailableDungeons;
		if (low < 0)
			low = 0;
		String dungeons[] = new String[limit - low + 1];
		for (int i = 0 ; i <= limit - low ; ++i)
		{
			DungeonRecord rec = fSuperStore.getDungeonRecord(limit - i);
			dungeons[i] = rec.toDisplayString();
		}

		mDungeons = new ChoiceGroup("Dungeon", Choice.EXCLUSIVE, dungeons, null);
		Utility.Trace("mDungeons.setSelectedIndex(" + limit + " - " + fSuperStore.getLastDungeonNumber() + ")");
		mDungeons.setSelectedIndex(limit-fSuperStore.getLastDungeonNumber(), true);
		mMainForm.append(mDungeons);

		// 0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15
		// 1 2 2 2 2 2 3 3 3 3 3  4  4  4  4  4  5
		// 1 2 2 2 2 3 3 3 3 3  4  4  4  4  4  5
		int limitPlayers = (limit / 5) + 2;
		if (limit == 0)
			limitPlayers = 1;
		Utility.Trace("allowed: " + limitPlayers);
		if (included_count > 0)
			mMainForm.addCommand(mStartCommand);
		if (excluded_count > 0 && included_count < limitPlayers)
			mMainForm.addCommand(mAddPlayerCommand);
		if (included_count > 0)
			mMainForm.addCommand(mRemovePlayerCommand);
		if (included_count > 1)
			mMainForm.addCommand(mRemoveAllCommand);
		if (fPlayerRecords.length < kMaxNumPlayers)
			mMainForm.addCommand(mNewPlayerCommand);
		if (TRACE_LOG.length() > 0)
			mMainForm.addCommand(mTraceCommand);

		mMainForm.setCommandListener(this);
	}
	private void setupAddPlayerForm()
	{
		int excluded = 0;
		for (int i = 0 ; i < kMaxNumPlayers ; i++)
		{
			if (fExcluded[i] == -1)
				break;
			else
				excluded++;
		}
		Utility.Trace("excluded: " + excluded);

		String players[] = new String[excluded];
		int excluded_count = 0;
		for (int i = 0 ; i < kMaxNumPlayers ; i++)
		{
			if (fExcluded[i] == -1)
				break;
			else
			{
				players[excluded_count] = fPlayerRecords[fExcluded[i]].toHealTimeString();
				excluded_count++;
			}
		}
		Utility.Assert(excluded == excluded_count, "excluded == excluded_count");

		mAddPlayerForm = new List("Adventurers", List.IMPLICIT, players, null);

		mAddPlayerForm.setCommandListener(this);
	}
	private void setupRemovePlayerForm()
	{
		int included = 0;
		for (int i = 0 ; i < kMaxNumPlayers ; i++)
		{
			if (fIncluded[i] == -1)
				break;
			else
				included++;
		}
		Utility.Trace("included: " + included);

		String party[] = new String[included];
		int included_count = 0;
		for (int i = 0 ; i < kMaxNumPlayers ; i++)
		{
			if (fIncluded[i] == -1)
				break;
			else
			{
				party[included_count] = fPlayerRecords[fIncluded[i]].toHealTimeString();
				included_count++;
			}
		}
		Utility.Assert(included == included_count, "included == included_count");

		mRemovePlayerForm = new List("Adventurers", List.IMPLICIT, party, null);

		mRemovePlayerForm.setCommandListener(this);
	}
	private void setCurrentForm(Screen form)
	{
		mCurrentForm = form;
		Display.getDisplay(this).setCurrent(form);
	}
	public void startApp() 
	{
		if (!fStarted)
		{
			this.setCurrentForm(mMainForm);
			fStarted = true;
		}
	}

	public void pauseApp() 
	{
		Utility.Trace("pauseApp");
	}

	public void destroyApp(boolean unconditional) 
	{ 
		Utility.Trace("destroyApp " + unconditional);
	}

	public void commandAction(Command c, Displayable s)
	{
		if (mCurrentForm == mMainForm)
		{
			/*		if (c == mExitCommand)
			 {
			 notifyDestroyed();
			 }
			 else */
			if (c == mNewPlayerCommand)
			{
				fSuperStore.setLastDungeonNumber(fSuperStore.getDungeonLimit() - mDungeons.getSelectedIndex());

				this.setCurrentForm(mNewPlayerForm);
			}
			else if (c == mAddPlayerCommand)
			{
				fSuperStore.setLastDungeonNumber(fSuperStore.getDungeonLimit() - mDungeons.getSelectedIndex());
				this.setupAddPlayerForm();
				this.setCurrentForm(mAddPlayerForm);
			}
			else if (c == mRemovePlayerCommand)
			{
				fSuperStore.setLastDungeonNumber(fSuperStore.getDungeonLimit() - mDungeons.getSelectedIndex());
				this.setupRemovePlayerForm();
				this.setCurrentForm(mRemovePlayerForm);
			}
			else if (c == mRemoveAllCommand)
			{
				fSuperStore.resetParty();
				fIncluded[0] = -1;
				fExcluded[fPlayerRecords.length] = -1;
				this.setupMainForm();
				this.setCurrentForm(mMainForm);
			}
			else if (c == mStartCommand)
			{	
				fSuperStore.setLastDungeonNumber(fSuperStore.getDungeonLimit() - mDungeons.getSelectedIndex());
				DungeonRecord theDungeon = fSuperStore.getDungeonRecord(fSuperStore.getLastDungeonNumber());
				int numPlayers = 0;
				for (int i = 0 ; i < kMaxNumPlayers ; i++)
				{
					if (fIncluded[i] == -1)
						break;
					else
						numPlayers++;
				}

				if (theDungeon.isOpen() && numPlayers > 0)
				{
					PlayerRecord[] thePlayers = new PlayerRecord[numPlayers];
					int count = 0;
					for (int i = 0 ; i < kMaxNumPlayers ; i++)
					{
						if (fIncluded[i] != -1)
						{
							thePlayers[count] = fPlayerRecords[fIncluded[i]];
							count++;
						}
						else
							break;
					}
					Utility.Assert(count == numPlayers, "count == numPlayers");

					Phone phone = new Phone(fSuperStore);
					fEngine = new GameEngine(phone, theDungeon, thePlayers);
					phone.init(this);
					fEngine.start();
				}
			}
			else if (c == mTraceCommand)
			{
				Alert alert = new Alert("Trace log:");
				alert.setString(TRACE_LOG);
				alert.setTimeout(Alert.FOREVER);
				Display.getDisplay(this).setCurrent(alert, mMainForm);
			}
			else if (c.getCommandType() == Command.BACK)
			{
				Display.getDisplay(this).setCurrent(mMainForm);
			}
		}
		else if (mCurrentForm == mNewPlayerForm)
		{
			if (c == mCreatePlayerCommand)
			{
				String name = mNameField.getString();
				int race = mRaces.getSelectedIndex() + 1;
				PlayerRecord rec = new PlayerRecord(name, race, 0);
				fSuperStore.addPlayerRecord(rec);
				for (int i = 0 ; i < kMaxNumPlayers ; i++)
				{
					if (fExcluded[i] == -1)
					{
						fExcluded[i] = fPlayerRecords.length;
						fExcluded[i+1] = -1;
						break;
					}
				}
				this.setupMainForm();
				this.setCurrentForm(mMainForm);
			}
			else if (c == mBackCommand)
			{
				this.setCurrentForm(mMainForm);
			}
		}
		else if (mCurrentForm == mAddPlayerForm)
		{
			int selected = mAddPlayerForm.getSelectedIndex();
			for (int i = 0 ; i < kMaxNumPlayers ; i++)
			{
				if (fIncluded[i] == -1)
				{
					fIncluded[i] = fExcluded[selected];
					fIncluded[i+1] = -1;
					break;
				}
			}
			for (int i = selected ; i < kMaxNumPlayers ; i++)
			{
				if (fExcluded[i] != -1)
					fExcluded[i] = fExcluded[i+1];
				else
					break;
			}
			this.setupMainForm();
			this.setCurrentForm(mMainForm);
		}
		else if (mCurrentForm == mRemovePlayerForm)
		{
			int selected = mRemovePlayerForm.getSelectedIndex();
			for (int i = 0 ; i < kMaxNumPlayers ; i++)
			{
				if (fExcluded[i] == -1)
				{
					fExcluded[i] = fIncluded[selected];
					fExcluded[i+1] = -1;
					break;
				}
			}
			for (int i = selected ; i < kMaxNumPlayers ; i++)
			{
				if (fIncluded[i] != -1)
					fIncluded[i] = fIncluded[i+1];
				else
					break;
			}
			this.setupMainForm();
			this.setCurrentForm(mMainForm);
		}
		else
		{
			Utility.Trace("The current form is unknown"); 
		}
	}
	public GameEngine getEngine()
	{
		return fEngine;
	}

	public void doPause()
	{
		this.pauseApp();
		this.notifyPaused();
	}
	public void doExit()
	{
		Utility.Trace("DungeonsMIDlet.doExit - enter");
		Utility.Trace("fSuperStore.close();");
		fSuperStore.close();
		this.destroyApp(true);
		this.notifyDestroyed();
		Utility.Trace("DungeonsMIDlet.doExit - exit");
	}
}
