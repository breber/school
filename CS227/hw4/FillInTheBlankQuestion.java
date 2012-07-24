package hw4;

/**
 * A Fill in the Blank question.
 * 
 * @author Brian Reber
 */
public class FillInTheBlankQuestion extends Question {

	private String answer;
	
	/**
	 * Creates a new FillInTheBlankQuestion.
	 * 
	 * @param question
	 * The statement to be answered.
	 * @param answer
	 * The correct answer to the question.
	 */
	public FillInTheBlankQuestion(String question, String answer) {
		super(question);
		this.answer = answer;
	}

	/* (non-Javadoc)
	 * @see hw4.Question#check(java.lang.String)
	 */
	@Override
	public boolean check(String studentAnswer) throws MalformedAnswer {
		if (studentAnswer.equals(""))
			throw new MalformedAnswer("No blank filled");

		return studentAnswer.equalsIgnoreCase(answer);
	}

}
