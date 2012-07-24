package edu.iastate.cs228.hw4;

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import edu.iastate.cs228.hw4.nodes.*;

/**
 * Utilities for parsing and evaluating arithmetic expressions.
 * 
 * @author Brian Reber
 */
public class ExpressionTrees
{
	/**
	 * Returns a String containing the toString() value of each node
	 * in a postorder traversal of the given tree. The values are separated
	 * by a single space. (The negation operation will be displayed as "NEG".)
	 * @param root node at the root of the tree
	 * @return String representation of the tree in postorder
	 */
	public static String getPostfixString(TreeNode root)
	{
		StringBuilder builder = new StringBuilder();
		getPostfixString(root, builder);
		return builder.toString();
	}

	/**
	 * Recursive method that passes along a StringBuilder to build the postfix string.
	 * 
	 * @param root
	 * The current node to start at
	 * @param builder
	 * The StringBuilder to append the string to
	 */
	private static void getPostfixString(TreeNode root, StringBuilder builder)
	{
		if (root == null) 
		{
			return;
		}
		if (root.getChildCount() == 1)
		{
			getPostfixString(root.right(), builder);
			builder.append(" ").append(root.toString());
		}
		else if (root.getChildCount() == 2)
		{
			getPostfixString(root.left(), builder);
			builder.append(" ");
			getPostfixString(root.right(), builder);
			builder.append(" ").append(root.toString());
		}
		else 
		{
			builder.append(root.toString());
		}
	}

	/**
	 * Returns a String containing the toString() value of each node
	 * in an inorder traversal of the given tree, where expressions 
	 * represented by subtrees are enclosed in parentheses where necessary
	 * to indicate the correct order of operations. Binary operators
	 * are surrounded by a space.  Negation should be represented by
	 * an ordinary minus sign, NOT followed by a space.
	 * @param root node at the root of the tree
	 * @return String representation of the tree in parenthesized inorder form
	 */
	public static String getInfixString(TreeNode root)
	{
		StringBuilder sb = new StringBuilder();
		getInfixStringRec(root, sb);
		return sb.toString();
	}
	
	/**
	 * Recursively builds the Infix string, by appending to the given StringBuilder.
	 * 
	 * @param root
	 * The root node of the given subtree
	 * @param sb
	 * The StringBuilder to append to
	 */
	private static void getInfixStringRec(TreeNode root, StringBuilder sb)
	{
		if (root == null)
		{
			return;
		}
		// Gets the string of this node, converting NEG to '-'
		String thisNode = (root.type() == TokenType.NEGATION) ? "-" : root.toString();

		// If there is only 1 child, we want to return the current node, then the right child
		if (root.getChildCount() == 1)
		{
			sb.append(thisNode);
			buildInfixStringForOneSide(root, root.right(), sb);
		}
		// If there are 2 children we will check left, then the current, then the right
		else if (root.getChildCount() == 2)
		{
			buildInfixStringForOneSide(root, root.left(), sb);
			sb.append(" ").append(thisNode).append(" ");
			buildInfixStringForOneSide(root, root.right(), sb);
		}
		// If there are no children, just return this node's string
		else
		{
			sb.append(thisNode);
		}
	}

	/**
	 * To prevent repetitive code (doing check for both right and left sides separately),
	 * extracted the logic into a method that checks the given root node, and the given
	 * child (either right or left)
	 * 
	 * @param root
	 * The current node
	 * @param child
	 * The child of the node currently being checked
	 * @return
	 * A formatted string containing the infix notation of the current node
	 */
	private static void buildInfixStringForOneSide(TreeNode root, TreeNode child, StringBuilder sb)
	{
		// Since EXP is usually right associative, we need to make a special case for when
		// it's child has a lesser or equal precedence. If the left child has a lower or equal
		// precedence, we will wrap with parenthesis
		if (root.type() == TokenType.EXP && root.left() == child && child.type().precedence() <= root.type().precedence())
		{
			sb.append("(");
			getInfixStringRec(child, sb);
			sb.append(")");
		}
		// If the right child has the same precedence (meaning another EXP node), we don't need to wrap it
		else if (root.type() == TokenType.EXP && root.right() == child && child.type().precedence() == root.type().precedence())
		{
			getInfixStringRec(child, sb);
		}
		else if (child.isLeaf() || ((root.right() == child && root.type() != TokenType.NEGATION) ? 
				child.type().precedence() > root.type().precedence() : child.type().precedence() >= root.type().precedence()))
		{
			// -If the child is a leaf, OR 
			// -The child is the right child of it's parent and the parent is not a Negation node and the child's 
			//  precedence is greater than than to the parent's precedence, OR
			// -The child is the left child of it's parent or the parent is a Negation node and the child's precenence is 
			//  greater than or equal to the parent's precedence
			// we just want to return the infix string
			getInfixStringRec(child, sb);
		}
		else
		{
			// Otherwise we want to wrap the infix string in parenthesis to keep order of operations
			sb.append("(");
			getInfixStringRec(child, sb);
			sb.append(")");
		}
	}

	/**
	 * Transforms the given tree so that each subtree not containing
	 * variables is replaced by a single IntNode containing the value
	 * of the subtree. If the tree contains at least one variable, then
	 * the return value is the given root node; otherwise, the return
	 * value is a single new IntNode containing the value of the tree.
	 * @param root node at the root of the given tree
	 * @return transformed tree in which all constant subtrees are
	 *   replaced by a single node
	 */
	public static TreeNode reduceConstants(TreeNode root)
	{
		if (getVariables(root).size() == 0)
		{
			try {
				// Since there are no variables below the current node,
				// just call evaluate with an empty array
				return new IntNode(evaluate(root, new Integer[]{}));
			} catch (UnboundIdentifierException e) {
				// This should never happen, because we know there are no variables
				// below this point in the tree...
			}
		}
		else
		{
			// Recursively create the tree by creating a node with the same type as the
			// current node, and children the values of the reduceConstants() for each subtree
			switch (root.type())
			{
			case ID: return new IDNode(root.toString());
			case NEGATION: return new NegationNode(reduceConstants(root.right()));
			case DIV: return new DivNode(reduceConstants(root.left()), reduceConstants(root.right()));
			case EXP: return new ExpNode(reduceConstants(root.left()), reduceConstants(root.right()));
			case MINUS: return new MinusNode(reduceConstants(root.left()), reduceConstants(root.right()));
			case PLUS: return new PlusNode(reduceConstants(root.left()), reduceConstants(root.right()));
			case TIMES: return new TimesNode(reduceConstants(root.left()), reduceConstants(root.right()));
			}
		}
		// This return statement shouldn't ever happen because all the types of nodes are 
		// taken care of above
		return null;
	}

	/**
	 * Returns the result of evaluating the given expression tree using the 
	 * supplied values to create the environment.  This method performs an initial
	 * traversal of the tree to create the environment, and then invokes
	 * the eval() method of the root node.
	 * <br>
	 * The given values are associated with identifiers as follows:  
	 * Identifiers are ordered according to the first occurrence in a left-to-right 
	 * traversal of the tree.  The i-th identifier in this ordering is associated with 
	 * the i-th Integer argument.  If there are more values supplied than identifiers, 
	 * the extra values are ignored.  If not enough values are supplied, an 
	 * UnboundIdentifierException is thrown.
	 * @param root node at the root of the tree
	 * @param values sequence of Integers to be used as values of variables in the expression
	 * @return result of evaluating the tree using the supplied values to construct the 
	 *     environment
	 * @throws UnboundIdentifierException if not enough values are supplied
	 */
	public static int evaluate(TreeNode root, Integer... values) throws UnboundIdentifierException
	{
		Map<String, Integer> env = new HashMap<String, Integer>();

		List<String> variables = getVariables(root);
		try 
		{
			for (int i = 0; i < variables.size(); i++)
			{
				String var = variables.get(i);
				if (!env.containsKey(var))
				{
					env.put(var, values[i]);
				}
			}
		}
		catch (IndexOutOfBoundsException e)
		{
			throw new UnboundIdentifierException("Not enough integers supplied");
		}

		return root.eval(env);
	}

	/**
	 * Gets a list of the IDNodes text values in the given TreeNode
	 * 
	 * @param root
	 * The node to start looking at to get the variables
	 * @return
	 * A list of Strings containing the variables in the given tree
	 */
	private static List<String> getVariables(TreeNode root)
	{
		if (root == null)
		{
			return new ArrayList<String>();
		}

		List<String> variables = new ArrayList<String>();
		if (root.getChildCount() == 0)
		{
			if (root.type() == TokenType.ID && !variables.contains(root.toString()))
			{
				variables.add(root.toString());
			}
		}

		// Recursively get all the variables in this node's children
		variables.addAll(getVariables(root.left()));
		variables.addAll(getVariables(root.right()));
		return variables;
	}

	/**
	 * Creates an expression tree (abstract syntax tree) for the given
	 * arithmetic expression.
	 * @param expr the expression to be parsed
	 * @return root of the expression tree
	 * @throws ParseException if the expression is invalid
	 */
	public static TreeNode createTree(String expr) throws ParseException
	{
		Tokenizer tokenizer = new Tokenizer(expr);
		Deque<Token> operators = new LinkedList<Token>();
		Deque<TreeNode> operands = new LinkedList<TreeNode>();

		boolean operandExpected = true;

		// For each token in the string, we add it to the correct stack
		for (Token token : tokenizer) 
		{
			if (token.isOperand())
			{
				if (!operandExpected) 
				{
					throw new ParseException("Should not have 2 operands in a row: " + token.text());
				}
				try 
				{
					if (token.type() == TokenType.INT)
					{
						operands.push(new IntNode(Integer.parseInt(token.text())));
					}
					else
					{
						operands.push(new IDNode(token.text()));
					}
				}
				catch (NumberFormatException e)
				{
					throw new ParseException("Error parsing number: " + token.text());
				}
				operandExpected = false;
			}
			else if (token.type() == TokenType.LPAREN)
			{
				if (!operandExpected)
				{
					throw new ParseException("Expected operator but received left parenthesis");
				}
				operators.push(token);
				operandExpected = true;
			}
			else
			{
				// If the this is a Minus sign, and we are expecting an operand,
				// push a new Token with type TokenType.NEGATION onto the operators stack
				if (token.type() == TokenType.MINUS && operandExpected)
				{
					operators.push(new Token(TokenType.NEGATION));
					operandExpected = true;
				}
				else 
				{
					int min = token.precedence();
					while (!operators.isEmpty() && ((operators.peek().type() == TokenType.EXP && operators.peek().precedence() > min) || 
							(operators.peek().type() != TokenType.EXP && operators.peek().precedence() >= min)))
					{
						popOperandsAndPushOntoStack(operators, operands);
					}

					if (token.type() == TokenType.RPAREN)
					{
						// Just pop it here and check...if it is a LParen, we took
						// care of the popping. If not, we throw an exception, so
						// it doesn't matter whether it is still on the stack or not
						try {
							if (operators.pop().type() != TokenType.LPAREN)
							{
								throw new ParseException("Right parenthesis without matching Left parenthesis");
							}
							operandExpected = false;
						} catch (NoSuchElementException e) {
							throw new ParseException("Right parenthesis without matching Left parenthesis");
						}
					}
					else
					{
						if (operandExpected)
						{
							throw new ParseException("There should not be 2 operators in a row: " + token.text());
						}
						operators.push(token);
						operandExpected = true;
					}
				}
			}
		}

		if (operators.isEmpty() && operands.isEmpty())
		{
			// This means we had an empty expression
			throw new ParseException("Empty expression");
		}

		// The string is finished parsing, so we now need to finish off creating the tree
		// by handling the rest of the operators.
		while (!operators.isEmpty())
		{
			popOperandsAndPushOntoStack(operators, operands);
		}

		TreeNode ret = operands.pop();

		if (!operands.isEmpty())
		{
			throw new ParseException("Too many operands. Extra operands: " + operands.toString());
		}

		return ret;
	}

	/**
	 * Pops the top operator off the stack and handles pushing a new operand onto the operand
	 * stack.  Extracted to helper method because this operation is done twice in the create tree method.
	 * 
	 * @param operators
	 * A stack of operators
	 * @param operands
	 * A stack of operands
	 * @throws ParseException
	 * If there is a problem popping an operand off the operands stack
	 */
	private static void popOperandsAndPushOntoStack(Deque<Token> operators, Deque<TreeNode> operands) throws ParseException
	{
		Token op = operators.pop();

		if (op.type() == TokenType.NEGATION)
		{
			operands.push(new NegationNode(operands.pop()));
		}
		else
		{
			TreeNode operandRight;
			TreeNode operandLeft;
			try 
			{
				operandRight = operands.pop();
				operandLeft = operands.pop();
			}
			catch (NoSuchElementException e) 
			{
				throw new ParseException("Too few operands");
			}
			switch (op.type())
			{
				case DIV: operands.push(new DivNode(operandLeft, operandRight)); break;
				case EXP: operands.push(new ExpNode(operandLeft, operandRight)); break;
				case MINUS: operands.push(new MinusNode(operandLeft, operandRight)); break;
				case PLUS: operands.push(new PlusNode(operandLeft, operandRight)); break;
				case TIMES: operands.push(new TimesNode(operandLeft, operandRight)); break;
			}
		}
	}
}