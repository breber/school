package hw4;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Creates a quiz from the file defined in the first argument, and
 * administers it if there are no problems with the text file.
 * 
 * @author Brian Reber
 */
public class Quizzer {

	public static void main(String[] args) {
		
		//Create a quiz
		Quiz quiz = new Quiz();
		
		//If no exception has been thrown, we will set this to false
		boolean exceptionThrown = true;
		
		try {
			File file;
			//If the user specified a filename, we create a file with it.
			try {
				file = new File(args[0]);
			} catch (ArrayIndexOutOfBoundsException e) {
				//Otherwise we throw an IllegalArgumentException
				throw new IllegalArgumentException("No file name provided");
			}
			//Create a new scanner to get the questions from the file
			Scanner questions = new Scanner(file);
			
			//While there are still questions in the file, we read them in
			while (questions.hasNextLine())
			{
				//Get the next line and split it using the ~ character
				String line = questions.nextLine();
				String[] tokenized = line.split("~");
				//If it has more than two sections, it is a MultipleChoiceQuestion
				if (tokenized.length > 2)
				{
					//Copy the options to a new array
					String[] temp = java.util.Arrays.copyOfRange(tokenized, 2, tokenized.length);
					//And create a new MultipleChoiceQuestion
					quiz.addQuestion(new MultipleChoiceQuestion(tokenized[0], Integer.parseInt(tokenized[1]), temp));
				} else if (tokenized[0].contains("___")) {
					//If it has ___ in it, it is a fill in the blank, so we create one of those
					quiz.addQuestion(new FillInTheBlankQuestion(tokenized[0],tokenized[1]));
				} else {
					//Otherwise it is a truefalse question
					quiz.addQuestion(new TrueFalseQuestion(tokenized[0], tokenized[1].equalsIgnoreCase("true")));
				}
			}
			//If we got here, we know no exceptions were thrown, so we know we can administer the quiz
			exceptionThrown = false;
			
		} catch (FileNotFoundException e) {
			System.err.println("Can't read quiz file");
		} catch (IllegalArgumentException e) {
			System.err.println(e.getMessage());
		}
		
		//If no exception was thrown, we will administer the quiz.
		if (!exceptionThrown)
			quiz.administer();
	}

}
