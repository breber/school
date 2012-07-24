package edu.iastate.cs228.hw3.test;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(AllTests.class.getName());
		//$JUnit-BEGIN$
		suite.addTestSuite(PriorityListTest.class);
		suite.addTestSuite(PriorityListTestOther.class);
		suite.addTestSuite(PriorityListTest3.class);
		//$JUnit-END$
		return suite;
	}
}
