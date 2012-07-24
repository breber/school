package edu.iastate.cs228.hw4.test;

import edu.iastate.cs228.hw4.ExpressionTrees;
import edu.iastate.cs228.hw4.ParseException;
import edu.iastate.cs228.hw4.UnboundIdentifierException;
import edu.iastate.cs228.hw4.nodes.TreeNode;
import edu.iastate.cs228.hw4.viewer.TreeViewer;

/**
 * @author brianreber
 *
 */
public class MainMethod {

	public static void main(String[] args) throws UnboundIdentifierException {
		TreeNode tn = null;
		try {
			tn = ExpressionTrees.createTree("(4 + 3) ^ 2");
//			System.out.println(ExpressionTrees.getInfixString(tn));
			TreeViewer.start(tn);
//			IntNode two = new IntNode(2);
//			IntNode three = new IntNode(3);
//			IntNode four = new IntNode(4);
//			MinusNode minus = new MinusNode(two, three);
//			MinusNode root = new MinusNode(minus, four);
//			System.out.println(ExpressionTrees.getInfixString(root));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
}
