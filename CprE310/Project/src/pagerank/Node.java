package pagerank;

import java.util.ArrayList;

/**
 * A Node in our PageRank graph.
 * 
 * @author Brian Reber
 */
public class Node {
	/**
	 * A list of all the Nodes this node has outgoing links to
	 */
	private ArrayList<String> outgoingLinks;

	/**
	 * A list of all the Nodes that link to this node
	 */
	private ArrayList<String> incomingLinks;

	/**
	 * A list of this Node's rank after each iteration
	 */
	private ArrayList<Double> rank;

	/**
	 * Creates a new Node with the default rank given
	 * 
	 * @param startRank The default rank (starting rank) of this Node
	 */
	public Node(double startRank) {
		rank = new ArrayList<Double>();
		rank.add(startRank);

		outgoingLinks = new ArrayList<String>();
		incomingLinks = new ArrayList<String>();
	}

	/**
	 * Adds a Node to our list of outgoing nodes
	 * 
	 * @param s The name of the Node we are connecting to
	 */
	public void addOutgoingLink(String s) {
		outgoingLinks.add(s);
	}

	/**
	 * Get the number of outgoing links from this node
	 * 
	 * @return the number of outgoing links from this node
	 */
	public int getOutgoingLinkCount() {
		return outgoingLinks.size();
	}

	/**
	 * Add an incoming link to our list
	 * 
	 * @param s The name of the Node that is linking to this node
	 */
	public void addIncomingLink(String s) {
		incomingLinks.add(s);
	}

	/**
	 * Get a list of all incoming links to this node.
	 * 
	 * @return a list of all the names of the nodes which have
	 * links to this node
	 */
	public ArrayList<String> getIncomingLinks() {
		return incomingLinks;
	}

	/**
	 * Get the rank of this node at a specific iteration
	 * 
	 * @param iteration the iteration to get the rank for
	 * @return the rank of this node at the given iteration
	 */
	public double getRank(int iteration) {
		return rank.get(iteration);
	}

	/**
	 * Gets the most recent rank
	 * 
	 * @return the rank at the latest iteration
	 */
	public double getRank() {
		return rank.get(rank.size() - 1);
	}

	/**
	 * Adds a new rank to this node's state
	 * 
	 * @param rank the rank to add
	 */
	public void setRank(double rank) {
		this.rank.add(rank);
	}
}
