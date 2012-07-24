package edu.iastate.cs228.hw4.test;

import org.junit.Test;

import edu.iastate.cs228.hw4.ExpressionTrees;
import edu.iastate.cs228.hw4.ParseException;

public class ExpressionTest {

	@Test
	public void testCreateTreeCorrectFormatting() throws ParseException {
		ExpressionTrees.createTree("(2+3)*4");
	}

	@Test
	public void testCreateTreeCorrectFormattingWithSpaces() throws ParseException {
		ExpressionTrees.createTree("( 2 + 3 ) * 4");
	}

	@Test(expected = ParseException.class)
	public void testCreateTree2Operands() throws ParseException {
		ExpressionTrees.createTree("(2 3 + 3 ) * 4");
	}

	@Test(expected = ParseException.class)
	public void testCreateTree2Operators() throws ParseException {
		ExpressionTrees.createTree("(2 + 3) / * 4");
	}
	
	@Test(expected = ParseException.class)
	public void testCreateTreeParseException() throws ParseException {
		ExpressionTrees.createTree("(()*(2))");
	}
	
	@Test(expected = ParseException.class)
	public void testCreateTreeParseException2() throws ParseException {
		ExpressionTrees.createTree("(()(2))");
	}

	@Test
	public void testCreateTreeNegationBeforeOperand() throws ParseException {
		ExpressionTrees.createTree("(-2 + 3 ) * 4");
	}

	@Test
	public void testCreateTreeNegationBeforeLParen() throws ParseException {
		ExpressionTrees.createTree("-(2 + 3 ) * 4");
	}

	@Test(expected = ParseException.class)
	public void testCreateTreeNegationBeforeOperator() throws ParseException {
		ExpressionTrees.createTree("(2 + 3 ) - * 4");
	}

	@Test
	public void testCreateTreeNegation() throws ParseException {
		ExpressionTrees.createTree("(2 + -3 ) * -4");
	}

	@Test(expected = ParseException.class)
	public void testCreateTreeInitialOperand() throws ParseException {
		ExpressionTrees.createTree("* (2 + 3 ) * 4");
	}

	@Test(expected = ParseException.class)
	public void testCreateTreeIntWithText() throws ParseException {
		ExpressionTrees.createTree("(2test + -3 ) * -4");
	}

	@Test(expected = ParseException.class)
	public void testCreateTreeMissingOperand() throws ParseException {
		ExpressionTrees.createTree("(2 + -3 ) * -4 /");
	}

	@Test(expected = ParseException.class)
	public void testCreateTreeExtraOperand() throws ParseException {
		ExpressionTrees.createTree("(2 + -3 ) * -4 / 8 * 9 8");
	}

	@Test(expected = ParseException.class)
	public void testCreateTreeExtraLParens() throws ParseException {
		ExpressionTrees.createTree("(2 + -3 ) * (-4 / 8 * 9 8");
	}

	@Test(expected = ParseException.class)
	public void testCreateTreeExtraRParens() throws ParseException {
		ExpressionTrees.createTree("(2 + -3) * (-4 / 8) * 9 8)");
	}

	@Test(expected = ParseException.class)
	public void testCreateTreeExtraOperators() throws ParseException {
		ExpressionTrees.createTree("(2 + -3 ) * (-4 / 8 * 9) *");
	}

	@Test(expected = ParseException.class)
	public void testCreateTreeNegateEnd() throws ParseException {
		ExpressionTrees.createTree("(2 + -3 ) * (-4 / 8 * 9) * -");
	}

	@Test
	public void testCreateTreeManyNegationAndVariables() throws ParseException {
		ExpressionTrees.createTree("-((-2 * -b ^ 5 + --4) / c)");
	}

	@Test(expected = ParseException.class)
	public void testCreateTreeEmptySpace() throws ParseException {
		ExpressionTrees.createTree(" ");
	}

	@Test(expected = ParseException.class)
	public void testCreateTreeEmptySpaceParenthesis() throws ParseException {
		ExpressionTrees.createTree("( )");
	}
}
