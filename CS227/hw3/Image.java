package hw3;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Encapsulates a 2-D grid of pixels.
 * 
 * @author Brian Reber
 */
public class Image {

	/**
	 * The 2D array containing the Color elements for the Image
	 */
	private Color[][] grid;

	/**
	 * A list of Effects pending application
	 */
	private ArrayList<Effect> effects = new ArrayList<Effect>();

	/**
	 * The maximum color intensity
	 */
	private int maxColorIntensity;

	/**
	 * Constructs a new Image with given dimensions.
	 * By default there are no effects, and it is White
	 * 
	 * @param width
	 * The width of the new image
	 * @param height
	 * The height of the new image
	 */
	public Image(int width, int height)
	{
		grid = new Color[width][height];
		this.maxColorIntensity = 255;

		for (int col = 0; col < width; col++)
		{
			for (int row = 0; row < height; row++)
			{
				grid[col][row] = Color.WHITE;
			}
		}
	}

	/**
	 * Creates an image with no height or width
	 */
	public Image()
	{
		grid = new Color[0][0];
	}

	/**
	 * Loads a PPM from the given path
	 * 
	 * @param path
	 * The path to the PPM file to read in.
	 * @throws FileNotFoundException
	 */
	public void load(String path) throws FileNotFoundException
	{
		Scanner file = new Scanner(new File(path));
		//read in the "P3"
		file.next();

		//Read in the next - we can check if it is a comment, 
		//otherwise we will parse it as an int (dimensions)
		String temp = file.next();

		//If file.next() starts with a #, we know it is a comment,
		//so we can take the next line.
		if (temp.startsWith("#", 0))
		{
			file.nextLine();
			grid = new Color[file.nextInt()][file.nextInt()];

		} else {
			grid = new Color[Integer.parseInt(temp)][file.nextInt()];
		}

		//max value
		maxColorIntensity = file.nextInt();

		for (int row = 0; row < getHeight(); row++)
		{
			for (int col = 0; col < getWidth(); col++)
			{
				int red = file.nextInt();
				int green = file.nextInt();
				int blue = file.nextInt();

				grid[col][row] = new Color(red,green,blue);
			}
		}
		file.close();
	}

	/**
	 * Gets the width of the current Image
	 * 
	 * @return
	 * The width of the current Image.
	 */
	public int getWidth()
	{
		return grid.length;
	}

	/**
	 * Gets the height of the current Image
	 * 
	 * @return
	 * The height of the current Image.
	 */
	public int getHeight()
	{
		return grid[0].length;
	}

	/**
	 * Gets the color instance at the given coordinates.
	 * Using this type of coordinate system.
	 * 
	 * (2,0) (2,1) (2,2)
	 * (1,0) (1,1) (1,2)
	 * (0,0) (1,0) (2,0)
	 * 
	 * @param col
	 * The column of the pixel to get.
	 * @param row
	 * The row of the pixel to get.
	 * @return
	 * The color instance at the given row and col.
	 */
	public Color getPixel(int col, int row)
	{
		return grid[col][getHeight() - 1 - row];
	}

	/**
	 * Sets the color instance at the given coordinates to the given Color
	 * Using this type of coordinate system.
	 * 
	 * (2,0) (2,1) (2,2)
	 * (1,0) (1,1) (1,2)
	 * (0,0) (1,0) (2,0)
	 * 
	 * @param col
	 * The column of the pixel to be set.
	 * @param row
	 * The row of the pixel to be set
	 * @param color
	 * The color to set (col, row).
	 */
	public void setPixel(int col, int row, Color color)
	{
		grid[col][getHeight() - 1 - row] = color;
	}

	/**
	 * Crops the image starting at the given column and row, giving
	 * the cropped image the given width and heights.
	 * 
	 * <b>Based off the bottom-left being (0,0)</b>
	 * 
	 * @param col
	 * The column of the new origin.
	 * @param row
	 * The row of the new origin.
	 * @param width
	 * The new width.
	 * @param height
	 * The new height.
	 */
	public void crop(int col, int row, int width, int height)
	{
		Color[][] temp = new Color[width][height];

		for (int c = col; c < (width + col); c++)
		{
			for (int r = row; r < (height + row); r++)
			{
				temp[c - col][height - 1 - (r - row)] = grid[c][getHeight() - 1 - r];
			}
		}
		grid = temp;
	}

	/**
	 * Rotates the image 90 degrees clockwise
	 */
	public void rotate()
	{
		int height = getWidth();
		int width = getHeight();

		Color[][] temp = new Color[width][height];

		for (int col = 0; col < width; col++)
		{
			for (int row = 0; row < height; row++)
			{
				Color c = grid[row][col];
				temp[width - col - 1][row] = c;
			}
		}

		grid = temp.clone();
	}

	/**
	 * Writes the current instance of Image to a file at the given path
	 * 
	 * @param path
	 * The path to which the image will be written.
	 * @throws IOException
	 */
	public void writeTo(String path) throws IOException
	{
		PrintStream out = new PrintStream(path);
		out.println("P3");
		out.println(getWidth() + " " + getHeight());
		out.println(maxColorIntensity);

		for (int row = 0; row < getHeight(); row++)
		{
			for (int col = 0; col < getWidth(); col++)
			{
				Color c = grid[col][row];
				out.print(c.getRed()+" "+c.getGreen()+" "+c.getBlue()+" ");
			}
			out.println();
		}
		out.close();
	}

	/**
	 * Adds the effect to a list of Effects to undergo, but doesn't apply it.
	 * 
	 * Call {@link hw3.Image#transform()} to apply the transforms
	 * 
	 * @param effect
	 * The effect to add to the list of Effects.
	 */
	public void addTransform(Effect effect)
	{
		effects.add(effect);
	}

	/**
	 * Applies all effects in the list to the image in the order they were added.
	 */
	public void transform()
	{
		for (Effect e : effects)
		{
			e.apply(this);
		}
	}
}
