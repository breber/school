package pagerank;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * A class that will perform a PageRank calculation
 * on a graph of nodes
 * 
 * @author Brian Reber
 */
public class PageRank {
	/**
	 * Parameters that can be configured based on command line args
	 */
	public static double  P				= .15;
	public static double  STOPDIFF 		= .001;
	public static double  INITIAL_RANK	= 1;
	public static boolean DEBUG 		= false;

	/**
	 * A map of Node names to actual Node objects
	 */
	private static Map<String, Node> theNodes = new HashMap<String, Node>();

	/**
	 * The main application entry point
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length < 2) {
			System.err.println("Please pass in a file name on the command line");
			printHelp();
			return;
		}

		Scanner scan = null;

		// Parse command line args
		if (args.length > 1) {
			for (int i = 0; i < args.length; i++) {
				if ("-p".equals(args[i])) {
					try {
						P = Double.parseDouble(args[++i]);
					} catch (NumberFormatException e) {
						P = .15;
					}
				} else if ("-stop".equals(args[i])) {
					try {
						STOPDIFF = Double.parseDouble(args[++i]);
					} catch (NumberFormatException e) {
						STOPDIFF = .001;
					}
				} else if ("-f".equals(args[i])) {
					try {
						scan = new Scanner(new File(args[++i]));
					} catch (FileNotFoundException e) {
						e.printStackTrace();
						return;
					}
				} else if ("-i".equals(args[i])) {
					try {
						INITIAL_RANK = Double.parseDouble(args[++i]);
					} catch (NumberFormatException e) {
						INITIAL_RANK = 1;
					}
				} else if ("-debug".equals(args[i])) {
					DEBUG = true;
				} else if ("-h".equals(args[i])) {
					printHelp();
					return;
				}
			}
		}

		// Start reading the file, one link per line
		while (scan.hasNextLine()) {
			Scanner line = new Scanner(scan.nextLine());
			String from = line.next();
			String to = line.next();


			// Add incoming connections to the node
			if (theNodes.containsKey(to)) {
				// The node already exists, so just add the incoming link
				theNodes.get(to).addIncomingLink(from);
			} else {
				// The node doesn't exist yet, so create it and add the incoming link
				Node node = new Node(INITIAL_RANK);
				node.addIncomingLink(from);
				theNodes.put(to, node);
			}

			// Add the outgoing connections from this node
			if (theNodes.containsKey(from)) {
				theNodes.get(from).addOutgoingLink(to);
			} else {
				Node node = new Node(INITIAL_RANK);
				node.addOutgoingLink(to);
				theNodes.put(from, node);
			}
		}

		// Perform the calculation of PageRank
		calculatePageRank();

		// Print out the results
		for (String n : theNodes.keySet()) {
			System.out.println(n + "\t" + theNodes.get(n).getRank());
		}
	}

	/**
	 * Calculate the page rank of all the nodes in our theNodes Map
	 */
	private static void calculatePageRank() {
		int iteration = 0;
		Set<String> keySet = theNodes.keySet();

		double prevDiff = Double.MAX_VALUE;

		// We want to continue processing as long as the maximum difference
		// of the ranks is greater than our STOPDIFF
		while (prevDiff > STOPDIFF) {
			prevDiff = 0;

			// Iterate through each node in our node list
			for (String n : keySet) {
				Node node = theNodes.get(n);
				double newRank = 0;
				double summed = 0;

				// Get all incoming connection to the current node, and sum their ranks / link count
				for (String s : node.getIncomingLinks()) {
					summed += (theNodes.get(s).getRank(iteration) / theNodes.get(s).getOutgoingLinkCount());
				}

				// Compute new rank based on the sum of all previous ranks of incoming nodes
				newRank = (1 - P) + summed * P;


				// Find the greatest difference between old rank and new rank
				if (prevDiff < Math.abs(node.getRank() - newRank)) {
					prevDiff = Math.abs(node.getRank() - newRank);
				}

				// Actually update the rank
				node.setRank(newRank);
			}

			// Print out ranks at end of each iteration if user
			// enables the -debug flag on the command line
			if (DEBUG) {
				System.out.println("Iteration " + iteration);
				for (String n : keySet) {
					System.out.println(n + "\t" + theNodes.get(n).getRank());
				}
				System.out.println();
			}

			iteration++;
		}
	}

	/**
	 * Usage printout
	 */
	private static void printHelp() {
		System.err.println("java -jar PageRank.jar -f [FILENAME]");
		System.err.println();
		System.err.println("Optional Arguments\n");
		System.err.println("\t-P [P Value]");
		System.err.println("\t\tSet the value for P in the formula");

		System.err.println("\t-i [Initial Rank Value]");
		System.err.println("\t\tSet the initial rank value for each node in the graph");

		System.err.println();
		System.err.println("\t-stop [Stop Value]");
		System.err.println("\t\tSet the value that defines smallest change allowed between iterations");

		System.err.println();
		System.err.println("\t-debug");
		System.err.println("\t\tEnable printing of ranks of all nodes at the end of each iteration");

		System.err.println();
		System.err.println("\t-help");
		System.err.println("\t\tPrint this help message");
	}
}
