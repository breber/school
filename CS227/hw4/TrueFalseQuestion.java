package hw4;

/**
 * A more specific Question. Represents a true/false question.
 * 
 * @author Brian Reber
 */
public class TrueFalseQuestion extends Question {

	private boolean correctAnswer;
	
	/**
	 * Creates a new TrueFalseQuestion
	 * 
	 * @param question
	 * The statement to be answered.
	 * @param correctAnswer
	 * The correct answer to the question
	 */
	public TrueFalseQuestion(String question, boolean correctAnswer) {
		super(question);
		
		this.correctAnswer = correctAnswer;
	}
	
	/**
	 * Returns "True or false:" followed by the question
	 */
	@Override
	public String toString()
	{
		return "True or false: " + super.toString();
	}

	/* (non-Javadoc)
	 * @see hw4.Question#check(java.lang.String)
	 */
	@Override
	public boolean check(String studentAnswer) throws MalformedAnswer {
		//If the student answer is F or f we will return true only if
		//the correct answer is false
		if (studentAnswer.equalsIgnoreCase("F"))
			return correctAnswer == false;
		//If the student answer is T or t, we will return true only if
		//the correct answer is true
		else if (studentAnswer.equalsIgnoreCase("T"))
			return correctAnswer == true;
		//If the student entered an invalid string, we will throw an exception
		else
			throw new MalformedAnswer("Answer T or F");
	}

}
