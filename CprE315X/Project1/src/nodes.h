/** 
 * Brian Reber (breber@iastate.edu)
 * CprE 315X, Spring 2012
 * Node/Edge File Parsing Header
 */
#ifndef NODES_H
#define NODES_H

#include <vector>

// A struct representing an edge to another node
typedef struct edge {
	double weight;
	int toNode;
	int fromNode;
} edge;

// A struct representing a node, its current distance, 
// and the outgoing edges it has
typedef struct node {
	int number;
	int distFrom;
	double dist;
	std::vector<edge *> * edges;
} node;

// A struct to hold all of the nodes
typedef struct nodelist {
	int numNodes;
	int numEdges;
	std::vector<node *> * nodes;
} nodelist;

// Read the nodes from cin and fill the nodelist with
// the data from cin
void readNodes(nodelist & nlist, bool needDoubleCount);

#endif
