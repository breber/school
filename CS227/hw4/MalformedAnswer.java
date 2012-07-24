package hw4;

/**
 * An exception that can be thrown when an answer is not in the correct format
 * for the specific question.
 * 
 * @author Brian Reber
 */
@SuppressWarnings("serial")
public class MalformedAnswer extends Exception{

	public MalformedAnswer(String message)
	{
		super(message);
	}
	
}
