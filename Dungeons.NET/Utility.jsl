//package Catacombs;

/**
 * Summary description for Utility.
 */
public class Utility
{
	public static String ASSERT_LOG = "";
	private static String[] TRACE_BUFFER = null;
	private static int TRACE_INDEX = 0;
	private static int TRACE_SIZE = 10;

	public static Device DEVICE = null;
	public static boolean TRACE = true;

// 	private static int[] FIBMEMO = null;

	public Utility()
	{
	}
/*
	static public void assert(boolean cond)
	{
		if (!cond)
		{
			ASSERT_LOG += "Assertion failed\n";
			System.out.println("Assertion failed");
		}
	}
*/	
	static public void Trace(String str)
	{
		if (TRACE)
		{
			if (TRACE_BUFFER == null)
			{
				TRACE_BUFFER = new String[TRACE_SIZE];
			}

			if (TRACE_BUFFER[TRACE_INDEX] == null)
				TRACE_BUFFER[TRACE_INDEX] = new String();
			TRACE_BUFFER[TRACE_INDEX] = str;
			TRACE_INDEX++;
			if (TRACE_INDEX >= TRACE_SIZE)
				TRACE_INDEX = 0;
		}
		System.out.println("trace: " + str);
	}
	static public String getTraceString()
	{
		String str = "";
		
		if (TRACE_BUFFER != null)
		{
			int index = TRACE_INDEX - 1;
			while (index >= 0)
			{
				if (TRACE_BUFFER[index] != null)
					if (TRACE_BUFFER[index].length() > 0)
						str += TRACE_BUFFER[index] + "\n";
				index--;
			}
			index = TRACE_SIZE-1;
			while (index >= TRACE_INDEX)
			{
				if (TRACE_BUFFER[index] != null)
					if (TRACE_BUFFER[index].length() > 0)
						str += TRACE_BUFFER[index] + "\n";
				index--;
			}
/*
			int index = TRACE_INDEX;
			while (index <= 255)
			{
				if (TRACE_BUFFER[index] != null)
					if (TRACE_BUFFER[index].length() > 0)
						str += TRACE_BUFFER[index] + "\n";
				index++;
			}
			index = 0;
			while (index < TRACE_INDEX)
			{
				if (TRACE_BUFFER[index] != null)
					if (TRACE_BUFFER[index].length() > 0)
						str += TRACE_BUFFER[index] + "\n";
				index++;
			}
*/			
		}
		return str;
	}
	static public void Assert(boolean cond, String str)
	{
		if (!cond)
		{
			TRACE = true;
			ASSERT_LOG += str + "\n";
			Utility.Trace("Assertion failed: " + str);
			if (DEVICE != null)
			{
				DEVICE.flushTrace();
			}
		}
	}
	static String numToStr(int num)
	{
//		String str = String.valueOf(num);
		if (num == 0) return "No";
		if (num == 1) return "One";
		if (num == 2) return "Two";
		if (num == 3) return "Three";
		if (num == 4) return "Four";
		if (num == 5) return "Five";
		if (num == 6) return "Six";
		if (num == 7) return "Seven";
		if (num == 8) return "Eight";
		if (num == 9) return "Nine";
		if (num == 10) return "Ten";
		if (num == 11) return "Eleven";
		if (num == 12) return "Twelve";
		if (num == 13) return "Thirteen";
		if (num == 14) return "Fourteen";
		if (num == 15) return "Fifteen";
		if (num == 16) return "Sixteen";
		if (num == 17) return "Seventeen";
		if (num == 18) return "Eighteen";
		if (num == 19) return "Nineteen";
		if (num >= 100)
			return Integer.toString(num);
		if (num == 90)
			return "Ninty";
		if (num > 90)
			return "Ninty" + numToStr(num - 90).toLowerCase();
		if (num == 80)
			return "Eighty";
		if (num > 80)
			return "Eighty" + numToStr(num - 80).toLowerCase();
		if (num == 70)
			return "Seventy";
		if (num > 70)
			return "Seventy" + numToStr(num - 70).toLowerCase();
		if (num == 60)
			return "Sixty";
		if (num > 60)
			return "Sixty" + numToStr(num - 60).toLowerCase();
		if (num == 50)
			return "Fifty";
		if (num > 50)
			return "Fifty" + numToStr(num - 50).toLowerCase();
		if (num == 40)
			return "Forty";
		if (num > 40)
			return "Forty" + numToStr(num - 40).toLowerCase();
		if (num == 30)
			return "Thirty";
		if (num > 30)
			return "Thirty" + numToStr(num - 30).toLowerCase();
		if (num == 20)
			return "Twenty";
		if (num > 20)
			return "Twenty" + numToStr(num - 20).toLowerCase();
		return Integer.toString(num);
	}
	static String numToStr(int num, String singular, String plural)
	{
		String str = singular; 
		if (num == 0)
			str = "No " + plural;
		else if (num > 1)
			str = Utility.numToStr(num) + " " + plural;
		return str;
	}
	static String numToStrLower(int num, String singular, String plural)
	{
		String str = singular; 
		if (num == 0)
			str = "no " + plural;
		else if (num > 1)
			str = Utility.numToStr(num).toLowerCase() + " " + plural;
		return str;
	}
	static int fib(int num)
	{
		int last = 1;
		int current = 1;
		for (int i = num ; i > 1 ; i--)
		{
			int temp = last + current;
			last = current;
			current = temp;
		}

		return current;
	}

// 	static int fib(int num)
// 	{
// 		if (num < 2)
// 			return 1;
// 		if (FIBMEMO == null)
// 		{
// 			FIBMEMO = new int[30];
// 			FIBMEMO[0] = 1;
// 			FIBMEMO[1] = 1;
// 		}
// 		int f1 = 0;
// 		int f2 = 0;
// 		if (num < 31)
// 		{
// 			f1 = FIBMEMO[num - 1];
// 			f2 = FIBMEMO[num - 2];
// 		}
// 		if (f1 == 0)
// 			f1 = fib(num - 1);
// 		if (f2 == 0)
// 			f2 = fib(num - 2);
// 		int f = f2 + f1;
// 		if (num < 30)
// 			FIBMEMO[num] = f;
// 		return f;
// 	}
	static int min(int num1, int num2)
	{
		if (num1 < num2)
			return num1;
		return num2;
	}
	static int max(int num1, int num2)
	{
		if (num1 > num2)
			return num1;
		return num2;
	}
}
