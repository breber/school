package lab03;

import java.util.Random;

public class RabbitModel5 extends RabbitModel{

	/**
	 * The current population of rabbits
	 */
	private int population;
	
	/**
	 * The current year
	 */
	private int year;
	
	/**
	 * Random number generator
	 */
	Random rand = new Random(100);
	
	/**
	 * Constructs a RabbitModel. Calls the reset method because
	 * it has the same functionality.
	 */
	public RabbitModel5()
	{
		reset();
		year = 0;
	}
	
	/**
	 * Resets the instance variables to their defaults.
	 */
	@Override
	public void reset()
	{
		population = 0;
	}
	
	/**
	 * Simulates a year according to specifications
	 * 
	 * This version a random number (0-10) of rabbits each year
	 */
	@Override
	public void simulateYear()
	{
		population += rand.nextInt(10);
		year++;
	}
	
	/**
	 * Gets the current population
	 * @return
	 * The current population
	 */
	@Override
	public int getPopulation()
	{
		return population;
	}
	
}
