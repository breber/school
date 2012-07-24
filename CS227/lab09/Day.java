package lab09;

/**
 * @author brianreber
 *
 */
public class Day {

	private String month;
	private int day;

	public Day(String month, int day)
	{
		this.month = month;
		this.day = day;
	}

	@Override
	public String toString()
	{
		String val = month + " " + day;

		String d = day+"";

		String lastDigit = d.substring(d.length()-1);

		switch (Integer.valueOf(lastDigit)) {
		case (1): val+="st"; break;
		case (2): val+="nd"; break;
		case (3): val+="rd"; break;
		default: val+="th"; break;
		}

		return val;
	}
}
