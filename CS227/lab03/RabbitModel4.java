package lab03;

public class RabbitModel4 extends RabbitModel{

	/**
	 * The current population of rabbits
	 */
	private int population;
	
	/**
	 * The current year
	 */
	private int year;
	
	/**
	 * Constructs a RabbitModel. Calls the reset method because
	 * it has the same functionality.
	 */
	public RabbitModel4()
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
	 * This version models by 40 * | sin(year) |
	 */
	@Override
	public void simulateYear()
	{
		population = (int) (40 * Math.abs(Math.sin(year)));
		
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
