package edu.iastate.cs228.hw4.test;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.iastate.cs228.hw4.ExpressionTrees;
import edu.iastate.cs228.hw4.ParseException;
import edu.iastate.cs228.hw4.UnboundIdentifierException;
import edu.iastate.cs228.hw4.nodes.DivNode;
import edu.iastate.cs228.hw4.nodes.ExpNode;
import edu.iastate.cs228.hw4.nodes.IntNode;
import edu.iastate.cs228.hw4.nodes.MinusNode;
import edu.iastate.cs228.hw4.nodes.NegationNode;
import edu.iastate.cs228.hw4.nodes.PlusNode;
import edu.iastate.cs228.hw4.nodes.TimesNode;
import edu.iastate.cs228.hw4.nodes.TreeNode;

/**
 * @author brianreber
 *
 */
public class EvaluateTests {

	@Test
	public void testSimpleAddition() throws UnboundIdentifierException, ParseException
	{
		IntNode first = new IntNode(3);
		IntNode second = new IntNode(4);
		PlusNode operation = new PlusNode(first, second);
		assertEquals(7, ExpressionTrees.evaluate(operation));

		TreeNode tree = ExpressionTrees.createTree("3 + 4");
		assertEquals("Problem when performing operation by using createTree()", 7, ExpressionTrees.evaluate(tree));
	}

	@Test
	public void testSimpleMultiplication() throws UnboundIdentifierException, ParseException
	{
		IntNode first = new IntNode(5);
		IntNode second = new IntNode(4);
		TimesNode operation = new TimesNode(first, second);
		assertEquals(20, ExpressionTrees.evaluate(operation));

		TreeNode tree = ExpressionTrees.createTree("5 * 4");
		assertEquals("Problem when performing operation by using createTree()", 20, ExpressionTrees.evaluate(tree));
	}

	@Test
	public void testSimpleSubtraction() throws UnboundIdentifierException, ParseException
	{
		IntNode first = new IntNode(7);
		IntNode second = new IntNode(2);
		MinusNode operation = new MinusNode(first, second);
		assertEquals(5, ExpressionTrees.evaluate(operation));

		TreeNode tree = ExpressionTrees.createTree("7-2");
		assertEquals("Problem when performing operation by using createTree()", 5, ExpressionTrees.evaluate(tree));
	}

	@Test
	public void testSimpleSubtractionNegative() throws UnboundIdentifierException, ParseException
	{
		IntNode first = new IntNode(3);
		IntNode second = new IntNode(5);
		MinusNode operation = new MinusNode(first, second);
		assertEquals(-2, ExpressionTrees.evaluate(operation));

		TreeNode tree = ExpressionTrees.createTree("3-5");
		assertEquals("Problem when performing operation by using createTree()", -2, ExpressionTrees.evaluate(tree));
	}

	@Test
	public void testSimpleSubtractionZero() throws UnboundIdentifierException, ParseException
	{
		IntNode first = new IntNode(0);
		IntNode second = new IntNode(0);
		MinusNode operation = new MinusNode(first, second);
		assertEquals(0, ExpressionTrees.evaluate(operation));

		TreeNode tree = ExpressionTrees.createTree("0 - 0");
		assertEquals("Problem when performing operation by using createTree()", 0, ExpressionTrees.evaluate(tree));
	}

	@Test
	public void testSimpleDivision() throws UnboundIdentifierException, ParseException
	{
		IntNode first = new IntNode(9);
		IntNode second = new IntNode(2);
		DivNode operation = new DivNode(first, second);
		assertEquals(4, ExpressionTrees.evaluate(operation));

		TreeNode tree = ExpressionTrees.createTree("9/2");
		assertEquals("Problem when performing operation by using createTree()", 4, ExpressionTrees.evaluate(tree));
	}

	@Test
	public void testNegationAddition() throws UnboundIdentifierException, ParseException
	{
		IntNode first = new IntNode(5);
		NegationNode second = new NegationNode(new IntNode(3));
		PlusNode operation = new PlusNode(first, second);
		assertEquals(2, ExpressionTrees.evaluate(operation));

		TreeNode tree = ExpressionTrees.createTree("5 + -3");
		assertEquals("Problem when performing operation by using createTree()", 2, ExpressionTrees.evaluate(tree));
	}

	@Test
	public void testNegationAdditionMoreComplex() throws UnboundIdentifierException, ParseException
	{
		IntNode first = new IntNode(2);
		NegationNode second = new NegationNode(new MinusNode(new IntNode(3), new IntNode(2)));
		PlusNode operation = new PlusNode(first, second);
		assertEquals(1, ExpressionTrees.evaluate(operation));

		TreeNode tree = ExpressionTrees.createTree("2+-(3-2)");
		assertEquals("Problem when performing operation by using createTree()", 1, ExpressionTrees.evaluate(tree));
	}

	@Test
	public void testNegationAdditionMultipleNegations() throws UnboundIdentifierException, ParseException
	{
		IntNode first = new IntNode(6);
		NegationNode second = new NegationNode(new NegationNode(new IntNode(4)));
		PlusNode operation = new PlusNode(first, second);
		assertEquals(10, ExpressionTrees.evaluate(operation));

		TreeNode tree = ExpressionTrees.createTree("6 + --4");
		assertEquals("Problem when performing operation by using createTree()", 10, ExpressionTrees.evaluate(tree));
	}

	@Test
	public void testNegationSubtraction() throws UnboundIdentifierException, ParseException
	{
		IntNode first = new IntNode(3);
		NegationNode second = new NegationNode(new IntNode(4));
		MinusNode operation = new MinusNode(first, second);
		assertEquals(7, ExpressionTrees.evaluate(operation));

		TreeNode tree = ExpressionTrees.createTree("3 - -4");
		assertEquals("Problem when performing operation by using createTree()", 7, ExpressionTrees.evaluate(tree));
	}

	@Test
	public void testParenthesis() throws UnboundIdentifierException, ParseException
	{
		IntNode first = new IntNode(8);
		PlusNode second = new PlusNode(new IntNode(2), new IntNode(1));
		TimesNode operation = new TimesNode(first, second);
		assertEquals(24, ExpressionTrees.evaluate(operation));

		TreeNode tree = ExpressionTrees.createTree("8 * ( 2 + 1 )");
		assertEquals("Problem when performing operation by using createTree()", 24, ExpressionTrees.evaluate(tree));
	}

	@Test
	public void testParenthesis1() throws UnboundIdentifierException, ParseException
	{
		PlusNode first = new PlusNode(new IntNode(3), new IntNode(2));
		TimesNode operation = new TimesNode(new IntNode(6), new IntNode(2));
		MinusNode second = new MinusNode(first, operation);
		assertEquals(-7, ExpressionTrees.evaluate(second));

		TreeNode tree = ExpressionTrees.createTree("(3+2) - (6*2)");
		assertEquals("Problem when performing operation by using createTree()", -7, ExpressionTrees.evaluate(tree));
	}

	@Test
	public void testParenthesis2() throws UnboundIdentifierException, ParseException
	{
		DivNode first = new DivNode(new IntNode(4), new IntNode(3));
		DivNode operation = new DivNode(new IntNode(3), new IntNode(4));
		TimesNode second = new TimesNode(first, operation);
		assertEquals(0, ExpressionTrees.evaluate(second));

		TreeNode tree = ExpressionTrees.createTree("(4/3)*(3/4)");
		assertEquals("Problem when performing operation by using createTree()", 0, ExpressionTrees.evaluate(tree));
	}

	@Test
	public void testParenthesis3() throws UnboundIdentifierException, ParseException
	{
		ExpNode first = new ExpNode(new IntNode(2), new IntNode(3));
		ExpNode second = new ExpNode(new IntNode(3), new IntNode(2));
		DivNode operation = new DivNode(first, second);
		assertEquals(0, ExpressionTrees.evaluate(operation));

		TreeNode tree = ExpressionTrees.createTree("(2^3)/(3^2)");
		assertEquals("Problem when performing operation by using createTree()", 0, ExpressionTrees.evaluate(tree));
	}

	@Test
	public void testParenthesis4() throws UnboundIdentifierException, ParseException
	{
		ExpNode first = new ExpNode(new IntNode(3), new IntNode(2));
		ExpNode second = new ExpNode(new IntNode(2), new IntNode(3));
		DivNode operation = new DivNode(first, second);
		assertEquals(1, ExpressionTrees.evaluate(operation));

		TreeNode tree = ExpressionTrees.createTree("(3^2)/(2^3)");
		assertEquals("Problem when performing operation by using createTree()", 1, ExpressionTrees.evaluate(tree));
	}

	@Test
	public void testParenthesis5() throws UnboundIdentifierException, ParseException
	{
		TimesNode operation = new TimesNode(new IntNode(2), new IntNode(3));
		assertEquals(6, ExpressionTrees.evaluate(operation));

		TreeNode tree = ExpressionTrees.createTree("3 * (2)");
		assertEquals("Problem when performing operation by using createTree()", 6, ExpressionTrees.evaluate(tree));
	}

	@Test
	public void testParenthesis6() throws UnboundIdentifierException, ParseException
	{
		TimesNode operation = new TimesNode(new IntNode(4), new IntNode(-2));
		assertEquals(-8, ExpressionTrees.evaluate(operation));

		TreeNode tree = ExpressionTrees.createTree("4 * (-2)");
		assertEquals("Problem when performing operation by using createTree()", -8, ExpressionTrees.evaluate(tree));
	}

	@Test
	public void testExponents() throws UnboundIdentifierException, ParseException
	{
		ExpNode operation = new ExpNode(new IntNode(3), new IntNode(2));
		assertEquals(9, ExpressionTrees.evaluate(operation));

		TreeNode tree = ExpressionTrees.createTree("3^2");
		assertEquals("Problem when performing operation by using createTree()", 9, ExpressionTrees.evaluate(tree));
	}

	@Test
	public void testExponents2() throws UnboundIdentifierException, ParseException
	{
		ExpNode operation = new ExpNode(new IntNode(2), new ExpNode(new IntNode(3), new IntNode(2)));
		assertEquals(512, ExpressionTrees.evaluate(operation));

		TreeNode tree = ExpressionTrees.createTree("2^3^2");
		assertEquals("Problem when performing operation by using createTree()", 512, ExpressionTrees.evaluate(tree));
	}

	@Test
	public void testExponents3() throws UnboundIdentifierException, ParseException
	{
		TreeNode tree = ExpressionTrees.createTree("2^(3^2)");
		assertEquals("Problem when performing operation by using createTree()", 512, ExpressionTrees.evaluate(tree));
	}

	@Test
	public void testExponents4() throws UnboundIdentifierException, ParseException
	{
		TreeNode tree = ExpressionTrees.createTree("(2^3)^2");
		assertEquals("Problem when performing operation by using createTree()", 64, ExpressionTrees.evaluate(tree));
	}

	//	@Test
	//	public void testExponentsWithNegative() throws UnboundIdentifierException, ParseException
	//	{
	//		ExpNode operation = new ExpNode(new IntNode(3), new IntNode(-2));
	//		assertEquals(0, ExpressionTrees.evaluate(operation));
	//		
	//		TreeNode tree = ExpressionTrees.createTree("3^-2");
	//		assertEquals("Problem when performing operation by using createTree()", 0, ExpressionTrees.evaluate(tree));
	//	}

	@Test
	public void testExponentsWithNegative2() throws UnboundIdentifierException, ParseException
	{
		ExpNode operation = new ExpNode(new IntNode(-4), new IntNode(1));
		assertEquals(-4, ExpressionTrees.evaluate(operation));

		TreeNode tree = ExpressionTrees.createTree("-4^1");
		assertEquals("Problem when performing operation by using createTree()", -4, ExpressionTrees.evaluate(tree));
	}

	@Test
	public void testExponentsSpecialCase() throws UnboundIdentifierException, ParseException
	{
		ExpNode operation = new ExpNode(new IntNode(2), new IntNode(0));
		assertEquals(1, ExpressionTrees.evaluate(operation));

		TreeNode tree = ExpressionTrees.createTree("2^0");
		assertEquals("Problem when performing operation by using createTree()", 1, ExpressionTrees.evaluate(tree));
	}

	@Test
	public void testExponentsSpecialCase2() throws UnboundIdentifierException, ParseException
	{
		ExpNode operation = new ExpNode(new IntNode(0), new IntNode(7));
		assertEquals(0, ExpressionTrees.evaluate(operation));

		TreeNode tree = ExpressionTrees.createTree("0^7");
		assertEquals("Problem when performing operation by using createTree()", 0, ExpressionTrees.evaluate(tree));
	}

	@Test
	public void testEvaluateFigure1() throws ParseException, UnboundIdentifierException
	{
		TreeNode root = ExpressionTrees.createTree("(3 + x)^y - (z * 2)");
		assertEquals(10, ExpressionTrees.evaluate(root, 1, 2, 3));
	}

	@Test
	public void testEvaluateFigure2() throws ParseException, UnboundIdentifierException
	{
		TreeNode root = ExpressionTrees.createTree("--(5^2 / x) * (2 / y)^z");
		assertEquals(5, ExpressionTrees.evaluate(root, 5, 2, 2));
	}

	@Test
	public void testEvaluateFigure3() throws ParseException, UnboundIdentifierException
	{
		TreeNode root = ExpressionTrees.createTree("(x^y - 1) * 2 + 1");
		assertEquals(31, ExpressionTrees.evaluate(root, 2, 4, 1, 5, 6));
	}

	@Test(expected = UnboundIdentifierException.class)
	public void testEvaluateFigure4() throws ParseException, UnboundIdentifierException
	{
		TreeNode root = ExpressionTrees.createTree("(x^y - 1) * 2 + 1");
			ExpressionTrees.evaluate(root, 1);
	}

	@Test
	public void testEvaluateFigure5() throws ParseException, UnboundIdentifierException
	{
		TreeNode root = ExpressionTrees.createTree("-x^0 + -6 / -(2 + 1)");
		assertEquals(1, ExpressionTrees.evaluate(root, 4));
	}
}
