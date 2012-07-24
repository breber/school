package hw4;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * A quiz that contains Questions to be administered.
 * 
 * @author Brian Reber
 */
public class Quiz {

	private ArrayList<Question> questions;
	private ArrayList<Boolean> scores;

	/**
	 * Create a new quiz with no questions
	 */
	public Quiz()
	{
		questions = new ArrayList<Question>();
		scores = new ArrayList<Boolean>();
	}

	/**
	 * Add the given question to the quiz
	 * 
	 * @param question
	 * The Question to be added to the quiz
	 */
	public void addQuestion(Question question)
	{
		questions.add(question);
	}

	/**
	 * Administer the quiz.
	 * 
	 * @return
	 * The number of correct answers.
	 */
	public int administer()
	{
		Scanner input = new Scanner(System.in);
		//We will go through each question in the quiz
		for (int i = 0; i < questions.size(); i++)
		{
			String studentAnswer;
			boolean correct = false;
			boolean exceptionThrown = false;

			//Print out the question as many times as necessary until we get a valid answer
			do {
				System.out.println((i + 1) + ". " + questions.get(i));
				System.out.println("Your answer: ");

				//If no exception is caused, we will continue, otherwise, we will continue to
				//print out the question
				try {
					studentAnswer = input.nextLine();
					correct = questions.get(i).check(studentAnswer);
					exceptionThrown = false;
				} catch (MalformedAnswer error) {
					System.out.println("Try again");
					exceptionThrown = true;
				}
			} while (exceptionThrown);
			
			//Add the result to this question to our results arraylist
			scores.add(correct);
		}
		
		int tallyCorrect = 0;
		
		//Go through and print out the results, and count how many correct answers there are
		for (int i = 0; i < scores.size(); i++)
		{
			if (scores.get(i))
				tallyCorrect++;
			System.out.print((i + 1) + ". ");
			System.out.println(scores.get(i) ? "Correct":"Incorrect");
			
		}
		
		System.out.println("Your score: " + tallyCorrect + "/" + scores.size());
		
		return tallyCorrect;
	}

}
