package edu.iastate.cs228.hw5.customviewer;

import java.util.Iterator;

import edu.iastate.cs228.hw5.ListGraphImpl;
import edu.iastate.cs228.hw5.PathFinderFactoryImpl;
import edu.iastate.cs228.hw5.api.GraphAnimation;
import edu.iastate.cs228.hw5.api.ListGraph;
import edu.iastate.cs228.hw5.api.PathFinderFactory;

/**
 * Runs Dijkstra's algorithm on a simple graph.
 */
public class SimpleTest
{
	public static void main(String[] args)
	{
		ListGraph<String> g = new ListGraphImpl<String>();
		String[] v = {"S", "A", "B", "C", "D"};
		for (String vertex : v)
		{
			g.addVertex(vertex);
		}
		g.addEdge(v[0], v[1], 2);
		g.addEdge(v[0], v[3], 5);
		g.addEdge(v[1], v[2], 6);
		g.addEdge(v[1], v[3], 4);
		g.addEdge(v[2], v[4], 2);
		g.addEdge(v[3], v[2], 2);
		g.addEdge(v[3], v[4], 5);

		PathFinderFactory factory = new PathFinderFactoryImpl();
		GraphAnimation<String> animator = factory.createDijkstraFinder(g, v[0], null);

		int count = 0;
		printState(animator, v, count);

		while (!animator.done())
		{
			animator.step();
			printState(animator, v, ++count);
		}
	}

	private static void printState(GraphAnimation<String> animator, String[] v, int count)
	{
		System.out.println("Step " + count);
		System.out.println("--------------------------------------------------");
		System.out.println("Closed set");
		System.out.println("----------");
		System.out.println(animator.closedSet().toString());
		System.out.println();

		System.out.println("pred map:");
		System.out.println("---------");
		for (String vertex : v)
		{
			String pred = animator.getPredecessor(vertex);
			if (pred != null)
			{
				System.out.println(vertex + " | " + pred);
			}
		}
		System.out.println();

		System.out.println("dist map");
		System.out.println("---------");
		for (String vertex : v)
		{
			Integer dist = animator.getDistance(vertex);
			if (dist >= 0)
			{
				System.out.println(vertex + " | " + dist);
			}
		}
		System.out.println();

		System.out.println("Priority queue");
		System.out.println("--------------");
		Iterator<String> iter = animator.openSet();
		while (iter.hasNext())
		{
			System.out.println(iter.next());
		}
		System.out.println();

	}
}