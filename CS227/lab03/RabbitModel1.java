package lab03;

public class RabbitModel1 extends RabbitModel{

	/**
	 * The current population of rabbits
	 */
	private int population;
	
	/**
	 * The current year
	 */
	private int year;
	
	/**
	 * Constructs a RabbitMode1. Calls the reset method because
	 * it has the same functionality.
	 */
	public RabbitModel1()
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
	 * This version adds one rabbit each year
	 */
	@Override
	public void simulateYear()
	{
		population++;
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
