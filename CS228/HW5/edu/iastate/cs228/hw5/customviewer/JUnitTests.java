package edu.iastate.cs228.hw5.customviewer;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.iastate.cs228.hw5.ListGraphImpl;
import edu.iastate.cs228.hw5.api.Graph.Edge;

/**
 * 
 * @author jimcarey
 *
 */
public class JUnitTests {

	Edge<Integer> edge11 = new Edge<Integer>(1, 1);
	Edge<Integer> edge12 = new Edge<Integer>(1, 2);
	Edge<Integer> edge10 = new Edge<Integer>(1, 0);
	ListGraphImpl<Integer> lg = new ListGraphImpl<Integer>();

	@Before
	public void setUp() throws Exception {

	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void tesAdd() {
		Iterator<Integer> iterVertices = lg.vertices();

		while (iterVertices.hasNext()) {
			System.out.println(iterVertices.next());
		}

		System.out.println("_________");
		assertTrue(lg.addVertex(1));
		assertTrue(lg.addVertex(2));
		iterVertices = lg.vertices();

		while (iterVertices.hasNext()) {
			System.out.println(iterVertices.next());
		}
		System.out.println("_________");
		assertFalse(lg.addVertex(2));
		iterVertices = lg.vertices();

		while (iterVertices.hasNext()) {
			System.out.println(iterVertices.next());
		}

		assertTrue((lg.addEdge(1, 2, 3)));
		assertFalse(lg.addEdge(1, 2, 3));
		assertTrue(lg.addEdge(1, 2, 4));

		Iterator<Edge<Integer>> iterEdge = lg.getNeighbors(1);
		while (iterEdge.hasNext()) {
			Edge<Integer> edge = iterEdge.next();
			System.out.println("Edge from 1 to " + edge.vertex
					+ " with wieght of " + edge.weight);
		}
		System.out.println("_________");

		iterEdge = lg.getNeighbors(2);
		while (iterEdge.hasNext()) {
			Edge<Integer> edge = iterEdge.next();
			System.out.println("Edge from 2 to " + edge.vertex
					+ " with wieght of " + edge.weight);
		}
		System.out.println("_________");

		lg.addVertex(3);
		lg.addEdge(1, 2, 9);
		lg.addEdge(1, 3, 10);
		iterEdge = lg.getNeighbors(1);
		while (iterEdge.hasNext()) {
			Edge<Integer> edge = iterEdge.next();
			System.out.println("Edge from 1 to " + edge.vertex
					+ " with wieght of " + edge.weight);
		}
		System.out.println("_________");

		lg.addEdge(2, 3, 5);
		lg.addEdge(2, 1, 0);

		iterEdge = lg.getNeighbors(2);
		while (iterEdge.hasNext()) {
			Edge<Integer> edge = iterEdge.next();
			System.out.println("Edge from 2 to " + edge.vertex
					+ " with wieght of " + edge.weight);
		}
		System.out.println("_________");
		System.out.println(lg.h(2, 3));

		
		try
	    {
			lg.addEdge(3, 9, 8);
	    	fail("This should throw a NoSuchElementException");
	    } catch (NoSuchElementException e)
	    {
	    	assertTrue(true);
		
	    }

	}
}