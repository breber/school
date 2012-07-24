package hw4;

import java.util.Scanner;

/**
 * A Multiple Choice Question with any amount of possible options.
 * 
 * @author Brian Reber
 */
public class MultipleChoiceQuestion extends Question {

	private String[] possibleAnswers;
	private int correctIndex;
	
	/**
	 * Creates a new MultipleChoiceQuestion.
	 * 
	 * @param question
	 * The statement to be answered
	 * @param indexCorrectAnswer
	 * The index of the correct answer
	 * @param possibleAnswers
	 * An array of possible answers
	 */
	public MultipleChoiceQuestion(String question, int indexCorrectAnswer, String[] possibleAnswers) {
		super(question);
		
		correctIndex = indexCorrectAnswer;
		this.possibleAnswers = possibleAnswers;
	}
	
	/**
	 * Returns the question, with each possible answer on it's own line
	 */
	@Override
	public String toString()
	{
		String output = "";
		for (int i = 0; i < possibleAnswers.length; i++)
		{
			output += "\n" + i + " " + possibleAnswers[i];
		}
		return super.toString() + output; 
	}

	/* (non-Javadoc)
	 * @see hw4.Question#check(java.lang.String)
	 */
	@Override
	public boolean check(String studentAnswer) throws MalformedAnswer {
		int givenIndex = -1;
		
		Scanner in = new Scanner(studentAnswer);
		//If the student's answer has an int we will set the givenIndex to that value
		if (in.hasNextInt())
			givenIndex = in.nextInt();
		
		//If their value is out of range (it is by default, so if they don't enter an index it will automatically
		//be out of rance), we will throw a MalformedAnswer exception
		if (givenIndex < 0 || givenIndex > (possibleAnswers.length - 1))
			throw new MalformedAnswer("Please enter an number in the range 0 - " + (possibleAnswers.length - 1));
		
		//If the indexes match up, they got it correct
		return (givenIndex == correctIndex);
	}

}
