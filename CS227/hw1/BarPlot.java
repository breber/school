package hw1;

public class BarPlot {

	/**
	 * The height of each bar
	 */
	public static final int BAR_HEIGHT = 20;
	
	/**
	 * Plots a bar at a specific (x,y) coordinate, with the bar width and label
	 * @param xCord
	 * X-Coordinate of the bar's lower-left corner
	 * @param yCord
	 * Y-Coordinate of the bar's lower-left corner
	 * @param barWidth
	 * The width of the bar
	 * @param textLabel
	 * The label that goes with the bar
	 */
	public static void plotBar(int xCord, int yCord, double barWidth, String textLabel)
	{
		System.out.println("newpath");
		System.out.println(xCord + " " + yCord + " moveto");
		System.out.println(barWidth + " 0 rlineto");
		System.out.println("0 " + BAR_HEIGHT + " rlineto");
		System.out.println((-barWidth) + " 0 rlineto");
		System.out.println("closepath");
		System.out.println("fill");
		System.out.println((barWidth + xCord) + " " + yCord + " moveto");
		System.out.println("(" + textLabel + ") show");
	}
	
}
