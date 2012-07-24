package lab03;

public class RabbitModel3 extends RabbitModel{

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
	public RabbitModel3()
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
	 * This version by 1 each year, but after 5 years it drops down to 0
	 */
	@Override
	public void simulateYear()
	{
		if (year % 5 == 0)
			reset();
		else population++;
		
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
