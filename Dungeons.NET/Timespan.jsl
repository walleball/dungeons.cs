//package Dungeons;

/**
 * Summary description for Timespan.
 */
public class Timespan
{
	private int fDays;
	private int fHours;
	private int fMinutes;
	private int fSeconds;
	public Timespan(long timespan)
	{
// 		Utility.Trace("Timespan - enter");
		long span = (long) timespan / 1000;
		fSeconds = (int) (span % 60); span /= 60;
		fMinutes = (int) (span % 60); span /= 60;
		fHours = (int) (span % 24); span /= 24;
		fDays = (int) span;
// 		Utility.Trace("Timespan(" + span + "): " + this.toString());
	}
	public int getDays()
	{
		if (fDays > 0)
			if (this.getHours() >= 12)
				return fDays + 1;
		return fDays;
	}
	public int getHours()
	{
		if (fHours > 0)
			if (this.getMinutes() >= 30)
				return fHours + 1;
		return fHours;
	}
	public int getMinutes()
	{
		if (fMinutes > 0)
			if (this.getSeconds() >= 30)
				return fMinutes + 1;
		return fMinutes;
	}
	public int getSeconds()
	{
		return fSeconds;
	}
	public String toString()
	{
		String str;
		if (fDays > 0)
			str = fDays + "d " + fHours + "h " + fMinutes + "m " + fSeconds + "s";
		else if (fHours > 0)
			str = fHours + "h " + fMinutes + "m " + fSeconds + "s";
		else if (fMinutes > 0)
			str = fMinutes + "m " + fSeconds + "s";
		else if (fSeconds > 0)
			str = fSeconds + "s";
		else
			str = "Now";
		return str;
	}
	public String toDisplayString()
	{
		if (this.getDays() > 0)
			return Utility.numToStr(this.getDays(), "One day", "days");
		if (this.getHours() > 0)
			return Utility.numToStr(this.getHours(), "One hour", "hours");
		if (this.getMinutes() > 0)
			return Utility.numToStr(this.getMinutes(), "One minute", "minutes");
		return Utility.numToStr(this.getSeconds(), "One second", "seconds");
	}
}
