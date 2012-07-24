package edu.iastate.cs228.hw4.test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import edu.iastate.cs228.hw4.ExpressionTrees;
import edu.iastate.cs228.hw4.ParseException;
import edu.iastate.cs228.hw4.UnboundIdentifierException;
import edu.iastate.cs228.hw4.nodes.ExpNode;
import edu.iastate.cs228.hw4.nodes.IDNode;
import edu.iastate.cs228.hw4.nodes.IntNode;
import edu.iastate.cs228.hw4.nodes.NegationNode;
import edu.iastate.cs228.hw4.nodes.PlusNode;
import edu.iastate.cs228.hw4.nodes.TimesNode;
import edu.iastate.cs228.hw4.nodes.TreeNode;

public class ExpressionTreesTest2
{
	static TreeNode fig1;
	static TreeNode fig2;
	static TreeNode fig3;
	static TreeNode fig4;
	static TreeNode fig5;
	static TreeNode fig6;
	static TreeNode fig7;
	static TreeNode varsonly;
	static TreeNode reduce1;
	static TreeNode leftAssoExp;
	static TreeNode reduce2;
	static TreeNode dupvars;

	@Before
	public void setUp() throws Exception
	{
		fig1 = ExpressionTrees.createTree("(2+3)*4");
		fig2 = ExpressionTrees.createTree("2 + 3 * 4");
		fig3 = ExpressionTrees.createTree("2 - 3 - 4");
		fig4 = ExpressionTrees.createTree("(x + 13) * y - 42 * 17");
		fig5 = ExpressionTrees.createTree("-2 + 3 * -(4 * (5 - 6))");
		fig6 = ExpressionTrees.createTree("2 + - (3 - 4)");
		fig7 = ExpressionTrees.createTree("2 ^ 3 ^ 2");
		varsonly = ExpressionTrees.createTree("a + b * -(c *(d - e))");
		dupvars = ExpressionTrees.createTree("x + 3 * y + x ^ 2 - x * y");
		reduce1 = ExpressionTrees.createTree("(2*3+x)*(y- 2^3)");
		leftAssoExp = ExpressionTrees.createTree("(2 ^ 3) ^ 4");
		setUpReduce2();
	}

	/**
	 * Initializes the Tree reduce2
	 */
	private static void setUpReduce2()
	{
		TreeNode l1 = new IDNode("x");
		TreeNode l2 = new IntNode(2);
		TreeNode l3 = new IntNode(3);
		TreeNode l4 = new IntNode(3);
		TreeNode l5 = new IntNode(2);
		TreeNode l6 = new IDNode("y");
		TreeNode l7 = new IntNode(3);
		TreeNode l8 = new IntNode(1);
		TreeNode l9 = new IntNode(1);

		TreeNode sub1 = new PlusNode(l2, l3);
		TreeNode sub2 = new NegationNode(sub1);
		TreeNode sub3 = new TimesNode(l1, sub2);
		TreeNode sub4 = new TimesNode(l4, l5);
		TreeNode sub5 = new NegationNode(sub4);
		TreeNode sub6 = new PlusNode(sub3, sub5);
		TreeNode sub7 = new PlusNode(l8, l9);
		TreeNode sub8 = new ExpNode(l7, sub7);
		TreeNode sub9 = new ExpNode(l6, sub8);

		reduce2 = new TimesNode(sub6, sub9);
	}

	@Test
	public void testGetPostfixString()
	{
		assertEquals(ExpressionTrees.getPostfixString(fig1), "2 3 + 4 *");
		assertEquals(ExpressionTrees.getPostfixString(fig3), "2 3 - 4 -");
		assertEquals(ExpressionTrees.getPostfixString(fig4), "x 13 + y * 42 17 * -");
		assertEquals(ExpressionTrees.getPostfixString(fig5), "2 NEG 3 4 5 6 - * NEG * +");
		assertEquals(ExpressionTrees.getPostfixString(varsonly), "a b c d e - * NEG * +");
	}

	@Test
	public void testGetInfixString()
	{
		assertEquals(ExpressionTrees.getInfixString(fig1), "(2 + 3) * 4");
		assertEquals(ExpressionTrees.getInfixString(fig2), "2 + 3 * 4");
		assertEquals(ExpressionTrees.getInfixString(fig3), "2 - 3 - 4");
		assertEquals(ExpressionTrees.getInfixString(fig4), "(x + 13) * y - 42 * 17");
		assertEquals(ExpressionTrees.getInfixString(fig5), "-2 + 3 * -(4 * (5 - 6))");
		assertEquals(ExpressionTrees.getInfixString(fig6), "2 + -(3 - 4)");
		assertEquals(ExpressionTrees.getInfixString(fig7), "2 ^ 3 ^ 2");
		assertEquals(ExpressionTrees.getInfixString(varsonly), "a + b * -(c * (d - e))");
		assertEquals(ExpressionTrees.getInfixString(leftAssoExp), "(2 ^ 3) ^ 4");
	}

	@Test
	/**
	 * Note this is dependent on a successful getPostfixString
	 */
	public void testReduceConstants()
	{
		TreeNode reduced1 = ExpressionTrees.reduceConstants(reduce1);
		TreeNode reduced2 = ExpressionTrees.reduceConstants(reduce2);
		assertEquals(ExpressionTrees.getPostfixString(reduced1), "6 x + y 8 - *");
		assertEquals(ExpressionTrees.getPostfixString(reduced2), "x -5 * -6 + y 9 ^ *");
	}

	@Test
	public void testEvaluate() throws UnboundIdentifierException
	{
		assertEquals(ExpressionTrees.evaluate(fig1, 0), 20);
		assertEquals(ExpressionTrees.evaluate(fig2, 0), 14);
		assertEquals(ExpressionTrees.evaluate(fig3, 0), -5);
		assertEquals(ExpressionTrees.evaluate(fig4, 0, 1), -701);
		assertEquals(ExpressionTrees.evaluate(fig5, 0), 10);
		assertEquals(ExpressionTrees.evaluate(fig6, 0), 3);
		assertEquals(ExpressionTrees.evaluate(fig7, 0), 512);
		assertEquals(ExpressionTrees.evaluate(varsonly, 0, 1, 2, 3, 4), 2);
		assertEquals(ExpressionTrees.evaluate(dupvars, 2, 3), 9);
	}

	@Test(expected = ParseException.class)
	public void testParseErrors() throws ParseException
	{
		ExpressionTrees.createTree("(2+3*4");
	}

	@Test(expected = ParseException.class)
	public void testParseErrors1() throws ParseException
	{
		ExpressionTrees.createTree("2+3)*4");
	}

	@Test(expected = ParseException.class)
	public void testParseErrors2() throws ParseException
	{
		ExpressionTrees.createTree("(+2+3)*4");
	}

	@Test(expected = ParseException.class)
	public void testParseErrors3() throws ParseException
	{
		ExpressionTrees.createTree("3 3");
	}

	@Test(expected = ParseException.class)
	public void testParseErrors4() throws ParseException
	{
		ExpressionTrees.createTree("* +");
	}

	@Test(expected = ParseException.class)
	public void testParseErrors5() throws ParseException
	{
		ExpressionTrees.createTree("foo +");
	}

	@Test(expected = ParseException.class)
	public void testParseErrors6() throws ParseException
	{
		ExpressionTrees.createTree("((2)())");
	}

	@Test(expected = ParseException.class)
	public void testParseErrors7() throws ParseException
	{
		ExpressionTrees.createTree("(()())");
	}

	@Test
	public void testParseErrors8() throws ParseException
	{
		ExpressionTrees.createTree("(2)");
	}

	@Test(expected = ParseException.class)
	public void testParseErrors9() throws ParseException
	{
		ExpressionTrees.createTree("(())");
	}

	@Test
	public void testParseErrors10() throws ParseException
	{
		ExpressionTrees.createTree("3");
	}

	@Test
	public void testEvaluateErrors() throws UnboundIdentifierException
	{
		ExpressionTrees.evaluate(varsonly, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
	}
	@Test
	public void testEvaluateErrors1() throws UnboundIdentifierException
	{
		ExpressionTrees.evaluate(dupvars, 0, 1);
	}

	@Test(expected = UnboundIdentifierException.class)
	public void testEvaluateErrors2() throws UnboundIdentifierException
	{
		ExpressionTrees.evaluate(varsonly, 3, 2);
	}

	@Test
	public void testEvaluateErrors3() throws UnboundIdentifierException
	{
		ExpressionTrees.evaluate(fig1, (Integer) null);
	}

	@Test
	public void testEvaluateErrors4() throws UnboundIdentifierException
	{
		ExpressionTrees.evaluate(fig1, 2, 3, 4);
	}

	@Test
	public void additionalTest() throws ParseException
	{
		TreeNode fig8 = null;
		fig8 = ExpressionTrees.createTree("1 + (2 - 3)");

		assertEquals(ExpressionTrees.getInfixString(fig8), "1 + (2 - 3)");
	}
}
