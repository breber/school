package edu.iastate.cs228.hw4.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import edu.iastate.cs228.hw4.ExpressionTrees;
import edu.iastate.cs228.hw4.ParseException;
import edu.iastate.cs228.hw4.TokenType;
import edu.iastate.cs228.hw4.Tokenizer;
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
import edu.iastate.cs228.hw4.viewer.TreeViewer;

public class MassiveTest {

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
  public void setUp() throws Exception {

    fig1 = ExpressionTrees.createTree("(2+3)*4");
    fig2 = ExpressionTrees.createTree("2 + 3 * 4");
    fig3 = ExpressionTrees.createTree("2 - 3 - 4");
    fig4 = ExpressionTrees.createTree("(x + 13) * y - 42 * 17");
    fig5 = ExpressionTrees.createTree("-2 + 3 * -(4 * (5 - 6))");
    fig6 = ExpressionTrees.createTree("2 + - (3 - 4)");
    fig7 = ExpressionTrees.createTree("2 ^ 3 ^ 2"); // note this is not as
    // the caption reads
    varsonly = ExpressionTrees.createTree("a + b * -(c *(d - e))");
    dupvars = ExpressionTrees.createTree("x + 3 * y + x ^ 2 - x * y");
    reduce1 = ExpressionTrees.createTree("(2*3+x)*(y- 2^3)");
    leftAssoExp = ExpressionTrees.createTree("(2 ^ 3) ^ 4");
    setUpReduce2();

  }

  /**
   * Initializes the Tree reduce2
   */
  private static void setUpReduce2() {

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
  public void testGetPostfixString() {
    assertEquals(ExpressionTrees.getPostfixString(fig1), "2 3 + 4 *");
    assertEquals(ExpressionTrees.getPostfixString(fig3), "2 3 - 4 -");
    assertEquals(ExpressionTrees.getPostfixString(fig4),
        "x 13 + y * 42 17 * -");
    assertEquals(ExpressionTrees.getPostfixString(fig5),
        "2 NEG 3 4 5 6 - * NEG * +");
    assertEquals(ExpressionTrees.getPostfixString(varsonly),
        "a b c d e - * NEG * +");
  }

  @Test
  public void testGetInfixString() {
    assertEquals(ExpressionTrees.getInfixString(fig1), "(2 + 3) * 4");
    assertEquals(ExpressionTrees.getInfixString(fig2), "2 + 3 * 4");
    assertEquals(ExpressionTrees.getInfixString(fig3), "2 - 3 - 4");
    assertEquals(ExpressionTrees.getInfixString(fig4),
        "(x + 13) * y - 42 * 17");
    assertEquals(ExpressionTrees.getInfixString(fig5),
        "-2 + 3 * -(4 * (5 - 6))");
    assertEquals(ExpressionTrees.getInfixString(fig6), "2 + -(3 - 4)");
    assertEquals(ExpressionTrees.getInfixString(fig7), "2 ^ 3 ^ 2");
    assertEquals(ExpressionTrees.getInfixString(varsonly),
        "a + b * -(c * (d - e))");
    assertEquals(ExpressionTrees.getInfixString(leftAssoExp), "(2 ^ 3) ^ 4");
  }

  @Test
  /**
   * Note this is dependent on a successful getPostfixString
   */
  public void testReduceConstants() {

    TreeNode reduced1 = ExpressionTrees.reduceConstants(reduce1);
    TreeNode reduced2 = ExpressionTrees.reduceConstants(reduce2);
    assertEquals(ExpressionTrees.getPostfixString(reduced1),
        "6 x + y 8 - *");
    assertEquals(ExpressionTrees.getPostfixString(reduced2),
        "x -5 * -6 + y 9 ^ *");
  }

  @Test
  public void testEvaluate() throws UnboundIdentifierException {
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

  @Test
  public void testParseErrors() {

    try {
      ExpressionTrees.createTree("(2+3*4");
      fail("Uncaught Exception, Extra L Parenthesis");
    } catch (ParseException e) {
      assertTrue(true);
    }

    try {
      ExpressionTrees.createTree("2+3)*4");
      fail("Uncaught Exception, Extra R Parenthesis");
    } catch (ParseException e) {
      assertTrue(true);
    }

    try {
      ExpressionTrees.createTree("(+2+3)*4");
      fail("Uncaught Exception, Operand expected");
    } catch (ParseException e) {
      assertTrue(true);
    }

    try {
      ExpressionTrees.createTree("3 3");
      fail("Uncaught Exception, Operator expected");
    } catch (ParseException e) {
      assertTrue(true);
    }

    try {
      ExpressionTrees.createTree("* +");
      fail("Uncaught Exception, Operand expected");
    } catch (ParseException e) {
      assertTrue(true);
    }

    try {
      ExpressionTrees.createTree("foo +");
      fail("Uncaught Exception, Operator expected");
    } catch (ParseException e) {
      assertTrue(true);
    }


    try {
      ExpressionTrees.createTree("(()())");
      fail("Uncaught Exception, Operator expected");
    } catch (ParseException e) {
      assertTrue(true);
    }

    try {
      ExpressionTrees.createTree("(2)");
      assertTrue(true);
    } catch (ParseException e) {
      fail("False exception: \"(2)\"");
    }

    try {
      ExpressionTrees.createTree("(())");
      fail("Uncaught Exception, Operator expected");
    } catch (ParseException e) {
      assertTrue(true);
    }

    try {
      ExpressionTrees.createTree("3");
      assertTrue(true);
    } catch (ParseException e) {
      fail("False exception, \"3\"");
    }

  }

  @Test
  public void testEvaluateErrors() {
    // varsonly = "a + b * -(c *(d - e))"
    // dupvars = "x + 3 * y + x ^ 2 - x * y"

    try {
      ExpressionTrees.evaluate(varsonly, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
      assertTrue(true);
    } catch (UnboundIdentifierException e) {
      fail("Should allow extra args");
    }

    try {
      ExpressionTrees.evaluate(dupvars, 0, 1);
      assertTrue(true);
    } catch (UnboundIdentifierException e) {
      fail("use duplicate vars");
    }

    try {
      ExpressionTrees.evaluate(varsonly, 3, 2);
      fail("too few args");
    } catch (UnboundIdentifierException e) {
      assertTrue(true);
    }

    try {
      ExpressionTrees.evaluate(fig1, (Integer) null);
      assertTrue(true);
    } catch (UnboundIdentifierException e) {
      fail("allow null args for no vars");
    }

    try {
      ExpressionTrees.evaluate(fig1, 2, 3, 4);
      assertTrue(true);
    } catch (UnboundIdentifierException e) {
      fail("allow extra args for no vars");
    }

  }

  @Test
  public void additionalTest() {

    // "However, there are also cases like 2 + (3 - 4) where the "-" subtree
    // has the same
    // precedence as the parent "+", but it's the right subtree and the
    // operation is normally
    // left-associative, so the parens are needed."

    TreeNode fig8 = null;
    try {
      fig8 = ExpressionTrees.createTree("1 + (2 - 3)");
    } catch (ParseException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    TreeViewer.start(fig8);
    assertEquals(ExpressionTrees.getInfixString(fig8), "1 + (2 - 3)");

  }

  @Test
  public void expTest2() throws ParseException,
      UnboundIdentifierException {
    String expr = "3^-2";
    TreeNode root = ExpressionTrees.createTree(expr);
    assertEquals("3 ^ (-2)", ExpressionTrees.getInfixString(root));
    assertEquals("3 2 NEG ^", ExpressionTrees.getPostfixString(root));
  }

  @Test
  public void expTest1() throws ParseException,
      UnboundIdentifierException {
    String expr = "3^(-2)";
    TreeNode root = ExpressionTrees.createTree(expr);
    assertEquals("3 ^ (-2)", ExpressionTrees.getInfixString(root));
    assertEquals("3 2 NEG ^", ExpressionTrees.getPostfixString(root));
  }

  /**
   * Test with 3 variables and constants for reduction.
   *
   * @throws ParseException
   * @throws UnboundIdentifierException
   */
  @Test
  public void test2() throws ParseException, UnboundIdentifierException {
    String expr = "8*A + 4/2 * (3*B) + (7+8)/C";
    TreeNode root = ExpressionTrees.createTree(expr);

    assertEquals("8 * A + 4 / 2 * (3 * B) + (7 + 8) / C",
        ExpressionTrees.getInfixString(root));
    assertEquals("8 A * 4 2 / 3 B * * + 7 8 + C / +",
        ExpressionTrees.getPostfixString(root));

    assertEquals(107, ExpressionTrees.evaluate(root, 6, 9, 3, 4, 5));
    try {
      ExpressionTrees.evaluate(root, 1);
      fail("Exception not thrown. Only 1 value supplied, but there are 3 variables.");
    } catch (UnboundIdentifierException e) {
      assertTrue(true);
    }
    assertEquals("8 * A + 2 * (3 * B) + 15 / C",
        ExpressionTrees.getInfixString(ExpressionTrees.reduceConstants(root)));
  }

  @Test
  public void testCreateTree11() {
    try {
      TreeNode root = ExpressionTrees.createTree("3--27");
      TreeNode expected = new MinusNode(new IntNode(3), new NegationNode(new IntNode(27)));
      assertEquals(ExpressionTrees.getInfixString(expected), ExpressionTrees.getInfixString(root));
    } catch (ParseException pe) {
      assertTrue("ParseException", false);
    }
  }

  @Test
  public void testCreateTree12() {
    try {
      TreeNode root = ExpressionTrees.createTree("---27");
      TreeNode expected = new NegationNode(new NegationNode(new NegationNode(new IntNode(27))));
      assertEquals(ExpressionTrees.getInfixString(expected), ExpressionTrees.getInfixString(root));
    } catch (ParseException pe) {
      assertTrue("ParseException", false);
    }
  }

  @Test
  public void testCreateTree10() {
    try {
      TreeNode root = ExpressionTrees.createTree("3---27");
      TreeNode expected = new MinusNode(new IntNode(3), new NegationNode(new NegationNode(new IntNode(27))));
      assertEquals(ExpressionTrees.getInfixString(expected), ExpressionTrees.getInfixString(root));
    } catch (ParseException pe) {
      assertTrue("ParseException", false);
    }
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
    TreeViewer.start(root);
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
    //
    MinusNode expected =
        new MinusNode(
        new TimesNode(
        new PlusNode(
        new IDNode("x"), new IntNode(13)),
        new IDNode("y")),
        new IntNode(714));

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
    //
    MinusNode expected =
        new MinusNode(
        new TimesNode(
        new IntNode(6),
        new IDNode("y")),
        new IntNode(714));
    assertEquals(ExpressionTrees.getInfixString(expected), ExpressionTrees.getInfixString(ExpressionTrees.reduceConstants(root)));
  }

  @Test
  public void testReduceConstantsExp() {
    PlusNode plus = new PlusNode(new ExpNode(new IntNode(2), new IntNode(3)), new IntNode(4));
    IntNode expected = new IntNode(12);
    assertEquals(
        ExpressionTrees.getInfixString(expected), ExpressionTrees.getInfixString(ExpressionTrees.reduceConstants(plus)));


  }

  @Test
  public void testReduceConstantsExp2() {
    IntNode expected = new IntNode(64);

    ExpNode exp = new ExpNode(new ExpNode(new IntNode(2), new IntNode(3)), new IntNode(2));
    assertEquals(
        ExpressionTrees.getInfixString(expected), ExpressionTrees.getInfixString(ExpressionTrees.reduceConstants(exp)));


  }

  @Test
  public void testReduceConstantsExp3() {
    NegationNode expected = new NegationNode(new IntNode(64));
    ExpNode exp = new ExpNode(new ExpNode(new IntNode(2), new IntNode(3)), new IntNode(2));
    NegationNode root = new NegationNode(exp);
    assertEquals(
        ExpressionTrees.getInfixString(expected), ExpressionTrees.getInfixString(ExpressionTrees.reduceConstants(root)));
  }

  @Test
  public void testEvaluateFigure1() throws ParseException, UnboundIdentifierException {
    TreeNode root = ExpressionTrees.createTree("(3 + x)^y - (z * 2)");
    assertEquals(
        10, ExpressionTrees.evaluate(root, 1, 2, 3));
  }

  @Test
  public void testEvaluateFigure2() throws ParseException, UnboundIdentifierException {
    TreeNode root = ExpressionTrees.createTree("(5^2 / x) * (2 / y)^z");
    assertEquals(5, ExpressionTrees.evaluate(root, 5, 2, 2));
  }

  @Test
  public void testEvaluateFigure3() throws ParseException, UnboundIdentifierException {
    TreeNode root = ExpressionTrees.createTree("(x^y - 1) * 2 + 1");
    assertEquals(
        31, ExpressionTrees.evaluate(root, 2, 4, 1, 5, 6));
  }

  @Test
  public void testEvaluateFigure4() throws ParseException, UnboundIdentifierException {
    TreeNode root = ExpressionTrees.createTree("(x^y - 1) * 2 + 1");

    try {
      ExpressionTrees.evaluate(root, 1);
      fail();


    } catch (UnboundIdentifierException ex) {
    }
  }

  @Test
  public void testEvaluateFigure5() throws ParseException, UnboundIdentifierException {
    TreeNode root = ExpressionTrees.createTree("-x^0 + -6 / -(2 + 1)");
    assertEquals(1, ExpressionTrees.evaluate(root, 4));
  }

  @Test
  public void testEvaluate6() throws ParseException, UnboundIdentifierException {
    TreeNode root = new ExpNode(new IntNode(4), new IntNode(0));
    assertEquals(1, ExpressionTrees.evaluate(root));
  }

  @Test
  public void testEvaluate7() throws ParseException, UnboundIdentifierException {
    TreeNode root = new ExpNode(new IntNode(0), new IntNode(0));
    assertEquals(1, ExpressionTrees.evaluate(root));
  }

  @Test
  public void testEvaluate8() throws ParseException, UnboundIdentifierException {
    TreeNode root = ExpressionTrees.createTree("0^0/2 + 1");
    assertEquals(1, ExpressionTrees.evaluate(root));
  }

  @Test
  public void testEvaluate9() throws ParseException, UnboundIdentifierException {
    TreeNode root = ExpressionTrees.createTree("0^0");
    assertEquals(1, ExpressionTrees.evaluate(root));
  }

  @Test
  public void testEvaluate10() throws ParseException, UnboundIdentifierException {
    TreeNode root = ExpressionTrees.createTree("((1))");
    assertEquals(1, ExpressionTrees.evaluate(root));
  }

  @Test
  public void testCreateTree1() {
    try {
      TreeNode root = ExpressionTrees.createTree("2-3");
      TreeNode expected = new MinusNode(new IntNode(2), new IntNode(3));
      assertEquals(ExpressionTrees.getInfixString(expected), ExpressionTrees.getInfixString(root));
    } catch (ParseException pe) {
      assertTrue("ParseException", false);
    }
  }

  @Test
  public void testCreateTree2() {
    try {
      TreeNode root = ExpressionTrees.createTree("7+9");
      TreeNode expected = new PlusNode(new IntNode(7), new IntNode(9));
      assertEquals(ExpressionTrees.getInfixString(expected), ExpressionTrees.getInfixString(root));
    } catch (ParseException pe) {
      assertTrue("ParseException", false);
    }
  }

  @Test
  public void testCreateTree3() {
    try {
      TreeNode root = ExpressionTrees.createTree("3*27");
      TreeNode expected = new TimesNode(new IntNode(3), new IntNode(27));
      assertEquals(ExpressionTrees.getInfixString(expected), ExpressionTrees.getInfixString(root));
    } catch (ParseException pe) {
      assertTrue("ParseException", false);
    }
  }

  @Test
  public void testCreateTree4() {
    try {
      TreeNode root = ExpressionTrees.createTree("4/17");
      TreeNode expected = new DivNode(new IntNode(4), new IntNode(17));
      assertEquals(ExpressionTrees.getInfixString(expected), ExpressionTrees.getInfixString(root));
    } catch (ParseException pe) {
      assertTrue("ParseException", false);
    }
  }

  @Test
  public void testCreateTree5() {
    try {
      TreeNode root = ExpressionTrees.createTree("3^2");
      TreeNode expected = new ExpNode(new IntNode(3), new IntNode(2));
      assertEquals(ExpressionTrees.getInfixString(expected), ExpressionTrees.getInfixString(root));
    } catch (ParseException pe) {
      assertTrue("ParseException", false);
    }
  }

  @Test
  public void testCreateTree6() {
    try {
      TreeNode root = ExpressionTrees.createTree("(3+2)");
      TreeNode expected = new PlusNode(new IntNode(3), new IntNode(2));
      assertEquals(ExpressionTrees.getInfixString(expected), ExpressionTrees.getInfixString(root));
    } catch (ParseException pe) {
      assertTrue("ParseException", false);
    }
  }

  @Test
  public void testCreateTree7() {
    try {
      TreeNode root = ExpressionTrees.createTree("((((2+7))))");
      TreeNode expected = new PlusNode(new IntNode(2), new IntNode(7));
      assertEquals(ExpressionTrees.getInfixString(expected), ExpressionTrees.getInfixString(root));
    } catch (ParseException pe) {
      assertTrue("ParseException", false);
    }
  }

  @Test
  public void testCreateTree8() {
    try {
      TreeNode root = ExpressionTrees.createTree("2--8");
      TreeNode expected = new MinusNode(new IntNode(2), new NegationNode(new IntNode(8)));
      assertEquals(ExpressionTrees.getInfixString(expected), ExpressionTrees.getInfixString(root));
    } catch (ParseException pe) {
      assertTrue("ParseException", false);
    }
  }

  @Test //TODO look at expected, verify validity
  public void testCreateTree9() {
    try {
      TreeNode root = ExpressionTrees.createTree("1^2^3");
      TreeNode expected = new ExpNode(new IntNode(1), new ExpNode(new IntNode(2), new IntNode(3)));
      assertEquals(ExpressionTrees.getInfixString(expected), ExpressionTrees.getInfixString(root));
    } catch (ParseException pe) {
      assertTrue("ParseException", false);
    }
  }

  @Test
  public void testEvaluateFigure6() throws ParseException,
      UnboundIdentifierException {
    TreeNode root = ExpressionTrees.createTree("2-----2");
    assertEquals(0, ExpressionTrees.evaluate(root));
  }
//} extra brace, ajmontag 11/13

  @Test
  public void simpleTest1() throws ParseException,
      UnboundIdentifierException {
    String expr = new String("3+4");
    TreeNode root = ExpressionTrees.createTree(expr);
    assertEquals(7, ExpressionTrees.evaluate(root));
    assertEquals("3 + 4", ExpressionTrees.getInfixString(root));
    assertEquals("3 4 +", ExpressionTrees.getPostfixString(root));
    assertEquals("7", ExpressionTrees.getInfixString(ExpressionTrees.reduceConstants(root)));
  }

  @Test
  public void simpleTest2() throws ParseException,
      UnboundIdentifierException {
    String expr = new String("5*4");
    TreeNode root = ExpressionTrees.createTree(expr);
    assertEquals(20, ExpressionTrees.evaluate(root));
    assertEquals("5 * 4", ExpressionTrees.getInfixString(root));
    assertEquals("5 4 *", ExpressionTrees.getPostfixString(root));
    assertEquals("20", ExpressionTrees.getInfixString(ExpressionTrees.reduceConstants(root)));
  }

  @Test
  public void simpleTest4() throws ParseException,
      UnboundIdentifierException {
    String expr = new String("7-2");
    TreeNode root = ExpressionTrees.createTree(expr);
    assertEquals(5, ExpressionTrees.evaluate(root));
    assertEquals("7 - 2", ExpressionTrees.getInfixString(root));
    assertEquals("7 2 -", ExpressionTrees.getPostfixString(root));
    assertEquals("5", ExpressionTrees.getInfixString(ExpressionTrees.reduceConstants(root)));
  }

  @Test
  public void simpleTest5() throws ParseException,
      UnboundIdentifierException {
    String expr = new String("3-5");
    TreeNode root = ExpressionTrees.createTree(expr);
    assertEquals(-2, ExpressionTrees.evaluate(root));
    assertEquals("3 - 5", ExpressionTrees.getInfixString(root));
    assertEquals("3 5 -", ExpressionTrees.getPostfixString(root));
    assertEquals("-2", ExpressionTrees.getInfixString(ExpressionTrees.reduceConstants(root)));
  }

  @Test
  public void simpleTest6() throws ParseException,
      UnboundIdentifierException {
    String expr = new String("0-0");
    TreeNode root = ExpressionTrees.createTree(expr);
    assertEquals(0, ExpressionTrees.evaluate(root));
    assertEquals("0 - 0", ExpressionTrees.getInfixString(root));
    assertEquals("0 0 -", ExpressionTrees.getPostfixString(root));
    assertEquals("0", ExpressionTrees.getInfixString(ExpressionTrees.reduceConstants(root)));
  }

  @Test
  public void simpleTest7() throws ParseException,
      UnboundIdentifierException {
    String expr = new String("9/2");
    TreeNode root = ExpressionTrees.createTree(expr);
    assertEquals(4, ExpressionTrees.evaluate(root));
    assertEquals("9 / 2", ExpressionTrees.getInfixString(root));
    assertEquals("9 2 /", ExpressionTrees.getPostfixString(root));
    assertEquals("4", ExpressionTrees.getInfixString(ExpressionTrees.reduceConstants(root)));
  }

  @Test
  public void negTest1() throws ParseException,
      UnboundIdentifierException {
    String expr = new String("5+-3");
    TreeNode root = ExpressionTrees.createTree(expr);
    assertEquals(2, ExpressionTrees.evaluate(root));
    assertEquals("5 + -3", ExpressionTrees.getInfixString(root));
    assertEquals("5 3 NEG +", ExpressionTrees.getPostfixString(root));
    assertEquals("2", ExpressionTrees.getInfixString(ExpressionTrees.reduceConstants(root)));
  }

  @Test
  public void negTest2() throws ParseException,
      UnboundIdentifierException {
    String expr = new String("2+-(3-2)");
    TreeNode root = ExpressionTrees.createTree(expr);
    assertEquals(1, ExpressionTrees.evaluate(root));
    TreeNode compare = new PlusNode(new IntNode(2), new NegationNode(new MinusNode(new IntNode(3), new IntNode(2))));
    assertEquals(ExpressionTrees.getInfixString(compare), ExpressionTrees.getInfixString(root));
    assertEquals(ExpressionTrees.getPostfixString(compare), ExpressionTrees.getPostfixString(root));
    assertEquals("2 + -(3 - 2)", ExpressionTrees.getInfixString(root));
    assertEquals("2 3 2 - NEG +", ExpressionTrees.getPostfixString(root));
    assertEquals("1", ExpressionTrees.getInfixString(ExpressionTrees.reduceConstants(root)));
  }

  @Test
  public void negTest3() throws ParseException,
      UnboundIdentifierException {
    String expr = new String("6+--4");
    TreeNode root = ExpressionTrees.createTree(expr);
    root = new PlusNode(new IntNode(6), new NegationNode(new NegationNode(new IntNode(4))));
    assertEquals(10, ExpressionTrees.evaluate(root));
    assertEquals("6 + --4", ExpressionTrees.getInfixString(root));
    assertEquals("6 4 NEG NEG +", ExpressionTrees.getPostfixString(root));
    assertEquals("10", ExpressionTrees.getInfixString(ExpressionTrees.reduceConstants(root)));
  }

  @Test
  public void negTest4() throws ParseException,
      UnboundIdentifierException {
    String expr = new String(" 3 --4");
    TreeNode root = ExpressionTrees.createTree(expr);
    assertEquals(7, ExpressionTrees.evaluate(root));
    assertEquals("3 - -4", ExpressionTrees.getInfixString(root));
    assertEquals("3 4 NEG -", ExpressionTrees.getPostfixString(root));
    assertEquals("7", ExpressionTrees.getInfixString(ExpressionTrees.reduceConstants(root)));
  }

  @Test
  public void parensTest1() throws ParseException,
      UnboundIdentifierException {
    String expr = new String("8*(2+1)");
    TreeNode root = ExpressionTrees.createTree(expr);
    assertEquals(24, ExpressionTrees.evaluate(root));
    assertEquals("8 * (2 + 1)", ExpressionTrees.getInfixString(root));
    assertEquals("8 2 1 + *", ExpressionTrees.getPostfixString(root));
    assertEquals("24", ExpressionTrees.getInfixString(ExpressionTrees.reduceConstants(root)));
  }

  @Test
  public void parensTest2() throws ParseException,
      UnboundIdentifierException {
    String expr = new String("(3+2)-(6*2)");
    TreeNode root = ExpressionTrees.createTree(expr);
    // assertEquals(-7, ExpressionTrees.evaluate(root));
    assertEquals("3 + 2 - 6 * 2", ExpressionTrees.getInfixString(root));
    assertEquals("3 2 + 6 2 * -", ExpressionTrees.getPostfixString(root));
    assertEquals("-7", ExpressionTrees.getInfixString(ExpressionTrees.reduceConstants(root)));
  }

  @Test
  public void parensTest3() throws ParseException,
      UnboundIdentifierException {
    String expr = new String("(4/3)*(3/4)");
    TreeNode root = ExpressionTrees.createTree(expr);
    assertEquals(0, ExpressionTrees.evaluate(root));
    assertEquals("4 / 3 * (3 / 4)", ExpressionTrees.getInfixString(root));
    assertEquals("4 3 / 3 4 / *", ExpressionTrees.getPostfixString(root));
    assertEquals("0", ExpressionTrees.getInfixString(ExpressionTrees.reduceConstants(root)));
  }

  @Test
  public void parensTest4() throws ParseException,
      UnboundIdentifierException {
    String expr = new String("(2^3)/(3^2)");
    TreeNode root = ExpressionTrees.createTree(expr);
    assertEquals(0, ExpressionTrees.evaluate(root));
    assertEquals("2 ^ 3 / 3 ^ 2", ExpressionTrees.getInfixString(root));
    assertEquals("2 3 ^ 3 2 ^ /", ExpressionTrees.getPostfixString(root));
    assertEquals("0", ExpressionTrees.getInfixString(ExpressionTrees.reduceConstants(root)));
  }

  @Test
  public void parensTest5() throws ParseException,
      UnboundIdentifierException {
    String expr = new String("(3^2)/(2^3)");
    TreeNode root = ExpressionTrees.createTree(expr);
    assertEquals(1, ExpressionTrees.evaluate(root));
    assertEquals("3 ^ 2 / 2 ^ 3", ExpressionTrees.getInfixString(root));
    assertEquals("3 2 ^ 2 3 ^ /", ExpressionTrees.getPostfixString(root));
    assertEquals("1", ExpressionTrees.getInfixString(ExpressionTrees.reduceConstants(root)));
  }

  /**
   * Simple test with parentheses. No constants to reduce.
   *
   * @throws ParseException
   * @throws UnboundIdentifierException
   */
  @Test
  public void test1() throws ParseException, UnboundIdentifierException {
    String expr = new String("(3+4*x-y)*(7*x)");
    TreeNode root = ExpressionTrees.createTree(expr);

    assertEquals("(3 + 4 * x - y) * (7 * x)",
        ExpressionTrees.getInfixString(root));
    assertEquals("3 4 x * + y - 7 x * *",
        ExpressionTrees.getPostfixString(root));

    assertEquals(35, ExpressionTrees.evaluate(root, 1, 2, 3, 4));
    try {
      ExpressionTrees.evaluate(root, 1);
      fail("Exception not thrown. Only 1 value supplied, but there are 2 variables.");
    } catch (UnboundIdentifierException e) {
      assertTrue(true);
    }
    assertEquals("(3 + 4 * x - y) * (7 * x)",
        ExpressionTrees.getInfixString(ExpressionTrees.reduceConstants(root)));
  }

  /**
   * Test with unneeded parentheses, longer variable names, negation, and
   * constant reduction.
   *
   * @throws ParseException
   * @throws UnboundIdentifierException
   */
  @Test
  public void test3() throws ParseException, UnboundIdentifierException {
    String expr = new String("(3+ab1)/c6+(4*7)*(df7/4)--54");
    TreeNode root = ExpressionTrees.createTree(expr);

    assertEquals("(3 + ab1) / c6 + 4 * 7 * (df7 / 4) - -54",
        ExpressionTrees.getInfixString(root));
    assertEquals("3 ab1 + c6 / 4 7 * df7 4 / * + 54 NEG -",
        ExpressionTrees.getPostfixString(root));

    assertEquals(55, ExpressionTrees.evaluate(root, -1, 2, 3, 4));
    try {
      ExpressionTrees.evaluate(root, 1);
      fail("Exception not thrown. Only 1 value supplied, but there are 3 variables.");
    } catch (UnboundIdentifierException e) {
      assertTrue(true);
    }
    assertEquals("(3 + ab1) / c6 + 28 * (df7 / 4) - -54",
        ExpressionTrees.getInfixString(ExpressionTrees.reduceConstants(root)));
  }

  /**
   * Test with unneeded parentheses, exponentiation, negation, and constant
   * reduction.
   *
   * @throws ParseException
   * @throws UnboundIdentifierException
   */
  @Test
  public void test4() throws ParseException, UnboundIdentifierException {
    String expr = new String("((5*x) ^ (-4*y))/2 + 7^2 *-4^z");
    TreeNode root = ExpressionTrees.createTree(expr);

    assertEquals("(5 * x) ^ (-4 * y) / 2 + 7 ^ 2 * -4 ^ z",
        ExpressionTrees.getInfixString(root));
    assertEquals("5 x * 4 NEG y * ^ 2 / 7 2 ^ 4 z ^ NEG * +",
        ExpressionTrees.getPostfixString(root));

    assertEquals(-195704, ExpressionTrees.evaluate(root, 2, -1, 6, 0));
    try {
      ExpressionTrees.evaluate(root, 1);
      fail("Exception not thrown. Only 1 value supplied, but there are 3 variables.");
    } catch (UnboundIdentifierException e) {
      assertTrue(true);
    }
    assertEquals("(5 * x) ^ (-4 * y) / 2 + 49 * -4 ^ z",
        ExpressionTrees.getInfixString(ExpressionTrees.reduceConstants(root)));
  }

  @Test
  public void ConvertMinusToNeg() throws ParseException {
    ExpressionTrees.createTree("(2+2)*2+-(3*5)");
  }

  @Test(expected = edu.iastate.cs228.hw4.ParseException.class)
  public void OptorWhenOpandExpected() throws ParseException {
    ExpressionTrees.createTree("(2+2)*2++(3*5)");
  }

  @Test(expected = edu.iastate.cs228.hw4.ParseException.class)
  public void MissingOperandAtEnd() throws ParseException {
    ExpressionTrees.createTree("2+");
  }

  @Test(expected = edu.iastate.cs228.hw4.ParseException.class)
  public void ParenthesisCheck() throws ParseException {
    ExpressionTrees.createTree("(2+2)(+2)");
  }

  @Test(expected = edu.iastate.cs228.hw4.ParseException.class)
  public void OpandWhenOptorExpected() throws ParseException {
    ExpressionTrees.createTree("(2+2)*2+(3*5) 5");
  }

  @Test(expected = edu.iastate.cs228.hw4.ParseException.class)
  public void InvalidIntToken() throws ParseException {
    ExpressionTrees.createTree("-2+3*-(4*(67e-6))");
  }

  @Test(expected = edu.iastate.cs228.hw4.ParseException.class)
  public void ExtraLParen() throws ParseException {
    ExpressionTrees.createTree("(2+2-(4*5)^6-(9*4+7)");
  }

  @Test(expected = edu.iastate.cs228.hw4.ParseException.class)
  public void ExtraRParen() throws ParseException {
    ExpressionTrees.createTree("2+2-(4*5))^6-(9*4+7)");
  }

  @Test
  public void testInfix() {
    IntNode one = new IntNode(1);
    IntNode two = new IntNode(2);
    IntNode three = new IntNode(3);
    PlusNode plus = new PlusNode(one, two);
    TimesNode times = new TimesNode(plus, three);

    assertEquals("(1 + 2) * 3", ExpressionTrees.getInfixString(times));
  }

  @Test
  public void testInfix2() {
    IntNode one = new IntNode(1);
    IntNode two = new IntNode(2);
    IntNode three = new IntNode(3);
    IntNode four = new IntNode(4);
    PlusNode plus = new PlusNode(one, two);
    MinusNode minus = new MinusNode(three, four);
    TimesNode times = new TimesNode(plus, minus);
    assertEquals("(1 + 2) * (3 - 4)", ExpressionTrees.getInfixString(times));
  }

  @Test
  public void testInfix3() {
    IntNode one = new IntNode(1);
    IntNode two = new IntNode(2);
    NegationNode negTwo = new NegationNode(two);
    IntNode three = new IntNode(3);
    IntNode four = new IntNode(4);
    PlusNode plus = new PlusNode(one, negTwo);
    MinusNode minus = new MinusNode(three, four);
    TimesNode times = new TimesNode(plus, minus);
    assertEquals("(1 + -2) * (3 - 4)", ExpressionTrees.getInfixString(times));
  }

  @Test
  public void testInfix4() {
    TreeNode root = null;
    try {
      root = ExpressionTrees.createTree("2 - (3 - 4)");
    } catch (ParseException e) {
      e.printStackTrace();
    }
    assertEquals("2 - (3 - 4)", ExpressionTrees.getInfixString(root));
  }

  @Test
  public void testInfix5() {
    TreeNode root = null;
    try {
      root = ExpressionTrees.createTree("2 + (3 - 4)");
    } catch (ParseException e) {
      e.printStackTrace();
    }
    assertEquals("2 + (3 - 4)", ExpressionTrees.getInfixString(root));
  }

  @Test
  public void testInfix6() {
    TreeNode root = null;
    try {
      root = ExpressionTrees.createTree("(2 - 3) - 4");
    } catch (ParseException e) {
      e.printStackTrace();
    }
    assertEquals("2 - 3 - 4", ExpressionTrees.getInfixString(root));
  }

  @Test
  public void testCreate1() {
    try {
      TreeNode root = ExpressionTrees.createTree("1-2");
      TreeNode minus = new MinusNode(new IntNode(1), new IntNode(2));
      assertEquals(ExpressionTrees.getInfixString(minus),
          ExpressionTrees.getInfixString(root));
    } catch (ParseException pe) {
      assertTrue("ParseException", false);
    }
  }

  @Test
  public void testCreate2() {
    try {
      TreeNode root = ExpressionTrees.createTree("7+9");
      TreeNode expected = new PlusNode(new IntNode(7), new IntNode(9));
      assertEquals(ExpressionTrees.getInfixString(expected),
          ExpressionTrees.getInfixString(root));
    } catch (ParseException pe) {
      assertTrue("ParseException", false);
    }
  }

  @Test
  public void testCreate3() {
    try {
      TreeNode root = ExpressionTrees.createTree("3*27");
      TreeNode expected = new TimesNode(new IntNode(3), new IntNode(27));
      assertEquals(ExpressionTrees.getInfixString(expected),
          ExpressionTrees.getInfixString(root));
    } catch (ParseException pe) {
      assertTrue("ParseException", false);
    }
  }

  @Test
  public void testCreate4() {
    try {
      TreeNode root = ExpressionTrees.createTree("4/17");
      TreeNode expected = new DivNode(new IntNode(4), new IntNode(17));
      assertEquals(ExpressionTrees.getInfixString(expected),
          ExpressionTrees.getInfixString(root));
    } catch (ParseException pe) {
      assertTrue("ParseException", false);
    }
  }

  @Test
  public void testCreate5() {
    try {
      TreeNode root = ExpressionTrees.createTree("3^2");
      TreeNode expected = new ExpNode(new IntNode(3), new IntNode(2));
      assertEquals(ExpressionTrees.getInfixString(expected),
          ExpressionTrees.getInfixString(root));
    } catch (ParseException pe) {
      assertTrue("ParseException", false);
    }
  }

  @Test
  public void testCreate6() {
    try {
      TreeNode root = ExpressionTrees.createTree("(3+2)");
      TreeNode expected = new PlusNode(new IntNode(3), new IntNode(2));
      assertEquals(ExpressionTrees.getInfixString(expected),
          ExpressionTrees.getInfixString(root));
    } catch (ParseException pe) {
      assertTrue("ParseException", false);
    }
  }

  @Test
  public void testCreate7() {
    try {
      TreeNode root = ExpressionTrees.createTree("((((2+7))))");
      TreeNode expected = new PlusNode(new IntNode(2), new IntNode(7));
      assertEquals(ExpressionTrees.getInfixString(expected),
          ExpressionTrees.getInfixString(root));
    } catch (ParseException pe) {
      assertTrue("ParseException", false);
    }
  }

  @Test
  public void testCreate8() {
    try {
      TreeNode root = ExpressionTrees.createTree("2--8");
      TreeNode expected = new MinusNode(new IntNode(2), new NegationNode(
          new IntNode(8)));
      assertEquals(ExpressionTrees.getInfixString(expected),
          ExpressionTrees.getInfixString(root));
    } catch (ParseException pe) {
      assertTrue("ParseException", false);
    }
  }

  @Test
// TODO look at expected, verify validity
  public void testCreate9() {
    try {
      TreeNode root = ExpressionTrees.createTree("1^2^3");
      TreeNode expected = new ExpNode(new IntNode(1), new ExpNode(
          new IntNode(2), new IntNode(3)));
      assertEquals(ExpressionTrees.getInfixString(expected),
          ExpressionTrees.getInfixString(root));
    } catch (ParseException pe) {
      assertTrue("ParseException", false);
    }
  }

  @Test
  public void testCreate10() {
    try {
      TreeNode root = ExpressionTrees.createTree("3---27");
      TreeNode expected = new MinusNode(new IntNode(3), new NegationNode(
          new NegationNode(new IntNode(27))));
      assertEquals(ExpressionTrees.getInfixString(expected),
          ExpressionTrees.getInfixString(root));
    } catch (ParseException pe) {
      assertTrue("ParseException", false);
    }
  }

  @Test
  public void testClarifications() throws ParseException {
    TreeNode root1 = ExpressionTrees.createTree("2 - (3 - 4)");
    String expr1 = ExpressionTrees.getInfixString(root1);
    assertEquals("2 - (3 - 4)", expr1);
  }

  @Test
  public void testClarifications2() throws ParseException {
    TreeNode root2 = ExpressionTrees.createTree("2 + (3 - 4)");
    String expr2 = ExpressionTrees.getInfixString(root2);
    assertEquals("2 + (3 - 4)", expr2);
  }

  @Test
  public void testClarifications3() throws ParseException {
    TreeNode root3 = ExpressionTrees.createTree("(2 - 3) - 4");
    String expr3 = ExpressionTrees.getInfixString(root3);
    assertEquals("2 - 3 - 4", expr3);
  }

  @Test
  public void testClarifications4() throws ParseException {
    TreeNode root4 = ExpressionTrees.createTree("2^(3^4)");
    String expr4 = ExpressionTrees.getInfixString(root4);
    assertEquals("2 ^ 3 ^ 4", expr4);
  }

  @Test
  public void testClarifications6() throws ParseException {
    TreeNode root5 = ExpressionTrees.createTree("(2^3)^4");
    String expr5 = ExpressionTrees.getInfixString(root5);
    assertEquals("(2 ^ 3) ^ 4", expr5);
  }

  @Test
  public void testClarifications7() throws ParseException {
    TreeNode root6 = ExpressionTrees.createTree("-2^3");
    String expr6 = ExpressionTrees.getInfixString(root6);
    assertEquals("-2 ^ 3", expr6);
  }

  @Test
  public void testClarifications8() throws ParseException {
    TreeNode root7 = ExpressionTrees.createTree("-(2^3)");
    String expr7 = ExpressionTrees.getInfixString(root7);
    assertEquals("-2 ^ 3", expr7);
  }

  @Test
  public void testClarifications9() throws ParseException {
    TreeNode root8 = ExpressionTrees.createTree("(-2)^3");
    String expr8 = ExpressionTrees.getInfixString(root8);
    assertEquals("(-2) ^ 3", expr8);
  }

  @Test
  public void testClarifications10() throws ParseException {
    TreeNode root9 = ExpressionTrees.createTree("2^(-3)");
    String expr9 = ExpressionTrees.getInfixString(root9);
    assertEquals("2 ^ (-3)", expr9);
  }

  @Test
  public void testClarifications11() throws ParseException {
    TreeNode root0 = ExpressionTrees.createTree("-(-(-42))");
    String expr0 = ExpressionTrees.getInfixString(root0);
    assertEquals("---42", expr0);
  }

  @Test
  public void testFigure1() throws ParseException {
    TreeNode root0 = ExpressionTrees.createTree("(2 + 3) * 4");
    String expr0 = ExpressionTrees.getInfixString(root0);
    assertEquals("(2 + 3) * 4", expr0);
  }

  @Test
  public void testFigure2() throws ParseException {
    TreeNode root0 = ExpressionTrees.createTree("2 + 3 * 4");
    String expr0 = ExpressionTrees.getInfixString(root0);
    assertEquals("2 + 3 * 4", expr0);
  }

  @Test
  public void testFigure3() throws ParseException {
    TreeNode root0 = ExpressionTrees.createTree("2 - 3 - 4");
    String expr0 = ExpressionTrees.getInfixString(root0);
    assertEquals("2 - 3 - 4", expr0);
  }

  @Test
  public void testFigure4() throws ParseException {
    TreeNode root0 = ExpressionTrees.createTree("(x + 13) * y - 42 * 17");
    String expr0 = ExpressionTrees.getInfixString(root0);
    assertEquals("(x + 13) * y - 42 * 17", expr0);
  }

  @Test
  public void testFigure5() throws ParseException {
    System.out.println(new Tokenizer("(2 + 3) * 4"));
  }

  @Test
  public void testFigure6() throws ParseException {
    TreeNode root0 = ExpressionTrees.createTree("-2 + 3 * -(4 * (5 - 6))");
    TreeViewer.start(root0);
  }

  @Test
  public void testFigure7() throws ParseException {
    System.out.println(new Tokenizer("2 + -(3-4)"));
  }
  TreeNode testTree;
  TreeNode reduced;
  String infixString;
  String postfixString;
  //Operands
  TreeNode one = new IntNode(1);
  TreeNode two = new IntNode(2);
  TreeNode three = new IntNode(3);
  TreeNode four = new IntNode(4);
  TreeNode five = new IntNode(5);
  TreeNode six = new IntNode(6);

  /**
   * Create a variety of trees and test the string returned by getPostfixString
   */
  @Test
  public void postfixTest() {
    simpleTree();
    assertTrue(postfixString.equals(ExpressionTrees.getPostfixString(testTree)));

    precedenceTree1();
    assertTrue(postfixString.equals(ExpressionTrees.getPostfixString(testTree)));

    precedenceTree2();
    assertTrue(postfixString.equals(ExpressionTrees.getPostfixString(testTree)));

    negationTree();
    assertTrue(postfixString.equals(ExpressionTrees.getPostfixString(testTree)));

    exponentTree();
    assertTrue(postfixString.equals(ExpressionTrees.getPostfixString(testTree)));

    variableTree();
    assertTrue(postfixString.equals(ExpressionTrees.getPostfixString(testTree)));
  }

  /**
   * Create a variety of trees and test the string returned by getInfixString
   */
  @Test
  public void infixTest() {
    simpleTree();
    assertTrue(infixString.equals(ExpressionTrees.getInfixString(testTree)));

//    precedenceTree1();
//    assertTrue(infixString.equals(ExpressionTrees.getInfixString(testTree)));

    precedenceTree2();
    assertTrue(infixString.equals(ExpressionTrees.getInfixString(testTree)));

    negationTree();
    assertTrue(infixString.equals(ExpressionTrees.getInfixString(testTree)));

    exponentTree();
    assertTrue(infixString.equals(ExpressionTrees.getInfixString(testTree)));

    variableTree();
    assertTrue(infixString.equals(ExpressionTrees.getInfixString(testTree)));

  }

  /**
   * Create a variety of trees to test the functionality of reduceConstsnts()
   * @throws UnboundIdentifierException
   */
  @Test
  public void reduceConstantsTest() throws UnboundIdentifierException {
    simpleTree();
    reduced = ExpressionTrees.reduceConstants(testTree);
    assertTrue(reduced.type() == TokenType.INT);
    assertEquals(16, reduced.eval(null));

    precedenceTree1();
    reduced = ExpressionTrees.reduceConstants(testTree);
    assertTrue(reduced.type() == TokenType.INT);
    assertEquals(36, reduced.eval(null));

    precedenceTree2();
    reduced = ExpressionTrees.reduceConstants(testTree);
    assertTrue(reduced.type() == TokenType.INT);
    assertEquals(1, reduced.eval(null));

    negationTree();
    reduced = ExpressionTrees.reduceConstants(testTree);
    assertTrue(reduced.type() == TokenType.INT);
    assertEquals(10, reduced.eval(null));

    exponentTree();
    reduced = ExpressionTrees.reduceConstants(testTree);
    assertTrue(reduced.type() == TokenType.INT);
    assertEquals(4096, reduced.eval(null));

    variableTree();
    reduced = ExpressionTrees.reduceConstants(testTree);
    assertTrue(reduced.type() == TokenType.MINUS);
    infixString = "(x + 13) * y - 714";
    assertEquals(infixString, ExpressionTrees.getInfixString(reduced));

  }

  @Test
  public void createTreeTest() throws ParseException {
    simpleTree();
    testTree = ExpressionTrees.createTree(infixString);
    //assertTrue(infixString.equals(ExpressionTrees.getInfixString(testTree)));
  }

  /**
   * Create a very simple tree with integer operands.
   * No parentheses needed.
   */
  private void simpleTree() {
    //root
    TreeNode simpleTree = new MinusNode(null, null);

    //left branch
    simpleTree.setLeftChild(new PlusNode(
        new TimesNode(new IntNode(5), new IntNode(4)), new IntNode(2)));


    //right branch
    simpleTree.setRightChild(new TimesNode(new IntNode(3), new IntNode(2)));

    testTree = simpleTree;
    infixString = "5 * 4 + 2 - 3 * 2";
    postfixString = "5 4 * 2 + 3 2 * -";
  }

  /**
   * Create a simple tree which requires parentheses.
   * Based on precedence only.
   */
  private void precedenceTree1() {
    //root
    TreeNode precedenceTree = new TimesNode(null, null);

    //left branch
    precedenceTree.setLeftChild(new TimesNode(new PlusNode(five, four), two));

    //right branch
    precedenceTree.setRightChild(new MinusNode(three, one));

    testTree = precedenceTree;
    infixString = "(5 + 4) * 2 * (3 - 1)";
    postfixString = "5 4 + 2 * 3 1 - *";
  }

  /**
   * Create a simple tree which requires parentheses.
   * Creates the case where precedence of subtree is same as that of root,
   * but subtree is right subtree and must therefore be in parentheses.
   */
  private void precedenceTree2() {
    //tree
    TreeNode precedenceTree2 = new PlusNode(two, new MinusNode(three, four));

    testTree = precedenceTree2;
    infixString = "2 + (3 - 4)";
    postfixString = "2 3 4 - +";
  }

  /**
   * Create a tree with an integer negation and an expression negation.
   */
  private void negationTree() {
    //root
    TreeNode negationTree = new PlusNode(null, null);

    //left branch
    negationTree.setLeftChild(new NegationNode(two));

    //right branch
    TreeNode fourth = new MinusNode(five, six);
    TreeNode third = new TimesNode(four, fourth);
    TreeNode second = new NegationNode(third);
    negationTree.setRightChild(new TimesNode(three, second));

    testTree = negationTree;
    infixString = "-2 + 3 * -(4 * (5 - 6))";
    postfixString = "2 NEG 3 4 5 6 - * NEG * +";
  }

  /**
   * Create a tree with consecutive exponent operators
   */
  private void exponentTree() {
    TreeNode exponentTree = new ExpNode(new ExpNode(two, three), four);

    testTree = exponentTree;
    infixString = "(2 ^ 3) ^ 4";
    postfixString = "2 3 ^ 4 ^";
  }

  private void variableTree() {
    //root
    TreeNode variableTree = new MinusNode(null, null);

    //left branch
    TreeNode thirteen = new IntNode(13);
    TreeNode x = new IDNode("x");
    TreeNode y = new IDNode("y");
    variableTree.setLeftChild(new TimesNode(new PlusNode(x, thirteen), y));

    //right branch
    TreeNode seventeen = new IntNode(17);
    TreeNode fortyTwo = new IntNode(42);
    variableTree.setRightChild(new TimesNode(fortyTwo, seventeen));

    testTree = variableTree;
    infixString = "(x + 13) * y - 42 * 17";
    postfixString = "x 13 + y * 42 17 * -";
  }
}
