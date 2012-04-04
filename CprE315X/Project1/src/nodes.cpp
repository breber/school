/** 
 * Brian Reber (breber@iastate.edu)
 * CprE 315X, Spring 2012
 * Node/Edge File Parsing
 */
#include <iostream>
#include <float.h>
#include "nodes.h"

/**
 * Read the node/edge list from cin and fill the nodelist struct
 * with the data corresponding to this set of data
 */
void readNodes(nodelist & nlist, bool needDoubleCount) {
	using namespace std;

	int counter = 0;

	// Get the number of nodes and number of edges
	cin >> nlist.numNodes;
	cin >> nlist.numEdges;

	nlist.nodes = new vector<node *>(nlist.numNodes);

	// Initialize all lists
	for (int i = 0; i < nlist.numNodes; i++) {
		node * n = new node;
		n->number = i;
		n->distFrom = -1;
		n->dist = DBL_MAX;
		n->edges = new vector<edge *>;
		nlist.nodes->at(i) = n;
	}

	while (counter++ < nlist.numEdges) {
		int from_node;
		int to_node;
		double weight;

		cin >> from_node;
		cin >> to_node;
		cin >> weight;

		// Make the nodes 0 indexed instead of 1 indexed
		from_node--;
		to_node--;

		// Add a path from "from" to "to"
		edge * newEdge = new edge;
		newEdge->weight = weight;
		newEdge->toNode = to_node;
		newEdge->fromNode = from_node;

		node * current = nlist.nodes->at(from_node);
		current->edges->push_back(newEdge);

		if (needDoubleCount) {
			// Add a path from "to" to "from"
			newEdge = new edge;
			newEdge->weight = weight;
			newEdge->toNode = from_node;
			newEdge->fromNode = to_node;

			current = nlist.nodes->at(to_node);
			current->edges->push_back(newEdge);
		}
	}
}
