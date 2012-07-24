package edu.iastate.cs228.hw4.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author brianreber
 *
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
	ExpressionTest.class,
	InfixPostfixReduceConstants.class,
	EvaluateTests.class,
	ExpressionTreesTest.class,
	ExpressionTreesTest2.class
})

public class AllTests { }