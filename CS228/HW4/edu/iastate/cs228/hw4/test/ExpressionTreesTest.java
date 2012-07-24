package edu.iastate.cs228.hw4.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import edu.iastate.cs228.hw4.ExpressionTrees;
import edu.iastate.cs228.hw4.ParseException;
import edu.iastate.cs228.hw4.UnboundIdentifierException;
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
 * @author Curtis Ullerich, Tyler Treat
 */
public class ExpressionTreesTest {
	
	@Test
	public void testGetInfixStringOther() throws ParseException {
		assertEquals("2 ^ (-(3 - 5))", ExpressionTrees.getInfixString(ExpressionTrees.createTree("2 ^ -(3 - 5)")));
	}
	
	@Test
	public void testGetInfixStringClarification1() throws ParseException {
		assertEquals("2 - (3 - 4)", ExpressionTrees.getInfixString(ExpressionTrees.createTree("2 - (3 - 4)")));
	}
	
	@Test
	public void testGetInfixStringClarification2() throws ParseException {
		assertEquals("2 + (3 - 4)", ExpressionTrees.getInfixString(ExpressionTrees.createTree("2 + (3 - 4)")));
	}
	
	@Test
	public void testGetInfixStringClarification3() throws ParseException {
		assertEquals("2 - 3 - 4", ExpressionTrees.getInfixString(ExpressionTrees.createTree("(2 - 3) - 4")));
	}
	
	@Test
	public void testGetInfixStringClarification4() throws ParseException {
		assertEquals("2 ^ 3 ^ 4", ExpressionTrees.getInfixString(ExpressionTrees.createTree("2^(3^4)")));
	}
	
	@Test
	public void testGetInfixStringClarification5() throws ParseException {
		assertEquals("(2 ^ 3) ^ 4", ExpressionTrees.getInfixString(ExpressionTrees.createTree("(2^3)^4")));
	}
	
	@Test
	public void testGetInfixStringClarification6() throws ParseException {
		assertEquals("-2 ^ 3", ExpressionTrees.getInfixString(ExpressionTrees.createTree("-2^3")));
	}
	
	@Test
	public void testGetInfixStringClarification7() throws ParseException {
		assertEquals("-2 ^ 3", ExpressionTrees.getInfixString(ExpressionTrees.createTree("-(2^3)")));
	}
	
	@Test
	public void testGetInfixStringClarification8() throws ParseException {
		assertEquals("(-2) ^ 3", ExpressionTrees.getInfixString(ExpressionTrees.createTree("(-2)^3")));
	}
	
	@Test
	public void testGetInfixStringClarification9() throws ParseException {
		assertEquals("2 ^ (-3)", ExpressionTrees.getInfixString(ExpressionTrees.createTree("2^-3")));
	}
	
	@Test
	public void testGetInfixStringClarification10() throws ParseException {
		assertEquals("---42", ExpressionTrees.getInfixString(ExpressionTrees.createTree("-(-(-42))")));
	}
	
	@Test
	public void testGetInfixStringFigure1() {
		IntNode two = new IntNode(2);
		IntNode three = new IntNode(3);
		IntNode four = new IntNode(4);
		PlusNode plus = new PlusNode(two, three);
		TimesNode root = new TimesNode(plus, four);

		assertEquals("(2 + 3) * 4", ExpressionTrees.getInfixString(root));
	}

	@Test
	public void testGetInfixStringFigure2() {
		IntNode two = new IntNode(2);
		IntNode three = new IntNode(3);
		IntNode four = new IntNode(4);
		TimesNode times = new TimesNode(three, four);
		PlusNode root = new PlusNode(two, times);
		assertEquals("2 + 3 * 4", ExpressionTrees.getInfixString(root));
	}

	@Test
	public void testGetInfixStringFigure3() {
		IntNode two = new IntNode(2);
		IntNode three = new IntNode(3);
		IntNode four = new IntNode(4);
		MinusNode minus = new MinusNode(two, three);
		MinusNode root = new MinusNode(minus, four);
		assertEquals("2 - 3 - 4", ExpressionTrees.getInfixString(root));
	}

	@Test //this is an exception Steve mentioned in a correction post
	public void testGetInfixStringException() {
		assertEquals("2 ^ (-x)", ExpressionTrees.getInfixString(
				new ExpNode(new IntNode(2), new NegationNode(new IDNode("x")))));
	}

	@Test //right-associativity of exponents
	public void testGetInfixStringException2() {
		ExpNode exp = new ExpNode(new ExpNode(new IntNode(2),
				new IntNode(3)), new IntNode(2));
		assertEquals("(2 ^ 3) ^ 2", ExpressionTrees.getInfixString(exp));
	}

	@Test //right-associativity of subtraction case
	public void testGetInfixStringException3() {
		assertEquals("2 + (3 - 4)", ExpressionTrees.getInfixString(new PlusNode(
				new IntNode(2), new MinusNode(new IntNode(3), new IntNode(4)))));
	}

	@Test
	public void testGetInfixStringFigure4() {
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
	public void testGetInfixStringFigure5() {
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
	public void testGetPostfixStringFigure1() {
		IntNode two = new IntNode(2);
		IntNode three = new IntNode(3);
		IntNode four = new IntNode(4);
		PlusNode plus = new PlusNode(two, three);
		TimesNode root = new TimesNode(plus, four);
		assertEquals("2 3 + 4 *", ExpressionTrees.getPostfixString(root));
	}

	@Test
	public void testGetPostfixStringFigure2() {
		IntNode two = new IntNode(2);
		IntNode three = new IntNode(3);
		IntNode four = new IntNode(4);
		TimesNode times = new TimesNode(three, four);
		PlusNode root = new PlusNode(two, times);
		assertEquals("2 3 4 * +", ExpressionTrees.getPostfixString(root));
	}

	@Test
	public void testGetPostfixStringFigure3() {
		IntNode two = new IntNode(2);
		IntNode three = new IntNode(3);
		IntNode four = new IntNode(4);
		MinusNode minus = new MinusNode(two, three);
		MinusNode root = new MinusNode(minus, four);
		assertEquals("2 3 - 4 -", ExpressionTrees.getPostfixString(root));
	}

	@Test
	public void testGetPostfixStringFigure4() {
		IntNode fortytwo = new IntNode(42);
		IntNode seventeen = new IntNode(17);
		IDNode x = new IDNode("x");
		IDNode y = new IDNode("y");
		TimesNode times = new TimesNode(x, y);
		TimesNode times2 = new TimesNode(fortytwo, seventeen);
		MinusNode root = new MinusNode(times, times2);
		assertEquals("x y * 42 17 * -", ExpressionTrees.getPostfixString(root));
	}

	@Test
	public void testGetPostfixStringFigure5() {
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
	public void testReduceConstantsFigure1() {
		IntNode two = new IntNode(2);
		IntNode three = new IntNode(3);
		IntNode four = new IntNode(4);
		PlusNode plus = new PlusNode(two, three);
		TimesNode root = new TimesNode(plus, four);
		IntNode expected = new IntNode(20);
		assertEquals(ExpressionTrees.getInfixString(expected), ExpressionTrees.getInfixString(ExpressionTrees.reduceConstants(root)));
	}

	@Test
	public void testReduceConstantsFigure2() {
		IntNode two = new IntNode(2);
		IntNode three = new IntNode(3);
		IntNode four = new IntNode(4);
		TimesNode times = new TimesNode(three, four);
		PlusNode root = new PlusNode(two, times);
		IntNode expected = new IntNode(14);
		assertEquals(ExpressionTrees.getInfixString(expected), ExpressionTrees.getInfixString(ExpressionTrees.reduceConstants(root)));

	}

	@Test
	public void testReduceConstantsFigure3() {
		IntNode two = new IntNode(2);
		IntNode three = new IntNode(3);
		IntNode four = new IntNode(4);
		MinusNode minus = new MinusNode(two, three);
		MinusNode root = new MinusNode(minus, four);
		IntNode expected = new IntNode(-5);
		assertEquals(ExpressionTrees.getInfixString(expected), ExpressionTrees.getInfixString(ExpressionTrees.reduceConstants(root)));
	}

	@Test
	public void testReduceConstantsFigure4() {
		IntNode thirteen = new IntNode(13);
		IntNode fortytwo = new IntNode(42);
		IntNode seventeen = new IntNode(17);
		IDNode x = new IDNode("x");
		PlusNode plus = new PlusNode(x, thirteen);
		IDNode y = new IDNode("y");
		TimesNode times = new TimesNode(plus, y);
		TimesNode times2 = new TimesNode(fortytwo, seventeen);
		MinusNode root = new MinusNode(times, times2);
		MinusNode expected = new MinusNode(new TimesNode(new PlusNode(new IDNode("x"), new IntNode(13)), new IDNode("y")), new IntNode(714));

		assertEquals(ExpressionTrees.getInfixString(expected), ExpressionTrees.getInfixString(ExpressionTrees.reduceConstants(root)));
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

		MinusNode expected = new MinusNode(new TimesNode(new IntNode(6), new IDNode("y")),	new IntNode(714));
		assertEquals(ExpressionTrees.getInfixString(expected), ExpressionTrees.getInfixString(ExpressionTrees.reduceConstants(root)));
	}

	@Test
	public void testReduceConstantsExp() {
		PlusNode plus = new PlusNode(new ExpNode(new IntNode(2), new IntNode(3)), new IntNode(4));
		IntNode expected = new IntNode(12);
		assertEquals(ExpressionTrees.getInfixString(expected), ExpressionTrees.getInfixString(ExpressionTrees.reduceConstants(plus)));
	}

	@Test
	public void testReduceConstantsExp2() {
		IntNode expected = new IntNode(64);

		ExpNode exp = new ExpNode(new ExpNode(new IntNode(2), new IntNode(3)), new IntNode(2));
		assertEquals(ExpressionTrees.getInfixString(expected), ExpressionTrees.getInfixString(ExpressionTrees.reduceConstants(exp)));
	}

	@Test
	public void testReduceConstantsExp3() {
		NegationNode expected = new NegationNode(new IntNode(64));
		ExpNode exp = new ExpNode(new ExpNode(new IntNode(2), new IntNode(3)), new IntNode(2));
		NegationNode root = new NegationNode(exp);
		assertEquals(ExpressionTrees.getInfixString(expected), ExpressionTrees.getInfixString(ExpressionTrees.reduceConstants(root)));
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

	@Test
	public void testEvaluateFigure6() throws ParseException, UnboundIdentifierException
	{
		TreeNode root = ExpressionTrees.createTree("2-----2");
		assertEquals(0, ExpressionTrees.evaluate(root));
	}

	@Test
	public void simpleTest1() throws ParseException, UnboundIdentifierException {
		String expr = new String("3+4");
		TreeNode root = ExpressionTrees.createTree(expr);
		assertEquals(7, ExpressionTrees.evaluate(root));
		assertEquals("3 + 4", ExpressionTrees.getInfixString(root));
		assertEquals("3 4 +", ExpressionTrees.getPostfixString(root));
		assertEquals("7", ExpressionTrees.getInfixString(ExpressionTrees.reduceConstants(root)));
	}

	@Test
	public void simpleTest2() throws ParseException, UnboundIdentifierException {
		String expr = new String("5*4");
		TreeNode root = ExpressionTrees.createTree(expr);
		assertEquals(20, ExpressionTrees.evaluate(root));
		assertEquals("5 * 4", ExpressionTrees.getInfixString(root));
		assertEquals("5 4 *", ExpressionTrees.getPostfixString(root));
		assertEquals("20", ExpressionTrees.getInfixString(ExpressionTrees.reduceConstants(root)));
	}

	@Test
	public void simpleTest4() throws ParseException, UnboundIdentifierException {
		String expr = new String("7-2");
		TreeNode root = ExpressionTrees.createTree(expr);
		assertEquals(5, ExpressionTrees.evaluate(root));
		assertEquals("7 - 2", ExpressionTrees.getInfixString(root));
		assertEquals("7 2 -", ExpressionTrees.getPostfixString(root));
		assertEquals("5", ExpressionTrees.getInfixString(ExpressionTrees.reduceConstants(root)));
	}

	@Test
	public void simpleTest5() throws ParseException, UnboundIdentifierException {
		String expr = new String("3-5");
		TreeNode root = ExpressionTrees.createTree(expr);
		assertEquals(-2, ExpressionTrees.evaluate(root));
		assertEquals("3 - 5", ExpressionTrees.getInfixString(root));
		assertEquals("3 5 -", ExpressionTrees.getPostfixString(root));
		assertEquals("-2", ExpressionTrees.getInfixString(ExpressionTrees.reduceConstants(root)));
	}

	@Test
	public void simpleTest6() throws ParseException, UnboundIdentifierException {
		String expr = new String("0-0");
		TreeNode root = ExpressionTrees.createTree(expr);
		assertEquals(0, ExpressionTrees.evaluate(root));
		assertEquals("0 - 0", ExpressionTrees.getInfixString(root));
		assertEquals("0 0 -", ExpressionTrees.getPostfixString(root));
		assertEquals("0", ExpressionTrees.getInfixString(ExpressionTrees.reduceConstants(root)));
	}

	@Test
	public void simpleTest7() throws ParseException, UnboundIdentifierException {
		String expr = new String("9/2");
		TreeNode root = ExpressionTrees.createTree(expr);
		assertEquals(4, ExpressionTrees.evaluate(root));
		assertEquals("9 / 2", ExpressionTrees.getInfixString(root));
		assertEquals("9 2 /", ExpressionTrees.getPostfixString(root));
		assertEquals("4", ExpressionTrees.getInfixString(ExpressionTrees.reduceConstants(root)));
	}

	@Test
	public void negTest1() throws ParseException, UnboundIdentifierException {
		String expr = new String("5+-3");
		TreeNode root = ExpressionTrees.createTree(expr);
		assertEquals(2, ExpressionTrees.evaluate(root));
		assertEquals("5 + -3", ExpressionTrees.getInfixString(root));
		assertEquals("5 3 NEG +", ExpressionTrees.getPostfixString(root));
		assertEquals("2", ExpressionTrees.getInfixString(ExpressionTrees.reduceConstants(root)));
	}

	@Test
	public void negTest2() throws ParseException, UnboundIdentifierException {
		String expr = new String("2+-(3-2)");
		TreeNode root = ExpressionTrees.createTree(expr);
		assertEquals(1, ExpressionTrees.evaluate(root));
		assertEquals("2 + -(3 - 2)", ExpressionTrees.getInfixString(root));
		assertEquals("2 3 2 - NEG +", ExpressionTrees.getPostfixString(root));
		assertEquals("1", ExpressionTrees.getInfixString(ExpressionTrees.reduceConstants(root)));
	}

	@Test
	public void negTest3() throws ParseException, UnboundIdentifierException {
		String expr = new String("6+--4");
		TreeNode root = ExpressionTrees.createTree(expr);
		assertEquals(10, ExpressionTrees.evaluate(root));
		assertEquals("6 + --4", ExpressionTrees.getInfixString(root));
		assertEquals("6 4 NEG NEG +", ExpressionTrees.getPostfixString(root));
		assertEquals("10", ExpressionTrees.getInfixString(ExpressionTrees.reduceConstants(root)));
	}

	@Test
	public void negTest4() throws ParseException, UnboundIdentifierException {
		String expr = new String(" 3 --4");
		TreeNode root = ExpressionTrees.createTree(expr);
		assertEquals(7, ExpressionTrees.evaluate(root));
		assertEquals("3 - -4", ExpressionTrees.getInfixString(root));
		assertEquals("3 4 NEG -", ExpressionTrees.getPostfixString(root));
		assertEquals("7", ExpressionTrees.getInfixString(ExpressionTrees.reduceConstants(root)));
	}

	@Test
	public void parensTest1() throws ParseException, UnboundIdentifierException {
		String expr = new String("8*(2+1)");
		TreeNode root = ExpressionTrees.createTree(expr);
		assertEquals(24, ExpressionTrees.evaluate(root));
		assertEquals("8 * (2 + 1)", ExpressionTrees.getInfixString(root));
		assertEquals("8 2 1 + *", ExpressionTrees.getPostfixString(root));
		assertEquals("24", ExpressionTrees.getInfixString(ExpressionTrees.reduceConstants(root)));
	}

	@Test
	public void parensTest2() throws ParseException, UnboundIdentifierException {
		String expr = new String("(3+2)-(6*2)");
		TreeNode root = ExpressionTrees.createTree(expr);
		assertEquals(-7, ExpressionTrees.evaluate(root));
		assertEquals("3 + 2 - 6 * 2", ExpressionTrees.getInfixString(root));
		assertEquals("3 2 + 6 2 * -", ExpressionTrees.getPostfixString(root));
		assertEquals("-7", ExpressionTrees.getInfixString(ExpressionTrees.reduceConstants(root)));
	}

	@Test
	public void parensTest3() throws ParseException, UnboundIdentifierException {
		String expr = new String("(4/3)*(3/4)");
		TreeNode root = ExpressionTrees.createTree(expr);
		assertEquals(0, ExpressionTrees.evaluate(root));
		assertEquals("4 / 3 * (3 / 4)", ExpressionTrees.getInfixString(root));
		assertEquals("4 3 / 3 4 / *", ExpressionTrees.getPostfixString(root));
		assertEquals("0", ExpressionTrees.getInfixString(ExpressionTrees.reduceConstants(root)));
	}

	@Test
	public void parensTest4() throws ParseException, UnboundIdentifierException {
		String expr = new String("(2^3)/(3^2)");
		TreeNode root = ExpressionTrees.createTree(expr);
		assertEquals(0, ExpressionTrees.evaluate(root));
		assertEquals("2 ^ 3 / 3 ^ 2", ExpressionTrees.getInfixString(root));
		assertEquals("2 3 ^ 3 2 ^ /", ExpressionTrees.getPostfixString(root));
		assertEquals("0", ExpressionTrees.getInfixString(ExpressionTrees.reduceConstants(root)));
	}

	@Test
	public void parensTest5() throws ParseException, UnboundIdentifierException {
		String expr = new String("(3^2)/(2^3)");
		TreeNode root = ExpressionTrees.createTree(expr);
		assertEquals(1, ExpressionTrees.evaluate(root));
		assertEquals("3 ^ 2 / 2 ^ 3", ExpressionTrees.getInfixString(root));
		assertEquals("3 2 ^ 2 3 ^ /", ExpressionTrees.getPostfixString(root));
		assertEquals("1", ExpressionTrees.getInfixString(ExpressionTrees.reduceConstants(root)));
	}

	@Test
	public void expTest1() throws ParseException, UnboundIdentifierException {
		String expr = new String("3^-2");
		TreeNode root = ExpressionTrees.createTree(expr);
		assertEquals("3 ^ (-2)", ExpressionTrees.getInfixString(root));
		assertEquals("3 2 NEG ^", ExpressionTrees.getPostfixString(root));
	}
}