//package Catacombs;

//import javax.microedition.lcdui.*;
//import javax.microedition.midlet.*;

/**
 * Summary description for DungeonRecord.
 */
public class DungeonRecord
{
    public int fNumber;
    public boolean fCompleted;
	protected long fOpenTime;
    protected long fTreasureTime;
    public String fName;
    public String fContent;
    public String fDescription;

	private static int kLairShare = 60;
	private static int kMaxMonsterScore = 100000; // 50000;
//	public static String kInitialName = "unknown";

// 	public boolean[] explored_rooms = null;

	//	public boolean fIncluded = false;

	public DungeonRecord(int number)
	{
		fNumber = number;
		fCompleted = false;
		fOpenTime = System.currentTimeMillis();
        fTreasureTime = fOpenTime;
		fName = "";
        fContent = "";
		fDescription = "";
	}
	public int getMinRooms()
	{
		return 5 + 1 * this.fNumber;
	}
	public int getMaxRooms()
	{
		return 5 + 2 * this.fNumber;
	}
	public int getLairPoints()
	{
		int score = (int) ((kLairShare * this.getTotalScore()) / 100) + 1;
		if (score < 2)
			score = 2;
		return score;
	}
	public int getMonsterPoints()
	{
		int score = (int) ((100 - kLairShare) * this.getTotalScore() / 100);
		return score;
	}
	protected int getTotalScore()
	{
// 		int test = Utility.fib(0);
// 		test = Utility.fib(1);
// 		test = Utility.fib(2);
// 		test = Utility.fib(5);

		int score = Utility.fib(this.fNumber + 2);
		if (score > kMaxMonsterScore)
			score = kMaxMonsterScore;
		return score;
	}
	public boolean isOpen()
	{
		long span = fOpenTime - System.currentTimeMillis();
		if (span > 0)
			return false;
		return true;
	}
    public boolean hasTreasure()
    {
        long span = fTreasureTime - System.currentTimeMillis();
        if (span > 0)
            return false;
        return true;
    }
	public void setClosed(int minutes)
	{
		Utility.Trace("DungeonRecord.setClosed - enter");
		long second = 1000;
		long minute = 60 * second;
		long hour = 60 * minute;
		long day = 24 * hour;
//		long wait = this.getTotalScore() * 60 * 1000;
//		long wait = this.getTotalScore() * 10 * 1000;
        long wait = (this.fNumber + 2) * minute + this.getTotalScore() * 10 * second;
//		if (this.isCompleted())
		{
			if (wait < hour)
				wait = hour; 
			if (wait > day)
				wait = day;
		}
		// wait one hour always
		wait = hour;
		wait = minutes * minute;
		// TODO: Remove this CHEAT code
//		wait = minute;
		Utility.Trace("DungeonRecord.setClosed - wait " + wait + "ms");
		fOpenTime = System.currentTimeMillis() + wait;
		Utility.Trace("DungeonRecord.setClosed - treasure in " + 6 * wait + "ms");
		fTreasureTime = System.currentTimeMillis() + 6 * wait;
		Utility.Trace("DungeonRecord.setClosed - exit");
	}
	public String toDisplayString()
	{
		String str = fNumber + ".";
		long span = fOpenTime - System.currentTimeMillis();
		if (span > 0)
		{
			Timespan t = new Timespan(span);
			str += " " + t.toDisplayString();
		}
		else
		{
            span = fTreasureTime - System.currentTimeMillis();
            if (span <= 0 && fContent != "")
                str += " " + fContent + " - ";
            str += " " + this.fName;
		}
		return str;
	}
	public String toString()
	{
		Utility.Trace("DungeonRecord.toString - enter");
        String str = this.fNumber + ". " + this.fName + "\n";
// 		if (explored_rooms != null)
// 		{
// 			str += " explored: ";
// 			for (int i = 0; i < explored_rooms.length; i++)
// 			{
// 				str += explored_rooms[i] + " ";
// 			}
// 			str += "\n";
// 		}
		str += " compl: " + fCompleted;
		long span = fOpenTime - System.currentTimeMillis();
		Utility.Trace("DungeonRecord.toString - open span: " + span);
        if (span <= 0)
        {
            span = fTreasureTime - System.currentTimeMillis();
			Utility.Trace("DungeonRecord.toString - treasure span: " + span);
			if (span > 0)
			{
				Timespan t = new Timespan(span);
				str += " empty. " + t.toDisplayString();
			}
			else
				str += " open.";
        }
        else
        {
            Timespan t = new Timespan(span);
            str += " closed. " + t.toDisplayString();
        }
		Utility.Trace("DungeonRecord.toString - returns: " + str);
		return str;
	}
}
