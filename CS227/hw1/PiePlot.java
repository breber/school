package hw1;

public class PiePlot {

	/**
	 * The x-coordinate of the center of the pie plot
	 */
	private int centerX;
	/**
	 * The y-coordinate of the center of the pie plot
	 */
	private int centerY;
	/**
	 * The radius of the pie plot
	 */
	private int radius;
	/**
	 * The amount of degrees that the previous slice ended at.
	 * Provides the starting degrees for the new slice.
	 */
	private double preDegrees;
	
	
	/**
	 * Create a new pie plot
	 * 
	 * @param centerXCord
	 * The x-coordinate of the center
	 * @param centerYCord
	 * The y-coordinate of the center
	 * @param _radius
	 * The radius of the pie plot
	 */
	public PiePlot(int centerXCord, int centerYCord, int _radius)
	{
		centerX = centerXCord;
		centerY = centerYCord;
		radius = _radius;
		preDegrees = 0;
	}
	
	/**
	 * Add a new slice to the pie
	 * 
	 * @param percent
	 * Percent of the pie to use
	 * @param sliceLabel
	 * The label that goes with this slice
	 */
	public void plotSlice(double percent, String sliceLabel)
	{
		// We add the percent multiplied by 360 to the current preDegrees
		// to get the ending degrees of this slice
		double postDegrees = preDegrees + (percent/100 * 360);
		double minDegrees = (postDegrees - preDegrees) / 2 + preDegrees;
		
		// Convert the preDegrees to radians to be used in cos/sin functions.
		double radPre = Math.toRadians(preDegrees);
		double preDegreesX = (radius * Math.cos(radPre));
		double preDegreesY = (radius * Math.sin(radPre));
		
		System.out.println("newpath");
		System.out.println(centerX + " " + centerY + " moveto");
		System.out.println(preDegreesX + " " + preDegreesY + " rlineto");
		System.out.println(centerX + " " + centerY + " " + radius + " " + preDegrees + " " + postDegrees + " arc");
		System.out.println(centerX + " " + centerY + " lineto");
		System.out.println("closepath");
		System.out.println("stroke");
		System.out.println("gsave");
		System.out.println(centerX + " " + centerY + " moveto");
		System.out.println(minDegrees + " rotate");
		System.out.println(radius + " 0 rmoveto");
		System.out.println("(" + sliceLabel + ") show");
		System.out.println("grestore");
	
		// Reset the preDegrees to the current post degrees to keep it in sync
		preDegrees = postDegrees;
	}
}
