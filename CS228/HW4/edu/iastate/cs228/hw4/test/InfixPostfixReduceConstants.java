package edu.iastate.cs228.hw4.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import edu.iastate.cs228.hw4.ExpressionTrees;
import edu.iastate.cs228.hw4.ParseException;
import edu.iastate.cs228.hw4.nodes.DivNode;
import edu.iastate.cs228.hw4.nodes.ExpNode;
import edu.iastate.cs228.hw4.nodes.IDNode;
import edu.iastate.cs228.hw4.nodes.IntNode;
import edu.iastate.cs228.hw4.nodes.MinusNode;
import edu.iastate.cs228.hw4.nodes.NegationNode;
import edu.iastate.cs228.hw4.nodes.PlusNode;
import edu.iastate.cs228.hw4.nodes.TimesNode;
import edu.iastate.cs228.hw4.nodes.TreeNode;

/**
 *
 * @author Curtis Ullerich
 */
public class InfixPostfixReduceConstants
{
	@Test
	public void testGetInfixStringNegation() throws ParseException
	{
		TreeNode root = ExpressionTrees.createTree("-(-3 + 4)");
		assertEquals("-(-3 + 4)", ExpressionTrees.getInfixString(root));
	}
	
	@Test
	public void testGetInfixStringFigure1()
	{
		IntNode two = new IntNode(2);
		IntNode three = new IntNode(3);
		IntNode four = new IntNode(4);
		PlusNode plus = new PlusNode(two, three);
		TimesNode root = new TimesNode(plus, four);
		assertEquals("(2 + 3) * 4", ExpressionTrees.getInfixString(root));
	}

	@Test
	public void testGetInfixStringFigure2()
	{
		IntNode two = new IntNode(2);
		IntNode three = new IntNode(3);
		IntNode four = new IntNode(4);
		TimesNode times = new TimesNode(three, four);
		PlusNode root = new PlusNode(two, times);
		assertEquals("2 + 3 * 4", ExpressionTrees.getInfixString(root));
	}

	@Test
	public void testGetInfixStringFigure3()
	{
		IntNode two = new IntNode(2);
		IntNode three = new IntNode(3);
		IntNode four = new IntNode(4);
		MinusNode minus = new MinusNode(two, three);
		MinusNode root = new MinusNode(minus, four);
		assertEquals("2 - 3 - 4", ExpressionTrees.getInfixString(root));
	}

	@Test
	public void testGetInfixStringFigure4()
	{
		IntNode thirteen = new IntNode(13);
		IntNode fortytwo = new IntNode(42);
		IntNode seventeen = new IntNode(17);
		IDNode x = new IDNode("x");
		PlusNode plus = new PlusNode(x, thirteen);
		IDNode y = new IDNode("y");
		TimesNode times = new TimesNode(plus, y);
		TimesNode times2 = new TimesNode(fortytwo, seventeen);
		MinusNode root = new MinusNode(times, times2);
		assertEquals("(x + 13) * y - 42 * 17", ExpressionTrees.getInfixString(root));
	}

	@Test
	public void testGetInfixStringFigure5() throws ParseException
	{
		IntNode two = new IntNode(2);
		NegationNode neg = new NegationNode(two);
		IDNode b = new IDNode("b");
		IntNode five = new IntNode(5);
		ExpNode exp = new ExpNode(b, five);
		NegationNode neg2 = new NegationNode(exp);
		TimesNode times = new TimesNode(neg, neg2);
		IntNode four = new IntNode(4);
		NegationNode neg3 = new NegationNode(four);
		NegationNode neg4 = new NegationNode(neg3);
		PlusNode plus = new PlusNode(times, neg4);
		IDNode c = new IDNode("c");
		DivNode div = new DivNode(plus, c);
		NegationNode root = new NegationNode(div);
		assertEquals("-((-2 * -b ^ 5 + --4) / c)", ExpressionTrees.getInfixString(root));
	}

	@Test
	public void testGetPostfixStringFigure1()
	{
		IntNode two = new IntNode(2);
		IntNode three = new IntNode(3);
		IntNode four = new IntNode(4);
		PlusNode plus = new PlusNode(two, three);
		TimesNode root = new TimesNode(plus, four);
		assertEquals("2 3 + 4 *", ExpressionTrees.getPostfixString(root));
	}

	@Test
	public void testGetPostfixStringFigure2()
	{
		IntNode two = new IntNode(2);
		IntNode three = new IntNode(3);
		IntNode four = new IntNode(4);
		TimesNode times = new TimesNode(three, four);
		PlusNode root = new PlusNode(two, times);
		assertEquals("2 3 4 * +", ExpressionTrees.getPostfixString(root));
	}

	@Test
	public void testGetPostfixStringFigure3()
	{
		IntNode two = new IntNode(2);
		IntNode three = new IntNode(3);
		IntNode four = new IntNode(4);
		MinusNode minus = new MinusNode(two, three);
		MinusNode root = new MinusNode(minus, four);
		assertEquals("2 3 - 4 -", ExpressionTrees.getPostfixString(root));
	}

	@Test
	public void testGetPostfixStringFigure4()
	{
		IntNode thirteen = new IntNode(13);
		IntNode fortytwo = new IntNode(42);
		IntNode seventeen = new IntNode(17);
		IDNode x = new IDNode("x");
		PlusNode plus = new PlusNode(x, thirteen);
		IDNode y = new IDNode("y");
		TimesNode times = new TimesNode(plus, y);
		TimesNode times2 = new TimesNode(fortytwo, seventeen);
		MinusNode root = new MinusNode(times, times2);
		assertEquals("x 13 + y * 42 17 * -", ExpressionTrees.getPostfixString(root));
	}

	@Test
	public void testGetPostfixStringFigure5()
	{
		IntNode two = new IntNode(2);
		NegationNode neg = new NegationNode(two);
		IDNode b = new IDNode("b");
		IntNode five = new IntNode(5);
		ExpNode exp = new ExpNode(b, five);
		NegationNode neg2 = new NegationNode(exp);
		TimesNode times = new TimesNode(neg, neg2);
		IntNode four = new IntNode(4);
		NegationNode neg3 = new NegationNode(four);
		NegationNode neg4 = new NegationNode(neg3);
		PlusNode plus = new PlusNode(times, neg4);
		IDNode c = new IDNode("c");
		DivNode div = new DivNode(plus, c);
		NegationNode root = new NegationNode(div);
		assertEquals("2 NEG b 5 ^ NEG * 4 NEG NEG + c / NEG", ExpressionTrees.getPostfixString(root));
	}

	@Test
	public void testReduceConstantsFigure1()
	{
		IntNode two = new IntNode(2);
		IntNode three = new IntNode(3);
		IntNode four = new IntNode(4);
		PlusNode plus = new PlusNode(two, three);
		TimesNode root = new TimesNode(plus, four);
		IntNode expected = new IntNode(20);
		TreeNode actual = ExpressionTrees.reduceConstants(root);
		assertEquals(expected.type(), actual.type());
		assertEquals(expected.toString(), actual.toString());
		assertEquals("Failed on infix expression...", ExpressionTrees.getInfixString(expected), ExpressionTrees.getInfixString(actual));
	}

	@Test
	public void testReduceConstantsFigure2()
	{
		IntNode two = new IntNode(2);
		IntNode three = new IntNode(3);
		IntNode four = new IntNode(4);
		TimesNode times = new TimesNode(three, four);
		PlusNode root = new PlusNode(two, times);
		IntNode expected = new IntNode(14);
		TreeNode actual = ExpressionTrees.reduceConstants(root);
		assertEquals(expected.type(), actual.type());
		assertEquals(expected.toString(), actual.toString());
		assertEquals("Failed on infix expression...", ExpressionTrees.getInfixString(expected), ExpressionTrees.getInfixString(actual));
	}

	@Test
	public void testReduceConstantsFigure3()
	{
		IntNode two = new IntNode(2);
		IntNode three = new IntNode(3);
		IntNode four = new IntNode(4);
		MinusNode minus = new MinusNode(two, three);
		MinusNode root = new MinusNode(minus, four);
		IntNode expected = new IntNode(-5);
		TreeNode actual = ExpressionTrees.reduceConstants(root);
		assertEquals(expected.type(), actual.type());
		assertEquals(expected.toString(), actual.toString());
		assertEquals("Failed on infix expression...", ExpressionTrees.getInfixString(expected), ExpressionTrees.getInfixString(actual));
	}

	@Test
	public void testReduceConstantsFigure4()
	{
		IntNode thirteen = new IntNode(13);
		IntNode fortytwo = new IntNode(42);
		IntNode seventeen = new IntNode(17);
		IDNode x = new IDNode("x");
		PlusNode plus = new PlusNode(x, thirteen);
		IDNode y = new IDNode("y");
		TimesNode times = new TimesNode(plus, y);
		TimesNode times2 = new TimesNode(fortytwo, seventeen);
		MinusNode root = new MinusNode(times, times2);
		MinusNode expected =
			new MinusNode(new TimesNode(new PlusNode(new IDNode("x"), new IntNode(13)),	new IDNode("y")), new IntNode(714));

		TreeNode actual = ExpressionTrees.reduceConstants(root);
		assertEquals(expected.type(), actual.type());
		assertEquals(expected.toString(), actual.toString());
		assertEquals("Failed on infix expression...", ExpressionTrees.getInfixString(expected), ExpressionTrees.getInfixString(actual));
	}

	@Test //this tests a negation node that is below an ID node in depth
	public void testReduceConstantsFigure4modified() {
		IntNode thirteen = new IntNode(13);
		IntNode fortytwo = new IntNode(42);
		IntNode seventeen = new IntNode(17);
		NegationNode x = new NegationNode(new IntNode(7));

		PlusNode plus = new PlusNode(x, thirteen);
		IDNode y = new IDNode("y");
		TimesNode times = new TimesNode(plus, y);
		TimesNode times2 = new TimesNode(fortytwo, seventeen);
		MinusNode root = new MinusNode(times, times2);

		MinusNode expected =
			new MinusNode(
					new TimesNode(
							new IntNode(6),
							new IDNode("y")),
							new IntNode(714));
		assertEquals(ExpressionTrees.getInfixString(expected), ExpressionTrees.getInfixString(ExpressionTrees.reduceConstants(root)));
	}
}