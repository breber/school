package hw4;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;

public class QuizzerTest {

   private Quiz quiz;

   /** Set to the number of questions in your test */
   private static final int NUM_QUESTIONS = 7;

   /** "Real" stdin */
   public static final InputStream stdin = System.in;
   
   @Before
   public void setup() {
    String[] q2ans = {"To seek the holy grail","what?","nee!"},
             q3ans = {"Blue","No, Yellow"},
             q4ans = {"Ashur","Calah","Dur Sharrukin","Nineveh","Qalat Sherqat","Nimrud","Khorsabad","Bagdhad","Near Constantinople and Persia, smart guy"},
             q9ans = {"8.8m/s","20mph","True"};
    this.quiz = new Quiz();
    this.quiz.addQuestion(new TrueFalseQuestion("What is your name", false));
    this.quiz.addQuestion(new MultipleChoiceQuestion("What is your quest?", 0, q2ans));
    this.quiz.addQuestion(new MultipleChoiceQuestion("What is your favorite color?", 1, q3ans));
    this.quiz.addQuestion(new MultipleChoiceQuestion("What is the capital of Assyria?",8,q4ans));
    this.quiz.addQuestion(new FillInTheBlankQuestion("The airspeed velocity of an unladen swallow is ___.", "African or European"));
    this.quiz.addQuestion(new FillInTheBlankQuestion("The average airspeed velocity of a hypothetical Hirundo rustica which is unladen but whose claws have both the extension capacity and strength to grasp a coconut by the husk, and whose wings can generate lift and propolsion as per an average Hirundo rustica, and whose migratory pattern takes it through Cornwal in South West England, after starting at the Easternmost point off the coast of Brazil, before finally resting in the Fjords of Norway, assuming the swallow does not need to stop for food or rest, is ___.", "Am I to assume the coconut weighs 8 kilograms"));
    this.quiz.addQuestion(new MultipleChoiceQuestion("Yes, you are to assume the coconut weighs 8 kilograms.", 2, q9ans));
   }
   
   @Test
   /**
    * Tests hw4.Quiz using a Monty Python quiz
    *
    * Thanks to
    *   http://stackoverflow.com/questions/1647907/junit-how-to-simulate-system-in-testing
    *   for the code to switch stdin, and
    *   http://stackoverflow.com/questions/1235179/simple-way-to-repeat-a-string-in-java
    *   for String.repeat implementation using only String.format, and to
    * John Cleese, Graham Chapman, et al
    *   for being the funniest men alive
    *
    * @author Sean McClain <seanm1@iastate.edu>
    * @author McDowell http://stackoverflow.com/users/304/mcdowell
    * @author I. J. Kennedy http://stackoverflow.com/users/8677/i-j-kennedy
    */
   public void testQuizzer() {
      String data = String.format (
        String.format (
          String.format("%%0%dd", NUM_QUESTIONS), 0
          ).replace("0","%s%n"),
        "F",
        "1",
        "2",
        "9",
        "AfRiCaN OR EuRoPeAn",
        "am I to ASSUME the COCONUT weighs 8 KiLoGrAmS",
        "3"
        );

      // All questions are right
      System.setIn(new ByteArrayInputStream(data.getBytes()));
      assertEquals(this.quiz.administer(), 7);

      // Test true false
      data = data.replace("F", "T");
      System.setIn(new ByteArrayInputStream(data.getBytes()));
      assertEquals(this.quiz.administer(), 6);

      // Test fill-in-the-blank
      data = data.replace("AfRiCaN OR EuRoPeAn", "Yes");
      System.setIn(new ByteArrayInputStream(data.getBytes()));
      assertEquals(this.quiz.administer(), 5);

      // Test multiple choice
      data = data.replace("9", "3");
      System.setIn(new ByteArrayInputStream(data.getBytes()));
      assertEquals(this.quiz.administer(), 4);

      System.setIn(QuizzerTest.stdin);
   }
}