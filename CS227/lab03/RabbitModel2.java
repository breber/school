package lab03;

public class RabbitModel2 extends RabbitModel{

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
	public RabbitModel2()
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
		population = 600;
	}
	
	/**
	 * Simulates a year according to specifications
	 * 
	 * This version decreases population by half each year
	 */
	@Override
	public void simulateYear()
	{
		population /= 2;
		
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
