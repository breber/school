package hw4;

/**
 * Represents a high level question. One can't instantiate a Question,
 * but should rather instantiate a subtype of Question.
 * 
 * @author Brian Reber
 */
public abstract class Question {

	private String question;
	
	/**
	 * Creates a new Question with the given String as the statement to be answered.
	 * 
	 * @param question
	 * The statement to be answered.
	 */
	public Question(String question)
	{
		this.question = question;
	}
	
	@Override
	public String toString()
	{
		return question;
	}
	
	/**
	 * Checks the given answer with the actual answer.
	 * 
	 * @param studentAnswer
	 * The answer that the student guessed.
	 * 
	 * @return
	 * True or false depending on whether the student answer is the same as the
	 * actual answer.
	 * @throws MalformedAnswer 
	 */
	public abstract boolean check(String studentAnswer) throws MalformedAnswer;
}
