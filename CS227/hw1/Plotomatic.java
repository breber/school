package hw1;

/**
 * Outputs postscript making a bar graph and 
 * a pie graph on the topit "Where are they now?"
 * 
 * @author brianreber
 *
 */
public class Plotomatic {

	public static void main(String[] args) {
				
		System.out.println("%!PS-Adobe-3.0 EPSF-3.0");
		System.out.println("% where are they now?");
		System.out.println("/Times-Roman findfont");
		System.out.println(BarPlot.BAR_HEIGHT + " scalefont");
		System.out.println("setfont");

		// The bar plot
		BarPlot.plotBar(20, 20 + BarPlot.BAR_HEIGHT * 0, 33.6, "Graduated in <= 4 years");
		BarPlot.plotBar(20, 20 + BarPlot.BAR_HEIGHT * 1, 6.4, "Left 3rd year or after");
		BarPlot.plotBar(20, 20 + BarPlot.BAR_HEIGHT * 2, 8.5, "Left 2nd year");
		BarPlot.plotBar(20, 20 + BarPlot.BAR_HEIGHT * 3, 15.8, "Left 1st year");
		BarPlot.plotBar(20, 20 + BarPlot.BAR_HEIGHT * 4, 1.9, "Still enrolled after 6 years");
		BarPlot.plotBar(20, 20 + BarPlot.BAR_HEIGHT * 5, 4.9, "Graduated 6th year");
		BarPlot.plotBar(20, 20 + BarPlot.BAR_HEIGHT * 6, 28.9, "Graduated 5th year");
		
		// We create a pie plot
		PiePlot pie = new PiePlot(350, 450, 100);
		
		// And add slices
		pie.plotSlice(33.6, "Graduated in <= 4 years");
		pie.plotSlice(6.4, "Left 3rd year or after");
		pie.plotSlice(8.5, "Left 2nd year");
		pie.plotSlice(15.8, "Left 1st year");
		pie.plotSlice(1.9, "Still enrolled after 6 years");
		pie.plotSlice(4.9, "Graduated 6th year");
		pie.plotSlice(28.9, "Graduated 5th year");
		
		System.out.println("showpage");

	}
	
}
