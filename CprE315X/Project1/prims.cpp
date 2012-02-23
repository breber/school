/** 
 * Brian Reber (breber@iastate.edu)
 * CprE 315X, Spring 2012
 * Prim's Algorithm Implementation
 */
#include <iostream>
#include <fstream>
#include <list>
#include "nodes.h"

using namespace std;

void doPrims(nodelist &);

double diffclock(clock_t clock1, clock_t clock2) {
	double diffticks = clock1 - clock2;
	double diffms = (diffticks * 1000) / CLOCKS_PER_SEC;
	return diffms / 1000;
}

int main(int argc, char ** argv) {
	nodelist nodeList;

	// Read the nodes from cin into our nodeList struct
	readNodes(nodeList, true);

	clock_t start = clock();
	// Perform Prim's Algorithm
	doPrims(nodeList);

	clock_t end = clock();
	cout << "Algorithm Runtime: " << double(diffclock(end, start)) << endl;
}

/**
 * Compare two nodes for sorting a list
 */
bool compareNodes(node * one, node * two) {
	return (one->dist < two->dist);
}

/**
 * Perform the Prims Algorithm on this list of nodes
 */
void doPrims(nodelist & nodeList) {
	// Initialize an empty "priority queue"
	// Actually using a list that we can sort because we will be updating distances
	// and the built-in C++ priority_queue datastructure doesn't automatically readjust
	list<node *> pQueue = list<node *>(nodeList.nodes->begin(), nodeList.nodes->end());
	vector<node *> explored = vector<node *>(nodeList.numNodes);

	// Initialize our explored array
	for (int i = 0; i < nodeList.numNodes; i++) {
		explored[i] = NULL;
	}

	// Set the first node's distance to 0
	node * cur = pQueue.front();
	cur->dist = 0;

	// Sort our "priority queue"
	pQueue.sort(compareNodes);

	// Prims Algorithm
	while (!pQueue.empty()) {
		bool needToSort = false;

		node * cur = pQueue.front();
		pQueue.pop_front();

		// Mark the node as explored
		explored[cur->number] = cur;

		// For all of the edges incident to the current edge, 
		// update the weight to the other node if it is not 
		// already in the closed set, and it has a weight
		// less than the current weight
		for (int i = 0; i < cur->edges->size(); i++) {
			edge * e = cur->edges->at(i);
			node * tempNode = nodeList.nodes->at(e->toNode);
			
			if (explored[e->toNode] == NULL && e->weight < tempNode->dist) {
				tempNode->dist = e->weight;
				tempNode->distFrom = cur->number;
				needToSort = true;
			}
		}

		if (needToSort) {
			// Resort our "priority queue" because we updated distances
			pQueue.sort(compareNodes);
		}
	}

	// FINISH: Output results
	fstream file("output.txt", fstream::out);
	file << nodeList.numNodes << endl;
	file << (nodeList.numNodes - 1) << endl;

	double totalWeight = 0;
	for (int i = 0; i < nodeList.numNodes; i++) {
		totalWeight += explored[i]->dist;

		if (explored[i]->distFrom != -1) {
			file << (explored[i]->number + 1) << " " << (explored[i]->distFrom + 1) << " " << explored[i]->dist << endl;
		}
	}

	cout << "Total Weight: " << totalWeight << endl;
}
