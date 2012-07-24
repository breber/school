package lab03;


public class RabbitModel {

	/**
	 * The current population of rabbits
	 */
	private int population;
	
	/**
	 * The current year
	 */
	private int year;
	
	/**
	 * Variables used to store the previous two population values
	 * used for the Fibonacci sequence.
	 */
	private int prev1, prev2;
	
	
	/**
	 * Constructs a RabbitModel. Calls the reset method because
	 * it has the same functionality.
	 */
	public RabbitModel()
	{
		reset();
		year = 0;
	}
	
	/**
	 * Resets the instance variables to their defaults.
	 */
	public void reset()
	{
		population = 0;
		prev1 = 1;
		prev2 = 1;
	}
	
	/**
	 * Simulates a year according to specifications
	 * 
	 * This version follows the Fibonacci sequence
	 */
	public void simulateYear()
	{
		population = prev1 + prev2;
	
		if (year % 2 == 0)
			prev1 = population;
		else prev2 = population;
		
		year++;
	}
	
	/**
	 * Gets the current population
	 * @return
	 * The current population
	 */
	public int getPopulation()
	{
		return population;
	}
	
}
